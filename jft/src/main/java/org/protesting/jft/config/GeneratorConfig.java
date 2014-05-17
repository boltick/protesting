package org.protesting.jft.config;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.protesting.jft.utils.ParseHelper;

import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;
import java.util.ResourceBundle;
import java.util.Locale;
import java.util.Properties;

/**
 * Class GeneratorConfig is used for getting all needed data from configuration files
 * User: ab83625
 * Date: 26.02.2008
 * Time: 17:40:51
 */
public class GeneratorConfig {

    private static final String GENERIC_XML_FILE = "generator.xml";
    private static final String SYMBOLS_CHAR_RANGE_FILE = "range";

    public static final String GENERIC_GENERATOR = "generic-generator";
    public static final String GENERATOR_CLASS = "class";
    public static final String GENERATOR_TYPE = "type";

    public static final String TYPIFIED_GENERATOR = "typified-generator";

    public static final String SYMBOL_GENERATOR = "symbols-generator";
    public static final String SYMBOL_ID = "id";
    public static final String SYMBOL_ALIASE = "alias";
    public static final String SYMBOL_CHAR_ENCODE = "encode";

    private Element generatorClassNameElement;
    private Element symbolPropertyElement;
    private ResourceBundle symbolCharRangeBundle;
    private Properties symbolCharRangeProperties;

    private static GeneratorConfig config;

    private GeneratorConfig() throws IOException {
        if (!Configurator.getInstance().getProperties().get("jft.local").equals("true")) {
            generatorClassNameElement = ParseHelper.getConfigElement(Configurator.getInstance().getProperties().get(Configurator.JFT_CONFIG_PATH)
                    + "/"
                    + GENERIC_XML_FILE);

            symbolPropertyElement = ParseHelper.getConfigElement(Configurator.getInstance().getProperties().get(Configurator.JFT_CONFIG_PATH)
                    + "/"
                    + GENERIC_XML_FILE);

            symbolCharRangeBundle = ParseHelper.getResourceBundle(Configurator.getInstance().getProperties().get(Configurator.JFT_CONFIG_PATH)
                    + "/"
                    + SYMBOLS_CHAR_RANGE_FILE, Locale.ENGLISH);
        } else {
            generatorClassNameElement = ParseHelper.getDocument(System.getProperty("user.dir")
                    + "/"
                    + Configurator.getInstance().getProperties().get(Configurator.JFT_CONFIG_PATH)
                    + "/"
                    + GENERIC_XML_FILE).getDocumentElement();

            symbolPropertyElement = ParseHelper.getDocument(System.getProperty("user.dir")
                    + "/"
                    + Configurator.getInstance().getProperties().get(Configurator.JFT_CONFIG_PATH)
                    + "/"
                    + GENERIC_XML_FILE).getDocumentElement();

            symbolCharRangeProperties = new Properties();
            try {
                File file = new File(System.getProperty("user.dir")
                        + "/"
                        + Configurator.getInstance().getProperties().get(Configurator.JFT_CONFIG_PATH)
                        + "/"
                        + SYMBOLS_CHAR_RANGE_FILE + ".properties");
                FileInputStream inputStream = new FileInputStream(file);
                symbolCharRangeProperties.load(inputStream);
            } catch (IOException e) {
                throw new IllegalStateException("Symbol range property file read error." + e);
            }
        }
    }


    public static GeneratorConfig getInstance() {
        if (config == null) {
            try {
                config = new GeneratorConfig();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return config;
    }

    /**
     * Method for getting class name of needed generator by generator type and data type from Generator.xml
     * @param genType generator type
     * @param dataType data type
     * @return class name of needed Generator
     */
    synchronized public String getGeneratorClassName(String genType, String dataType) {
        NodeList nl = generatorClassNameElement.getElementsByTagName(genType);
        if(nl != null && nl.getLength() > 0) {
            for(int i = 0 ; i < nl.getLength();i++) {
                Element el = (Element)nl.item(i);
                NodeList nodeList = el.getElementsByTagName(dataType);
                String type = el.getAttributes().getNamedItem(GENERATOR_TYPE).getNodeValue();
                if (type.equals(dataType)) {
                    return el.getAttributes().getNamedItem(GENERATOR_CLASS).getNodeValue();
                }
            }
        }
        throw new IllegalStateException("Generic generator for type["+dataType+"] is not correctly defined");
    }

    /**
     * Method for getting char range of needed symbol from range.property
     * @param symbolID id of symbol to be
     * @return char range of defined symbol
     */
    synchronized public String getSymbolCharRange(String symbolID) {
        Object range = null;
        if (!Configurator.getInstance().getProperties().get("jft.local").equals("true")) {
            range = symbolCharRangeBundle.getString(symbolID);
        } else {
            range = symbolCharRangeProperties.get(symbolID);
        }
        if (range != null) {
            return range.toString();
        }
        throw new IllegalStateException("Symbol range for id ["+symbolID+"] is not correctly defined in ");
    }

    /**
     * Method gets defined property of defined symbol from generator.xml
     * @param symbolID - symbol id
     * @param propertyTofind - needed property
     * @return value of defined property of defined in generator.xml symbol
     */
    synchronized public String getSymbolProperty(String symbolID, String propertyTofind) {
        NodeList nl = symbolPropertyElement.getElementsByTagName(SYMBOL_GENERATOR);
        if(nl != null && nl.getLength() > 0) {
            for(int i = 0 ; i < nl.getLength();i++) {
                Element el = (Element)nl.item(i);
                NodeList nodeList = el.getElementsByTagName(symbolID);
                String type = el.getAttributes().getNamedItem(SYMBOL_ID).getNodeValue();
                if (type.equals(symbolID)) {
                    return el.getAttributes().getNamedItem(propertyTofind).getNodeValue();
                }
            }
        }
        throw new IllegalStateException("Symbol generator for id ["+symbolID+"] is not correctly defined");
    }

    public static void main(String[] args) throws IOException {
        Configurator.init(Configurator.getInstance().getClass().getClassLoader().getResource("config/jft.properties").openStream());

        String chars = GeneratorConfig.getInstance().getSymbolCharRange("symbols.japanese.hiragana");
        CharSequence sequence = new StringBuffer(chars);

        System.out.println(chars);
    }

}
