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
 * Date:    03/05/2007 14:48:53
 *
 * Author:  Andr� Penteado
 */

package br.com.alphadev.core;

import java.util.Collection;

/**
 * @author Andr� Penteado
 * @since 03/05/2007 - 14:48:53
 */
public abstract class BasicService extends Connection {

    protected Collection<?> executeQuery(final String query, Object[]... parameters) {
        return getService().executeQuery(query, parameters);
    }

    protected Object executeFind(final String query, Object[]... parameters) {
        return getService().executeFind(query, parameters);
    }

    protected Object findByPK(Class<?> domain, Long id) {
        return getService().findByPK(domain, id);
    }

    protected Object findByField(Class<?> domain, String field, Object value) {
        return getService().findByField(domain, field, value);
    }

    protected void executeUpdate(final String query, Object[]... parameters) {
        getService().executeUpdate(query, parameters);
    }

    protected Object save(Object domain) {
        return getService().save(domain);
    }

    protected void delete(Object domain) {
        getService().delete(domain);
    }

    protected void beginTransaction() {
        getService().beginTransaction();
    }

    protected void commit() {
        getService().commit();
    }

    protected void rollback() {
        getService().rollback();
    }
}