package org.protesting.jft.form.fields;

import org.protesting.jft.form.requirements.BooleanRequirement;
import org.protesting.jft.form.requirements.Requirement;

/**
 * User: ab83625
 * Date: 22.01.2008
 * Time: 15:45:37
 */
public class FieldCheckBox extends Field {

    public FieldCheckBox(String fieldName, Requirement requirement) {
        super(fieldName, requirement);
        this.setType("check_box");
    }


    public FieldCheckBox(String fieldName) {
        this(fieldName, new BooleanRequirement());
    }

}
