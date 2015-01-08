
package br.com.alphadev.web;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.mentawai.action.BaseLoginAction;

import br.com.alphadev.core.ServicesFactory;
import br.com.alphadev.util.ConfigHelper;
import br.com.alphadev.util.Log4jWrapper;
import br.com.alphadev.util.SettingsConfig;
import br.com.alphadev.util.UsuarioLogadoWrapper;

/**
 * @author André Penteado
 * @since 03/05/2007 - 15:29:01
 */
public class ServletInitListener implements ServletContextListener, HttpSessionListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println("\n* * * * * APLICAÇÂO " + servletContextEvent.getServletContext().getServletContextName() + " INICIANDO ... * * * * *");
        System.setProperty(SettingsConfig.K_PROPRIEDADE_ROOT_CLASSPATH, servletContextEvent.getServletContext().getRealPath("/WEB-INF/classes"));
        ConfigHelper.load();
        Log4jWrapper.initConfig();
        Log4jWrapper log = new Log4jWrapper(ServletInitListener.class, null);
        log.info("Aplicação " + servletContextEvent.getServletContext().getServletContextName() + " iniciada");
        System.setProperty(SettingsConfig.K_PROPRIEDADE_ROOT_CLASSPATH, "");
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        Log4jWrapper log = new Log4jWrapper(ServletInitListener.class, null);
        log.info("Aplicação " + servletContextEvent.getServletContext().getServletContextName() + " interrompida");
        System.out.println("* * * * * APLICAÇÂO " + servletContextEvent.getServletContext().getServletContextName() + " INTERROMPIDA * * * * *\n");
    }

    @Override
    public void sessionCreated(HttpSessionEvent event) {
        // TODO Auto-generated method stub
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        // TODO Auto-generated method stub
        HttpSession session = event.getSession();
        UsuarioLogadoWrapper userLogin = (UsuarioLogadoWrapper)BaseLoginAction.getUserSession(session);
        ServicesFactory.removeUserInstances(userLogin);
    }
}
