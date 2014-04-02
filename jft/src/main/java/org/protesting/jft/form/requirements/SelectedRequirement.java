package org.protesting.jft.form.requirements;

/**
 * User: ab83625
 * Date: 29.04.2008
 * Time: 11:17:30
 */
public class SelectedRequirement extends Requirement {

    private int itemsNumber;

    private boolean isMultiSelectAllowed;

    private SelectedRequirement() {
        super("selected");
    }

    public SelectedRequirement(int number) {
        this();
        this.itemsNumber = number;
    }

    public int getItemsNumber() {
        return itemsNumber;
    }


    public void setItemsNumber(int itemsNumber) {
        this.itemsNumber = itemsNumber;
    }


    public boolean isMultiSelectAllowed() {
        return isMultiSelectAllowed;
    }

    public void setMultiSelectAllowed(boolean multiSelectAllowed) {
        isMultiSelectAllowed = multiSelectAllowed;
    }
}
