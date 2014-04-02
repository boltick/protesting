package org.protesting.jft.form.fields;

import org.protesting.jft.form.requirements.Requirement;

/**
 * User: ab83625
 * Date: 17.03.2008
 * Time: 10:41:39
 */
public class FieldUnknown extends Field {

    protected FieldUnknown(String fieldName) {
        super(fieldName);
        this.setType("unknown_type");
    }

    protected FieldUnknown(String fieldName, Requirement requirement) {
        super(fieldName, requirement);
    }
}
