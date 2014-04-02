package org.protesting.jft.data.xml.requirements;

import org.apache.xerces.dom.DeferredElementImpl;
import org.protesting.jft.form.requirements.Requirement;

/**
 * User: ab83625
 * Date: 18.01.2008
 * Time: 17:13:13
 */
public class LengthRequirement extends CommonRequirement implements XmlRequirement {

    public LengthRequirement(DeferredElementImpl xmlRequirementElement) {
        super(xmlRequirementElement);
    }


    public Requirement getRequirement() {
        int [] length = getIntValueFromXml(getXmlRequirementElement().getElementsByTagName("length"));
        Requirement requirement = null;
        if (length[1] == -1) {
            requirement = new org.protesting.jft.form.requirements.LengthRequirement(length[1]);
        } else {
            requirement = new org.protesting.jft.form.requirements.LengthRequirement(new Integer(length[0]), new Integer(length[1]), true);
        }
        // Format
        if (getXmlRequirementElement().getElementsByTagName("format").getLength() > 0) {
            requirement.setFormatType(getFormatFromXml("format", "type", "value"));
        }
        // Charset
        if (getXmlRequirementElement().getElementsByTagName("charset").getLength() > 0) {
            requirement.setAcceptedChars(getPropertyFromXml("charset", "accepted"));
            requirement.setForbiddenChars(getPropertyFromXml("charset", "rejected"));
        }

        return requirement;
    }

}