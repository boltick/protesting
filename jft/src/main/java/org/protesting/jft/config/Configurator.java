package org.protesting.jft.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import sun.security.util.Resources;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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

    private static Properties properties;
    private static Map symbols;

    /**
     * static initialization of log folder and Jft Configuration logger
     */
    static {
//        File log = new File("logs");
//        if (!log.exists()) {
//            log.mkdir();
//        }
//        logger = LogFactory.getLog(Configurator.class);
    }

    /**
     * Default constructor
     */
    Configurator() {
        new GeneratorConfig();
        new RequirementConfig();
    }

    /**
     * Static initialization method is used for initialization of property file data
     * @param propertyFile - Property file
     * @throws java.io.IOException, if property file does not exist
     */
    public static void init(File propertyFile) throws IOException {
        properties = new Properties();
        if (propertyFile.exists()) {
            properties.load(new FileInputStream(propertyFile));
        } else {
            init(Resources.getBundle("jft"));
//            throw new IllegalStateException("Jft properties are not defined");
        }
        initSupportedSymbols();
    }

    public static void init(ResourceBundle resourceBundle) throws IOException {
        properties = new Properties();
        Enumeration  keys = resourceBundle.getKeys();
        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            properties.put(key, resourceBundle.getString(key));
        }
        initSupportedSymbols();
    }

    /**
     * Static propertes Getter
     * @return Propertis object with Jft intialized Jft properties
     */
    public static Properties getProperties() {
        return properties;
    }

    /**
     * Method for getting of value of defined properties key
     * @param key properties key
     * @return value of properties <code>key</code>
     */
    public static String getProperty(String key) {
        if (getProperties().getProperty(key) == null) {
            throw new IllegalStateException("Property ["+key+"] is not defined");
        }
        return getProperties().getProperty(key).trim();
    }

    /**
     * Private method fro initialization of supported for generation symbols
     */
    private static void initSupportedSymbols() {
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
    private static boolean checkSymbolsSetup(String key) {
        if(GeneratorConfig.getSymbolCharRange(key) == null || GeneratorConfig.getSymbolCharRange(key).equals("")) {
            logger.debug("Char Range is no definet for key ["+key+"]");
            return false;
        }
        if (GeneratorConfig.getSymbolPropery(key, GeneratorConfig.SYMBOL_ALIASE) == null
                || GeneratorConfig.getSymbolPropery(key, GeneratorConfig.SYMBOL_ALIASE).equals("")) {
            logger.debug("Symbol key ["+key+"] is not properly defined in Generator.xml");
            return false;
        }
        return true;
    }

    /**
     * Method returms map of supposrted for generation symbols
     * @return Map of supported for Jft symbols
     */
    public static Map getSupportedSymbols() {
        return symbols;
    }

}
