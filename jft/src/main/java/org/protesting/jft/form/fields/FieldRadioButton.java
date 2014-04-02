package org.protesting.jft.form.fields;

import org.protesting.jft.form.requirements.Requirement;

/**
 * User: ab83625
 * Date: 22.01.2008
 * Time: 15:44:47
 */
public class FieldRadioButton extends Field {

    public FieldRadioButton(String fieldName, Requirement requirement) {
        super(fieldName, requirement);
        this.setType("radio_button");
    }


    public FieldRadioButton(String fieldName) {
        this(fieldName, new Requirement());
    }

}
