package org.protesting.jft.form;

import org.protesting.jft.form.fields.Field;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * User: ab83625
 * Date: 23.05.2008
 * Time: 13:20:51
 */
public class JftEntity {

    protected static Log logger = LogFactory.getLog(JftEntity.class);

    private int entityType;

    private String name;

    private List fieldsList;

    public JftEntity(String name, int entityType) {
        this.name = name;
        this.entityType = entityType;
        this.fieldsList = new ArrayList();
    }

    public List getFieldsList() {
        return fieldsList;
    }

    public void addField(Field field) {
        this.fieldsList.add(field);
    }

    public Field getFieldByName(String name) {
        for (int i = 0; i < fieldsList.size(); i++) {
            Field field = (Field) fieldsList.get(i);
            if (field.getName().equals(name)) {
                return field;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }


    public int getEntityType() {
        return entityType;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Form: ").append(name).append(System.getProperty("line.separator"));
        for (int i = 0; i < fieldsList.size(); i++) {
            Field field = (Field) fieldsList.get(i);
            sb.append(field.toString());
        }
        return sb.toString();
    }


}
