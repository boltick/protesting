package org.protesting.jft.data.xml;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.protesting.jft.config.RequirementConfig;
import org.protesting.jft.form.Form;
import org.protesting.jft.form.Template;
import org.protesting.jft.form.fields.Field;
import org.protesting.jft.form.requirements.Requirement;
import org.protesting.jft.config.TcGenConfigurator;
import org.protesting.jft.data.DataParser;
import org.protesting.jft.form.fields.FieldButton;
import org.protesting.jft.form.fields.FieldFactory;
import org.protesting.jft.utils.ParseHelper;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;


public class FileDataParser implements DataParser {

    private static Log logger = LogFactory.getLog(FileDataParser.class);
    private Element docEle;

    public Form getForm(File xmlFile) {
        //get the root elememt
        docEle = ParseHelper.getDocument(xmlFile).getDocumentElement();

        Form form = null;
        NodeList nl = null;
        if (docEle.getNodeName().equalsIgnoreCase("form")) {
            form = new Form(docEle.getAttributes().getNamedItem("name").getNodeValue());
        } else {
            return null;
        }

        //get a nodelist of Fields
        nl = docEle.getElementsByTagName("field");

        if (nl != null && nl.getLength() > 0) {
            for (int i = 0; i < nl.getLength(); i++) {
                //get the field element
                Element el = (Element) nl.item(i);
                Field field = getFormField(el);

                form.addField(field);
            }
        }

        nl = docEle.getElementsByTagName("button");
        if (nl != null && nl.getLength() > 0) {
            for (int i = 0; i < nl.getLength(); i++) {
                //get the field element
                Element el = (Element) nl.item(i);
                Field field = getButtonField(el);
                form.addField(field);
            }
        }

        return form;
    }

    private Element getElementByName(String name) {
        NodeList nl = docEle.getElementsByTagName("field");
        if (nl != null && nl.getLength() > 0) {
            for (int i = 0; i < nl.getLength(); i++) {
                Element el = (Element) nl.item(i);
                if (el.getAttributes().getNamedItem("name").getNodeValue().equals(name)) {
                    return el;
                }
            }
        }

        return null;
    }


    private Field getButtonField(Element el) {
        String name = "";
        String action  = "";
        if (el != null) {
            name = el.getAttributes().getNamedItem("name").getNodeValue();
            action = el.getAttributes().getNamedItem("action").getNodeValue();
        }
        return new FieldButton(name, action);
    }


    public Form getTemplate(File xmlFile) {
        Element docEle = ParseHelper.getDocument(xmlFile).getDocumentElement();

        Template form = null;
        NodeList nl = null;

        if (docEle.getNodeName().equalsIgnoreCase("type")) {
            form = new Template(docEle.getAttributes().getNamedItem("name").getNodeValue());
        } else {
            return null;
        }

        nl = docEle.getElementsByTagName("chunk");
        if (nl != null && nl.getLength() > 0) {
            for (int i = 0; i < nl.getLength(); i++) {
                //get the field element
                Element el = (Element) nl.item(i);
                Field field = getFormField(el);
                form.addField(field);
            }
        }

        Node chain = docEle.getFirstChild().getNextSibling();
        LinkedHashMap map = new LinkedHashMap();
        int chCounter = 1;
        int sepCounter = 0;
        map.put(chain.getNodeName() +"_"+ chCounter, chain.getAttributes().getNamedItem("name").getNodeValue());
        while(chain!=null) {
            chain = chain.getNextSibling().getNextSibling();
            if(chain == null) {
                break;
            }
            if (chain.getAttributes().getNamedItem("name") != null) {
                chCounter++;
                map.put(chain.getNodeName() +"_"+ chCounter, chain.getAttributes().getNamedItem("name").getNodeValue());
            }
            if (chain.getAttributes().getNamedItem("value") != null) {
                sepCounter++;
                map.put(chain.getNodeName() +"_"+ sepCounter, chain.getAttributes().getNamedItem("value").getNodeValue());
            }
        }
        form.setConstructor(map);
        return form;

    }


    synchronized private Field getFormField(Element el) {
        // TODO  Map of attributes?!
        String fieldType = null;
        Requirement reqObj = null;
        boolean isRequired = false;
        if (el.getAttributes().getNamedItem("type") != null) {
            fieldType = el.getAttributes().getNamedItem("type").getNodeValue();
        } else {
            fieldType = FieldFactory.FIELD_TYPE_ARRAY[FieldFactory.FIELD_UNKNOWN];
        }

        String fieldName = el.getAttributes().getNamedItem("name").getNodeValue();

        // custom fields support
        String sourceName = null;
        Field field = null;
        if (el.getAttributes().getNamedItem("source") != null) {
            sourceName = el.getAttributes().getNamedItem("source").getNodeValue();
            reqObj = new Requirement("custom");
            File template = TcGenConfigurator.getTemplateFile(sourceName);
            field = FieldFactory.getField(fieldName, FieldFactory.getFieldIdByType(fieldType), (Template) getTemplate(template));
        } else {
            field = FieldFactory.getField(fieldName, FieldFactory.getFieldIdByType(fieldType));
        }

        if (el.getAttributes().getNamedItem("isRequired") != null ) {
            isRequired = Boolean.valueOf(el.getAttributes().getNamedItem("isRequired").getNodeValue()).booleanValue();
        }

        field.getRequirement().setRequired(isRequired);

        NodeList nlReq = el.getElementsByTagName("value");
        String defaultValue = "";
        if (nlReq != null && nlReq.getLength() > 0) {
            for (int j = 0; j < nlReq.getLength(); j++) {
                Element req = (Element) nlReq.item(j);
                defaultValue = req.getAttributes().getNamedItem("default").getNodeValue();
            }
        }

        field.setValue(defaultValue);

        // linked fields support
        if (el.getAttributes().getNamedItem("sameas") != null) {
            String same = el.getAttributes().getNamedItem("sameas").getNodeValue();
            field.setLinkedFieldName(same);
            el = getElementByName(same);
        }

        nlReq = el.getElementsByTagName("requirement");
        if (nlReq != null && nlReq.getLength() > 0) {
            for (int j = 0; j < nlReq.getLength(); j++) {
                Element req = (Element) nlReq.item(j);
                String value = req.getAttributes().getNamedItem("type").getNodeValue();
                String className = RequirementConfig.getRequirementData(value, RequirementConfig.XML_PARSER_CLASS);
                try {
                    Class c = Class.forName(className);
                    Constructor con = c.getConstructor(new Class[]{(req).getClass()});
                    Object o = con.newInstance(new Object[]{req});
                    Method m = o.getClass().getMethod("getRequirement", new Class[]{});
                    Object requirement = m.invoke(o, new Object[]{});
                    reqObj = (Requirement) requirement;
                    field.assignRequirement(reqObj);
                } catch (Exception e) {
                    logger.error("Field requirement parsing error: Field ["+ fieldName+"] contains wrong data", e);
                }
            }
        } else {
            field.assignRequirement(reqObj);
        }

        return field;
    }

}
