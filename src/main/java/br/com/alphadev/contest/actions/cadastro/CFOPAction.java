
package br.com.alphadev.contest.actions.cadastro;

import java.util.List;

import org.mentawai.authorization.Authorizable;
import org.mentawai.core.BaseAction;
import org.mentawai.validation.Validatable;
import org.mentawai.validation.Validator;

import br.com.alphadev.annotations.ActionClass;
import br.com.alphadev.annotations.ConsequenceOutput;
import br.com.alphadev.annotations.Consequences;
import br.com.alphadev.contest.KGlobal;
import br.com.alphadev.contest.services.CadastroService;
import br.com.alphadev.core.ServicesFactory;
import br.com.alphadev.util.ConfigHelper;
import br.com.alphadev.util.Log4jWrapper;
import br.com.alphadev.util.UsuarioLogadoWrapper;

/**
 * @author Andre Penteado
 * @since 12/08/2007 - 16:41:37
 */
@ActionClass(prefix = "/cadastros/cfop")
public class CFOPAction extends BaseAction implements Authorizable, Validatable {

    private Log4jWrapper log = new Log4jWrapper(CFOPAction.class, null);
    private CadastroService cadastroServices = null;

    private void instanciarServicos(UsuarioLogadoWrapper userLogin) {
        try {
            cadastroServices = (CadastroService)ServicesFactory.getInstance(CadastroService.class, new Object[] { userLogin });
            log = new Log4jWrapper(CFOPAction.class, userLogin);
        }
        catch (Exception ex) {
            log.fatal("AÇÕES NÃO INSTANCIADAS: ".concat(CFOPAction.class.getName()), ex);
        }
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean authorize(String innerAction, Object user, List groups) {
        if (user == null || groups == null)
            return false;

        instanciarServicos((UsuarioLogadoWrapper)user);

        if ("iniciar".equals(innerAction) || "pesquisar".equals(innerAction))
            return true;

        if (groups.indexOf(KGlobal.CATEGORIA_SUPERUSUARIO) != -1 || groups.indexOf(KGlobal.CATEGORIA_ADMINISTRATIVO) != -1
                        || groups.indexOf(KGlobal.CATEGORIA_COMPRAS) != -1 || groups.indexOf(KGlobal.CATEGORIA_ESTOQUE) != -1)
            return true;

        return false;
    }

    public void prepareValidator(Validator validator, String innerAction) {

    }

    @Consequences(outputs = @ConsequenceOutput(page = "/cadastros/cfop/pesquisar.jsp"))
    public String iniciar() {
        return SUCCESS;
    }

    @Consequences(outputs = @ConsequenceOutput(page = "/cadastros/cfop/pesquisar.jsp"))
    public String pesquisar() {
        try {
            String codigoDescricao = input.getString("txt_codigo_descricao");

            output.setValue("listaCFOP", cadastroServices.filtrarCFOPPorCodigoDescricao(codigoDescricao));
            output.setValue("foco", "txt_codigo_cfop");

            // Colunas do relatório
            String[] colunas = input.getStrings("chk_colunas_pesquisa");
            if (colunas != null && colunas.length > 0)
                for (String col : colunas)
                    output.setValue(col, '1');
        }
        catch (Exception ex) {
            addError(ConfigHelper.get().getString("error.general"));
            log.error(ConfigHelper.get().getString("error.general"), ex);
            return ERROR;
        }
        return SUCCESS;
    }

}
