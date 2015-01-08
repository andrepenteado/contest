
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
