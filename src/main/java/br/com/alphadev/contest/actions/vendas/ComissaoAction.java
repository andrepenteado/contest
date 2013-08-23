
package br.com.alphadev.contest.actions.vendas;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.mentawai.authorization.Authorizable;
import org.mentawai.core.BaseAction;

import br.com.alphadev.annotations.ActionClass;
import br.com.alphadev.annotations.ConsequenceOutput;
import br.com.alphadev.annotations.Consequences;
import br.com.alphadev.contest.KGlobal;
import br.com.alphadev.contest.KGlobal.TipoPesquisaVenda;
import br.com.alphadev.contest.entity.Comissao;
import br.com.alphadev.contest.entity.Funcionario;
import br.com.alphadev.contest.entity.valueObject.VistaPrazo;
import br.com.alphadev.contest.filters.VendaFilter;
import br.com.alphadev.contest.services.ComissaoService;
import br.com.alphadev.core.ServicesFactory;
import br.com.alphadev.util.ConfigHelper;
import br.com.alphadev.util.FunctionsHelper;
import br.com.alphadev.util.Log4jWrapper;
import br.com.alphadev.util.UsuarioLogadoWrapper;

@ActionClass(prefix = "/vendas/comissao")
public class ComissaoAction extends BaseAction implements Authorizable {

    private Log4jWrapper log = new Log4jWrapper(ComissaoAction.class, null);
    private ComissaoService comissaoServices = null;

    private void instanciarServicos(UsuarioLogadoWrapper userLogin) {
        try {
            comissaoServices = (ComissaoService)ServicesFactory.getInstance(ComissaoService.class, new Object[] { userLogin });
            log = new Log4jWrapper(ComissaoAction.class, userLogin);
        }
        catch (Exception ex) {
            log.fatal("AÇÕES NÃO INSTANCIADAS: ".concat(ComissaoAction.class.getName()), ex);
        }
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean authorize(String innerAction, Object user, List groups) {
        if (user == null || groups == null)
            return false;

        instanciarServicos((UsuarioLogadoWrapper)user);

        if (groups.indexOf(KGlobal.CATEGORIA_SUPERUSUARIO) != -1 || groups.indexOf(KGlobal.CATEGORIA_ADMINISTRATIVO) != -1)
            return true;

        if (("iniciarPendente".equals(innerAction) || "pendente".equals(innerAction) || "iniciarPaga".equals(innerAction)
                        || "paga".equals(innerAction) || "detalharPagas".equals(innerAction))
                        && groups.indexOf(KGlobal.CATEGORIA_VENDEDOR) != -1)
            return true;

        return false;
    }

    @Consequences(outputs = @ConsequenceOutput(page = "/vendas/comissao/pesquisar.jsp"))
    public String pesquisar() {
        try {
            Date emissaoInicial = input.getDate("txt_emissao_inicial", "dd/MM/yyyy");
            Date emissaoFinal = input.getDate("txt_emissao_final", "dd/MM/yyyy");
            Date vencimentoInicial = input.getDate("txt_vencimento_inicial", "dd/MM/yyyy");
            Date vencimentoFinal = input.getDate("txt_vencimento_final", "dd/MM/yyyy");
            long idCliente = input.getLong("txt_id_cliente");
            long numero = input.getLong("txt_numero");

            VendaFilter<Comissao> filter = ServicesFactory.getInstance(VendaFilter.class, Comissao.class);
            Collection<Comissao> listaComissao = new ArrayList<Comissao>();

            if (input.getString(TipoPesquisaVenda.NUMERO.name()) != null && input.getString(TipoPesquisaVenda.NUMERO.name()).length() > 0) {
                filter.setNumeroVenda(numero);
                listaComissao = filter.executeFilter();
                output.setValue("tabName", TipoPesquisaVenda.NUMERO);
            }
            else if (input.getString(TipoPesquisaVenda.CLIENTE.name()) != null && input.getString(TipoPesquisaVenda.CLIENTE.name()).length() > 0) {
                filter.setIdCliente(idCliente);
                listaComissao = filter.executeFilter();
                output.setValue("tabName", TipoPesquisaVenda.CLIENTE);
            }
            else if (input.getString(TipoPesquisaVenda.EMISSAO.name()) != null && input.getString(TipoPesquisaVenda.EMISSAO.name()).length() > 0) {
                filter.setDataEmissaoInicial(emissaoInicial);
                filter.setDataEmissaoFinal(emissaoFinal);
                listaComissao = filter.executeFilter();
                output.setValue("tabName", TipoPesquisaVenda.EMISSAO);
            }
            else if (input.getString(TipoPesquisaVenda.VENCIMENTO.name()) != null
                            && input.getString(TipoPesquisaVenda.VENCIMENTO.name()).length() > 0) {
                filter.setDataVencimentoInicial(vencimentoInicial);
                filter.setDataVencimentoFinal(vencimentoFinal);
                listaComissao = filter.executeFilter();
                output.setValue("tabName", TipoPesquisaVenda.VENCIMENTO);
            }

            output.setValue("listaComissao", listaComissao);
        }
        catch (Exception ex) {
            addError(ConfigHelper.get().getString("error.general"));
            log.error(ConfigHelper.get().getString("error.general"), ex);
            return ERROR;
        }
        return SUCCESS;
    }

    @Consequences(outputs = @ConsequenceOutput(page = "/vendas/pedido/exibir.jsp"))
    public String exibir() {
        try {
            Comissao comissao = new Comissao(input.getLong("txt_id"));
            output.setValue("venda", comissao.getVenda());
        }
        catch (Exception ex) {
            addError(ConfigHelper.get().getString("error.general"));
            log.error(ConfigHelper.get().getString("error.general"), ex);
            return ERROR;
        }
        return pesquisar();
    }

    @Consequences(outputs = @ConsequenceOutput(page = "/vendas/comissao/cadastro.jsp"))
    public String cadastro() {
        try {
            Comissao comissao = new Comissao(input.getLong("txt_id"));
            output.setValue("comissao", comissao);
        }
        catch (Exception ex) {
            addError(ConfigHelper.get().getString("error.general"));
            log.error(ConfigHelper.get().getString("error.general"), ex);
            return ERROR;
        }
        return SUCCESS;
    }

    @Consequences(outputs = { @ConsequenceOutput(result = SUCCESS, page = "/comum/mensagem.jsp"),
                    @ConsequenceOutput(result = ERROR, page = "/vendas/comissao/cadastro.jsp") })
    public String gravar() {
        try {
            Comissao comissao = new Comissao(input.getLong("hid_id"));
            if (!"".equals(input.getString("txt_descricao")))
                comissao.setDescricao(input.getString("txt_descricao"));
            Funcionario funcionario = new Funcionario(input.getLong("cbo_vendedor"));
            if (funcionario.getId() == null)
                addError("cbo_vendedor", ConfigHelper.getProperty("error.required", "Vendedor"));
            comissao.getVenda().setFuncionario(funcionario);
            comissao.setDataPagamento(input.getDate("txt_data_pagamento", "dd/MM/yyyy"));
            try {
                if (!"".equals(input.getString("txt_valor")))
                    comissao.setValor(new Double(input.getString("txt_valor").replace(".", "").replace(",", ".")));
            }
            catch (Exception ex) {
                addError("txt_valor", ConfigHelper.getProperty("error.invalid", "Valor"));
                return ERROR;
            }
            comissao.setObservacao(input.getString("txt_observacao"));
            comissao.gravar();
        }
        catch (Exception ex) {
            addError(ConfigHelper.get().getString("error.general"));
            log.error(ConfigHelper.get().getString("error.general"), ex);
            return ERROR;
        }

        output.setValue("titulo", "Comissão");
        output.setValue("mensagem", ConfigHelper.getProperty("info.saveOk", "Comissão de Vendedor"));
        output.setValue("url", input.getProperty("contextPath") + "/vendas/comissao.pesquisar.action");
        return SUCCESS;
    }

    @Consequences(outputs = @ConsequenceOutput(page = "/vendas/comissao/pendente.jsp"))
    public String iniciarPendente() {
        return SUCCESS;
    }

    @Consequences(outputs = @ConsequenceOutput(page = "/vendas/comissao/paga.jsp"))
    public String iniciarPaga() {
        return SUCCESS;
    }

    @Consequences(outputs = @ConsequenceOutput(page = "/vendas/comissao/pendente.jsp"))
    public String pendente() {
        VistaPrazo vistaPrazo = input.getString("cbo_vista_prazo") != null && !"".equals(input.getString("cbo_vista_prazo")) ? VistaPrazo
                        .valueOf(input.getString("cbo_vista_prazo")) : null;

        Funcionario funcionario = new Funcionario(input.getLong("txt_id_funcionario"));
        if (funcionario == null || funcionario.getId() == null)
            return SUCCESS;

        List<Comissao> comissoesPendentes = comissaoServices.pesquisarComissoesPendentes(funcionario, vistaPrazo);

        // Calcular valor da comissão
        double totalComissao = 0d;
        for (Comissao comissao : comissoesPendentes)
            totalComissao += comissao.getValor().doubleValue();

        // Colunas do relatório
        String[] colunas = input.getStrings("chk_colunas_pesquisa");
        if (colunas != null && colunas.length > 0)
            for (String col : colunas)
                output.setValue(col, '1');

        output.setValue("totalComissao", totalComissao);
        output.setValue("comissoesPendentes", comissoesPendentes);

        return SUCCESS;
    }

    @Consequences(outputs = @ConsequenceOutput(page = "/comum/mensagem.jsp"))
    public String pagamento() {
        int[] idsComissao = input.getInts("chk_id_comissao");

        if (idsComissao != null && idsComissao.length > 0) {
            for (long id : idsComissao) {
                double valor = new Double(input.getString("txt_valor_" + id).replace(".", "").replace(",", "."));
                Comissao comissao = new Comissao(id);
                comissao.setValor(valor);
                comissao.setDataPagamento(new Date());
                comissao.gravar();
            }
            output.setValue("mensagem", ConfigHelper.getProperty("info.saveOk", "Pagamento de Comissão"));
        }
        else {
            output.setValue("mensagem", ConfigHelper.getProperty("error.notSelected", "Pagamento de Comissão"));
        }

        output.setValue("titulo", "Comissão de Funcionário");
        output.setValue("url", input.getProperty("contextPath") + "/vendas/comissao.iniciarPendente.action");
        return SUCCESS;
    }

    @Consequences(outputs = @ConsequenceOutput(page = "/vendas/comissao/paga.jsp"))
    public String paga() {
        long idFuncionario = input.getLong("txt_id_funcionario");
        Funcionario funcionario = new Funcionario(idFuncionario);
        List<Object[]> listaComissoesPagas = comissaoServices.pesquisarComissoesPagas(funcionario);
        output.setValue("listaComissoesPagas", listaComissoesPagas);
        return SUCCESS;
    }

    @Consequences(outputs = @ConsequenceOutput(page = "/vendas/comissao/detalhar_pagas.jsp"))
    public String detalharPagas() {
        long idFuncionario = input.getLong("txt_id_funcionario");
        Funcionario funcionario = new Funcionario(idFuncionario);
        Date dataPagamento = input.getDate("hid_data_pagamento", "dd/MM/yyyy");

        output.setValue("listaComissao", comissaoServices.pesquisarComissoesPagasPorDataPagamento(funcionario, dataPagamento));
        output.setValue("hid_data_pagamento", FunctionsHelper.dateFormat(dataPagamento, "dd/MM/yyyy"));
        output.setValue("txt_id_funcionario", idFuncionario);

        // Colunas do relatório
        String[] colunas = input.getStrings("chk_colunas_pesquisa");
        if (colunas != null && colunas.length > 0)
            for (String col : colunas)
                output.setValue(col, '1');

        return SUCCESS;
    }
}
