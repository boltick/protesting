package org.protesting.jft.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.protesting.jft.utils.ParseHelper;
import org.protesting.jft.utils.TemplateFilenameFilter;
import org.w3c.dom.Element;
import sun.security.util.Resources;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.*;

/**
 * Main configuration class. Used for configuration of Jft properties from defined property file
 * User: ab83625
 * Date: 14.02.2008
 * Time: 11:43:59
 */
public class Configurator {

    protected static Log logger = LogFactory.getLog(Configurator.class);

    public static final String TEST_CASE_TYPE = "test.case.type";
    public static final String JFT_CONFIG_PATH = "jft.config.path";

    public static final String CUSTOM_TEMPLATE_PATH = "custom.templates.path";
    public static final String OUTPUT_DATA_SOURCE = "output.data.source";
    public static final String OUTPUT_PATH = "output.path";

    public static final String INPUT_DATA_SOURCE = "input.data.source";
    public static final String INPUT_PATH = "input.path";

    private Properties properties;
    private static Map symbols;
    private static Configurator configurator;
    private static Map templates;


    private Configurator() {
    }

    /**
     * Default constructor
     */
    private Configurator(Properties properties) {
        super();
        this.properties = properties;
    }

    /**
     * Static initialization method is used for initialization of property file data
     * @param propertyFile - Property file
     * @throws java.io.IOException, if property file does not exist
     */
    public static void init(File propertyFile) throws IOException {
        Properties properties = new Properties();
        if (propertyFile.exists()) {
            properties.load(new FileInputStream(propertyFile));
        } else {
            init(Resources.getBundle("jft"));
        }
        configurator = new Configurator(properties);
        configurator.initSupportedSymbols();
    }

    public static void init(InputStream propertyInputStream) throws IOException {
        Properties properties = new Properties();
        if (null != propertyInputStream) {
            properties.load(propertyInputStream);
        }
        configurator = new Configurator(properties);
        configurator.initSupportedSymbols();
        configurator.initResultDir();
        configurator.initTemplates();
    }

    public static void init(ResourceBundle resourceBundle) throws IOException {
        Properties properties = new Properties();
        Enumeration  keys = resourceBundle.getKeys();
        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            properties.put(key, resourceBundle.getString(key));
        }
        configurator = new Configurator(properties);
        configurator.initSupportedSymbols();
    }



    /**
     * Static propertes Getter
     * @return Propertis object with Jft intialized Jft properties
     */
    public Properties getProperties() {
        return properties;
    }

    /**
     * Method for getting of value of defined properties key
     * @param key properties key
     * @return value of properties <code>key</code>
     */
    public String getProperty(String key) {
        if (getProperties().getProperty(key) == null) {
            throw new IllegalStateException("Property ["+key+"] is not defined");
        }
        return getProperties().getProperty(key).trim();
    }

    /**
     * Private method fro initialization of supported for generation symbols
     */
    private void initSupportedSymbols() {
        symbols = new HashMap();
        Enumeration en = properties.keys();
        logger.debug("Supported Symbols init - started");
        while (en.hasMoreElements()) {
            String key = (String) en.nextElement();
            if(key.startsWith("symbols.")) {
                if (checkSymbolsSetup(key)) {
                    if (Boolean.valueOf(properties.get(key).toString()).booleanValue()) {
                        symbols.put(key, Boolean.valueOf(properties.get(key).toString()));
                        logger.debug(key+"="+ properties.get(key));
                    }
                }
            }
        }
        logger.debug("Supported Symbols init - finished");
    }

    /**
     * Private method for checking completeness of symbols configuration
     * @param key symbol properties key
     * @return true, if symbol has complete configuration <br>
     *         false, if in all other cases
     */
    private boolean checkSymbolsSetup(String key) {
        if(GeneratorConfig.getInstance().getSymbolCharRange(key) == null || GeneratorConfig.getInstance().getSymbolCharRange(key).equals("")) {
            logger.debug("Char Range is no definet for key ["+key+"]");
            return false;
        }
        if (GeneratorConfig.getInstance().getSymbolProperty(key, GeneratorConfig.SYMBOL_ALIASE) == null
                || GeneratorConfig.getInstance().getSymbolProperty(key, GeneratorConfig.SYMBOL_ALIASE).equals("")) {
            logger.debug("Symbol key ["+key+"] is not properly defined in Generator.xml");
            return false;
        }
        return true;
    }

    /**
     * Method returms map of supposrted for generation symbols
     * @return Map of supported for Jft symbols
     */
    public Map getSupportedSymbols() {
        return symbols;
    }

    void initResultDir() {
        String output =
                getInstance().getProperties().getProperty(OUTPUT_PATH) == null ?
                        System.getProperty("user.dir") : getInstance().getProperties().getProperty(OUTPUT_PATH);
        File resultDir = new File(output);
        if (!resultDir.exists() && !resultDir.isDirectory()) {
            resultDir.mkdir();
        }
    }


    void initTemplates() {
        templates = new HashMap();
        File dir = null;
        try {
            dir = new File(System.getProperty("user.dir"), getInstance().getProperty(CUSTOM_TEMPLATE_PATH));
            if(!dir.exists()) {
                dir = new File(Configurator.getInstance().getClass().getClassLoader().getResource(getInstance().getProperty(CUSTOM_TEMPLATE_PATH)).toURI());
            }
        } catch (URISyntaxException e) {
            logger.error("Error reading templates directory: " + getInstance().getProperty(CUSTOM_TEMPLATE_PATH));
            e.printStackTrace();
        }
        File [] list = dir.listFiles(new TemplateFilenameFilter());
        if (list == null || list.length == 0) {
            logger.debug("No Templates available");
            return;
        }

        for (int i = 0; i < list.length; i++) {
            File file = list[i];
            try {
                if (file.isDirectory()) continue;
                Element docEle = ParseHelper.getDocument(file).getDocumentElement();

                if (docEle.getNodeName().equalsIgnoreCase("type")) {
                    String name = docEle.getAttributes().getNamedItem("name").getNodeValue();
                    if (templates.containsKey(name)) {
                        logger.warn("Non unique template: name=["+ name+"] and path: " + file.getPath());
                    } else {
                        templates.put(name, file.getPath());
                    }
                }
            } catch (Exception ex) {
                logger.warn("file " + file.getName() + " is skipped. " + ex);
            }
        }
    }

    public File getTemplateFile(String templateName) {
        String fileName = (String)templates.get(templateName);
        if (fileName != null) {
            return new File(fileName);
        }

        logger.error("Source Template ["+templateName+"] is not found in folder: " + getProperty("custom.templates.path"));
        throw  new IllegalStateException("Source Template ["+templateName+"] is not found in folder: " + getProperty("custom.templates.path"));
    }

    public static Configurator getInstance() {
        return null != configurator ? configurator : new Configurator();
    }
}
