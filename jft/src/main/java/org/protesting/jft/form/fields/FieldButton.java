package org.protesting.jft.form.fields;

/**
 * User: ab83625
 * Date: 22.01.2008
 * Time: 15:42:30
 */
public class FieldButton extends Field {

    private String action;


    public FieldButton(String fieldName, String action) {
        this(fieldName);
        this.action = action;
    }

    public FieldButton(String fieldName) {
        super(fieldName);
        this.setType("button");
    }


    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }


    public String toString() {
        return super.toString() + "; action="+getAction();
    }
}
