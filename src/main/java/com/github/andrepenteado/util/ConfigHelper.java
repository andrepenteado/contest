
package com.github.andrepenteado.util;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import java.util.TreeMap;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;


/**
 * @author André Penteado
 * @since 12/04/2007 - 18:29:30
 */
public class ConfigHelper {

    private static CompositeConfiguration config = null;
    private static Map<String, Configuration> configTypes = null;

    public static Configuration get() {
        if (config == null) {
            load();
        }
        return config;
    }

    /**
     * Busca e separa em um array propriedade com valores separados
     *
     * por um delimitador (ex: prop = "joao|jose|maria" -> {"joao", "jose", "maria"} 
     * @param property Nome da propriedade
     * @param delimiter Delimitador que separa os valores
     * @return Array com os valores
     */
    public static String[] getStringArray(final String property, final String delimiter) {
        final StringTokenizer st = new StringTokenizer(config.getString(property), delimiter);
        final int tokens = st.countTokens();
        String[] result = new String[tokens];
        for (int i = 0; i < tokens; i++)
            result[i] = st.nextToken().replace(delimiter, "").trim();
        return result;
    }

    /**
     * Busca a propriedade alterando com o parametro
     *
     * @param property Propriedade
     * @param parameter Parametro
     * @return Propriedade formatada
     */
    public static String getProperty(final String property, final String parameter) {
        return getProperty(property, new String[] { parameter });
    }

    /**
     * Busca a propriedade alterando com parametros
     *
     * @param property Propriedade
     * @param parameters Parametros
     * @return Propriedade formatada
     */
    public static String getProperty(final String property, final String[] parameters) {
        String result = config.getString(property);
        final int qtd = parameters.length;
        for (int i = 0; i < qtd; i++)
            result = result.replace("{" + i + "}", parameters[i]);
        if (result != null)
            return result.toString();
        return "";
    }

    /**
     * Busca configurações de um subconjunto baseado no nome
     * do arquivo das configurações
     * 
     * @param configName = Nome do subconjunto baseado no nome
     *                     de arquivo das configurações
     * @return Mapa com chave e valor das configurações
     */
    public static Map<String, String> getConfigByName(String configName) {
        Map<String, String> result = new TreeMap<String, String>();
        Iterator<Entry<String, Configuration>> it = configTypes.entrySet().iterator();

        while (it.hasNext()) {
            Entry<String, Configuration> entry = it.next();
            String nomeArquivo = entry.getKey();
            Configuration config = entry.getValue();

            if ((config instanceof PropertiesConfiguration || config instanceof XMLConfiguration)
                            && nomeArquivo.toUpperCase().contains(configName.toUpperCase())) {
                Iterator<String> keys = config.getKeys();

                while (keys.hasNext()) {
                    String k = keys.next();
                    result.put(k, config.getString(k));
                }
            }
        }

        return result;
    }

    /**
     * NÃO FUNCIONA, NÃO USAR
     * 
     * Setar propriedade em determinada configuração
     * 
     * NÃO FUNCIONA, NÃO USAR
     *
     * @param configName = Nome da configuração
     * @param key = Chave
     * @param value = Valor da propriedade
     * 
     * NÃO FUNCIONA, NÃO USAR
     */
    /*public static void setPropertyInConfig(String configName, String key, String value) throws Exception {
        Configuration cfg = configTypes.get(configName);
        if (cfg instanceof PropertiesConfiguration) {
            PropertiesConfiguration pcfg = (PropertiesConfiguration)cfg;
            pcfg.setProperty(key, value);
            pcfg.save();
        }
        else if (cfg instanceof XMLConfiguration) {
            XMLConfiguration xcfg = (XMLConfiguration)cfg;
            xcfg.setProperty(key, value);
            xcfg.save();
        }
    }*/

    /**
     * Carregar configurações default
     */
    public static void load() {
        load(null);
    }

    /**
     * Carregar configurações de determinado dominio.
     * 
     * As configurações serão carregadas do diretório específicado
     * pela propriedade de sistema SettingsConfig.K_PROPRIEDADE_ROOT_CLASSPATH.
     * 
     * Por definição, o diretório base das configurações é "/config".
     * 
     * Também é levado em consideração o ambiente de execução que
     * é o nome da máquina sem o sufixo do domínio. Por Exemplo:
     * máquina "devel.fc.unesp.br", o ambiente será "devel".
     * 
     * A ordem dos diretórios que prevalecará é a seguinte:
     * 1o. /config/(ambiente de execução)/(domínio)/*
     * 2o. /config/(domínio)/*
     * 3o. /config/(ambiente de execução)/*
     * 4o. /config/*
     * 
     * Serão detectados arquivos de configuração de extensão
     * *.properties e *.xml
     * 
     * @domain = O domínio pode ser por exemplo: FAAC, FC, UNESP, CPBAURU, MM, etc ...
     *
     */
    public synchronized static void load(String domain) {
        try {
            String enviromentPath = SettingsConfig.K_CAMINHO_CONFIG + FunctionsHelper.getLocalHostName() + "/";
            String enviromentDomainPath = domain != null ? enviromentPath + domain + "/" : null;
            String domainPath = domain != null ? SettingsConfig.K_CAMINHO_CONFIG + domain + "/" : null;
            String[] orderByFindPaths = new String[] { enviromentDomainPath, domainPath, enviromentPath, SettingsConfig.K_CAMINHO_CONFIG };

            configTypes = new HashMap<String, Configuration>();
            config = new CompositeConfiguration();
            config.clear();

            // Carregar configuração específicas do projeto (podendo substituir as default)
            for (int j = 0; j < orderByFindPaths.length; j++) {
                if (orderByFindPaths[j] == null)
                    continue;

                String dirAppConfig = System.getProperty(SettingsConfig.K_PROPRIEDADE_ROOT_CLASSPATH) + orderByFindPaths[j];
                File[] listAppFiles = new File(dirAppConfig).listFiles();

                if (listAppFiles == null) {
                    System.out.println("Diretório de configuração vazio: " + dirAppConfig);
                    continue;
                }

                System.out.println("Carregando configurações do diretório: " + dirAppConfig);

                for (int i = 0; i < listAppFiles.length; i++) {
                    if (listAppFiles[i].isFile()) {
                        Configuration cfgFile = null;
                        if (listAppFiles[i].getName().endsWith(".properties")) {
                            PropertiesConfiguration cfgProp = new PropertiesConfiguration(dirAppConfig + listAppFiles[i].getName());
                            cfgProp.setReloadingStrategy(new FileChangedReloadingStrategy());
                            config.addConfiguration(cfgProp);
                            cfgFile = cfgProp;
                            System.out.println("Carregado arquivo de configuração: " + listAppFiles[i].getName());
                        }
                        else if (listAppFiles[i].getName().endsWith(".xml")) {
                            XMLConfiguration cfgXml = new XMLConfiguration(dirAppConfig + listAppFiles[i].getName());
                            cfgXml.setReloadingStrategy(new FileChangedReloadingStrategy());
                            config.addConfiguration(cfgXml);
                            cfgFile = cfgXml;
                            System.out.println("Carregado arquivo de configuração: " + listAppFiles[i].getName());
                        }
                        configTypes.put(listAppFiles[i].getName(), cfgFile);
                    }
                }
            }

            // Carregar configurações default de core.properties e core.xml
            /*String[] resourcesDefault = new String[] { "/core.properties", "/core.xml" };
            for (int i = 0; i < resourcesDefault.length; i++) {
                InputStream stream = null;
                String stripped = resourcesDefault[i].startsWith("/") ? resourcesDefault[i].substring(1) : resourcesDefault[i];
                ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                if (classLoader != null)
                    stream = classLoader.getResourceAsStream(stripped);
                if (stream == null)
                    ConfigHelper.class.getResourceAsStream(resourcesDefault[i]);
                if (stream == null)
                    stream = ConfigHelper.class.getClassLoader().getResourceAsStream(stripped);
                if (stream == null)
                    throw new RuntimeException(resourcesDefault[i] + " not found");
                else {
                    byte[] byteArray = new byte[stream.available()];
                    stream.read(byteArray);

                    File fTemp = File.createTempFile(resourcesDefault[i].replace(".", ""), "dat");
                    FileOutputStream fOut = new FileOutputStream(fTemp);
                    fOut.write(byteArray);
                    fOut.flush();
                    fOut.close();

                    Configuration cfg = (i == 0 ? new PropertiesConfiguration(fTemp) : new XMLConfiguration(fTemp));
                    config.addConfiguration(cfg);
                    configTypes.put(fTemp.getName(), cfg);
                    System.out.println("Carregado arquivo de configuração: core.jar [" + resourcesDefault[i] + "]");
                    fTemp.delete();
                }
            }*/
        }
        catch (Exception ex) {
            System.err.println("Configurações não carregadas");
            ex.printStackTrace();
        }
    }
}
