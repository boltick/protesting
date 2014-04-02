package org.protesting.jft.testsuite;

import java.util.ArrayList;
import java.util.List;

/**
 * Test Case object. The main object of generated data.
 *
 * User: abulat
 * Date: 19.02.2008
 * Time: 21:31:06
 */
public class TestCase {

    private String id;
    private String name;
    private String type;

    private List fields;

    public TestCase() {
        this.fields = new ArrayList();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List getFields() {
        return fields;
    }

    public void setFields(List fields) {
        this.fields = fields;
    }


    public Object getValueByFieldName(String fieldName) {
        for (int i = 0; i < fields.size(); i++) {
            TestField testField = (TestField) fields.get(i);
            if (testField.getName().equalsIgnoreCase(fieldName)) {
                return testField.getValue().getValue();
            }
        }
        return "";
    }

    public String getDescriptionByFieldName(String fieldName) {
        for (int i = 0; i < fields.size(); i++) {
            TestField testField = (TestField) fields.get(i);
            if (testField.getName().equalsIgnoreCase(fieldName)) {
                return testField.getValue().getDescription();
            }
        }
        return "";
    }

    public List getFieldsByValidness(boolean isValid) {
        List invalidFields = new ArrayList();
        for (int i = 0; i < fields.size(); i++) {
            TestField testField = (TestField) fields.get(i);
            if (testField.getValue().isOk() == isValid) {
                invalidFields.add(testField);
            }
        }
        return invalidFields;
    }


    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(id).append("; ");
        for (int i = 0; i < fields.size(); i++) {
            TestField testField = (TestField) fields.get(i);
            sb.append(testField.toString());
        }
        sb.append(System.getProperty("line.separator"));
        return sb.toString();
    }
}
