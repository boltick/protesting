package org.protesting.jft.generator;

import org.protesting.jft.form.fields.Field;
import org.protesting.jft.form.requirements.SelectedRequirement;
import org.protesting.jft.generator.generic.IntegerGenerator;
import org.protesting.jft.testsuite.TestValue;

/**
 * User: ab83625
 * Date: 29.04.2008
 * Time: 11:34:17
 */
public class SelectedValueGenerator extends AbstractSelectedValueGenerator {

    public SelectedValueGenerator(Field field) throws Exception {
        super(field);
    }

    public SelectedValueGenerator(SelectedRequirement requirement) throws Exception {
        super(requirement);
    }

    public void generateRowValues() {
        Integer index = (Integer) new IntegerGenerator().getValue(new Integer(0), new Integer(getRequirement().getItemsNumber()));
        addValue(new TestValue("SELECTED ITEM=" + index.intValue(), index , getRequirement(), true));
        addValue(new TestValue("EMPTY SELECTION", "", getRequirement(), true));
    }

    public void addMandatoryValues() {
        if(getField().getRequirement().isRequired()) {
            for (int i = 0; i < getRowValues().size(); i++) {
                TestValue value = (TestValue) getRowValues().get(i);
                if(value.getValue().equals("")) {
                    value.setOk(false);
                }
            }
        }
    }

}
