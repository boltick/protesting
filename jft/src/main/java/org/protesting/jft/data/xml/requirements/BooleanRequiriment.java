package org.protesting.jft.data.xml.requirements;

import org.apache.xerces.dom.DeferredElementImpl;
import org.protesting.jft.form.requirements.Requirement;

/**
 * User: ab83625
 * Date: 28.04.2008
 * Time: 13:05:54
 */
public class BooleanRequiriment extends CommonRequirement implements XmlRequirement {


    public BooleanRequiriment(DeferredElementImpl xmlRequirementElement) {
        super(xmlRequirementElement);
    }

    public Requirement getRequirement() {
        Requirement requirement = new org.protesting.jft.form.requirements.BooleanRequirement();

        return requirement;
    }


}
