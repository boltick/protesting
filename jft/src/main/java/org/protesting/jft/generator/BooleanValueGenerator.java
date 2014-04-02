package org.protesting.jft.generator;

import org.protesting.jft.form.fields.Field;
import org.protesting.jft.form.requirements.BooleanRequirement;
import org.protesting.jft.testsuite.TestValue;

/**
 * User: ab83625
 * Date: 28.04.2008
 * Time: 13:14:29
 */
public class BooleanValueGenerator extends AbstractBooleanValueGenerator {

    public BooleanValueGenerator(Field field) throws Exception {
        super(field);
    }

    public BooleanValueGenerator(BooleanRequirement requirement) throws Exception {
        super(requirement);
    }

    public void generateRowValues() {
        addValue(new TestValue("TRUE", Boolean.TRUE, getRequirement(), true));
        addValue(new TestValue("FALSE", Boolean.FALSE, getRequirement(), true));
    }

    public void addMandatoryValues() {
        if(getField().getRequirement().isRequired()) {
            for (int i = 0; i < getRowValues().size(); i++) {
                TestValue value = (TestValue) getRowValues().get(i);
                if(value.getValue().equals(Boolean.FALSE)) {
                    value.setOk(false);
                }
            }
        }
    }
}
