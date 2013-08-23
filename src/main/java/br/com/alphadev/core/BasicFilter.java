/*
 * Copyright (c) 2006-2007 by UNESP - Universidade Estadual Paulista "JÚLIO DE MESQUITA FILHO"
 *               Faculdade de Ciências, Bauru, São Paulo, Brasil
 *               http://www.fc.unesp.br, http://www.unesp.br
 *
 *
 * Este arquivo é parte do programa WebUnesp.
 *
 * WebUnesp é um software livre; você pode redistribui-lo e/ou 
 * modifica-lo dentro dos termos da Licença Pública Geral GNU como 
 * publicada pela Fundação do Software Livre (FSF); na versão 2 da 
 * Licença, ou (na sua opnião) qualquer versão.
 *
 * WebUnesp é distribuido na esperança que possa ser util, 
 * mas SEM NENHUMA GARANTIA; sem uma garantia implicita de ADEQUAÇÂO a qualquer
 * MERCADO ou APLICAÇÃO EM PARTICULAR. Veja a
 * Licença Pública Geral GNU para maiores detalhes.
 *
 * Você deve ter recebido uma cópia da Licença Pública Geral GNU
 * junto com WebUnesp, se não, escreva para a Fundação do Software
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
 * Author:  André Penteado
 */

package br.com.alphadev.core;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author André Penteado
 * @since 03/05/2007 - 14:48:53
 */
public abstract class BasicFilter extends Connection {

    protected StringBuilder filterDescription = new StringBuilder();

    protected Collection<Object[]> parameters = new ArrayList<Object[]>();

    @SuppressWarnings("rawtypes")
    public abstract Collection executeFilter();

    public abstract void createFilter();

    public String getFilter() {
        return filterDescription.toString();
    }

    public Collection<Object[]> getParameters() {
        return parameters;
    }

    protected Collection<?> executeQuery(final String query, Object[]... parameters) {
        return getService().executeQuery(query, parameters);
    }
}
