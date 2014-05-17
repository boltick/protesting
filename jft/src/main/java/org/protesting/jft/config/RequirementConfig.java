package org.protesting.jft.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.protesting.jft.utils.ParseHelper;

import java.io.File;
import java.io.IOException;

/**
 * Service class for getting needed data from requirements.xml 
 * User: ab83625
 * Date: 05.02.2008
 * Time: 13:12:40
 */
public final class RequirementConfig {

    private static Log logger = LogFactory.getLog(RequirementConfig.class);

    private static final String REQUIREMENTS_XML_FILE = "requirements.xml";

    public static final String REQUIREMENT_ID = "id";

    public static final String REQUIREMENT_CLASS = "class";
    public static final String XML_PARSER_CLASS = "xml-parser-class";
    public static final String CSV_PARSER_CLASS = "csv-parser-class";
    public static final String DATA_GENERATOR_CLASS = "data-generator-class";

    private static Element requirementDataElenemt;

    private static RequirementConfig config;

    private RequirementConfig() throws IOException {
        if (!Configurator.getInstance().getProperties().get("jft.local").equals("true")) {
            requirementDataElenemt = ParseHelper.getConfigElement(Configurator.getInstance().getProperties().get(Configurator.JFT_CONFIG_PATH)
                    + "/"
                    + REQUIREMENTS_XML_FILE);
        } else {
            requirementDataElenemt = ParseHelper.getDocument(System.getProperty("user.dir")
                    + "/"
                    + Configurator.getInstance().getProperties().get(Configurator.JFT_CONFIG_PATH)
                    + "/"
                    + REQUIREMENTS_XML_FILE).getDocumentElement();
        }
    }

    public static RequirementConfig getInstance() {
        if (config == null) {
            try {
                config = new RequirementConfig();
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        return config;
    }

    /**
     * Method returns requirement data by defined required ID and data type
     * @param reqId  - ID of Requirement
     * @param data - needed requirement data
     * @return string with requirement data
     */
    synchronized public String getRequirementData(String reqId, String data) {
        NodeList nl = requirementDataElenemt.getElementsByTagName("req");
        if(nl != null && nl.getLength() > 0) {
            for(int i = 0 ; i < nl.getLength();i++) {
                Element el = (Element)nl.item(i);
                String idValue = el.getAttributes().getNamedItem(REQUIREMENT_ID).getNodeValue();
                if (idValue.equalsIgnoreCase(reqId)) {
                    el.getElementsByTagName(data);
                    NodeList nodeList = el.getElementsByTagName(data);
                    if (nodeList.getLength() != 1) {
                        throw new IllegalStateException("element " + data + " is missed or incorrectly defined");
                    }
                    return nodeList.item(0).getAttributes().getNamedItem("name").getNodeValue();
                }
            }
        }
        IllegalStateException e = new IllegalStateException("Requirement id ["+reqId+"] or needed data ["+ data +"] is not correctly defined");
        logger.error("Requirement id ["+reqId+"] or needed data ["+ data +"] is not correctly defined", e);
        throw e;
    }

}
