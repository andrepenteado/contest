/*
 * Copyright (c) 2006 by UNESP - Universidade Estadual Paulista "J�LIO DE MESQUITA FILHO"
 *               Faculdade de Ci�ncias, Bauru, S�o Paulo, Brasil
 *               http://www.fc.unesp.br, http://www.unesp.br
 *
 *
 * Este arquivo � parte do programa Core.
 *
 * Core � um software livre; voc� pode redistribui-lo e/ou 
 * modifica-lo dentro dos termos da Licen�a P�blica Geral GNU como 
 * publicada pela Funda��o do Software Livre (FSF); na vers�o 2 da 
 * Licen�a, ou (na sua opni�o) qualquer vers�o.
 *
 * Core � distribuido na esperan�a que possa ser util, 
 * mas SEM NENHUMA GARANTIA; sem uma garantia implicita de ADEQUA��O a qualquer
 * MERCADO ou APLICA��O EM PARTICULAR. Veja a
 * Licen�a P�blica Geral GNU para maiores detalhes.
 *
 * Voc� deve ter recebido uma c�pia da Licen�a P�blica Geral GNU
 * junto com Core, se n�o, escreva para a Funda��o do Software
 * Livre(FSF) Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 *
 * This file is part of Core.
 *
 * Core is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Core; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA *
 *
 *
 * Date:    17/12/2007 15:35:50
 *
 * Author:  Andr� Penteado
 */

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
     * Inicializa configura��o do log4j
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

            // Configura os n�veis e tipos de logs para as categorias
            if (ConfigHelper.get().getBoolean("sistema.debug"))
                prop.setProperty("log4j.rootCategory", "DEBUG, CONSOLE");
            else
                prop.setProperty("log4j.rootCategory", "ERROR, CONSOLE");

            // Retirar grava��o em BD
            //prop.setProperty("log4j.category." + SettingsConfig.K_PACOTE_LOG, "DEBUG, BD, SEMANAL");
            prop.setProperty("log4j.category." + SettingsConfig.K_PACOTE_LOG, "DEBUG, SEMANAL");

            PropertyConfigurator.configure(prop);

            System.out.println("Configura��o de auditoria carregada");
        }
    }

    /**
     * Iniciar param�tros din�micos a ser substitu�dos nos logs
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
     * Limpar param�tros din�micos
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
