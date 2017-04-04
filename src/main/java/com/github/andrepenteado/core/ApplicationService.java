
package com.github.andrepenteado.core;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import com.github.andrepenteado.util.Log4jWrapper;
import com.github.andrepenteado.util.SettingsConfig;


public class ApplicationService extends JPAService {

    private static final Log4jWrapper log = new Log4jWrapper(ApplicationService.class, null);
    private static final EntityManagerFactory factory = Persistence.createEntityManagerFactory(SettingsConfig.K_CONNECTION);
    private static final ThreadLocal<EntityManager> managerLocal = new ThreadLocal<EntityManager>();
    private static final ThreadLocal<EntityTransaction> transactionLocal = new ThreadLocal<EntityTransaction>();

    /**
     * Instaciando a classe singleton
     */
    public ApplicationService() {
    }

    /**
     * Fechar o objeto manipulador do banco
     */
    public static void closeResource() {
        if (managerLocal.get() != null && managerLocal.get().isOpen()) {
            managerLocal.get().close();
            transactionLocal.remove();
            managerLocal.remove();
        }
    }

    /**
     * @return Instancia da fabrica
     */
    private EntityManagerFactory getFactory() {
        return factory;
    }

    /**
     * Abrir o objeto manipulador do banco unico para todo o sistema (pattern sigleton)
     *
     * @return Objeto manipulador do banco.
     */
    @Override
    protected EntityManager getManager() {
        if (managerLocal.get() == null || !managerLocal.get().isOpen()) {
            final EntityManager manager = getFactory().createEntityManager();
            managerLocal.set(manager);
        }
        return managerLocal.get();
    }

    /**
     * Buscar o manipulador de transa��es �nico para o sistema
     *
     * @return Manipulador de Transa��es
     */
    private EntityTransaction getTransaction() {
        if (transactionLocal.get() == null) {
            final EntityTransaction transaction = getManager().getTransaction();
            transactionLocal.set(transaction);
        }
        return transactionLocal.get();
    }

    /**
     * Iniciar uma transa��o
     */
    @Override
    public void beginTransaction() {
        getTransaction().begin();
        log.debug("Iniciada transa��o de banco de dados");
    }

    /**
     * Consolidar altera��es de uma transa��o
     */
    @Override
    public void commit() {
        if (getTransaction().isActive()) {
            getTransaction().commit();
            log.debug("Consolidada transa��o de banco de dados");
        }
    }

    /**
     * Cancelar altera��es de uma transa��o
     */
    @Override
    public void rollback() {
        if (getTransaction().isActive()) {
            getTransaction().rollback();
            log.debug("Cancelada transa��o de banco de dados");
        }
    }
}