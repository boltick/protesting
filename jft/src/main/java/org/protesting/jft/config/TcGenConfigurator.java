package org.protesting.jft.config;

import org.protesting.jft.utils.ParseHelper;
import org.protesting.jft.utils.TemplateFilenameFilter;
import org.w3c.dom.Element;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * User: ab83625
 * Date: 29.07.2008
 * Time: 15:09:16
 */
public final class TcGenConfigurator extends Configurator {

    public static final String CUSTOM_TEMPLATE_PATH = "custom.templates.path";
    public static final String OUTPUT_DATA_SOURCE = "output.data.source";
    public static final String OUTPUT_PATH = "output.path";

    public static final String INPUT_DATA_SOURCE = "input.data.source";
    public static final String INPUT_PATH = "input.path";

    private static Map templates;

    static {
        System.out.println("Jft-TcGen. All rights reserved.");
        System.out.println("Copyright Alexey Bulat, protesting.ru");
    }

    public static void initTcGen(File propertyFile) throws IOException {
        Configurator.init(propertyFile);
//        Configurator.init(Resources.getBundle("jft"));
        initResultDir();
        initTemplates();
    }

    private static void initResultDir() {
        String output =
                getProperties().getProperty(OUTPUT_PATH) == null ?
                        System.getProperty("user.dir") : getProperties().getProperty(OUTPUT_PATH);
        File resultDir = new File(output);
        if (!resultDir.exists() && !resultDir.isDirectory()) {
            resultDir.mkdir();
        }
    }


    private static void initTemplates() {
        templates = new HashMap();
//        File dir = new File(Configurator.getProperty(CUSTOM_TEMPLATE_PATH));
        File dir = null;
        try {
            dir = new File(System.getProperty("user.dir")+File.separator + Configurator.getProperty(CUSTOM_TEMPLATE_PATH));
//            dir = new File(getProperty(CUSTOM_TEMPLATE_PATH));
            if(!dir.exists()) {
                dir = new File(Configurator.class.getClassLoader().getResource(getProperty(CUSTOM_TEMPLATE_PATH)).toURI());
            }
        } catch (URISyntaxException e) {
            logger.error("Error reading templates directory: " + getProperty(CUSTOM_TEMPLATE_PATH));
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

    public static File getTemplateFile(String templateName) {
        String fileName = (String)templates.get(templateName);
        if (fileName != null) {
            return new File(fileName);
        }

        logger.error("Source Template ["+templateName+"] is not found in folder: " + Configurator.getProperty("custom.templates.path"));
        throw  new IllegalStateException("Source Template ["+templateName+"] is not found in folder: " + Configurator.getProperty("custom.templates.path"));
    }


}
