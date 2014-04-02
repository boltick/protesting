package org.protesting.jft.data.xml.requirements;

import org.apache.xerces.dom.DeferredElementImpl;
import org.protesting.jft.form.requirements.Requirement;

/**
 * User: ab83625
 * Date: 29.04.2008
 * Time: 11:19:43
 */
public class SelectedRequirement extends CommonRequirement implements XmlRequirement {
    
    public SelectedRequirement(DeferredElementImpl xmlRequirementElement) {
        super(xmlRequirementElement);
    }

    public Requirement getRequirement() {
        Requirement requirement = null;
        String number = getPropertyFromXml("select", "number");
        if(number != null) {
             requirement = new org.protesting.jft.form.requirements.SelectedRequirement(Integer.parseInt(number));
        } else {
            throw new IllegalStateException("Requirement is not correctly defined");
        }

        // todo isMultiSelectAllowed implementation
        // ----


        return requirement;

    }
}
