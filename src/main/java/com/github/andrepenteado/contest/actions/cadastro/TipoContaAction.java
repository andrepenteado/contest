
package com.github.andrepenteado.contest.actions.cadastro;

import java.util.List;

import org.mentawai.authorization.Authorizable;
import org.mentawai.core.BaseAction;
import org.mentawai.rule.RequiredFieldRule;
import org.mentawai.validation.Validatable;
import org.mentawai.validation.Validator;

import com.github.andrepenteado.annotations.ActionClass;
import com.github.andrepenteado.annotations.ConsequenceOutput;
import com.github.andrepenteado.annotations.Consequences;
import com.github.andrepenteado.contest.KGlobal;
import com.github.andrepenteado.contest.entity.TipoConta;
import com.github.andrepenteado.contest.services.CadastroService;
import com.github.andrepenteado.core.ServicesFactory;
import com.github.andrepenteado.util.ConfigHelper;
import com.github.andrepenteado.util.Log4jWrapper;
import com.github.andrepenteado.util.UsuarioLogadoWrapper;

/**
 * @author Andre Penteado
 * @since 12/08/2007 - 16:41:37
 */
@ActionClass(prefix = "/cadastros/tipoConta")
public class TipoContaAction extends BaseAction implements Authorizable, Validatable {

    private Log4jWrapper log = new Log4jWrapper(TipoContaAction.class, null);
    private CadastroService cadastroServices = null;

    private void instanciarServicos(UsuarioLogadoWrapper userLogin) {
        try {
            cadastroServices = (CadastroService)ServicesFactory.getInstance(CadastroService.class, new Object[] { userLogin });
            log = new Log4jWrapper(TipoContaAction.class, userLogin);
        }
        catch (Exception ex) {
            log.fatal("AÇÕES NÃO INSTANCIADAS: ".concat(TipoContaAction.class.getName()), ex);
        }
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean authorize(String innerAction, Object user, List groups) {
        if (user == null || groups == null)
            return false;

        instanciarServicos((UsuarioLogadoWrapper)user);

        if (groups.indexOf(KGlobal.CATEGORIA_SUPERUSUARIO) != -1 || groups.indexOf(KGlobal.CATEGORIA_FINANCEIRO) != -1)
            return true;

        return false;
    }

    public void prepareValidator(Validator validator, String innerAction) {
        if (innerAction.equals("gravar")) {
            validator.add("txt_descricao", new RequiredFieldRule(), ConfigHelper.getProperty("error.required", "Descrição"));
        }
    }

    @Consequences(outputs = @ConsequenceOutput(page = "/cadastros/tipo_conta/pesquisar.jsp"))
    public String pesquisar() {
        try {
            List<TipoConta> listaTiposConta = cadastroServices.pesquisarTiposContaPorDescricao("");
            output.setValue("listaTiposConta", listaTiposConta);
        }
        catch (Exception ex) {
            addError(ConfigHelper.get().getString("error.general"));
            log.error(ConfigHelper.get().getString("error.general"), ex);
            return ERROR;
        }
        return SUCCESS;
    }

    @Consequences(outputs = @ConsequenceOutput(page = "/cadastros/tipo_conta/cadastro.jsp"))
    public String cadastro() {
        try {
            long id = input.getLong("txt_id");
            TipoConta tipoConta = new TipoConta(id);
            output.setValue("tipoConta", tipoConta);
        }
        catch (Exception ex) {
            addError(ConfigHelper.get().getString("error.general"));
            log.error(ConfigHelper.get().getString("error.general"), ex);
            return ERROR;
        }
        return SUCCESS;
    }

    @Consequences(outputs = { @ConsequenceOutput(result = SUCCESS, page = "/comum/mensagem.jsp"),
                    @ConsequenceOutput(result = ERROR, page = "/cadastros/tipo_conta/cadastro.jsp") })
    public String gravar() {
        try {
            TipoConta tipoConta = new TipoConta(input.getLong("hid_id"));
            tipoConta.setId(input.getLong("hid_id"));
            tipoConta.setDescricao(input.getString("txt_descricao"));
            tipoConta.setObservacao(input.getString("txt_observacao"));
            tipoConta.gravar();
        }
        catch (Exception ex) {
            addError(ConfigHelper.get().getString("error.general"));
            log.error(ConfigHelper.get().getString("error.general"), ex);
            return ERROR;
        }

        output.setValue("titulo", "Cadastro de Tipo de Conta");
        output.setValue("mensagem", ConfigHelper.getProperty("info.saveOk", "Tipo de Conta"));
        output.setValue("url", input.getProperty("contextPath") + "/cadastros/tipoConta.iniciar.action");
        return SUCCESS;
    }

    @Consequences(outputs = @ConsequenceOutput(page = "/cadastros/tipo_conta/pesquisar.jsp"))
    public String excluir() {
        try {
            TipoConta tipoConta = new TipoConta(input.getLong("txt_id"));
            if (tipoConta != null && tipoConta.getId() != null)
                tipoConta.excluir();
        }
        catch (Exception ex) {
            addError(ConfigHelper.get().getString("error.general"));
            log.error(ConfigHelper.get().getString("error.general"), ex);
        }
        return SUCCESS;
    }
}
