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
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.alphadev.util.Log4jWrapper;


/**
 * @author Andr� Penteado
 * @since 03/05/2007 - 14:48:53
 */
public abstract class JPAService implements Service {

    private final Log4jWrapper log = new Log4jWrapper(JPAService.class, null);

    protected abstract EntityManager getManager();

    /**
     * Executar consulta EJB-QL nomeada
     *
     * @param namedQuery = Nome da consulta configurada no hibernate
     * @param parameters Matriz com os nomes e valores dos parametros <br>
     *               Posicao 0 - Nome do parametro <br>
     *               Posicao 1 - Valor do parametro <br> <br>
     *               Exemplo: parameters[0] = {"codigo","10"}; parameters[1] = {"nome", "jose"};
     * 
     * @return Colecao com o resultado da consulta
     */
    @Override
    public List<?> executeNamedQuery(final String namedQuery, final Object[]... parameters) {
        log.debug("Executando consulta EJB-QL nomeada " + namedQuery);
        //getManager().clear();
        final Query query = getManager().createNamedQuery(namedQuery);
        query.setMaxResults(1000);
        if (parameters != null) {
            for (int i = 0; i < parameters.length; i++) {
                log.debug(">>> Param�tro: " + parameters[i][0] + " = " + parameters[i][1]);
                query.setParameter((String)parameters[i][0], parameters[i][1]);
            }
        }
        List<?> result = query.getResultList();
        log.debug(">>> Itens retornados: " + result.size());
        return result;
    }

    /**
     * Executar consulta EJB-QL
     * 
     * @param ejbql = Sintaxe EJB-QL da consulta a ser executada
     * @param parameters Matriz com os nomes e valores dos parametros <br>
     *               Posicao 0 - Nome do parametro <br>
     *               Posicao 1 - Valor do parametro <br> <br>
     *               Exemplo: parameters[0] = {"codigo","10"}; parameters[1] = {"nome", "jose"};
     * 
     * @return Colecao com o resultado da consulta
     */
    @Override
    public Collection<?> executeQuery(final String ejbql, final Object[]... parameters) {
        log.debug("Executando consulta EJB-QL: " + ejbql);
        //getManager().clear();
        final Query query = getManager().createQuery(ejbql);
        query.setMaxResults(1000);
        if (parameters != null) {
            for (int i = 0; i < parameters.length; i++) {
                if (parameters[i][0] == null)
                    continue;
                log.debug(">>> Param�tro: " + parameters[i][0] + " = " + parameters[i][1]);
                query.setParameter((String)parameters[i][0], parameters[i][1]);
            }
        }
        Collection<?> result = query.getResultList();
        log.debug(">>> Itens retornados: " + result.size());
        return result;
    }

    /**
     * Executar busca EJB-QL, trazendo 1 registro do banco de dados
     * 
     * @param ejbql = Sintaxe EJB-QL da busca a ser executada
     * @param parameters Matriz com os nomes e valores dos parametros <br>
     *               Posicao 0 - Nome do parametro <br>
     *               Posicao 1 - Valor do parametro <br> <br>
     *               Exemplo: parameters[0] = {"codigo","10"}; parameters[1] = {"nome", "jose"};
     * 
     * @return Objeto resultado da busca
     */
    @Override
    public Object executeFind(final String ejbql, final Object[]... parameters) {
        log.debug("Executando busca EJB-QL: " + ejbql);
        //getManager().clear();
        final Query query = getManager().createQuery(ejbql);
        query.setMaxResults(1);
        if (parameters != null) {
            for (int i = 0; i < parameters.length; i++) {
                if (parameters[i][0] == null)
                    continue;
                log.debug(">>> Param�tro: " + parameters[i][0] + " = " + parameters[i][1]);
                query.setParameter((String)parameters[i][0], parameters[i][1]);
            }
        }
        Object ret = null;
        List<?> result = query.getResultList();
        if (result != null && !result.isEmpty()) {
            ret = result.get(0);
            try {
                if (getManager().contains(ret))
                    getManager().refresh(ret);
            }
            catch (IllegalArgumentException ex) {
            }
        }
        log.debug(ret == null ? ">>> N�o Encontrado " : ">>> Encontrado ");
        return ret;
    }

    /**
     * Buscar objeto de uma classe de entidade pela chave prim�ria
     * 
     * @param entityClass = Classe de entidade
     * @param id = Chave prim�ria
     * 
     * @return Objeto da classe de entidade consultada
     */
    @Override
    public <T> T findByPK(final Class<T> entityClass, final Long id) {
        T result = (T)getManager().find(entityClass, Long.valueOf(id));
        if (result != null && getManager().contains(result)) {
            getManager().refresh(result);
        }
        log.debug("Buscar entidade " + entityClass.getName() + " pelo ID " + id);
        log.debug((result == null ? ">>> N�o Encontrado " : ">>> Encontrado ") + entityClass.getName() + " de ID " + id);
        return result;
    }

    /**
     * Buscar objeto de uma classe de entidade por um atributo qualquer
     * 
     * @param entityClass = Classe de entidade
     * @param name = Nome de algum atributo da classe de entidade
     * @param value = Valor do atributo a ser pesquisado
     * 
     * @return Objeto da classe de entidade consultada
     */
    @Override
    public <T> T findByField(final Class<T> entityClass, final String name, final Object value) {
        StringBuilder query = new StringBuilder("SELECT entity FROM ");
        query.append(entityClass.getName()).append(" entity ");
        query.append(" WHERE ");
        query.append("entity.").append(name);
        query.append(" = :pParam");

        log.debug("Buscar entidade " + entityClass.getName() + " por campo: " + query.toString());

        T ret = null;
        Query q = getManager().createQuery(query.toString());
        q.setParameter("pParam", value);
        List<T> result = q.getResultList();
        if (result != null && !result.isEmpty()) {
            ret = result.get(0);
            if (getManager().contains(ret))
                getManager().refresh(ret);
        }
        log.debug((ret == null ? ">>> N�o Encontrado " : ">>> Encontrado ") + entityClass.getName() + " por campo: " + query.toString());
        return ret;
    }

    /**
     * Executar EJB-QL de altera��o em lote
     * 
     * @param ejbql = Sintaxe EJB-QL da consulta a ser executada
     * @param parameters Matriz com os nomes e valores dos parametros <br>
     *               Posicao 0 - Nome do parametro <br>
     *               Posicao 1 - Valor do parametro <br> <br>
     *               Exemplo: parameters[0] = {"codigo","10"}; parameters[1] = {"nome", "jose"};
     */
    @Override
    public void executeUpdate(final String ejbql, final Object[]... parameters) {
        log.debug("Executando EJB-QL de altera��o em lote: " + ejbql);

        final Query query = getManager().createQuery(ejbql);

        if (parameters != null) {
            for (int i = 0; i < parameters.length; i++) {
                if (parameters[i][0] == null)
                    continue;
                log.debug(">>> Param�tro: " + parameters[i][0] + " = " + parameters[i][1]);
                query.setParameter((String)parameters[i][0], parameters[i][1]);
            }
        }

        try {
            query.executeUpdate();
        }
        catch (Exception ex) {
            rollback();
            throw new RuntimeException(ex);
        }
    }

    /**
     * Salvar dentro de uma transa��o objeto de classe de entidade
     * 
     * @param domain = Objeto de classe de entidade
     */
    @Override
    public Object save(final Object domain) {
        log.debug("Pedido de grava��o em transa��o de objeto: " + domain.getClass().getName());
        Object result = getManager().merge(domain);
        log.debug(">>> Gravado em transa��o objeto: " + result.getClass().getName());
        return result;
    }

    /**
     * Excluir em transa��o objeto de classe de entidade
     * 
     * @param domain = Objeto de classe de entidade
     */
    @Override
    public void delete(final Object domain) {
        log.debug("Pedido de exclus�o em transa��o de objeto: " + domain.getClass().getName());
        getManager().remove(getManager().merge(domain));
        log.debug(">>> Exclu�do na transa��o objeto: " + domain.getClass().getName());
    }
}