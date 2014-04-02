package org.protesting.jft.data.xml.requirements;

import org.apache.xerces.dom.DeferredElementImpl;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.lang.reflect.Constructor;
import java.text.Format;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: ab83625
 * Date: 04.03.2008
 * Time: 14:40:03
 */
public class CommonRequirement {

    private DeferredElementImpl xmlRequirementElement;

    private CommonRequirement() {
    }
    
    CommonRequirement(DeferredElementImpl xmlRequirementElement) {
        this.xmlRequirementElement = xmlRequirementElement;
    }

    protected int [] getIntValueFromXml(NodeList reqParamList) {
        int [] lenghtArr = new int[]{-1, -1};
        if(reqParamList != null && reqParamList.getLength() > 0) {
            String length = reqParamList.item(0).getAttributes().getNamedItem("value").getNodeValue();
            Pattern pattern = Pattern.compile("[0-9]*-[0-9]*");
            Matcher match = pattern.matcher(length);
            if (match.matches()) {
                String [] temp = length.split("-");
                lenghtArr[0] = !temp[0].equals("") ? lenghtArr[0] = Integer.parseInt(temp[0]) : 1;
                lenghtArr[1] = Integer.parseInt(temp[1]);
            }
            pattern = Pattern.compile("[0-9]*");
            match = pattern.matcher(length);
            if (match.matches()) {
                lenghtArr[0] = 1;
                lenghtArr[1] = Integer.parseInt(length);
            }
        }
        if (lenghtArr[0] == -1 || lenghtArr[1] == -1) {
            throw new IllegalStateException("Requirement Length is not correctly defined");
        }
        return lenghtArr;
    }

    protected Format getFormatFromXml(String tag, String formatType, String formatValue) {
        String format = null;
        String type = null ;
        NodeList reqParamList = xmlRequirementElement.getElementsByTagName(tag);
        if(reqParamList != null && reqParamList.getLength() > 0) {
            for (int k = 0; k < reqParamList.getLength(); k++) {
                Element reqParam = (Element) reqParamList.item(k);
                type = reqParam.getAttributes().getNamedItem(formatType).getNodeValue();
                format = reqParam.getAttributes().getNamedItem(formatValue).getNodeValue();
            }
        }
        if (type != null) {
            try {
                Class c = Class.forName(type);
                Constructor cc = c.getConstructor(new Class[]{Class.forName("java.lang.String")});
                Object obj = cc.newInstance(new Object[]{format});
                return (Format) obj;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } 

        return null;
    }


    public DeferredElementImpl getXmlRequirementElement() {
        return xmlRequirementElement;
    }

    public void setXmlRequirementElement(DeferredElementImpl xmlRequirementElement) {
        this.xmlRequirementElement = xmlRequirementElement;
    }


    public String getPropertyFromXml(String tag, String property) {
        NodeList reqParamList  = xmlRequirementElement.getElementsByTagName(tag);
        if(reqParamList != null && reqParamList.getLength() > 0) {
            if (reqParamList.item(0).getAttributes().getNamedItem(property) != null) {
                return reqParamList.item(0).getAttributes().getNamedItem(property).getNodeValue();
            }
        }
        return "";
    }

    

}
