
package com.github.andrepenteado.contest.actions;

import org.mentawai.action.BaseLoginAction;

import com.github.andrepenteado.contest.entity.Funcionario;
import com.github.andrepenteado.contest.services.LoginService;
import com.github.andrepenteado.core.ServicesFactory;
import com.github.andrepenteado.exception.ServiceValidationException;
import com.github.andrepenteado.util.ConfigHelper;
import com.github.andrepenteado.util.Log4jWrapper;
import com.github.andrepenteado.util.UsuarioLogadoWrapper;

/**
 * @author André Penteado
 * @since 13/04/2007 - 17:42:45
 */
public class LoginAction extends BaseLoginAction {

    private Log4jWrapper log = new Log4jWrapper(LoginAction.class, null);
    private LoginService loginService = null;

    public LoginAction() {
        try {
            loginService = (LoginService)ServicesFactory.getInstance(LoginService.class, null);
        }
        catch (Exception ex) {
            log.fatal("AÇÕES NÃO INSTANCIADAS: ".concat(LoginAction.class.getName()), ex);
        }
    }

    public String abrir() {
        if (isLogged())
            return SUCCESS;
        return ERROR;
    }

    @Override
    public String execute() throws Exception {
        String strUsuario = input.getString("txt_usuario");
        String senha = input.getString("txt_senha");

        try {
            String ip = input.getProperty("remoteAddr");
            Funcionario funcionario = loginService.authenticator(strUsuario, senha, ip);

            if (funcionario != null) {
                UsuarioLogadoWrapper userLogin = new UsuarioLogadoWrapper();
                userLogin.setFuncionario(funcionario);
                userLogin.setIp(ip);
                setUserSession(userLogin);
                session.setAttribute("user", userLogin);
            }
        }
        catch (ServiceValidationException unex) {
            addError(unex.getMessage());
            return ERROR;
        }
        catch (Exception ex) {
            log.error(ConfigHelper.get().getString("error.general"), ex);
            addError(ConfigHelper.get().getString("error.general"));
            return ERROR;
        }
        return SUCCESS;
    }
}
