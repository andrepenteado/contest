
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
 * @author Andr� Penteado
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
     * Busca configura��es de um subconjunto baseado no nome
     * do arquivo das configura��es
     * 
     * @param configName = Nome do subconjunto baseado no nome
     *                     de arquivo das configura��es
     * @return Mapa com chave e valor das configura��es
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
     * N�O FUNCIONA, N�O USAR
     * 
     * Setar propriedade em determinada configura��o
     * 
     * N�O FUNCIONA, N�O USAR
     *
     * @param configName = Nome da configura��o
     * @param key = Chave
     * @param value = Valor da propriedade
     * 
     * N�O FUNCIONA, N�O USAR
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
     * Carregar configura��es default
     */
    public static void load() {
        load(null);
    }

    /**
     * Carregar configura��es de determinado dominio.
     * 
     * As configura��es ser�o carregadas do diret�rio espec�ficado
     * pela propriedade de sistema SettingsConfig.K_PROPRIEDADE_ROOT_CLASSPATH.
     * 
     * Por defini��o, o diret�rio base das configura��es � "/config".
     * 
     * Tamb�m � levado em considera��o o ambiente de execu��o que
     * � o nome da m�quina sem o sufixo do dom�nio. Por Exemplo:
     * m�quina "devel.fc.unesp.br", o ambiente ser� "devel".
     * 
     * A ordem dos diret�rios que prevalecar� � a seguinte:
     * 1o. /config/(ambiente de execu��o)/(dom�nio)/*
     * 2o. /config/(dom�nio)/*
     * 3o. /config/(ambiente de execu��o)/*
     * 4o. /config/*
     * 
     * Ser�o detectados arquivos de configura��o de extens�o
     * *.properties e *.xml
     * 
     * @domain = O dom�nio pode ser por exemplo: FAAC, FC, UNESP, CPBAURU, MM, etc ...
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

            // Carregar configura��o espec�ficas do projeto (podendo substituir as default)
            for (int j = 0; j < orderByFindPaths.length; j++) {
                if (orderByFindPaths[j] == null)
                    continue;

                String dirAppConfig = System.getProperty(SettingsConfig.K_PROPRIEDADE_ROOT_CLASSPATH) + orderByFindPaths[j];
                File[] listAppFiles = new File(dirAppConfig).listFiles();

                if (listAppFiles == null) {
                    System.out.println("Diret�rio de configura��o vazio: " + dirAppConfig);
                    continue;
                }

                System.out.println("Carregando configura��es do diret�rio: " + dirAppConfig);

                for (int i = 0; i < listAppFiles.length; i++) {
                    if (listAppFiles[i].isFile()) {
                        Configuration cfgFile = null;
                        if (listAppFiles[i].getName().endsWith(".properties")) {
                            PropertiesConfiguration cfgProp = new PropertiesConfiguration(dirAppConfig + listAppFiles[i].getName());
                            cfgProp.setReloadingStrategy(new FileChangedReloadingStrategy());
                            config.addConfiguration(cfgProp);
                            cfgFile = cfgProp;
                            System.out.println("Carregado arquivo de configura��o: " + listAppFiles[i].getName());
                        }
                        else if (listAppFiles[i].getName().endsWith(".xml")) {
                            XMLConfiguration cfgXml = new XMLConfiguration(dirAppConfig + listAppFiles[i].getName());
                            cfgXml.setReloadingStrategy(new FileChangedReloadingStrategy());
                            config.addConfiguration(cfgXml);
                            cfgFile = cfgXml;
                            System.out.println("Carregado arquivo de configura��o: " + listAppFiles[i].getName());
                        }
                        configTypes.put(listAppFiles[i].getName(), cfgFile);
                    }
                }
            }

            // Carregar configura��es default de core.properties e core.xml
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
                    System.out.println("Carregado arquivo de configura��o: core.jar [" + resourcesDefault[i] + "]");
                    fTemp.delete();
                }
            }*/
        }
        catch (Exception ex) {
            System.err.println("Configura��es n�o carregadas");
            ex.printStackTrace();
        }
    }
}
