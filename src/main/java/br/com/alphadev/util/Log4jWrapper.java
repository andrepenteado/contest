
package br.com.alphadev.util;

import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.apache.log4j.PropertyConfigurator;


/**
 * @author Andre Penteado
 * @since 17/12/2007 - 15:35:50
 */
public class Log4jWrapper {

    private Logger log = null;
    private UsuarioLogadoWrapper userLogin = null;

    public Log4jWrapper(Class<? extends Object> className, UsuarioLogadoWrapper userLogin) {
        this.log = Logger.getLogger(className);
        this.userLogin = userLogin;
    }

    /**
     * Inicializa configuração do log4j
     */
    public static void initConfig() {
        if (Logger.getRootLogger() == null || Logger.getRootLogger().getAppender("CONSOLE") == null
                        || Logger.getRootLogger().getAppender("SEMANAL") == null || Logger.getRootLogger().getAppender("BD") == null) {
            Properties prop = new Properties();

            // Enviar logs para console
            prop.setProperty("log4j.appender.CONSOLE", "org.apache.log4j.ConsoleAppender");
            prop.setProperty("log4j.appender.CONSOLE.Threshold", "DEBUG");
            prop.setProperty("log4j.appender.CONSOLE.layout", "org.apache.log4j.PatternLayout");
            prop.setProperty("log4j.appender.CONSOLE.layout.ConversionPattern", ConfigHelper.get().getString("log.mascara"));

            // Gravar logs em arquivos semanais
            prop.setProperty("log4j.appender.SEMANAL", "org.apache.log4j.DailyRollingFileAppender");
            prop.setProperty("log4j.appender.SEMANAL.Threshold", "INFO");
            prop.setProperty("log4j.appender.SEMANAL.DatePattern", "'.'yyyy-ww");
            prop.setProperty("log4j.appender.SEMANAL.File", ConfigHelper.get().getString("log.arquivo"));
            prop.setProperty("log4j.appender.SEMANAL.layout", "org.apache.log4j.PatternLayout");
            prop.setProperty("log4j.appender.SEMANAL.layout.ConversionPattern", ConfigHelper.get().getString("log.mascara"));

            // Configura os níveis e tipos de logs para as categorias
            if (ConfigHelper.get().getBoolean("sistema.debug"))
                prop.setProperty("log4j.rootCategory", "DEBUG, CONSOLE");
            else
                prop.setProperty("log4j.rootCategory", "ERROR, CONSOLE");

            // Retirar gravação em BD
            //prop.setProperty("log4j.category." + SettingsConfig.K_PACOTE_LOG, "DEBUG, BD, SEMANAL");
            prop.setProperty("log4j.category." + SettingsConfig.K_PACOTE_LOG, "DEBUG, SEMANAL");

            PropertyConfigurator.configure(prop);

            System.out.println("Configuração de auditoria carregada");
        }
    }

    /**
     * Iniciar paramêtros dinâmicos a ser substituídos nos logs
     */
    private void initParams() {
        if (userLogin != null) {
            if (userLogin.getIdentificacao() != null)
                MDC.put("USER", userLogin.getIdentificacao());
            else
                MDC.put("USER", "");

            if (userLogin.getIp() != null)
                MDC.put("IP", userLogin.getIp());
            else
                MDC.put("IP", FunctionsHelper.getLocalHostName());

            if (userLogin.getCategoria() != null)
                MDC.put("CATEGORIA", userLogin.getCategoria());
            else
                MDC.put("CATEGORIA", "");

            if (userLogin.getCategoria() != null)
                MDC.put("SISTEMA", userLogin.getModulo());
            else
                MDC.put("SISTEMA", "");
        }
    }

    /**
     * Limpar paramêtros dinâmicos
     */
    private void finishParams() {
        MDC.remove("USER");
        MDC.remove("IP");
        MDC.remove("CATEGORIA");
        MDC.remove("SISTEMA");
    }

    public void debug(String msg) {
        initParams();
        log.debug(msg);
        finishParams();
    }

    public void info(String msg) {
        initParams();
        log.info(msg);
        finishParams();
    }

    public void warn(String msg) {
        initParams();
        log.warn(msg);
        finishParams();
    }

    public void error(String msg, Throwable ex) {
        initParams();
        log.error(msg, ex);
        finishParams();
    }

    public void fatal(String msg, Throwable ex) {
        initParams();
        log.fatal(msg, ex);
        finishParams();
    }
}
