package org.protesting.jft.form.fields;

import org.protesting.jft.form.Template;

/**
 * User: ab83625
 * Date: 23.05.2008
 * Time: 14:26:25
 */
public class CustomField extends Field {

//    private String source;

    private Template template;


    public CustomField(String fieldName, Template template) {
        super(fieldName);
        this.template = template;
    }

    protected CustomField(String fieldName, String type, Template template) {
        this(fieldName, template);
        setType(type);
    }

    public String getSource() {
        return template.getName();
    }


    public String toString() {
        return super.toString() + "source="+getSource();
    }


    public Template getTemplate() {
        return template;
    }
}
