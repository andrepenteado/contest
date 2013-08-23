
package br.com.alphadev.contest.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import javax.ejb.Stateless;

import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import br.com.alphadev.contest.KGlobal;
import br.com.alphadev.contest.KGlobal.ComboPrecoComissao;
import br.com.alphadev.contest.entity.AliquotaImposto;
import br.com.alphadev.contest.entity.CFOP;
import br.com.alphadev.contest.entity.Caixa;
import br.com.alphadev.contest.entity.Comissao;
import br.com.alphadev.contest.entity.FaixaPreco;
import br.com.alphadev.contest.entity.Funcionario;
import br.com.alphadev.contest.entity.ImpostoNotaFiscal;
import br.com.alphadev.contest.entity.ItemCompra;
import br.com.alphadev.contest.entity.ItemVenda;
import br.com.alphadev.contest.entity.NFe;
import br.com.alphadev.contest.entity.NotaFiscal;
import br.com.alphadev.contest.entity.Orcamento;
import br.com.alphadev.contest.entity.Produto;
import br.com.alphadev.contest.entity.Receber;
import br.com.alphadev.contest.entity.Recebido;
import br.com.alphadev.contest.entity.Venda;
import br.com.alphadev.contest.entity.valueObject.FormaPagamento;
import br.com.alphadev.contest.entity.valueObject.Imposto;
import br.com.alphadev.contest.entity.valueObject.TipoCompraVenda;
import br.com.alphadev.contest.entity.valueObject.TipoMovimentacao;
import br.com.alphadev.contest.entity.valueObject.TipoProduto;
import br.com.alphadev.contest.entity.valueObject.VistaPrazo;
import br.com.alphadev.core.BasicService;
import br.com.alphadev.core.ServicesFactory;
import br.com.alphadev.exception.ServiceValidationException;
import br.com.alphadev.util.ConfigHelper;
import br.com.alphadev.util.FunctionsHelper;

/**
 * @author Andre Penteado
 * @since 29/08/2007 - 21:51:26
 */
@Stateless
public class VendaService extends BasicService {

    private EstoqueService estoqueService;
    private NfeService nfeService;
    private CompraService compraService;

    @Override
    public void setParameters(Object[] params) {
        super.setParameters(params);
        this.estoqueService = (EstoqueService)ServicesFactory.getInstance(EstoqueService.class, params);
        this.nfeService = (NfeService)ServicesFactory.getInstance(NfeService.class, params);
        this.compraService = (CompraService)ServicesFactory.getInstance(CompraService.class, params);
    }

    public String gravarNovaVendaCaixa(Venda venda, Caixa caixa, FormaPagamento formaPagamento) throws ServiceValidationException {
        if (venda.getRecebimentos() == null)
            venda.setRecebimentos(new ArrayList<Receber>());

        venda.setEmpresa(userLogin.getFuncionario().getEmpresa());
        venda.setDataLancamento(new Date());
        venda.setFormaPagamento(formaPagamento);

        Receber receber = new Receber();
        receber.setRecebimentos(new ArrayList<Recebido>());
        receber.setId(-1l);
        receber.setParcela(0);
        receber.setVencimento(venda.getDataLancamento());
        receber.setValor(venda.getValorTotal());
        receber.setVenda(venda);
        venda.getRecebimentos().add(receber);

        beginTransaction();

        // Atualizar estoque
        estoqueService.atualizarMovimentacaoEstoque(venda.getItens().iterator(), VistaPrazo.VISTA, TipoMovimentacao.EMISSAO, null);

        // Gravar venda
        Venda vendaAtualizada = (Venda)save(venda);

        // Gravar orçamento
        Orcamento orcamento = new Orcamento();
        orcamento.setVenda(vendaAtualizada);
        orcamento.setEmissao(new Date());
        orcamento.setId(vendaAtualizada.getId());
        save(orcamento);

        // Grava recebimento
        Recebido recebido = new Recebido();
        recebido.setId(-1l);
        recebido.setDataPagamento(vendaAtualizada.getDataLancamento());
        recebido.setValorPago(vendaAtualizada.getValorTotal());
        recebido.setCaixa(caixa);
        recebido.setFormaPagamento(formaPagamento);
        recebido.setReceber((Receber)vendaAtualizada.getRecebimentos().toArray()[0]);
        save(recebido);

        String cupom = gerarCupomVenda(vendaAtualizada, formaPagamento);

        commit();

        log.info("Gravar nova venda caixa: ".concat(caixa.toString()).concat(" - Venda: ").concat(venda.toString()));
        return cupom;
    }

    public void gravarPedidoVenda(Venda venda) throws ServiceValidationException {
        if (venda.getDataLancamento() == null)
            throw new ServiceValidationException(ConfigHelper.getProperty("error.required", "Data da Venda"));
        if (venda.getItens() == null || venda.getItens().size() <= 0)
            throw new ServiceValidationException(ConfigHelper.getProperty("error.isEmpty", "Itens da Venda"));
        if (venda.isConsolidada())
            throw new ServiceValidationException(ConfigHelper.get().getString("error.vendaConsolidada"));
        if (venda.getComissao() != null && venda.getComissao().getDataPagamento() != null)
            throw new ServiceValidationException(ConfigHelper.get().getString("error.vendaComissaoPaga"));
        if (venda.getTipo().equals(TipoMovimentacao.EMISSAO) && venda.getPrazoPagamento() == null)
            throw new ServiceValidationException(ConfigHelper.getProperty("error.required", "Prazo de Pagamento"));

        if (venda.getId() == null)
            venda.setId(-1l);
        else if (venda.getId().intValue() > 0) {
            venda.getRecebimentos().clear();
        }

        venda.setEmpresa(userLogin.getFuncionario().getEmpresa());
        if (userLogin.getCategoria().equals(KGlobal.CATEGORIA_VENDEDOR))
            venda.setFuncionario(userLogin.getFuncionario());

        try {
            beginTransaction();

            Venda vendaAtualizada = (Venda)save(venda);

            if (venda.getEmissao() != null)
                atualizarRecebimentos(vendaAtualizada);

            commit();

            log.info("Gravar venda: ".concat(venda.toString()));
        }
        catch (Exception ex) {
            rollback();
            log.error(ConfigHelper.get().getString("error.general"), ex);
            throw new ServiceValidationException(ConfigHelper.get().getString("error.general"));
        }
    }

    public static double aplicarJuroDesconto(Venda venda, double valor) {
        double porcentagem = 0d;
        double valorAtualizado = valor;

        if (venda.getDesconto() != null && venda.getDesconto() > 0d)
            porcentagem -= venda.getDesconto();
        if (venda.getPrazoPagamento() != null && venda.getPrazoPagamento().getDescontoJuros() != null)
            porcentagem += venda.getPrazoPagamento().getDescontoJuros();

        if (porcentagem > 0)
            valorAtualizado += (valor * (porcentagem / 100d));
        else if (porcentagem < 0)
            valorAtualizado -= (valor * (Math.abs(porcentagem) / 100d));

        return valorAtualizado;
    }

    private void atualizarValorComissao(Venda venda) {
        TipoCompraVenda tipo = venda.getTipo();
        if (tipo.equals(TipoCompraVenda.EMISSAO) || tipo.equals(TipoCompraVenda.ENTREGA_FUTURA) && venda.getPrazoPagamento() != null) {
            // Calcula valor da comissão da venda
            double valorComissao = 0d;
            if (venda.getFuncionario() != null && venda.getFuncionario().getId() != null) {
                for (ItemVenda item : venda.getItens()) {
                    if (item.getComissao() != null && item.getComissao() > 0d) {
                        double valorItemComissao = (item.getSubTotal() * (item.getComissao() / 100));
                        valorItemComissao = aplicarJuroDesconto(venda, valorItemComissao);
                        valorComissao += valorItemComissao;
                    }
                }
            }

            // Caso haja, gravar valor da comissão
            Comissao comissao = venda.getComissao();
            if (valorComissao > 0d) {
                if (comissao == null)
                    comissao = new Comissao();
                comissao.setVenda(venda);
                comissao.setValor(valorComissao);
                save(comissao);
            }
            else {
                if (comissao != null)
                    delete(comissao);
            }
        }
    }

    private void atualizarRecebimentos(Venda venda) {
        if (venda.getId().intValue() > 0)
            venda.getRecebimentos().clear();

        // Se for emissão ou entrega futura e prazo de pagamento com parcelas, atualiza recebimentos
        TipoCompraVenda tipo = venda.getTipo();
        if (tipo.equals(TipoCompraVenda.EMISSAO) || tipo.equals(TipoCompraVenda.ENTREGA_FUTURA) && venda.getPrazoPagamento() != null) {
            // Tem vencimento(s)
            if (venda.getPrazoPagamento().getParcelas() != null && !venda.getPrazoPagamento().getParcelas().isEmpty()) {
                double total = venda.getValorTotal();
                double soma = 0;
                ArrayList<Integer> dias = new ArrayList<Integer>();
                StringTokenizer stk = new StringTokenizer(venda.getPrazoPagamento().getParcelas(), ",");

                // Verificar se já foi emitida e definir a data de emissão
                GregorianCalendar emissao = new GregorianCalendar();
                if (venda.getEmissao() != null)
                    emissao.setTime(venda.getEmissao());

                // Separa as parcelas
                while (stk.hasMoreElements())
                    dias.add(Integer.parseInt((String)stk.nextElement()));

                // Soma no vencimento e divide o valor da parcela
                for (int i = 0; i < dias.size(); i++) {
                    GregorianCalendar vencimento = new GregorianCalendar();
                    vencimento.setTimeInMillis(emissao.getTimeInMillis());
                    vencimento.add(Calendar.DAY_OF_MONTH, dias.get(i));

                    BigDecimal parcela = new BigDecimal(total / dias.size()).setScale(2, BigDecimal.ROUND_HALF_UP);
                    double totalParcela = parcela.doubleValue();

                    if (i == dias.size() - 1)
                        totalParcela = total - soma;

                    Receber receber = new Receber();
                    receber.setRecebimentos(new ArrayList<Recebido>());
                    receber.setParcela(i + 1);
                    receber.setVencimento(vencimento.getTime());
                    receber.setValor(totalParcela);
                    receber.setVenda(venda);
                    Receber receberAtualizado = (Receber)save(receber);

                    // Baixa automaticamente a parcela
                    /*Recebido recebido = new Recebido();
                    recebido.setReceber(receberAtualizado);
                    recebido.setDataPagamento(vencimento.getTime());
                    recebido.setFormaPagamento(venda.getFormaPagamento());
                    recebido.setValorPago(totalParcela);
                    Recebido recebidoAtualizado = (Recebido)save(recebido);

                    receber.getRecebimentos().add(recebidoAtualizado);*/
                    venda.getRecebimentos().add(receberAtualizado);
                    soma += totalParcela;
                }
            }
            // Sem vencimento (fiado)
            else {
                Receber receber = new Receber();
                receber.setRecebimentos(new ArrayList<Recebido>());
                receber.setParcela(1);
                receber.setVencimento(null);
                receber.setValor(venda.getValorTotal());
                receber.setVenda(venda);
                Receber receberAtualizado = (Receber)save(receber);
                venda.getRecebimentos().add(receberAtualizado);
            }
        }

        save(venda);
    }

    /**
     * @param venda
     * @param item
     * @param substituicaoTributariaIcms
     */
    private void tributarImpostosItem(ItemVenda item) {
        if (item.getImpostos() != null)
            item.getImpostos().clear();

        if (TipoProduto.SERVICO.name().equals(item.getProduto().getTipo().name())) {
            ImpostoNotaFiscal imposto = new ImpostoNotaFiscal();
            imposto.setItemVenda(item);
            imposto.setImposto(item.getVenda().getEmpresa().getImpostoServico().getImposto());
            imposto.setAliquota(item.getVenda().getEmpresa().getImpostoServico().getAliquota());
            ImpostoNotaFiscal impostoAtualizado = (ImpostoNotaFiscal)save(imposto);
            item.getImpostos().add(impostoAtualizado);
        }
        else {
            ItemCompra ultimoItem = compraService.buscarUltimoItemCompraPrazo(item.getProduto());
            boolean substituicaoTributariaIcms = false;
            if (ultimoItem != null)
                substituicaoTributariaIcms = ultimoItem.isSubstituicaoTributariaIcms();

            for (AliquotaImposto aliquotaImposto : item.getVenda().getEmpresa().getImpostos()) {
                if (substituicaoTributariaIcms && aliquotaImposto.getImposto().name().equals(Imposto.ICMS.name()))
                    continue;
                if (aliquotaImposto.getImposto().name().equals(Imposto.ISS.name()))
                    continue;
                if (item.getVenda().getTipo().equals(TipoCompraVenda.ENTREGA_FUTURA)
                                && aliquotaImposto.getImposto().name().equals(Imposto.ICMS.name()))
                    continue;
                if (item.getVenda().getTipo().equals(TipoCompraVenda.REMESSA_ENTREGA_FUTURA))
                    continue;
                ImpostoNotaFiscal imposto = new ImpostoNotaFiscal();
                imposto.setItemVenda(item);
                imposto.setImposto(aliquotaImposto.getImposto());
                imposto.setAliquota(aliquotaImposto.getAliquota());
                ImpostoNotaFiscal impostoAtualizado = (ImpostoNotaFiscal)save(imposto);
                item.getImpostos().add(impostoAtualizado);
            }
            if (substituicaoTributariaIcms) {
                ImpostoNotaFiscal imposto = new ImpostoNotaFiscal();
                imposto.setItemVenda(item);
                imposto.setImposto(Imposto.SUBSTITUICAO_TRIBUTARIA_ICMS);
                ImpostoNotaFiscal impostoAtualizado = (ImpostoNotaFiscal)save(imposto);
                item.getImpostos().add(impostoAtualizado);
            }
        }
    }

    private CFOP buscarCFOPItemVenda(ItemVenda item) {
        log.debug("Buscar CFOP do item " + item.getProduto().getReferencia());

        boolean mesmoEstado = item.getVenda().getEmpresa().getCidade().getEstado().getSigla()
                        .equals(item.getVenda().getCliente().getCidade().getEstado().getSigla());
        TipoCompraVenda operacao = item.getVenda().getTipo();

        if (TipoProduto.SERVICO.equals(item.getProduto().getTipo()) & mesmoEstado)
            return (CFOP)findByField(CFOP.class, "codigo", "5933");
        else if (TipoProduto.SERVICO.equals(item.getProduto().getTipo()) & !mesmoEstado)
            return (CFOP)findByField(CFOP.class, "codigo", "6933");
        else if (operacao.equals(TipoCompraVenda.EMISSAO) && item.isSubstituicaoTributariaIcms())
            return (CFOP)findByField(CFOP.class, "codigo", "5405");
        else if (operacao.equals(TipoCompraVenda.EMISSAO) && mesmoEstado)
            return (CFOP)findByField(CFOP.class, "codigo", "5102");
        else if (operacao.equals(TipoCompraVenda.EMISSAO) && !mesmoEstado)
            return (CFOP)findByField(CFOP.class, "codigo", "6102");
        else if (operacao.equals(TipoCompraVenda.DEVOLUCAO) && mesmoEstado)
            return (CFOP)findByField(CFOP.class, "codigo", "5202");
        else if (operacao.equals(TipoCompraVenda.DEVOLUCAO) && !mesmoEstado)
            return (CFOP)findByField(CFOP.class, "codigo", "6202");
        else if (operacao.equals(TipoCompraVenda.BONIFICACAO) && mesmoEstado)
            return (CFOP)findByField(CFOP.class, "codigo", "5910");
        else if (operacao.equals(TipoCompraVenda.BONIFICACAO) && !mesmoEstado)
            return (CFOP)findByField(CFOP.class, "codigo", "6910");
        else if (operacao.equals(TipoCompraVenda.ENTREGA_FUTURA) && mesmoEstado)
            return (CFOP)findByField(CFOP.class, "codigo", "5922");
        else if (operacao.equals(TipoCompraVenda.ENTREGA_FUTURA) && !mesmoEstado)
            return (CFOP)findByField(CFOP.class, "codigo", "6922");
        else if (operacao.equals(TipoCompraVenda.REMESSA_ENTREGA_FUTURA) && mesmoEstado)
            return (CFOP)findByField(CFOP.class, "codigo", "5117");
        else if (operacao.equals(TipoCompraVenda.REMESSA_ENTREGA_FUTURA) && !mesmoEstado)
            return (CFOP)findByField(CFOP.class, "codigo", "6117");

        return null;
    }

    public List<Venda> pesquisarVendasNaoEmitidas() {
        log.info("Pesquisar vendas não emitidas");

        StringBuilder hql = new StringBuilder();
        Object[][] params = null;

        hql.append("SELECT v FROM br.com.alphadev.contest.entity.Venda v ");
        hql.append("WHERE NOT EXISTS ( ");
        hql.append("   SELECT nf FROM br.com.alphadev.contest.entity.NotaFiscal nf ");
        hql.append("   WHERE nf.venda.id = v.id ");
        hql.append(") ");
        hql.append("AND NOT EXISTS ( ");
        hql.append("   SELECT o FROM br.com.alphadev.contest.entity.Orcamento o ");
        hql.append("   WHERE o.venda.id = v.id ");
        hql.append(") ");

        // Categoria vendedor, filtrar por vendedor
        if (userLogin.getCategoria().equals(KGlobal.CATEGORIA_VENDEDOR)) {
            hql.append("AND v.funcionario.id = :pIdFuncionario ");
            params = new Object[][] { { "pIdFuncionario", userLogin.getFuncionario().getId() } };
        }

        hql.append("ORDER BY v.dataLancamento DESC");

        return (List<Venda>)executeQuery(hql.toString(), params);
    }

    public void excluirPedidoVenda(Venda venda) throws ServiceValidationException {
        if (venda.getOrcamento() != null || venda.getNotaFiscal() != null)
            throw new ServiceValidationException(ConfigHelper.get().getString("error.vendaEmitida"));

        beginTransaction();

        // Excluir os movimentos de estoque, caso exista
        StringBuilder hql = new StringBuilder();
        hql.append("DELETE FROM br.com.alphadev.contest.entity.MovimentoEstoque me ");
        hql.append("WHERE me.itemVenda.id IN ( ");
        hql.append("   SELECT iv.id FROM br.com.alphadev.contest.entity.ItemVenda iv ");
        hql.append("   WHERE iv.venda.id = :pIdVenda ");
        hql.append(")");
        executeUpdate(hql.toString(), new Object[][] { { "pIdVenda", venda.getId() } });

        delete(venda);
        commit();
    }

    public List<ItemVenda> pesquisarItensVendidosPorCliente(long idCliente) {
        if (idCliente <= 0)
            return null;

        log.info("Pesquisar itens vendidos para cliente ID: ".concat(FunctionsHelper.toStringNotNull(idCliente)));

        StringBuilder hql = new StringBuilder();
        hql.append("SELECT item ");
        hql.append("FROM br.com.alphadev.contest.entity.ItemVenda item ");
        hql.append("WHERE item.venda.cliente.id = :pIdCliente ");
        hql.append("ORDER BY item.venda.dataLancamento DESC ");

        Object[][] params = new Object[][] { { "pIdCliente", idCliente } };
        return (List<ItemVenda>)executeQuery(hql.toString(), params);
    }

    public long buscarProximoNumeroNotaFiscal() {
        StringBuilder hql = new StringBuilder();
        hql.append("SELECT MAX(nf.numero) + 1 FROM br.com.alphadev.contest.entity.NotaFiscal nf ");
        hql.append("WHERE nf.venda.empresa.id = ").append(userLogin.getFuncionario().getEmpresa().getId());
        if (userLogin.getFuncionario().getEmpresa().getConfiguracao().getSituacaoNfe().intValue() > 0)
            hql.append("AND nf.id IN (SELECT nfe.notaFiscal.id FROM br.com.alphadev.contest.entity.NFe nfe)");

        List<Long> result = ((List<Long>)executeQuery(hql.toString(), null));
        if (result == null || result.get(0) == null || result.get(0) <= 0)
            return 1;
        log.info("Buscar próximo número de nota fiscal: ".concat(Long.toString(result.get(0))));
        return result.get(0);
    }

    public String verificarAvisosVenda(Venda venda) {
        StringBuilder avisos = new StringBuilder();

        if (venda.getCliente() == null || venda.getCliente().getId() == null)
            avisos.append(ConfigHelper.getProperty("error.notFound", "Cliente")).append(". ");
        if (venda.getItens() == null || venda.getItens().isEmpty())
            avisos.append(ConfigHelper.getProperty("error.notFound", "Itens")).append(". ");
        if (venda.getPrazoPagamento() == null || venda.getPrazoPagamento().getId() == null)
            avisos.append(ConfigHelper.getProperty("error.notFound", "Prazo de pagamento"));

        if (avisos.length() > 0)
            return avisos.toString();

        StringBuilder hql1 = new StringBuilder();
        hql1.append("SELECT COUNT(v.id) FROM br.com.alphadev.contest.entity.Venda v ");
        hql1.append("WHERE v.cliente.id = :pIdCliente ");
        hql1.append("AND ( ");
        hql1.append("   v.id IN (SELECT nf.venda.id FROM br.com.alphadev.contest.entity.NotaFiscal nf) ");
        hql1.append("   OR v.id IN (SELECT o.venda.id FROM br.com.alphadev.contest.entity.Orcamento o) ");
        hql1.append(") ");
        Object[][] params1 = new Object[][] { { "pIdCliente", venda.getCliente().getId() } };
        Long qtdVendas = (Long)executeFind(hql1.toString(), params1);
        if (qtdVendas.intValue() <= 0)
            avisos.append("1a COMPRA DO CLIENTE. ");

        StringBuilder hql = new StringBuilder();
        hql.append("SELECT v FROM br.com.alphadev.contest.entity.Venda v ");
        hql.append("WHERE v.cliente.id = :pIdCliente ");
        hql.append("ORDER BY v.dataLancamento DESC ");
        Object[][] params = new Object[][] { { "pIdCliente", venda.getCliente().getId() } };
        Venda ultimaVenda = (Venda)executeFind(hql.toString(), params);

        if (venda.getCliente().getObservacao() != null && !venda.getCliente().getObservacao().isEmpty())
            avisos.append("Obs.cliente: ").append(venda.getCliente().getObservacao()).append(". ");

        if (venda.getObservacao() != null && !venda.getObservacao().isEmpty())
            avisos.append("Obs.vendedor: ").append(venda.getObservacao()).append(". ");

        if (ultimaVenda != null && venda.getPrazoPagamento().getId().longValue() != ultimaVenda.getPrazoPagamento().getId().longValue())
            avisos.append(ConfigHelper.getProperty("error.notEquals", new String[] {
                            "Último prazo de pagamento " + ultimaVenda.getPrazoPagamento().getDescricao(),
                            "o atual " + venda.getPrazoPagamento().getDescricao() })).append(". ");

        for (ItemVenda item : venda.getItens())
            avisos.append(verificarAvisosItemVenda(item));

        return avisos.toString();
    }

    private String verificarAvisosItemVenda(ItemVenda itemVenda) {
        if (itemVenda.getValorVenda() == null || itemVenda.getValorVenda().doubleValue() <= 0d)
            return ConfigHelper.getProperty("error.notFound", "Preço do item " + itemVenda.getProduto().getReferencia());

        StringBuilder hql = new StringBuilder();
        hql.append("SELECT iv FROM br.com.alphadev.contest.entity.ItemVenda iv ");
        hql.append("WHERE iv.produto.id = :pIdProduto ");
        hql.append("AND iv.venda.cliente.id = :pIdCliente ");
        hql.append("ORDER BY iv.venda.dataLancamento DESC ");

        Object[][] params = new Object[][] { { "pIdProduto", itemVenda.getProduto().getId() },
                        { "pIdCliente", itemVenda.getVenda().getCliente().getId() } };
        ItemVenda ultimoItem = (ItemVenda)executeFind(hql.toString(), params);

        StringBuilder avisos = new StringBuilder();
        if (ultimoItem != null) {
            if (itemVenda.getValorVenda().doubleValue() != ultimoItem.getValorVenda().doubleValue()) {
                avisos.append("Preço do item ").append(itemVenda.getProduto().getReferencia()).append(" diferente do último pedido feito no dia ");
                avisos.append(FunctionsHelper.dateFormat(ultimoItem.getVenda().getDataLancamento(), "dd/MM/yyyy"));
                avisos.append(". Último: ");
                avisos.append(FunctionsHelper.numberFormat(ultimoItem.getValorVenda(), "#0.00"));
                avisos.append(". Atual: ");
                avisos.append(FunctionsHelper.numberFormat(itemVenda.getValorVenda(), "#0.00")).append(". ");
            }
        }

        return avisos.toString();
    }

    public byte[] emitirNotaFiscalVenda(NotaFiscal notaFiscal) throws ServiceValidationException {
        byte[] result = null;

        if (notaFiscal.getNumero() <= 0)
            throw new ServiceValidationException(ConfigHelper.getProperty("error.invalid", "Número de Nota Fiscal"));
        if (notaFiscal.getVenda().getCliente().getNome() == null || "".equals(notaFiscal.getVenda().getCliente().getNome()))
            throw new ServiceValidationException(ConfigHelper.getProperty("error.required", "Nome do cliente"));
        if (notaFiscal.getVenda().getCliente().getRua() == null || "".equals(notaFiscal.getVenda().getCliente().getRua()))
            throw new ServiceValidationException(ConfigHelper.getProperty("error.required", "Endereço do cliente"));
        if (notaFiscal.getVenda().getCliente().getCidade() == null || "".equals(notaFiscal.getVenda().getCliente().getCidade()))
            throw new ServiceValidationException(ConfigHelper.getProperty("error.required", "Cidade do cliente"));
        if (notaFiscal.getVenda().getCliente().getCnpj() == null || "".equals(notaFiscal.getVenda().getCliente().getCnpj()))
            throw new ServiceValidationException(ConfigHelper.getProperty("error.required", "CNPJ do cliente"));

        try {
            // Validar itens
            for (ItemVenda item : notaFiscal.getVenda().getItens()) {
                if (item.getValorVenda() == null || item.getValorVenda().doubleValue() <= 0)
                    throw new ServiceValidationException(ConfigHelper.getProperty("error.lessThan", new String[] { item.getProduto().getReferencia(),
                                    "0 (zero)" }));
                tributarImpostosItem(item);
                item.setCfop(buscarCFOPItemVenda(item));
            }

            beginTransaction();

            atualizarValorComissao(notaFiscal.getVenda());
            atualizarRecebimentos(notaFiscal.getVenda());

            // Se não for entrega futura [empenho], atualiza movimentação de estoque
            if (!notaFiscal.getVenda().getTipo().equals(TipoCompraVenda.ENTREGA_FUTURA)) {
                // Devolução (aumento na qtde de estoque)
                if (notaFiscal.getVenda().getTipo().equals(TipoCompraVenda.DEVOLUCAO))
                    estoqueService.atualizarMovimentacaoEstoque(notaFiscal.getVenda().getItens().iterator(), VistaPrazo.PRAZO,
                                    TipoMovimentacao.DEVOLUCAO, null);
                else
                    estoqueService.atualizarMovimentacaoEstoque(notaFiscal.getVenda().getItens().iterator(), VistaPrazo.PRAZO,
                                    TipoMovimentacao.EMISSAO, null);
            }

            notaFiscal.setCancelada(false);
            NotaFiscal nfAtualizada = (NotaFiscal)save(notaFiscal);

            if (nfAtualizada.getVenda().getEmpresa().getConfiguracao().getSituacaoNfe().intValue() > 0) {
                NFe nfe = new NFe();
                nfe.setNotaFiscal(nfAtualizada);
                nfe.setId(nfAtualizada.getId());
                save(nfe);
                if (nfe.getNotaFiscal().getVenda().getEmpresa().getConfiguracao().getVersaoEmissorNfe().startsWith("2"))
                    result = nfeService.gerarArquivoEmissorNFe_v2(nfe.getNotaFiscal());
                else if (nfe.getNotaFiscal().getVenda().getEmpresa().getConfiguracao().getVersaoEmissorNfe().startsWith("1"))
                    throw new ServiceValidationException(ConfigHelper.getProperty("error.notSupported", "Versão do arquivo NFe"));
            }
            else {
                String dirReport = ConfigHelper.get().getString("dir.relatorio");
                HashMap<String, Object> parametros = new HashMap<String, Object>();
                parametros.put("DiretorioBase", dirReport);
                JRBeanArrayDataSource ds = new JRBeanArrayDataSource(new NotaFiscal[] { nfAtualizada });
                result = JasperRunManager.runReportToPdf(dirReport + "NotaFiscal.jasper", parametros, ds);
            }

            commit();
            log.info("Gerar nota fiscal: ".concat(nfAtualizada.toString()));
        }
        catch (ServiceValidationException svex) {
            rollback();
            throw new ServiceValidationException(svex.getMessage());
        }
        catch (Exception ex) {
            rollback();
            log.error(ConfigHelper.get().getString("error.general"), ex);
            throw new ServiceValidationException(ConfigHelper.get().getString("error.general"));
        }

        return result;
    }

    public void cancelarNotaFiscal(NotaFiscal notaFiscal) throws ServiceValidationException {
        try {
            boolean cancelada = notaFiscal.getCancelada() == null ? false : notaFiscal.getCancelada().booleanValue();

            beginTransaction();

            // Devolução (aumento na qtde de estoque)
            if (notaFiscal.getVenda().getTipo().equals(TipoMovimentacao.DEVOLUCAO))
                estoqueService.atualizarMovimentacaoEstoque(notaFiscal.getVenda().getItens().iterator(), VistaPrazo.PRAZO,
                                TipoMovimentacao.ESTORNO_DEVOLUCAO, null);
            else
                estoqueService.atualizarMovimentacaoEstoque(notaFiscal.getVenda().getItens().iterator(), VistaPrazo.PRAZO, TipoMovimentacao.ESTORNO,
                                null);

            // Excluir comissão
            executeUpdate("DELETE FROM br.com.alphadev.contest.entity.Comissao c WHERE c.venda.id = :pIdVenda", new Object[][] { { "pIdVenda",
                            notaFiscal.getVenda().getId() } });

            // Excluir os recebimentos
            executeUpdate("DELETE FROM br.com.alphadev.contest.entity.Recebido rb WHERE rb.receber.id IN (SELECT r.id FROM br.com.alphadev.contest.entity.Receber r WHERE r.venda.id = :pIdVenda)",
                            new Object[][] { { "pIdVenda", notaFiscal.getVenda().getId() } });
            executeUpdate("DELETE FROM br.com.alphadev.contest.entity.Receber r WHERE r.venda.id = :pIdVenda", new Object[][] { { "pIdVenda",
                            notaFiscal.getVenda().getId() } });

            // Anotar flag de nota cancelada
            notaFiscal.setCancelada(!cancelada);
            save(notaFiscal);
            commit();

            log.info("Cancelar nota fiscal: ".concat(notaFiscal.toString()));
        }
        catch (Exception ex) {
            rollback();
            log.error(ConfigHelper.get().getString("error.general"), ex);
            throw new ServiceValidationException(ConfigHelper.get().getString("error.general"));
        }
    }

    public void estornarNotaFiscal(NotaFiscal notaFiscal) throws ServiceValidationException {
        if (notaFiscal.getCancelada() != null && notaFiscal.getCancelada().booleanValue())
            throw new ServiceValidationException("Nota Fiscal cancelada. Não é possível estornar");
        try {
            beginTransaction();

            // Se não for entrega futura [empenho], atualiza movimentação de estoque
            if (!notaFiscal.getVenda().getTipo().equals(TipoCompraVenda.ENTREGA_FUTURA)) {
                // Devolução (aumento na qtde de estoque)
                if (notaFiscal.getVenda().getTipo().equals(TipoMovimentacao.DEVOLUCAO))
                    estoqueService.atualizarMovimentacaoEstoque(notaFiscal.getVenda().getItens().iterator(), VistaPrazo.PRAZO,
                                    TipoMovimentacao.ESTORNO_DEVOLUCAO, null);
                else
                    estoqueService.atualizarMovimentacaoEstoque(notaFiscal.getVenda().getItens().iterator(), VistaPrazo.PRAZO,
                                    TipoMovimentacao.ESTORNO, null);
            }

            notaFiscal.getVenda().setDesconto(0);
            save(notaFiscal.getVenda());
            Venda venda = notaFiscal.getVenda();

            // Excluir comissão
            executeUpdate("DELETE FROM br.com.alphadev.contest.entity.Comissao c WHERE c.venda.id = :pIdVenda",
                            new Object[][] { { "pIdVenda", venda.getId() } });

            // Excluir os recebimentos
            executeUpdate("DELETE FROM br.com.alphadev.contest.entity.Recebido rb WHERE rb.receber.id IN (SELECT r.id FROM br.com.alphadev.contest.entity.Receber r WHERE r.venda.id = :pIdVenda)",
                            new Object[][] { { "pIdVenda", venda.getId() } });
            executeUpdate("DELETE FROM br.com.alphadev.contest.entity.Receber r WHERE r.venda.id = :pIdVenda",
                            new Object[][] { { "pIdVenda", venda.getId() } });

            // Excluir NFe, caso exista
            if (notaFiscal.getVenda().getEmpresa().getConfiguracao().getSituacaoNfe().intValue() > 0)
                executeUpdate("DELETE FROM br.com.alphadev.contest.entity.NFe nfe WHERE nfe.notaFiscal.id IN (SELECT nf.id FROM br.com.alphadev.contest.entity.NotaFiscal nf WHERE nf.venda.id = :pIdVenda)",
                                new Object[][] { { "pIdVenda", notaFiscal.getVenda().getId() } });

            // Excluir os registros de movimentos de estoque
            executeUpdate("DELETE FROM br.com.alphadev.contest.entity.MovimentoEstoque me WHERE me.itemVenda.id IN (SELECT iv.id FROM br.com.alphadev.contest.entity.ItemVenda iv WHERE iv.venda.id = :pIdVenda)",
                            new Object[][] { { "pIdVenda", venda.getId() } });

            // Desvincular nota fiscal da venda e excluir nota fiscal
            notaFiscal.setVenda(null);
            save(notaFiscal);
            delete(notaFiscal);
            commit();

            log.info("Estornar nota fiscal: ".concat(notaFiscal.toString()));
        }
        catch (Exception ex) {
            rollback();
            log.error(ConfigHelper.get().getString("error.general"), ex);
            throw new ServiceValidationException(ConfigHelper.get().getString("error.general"));
        }
    }

    public byte[] emitirOrcamentoVenda(Orcamento orcamento) throws ServiceValidationException {
        byte[] result = null;

        if (!orcamento.getVenda().getTipo().equals(TipoCompraVenda.EMISSAO))
            throw new ServiceValidationException(ConfigHelper.getProperty("error.notEquals", new String[] { "Operação", "tipo emissão" }));

        // Validar itens
        for (ItemVenda item : orcamento.getVenda().getItens()) {
            if (item.getValorVenda() == null || item.getValorVenda().doubleValue() <= 0)
                throw new ServiceValidationException(ConfigHelper.getProperty("error.lessThan", new String[] { item.getProduto().getReferencia(),
                                "0 (zero)" }));
            item.setCfop(null);
            item.getImpostos().clear();
        }

        try {
            beginTransaction();

            atualizarRecebimentos(orcamento.getVenda());
            atualizarValorComissao(orcamento.getVenda());

            Iterator<ItemVenda> itens = orcamento.getVenda().getItens().iterator();
            estoqueService.atualizarMovimentacaoEstoque(itens, VistaPrazo.VISTA, TipoMovimentacao.EMISSAO, null);

            save(orcamento);

            String dirReport = ConfigHelper.get().getString("dir.relatorio");
            HashMap<String, Object> parametros = new HashMap<String, Object>();
            parametros.put("DiretorioBase", dirReport);

            JRBeanArrayDataSource ds = new JRBeanArrayDataSource(new Orcamento[] { orcamento });
            if (orcamento.getVenda().getItens().size() > 10)
                result = JasperRunManager.runReportToPdf(dirReport + "Venda.jasper", parametros, ds);
            else
                result = JasperRunManager.runReportToPdf(dirReport + "Venda_2_folhas.jasper", parametros, ds);

            commit();
            log.info("Gerar orçamento: ".concat(orcamento.toString()));
        }
        catch (Exception ex) {
            rollback();
            log.error(ConfigHelper.get().getString("error.general"), ex);
            throw new ServiceValidationException(ConfigHelper.get().getString("error.general"));
        }

        return result;
    }

    public void estornarOrcamento(Orcamento orcamento) throws ServiceValidationException {
        try {
            beginTransaction();
            Venda venda = orcamento.getVenda();

            // Excluir comissão
            executeUpdate("DELETE FROM br.com.alphadev.contest.entity.Comissao c WHERE c.venda.id = :pIdVenda",
                            new Object[][] { { "pIdVenda", venda.getId() } });

            // Excluir os recebimentos
            executeUpdate("DELETE FROM br.com.alphadev.contest.entity.Recebido rb WHERE rb.receber.id IN (SELECT r.id FROM br.com.alphadev.contest.entity.Receber r WHERE r.venda.id = :pIdVenda)",
                            new Object[][] { { "pIdVenda", venda.getId() } });
            executeUpdate("DELETE FROM br.com.alphadev.contest.entity.Receber r WHERE r.venda.id = :pIdVenda",
                            new Object[][] { { "pIdVenda", venda.getId() } });

            Iterator<ItemVenda> itens = orcamento.getVenda().getItens().iterator();
            estoqueService.atualizarMovimentacaoEstoque(itens, VistaPrazo.VISTA, TipoMovimentacao.ESTORNO, null);

            // Excluir os registros de movimentos de estoque
            executeUpdate("DELETE FROM br.com.alphadev.contest.entity.MovimentoEstoque me WHERE me.itemVenda.id IN (SELECT iv.id FROM br.com.alphadev.contest.entity.ItemVenda iv WHERE iv.venda.id = :pIdVenda)",
                            new Object[][] { { "pIdVenda", venda.getId() } });

            orcamento.setVenda(null);
            save(orcamento);
            delete(orcamento);
            commit();

            log.info("Estornar orçamento: ".concat(orcamento.toString()));
        }
        catch (Exception ex) {
            rollback();
            log.error(ConfigHelper.get().getString("error.general"), ex);
            throw new ServiceValidationException(ConfigHelper.get().getString("error.general"));
        }
    }

    /**
     * Buscar ano inicial das vendas
     * 
     * @return Ano inicial das vendas
     */
    public Integer buscarAnoInicioVendas() {
        return ((List<Integer>)executeQuery("SELECT MIN(YEAR(v.dataLancamento)) FROM br.com.alphadev.contest.entity.Venda v", null)).get(0);
    }

    /**
     * Pesquisar produtos vendidos por cidade no periodo
     * 
     * @param dataInicial = Data Inicial
     * @param dataFinal = Data Final
     * @param ordenacao = 1-Quantidade, 2-Valor
     * @return Lista com referência, produto, cidade, soma de quantidade e valor
     */
    public List<Object[]> pesquisarProdutosVendidosPorCidade(Date dataInicial, Date dataFinal, String cidade, int ordenacao) {
        if (dataInicial == null || dataFinal == null)
            return null;

        log.info("Pesquisar produtos mais vendidos por cidade no período: ".concat(FunctionsHelper.toStringNotNull(cidade)));
        log.debug(">>> Data início: ".concat(FunctionsHelper.dateFormat(dataInicial, "dd/MM/yyyy")));
        log.debug(">>> Data fim: ".concat(FunctionsHelper.dateFormat(dataFinal, "dd/MM/yyyy")));

        StringBuilder hql = new StringBuilder();
        hql.append("SELECT iv.produto.referencia, iv.produto.descricao, ");
        hql.append("  iv.venda.cliente.cidade.nome, SUM(iv.quantidade), SUM(iv.valorVenda * iv.quantidade) ");
        hql.append("FROM br.com.alphadev.contest.entity.ItemVenda iv ");
        hql.append("WHERE iv.venda.dataLancamento BETWEEN :pDataInicial AND :pDataFinal ");
        if (cidade != null)
            hql.append("AND UPPER(iv.venda.cliente.cidade.nome) LIKE UPPER('%").append(cidade).append("%') ");
        hql.append("GROUP BY iv.produto.referencia, iv.produto.descricao, iv.venda.cliente.cidade.nome ");
        if (ordenacao == 1)
            hql.append("ORDER BY iv.venda.cliente.cidade.nome, SUM(iv.quantidade) DESC ");
        else if (ordenacao == 2)
            hql.append("ORDER BY iv.venda.cliente.cidade.nome, SUM(iv.valorVenda * iv.quantidade) DESC ");

        // Seta a hora da data inicial para 0
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(dataInicial);
        gc.set(Calendar.HOUR_OF_DAY, 0);
        dataInicial = gc.getTime();

        // Seta a hora da data final para 24 e soma 1 dia
        gc.setTime(dataFinal);
        gc.set(Calendar.HOUR_OF_DAY, 24);
        gc.add(Calendar.DAY_OF_MONTH, 1);
        dataFinal = gc.getTime();

        Object[][] params = new Object[][] { { "pDataInicial", dataInicial }, { "pDataFinal", dataFinal } };
        return (List<Object[]>)executeQuery(hql.toString(), params);
    }

    public List<Object[]> pesquisarProdutosVendidosPorVendedor(Date dataInicial, Date dataFinal, Funcionario funcionario, int ordenacao) {
        if (dataInicial == null || dataFinal == null)
            return null;

        log.info("Pesquisar produtos vendidos por funcionário no período: ".concat(funcionario.toString()));
        log.debug(">>> Data início: ".concat(FunctionsHelper.dateFormat(dataInicial, "dd/MM/yyyy")));
        log.debug(">>> Data fim: ".concat(FunctionsHelper.dateFormat(dataFinal, "dd/MM/yyyy")));

        StringBuilder hql = new StringBuilder();
        hql.append("SELECT c.funcionario.nome, iv.produto.descricao, SUM(iv.quantidade), SUM(iv.valorVenda * iv.quantidade) ");
        hql.append("FROM br.com.alphadev.contest.entity.Comissao c, IN(c.venda.itens) iv ");
        hql.append("WHERE c.venda.dataLancamento BETWEEN :pDataInicial AND :pDataFinal ");
        if (funcionario != null)
            hql.append("AND c.funcionario.id = ").append(funcionario.getId());
        hql.append(" GROUP BY c.funcionario.nome, iv.produto.descricao ");
        if (ordenacao == 1)
            hql.append("ORDER BY SUM(iv.quantidade) DESC ");
        else if (ordenacao == 2)
            hql.append("ORDER BY SUM(iv.valorVenda * iv.quantidade) DESC ");

        // Seta a hora da data inicial para 0
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(dataInicial);
        gc.set(Calendar.HOUR_OF_DAY, 0);
        dataInicial = gc.getTime();

        // Seta a hora da data final para 24 e soma 1 dia
        gc.setTime(dataFinal);
        gc.set(Calendar.HOUR_OF_DAY, 24);
        gc.add(Calendar.DAY_OF_MONTH, 1);
        dataFinal = gc.getTime();

        Object[][] params = new Object[][] { { "pDataInicial", dataInicial }, { "pDataFinal", dataFinal } };
        return (List<Object[]>)executeQuery(hql.toString(), params);
    }

    /**
     * Pesquisar produtos mais vendidos
     * 
     * @param dataInicial = Data inicial
     * @param dataFinal = Data final
     * @param ordenacao = 1-Por Quantidade, 2-Por valor
     * @return Lista com referencia, descrição, soma de quantidades e soma de valores
     */
    public List<Object[]> pesquisarProdutosMaisVendidos(Date dataInicial, Date dataFinal, int ordenacao) {
        if (dataInicial == null || dataFinal == null)
            return null;

        log.info("Pesquisar produtos mais vendidos por período: ");
        log.debug(">>> Data início: ".concat(FunctionsHelper.dateFormat(dataInicial, "dd/MM/yyyy")));
        log.debug(">>> Data fim: ".concat(FunctionsHelper.dateFormat(dataFinal, "dd/MM/yyyy")));

        StringBuilder hql = new StringBuilder();
        hql.append("SELECT iv.produto.referencia, iv.produto.descricao, SUM(iv.quantidade), SUM(iv.valorVenda * iv.quantidade) ");
        hql.append("FROM br.com.alphadev.contest.entity.ItemVenda iv ");
        hql.append("WHERE iv.venda.dataLancamento BETWEEN :pDataInicial AND :pDataFinal ");
        hql.append("GROUP BY iv.produto.referencia, iv.produto.descricao ");
        if (ordenacao == 1)
            hql.append("ORDER BY SUM(iv.quantidade) DESC ");
        else if (ordenacao == 2)
            hql.append("ORDER BY SUM(iv.valorVenda * iv.quantidade) DESC ");

        // Seta a hora da data inicial para 0
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(dataInicial);
        gc.set(Calendar.HOUR_OF_DAY, 0);
        dataInicial = gc.getTime();

        // Seta a hora da data final para 24 e soma 1 dia
        gc.setTime(dataFinal);
        gc.set(Calendar.HOUR_OF_DAY, 24);
        gc.add(Calendar.DAY_OF_MONTH, 1);
        dataFinal = gc.getTime();

        Object[][] params = new Object[][] { { "pDataInicial", dataInicial }, { "pDataFinal", dataFinal } };
        return (List<Object[]>)executeQuery(hql.toString(), params);
    }

    public List<Recebido> pesquisarRecebimentosPorCaixa(long idCaixa) {
        log.info("Pesquisar vendas do caixa id: " + idCaixa);

        StringBuilder hql = new StringBuilder();
        hql.append("SELECT r ");
        hql.append("FROM br.com.alphadev.contest.entity.Recebido r ");
        hql.append("WHERE r.caixa.id = :pIdCaixa ");
        hql.append("ORDER BY r.receber.venda.dataLancamento");

        Object[][] params = new Object[][] { { "pIdCaixa", idCaixa } };
        return (List<Recebido>)executeQuery(hql.toString(), params);
    }

    public Double calcularTotalVendidoCaixa(long idCaixa) {
        log.info("Calcular total vendido no caixa id: " + idCaixa);

        StringBuilder hql = new StringBuilder();
        hql.append("SELECT r ");
        hql.append("FROM br.com.alphadev.contest.entity.Recebido r ");
        hql.append("WHERE r.caixa.id = :pIdCaixa");

        Object[][] params = new Object[][] { { "pIdCaixa", idCaixa } };
        List<Recebido> listaRecebimentosCaixa = (List<Recebido>)executeQuery(hql.toString(), params);

        double total = 0d;
        Iterator<Recebido> it = listaRecebimentosCaixa.iterator();
        while (it.hasNext()) {
            Recebido recebido = it.next();
            total += recebido.getValorPago();
        }

        return total;
    }

    public List<Object[]> pesquisarTotalVendidoCaixaPorPeriodo(Date dataInicial, Date dataFinal) {
        log.info("Pesquisar total vendido nos caixa no período");
        StringBuilder hql = new StringBuilder();
        hql.append("SELECT c.dataAbertura, c.funcionario.nome, SUM(c.valorFechamento) ");
        hql.append("FROM br.com.alphadev.contest.entity.Caixa AS c ");
        hql.append("WHERE c.dataAbertura >= :pDataInicial ");
        hql.append("AND c.dataFechamento <= :pDataFinal ");
        hql.append("AND c.dataFechamento IS NOT EMPTY ");
        hql.append("GROUP BY c.dataAbertura, c.funcionario.nome, c.horaAbertura ");
        hql.append("ORDER BY c.dataAbertura, c.horaAbertura ");
        Object[][] params = new Object[][] { { "pDataInicial", dataInicial }, { "pDataFinal", dataFinal } };
        return (List<Object[]>)executeQuery(hql.toString(), params);
    }

    public Caixa buscarCaixaAbertoPorFuncionario(Funcionario funcionario) {
        log.info("Buscar caixa aberto pelo funcionário: " + funcionario.toString());
        StringBuilder hql = new StringBuilder();
        hql.append("SELECT c ");
        hql.append("FROM br.com.alphadev.contest.entity.Caixa c ");
        hql.append("WHERE c.dataFechamento IS NULL ");
        hql.append("AND c.horaFechamento IS NULL ");
        hql.append("AND c.funcionario.id = :pIdFunc");
        Object[][] params = new Object[][] { { "pIdFunc", funcionario.getId() } };
        List<Caixa> lista = (List<Caixa>)executeQuery(hql.toString(), params);
        if (lista != null && lista.size() > 0)
            return lista.get(0);
        return null;
    }

    public String gerarListagemFechamentoCaixa(long idCaixa) {
        Caixa caixa = new Caixa(idCaixa);

        if (caixa == null || caixa.getDataFechamento() == null || caixa.getHoraFechamento() == null || "".equals(caixa.getHoraFechamento())) {
            return "CAIXA NÃO EXISTE OU NÃO FOI FECHADO";
        }

        log.info("Gerar cupom de fechamento do caixa: ".concat(caixa.toString()));

        HashMap<FormaPagamento, Double> totalFormaPagto = new HashMap<FormaPagamento, Double>();
        StringBuilder result = new StringBuilder();
        List<Recebido> recebimentosCaixa = pesquisarRecebimentosPorCaixa(idCaixa);

        if (recebimentosCaixa != null && recebimentosCaixa.size() > 0) {
            float totalItens = 0;

            result.append("----------------------------------------\n");
            result.append("    RELATÓRIO DE FECHAMENTO DE CAIXA    \n");
            result.append("----------------------------------------\n");
            result.append("Código......: ").append(caixa.getId()).append("\n");
            result.append("Nome........: ").append(caixa.getFuncionario().getNome()).append("\n");
            result.append("Abertura....: ").append(FunctionsHelper.dateFormat(caixa.getDataAbertura(), "dd/MM/yyyy")).append(" - ")
                            .append(caixa.getHoraAbertura()).append("\n");
            result.append("Valor Abert.: ").append(FunctionsHelper.numberFormat(caixa.getValorAbertura().doubleValue(), "###,##0.00")).append("\n");
            result.append("Fechamento..: ").append(FunctionsHelper.dateFormat(caixa.getDataFechamento(), "dd/MM/yyyy")).append(" - ")
                            .append(caixa.getHoraFechamento()).append("\n");
            result.append("----------------------------------------\n");
            result.append("               I T E N S                \n");
            result.append("----------------------------------------\n");

            for (int i = 0; i < recebimentosCaixa.size(); i++) {
                Recebido recebido = recebimentosCaixa.get(i);

                Iterator<ItemVenda> itens = recebido.getReceber().getVenda().getItens().iterator();
                while (itens.hasNext()) {
                    ItemVenda item = itens.next();
                    result.append(FunctionsHelper.fillSpacesRight(item.getProduto().getDescricao(), 18));
                    result.append(" ");
                    result.append(FunctionsHelper.fillSpacesLeft(Integer.toString(item.getQuantidade()).trim(), 3));
                    result.append(" X ");
                    result.append(FunctionsHelper.fillSpacesLeft(FunctionsHelper.numberFormat(item.getValorVenda().doubleValue(), "###,##0.00")
                                    .trim(), 6));
                    result.append(" = ");
                    result.append(FunctionsHelper.fillSpacesLeft(FunctionsHelper.numberFormat(item.getSubTotal(), "###,##0.00").trim(), 6));
                    result.append("\n");
                }

                result.append("SUB-TOTAL: ");
                result.append(FunctionsHelper.numberFormat(recebido.getReceber().getVenda().getValorTotal(), "###,##0.00"));
                result.append("\n");
                totalItens += recebido.getReceber().getVenda().getValorTotal();

                Double totalAtual = totalFormaPagto.get(recebido.getFormaPagamento());
                if (totalAtual == null)
                    totalAtual = new Double(0);
                totalAtual = totalAtual + recebido.getValorPago();
                totalFormaPagto.put(recebido.getFormaPagamento(), totalAtual);
            }

            // Imprimir total por formas de pagamento
            result.append("----------------------------------------\n");
            result.append("  F O R M A S   D E   P A G A M E N T O \n");
            result.append("----------------------------------------\n");
            float totalFPagto = 0;
            for (FormaPagamento fp : FormaPagamento.values()) {
                Double subTotal = totalFormaPagto.get(fp);
                if (subTotal == null)
                    continue;
                totalFPagto += subTotal;
                result.append(FunctionsHelper.fillSpacesRight("====> ".concat(fp.getDescricao()).concat(": "), 30));
                result.append(FunctionsHelper.fillSpacesLeft(FunctionsHelper.numberFormat(subTotal, "###,##0.00"), 10)).append("\n");
            }

            result.append("----------------------------------------\n");
            result.append("TOTAL FORMA PAGTO => ");
            result.append(FunctionsHelper.numberFormat(totalFPagto, "###,##0.00")).append("\n");
            result.append("TOTAL ITENS =======> ");
            result.append(FunctionsHelper.numberFormat(totalItens, "###,##0.00")).append("\n");
            result.append("TOTAL FECHAMENTO ==> ");
            result.append(FunctionsHelper.numberFormat(caixa.getValorFechamento().doubleValue(), "###,##0.00")).append("\n");
            result.append("----------------------------------------\n");
        }

        result.append("\n");
        result.append("\n");
        result.append("\n");
        result.append("\n");
        result.append("\n");
        result.append("\n");
        result.append("\n");
        return result.toString();
    }

    public String gerarCupomVenda(Venda venda, FormaPagamento formaPagamento) {
        log.info("Gerar cupom da venda: ".concat(venda.toString()));

        StringBuilder result = new StringBuilder();
        result.append("----------------------------------------\n");
        result.append(FunctionsHelper.fillSpacesLeftRight(venda.getEmpresa().getNomeFantasia(), 40)).append("\n");
        result.append(FunctionsHelper.fillSpacesLeftRight("Fone: ".concat(venda.getEmpresa().getTelefone()), 40)).append("\n");
        result.append("----------------------------------------\n");
        result.append("Código....: ").append(venda.getId()).append("\n");
        result.append("Data......: ").append(FunctionsHelper.dateFormat(new Date(), "dd/MM/yyyy HH:mm")).append("\n");
        result.append("Forma Pgto: ").append(formaPagamento.getDescricao()).append("\n");
        result.append("----------------------------------------\n");
        result.append("               I T E N S                \n");
        result.append("----------------------------------------\n");

        Iterator<ItemVenda> itens = venda.getItens().iterator();
        while (itens.hasNext()) {
            ItemVenda item = itens.next();
            result.append(FunctionsHelper.fillSpacesRight(item.getProduto().getDescricao(), 18));
            result.append(" ");
            result.append(FunctionsHelper.fillSpacesLeft(Integer.toString(item.getQuantidade()).trim(), 3));
            result.append(" X ");
            result.append(FunctionsHelper.fillSpacesLeft(FunctionsHelper.numberFormat(item.getValorVenda().doubleValue(), "#####0.00").trim(), 6));
            result.append(" = ");
            result.append(FunctionsHelper.fillSpacesLeft(FunctionsHelper.numberFormat(item.getSubTotal(), "#####0.00").trim(), 6));
            result.append("\n");
        }

        result.append("----------------------------------------\n");
        result.append("T O T A L =========> ");
        result.append(FunctionsHelper.numberFormat(venda.getValorTotal(), "###,##0.00"));
        result.append("\n");
        result.append("----------------------------------------\n");
        result.append("          Cupom sem valor fiscal        \n");
        result.append("\n");
        result.append("\n");
        result.append("\n");
        result.append("\n");
        result.append("\n");
        result.append("\n");
        result.append("\n");

        return result.toString();
    }

    public List<ComboPrecoComissao> calcularPrecosVenda(Produto produto) {
        ArrayList<ComboPrecoComissao> result = new ArrayList<ComboPrecoComissao>();

        double custoVista = produto.getCustoVista();
        double custoPrazo = produto.getCustoPrazo();

        if (produto.getTabelaPreco() == null)
            return null;

        // Calcular preços de venda a partir das faixas das tabelas de preços 
        for (FaixaPreco faixa : produto.getTabelaPreco().getFaixas()) {
            ComboPrecoComissao cpc = new ComboPrecoComissao();
            if (custoVista > 0d && faixa.getMarkupVista() != null && faixa.getMarkupVista() > 0d) {
                BigDecimal precoVista = new BigDecimal(custoVista * ((100 + faixa.getMarkupVista()) / 100)).setScale(2, BigDecimal.ROUND_HALF_UP);
                if (precoVista.doubleValue() > 0d) {
                    cpc.setPreco(precoVista.doubleValue());
                    cpc.setComissao(faixa.getComissao().doubleValue());
                    cpc.setVistaPrazo(VistaPrazo.VISTA);
                    result.add(cpc);
                }
            }
            if (custoPrazo > 0d && faixa.getMarkupPrazo() != null && faixa.getMarkupPrazo() > 0d) {
                BigDecimal precoPrazo = new BigDecimal(custoPrazo * ((100 + faixa.getMarkupPrazo()) / 100)).setScale(2, BigDecimal.ROUND_HALF_UP);
                if (precoPrazo.doubleValue() > 0d) {
                    cpc.setPreco(precoPrazo.doubleValue());
                    cpc.setComissao(faixa.getComissao().doubleValue());
                    cpc.setVistaPrazo(VistaPrazo.PRAZO);
                    result.add(cpc);
                }
            }
        }

        return result;
    }
}