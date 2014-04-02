package org.protesting.jft.form.fields;

import org.protesting.jft.form.requirements.Requirement;

/**
 * User: ab83625
 * Date: 22.01.2008
 * Time: 15:44:09
 */
public class FieldEditBox extends Field {

    public FieldEditBox(String fieldName, Requirement requirement) {
        super(fieldName, requirement);
        this.setType("edit_box");
    }


    public FieldEditBox(String fieldName) {
        this(fieldName, new Requirement());
    }

}
