
package br.com.alphadev.contest.actions.compras;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.mentawai.authorization.Authorizable;
import org.mentawai.core.BaseAction;
import org.mentawai.rule.IntegerRule;
import org.mentawai.rule.RequiredFieldRule;
import org.mentawai.validation.Validatable;
import org.mentawai.validation.Validator;

import br.com.alphadev.annotations.ActionClass;
import br.com.alphadev.annotations.ConsequenceOutput;
import br.com.alphadev.annotations.Consequences;
import br.com.alphadev.contest.KGlobal;
import br.com.alphadev.contest.KGlobal.TipoPesquisaCompra;
import br.com.alphadev.contest.entity.CFOP;
import br.com.alphadev.contest.entity.Compra;
import br.com.alphadev.contest.entity.Fornecedor;
import br.com.alphadev.contest.entity.ImpostoCompra;
import br.com.alphadev.contest.entity.ItemCompra;
import br.com.alphadev.contest.entity.PrazoPagamento;
import br.com.alphadev.contest.entity.Produto;
import br.com.alphadev.contest.entity.valueObject.Frete;
import br.com.alphadev.contest.entity.valueObject.Imposto;
import br.com.alphadev.contest.filters.CompraFilter;
import br.com.alphadev.contest.services.CadastroService;
import br.com.alphadev.contest.services.CompraService;
import br.com.alphadev.core.ServicesFactory;
import br.com.alphadev.exception.ServiceValidationException;
import br.com.alphadev.util.ConfigHelper;
import br.com.alphadev.util.FunctionsHelper;
import br.com.alphadev.util.Log4jWrapper;
import br.com.alphadev.util.UsuarioLogadoWrapper;

/**
 * @author Andre Penteado
 * @since 29/08/2007 - 20:59:52
 */
@ActionClass(prefix = "/compras/pedido")
public class CompraAction extends BaseAction implements Authorizable, Validatable {

    private Log4jWrapper log = new Log4jWrapper(CompraAction.class, null);
    private CompraService compraServices = null;
    private CadastroService cadastroServices = null;

    private void instanciarServicos(UsuarioLogadoWrapper userLogin) {
        try {
            compraServices = (CompraService)ServicesFactory.getInstance(CompraService.class, new Object[] { userLogin });
            cadastroServices = (CadastroService)ServicesFactory.getInstance(CadastroService.class, new Object[] { userLogin });
            log = new Log4jWrapper(CompraAction.class, userLogin);
        }
        catch (Exception ex) {
            log.fatal("AÇÕES NÃO INSTANCIADAS: ".concat(CompraAction.class.getName()), ex);
        }
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean authorize(String innerAction, Object user, List groups) {
        if (user == null || groups == null)
            return false;

        instanciarServicos((UsuarioLogadoWrapper)user);

        if (groups.indexOf(KGlobal.CATEGORIA_SUPERUSUARIO) != -1 || groups.indexOf(KGlobal.CATEGORIA_COMPRAS) != -1)
            return true;

        return false;
    }

    public void prepareValidator(Validator validator, String innerAction) {
        if (innerAction.equals("gravar")) {
            validator.add("txt_nome_fornecedor", new RequiredFieldRule(), ConfigHelper.getProperty("error.required", "Fornecedor"));
            validator.add("cbo_prazo_pagamento", new RequiredFieldRule(), ConfigHelper.getProperty("error.required", "Forma de Pagamento"));
            validator.add("txt_id_fornecedor", new IntegerRule(), ConfigHelper.getProperty("error.integer", "Código do Fornecedor"));
        }
        else if (innerAction.equals("incluirItem")) {
            validator.add("txt_descricao_produto", new RequiredFieldRule(), ConfigHelper.getProperty("error.required", "Produto"));
            validator.add("txt_quantidade", new RequiredFieldRule(), ConfigHelper.getProperty("error.required", "Quantidade"));
            validator.add("txt_preco_produto", new RequiredFieldRule(), ConfigHelper.getProperty("error.required", "Preço"));
        }
        else if (innerAction.equals("emitir")) {
            validator.add("txt_emissao", new RequiredFieldRule(), ConfigHelper.getProperty("error.required", "Data da Emissão"));
            validator.add("txt_nome_fornecedor", new RequiredFieldRule(), ConfigHelper.getProperty("error.required", "Fornecedor"));
            validator.add("cbo_prazo_pagamento", new RequiredFieldRule(), ConfigHelper.getProperty("error.required", "Forma de Pagamento"));
            validator.add("txt_id_fornecedor", new IntegerRule(), ConfigHelper.getProperty("error.integer", "Código do Fornecedor"));
        }
        else if (innerAction.equals("alterar")) {
            validator.add("txt_emissao", new RequiredFieldRule(), ConfigHelper.getProperty("error.required", "Data da Emissão"));
            validator.add("txt_nome_fornecedor", new RequiredFieldRule(), ConfigHelper.getProperty("error.required", "Fornecedor"));
            validator.add("cbo_prazo_pagamento", new RequiredFieldRule(), ConfigHelper.getProperty("error.required", "Forma de Pagamento"));
            validator.add("txt_id_fornecedor", new IntegerRule(), ConfigHelper.getProperty("error.integer", "Código do Fornecedor"));
        }
    }

    @Consequences(outputs = @ConsequenceOutput(page = "/compras/pedidos.jsp"))
    public String iniciar() {
        try {
            List<Compra> listaComprasNaoEmitidas = compraServices.pesquisarComprasNaoEmitidas();
            output.setValue("listaPedidos", listaComprasNaoEmitidas);
        }
        catch (Exception ex) {
            addError(ConfigHelper.get().getString("error.general"));
            log.error(ConfigHelper.get().getString("error.general"), ex);
            return ERROR;
        }
        return SUCCESS;
    }

    @Consequences(outputs = @ConsequenceOutput(page = "/compras/pesquisar.jsp"))
    public String pesquisar() {
        try {
            Date emissaoInicial = input.getDate("txt_emissao_inicial", "dd/MM/yyyy");
            Date emissaoFinal = input.getDate("txt_emissao_final", "dd/MM/yyyy");
            Date vencimentoInicial = input.getDate("txt_vencimento_inicial", "dd/MM/yyyy");
            Date vencimentoFinal = input.getDate("txt_vencimento_final", "dd/MM/yyyy");
            long idFornecedor = input.getLong("txt_id_fornecedor");
            long numero = input.getLong("txt_numero");

            CompraFilter<Compra> filter = ServicesFactory.getInstance(CompraFilter.class, Compra.class);
            Collection<Compra> listaCompra = new ArrayList<Compra>();
            filter.setDataEmissaoInicial(FunctionsHelper.stringToDate("01/01/1900"));
            filter.setDataEmissaoFinal(FunctionsHelper.stringToDate("01/01/2900"));

            if (input.getString(TipoPesquisaCompra.NUMERO.name()) != null && input.getString(TipoPesquisaCompra.NUMERO.name()).length() > 0) {
                Compra compra = compraServices.buscarCompraPorNotaFiscal(numero);
                if (compra != null)
                    listaCompra.add(compra);
                output.setValue("tabName", TipoPesquisaCompra.NUMERO);
            }
            else if (input.getString(TipoPesquisaCompra.FORNECEDOR.name()) != null && input.getString(TipoPesquisaCompra.FORNECEDOR.name()).length() > 0) {
                filter.setIdFornecedor(idFornecedor);
                listaCompra = filter.executeFilter();
                output.setValue("tabName", TipoPesquisaCompra.FORNECEDOR);
            }
            else if (input.getString(TipoPesquisaCompra.EMISSAO.name()) != null && input.getString(TipoPesquisaCompra.EMISSAO.name()).length() > 0) {
                filter.setDataEmissaoInicial(emissaoInicial);
                filter.setDataEmissaoFinal(emissaoFinal);
                listaCompra = filter.executeFilter();
                output.setValue("tabName", TipoPesquisaCompra.EMISSAO);
            }
            else if (input.getString(TipoPesquisaCompra.VENCIMENTO.name()) != null && input.getString(TipoPesquisaCompra.VENCIMENTO.name()).length() > 0) {
                filter.setDataVencimentoInicial(vencimentoInicial);
                filter.setDataVencimentoFinal(vencimentoFinal);
                listaCompra = filter.executeFilter();
                output.setValue("tabName", TipoPesquisaCompra.VENCIMENTO);
            }

            output.setValue("listaCompra", listaCompra);
        }
        catch (Exception ex) {
            addError(ConfigHelper.get().getString("error.general"));
            log.error(ConfigHelper.get().getString("error.general"), ex);
            return ERROR;
        }
        return SUCCESS;
    }

    @Consequences(outputs = @ConsequenceOutput(page = "/compras/cadastro.jsp"))
    public String cadastro() {
        try {
            long id = input.getLong("hid_id");
            Compra compra = new Compra(id);
            if (compra.getId() == null) {
                compra.setDataPedido(new Date());
                compra.setItens(new ArrayList<ItemCompra>());
            }

            session.setAttribute("compra", compra);
            output.setValue("foco", "txt_pedido");
            session.setAttribute("produtoVenda", false);
        }
        catch (Exception ex) {
            addError(ConfigHelper.get().getString("error.general"));
            log.error(ConfigHelper.get().getString("error.general"), ex);
            return ERROR;
        }
        return SUCCESS;
    }

    @Consequences(outputs = @ConsequenceOutput(page = "/compras/cadastro.jsp"))
    public String incluirItem() {
        try {
            Produto produto = cadastroServices.buscarProdutoPorReferencia(input.getString("txt_referencia_produto"));
            if (produto == null)
                return ERROR;

            Compra compra = (Compra)session.getAttribute("compra");

            // Verifica se item já foi incluído
            Iterator<ItemCompra> it = compra.getItens().iterator();
            while (it.hasNext()) {
                ItemCompra itemAtual = it.next();
                if (itemAtual.getProduto().getId().intValue() == produto.getId().intValue()) {
                    addError("txt_descricao_produto", ConfigHelper.getProperty("error.found", "Produto"));
                    return SUCCESS;
                }
            }

            ItemCompra item = new ItemCompra();
            item.setProduto(produto);
            item.setQuantidade(input.getInt("txt_quantidade"));
            item.setValorCompra(new Double(input.getString("txt_preco_produto").replace(".", "").replace(",", ".")));
            item.setCompra(compra);

            Long cfop = input.getLong("txt_id_cfop");
            if (cfop != null && cfop != -1)
                item.setCfop(new CFOP(cfop));

            double ipi = 0;
            if (!input.getString("txt_ipi").isEmpty())
                ipi = new Double(input.getString("txt_ipi").replace(".", "").replace(",", "."));
            double icms = 0;
            if (!input.getString("txt_icms").isEmpty())
                icms = new Double(input.getString("txt_icms").replace(".", "").replace(",", "."));

            if (ipi > 0 || icms > 0)
                item.setImpostos(new ArrayList<ImpostoCompra>());

            if (ipi > 0) {
                ImpostoCompra imposto = new ImpostoCompra();
                imposto.setImposto(Imposto.IPI);
                imposto.setAliquota(ipi);
                item.getImpostos().add(imposto);
            }
            if (icms > 0) {
                ImpostoCompra imposto = new ImpostoCompra();
                imposto.setImposto(Imposto.ICMS);
                imposto.setAliquota(icms);
                item.getImpostos().add(imposto);
            }

            compra.getItens().add(item);

            session.setAttribute("compra", compra);
            output.setValue("txt_referencia_produto", "");
            output.setValue("txt_descricao_produto", "");
            output.setValue("txt_quantidade", "");
            output.setValue("txt_id_cfop", "");
            output.setValue("txt_codigo_cfop", "");
            output.setValue("txt_descricao_cfop", "");
            output.setValue("txt_preco_produto", "");
            output.setValue("txt_icms", "");
            output.setValue("txt_ipi", "");
            output.setValue("foco", "txt_referencia_produto");
        }
        catch (Exception ex) {
            addError(ConfigHelper.get().getString("error.general"));
            log.error(ConfigHelper.get().getString("error.general"), ex);
            return ERROR;
        }
        return SUCCESS;
    }

    @Consequences(outputs = @ConsequenceOutput(page = "/compras/cadastro.jsp"))
    public String excluirItem() {
        try {
            int indice = input.getInt("txt_indice");
            Compra compra = (Compra)session.getAttribute("compra");
            if (indice > 0 && indice <= compra.getItens().size())
                ((List<ItemCompra>)compra.getItens()).remove(indice - 1);
            session.setAttribute("compra", compra);
            output.setValue("foco", "txt_referencia_produto");
        }
        catch (Exception ex) {
            addError(ConfigHelper.get().getString("error.general"));
            log.error(ConfigHelper.get().getString("error.general"), ex);
            return ERROR;
        }
        return SUCCESS;
    }

    @Consequences(outputs = { @ConsequenceOutput(result = SUCCESS, page = "/comum/mensagem.jsp"),
                    @ConsequenceOutput(result = ERROR, page = "/compras/cadastro.jsp") })
    public String gravar() {
        try {
            Compra compra = preencherCompra();

            compraServices.gravarPedidoCompra(compra);

            output.setValue("titulo", "Pedido de Compra");
            output.setValue("mensagem", ConfigHelper.getProperty("info.saveOk", "Pedido de Compra"));
            output.setValue("url", input.getProperty("contextPath") + "/compras/pedido.iniciar.action");
        }
        catch (ServiceValidationException ex) {
            addError(ex.getMessage());
            return ERROR;
        }
        catch (Exception ex) {
            addError(ConfigHelper.get().getString("error.general"));
            log.error(ConfigHelper.get().getString("error.general"), ex);
            return ERROR;
        }
        return SUCCESS;
    }

    @Consequences(outputs = { @ConsequenceOutput(result = SUCCESS, page = "/comum/mensagem.jsp"),
                    @ConsequenceOutput(result = ERROR, page = "/compras/cadastro.jsp") })
    public String alterar() {
        try {
            Compra compra = preencherCompra();

            compraServices.alterarCompraEmitida(compra);

            output.setValue("titulo", "Pedido de Compra");
            output.setValue("mensagem", ConfigHelper.getProperty("info.saveOk", "Pedido de Compra"));
            output.setValue("url", input.getProperty("contextPath") + "/compras/pedido.pesquisar.action");
        }
        catch (ServiceValidationException ex) {
            addError(ex.getMessage());
            return ERROR;
        }
        catch (Exception ex) {
            addError(ConfigHelper.get().getString("error.general"));
            log.error(ConfigHelper.get().getString("error.general"), ex);
            return ERROR;
        }
        return SUCCESS;
    }

    private Compra preencherCompra() {
        Compra compra = (Compra)session.getAttribute("compra");

        Fornecedor fornecedor = new Fornecedor(input.getLong("txt_id_fornecedor"));

        if (!compra.isEmitido())
            compra.setId(input.getLong("hid_id"));

        PrazoPagamento prazoPagamento = new PrazoPagamento(input.getLong("cbo_prazo_pagamento"));
        compra.setPrazoPagamento(prazoPagamento);

        compra.setPedido(input.getLong("txt_pedido"));
        compra.setDataPedido(input.getDate("txt_data", "dd/MM/yyyy"));
        compra.setPrevisaoEntrega(input.getDate("txt_previsao_entrega", "dd/MM/yyyy"));
        compra.setFornecedor(fornecedor);

        compra.setNotaFiscal(input.getLong("txt_nota_fiscal") > 0 ? input.getLong("txt_nota_fiscal") : null);
        compra.setEmissao(input.getDate("txt_emissao", "dd/MM/yyyy"));

        String frete = input.getString("cbo_frete");
        if (frete != null && !"".equals(frete))
            compra.setFrete(Frete.valueOf(frete));
        compra.setTransportador(input.getString("txt_transportador"));

        if (compra.getId() == null)
            compra.setId(-1l);

        compra.setEmpresa(((UsuarioLogadoWrapper)getUserSession()).getFuncionario().getEmpresa());

        // Atualizar, se for o caso, os preços dos produtos, digitados no grid
        for (ItemCompra item : compra.getItens()) {
            String valorCompraAtualizado = input.getString("txt_preco_produto_".concat(item.getProduto().getId().toString()));
            if (valorCompraAtualizado != null && !valorCompraAtualizado.isEmpty())
                item.setValorCompra(new Double(valorCompraAtualizado.replace(".", "").replace(",", ".")));
        }

        return compra;
    }

    @Consequences(outputs = @ConsequenceOutput(page = "/compras/pesquisar.jsp"))
    public String estornar() {
        try {
            Compra compra = new Compra(input.getLong("hid_id"));
            compraServices.estornarCompraEmitida(compra);
        }
        catch (ServiceValidationException vex) {
            addError(vex.getMessage());
            return ERROR;
        }
        catch (Exception ex) {
            addError(ConfigHelper.get().getString("error.general"));
            log.error(ConfigHelper.get().getString("error.general"), ex);
            return ERROR;
        }
        return pesquisar();
    }

    @Consequences(outputs = { @ConsequenceOutput(result = SUCCESS, page = "/comum/mensagem.jsp"),
                    @ConsequenceOutput(result = ERROR, page = "/compras/pesquisar.jsp") })
    public String excluir() {
        try {
            Compra compra = new Compra(input.getLong("hid_id"));
            compraServices.excluirPedidoCompra(compra);

            output.setValue("url", input.getProperty("contextPath") + "/compras/pedido.iniciar.action");
            output.setValue("titulo", "Exclusão de Compra");
            output.setValue("mensagem", ConfigHelper.getProperty("info.saveOk", "Compra"));
        }
        catch (Exception ex) {
            addError(ConfigHelper.get().getString("error.general"));
            log.error(ConfigHelper.get().getString("error.general"), ex);
            return ERROR;
        }
        return SUCCESS;
    }

    @Consequences(outputs = @ConsequenceOutput(page = "/compras/exibir.jsp"))
    public String exibir() {
        try {
            Compra compra = new Compra(input.getLong("txt_id"));
            output.setValue("compra", compra);
        }
        catch (Exception ex) {
            addError(ConfigHelper.get().getString("error.general"));
            log.error(ConfigHelper.get().getString("error.general"), ex);
        }
        return pesquisar();
    }

    @Consequences(outputs = { @ConsequenceOutput(result = SUCCESS, page = "/comum/mensagem.jsp"),
                    @ConsequenceOutput(result = ERROR, page = "/compras/cadastro.jsp") })
    public String emitir() {
        Compra compra = (Compra)session.getAttribute("compra");
        try {
            Fornecedor fornecedor = new Fornecedor(input.getLong("txt_id_fornecedor"));
            PrazoPagamento prazoPagamento = new PrazoPagamento(input.getLong("cbo_prazo_pagamento"));

            compra.setId(input.getLong("hid_id"));
            compra.setPedido(input.getLong("txt_pedido"));
            compra.setDataPedido(input.getDate("txt_data", "dd/MM/yyyy"));
            compra.setFornecedor(fornecedor);
            compra.setPrazoPagamento(prazoPagamento);

            compra.setNotaFiscal(input.getLong("txt_nota_fiscal") > 0 ? input.getLong("txt_nota_fiscal") : null);
            compra.setEmissao(input.getDate("txt_emissao", "dd/MM/yyyy"));

            String frete = input.getString("cbo_frete");
            if (frete != null && !"".equals(frete))
                compra.setFrete(Frete.valueOf(frete));
            compra.setTransportador(input.getString("txt_transportador"));

            if (compra.getId() == null)
                compra.setId(-1l);

            compra.setEmpresa(((UsuarioLogadoWrapper)getUserSession()).getFuncionario().getEmpresa());

            compraServices.emitirCompra(compra);

            output.setValue("titulo", "Nota Fiscal de Compras");
            output.setValue("mensagem", ConfigHelper.getProperty("info.saveOk", "Nota Fiscal de Compra"));
            output.setValue("url", input.getProperty("contextPath") + "/compras/pedido.iniciar.action");
        }
        catch (ServiceValidationException ex) {
            addError(ex.getMessage());
            compra.setEmissao(null);
            return ERROR;
        }
        catch (Exception ex) {
            addError(ConfigHelper.get().getString("error.general"));
            log.error(ConfigHelper.get().getString("error.general"), ex);
            compra.setEmissao(null);
            return ERROR;
        }

        return SUCCESS;
    }
}
