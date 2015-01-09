package br.com.alphadev.contest.actions.financeiro;import java.util.ArrayList;import java.util.Collection;import java.util.Date;import java.util.List;import org.mentawai.authorization.Authorizable;import org.mentawai.core.BaseAction;import org.mentawai.core.StreamConsequence;import org.mentawai.rule.RequiredFieldRule;import org.mentawai.validation.Validatable;import org.mentawai.validation.Validator;import br.com.alphadev.annotations.ActionClass;import br.com.alphadev.annotations.ConsequenceOutput;import br.com.alphadev.annotations.ConsequenceType;import br.com.alphadev.annotations.Consequences;import br.com.alphadev.contest.KGlobal;import br.com.alphadev.contest.KGlobal.TipoPesquisaReceber;import br.com.alphadev.contest.entity.Receber;import br.com.alphadev.contest.entity.Recebido;import br.com.alphadev.contest.entity.TipoConta;import br.com.alphadev.contest.entity.valueObject.FormaPagamento;import br.com.alphadev.contest.filters.VendaFilter;import br.com.alphadev.contest.services.FinanceiroService;import br.com.alphadev.core.ServicesFactory;import br.com.alphadev.exception.ServiceValidationException;import br.com.alphadev.util.ConfigHelper;import br.com.alphadev.util.Log4jWrapper;import br.com.alphadev.util.UsuarioLogadoWrapper;/** * @author Andre Penteado * @since 14/11/2007 - 17:50:16 */@ActionClass(prefix = "/financeiro/receber")public class ReceberAction extends BaseAction implements Authorizable, Validatable {    private Log4jWrapper log = new Log4jWrapper(ReceberAction.class, null);    private FinanceiroService financeiroService = null;    private void instanciarServicos(UsuarioLogadoWrapper userLogin) {        try {            log = new Log4jWrapper(ReceberAction.class, userLogin);            financeiroService = (FinanceiroService)ServicesFactory.getInstance(FinanceiroService.class, new Object[] { userLogin });        }        catch (Exception ex) {            log.fatal("A��ES N�O INSTANCIADAS: ".concat(ReceberAction.class.getName()), ex);        }    }    @SuppressWarnings("rawtypes")    @Override    public boolean authorize(String innerAction, Object user, List groups) {        if (user == null || groups == null)            return false;        instanciarServicos((UsuarioLogadoWrapper)user);        if (groups.indexOf(KGlobal.CATEGORIA_SUPERUSUARIO) != -1 || groups.indexOf(KGlobal.CATEGORIA_FINANCEIRO) != -1)            return true;        return false;    }    @Override    public void prepareValidator(Validator validator, String innerAction) {        if (innerAction.equals("gravar")) {            validator.add("txt_valor", new RequiredFieldRule(), ConfigHelper.getProperty("error.required", "Valor"));        }    }    @Consequences(outputs = @ConsequenceOutput(page = "/financeiro/receber/pesquisar.jsp"))    public String pesquisar() {        try {            Date emissaoInicial = input.getDate("txt_emissao_inicial", "dd/MM/yyyy");            Date emissaoFinal = input.getDate("txt_emissao_final", "dd/MM/yyyy");            Date vencimentoInicial = input.getDate("txt_vencimento_inicial", "dd/MM/yyyy");            Date vencimentoFinal = input.getDate("txt_vencimento_final", "dd/MM/yyyy");            int[] idsTiposConta = input.getInts("chk_tipos_conta");            long idCliente = input.getLong("txt_id_cliente");            long numero = input.getLong("txt_numero");            VendaFilter<Receber> filter = ServicesFactory.getInstance(VendaFilter.class, Receber.class);            Collection<Receber> listaReceber = new ArrayList<Receber>();            if (input.getString(TipoPesquisaReceber.NUMERO.name()) != null && input.getString(TipoPesquisaReceber.NUMERO.name()).length() > 0) {                filter.setNumeroVenda(numero);                listaReceber = filter.executeFilter();                output.setValue("tabName", TipoPesquisaReceber.NUMERO);            }            else if (input.getString(TipoPesquisaReceber.CLIENTE.name()) != null && input.getString(TipoPesquisaReceber.CLIENTE.name()).length() > 0) {                filter.setIdCliente(idCliente);                listaReceber = filter.executeFilter();                output.setValue("tabName", TipoPesquisaReceber.CLIENTE);            }            else if (input.getString(TipoPesquisaReceber.EMISSAO.name()) != null && input.getString(TipoPesquisaReceber.EMISSAO.name()).length() > 0) {                filter.setDataEmissaoInicial(emissaoInicial);                filter.setDataEmissaoFinal(emissaoFinal);                listaReceber = filter.executeFilter();                output.setValue("tabName", TipoPesquisaReceber.EMISSAO);            }            else if (input.getString(TipoPesquisaReceber.VENCIMENTO.name()) != null && input.getString(TipoPesquisaReceber.VENCIMENTO.name()).length() > 0) {                filter.setDataVencimentoInicial(vencimentoInicial);                filter.setDataVencimentoFinal(vencimentoFinal);                listaReceber = filter.executeFilter();                output.setValue("tabName", TipoPesquisaReceber.VENCIMENTO);            }            else if (input.getString(TipoPesquisaReceber.TIPO_CONTA.name()) != null && input.getString(TipoPesquisaReceber.TIPO_CONTA.name()).length() > 0) {                filter.setIdsTiposConta(idsTiposConta);                listaReceber = filter.executeFilter();                output.setValue("tabName", TipoPesquisaReceber.TIPO_CONTA);            }            else { // Listar contas em aberto                listaReceber = filter.executeFilter();            }            output.setValue("listaReceber", listaReceber);        }        catch (Exception ex) {            addError(ConfigHelper.get().getString("error.general"));            log.error(ConfigHelper.get().getString("error.general"), ex);            return ERROR;        }        return SUCCESS;    }    @Consequences(outputs = @ConsequenceOutput(page = "/financeiro/receber/cadastro.jsp"))    public String cadastro() {        try {            Receber receber = new Receber(input.getLong("txt_id"));            output.setValue("receber", receber);        }        catch (Exception ex) {            addError(ConfigHelper.get().getString("error.general"));            log.error(ConfigHelper.get().getString("error.general"), ex);            return ERROR;        }        return SUCCESS;    }    @Consequences(outputs = { @ConsequenceOutput(result = SUCCESS, page = "/comum/mensagem.jsp"),                    @ConsequenceOutput(result = ERROR, page = "/financeiro/receber/cadastro.jsp") })    public String gravar() {        try {            Receber receber = new Receber(input.getLong("hid_id"));            if (receber.getRecebimentos() == null)                receber.setRecebimentos(new ArrayList<Recebido>());            if (!"".equals(input.getString("txt_descricao")))                receber.setDescricao(input.getString("txt_descricao"));            TipoConta tipoConta = new TipoConta(input.getLong("cbo_tipo_conta"));            receber.setTipoConta(tipoConta.getId() != null ? tipoConta : null);            receber.setVencimento(input.getDate("txt_vencimento", "dd/MM/yyyy"));            receber.setNumeroDocumento(input.getString("txt_numero_documento"));            try {                if (!"".equals(input.getString("txt_valor")))                    receber.setValor(new Double(input.getString("txt_valor").replace(".", "").replace(",", ".")));            }            catch (Exception ex) {                addError("txt_valor", ConfigHelper.getProperty("error.invalid", "Valor"));                return ERROR;            }            receber.gravar();        }        catch (Exception ex) {            addError(ConfigHelper.get().getString("error.general"));            log.error(ConfigHelper.get().getString("error.general"), ex);            return ERROR;        }        output.setValue("titulo", "Contas a Receber");        output.setValue("mensagem", ConfigHelper.getProperty("info.saveOk", "Conta a Receber"));        output.setValue("url", input.getProperty("contextPath") + "/financeiro/receber.pesquisar.action");        return SUCCESS;    }    @Consequences(outputs = @ConsequenceOutput(page = "/financeiro/receber/baixar.jsp"))    public String darBaixa() {        try {            Receber receber = new Receber(input.getLong("txt_id"));            output.setValue("receber", receber);        }        catch (Exception ex) {            addError(ConfigHelper.get().getString("error.general"));            log.error(ConfigHelper.get().getString("error.general"), ex);        }        return SUCCESS;    }    @Consequences(outputs = { @ConsequenceOutput(result = SUCCESS, page = "/comum/mensagem.jsp"),                    @ConsequenceOutput(result = ERROR, page = "/financeiro/receber/baixar.jsp") })    public String baixar() {        try {            Receber receber = new Receber(input.getLong("hid_id"));            receber.setNumeroDocumento(input.getString("txt_numero_documento"));            receber.setObservacao(input.getString("txt_observacao"));            if (receber.getRecebimentos() == null)                receber.setRecebimentos(new ArrayList<Recebido>());            receber.gravar();            Recebido recebido = new Recebido();            recebido.setReceber(receber);            recebido.setFormaPagamento(FormaPagamento.valueOf(input.getString("cbo_forma_pagamento")));            recebido.setDataPagamento(input.getDate("txt_data_recebimento", "dd/MM/yyyy"));            try {                if (!"".equals(input.getString("txt_valor_recebido")))                    recebido.setValorPago(new Double(input.getString("txt_valor_recebido").replace(".", "").replace(",", ".")));            }            catch (Exception ex) {                addError("txt_valor_recebido", ConfigHelper.getProperty("error.invalid", "Valor Recebido"));                return ERROR;            }            recebido.gravar();        }        catch (Exception ex) {            addError(ConfigHelper.get().getString("error.general"));            log.error(ConfigHelper.get().getString("error.general"), ex);            return ERROR;        }        output.setValue("titulo", "Contas Recebida");        output.setValue("mensagem", ConfigHelper.getProperty("info.saveOk", "Conta Recebida"));        output.setValue("url", input.getProperty("contextPath") + "/financeiro/receber.pesquisar.action");        return SUCCESS;    }    @Consequences(outputs = { @ConsequenceOutput(result = SUCCESS, page = "/comum/mensagem.jsp"),                    @ConsequenceOutput(result = ERROR, page = "/financeiro/receber/pesquisar.jsp") })    public String baixarLote() {        try {            String[] ids = input.getStrings("chk_id_receber");            for (String id : ids) {                Receber receber = new Receber(new Long(id));                Recebido recebido = new Recebido();                recebido.setReceber(receber);                recebido.setFormaPagamento(receber.getVenda().getFormaPagamento());                recebido.setDataPagamento(receber.getVencimento());                recebido.setValorPago(receber.getValor());                recebido.gravar();            }        }        catch (Exception ex) {            addError(ConfigHelper.get().getString("error.general"));            log.error(ConfigHelper.get().getString("error.general"), ex);            return ERROR;        }        output.setValue("titulo", "Baixar em Lote");        output.setValue("mensagem", ConfigHelper.getProperty("info.saveOk", "Contas Recebidas"));        output.setValue("url", input.getProperty("contextPath") + "/financeiro/receber.pesquisar.action");        return SUCCESS;    }    @Consequences(outputs = { @ConsequenceOutput(result = SUCCESS, page = "application/text", type = ConsequenceType.STREAMCONSEQUENCE),                    @ConsequenceOutput(result = ERROR, page = "/financeiro/receber/pesquisar.jsp") })    public String gerarBoleto() {        try {            String[] ids = input.getStrings("chk_id_receber");            Receber[] recebimentos = new Receber[ids.length];            for (int i = 0; i < ids.length; i++) {                Receber receber = new Receber(new Long(ids[i]));                recebimentos[i] = receber;            }            String lote = financeiroService.gerarArquivoRemessaBoletoCNAB(recebimentos);            output.setValue(StreamConsequence.CONTENT_DISPOSITION, "attachment; filename=\"lote_boletos.rem" + "\"");            output.setValue(StreamConsequence.STREAM, lote.getBytes());            output.setValue(StreamConsequence.CONTENT_LENGTH, lote.getBytes().length);        }        catch (ServiceValidationException sve) {            addError(sve.getMessage());            return ERROR;        }        catch (Exception ex) {            addError(ConfigHelper.get().getString("error.general"));            log.error(ConfigHelper.get().getString("error.general"), ex);            return ERROR;        }        return SUCCESS;    }}