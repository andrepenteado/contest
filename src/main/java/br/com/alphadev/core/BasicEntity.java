
package br.com.alphadev.core;

import org.apache.commons.beanutils.BeanUtils;

import br.com.alphadev.util.Log4jWrapper;


public abstract class BasicEntity<T> extends Connection {

    private Class<T> genericClass;

    public BasicEntity(Class<T> genericClass) {
        this.genericClass = genericClass;
        this.log = new Log4jWrapper(genericClass, null);
    }

    @SuppressWarnings("unchecked")
    public BasicEntity(Class<T> genericClass, Long id) {
        this.genericClass = genericClass;
        this.log = new Log4jWrapper(genericClass, null);
        T result = (T)getService().findByPK(genericClass, id);
        log.info("Buscar ".concat(this.toString()).concat(" ID: ").concat(Long.toString(id)));
        try {
            BeanUtils.copyProperties(this, result);
        }
        catch (Exception ex) {
        }
    }

    @SuppressWarnings("unchecked")
    public T buscar() {
        T result = (T)getService().findByPK(genericClass, getId());
        log.info("Buscar ".concat(this.toString()).concat(" ID: ").concat(Long.toString(getId())));
        return result;
    }

    @SuppressWarnings("unchecked")
    public T buscar(Long id) {
        T result = (T)getService().findByPK(genericClass, id);
        log.info("Buscar ".concat(this.toString()).concat(" ID: ").concat(Long.toString(id)));
        return result;
    }

    public void gravar() {
        getService().beginTransaction();
        try {
            getService().save(this);
            getService().commit();
            log.info("Gravar ".concat(this.toString()));
        }
        catch (Exception ex) {
            getService().rollback();
            throw new RuntimeException(ex);
        }
    }

    public void excluir() {
        getService().beginTransaction();
        try {
            getService().delete(this);
            getService().commit();
            log.info("Excluir ".concat(this.toString()));
        }
        catch (Exception ex) {
            getService().rollback();
            throw new RuntimeException(ex);
        }
    }

    public abstract Long getId();

    public abstract void setId(Long id);
}