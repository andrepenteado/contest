
package br.com.alphadev.core;

import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import br.com.alphadev.util.ConfigHelper;
import br.com.alphadev.util.Log4jWrapper;
import br.com.alphadev.util.SettingsConfig;
import br.com.alphadev.util.UsuarioLogadoWrapper;

public class Connection {

    @PersistenceContext(unitName = SettingsConfig.K_CONTEST_PU)
    private EntityManager em;

    private Service service;

    protected UsuarioLogadoWrapper userLogin;

    protected Object[] params;

    protected Log4jWrapper log = new Log4jWrapper(Connection.class, null);

    public void setParameters(Object[] params) {
        if (params != null && params[0] instanceof UsuarioLogadoWrapper) {
            this.userLogin = (UsuarioLogadoWrapper)params[0];
            this.log = new Log4jWrapper(Connection.class, userLogin);
        }
        this.params = params;
    }

    protected Service getService() {
        try {
            if (this.service != null)
                return this.service;

            if (this.em == null) {
                try {
                    this.em = (EntityManager)new InitialContext().lookup("java:/comp/env/" + SettingsConfig.K_CONTEST_PU);
                }
                catch (Exception ex) {
                }
            }

            if (this.em != null)
                this.service = new ContainerService(this.em);
            else
                this.service = new ApplicationService();
        }
        catch (Exception ex) {
            log.error(ConfigHelper.get().getString("error.general"), ex);
        }

        return this.service;
    }

}
