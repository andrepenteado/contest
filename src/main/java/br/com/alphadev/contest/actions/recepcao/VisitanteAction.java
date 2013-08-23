
package br.com.alphadev.contest.actions.recepcao;

import java.util.List;

import org.mentawai.authorization.Authorizable;
import org.mentawai.core.BaseAction;

import br.com.alphadev.annotations.ActionClass;
import br.com.alphadev.annotations.ConsequenceOutput;
import br.com.alphadev.annotations.Consequences;
import br.com.alphadev.contest.KGlobal;
import br.com.alphadev.util.ConfigHelper;
import br.com.alphadev.util.Log4jWrapper;
import br.com.alphadev.util.UsuarioLogadoWrapper;

/**
 * @author Andre Penteado
 * @since 30/03/2012 - 16:41:37
 */
@ActionClass(prefix = "/recepcao/visitante")
public class VisitanteAction extends BaseAction implements Authorizable {

    private Log4jWrapper log = new Log4jWrapper(VisitanteAction.class, null);

    private void instanciarServicos(UsuarioLogadoWrapper userLogin) {
        try {
            log = new Log4jWrapper(VisitanteAction.class, userLogin);
        }
        catch (Exception ex) {
            log.fatal("AÇÕES NÃO INSTANCIADAS: ".concat(VisitanteAction.class.getName()), ex);
        }
    }

    @SuppressWarnings({ "rawtypes" })
    @Override
    public boolean authorize(String innerAction, Object user, List groups) {
        if (user == null || groups == null)
            return false;

        instanciarServicos((UsuarioLogadoWrapper)user);

        if (groups.indexOf(KGlobal.CATEGORIA_RECEPCAO) != -1)
            return true;

        return false;
    }

    @Consequences(outputs = @ConsequenceOutput(page = "/recepcao/visitante/pesquisar.jsp"))
    public String pesquisar() {
        try {
        }
        catch (Exception ex) {
            addError(ConfigHelper.get().getString("error.general"));
            log.error(ConfigHelper.get().getString("error.general"), ex);
            return ERROR;
        }
        return SUCCESS;
    }

    @Consequences(outputs = @ConsequenceOutput(page = "/recepcao/visitante/cadastro.jsp"))
    public String cadastro() {
        try {
            output.setValue("foco", "txt_nome");
        }
        catch (Exception ex) {
            addError(ConfigHelper.get().getString("error.general"));
            log.error(ConfigHelper.get().getString("error.general"), ex);
            return ERROR;
        }
        return SUCCESS;
    }

    @Consequences(outputs = { @ConsequenceOutput(result = SUCCESS, page = "/comum/mensagem.jsp"),
                    @ConsequenceOutput(result = ERROR, page = "/recepcao/visitante/cadastro.jsp") })
    public String gravar() {
        try {
        }
        catch (Exception ex) {
            addError(ConfigHelper.get().getString("error.general"));
            log.error(ConfigHelper.get().getString("error.general"), ex);
            return ERROR;
        }

        output.setValue("titulo", "Registro de Visitantes");
        output.setValue("mensagem", ConfigHelper.getProperty("info.saveOk", "Visitante"));
        output.setValue("url", input.getProperty("contextPath") + "/recepcao/visitante.cadastro.action");
        return SUCCESS;
    }

    @Consequences(outputs = @ConsequenceOutput(page = "/recepcao/visitante/pesquisar.jsp"))
    public String excluir() {
        try {
        }
        catch (Exception ex) {
            addError(ConfigHelper.get().getString("error.general"));
            log.error(ConfigHelper.get().getString("error.general"), ex);
            return ERROR;
        }
        return SUCCESS;
    }
}