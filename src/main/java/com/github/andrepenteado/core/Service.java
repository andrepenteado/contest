
package com.github.andrepenteado.core;

import java.util.Collection;

public interface Service {

    public Collection<?> executeQuery(final String query, final Object[]... parameters);

    public Collection<?> executeNamedQuery(final String queryName, final Object[]... parameters);
    
    public void executeUpdate(final String updateQuery, final Object[]... parameters);

    public Object executeFind(final String query, final Object[]... parameters);

    public <T> T findByPK(final Class<T> domain, final Long id);

    public <T> T findByField(final Class<T> domain, final String field, final Object value);
    
    public void beginTransaction();
    
    public void commit();
    
    public void rollback();
    
    public Object save(Object domain);
    
    public void delete(Object domain);
}
