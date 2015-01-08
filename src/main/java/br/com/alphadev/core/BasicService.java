
package br.com.alphadev.core;

import java.util.Collection;

/**
 * @author André Penteado
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