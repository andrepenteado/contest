
package br.com.alphadev.contest.actions;

import java.util.ArrayList;
import java.util.List;

import org.mentawai.action.BaseLoginAction;
import org.mentawai.authorization.Authorizable;
import org.mentawai.core.BaseAction;

import br.com.alphadev.contest.entity.Categoria;
import br.com.alphadev.contest.services.LoginService;
import br.com.alphadev.core.ServicesFactory;
import br.com.alphadev.exception.ServiceValidationException;
import br.com.alphadev.util.ConfigHelper;
import br.com.alphadev.util.UsuarioLogadoWrapper;

/**
 * @author André Penteado
 * @since 13/04/2007 - 14:17:20
 */
public class ComumAction extends BaseAction implements Authorizable {

    @SuppressWarnings("rawtypes")
    @Override
    public boolean authorize(String innerAction, Object user, List groups) {
        if ("sair".equals(innerAction) || "exception".equals(innerAction))
            return true;
        if (user != null && BaseLoginAction.isLogged(session))
            return true;
        return false;
    }

    public String home() throws Exception {
        return SUCCESS;
    }

    public String carregarModulos() throws Exception {
        UsuarioLogadoWrapper userLogin = (UsuarioLogadoWrapper)getUserSession();
        ArrayList<Categoria> categorias = new ArrayList<Categoria>(userLogin.getFuncionario().getCategorias());
        Categoria categoriaAtual = null;

        // Se usuário esta em apenas uma categoria
        if (categorias.size() == 1)
            categoriaAtual = categorias.get(0);

        // Foi escolhido a categoria para acesso
        if (input.getInt(("id_categoria")) > 0)
            categoriaAtual = new Categoria(input.getLong("id_categoria"));

        if (categoriaAtual != null && categorias.contains(categoriaAtual)) {
            userLogin.setCategoriaAtual(categoriaAtual);
            BaseLoginAction.setUserSession(userLogin, session);
            BaseLoginAction.setUserGroup(categoriaAtual.getNome(), session);
            session.setAttribute("user", userLogin);
        }

        return SUCCESS;
    }

    public String sair() throws Exception {
        UsuarioLogadoWrapper userLogin = (UsuarioLogadoWrapper)getUserSession();
        if (userLogin != null) {
            if (userLogin.getCategoriaAtual() != null && userLogin.getFuncionario().getCategorias().size() > 1) {
                userLogin.setCategoriaAtual(null);
                BaseLoginAction.setUserGroup("", session);
                BaseLoginAction.setUserSession(userLogin, session);
                session.setAttribute("user", userLogin);
            }
            else {
                LoginService loginService = (LoginService)ServicesFactory.getInstance(LoginService.class, null);
                loginService.disconnect(userLogin, input.getProperty("remoteAddr"));
                session.reset();
            }
        }
        return SUCCESS;
    }

    public String alterarSenha() {
        try {
            if (getUserSession() == null)
                throw new ServiceValidationException(ConfigHelper.get().getString("error.accessDenied"));
            UsuarioLogadoWrapper userLogin = (UsuarioLogadoWrapper)getUserSession();
            String senhaAtual = input.getString("txt_senha_atual");
            String novaSenha = input.getString("txt_nova_senha");
            LoginService loginService = (LoginService)ServicesFactory.getInstance(LoginService.class, null);
            loginService.alterarSenha(userLogin.getFuncionario(), senhaAtual, novaSenha, input.getProperty("remoteAddr"));
            output.setValue("titulo", "Alterar Senha");
            output.setValue("url", input.getProperty("contextPath") + "/comum.home.action");
            output.setValue("mensagem", "Senha alterada com sucesso.");
        }
        catch (ServiceValidationException unex) {
            addError(unex.getMessage());
            return ERROR;
        }
        catch (Exception ex) {
            addError(ConfigHelper.get().getString("error.general"));
            return ERROR;
        }
        return SUCCESS;
    }

    public String abrirAlterarSenha() {
        return SUCCESS;
    }

    /* Exibe página de erro */
    public String erro() {
        return SUCCESS;
    }

    public String acessoNegado() {
        session.reset();
        return SUCCESS;
    }
}