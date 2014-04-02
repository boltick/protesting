package org.protesting.jft.form.fields;

import org.protesting.jft.form.requirements.Requirement;

/**
 * User: ab83625
 * Date: 22.01.2008
 * Time: 15:46:16
 */
public class FieldTextArea extends Field {

    public FieldTextArea(String fieldName, Requirement requirement) {
        super(fieldName, requirement);
        this.setType("text_area");
    }


    public FieldTextArea(String fieldName) {
        this(fieldName, new Requirement());
    }

}
