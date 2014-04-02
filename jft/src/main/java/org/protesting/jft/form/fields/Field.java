package org.protesting.jft.form.fields;

import org.protesting.jft.form.requirements.Requirement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * User: ab83625
 * Date: 22.01.2008
 * Time: 14:47:02
 */
public class Field {

    private static Log logger = LogFactory.getLog(Field.class);

    private String type;

    protected String name;

    protected String value;

    protected String linkedFieldName;

//    protected boolean isRequired;

    protected Requirement requirement;


    public Field(String fieldName, Requirement requirement) {
        this.type = "field";
        this.name = fieldName;
        this.requirement = requirement;
//        this.isRequired = true;
        logger.debug("Field ["+fieldName+"] is initialized");
    }

    public Field(String fieldName) {
        this(fieldName, new Requirement());
    }


    public void assignRequirement(Requirement requirement) {
        this.requirement = requirement;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Requirement getRequirement() {
        return requirement;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getLinkedFieldName() {
        return linkedFieldName;
    }

    public void setLinkedFieldName(String linkedFieldName) {
        this.linkedFieldName = linkedFieldName;
    }


//    public boolean isRequired() {
//        return isRequired;
//    }

//    public void setRequired(boolean required) {
//        isRequired = required;
//    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("name=").append(name).append("; ")
                .append("type=").append(type).append("; ")
                .append("value=").append(value).append("; ");
//                .append("isRequired=").append(isRequired).append(". ");
        if (requirement != null) {
            sb.append(requirement.toString());
        }

        return sb.toString();
    }



}
