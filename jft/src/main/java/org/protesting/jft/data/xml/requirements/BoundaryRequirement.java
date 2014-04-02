package org.protesting.jft.data.xml.requirements;

import org.apache.xerces.dom.DeferredElementImpl;
import org.protesting.jft.form.requirements.Requirement;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.text.Format;
import java.text.ParseException;

/**
 * User: ab83625
 * Date: 18.01.2008
 * Time: 17:13:13
 */
public class BoundaryRequirement extends CommonRequirement implements XmlRequirement{

    private Format dataFormat;

    public BoundaryRequirement(DeferredElementImpl xmlRequirementElement) {
        super(xmlRequirementElement);
    }

    public Requirement getRequirement() {
        initDataFormat();

        Requirement requirement = new org.protesting.jft.form.requirements.BoundaryRequirement(
                getTypifiedObject(getPropertyFromXml("border", "type"), getPropertyFromXml("border", "leftValue")),
                getTypifiedObject(getPropertyFromXml("border", "type"), getPropertyFromXml("border", "rightValue")),
                Boolean.valueOf(getPropertyFromXml("border", "inclusive")).booleanValue());

        // Format
        if (dataFormat != null) {
            requirement.setFormatType(dataFormat);
        }
        // Charset
        requirement.setAcceptedChars(getPropertyFromXml("charset", "accepted"));
        requirement.setForbiddenChars(getPropertyFromXml("charset", "rejected"));

        return requirement;
    }

    private void initDataFormat() {
        if (getXmlRequirementElement().getElementsByTagName("format") != null) {
            this.dataFormat = getFormatFromXml("format", "type", "value");
        } 
    }

    private Object getTypifiedObject(String type, String value) {
        try {
            Class c = Class.forName(type);
            if (!type.equals("java.util.Date")) {
                Constructor cc = c.getConstructor(new Class[]{Class.forName("java.lang.String")});
                return cc.newInstance(new Object[]{value});
            } else {
                if (dataFormat!=null) {
                    try {
                        return dataFormat.parseObject(value);
                    } catch (ParseException e) {
                        throw new IllegalArgumentException("Date parsing error: value [" + value + "] is in wrong format");
                    }
                } else {
                    throw new IllegalArgumentException("Data format is no defined");
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        throw new IllegalArgumentException("type ["+type+"] or value ["+value +"] is not defined");
    }

}
