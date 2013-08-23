/*
 * Copyright (c) 2006-2007 by UNESP - Universidade Estadual Paulista "J�LIO DE MESQUITA FILHO"
 *               Faculdade de Ci�ncias, Bauru, S�o Paulo, Brasil
 *               http://www.fc.unesp.br, http://www.unesp.br
 *
 *
 * Este arquivo � parte do programa WebUnesp.
 *
 * WebUnesp � um software livre; voc� pode redistribui-lo e/ou 
 * modifica-lo dentro dos termos da Licen�a P�blica Geral GNU como 
 * publicada pela Funda��o do Software Livre (FSF); na vers�o 2 da 
 * Licen�a, ou (na sua opni�o) qualquer vers�o.
 *
 * WebUnesp � distribuido na esperan�a que possa ser util, 
 * mas SEM NENHUMA GARANTIA; sem uma garantia implicita de ADEQUA��O a qualquer
 * MERCADO ou APLICA��O EM PARTICULAR. Veja a
 * Licen�a P�blica Geral GNU para maiores detalhes.
 *
 * Voc� deve ter recebido uma c�pia da Licen�a P�blica Geral GNU
 * junto com WebUnesp, se n�o, escreva para a Funda��o do Software
 * Livre(FSF) Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 *
 * This file is part of WebUnesp.
 *
 * WebUnesp is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * WebUnesp is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with WebUnesp; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA *
 *
 *
 * Date:    04/05/2007 10:00:33
 *
 * Author:  André Penteado
 */

package br.com.alphadev.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.naming.InitialContext;

import br.com.alphadev.exception.IllegalServiceClassException;
import br.com.alphadev.util.Log4jWrapper;
import br.com.alphadev.util.UsuarioLogadoWrapper;

/**
 * @author Andr� Penteado
 * @since 04/05/2007 - 10:00:34
 */
public final class ServicesFactory {

    private static final Log4jWrapper log = new Log4jWrapper(ServicesFactory.class, null);
    private static HashMap<String, Connection> servicesInstancesPool = new HashMap<String, Connection>();

    /**
     * Retorna o objeto de servi�o para a camada de visualiza��o.
     * Utiliza o padr�o Singleton (uma inst�ncia do objeto em toda
     * aplica��o) para retornar o objeto.
     *
     * @param classe = Classe a ser instanciada
     * @param parametros = Param�tros para o servi�o
     * @return Objeto de servi�o
     * @throws IllegalServiceClassException = Problemas de instancia da classe de servi�o
     */
    public static <T extends Connection> T getInstance(Class<T> classe, Object... parametros) throws IllegalServiceClassException {
        T result = null;

        try {
            try {
                // Verifica se servi�o � um EJB
                result = (T)new InitialContext().lookup("java:module/" + classe.getSimpleName());
                result.setParameters(parametros);
                return result;
            }
            catch (Exception ex) {
                log.debug(classe.getSimpleName().concat(" n�o � um EJB"));
                StringBuilder key = new StringBuilder(classe.getName());
                if (parametros != null && parametros.length > 0) {
                    for (int i = 0; i < parametros.length; i++)
                        key.append(parametros[i].toString());
                }

                result = (T)servicesInstancesPool.get(key.toString());

                if (result == null) {
                    log.debug("Nova classe de servi�o instanciada: " + key.toString());
                    result = classe.newInstance();
                    result.setParameters(parametros);
                    servicesInstancesPool.put(key.toString(), result);
                }
            }
        }
        catch (Exception ex) {
            log.fatal("Instancia de classe de servi�o incorreta: " + classe.getName(), ex);
            throw new IllegalServiceClassException("Instancia de classe de servi�o incorreta: " + classe.getName(), ex);
        }

        return result;
    }

    public static void removeUserInstances(UsuarioLogadoWrapper user) {
        if (user == null)
            return;
        List<String> keys = new ArrayList<String>();
        // Faz uma c�pia das chaves
        // Conserta erro ConcurrentModificationException ao remover itens do HashMap
        for (String key : servicesInstancesPool.keySet()) {
            keys.add(key);
        }
        for (String key : keys) {
            if (key.indexOf(user.toString()) != -1) {
                log.debug("Classe de servi�o removida: " + key.toString());
                servicesInstancesPool.remove(key);
            }
        }
    }
}
