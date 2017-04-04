
package com.github.andrepenteado.contest.actions.cadastro;

import java.util.List;

import org.mentawai.authorization.Authorizable;
import org.mentawai.core.BaseAction;
import org.mentawai.list.ListManager;
import org.mentawai.list.SimpleListData;
import org.mentawai.rule.RequiredFieldRule;
import org.mentawai.validation.Validatable;
import org.mentawai.validation.Validator;

import com.github.andrepenteado.annotations.ActionClass;
import com.github.andrepenteado.annotations.ConsequenceOutput;
import com.github.andrepenteado.annotations.Consequences;
import com.github.andrepenteado.contest.KGlobal;
import com.github.andrepenteado.contest.entity.PrazoPagamento;
import com.github.andrepenteado.contest.services.CadastroService;
import com.github.andrepenteado.core.ServicesFactory;
import com.github.andrepenteado.util.ConfigHelper;
import com.github.andrepenteado.util.Log4jWrapper;
import com.github.andrepenteado.util.UsuarioLogadoWrapper;

/**
 * @author Andre Penteado
 * @since 12/08/2007 - 16:41:37
 */
@ActionClass(prefix = "/cadastros/prazoPagamento")
public class PrazoPagamentoAction extends BaseAction implements Authorizable, Validatable {

    private Log4jWrapper log = new Log4jWrapper(PrazoPagamentoAction.class, null);
    private CadastroService cadastroServices = null;

    private void instanciarServicos(UsuarioLogadoWrapper userLogin) {
        try {
            cadastroServices = (CadastroService)ServicesFactory.getInstance(CadastroService.class, new Object[] { userLogin });
            log = new Log4jWrapper(PrazoPagamentoAction.class, userLogin);
        }
        catch (Exception ex) {
            log.fatal("AÇÕES NÃO INSTANCIADAS: ".concat(PrazoPagamentoAction.class.getName()), ex);
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

        return false;
    }

    public void prepareValidator(Validator validator, String innerAction) {
        if (innerAction.equals("gravar")) {
            validator.add("txt_descricao", new RequiredFieldRule(), ConfigHelper.getProperty("error.required", "Descrição"));
        }
    }

    @Consequences(outputs = @ConsequenceOutput(page = "/cadastros/prazo_pagamento/pesquisar.jsp"))
    public String pesquisar() {
        try {
            List<PrazoPagamento> listaPrazosPagamentos = cadastroServices.listarPrazosPagamentos();
            output.setValue("listaPrazosPagamentos", listaPrazosPagamentos);
        }
        catch (Exception ex) {
            addError(ConfigHelper.get().getString("error.general"));
            log.error(ConfigHelper.get().getString("error.general"), ex);
            return ERROR;
        }
        return SUCCESS;
    }

    @Consequences(outputs = @ConsequenceOutput(page = "/cadastros/prazo_pagamento/cadastro.jsp"))
    public String cadastro() {
        try {
            long id = input.getLong("txt_id");
            PrazoPagamento prazoPagamento = new PrazoPagamento(id);
            output.setValue("prazoPagamento", prazoPagamento);
        }
        catch (Exception ex) {
            addError(ConfigHelper.get().getString("error.general"));
            log.error(ConfigHelper.get().getString("error.general"), ex);
            return ERROR;
        }
        return SUCCESS;
    }

    @Consequences(outputs = { @ConsequenceOutput(result = SUCCESS, page = "/comum/mensagem.jsp"),
                    @ConsequenceOutput(result = ERROR, page = "/cadastros/prazo_pagamento/cadastro.jsp") })
    public String gravar() {
        try {
            PrazoPagamento prazoPagamento = new PrazoPagamento(input.getLong("hid_id"));
            prazoPagamento.setId(input.getLong("hid_id"));
            prazoPagamento.setDescricao(input.getString("txt_descricao"));
            prazoPagamento.setParcelas(input.getString("txt_parcelas"));
            prazoPagamento.setObservacao(input.getString("txt_observacao"));
            // Desconto / Juros
            try {
                if (!"".equals(input.getString("txt_desconto_juros")))
                    prazoPagamento.setDescontoJuros(new Double(input.getString("txt_desconto_juros").replace(".", "").replace(",", ".")));
            }
            catch (Exception ex) {
                addError("txt_desconto_juros", ConfigHelper.getProperty("error.invalid", "Desconto / Juros"));
                return ERROR;
            }
            prazoPagamento.gravar();
        }
        catch (Exception ex) {
            addError(ConfigHelper.get().getString("error.general"));
            log.error(ConfigHelper.get().getString("error.general"), ex);
            return ERROR;
        }

        SimpleListData listaPrazosPagamentos = new SimpleListData("listaPrazosPagamentos");
        for (PrazoPagamento prazoPagamento : cadastroServices.listarPrazosPagamentos())
            listaPrazosPagamentos.add(prazoPagamento.getId().intValue(), prazoPagamento.getDescricao());
        ListManager.addList(listaPrazosPagamentos);

        output.setValue("titulo", "Cadastro de Prazos de Pagamentos");
        output.setValue("mensagem", ConfigHelper.getProperty("info.saveOk", "Prazo de Pagamento"));
        output.setValue("url", input.getProperty("contextPath") + "/cadastros/prazoPagamento.pesquisar.action");
        return SUCCESS;
    }

    @Consequences(outputs = @ConsequenceOutput(page = "/cadastros/prazo_pagamento/pesquisar.jsp"))
    public String excluir() {
        try {
            PrazoPagamento prazoPagamento = new PrazoPagamento(input.getLong("txt_id"));
            if (prazoPagamento != null && prazoPagamento.getId() != null)
                prazoPagamento.excluir();
        }
        catch (Exception ex) {
            addError(ConfigHelper.get().getString("error.general"));
            log.error(ConfigHelper.get().getString("error.general"), ex);
            return ERROR;
        }
        return SUCCESS;
    }
}
