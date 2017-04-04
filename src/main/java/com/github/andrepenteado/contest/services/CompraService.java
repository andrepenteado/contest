
package com.github.andrepenteado.contest.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.StringTokenizer;

import javax.ejb.Stateless;

import com.github.andrepenteado.contest.entity.Compra;
import com.github.andrepenteado.contest.entity.Fornecedor;
import com.github.andrepenteado.contest.entity.ItemCompra;
import com.github.andrepenteado.contest.entity.Pagar;
import com.github.andrepenteado.contest.entity.Produto;
import com.github.andrepenteado.contest.entity.valueObject.TipoCompraVenda;
import com.github.andrepenteado.contest.entity.valueObject.TipoMovimentacao;
import com.github.andrepenteado.contest.entity.valueObject.VistaPrazo;
import com.github.andrepenteado.core.BasicService;
import com.github.andrepenteado.core.ServicesFactory;
import com.github.andrepenteado.exception.ServiceValidationException;
import com.github.andrepenteado.util.ConfigHelper;
import com.github.andrepenteado.util.FunctionsHelper;

/**
 * @author Andre Penteado
 * @since 29/08/2007 - 21:51:26
 */
@Stateless
public class CompraService extends BasicService {

    private EstoqueService estoqueService;

    @Override
    public void setParameters(Object[] params) {
        super.setParameters(params);
        this.estoqueService = (EstoqueService)ServicesFactory.getInstance(EstoqueService.class, params);
    }

    public void emitirCompra(Compra compra) throws ServiceValidationException {
        this.validarCompra(compra);

        // Reiniciar array de recebimentos
        if (compra.getPagamentos() == null)
            compra.setPagamentos(new ArrayList<Pagar>());
        compra.getPagamentos().clear();

        compra.setPagamentos(this.gerarPagamentos(compra));

        compra.setTipo(TipoCompraVenda.EMISSAO);

        beginTransaction();
        try {
            Compra compraAtualizada = (Compra)save(compra);
            VistaPrazo vistaPrazo = compra.getNotaFiscal() == null || compra.getNotaFiscal() <= 0 ? VistaPrazo.VISTA : VistaPrazo.PRAZO;
            estoqueService.atualizarMovimentacaoEstoque(compraAtualizada.getItens().iterator(), vistaPrazo, TipoMovimentacao.EMISSAO, null);
            commit();
        }
        catch (Exception ex) {
            rollback();
            log.error(ConfigHelper.get().getString("error.general"), ex);
            throw new ServiceValidationException(ConfigHelper.get().getString("error.general"));
        }
        log.info("Emitir compra: ".concat(compra.toString()));
    }

    /**
     * Altera uma compra já emitida. Não faz a atualização de estoque pois os itens não são alterados
     * @param compra
     * @throws ServiceValidationException
     */
    public void alterarCompraEmitida(Compra compra) throws ServiceValidationException {
        this.validarCompra(compra);

        // Reiniciar array de recebimentos
        if (compra.getPagamentos() == null)
            compra.setPagamentos(new ArrayList<Pagar>());
        compra.getPagamentos().clear();

        compra.setPagamentos(this.gerarPagamentos(compra));

        compra.setTipo(TipoCompraVenda.EMISSAO);

        beginTransaction();
        try {
            save(compra);
            commit();
        }
        catch (Exception ex) {
            rollback();
            log.error(ConfigHelper.get().getString("error.general"), ex);
            throw new ServiceValidationException(ConfigHelper.get().getString("error.general"));
        }
        log.info("Alterar compra emitida: ".concat(compra.toString()));
    }

    /**
     * Valida a Compra para Emissão
     * @param compra
     * @throws ServiceValidationException
     */
    private void validarCompra(Compra compra) throws ServiceValidationException {
        if (compra.getFornecedor() == null)
            throw new ServiceValidationException(ConfigHelper.getProperty("error.required", "Fornecedor"));
        else if (compra.getDataPedido() == null)
            throw new ServiceValidationException(ConfigHelper.getProperty("error.required", "Data de Emissão"));
        else if (compra.getPrazoPagamento() == null)
            throw new ServiceValidationException(ConfigHelper.getProperty("error.required", "Forma de Pagamento"));
        else if (compra.getItens() == null || compra.getItens().size() <= 0)
            throw new ServiceValidationException(ConfigHelper.getProperty("error.isEmpty", "Itens da Compra"));

        for (ItemCompra itemCompra : compra.getItens()) {
            if (itemCompra.getCfop() == null && compra.getNotaFiscal() != null)
                throw new ServiceValidationException(ConfigHelper.getProperty("error.required", "CFOP"));
        }

        if (compra.getNotaFiscal() != null) {
            Compra compraExistente = buscarNotaFiscalCompraPorFornecedor(compra.getNotaFiscal(), compra.getFornecedor());
            if (compraExistente != null && !compra.getId().equals(compraExistente.getId()))
                throw new ServiceValidationException(ConfigHelper.get().getString("error.notaFiscalExistente"));
        }

    }

    /**
     * Gera a lista de Pagamentos de uma Compra
     * @param compra
     * @return List<Pagar>
     */
    private List<Pagar> gerarPagamentos(Compra compra) {

        StringTokenizer stk = new StringTokenizer(compra.getPrazoPagamento().getParcelas(), ",");
        double soma = 0;
        double total = compra.getValorTotal();
        ArrayList<Integer> dias = new ArrayList<Integer>();

        // Separa as parcelas
        while (stk.hasMoreElements())
            dias.add(Integer.parseInt((String)stk.nextElement()));

        ArrayList<Pagar> pagamentos = new ArrayList<Pagar>();

        // Soma no vencimento e divide o valor da parcela
        for (int i = 0; i < dias.size(); i++) {
            GregorianCalendar vencimento = new GregorianCalendar();
            vencimento.add(Calendar.DAY_OF_MONTH, dias.get(i));

            BigDecimal parcela = new BigDecimal(total / dias.size());
            parcela.setScale(2, BigDecimal.ROUND_HALF_UP);
            double totalParcela = parcela.doubleValue();

            if (i == dias.size() - 1)
                totalParcela = total - soma;

            Pagar pagar = new Pagar();
            pagar.setParcela(i);
            pagar.setVencimento(vencimento.getTime());
            pagar.setValor(totalParcela);
            pagar.setCompra(compra);

            pagamentos.add(pagar);
            soma += totalParcela;
        }

        return pagamentos;
    }

    public void excluirPedidoCompra(Compra compra) throws ServiceValidationException {
        if (compra.getEmissao() != null)
            throw new ServiceValidationException(ConfigHelper.get().getString("error.compraEmitida"));

        beginTransaction();

        // Excluir os movimentos de estoque, caso exista
        StringBuilder hql = new StringBuilder();
        hql.append("DELETE FROM com.github.andrepenteado.contest.entity.MovimentoEstoque me ");
        hql.append("WHERE me.itemCompra.id IN ( ");
        hql.append("   SELECT ic.id FROM com.github.andrepenteado.contest.entity.ItemCompra ic ");
        hql.append("   WHERE ic.compra.id = :pIdCompra ");
        hql.append(")");
        executeUpdate(hql.toString(), new Object[][] { { "pIdCompra", compra.getId() } });

        delete(compra);
        commit();
    }

    public void estornarCompraEmitida(Compra compra) throws ServiceValidationException {
        try {
            beginTransaction();

            // Devolução (diminuição na qtde de estoque)
            if (compra.getTipo().equals(TipoMovimentacao.DEVOLUCAO))
                estoqueService.atualizarMovimentacaoEstoque(compra.getItens().iterator(), VistaPrazo.PRAZO, TipoMovimentacao.ESTORNO_DEVOLUCAO, null);
            else
                estoqueService.atualizarMovimentacaoEstoque(compra.getItens().iterator(), VistaPrazo.PRAZO, TipoMovimentacao.ESTORNO, null);

            // Excluir os pagamentos
            executeUpdate("DELETE FROM com.github.andrepenteado.contest.entity.Pago pg WHERE pg.pagar.id IN (SELECT p.id FROM com.github.andrepenteado.contest.entity.Pagar p WHERE p.compra.id = :pIdCompra)",
                            new Object[][] { { "pIdCompra", compra.getId() } });
            executeUpdate("DELETE FROM com.github.andrepenteado.contest.entity.Pagar p WHERE p.compra.id = :pIdCompra",
                            new Object[][] { { "pIdCompra", compra.getId() } });

            compra.setEmissao(null);
            save(compra);
            commit();

            log.info("Estornar compra: ".concat(compra.toString()));
        }
        catch (Exception ex) {
            rollback();
            log.error(ConfigHelper.get().getString("error.general"), ex);
            throw new ServiceValidationException(ConfigHelper.get().getString("error.general"));
        }
    }

    public List<ItemCompra> pesquisarItensCompradosPorFornecedor(long idFornecedor) {
        if (idFornecedor <= 0)
            return null;

        log.info("Pesquisar itens compras por fornecedor ID: ".concat(FunctionsHelper.toStringNotNull(idFornecedor)));

        StringBuilder hql = new StringBuilder();
        hql.append("SELECT item ");
        hql.append("FROM com.github.andrepenteado.contest.entity.ItemCompra item ");
        hql.append("WHERE item.compra.fornecedor.id = :pIdFornecedor ");
        hql.append("ORDER BY item.compra.emissao DESC ");

        Object[][] params = new Object[][] { { "pIdFornecedor", idFornecedor } };
        return (List<ItemCompra>)executeQuery(hql.toString(), params);
    }

    public ItemCompra buscarUltimoItemCompraVista(Produto produto) {
        log.debug("Buscar útimo item comprado vista do produto " + produto.getReferencia());

        StringBuilder hql = new StringBuilder();
        hql.append("SELECT me.itemCompra ");
        hql.append("FROM com.github.andrepenteado.contest.entity.MovimentoEstoque me ");
        hql.append("WHERE me.itemCompra.produto.id = :pIdProduto ");
        hql.append("AND me.itemCompra.valorCompra > 0 ");
        hql.append("AND me.vistaPrazo = '").append(VistaPrazo.VISTA.name()).append("' ");
        hql.append("ORDER BY me.data DESC ");

        Object[][] params = new Object[][] { { "pIdProduto", produto.getId() } };
        return (ItemCompra)executeFind(hql.toString(), params);
    }

    public ItemCompra buscarUltimoItemCompraPrazo(Produto produto) {
        log.debug("Buscar útimo item comprado prazo do produto " + produto.getReferencia());

        StringBuilder hql = new StringBuilder();
        hql.append("SELECT me.itemCompra ");
        hql.append("FROM com.github.andrepenteado.contest.entity.MovimentoEstoque me ");
        hql.append("WHERE me.itemCompra.produto.id = :pIdProduto ");
        hql.append("AND me.itemCompra.valorCompra > 0 ");
        hql.append("AND me.vistaPrazo = '").append(VistaPrazo.PRAZO.name()).append("' ");
        hql.append("ORDER BY me.data DESC ");

        Object[][] params = new Object[][] { { "pIdProduto", produto.getId() } };
        return (ItemCompra)executeFind(hql.toString(), params);
    }

    public Compra buscarCompraPorNotaFiscal(long numero) {
        log.info("Pesquisar compra pelo número: " + numero);
        return (Compra)findByField(Compra.class, "notaFiscal", numero);
    }

    public Compra buscarNotaFiscalCompraPorFornecedor(long nf, Fornecedor fornecedor) {
        log.info("Buscar nota fiscal de compra por fornecedor: NF " + nf);

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT c FROM com.github.andrepenteado.contest.entity.Compra c ");
        sb.append("WHERE c.notaFiscal = :pNF ");
        sb.append("AND c.fornecedor = :pFornecedor");

        Object[][] params = new Object[][] { { "pNF", nf }, { "pFornecedor", fornecedor } };
        return (Compra)executeFind(sb.toString(), params);
    }

    public List<Compra> pesquisarComprasNaoEmitidas() {
        log.info("Pesquisar compras não emitidas");

        StringBuilder hql = new StringBuilder();

        hql.append("SELECT compra ");
        hql.append("FROM com.github.andrepenteado.contest.entity.Compra compra ");
        hql.append("WHERE compra.emissao IS NULL ");
        hql.append("ORDER BY compra.dataPedido DESC");

        return (List<Compra>)executeQuery(hql.toString(), null);
    }

    public void gravarPedidoCompra(Compra compra) throws ServiceValidationException {
        //Não pode preencher Nota Fiscal nem Emissão se for apenas gravar
        if (compra.getNotaFiscal() != null && compra.getNotaFiscal() != -1)
            throw new ServiceValidationException(ConfigHelper.getProperty("error.gravarCompraCampoInvalido", "Número da Nota Fiscal"));
        if (compra.getEmissao() != null) {
            compra.setEmissao(null);
            throw new ServiceValidationException(ConfigHelper.getProperty("error.gravarCompraCampoInvalido", "Data de Emissão"));
        }

        compra.gravar();
    }
}