
package com.github.andrepenteado.contest.actions.vendas;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.mentawai.authorization.Authorizable;
import org.mentawai.core.BaseAction;
import org.mentawai.core.StreamConsequence;

import com.github.andrepenteado.annotations.ActionClass;
import com.github.andrepenteado.annotations.ConsequenceOutput;
import com.github.andrepenteado.annotations.ConsequenceType;
import com.github.andrepenteado.annotations.Consequences;
import com.github.andrepenteado.contest.KGlobal;
import com.github.andrepenteado.contest.KGlobal.TipoPesquisaVenda;
import com.github.andrepenteado.contest.entity.NotaFiscal;
import com.github.andrepenteado.contest.entity.Orcamento;
import com.github.andrepenteado.contest.entity.Venda;
import com.github.andrepenteado.contest.filters.VendaFilter;
import com.github.andrepenteado.contest.services.VendaService;
import com.github.andrepenteado.core.ServicesFactory;
import com.github.andrepenteado.exception.ServiceValidationException;
import com.github.andrepenteado.util.ConfigHelper;
import com.github.andrepenteado.util.Log4jWrapper;
import com.github.andrepenteado.util.UsuarioLogadoWrapper;

/**
 * @author Andre Penteado
 * @since 12/09/2007 - 21:25:42
 */
@ActionClass(prefix = "/vendas/orcamento")
public class OrcamentoAction extends BaseAction implements Authorizable {

    private Log4jWrapper log = new Log4jWrapper(OrcamentoAction.class, null);
    private VendaService vendaServices = null;

    private void instanciarServicos(UsuarioLogadoWrapper userLogin) {
        try {
            vendaServices = (VendaService)ServicesFactory.getInstance(VendaService.class, new Object[] { userLogin });
            log = new Log4jWrapper(OrcamentoAction.class, userLogin);
        }
        catch (Exception ex) {
            log.fatal("AÇÕES NÃO INSTANCIADAS: ".concat(OrcamentoAction.class.getName()), ex);
        }
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean authorize(String innerAction, Object user, List groups) {
        if (user == null || groups == null)
            return false;

        instanciarServicos((UsuarioLogadoWrapper)user);

        if (groups.indexOf(KGlobal.CATEGORIA_SUPERUSUARIO) != -1 || groups.indexOf(KGlobal.CATEGORIA_VENDAS) != -1
                        || groups.indexOf(KGlobal.CATEGORIA_VENDEDOR) != -1)
            return true;

        return false;
    }

    @Consequences(outputs = @ConsequenceOutput(page = "/vendas/orcamento/pesquisar.jsp"))
    public String pesquisar() {
        try {
            UsuarioLogadoWrapper userLogin = (UsuarioLogadoWrapper)getUserSession();
            Date emissaoInicial = input.getDate("txt_emissao_inicial", "dd/MM/yyyy");
            Date emissaoFinal = input.getDate("txt_emissao_final", "dd/MM/yyyy");
            Date vencimentoInicial = input.getDate("txt_vencimento_inicial", "dd/MM/yyyy");
            Date vencimentoFinal = input.getDate("txt_vencimento_final", "dd/MM/yyyy");
            long idCliente = input.getLong("txt_id_cliente");
            long numero = input.getLong("txt_numero");

            VendaFilter<Orcamento> filter = ServicesFactory.getInstance(VendaFilter.class, Orcamento.class);
            Collection<Orcamento> listaOrcamentos = new ArrayList<Orcamento>();

            if (userLogin.getCategoria().equals(KGlobal.CATEGORIA_VENDEDOR))
                filter.setIdVendedor(userLogin.getFuncionario().getId());

            if (input.getString(TipoPesquisaVenda.NUMERO.name()) != null && input.getString(TipoPesquisaVenda.NUMERO.name()).length() > 0) {
                filter.setNumeroVenda(numero);
                listaOrcamentos = filter.executeFilter();
                output.setValue("tabName", TipoPesquisaVenda.NUMERO);
            }
            else if (input.getString(TipoPesquisaVenda.CLIENTE.name()) != null && input.getString(TipoPesquisaVenda.CLIENTE.name()).length() > 0) {
                filter.setIdCliente(idCliente);
                listaOrcamentos = filter.executeFilter();
                output.setValue("tabName", TipoPesquisaVenda.CLIENTE);
            }
            else if (input.getString(TipoPesquisaVenda.EMISSAO.name()) != null && input.getString(TipoPesquisaVenda.EMISSAO.name()).length() > 0) {
                filter.setDataEmissaoInicial(emissaoInicial);
                filter.setDataEmissaoFinal(emissaoFinal);
                listaOrcamentos = filter.executeFilter();
                output.setValue("tabName", TipoPesquisaVenda.EMISSAO);
            }
            else if (input.getString(TipoPesquisaVenda.VENCIMENTO.name()) != null
                            && input.getString(TipoPesquisaVenda.VENCIMENTO.name()).length() > 0) {
                filter.setDataVencimentoInicial(vencimentoInicial);
                filter.setDataVencimentoFinal(vencimentoFinal);
                listaOrcamentos = filter.executeFilter();
                output.setValue("tabName", TipoPesquisaVenda.VENCIMENTO);
            }

            output.setValue("listaOrcamentos", listaOrcamentos);
        }
        catch (Exception ex) {
            addError(ConfigHelper.get().getString("error.general"));
            log.error(ConfigHelper.get().getString("error.general"), ex);
            return ERROR;
        }
        return SUCCESS;
    }

    @Consequences(outputs = { @ConsequenceOutput(result = SUCCESS, page = "/vendas/orcamento/emitir.jsp"),
                    @ConsequenceOutput(result = ERROR, page = "/vendas/pedido/pesquisar.jsp") })
    public String emitir() {
        long id = input.getLong("hid_id");
        Orcamento orcamento = new Orcamento(id);
        NotaFiscal nf = new NotaFiscal(id);
        Venda venda = new Venda(id);

        if (nf.getId() != null || orcamento.getId() != null) {
            addError("Venda já foi emitida");
            return ERROR;
        }

        if (venda.getItens().size() > 8)
            addMessage("Emitir orçamento em 2 folhas (2 cópias)");

        String avisos = vendaServices.verificarAvisosVenda(venda);
        if (avisos != null && avisos.length() > 0)
            addMessage(avisos);

        output.setValue("hid_id", input.getValue("hid_id"));
        return SUCCESS;
    }

    @Consequences(outputs = @ConsequenceOutput(page = "/vendas/orcamento/pesquisar.jsp"))
    public String estornar() {
        try {
            Orcamento orcamento = new Orcamento(input.getLong("hid_id"));
            vendaServices.estornarOrcamento(orcamento);
        }
        catch (ServiceValidationException vex) {
            addError(vex.getMessage());
        }
        catch (Exception ex) {
            addError(ConfigHelper.get().getString("error.general"));
            log.error(ConfigHelper.get().getString("error.general"), ex);
            return ERROR;
        }
        return pesquisar();
    }

    @Consequences(outputs = @ConsequenceOutput(page = "/vendas/pedido/exibir.jsp"))
    public String exibir() {
        try {
            Orcamento orcamento = new Orcamento(input.getLong("txt_id"));
            output.setValue("venda", orcamento.getVenda());
        }
        catch (Exception ex) {
            addError(ConfigHelper.get().getString("error.general"));
            log.error(ConfigHelper.get().getString("error.general"), ex);
            return ERROR;
        }
        return pesquisar();
    }

    @Consequences(outputs = { @ConsequenceOutput(result = SUCCESS, page = "application/pdf", type = ConsequenceType.STREAMCONSEQUENCE),
                    @ConsequenceOutput(result = ERROR, page = "/vendas/orcamento/emitir.jsp") })
    public String gravar() {
        try {
            Orcamento orcamento = new Orcamento();
            Venda venda = new Venda(input.getLong("hid_id"));
            orcamento.setVenda(venda);
            orcamento.setId(venda.getId());
            orcamento.setEmissao(new Date());
            orcamento.setTransportador(input.getString("txt_transportador"));
            orcamento.setObservacao(input.getString("txt_observacao"));

            byte[] saida = vendaServices.emitirOrcamentoVenda(orcamento);
            output.setValue(StreamConsequence.CONTENT_DISPOSITION, "attachment; filename=\"orcamento_" + orcamento.getId() + ".pdf" + "\"");
            output.setValue(StreamConsequence.STREAM, saida);
            output.setValue(StreamConsequence.CONTENT_LENGTH, saida.length);
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
        return SUCCESS;
    }
}
