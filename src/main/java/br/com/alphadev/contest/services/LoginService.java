
package br.com.alphadev.contest.services;

import java.util.Date;

import javax.ejb.Stateless;

import br.com.alphadev.contest.entity.Funcionario;
import br.com.alphadev.core.BasicService;
import br.com.alphadev.exception.ServiceValidationException;
import br.com.alphadev.util.ConfigHelper;
import br.com.alphadev.util.FunctionsHelper;
import br.com.alphadev.util.Log4jWrapper;
import br.com.alphadev.util.SHA512CryptLinuxShadow;
import br.com.alphadev.util.UsuarioLogadoWrapper;

/**
 * @author Andr� Penteado
 * @since 29/05/2007 - 14:48:11
 */
@Stateless
public class LoginService extends BasicService {

    /**
     * Autenticar funcion�rio no sistema
     *
     * @param identificacao = Identifica��o
     * @param senha = Senha para acesso
     * @param ip = IP da m�quina de acesso
     * @return = Objeto usu�rio
     * @throws ServiceValidationException = Erro de valida��o
     * @throws InterruptedException 
     */
    public Funcionario authenticator(final String identificacao, String senha, String ip) throws ServiceValidationException, InterruptedException {
        if ("".equals(identificacao)) {
            log.warn(ConfigHelper.getProperty("error.required", new String[] { "Identifica��o" }));
            throw new ServiceValidationException(ConfigHelper.getProperty("error.required", new String[] { "Identifica��o" }));
        }
        if ("".equals(senha)) {
            log.warn(ConfigHelper.getProperty("error.required", new String[] { "Senha" }));
            throw new ServiceValidationException(ConfigHelper.getProperty("error.required", new String[] { "Senha" }));
        }

        Funcionario funcionario = findUserByIdentification(identificacao);

        if (funcionario != null) {
            log = new Log4jWrapper(LoginService.class, new UsuarioLogadoWrapper(funcionario));

            boolean autenticado = SHA512CryptLinuxShadow.crypt(senha, funcionario.getSenha()).equals(funcionario.getSenha());
            if (!autenticado) {
                // Pausa para evitar quebra por for�a bruta
                Thread.currentThread();
                Thread.sleep(3000);
                log.warn(ConfigHelper.getProperty("error.invalid", new String[] { "Senha" }));
                log = new Log4jWrapper(LoginService.class, null);
                throw new ServiceValidationException(ConfigHelper.getProperty("error.invalid", new String[] { "Senha" }));
            }
        }
        else {
            log.warn(ConfigHelper.getProperty("error.notFound", new String[] { "E-mail" }));
            throw new ServiceValidationException(ConfigHelper.getProperty("error.notFound", new String[] { "E-mail" }));
        }

        if (funcionario.getEmpresa() == null) {
            log.warn(ConfigHelper.getProperty("error.notFound", new String[] { "Empresa do funcion�rio" }));
            throw new ServiceValidationException(ConfigHelper.getProperty("error.notFound", new String[] { "Empresa do funcion�rio" }));
        }

        funcionario.getCategorias().size();
        funcionario.setIpUltimoAcesso(ip);
        funcionario.setDataUltimoAcesso(new Date());
        funcionario.gravar();

        log = new Log4jWrapper(LoginService.class, new UsuarioLogadoWrapper(funcionario));
        log.info("Usu�rio autenticado no sistema");
        log = new Log4jWrapper(LoginService.class, null);

        return funcionario;
    }

    /**
     * Logoff do sistema
     *
     * @param funcionario = Usu�rio que desconectou
     */
    public void disconnect(UsuarioLogadoWrapper userLogin, String ip) {
        log = new Log4jWrapper(LoginService.class, userLogin);
        log.info("Usu�rio desconectou do sistema");
        log = new Log4jWrapper(LoginService.class, null);
    }

    /**
     * Busca funcionario filtrando por identificacao
     *
     * @param identificacao
     * @return Usuario
     */
    public Funcionario findUserByIdentification(String identificacao) {
        identificacao = FunctionsHelper.toStringNotNull(identificacao);
        log.info("Buscar funcion�rio pela identifica��o: ".concat(identificacao));
        return (Funcionario)findByField(Funcionario.class, "identificacao", identificacao);
    }

    /**
     * Alterar Senha do usu�rio
     *
     * @param funcionario = Usu�rio
     * @param senhaAtual = Senha Atual
     * @param novaSenha = Nova Senha
     * @exception ServiceValidationException = Erro de valida��o
     */
    public void alterarSenha(Funcionario funcionario, String senhaAtual, String novaSenha, String ip) throws ServiceValidationException {
        if (!SHA512CryptLinuxShadow.crypt(senhaAtual, funcionario.getSenha()).equals(funcionario.getSenha()))
            throw new ServiceValidationException(ConfigHelper.getProperty("error.notEquals", new String[] { "Senha Atual", "Nova Senha" }));
        if (novaSenha.length() < 6)
            throw new ServiceValidationException(ConfigHelper.getProperty("error.lessThan", new String[] { "Nova Senha", "6 caracteres" }));

        funcionario.setSenha(SHA512CryptLinuxShadow.crypt(novaSenha));
        funcionario.gravar();

        log = new Log4jWrapper(LoginService.class, new UsuarioLogadoWrapper(funcionario));
        log.info("Senha do funcion�rio alterada");
        log = new Log4jWrapper(LoginService.class, null);
    }
}