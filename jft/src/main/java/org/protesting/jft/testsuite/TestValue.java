package org.protesting.jft.testsuite;

import org.protesting.jft.form.requirements.Requirement;

/**
 * Test Value contains value contains test value with detailed description and requirement for the generated value
 *
 * User: ab83625
 * Date: 29.02.2008
 * Time: 14:21:26
 */
public class TestValue {

    private Requirement requirement;

    private String description;

    private boolean isOk;

    private Object value;


    public TestValue(String description, Object value, Requirement requirement, boolean ok) {
        this.requirement = requirement;
        this.description = description;
        this.isOk = ok;
        this.value = value;
    }

    public TestValue(TestValue value) {
        this(value.getDescription(), value.getValue(), value.getRequirement(), value.isOk());
    }



    public Requirement getRequirement() {
        return requirement;
    }

    public void setRequirement(Requirement requirement) {
        this.requirement = requirement;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isOk() {
        return isOk;
    }

    public void setOk(boolean ok) {
        isOk = ok;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }


    public String toString() {
        StringBuffer buffer = new StringBuffer()
                .append("(")
                .append(requirement).append("; ")
                .append("Description=").append(description).append("; ")
                .append("isOK=").append(isOk).append("; ")
                .append("value=").append(value).append(")");
        
        return buffer.toString();
    }
}
