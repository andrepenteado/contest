/*
 * Copyright (c) 2006-2007 by UNESP - Universidade Estadual Paulista "J�LIO DE MESQUITA FILHO"
 *               Faculdade de Ci�ncias, Bauru, S�o Paulo, Brasil
 *               http://www.fc.unesp.br, http://www.unesp.br
 *
 *
 * Este arquivo � parte do programa CPA.
 *
 * CPA � um software livre; voc� pode redistribui-lo e/ou 
 * modifica-lo dentro dos termos da Licen�a P�blica Geral GNU como 
 * publicada pela Funda��o do Software Livre (FSF); na vers�o 2 da 
 * Licen�a, ou (na sua opni�o) qualquer vers�o.
 *
 * CPA � distribuido na esperan�a que possa ser util, 
 * mas SEM NENHUMA GARANTIA; sem uma garantia implicita de ADEQUA��O a qualquer
 * MERCADO ou APLICA��O EM PARTICULAR. Veja a
 * Licen�a P�blica Geral GNU para maiores detalhes.
 *
 * Voc� deve ter recebido uma c�pia da Licen�a P�blica Geral GNU
 * junto com CPA, se n�o, escreva para a Funda��o do Software
 * Livre(FSF) Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 *
 * This file is part of CPA.
 *
 * CPA is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * CPA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with CPA; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA *
 *
 *
 * Date:    03/05/2007 15:29:01
 *
 * Author:  Andr� Penteado
 */

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
 * @author Andr� Penteado
 * @since 03/05/2007 - 15:29:01
 */
public class ServletInitListener implements ServletContextListener, HttpSessionListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println("\n* * * * * APLICA��O " + servletContextEvent.getServletContext().getServletContextName() + " INICIANDO ... * * * * *");
        System.setProperty(SettingsConfig.K_PROPRIEDADE_ROOT_CLASSPATH, servletContextEvent.getServletContext().getRealPath("/WEB-INF/classes"));
        ConfigHelper.load();
        Log4jWrapper.initConfig();
        Log4jWrapper log = new Log4jWrapper(ServletInitListener.class, null);
        log.info("Aplica��o " + servletContextEvent.getServletContext().getServletContextName() + " iniciada");
        System.setProperty(SettingsConfig.K_PROPRIEDADE_ROOT_CLASSPATH, "");
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        Log4jWrapper log = new Log4jWrapper(ServletInitListener.class, null);
        log.info("Aplica��o " + servletContextEvent.getServletContext().getServletContextName() + " interrompida");
        System.out.println("* * * * * APLICA��O " + servletContextEvent.getServletContext().getServletContextName() + " INTERROMPIDA * * * * *\n");
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
