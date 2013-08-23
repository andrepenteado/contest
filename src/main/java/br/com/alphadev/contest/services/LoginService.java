
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
 * @author André Penteado
 * @since 29/05/2007 - 14:48:11
 */
@Stateless
public class LoginService extends BasicService {

    /**
     * Autenticar funcionário no sistema
     *
     * @param identificacao = Identificação
     * @param senha = Senha para acesso
     * @param ip = IP da máquina de acesso
     * @return = Objeto usuário
     * @throws ServiceValidationException = Erro de validação
     * @throws InterruptedException 
     */
    public Funcionario authenticator(final String identificacao, String senha, String ip) throws ServiceValidationException, InterruptedException {
        if ("".equals(identificacao)) {
            log.warn(ConfigHelper.getProperty("error.required", new String[] { "Identificação" }));
            throw new ServiceValidationException(ConfigHelper.getProperty("error.required", new String[] { "Identificação" }));
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
                // Pausa para evitar quebra por força bruta
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
            log.warn(ConfigHelper.getProperty("error.notFound", new String[] { "Empresa do funcionário" }));
            throw new ServiceValidationException(ConfigHelper.getProperty("error.notFound", new String[] { "Empresa do funcionário" }));
        }

        funcionario.getCategorias().size();
        funcionario.setIpUltimoAcesso(ip);
        funcionario.setDataUltimoAcesso(new Date());
        funcionario.gravar();

        log = new Log4jWrapper(LoginService.class, new UsuarioLogadoWrapper(funcionario));
        log.info("Usuário autenticado no sistema");
        log = new Log4jWrapper(LoginService.class, null);

        return funcionario;
    }

    /**
     * Logoff do sistema
     *
     * @param funcionario = Usuário que desconectou
     */
    public void disconnect(UsuarioLogadoWrapper userLogin, String ip) {
        log = new Log4jWrapper(LoginService.class, userLogin);
        log.info("Usuário desconectou do sistema");
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
        log.info("Buscar funcionário pela identificação: ".concat(identificacao));
        return (Funcionario)findByField(Funcionario.class, "identificacao", identificacao);
    }

    /**
     * Alterar Senha do usuário
     *
     * @param funcionario = Usuário
     * @param senhaAtual = Senha Atual
     * @param novaSenha = Nova Senha
     * @exception ServiceValidationException = Erro de validação
     */
    public void alterarSenha(Funcionario funcionario, String senhaAtual, String novaSenha, String ip) throws ServiceValidationException {
        if (!SHA512CryptLinuxShadow.crypt(senhaAtual, funcionario.getSenha()).equals(funcionario.getSenha()))
            throw new ServiceValidationException(ConfigHelper.getProperty("error.notEquals", new String[] { "Senha Atual", "Nova Senha" }));
        if (novaSenha.length() < 6)
            throw new ServiceValidationException(ConfigHelper.getProperty("error.lessThan", new String[] { "Nova Senha", "6 caracteres" }));

        funcionario.setSenha(SHA512CryptLinuxShadow.crypt(novaSenha));
        funcionario.gravar();

        log = new Log4jWrapper(LoginService.class, new UsuarioLogadoWrapper(funcionario));
        log.info("Senha do funcionário alterada");
        log = new Log4jWrapper(LoginService.class, null);
    }
}