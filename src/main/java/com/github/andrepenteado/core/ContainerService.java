
package com.github.andrepenteado.core;

import javax.persistence.EntityManager;

public class ContainerService extends JPAService {

    private EntityManager em;

    public ContainerService(EntityManager em) {
        this.em = em;
    }

    @Override
    public void beginTransaction() {
    }

    @Override
    public void commit() {
    }

    @Override
    public void rollback() {
    }

    @Override
    protected EntityManager getManager() {
        return em;
    }

}
