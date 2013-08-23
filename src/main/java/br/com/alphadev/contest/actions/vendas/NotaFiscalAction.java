
package br.com.alphadev.contest.actions.vendas;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.mentawai.authorization.Authorizable;
import org.mentawai.core.BaseAction;
import org.mentawai.core.StreamConsequence;

import br.com.alphadev.annotations.ActionClass;
import br.com.alphadev.annotations.ConsequenceOutput;
import br.com.alphadev.annotations.ConsequenceType;
import br.com.alphadev.annotations.Consequences;
import br.com.alphadev.contest.KGlobal;
import br.com.alphadev.contest.KGlobal.TipoPesquisaVenda;
import br.com.alphadev.contest.entity.NotaFiscal;
import br.com.alphadev.contest.entity.Orcamento;
import br.com.alphadev.contest.entity.Venda;
import br.com.alphadev.contest.entity.valueObject.Frete;
import br.com.alphadev.contest.filters.VendaFilter;
import br.com.alphadev.contest.services.VendaService;
import br.com.alphadev.core.ServicesFactory;
import br.com.alphadev.exception.ServiceValidationException;
import br.com.alphadev.util.ConfigHelper;
import br.com.alphadev.util.Log4jWrapper;
import br.com.alphadev.util.UsuarioLogadoWrapper;

/**
 * @author Andre Penteado
 * @since 12/09/2007 - 21:25:42
 */
@ActionClass(prefix = "/vendas/notaFiscal")
public class NotaFiscalAction extends BaseAction implements Authorizable {

    private Log4jWrapper log = new Log4jWrapper(NotaFiscalAction.class, null);
    private VendaService vendaService = null;

    private void instanciarServicos(UsuarioLogadoWrapper userLogin) {
        try {
            vendaService = (VendaService)ServicesFactory.getInstance(VendaService.class, new Object[] { userLogin });
            log = new Log4jWrapper(NotaFiscalAction.class, userLogin);
        }
        catch (Exception ex) {
            log.fatal("AÇÕES NÃO INSTANCIADAS: ".concat(NotaFiscalAction.class.getName()), ex);
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

    @Consequences(outputs = @ConsequenceOutput(page = "/vendas/nota_fiscal/pesquisar.jsp"))
    public String pesquisar() {
        try {
            UsuarioLogadoWrapper userLogin = (UsuarioLogadoWrapper)getUserSession();
            Date emissaoInicial = input.getDate("txt_emissao_inicial", "dd/MM/yyyy");
            Date emissaoFinal = input.getDate("txt_emissao_final", "dd/MM/yyyy");
            Date vencimentoInicial = input.getDate("txt_vencimento_inicial", "dd/MM/yyyy");
            Date vencimentoFinal = input.getDate("txt_vencimento_final", "dd/MM/yyyy");
            long idCliente = input.getLong("txt_id_cliente");
            long numero = input.getLong("txt_numero");

            VendaFilter<NotaFiscal> filter = ServicesFactory.getInstance(VendaFilter.class, NotaFiscal.class);
            Collection<NotaFiscal> listaNotaFiscal = new ArrayList<NotaFiscal>();

            if (userLogin.getCategoria().equals(KGlobal.CATEGORIA_VENDEDOR))
                filter.setIdVendedor(userLogin.getFuncionario().getId());

            // Filtrar ambas, canceladas e não canceladas
            filter.setCancelada(null);

            if (input.getString(TipoPesquisaVenda.NUMERO.name()) != null && input.getString(TipoPesquisaVenda.NUMERO.name()).length() > 0) {
                filter.setNumeroVenda(numero);
                listaNotaFiscal = filter.executeFilter();
                output.setValue("tabName", TipoPesquisaVenda.NUMERO);
            }
            else if (input.getString(TipoPesquisaVenda.CLIENTE.name()) != null && input.getString(TipoPesquisaVenda.CLIENTE.name()).length() > 0) {
                filter.setIdCliente(idCliente);
                listaNotaFiscal = filter.executeFilter();
                output.setValue("tabName", TipoPesquisaVenda.CLIENTE);
            }
            else if (input.getString(TipoPesquisaVenda.EMISSAO.name()) != null && input.getString(TipoPesquisaVenda.EMISSAO.name()).length() > 0) {
                filter.setDataEmissaoInicial(emissaoInicial);
                filter.setDataEmissaoFinal(emissaoFinal);
                listaNotaFiscal = filter.executeFilter();
                output.setValue("tabName", TipoPesquisaVenda.EMISSAO);
            }
            else if (input.getString(TipoPesquisaVenda.VENCIMENTO.name()) != null
                            && input.getString(TipoPesquisaVenda.VENCIMENTO.name()).length() > 0) {
                filter.setDataVencimentoInicial(vencimentoInicial);
                filter.setDataVencimentoFinal(vencimentoFinal);
                listaNotaFiscal = filter.executeFilter();
                output.setValue("tabName", TipoPesquisaVenda.VENCIMENTO);
            }

            output.setValue("listaNotaFiscal", listaNotaFiscal);
        }
        catch (Exception ex) {
            addError(ConfigHelper.get().getString("error.general"));
            log.error(ConfigHelper.get().getString("error.general"), ex);
            return ERROR;
        }
        return SUCCESS;
    }

    @Consequences(outputs = { @ConsequenceOutput(result = SUCCESS, page = "/vendas/nota_fiscal/emitir.jsp"),
                    @ConsequenceOutput(result = ERROR, page = "/vendas/pedido/pesquisar.jsp") })
    public String emitir() {
        long id = input.getLong("hid_id");
        Orcamento orcamento = new Orcamento(id);
        NotaFiscal nf = new NotaFiscal(id);

        if (nf.getId() != null || orcamento.getId() != null) {
            addError("Venda já foi emitida");
            return ERROR;
        }

        Venda venda = new Venda(id);
        String avisos = vendaService.verificarAvisosVenda(venda);
        if (avisos != null && avisos.length() > 0)
            addMessage(avisos);

        output.setValue("hid_id", input.getValue("hid_id"));
        output.setValue("txt_numero", vendaService.buscarProximoNumeroNotaFiscal());
        output.setValue("txt_icms", "18");
        return SUCCESS;
    }

    @Consequences(outputs = @ConsequenceOutput(page = "/vendas/nota_fiscal/pesquisar.jsp"))
    public String cancelar() {
        try {
            NotaFiscal notaFiscal = new NotaFiscal(input.getLong("hid_id"));
            vendaService.cancelarNotaFiscal(notaFiscal);
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
            NotaFiscal notaFiscal = new NotaFiscal(input.getLong("txt_id"));
            output.setValue("venda", notaFiscal.getVenda());
        }
        catch (Exception ex) {
            addError(ConfigHelper.get().getString("error.general"));
            log.error(ConfigHelper.get().getString("error.general"), ex);
            return ERROR;
        }
        return pesquisar();
    }

    @Consequences(outputs = @ConsequenceOutput(page = "/vendas/nota_fiscal/pesquisar.jsp"))
    public String estornar() {
        try {
            NotaFiscal notaFiscal = new NotaFiscal(input.getLong("hid_id"));
            vendaService.estornarNotaFiscal(notaFiscal);
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

    @Consequences(outputs = { @ConsequenceOutput(result = SUCCESS, page = "application/pdf", type = ConsequenceType.STREAMCONSEQUENCE),
                    @ConsequenceOutput(result = ERROR, page = "/vendas/nota_fiscal/emitir.jsp") })
    public String gravar() {
        try {
            NotaFiscal nf = new NotaFiscal();
            Venda venda = new Venda(input.getLong("hid_id"));
            nf.setVenda(venda);
            nf.setId(venda.getId());
            nf.setEmissao(new Date());
            nf.setNumero(input.getLong("txt_numero"));
            nf.setFrete(Frete.valueOf(input.getString("cbo_frete")));
            nf.setTransportador(input.getString("txt_transportador"));
            nf.setObservacao(input.getString("txt_observacao"));

            byte[] saida = vendaService.emitirNotaFiscalVenda(nf);
            if (nf.getVenda().getEmpresa().getConfiguracao().getSituacaoNfe().intValue() > 0)
                output.setValue(StreamConsequence.CONTENT_DISPOSITION, "attachment; filename=\"nfe_" + nf.getNumero() + ".txt" + "\"");
            else
                output.setValue(StreamConsequence.CONTENT_DISPOSITION, "attachment; filename=\"nf_" + nf.getNumero() + ".pdf" + "\"");
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
