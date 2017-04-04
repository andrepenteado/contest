
package com.github.andrepenteado.web;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.mentawai.ajax.AjaxConsequence;
import org.mentawai.ajax.AjaxRenderer;
import org.mentawai.ajax.renderer.MapAjaxRenderer;
import org.mentawai.core.ActionConfig;
import org.mentawai.core.ApplicationManager;
import org.mentawai.core.BaseAction;
import org.mentawai.core.Chain;
import org.mentawai.core.Consequence;
import org.mentawai.core.Forward;
import org.mentawai.core.Redirect;
import org.mentawai.core.StreamConsequence;
import org.mentawai.filter.AuthenticationFilter;
import org.mentawai.filter.AuthorizationFilter;
import org.mentawai.filter.ExceptionFilter;
import org.mentawai.filter.FileUploadFilter;
import org.mentawai.filter.ValidatorFilter;
import org.mentawai.list.ListManager;
import org.mentawai.list.SimpleListData;

import com.github.andrepenteado.annotations.ActionClass;
import com.github.andrepenteado.annotations.ConsequenceOutput;
import com.github.andrepenteado.annotations.ConsequenceType;
import com.github.andrepenteado.annotations.Consequences;
import com.github.andrepenteado.util.ConfigHelper;
import com.github.andrepenteado.util.Log4jWrapper;
import com.github.andrepenteado.util.SettingsConfig;

/**
 * @author Andre Penteado
 * @since 06/03/2008 - 15:37:33
 */
public abstract class AbstractActionsManager extends ApplicationManager {

    private final Log4jWrapper log = new Log4jWrapper(AbstractActionsManager.class, null);

    public abstract String getPaginaErro();

    public abstract String getPaginaLogin();

    public abstract String getPaginaAcessoNegado();

    @SuppressWarnings("rawtypes")
    private HashSet<Class> annotatedClasses = new HashSet<Class>();

    @Override
    public void loadActions() {
        // Debug
        setDebugMode(ConfigHelper.get().getBoolean("sistema.debug"));

        // Diretório das páginas JSPs
        setViewDir(SettingsConfig.K_CAMINHO_JSPs);

        // Filtro para exibir página de erro em caso de exceções
        addGlobalFilter(new ExceptionFilter());
        addGlobalConsequence(EXCEPTION, new Forward(getPaginaErro()));

        // Filtro para upload de arquivos
        addGlobalFilter(new FileUploadFilter());

        // Filtro para autenticação
        addGlobalFilter(new AuthenticationFilter());
        addGlobalConsequence(AuthenticationFilter.LOGIN, new Redirect(getPaginaLogin()));
        addGlobalConsequence(AuthorizationFilter.ACCESSDENIED, new Forward(getPaginaAcessoNegado()));

        // Filtro para autorização das actions
        addGlobalFilter(new AuthorizationFilter());

        // Filtro para validação dentro das actions
        addGlobalFilter(new ValidatorFilter());

        //Consequencias anotadas diretamente em classes
        carregarClassesAcaoAnotadas();
    }

    @Override
    public void loadLists() throws IOException {
        Map<String, String> listas = ConfigHelper.getConfigByName("listas");
        for (Map.Entry<String, String> entry : listas.entrySet()) {
            if (entry.getKey().indexOf(".") < 0) {
                log.debug("Carregando lista: " + entry.getKey());
                SimpleListData listaMentawai = new SimpleListData(entry.getKey());
                List<Object> listaProperties = ConfigHelper.get().getList(entry.getKey());
                if (listaProperties != null && listaProperties.size() > 0) {
                    for (int i = 0; i < listaProperties.size(); i++)
                        listaMentawai.add(listaProperties.get(i).toString(), ConfigHelper.get().getString(
                                        entry.getKey().concat(".").concat(listaProperties.get(i).toString())));
                }
                ListManager.addList(listaMentawai);
            }
        }
    }

    private void procurarClassesAcaoAnotadas(File f) throws IOException {
        if (f == null)
            f = new File(getRealPath() + File.separator + "WEB-INF" + File.separator + "classes");
        if (f.isDirectory()) {
            // check directory...
            File[] childs = f.listFiles();
            for (File fl : childs) {
                procurarClassesAcaoAnotadas(fl);
            }
        }
        else if (f.isFile()) {
            String filename = f.toString();

            // Precisa estar num dos subdiretórios de .actions.
            if (!filename.endsWith("class") || !filename.contains(File.separator + "actions" + File.separator))
                return;
            filename = filename.replace(getRealPath() + File.separator + "WEB-INF" + File.separator + "classes" + File.separator, "");
            filename = filename.replace(".class", "");
            filename = filename.replace(File.separator, ".");

            try {
                Class<? extends Object> klass = Class.forName(filename);
                if (BaseAction.class.isAssignableFrom(klass)) {
                    log.debug("Carregando classe anotada: " + filename);
                    annotatedClasses.add(klass);
                }
            }
            catch (Exception e) {
                log.error("Erro! não foi instanciada a classe:" + filename, e);
            }
        }
    }

    public void carregarClassesAcaoAnotadas() {
        ActionClass annotation;
        ActionConfig ac;
        ConsequenceOutput[] mapConsequences;
        AjaxRenderer ajaxRender;
        Consequence customObject;
        ArrayList<ActionConfig> acListForChains = new ArrayList<ActionConfig>();
        String[] explodedParameter; //Usado para explodir parameters no caso de uma chain 

        // Para carregamento das Chains
        ArrayList<String> actionName = new ArrayList<String>();
        ArrayList<String> innerActionName = new ArrayList<String>();
        ArrayList<String> actionChain = new ArrayList<String>();
        ArrayList<String> innerChain = new ArrayList<String>();

        try {
            procurarClassesAcaoAnotadas(null);
        }
        catch (Exception e) {
            log.error("Não foi possível carregar pacote com classes anotadas: ", e);
            e.printStackTrace();
            return;
        }
        if (annotatedClasses.size() > 0) {
            for (Class<? extends Object> klass : annotatedClasses) {
                if (klass.isAnnotationPresent(ActionClass.class)) {
                    log.debug("Carregando ações da classe: " + klass.getName());
                    annotation = klass.getAnnotation(ActionClass.class);
                    ac = new ActionConfig(annotation.prefix(), klass);

                    for (Method method : klass.getMethods()) {
                        if (method.isAnnotationPresent(Consequences.class)) {
                            log.debug("Carregando consequência: " + annotation.prefix() + "." + method.getName());
                            mapConsequences = method.getAnnotation(Consequences.class).outputs();

                            if (mapConsequences != null && mapConsequences.length > 0) {
                                for (ConsequenceOutput output : mapConsequences) {
                                    if (output.type() == ConsequenceType.FORWARD) {
                                        // Caso seja a consequencia padrão, mapear sucesso e erro para a mesma página
                                        if (ConsequenceOutput.SUCCESS_ERROR.equals(output.result())) {
                                            ac.addConsequence(SUCCESS, method.getName(), new Forward(output.page()));
                                            ac.addConsequence(ERROR, method.getName(), new Forward(output.page()));
                                        }
                                        else
                                            ac.addConsequence(output.result(), method.getName(), new Forward(output.page()));
                                    }
                                    else if (output.type() == ConsequenceType.REDIRECT) {
                                        ac.addConsequence(output.result(), method.getName(), "".equals(output.page()) ? new Redirect(output
                                                        .RedirectWithParameters()) : new Redirect(output.page(), output.RedirectWithParameters()));
                                    }
                                    else if (output.type() == ConsequenceType.STREAMCONSEQUENCE) {
                                        ac.addConsequence(output.result(), method.getName(), "".equals(output.page()) ? new StreamConsequence()
                                                        : new StreamConsequence(output.page()));
                                    }
                                    else if (output.type() == ConsequenceType.AJAXCONSEQUENCE) {
                                        try {
                                            ajaxRender = (AjaxRenderer)Class.forName(output.page()).newInstance();
                                            ac.addConsequence(output.result(), method.getName(), new AjaxConsequence(ajaxRender));
                                        }
                                        catch (InstantiationException ex) {
                                            ac.addConsequence(output.result(), method.getName(), new AjaxConsequence(new MapAjaxRenderer()));
                                        }
                                        catch (Exception ex) {
                                            log.debug("Não foi possível carregar AjaxConsequence. Carregado o padrão MapAjaxRenderer");
                                            ex.printStackTrace();
                                        }
                                    }
                                    else if (output.type() == ConsequenceType.CHAIN) {
                                        explodedParameter = output.page().split("\\.");
                                        actionName.add(output.result());
                                        innerActionName.add(method.getName());
                                        actionChain.add(explodedParameter[0]);
                                        innerChain.add(explodedParameter.length > 0 ? explodedParameter[1] : null);
                                        acListForChains.add(ac);
                                    }
                                    else if (output.type() == ConsequenceType.CUSTOM) {
                                        try {
                                            customObject = (Consequence)Class.forName(output.page()).newInstance();
                                            ac.addConsequence(output.result(), method.getName(), customObject);
                                        }
                                        catch (Exception ex) {
                                            log.debug("Não foi possível carregar consequência: Ação não carregada: " + klass.getSimpleName() + "."
                                                            + method.getName());
                                            ex.printStackTrace();
                                        }
                                    }
                                }
                            }
                        }
                    }
                    add(ac);
                }
            }
        }
        //Carrega as chains atrasadas porque os ActionConfigs podem ainda nao ter sido carregados
        int length = actionName.size();
        Chain chain;
        for (int i = 0; i < length; i++) {
            try {
                ac = acListForChains.get(i);
                if (innerChain.get(i) == null)
                    chain = new Chain(getActionConfig(actionChain.get(i)));
                else
                    chain = new Chain(getActionConfig(actionChain.get(i)), innerChain.get(i));
                ac.addConsequence(actionName.get(i), innerActionName.get(i), chain);

            }
            catch (Exception e) {
                log.debug("Não foi possível carregar consequência chain: ação não carregada: " + actionChain.get(i) + "." + innerActionName.get(i));
            }
        }
    }
}