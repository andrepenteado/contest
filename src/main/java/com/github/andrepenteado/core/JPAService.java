
package com.github.andrepenteado.core;

import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.github.andrepenteado.util.Log4jWrapper;


/**
 * @author André Penteado
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
                log.debug(">>> Paramêtro: " + parameters[i][0] + " = " + parameters[i][1]);
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
                log.debug(">>> Paramêtro: " + parameters[i][0] + " = " + parameters[i][1]);
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
                log.debug(">>> Paramêtro: " + parameters[i][0] + " = " + parameters[i][1]);
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
        log.debug(ret == null ? ">>> Não Encontrado " : ">>> Encontrado ");
        return ret;
    }

    /**
     * Buscar objeto de uma classe de entidade pela chave primária
     * 
     * @param entityClass = Classe de entidade
     * @param id = Chave primária
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
        log.debug((result == null ? ">>> Não Encontrado " : ">>> Encontrado ") + entityClass.getName() + " de ID " + id);
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
        log.debug((ret == null ? ">>> Não Encontrado " : ">>> Encontrado ") + entityClass.getName() + " por campo: " + query.toString());
        return ret;
    }

    /**
     * Executar EJB-QL de alteração em lote
     * 
     * @param ejbql = Sintaxe EJB-QL da consulta a ser executada
     * @param parameters Matriz com os nomes e valores dos parametros <br>
     *               Posicao 0 - Nome do parametro <br>
     *               Posicao 1 - Valor do parametro <br> <br>
     *               Exemplo: parameters[0] = {"codigo","10"}; parameters[1] = {"nome", "jose"};
     */
    @Override
    public void executeUpdate(final String ejbql, final Object[]... parameters) {
        log.debug("Executando EJB-QL de alteração em lote: " + ejbql);

        final Query query = getManager().createQuery(ejbql);

        if (parameters != null) {
            for (int i = 0; i < parameters.length; i++) {
                if (parameters[i][0] == null)
                    continue;
                log.debug(">>> Paramêtro: " + parameters[i][0] + " = " + parameters[i][1]);
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
     * Salvar dentro de uma transação objeto de classe de entidade
     * 
     * @param domain = Objeto de classe de entidade
     */
    @Override
    public Object save(final Object domain) {
        log.debug("Pedido de gravação em transação de objeto: " + domain.getClass().getName());
        Object result = getManager().merge(domain);
        log.debug(">>> Gravado em transação objeto: " + result.getClass().getName());
        return result;
    }

    /**
     * Excluir em transação objeto de classe de entidade
     * 
     * @param domain = Objeto de classe de entidade
     */
    @Override
    public void delete(final Object domain) {
        log.debug("Pedido de exclusão em transação de objeto: " + domain.getClass().getName());
        getManager().remove(getManager().merge(domain));
        log.debug(">>> Excluído na transação objeto: " + domain.getClass().getName());
    }
}