package org.protesting.jft.form.fields;

import org.protesting.jft.form.requirements.Requirement;

import java.util.ArrayList;
import java.util.List;

/**
 * User: ab83625
 * Date: 22.01.2008
 * Time: 15:46:40
 */
public class FieldComboBox extends Field {

    private List items;

    public FieldComboBox(String fieldName, Requirement requirement) {
        super(fieldName, requirement);
        this.setType("combo_box");
        this.items = new ArrayList();

    }

    public FieldComboBox(String fieldName) {
        this(fieldName, new Requirement());
    }

    public List getItems() {
        return items;
    }

    public void setItems(List items) {
        this.items = items;
    }

    public void addItem(String item) {
        items.add(item);
    }

}
