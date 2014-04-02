package org.protesting.jft.generator;

import org.protesting.jft.form.fields.Field;
import org.protesting.jft.form.requirements.BoundaryRequirement;
import org.protesting.jft.testsuite.TestValue;

import java.util.ArrayList;
import java.util.List;

/**
 * Classs is used for generation of values by requirements or defined field.
 * Pass requireemtns to the constructor and get Positive and negative values
 *
 * @author Alexey Bulat
 */
public class BoundaryValueGenerator extends AbstractBoundaryValueGenerator {

    public BoundaryValueGenerator(BoundaryRequirement requirement) throws Exception {
        super(requirement);
    }

    public BoundaryValueGenerator(Field field) throws Exception {
        super(field);
    }


    public void generateRowValues() {
        if (validateBorders()) {
            addValue(new TestValue("GREATER THAN LEFT BORDER", getGenerator().getGreaterThan(getRequirement().getLeftBoundary()), getRequirement(), true));
            addValue(new TestValue("SMALLER THAN RIGHT BORDER", getGenerator().getSmallerThan(getRequirement().getRightBoundary()), getRequirement(), true));
            addValue(new TestValue("BETWEEN LEFT AND RIGHT BOUNDARIES", getGenerator().getValue(getRequirement().getLeftBoundary(), getRequirement().getRightBoundary()), getRequirement(), true));
        }
        addValue(new TestValue("SMALLER THAN LEFT BORDER", getGenerator().getSmallerThan(getRequirement().getLeftBoundary()), getRequirement(), false));
        addValue(new TestValue("GREATER THAN RIGHT BORDER", getGenerator().getGreaterThan(getRequirement().getRightBoundary()), getRequirement(), false));

        addValue(new TestValue("LEFT BOUNDARY", getRequirement().getLeftBoundary(), getRequirement(), getRequirement().isInclusive()));
        addValue(new TestValue("RIGHT BOUNDARY", getRequirement().getRightBoundary(), getRequirement(), getRequirement().isInclusive()));

        addTrickyValue();
    }

    public void addFormattedValues() {
        // todo: remove duplicated piece of code (see LengthValueGenerator class)

        if(getRequirement().getFormatType() == null) {
            return;
        }
        // format values
        List first = new ArrayList(getRowValues());
        for (int i = 0; i < first.size(); i++) {
            TestValue value = (TestValue) first.get(i);
            TestValue testValue = new TestValue(value);
            testValue.setValue(getRequirement().getFormatType().format(testValue.getValue()));
            testValue.setDescription(testValue.getDescription() + " CORRECTLY FORMATTED");
            addValue(testValue);
            getRowValues().remove(value);
        }
    }

    public void addFilteredValues() {
        // TODO
    }

//    public void addMandatoryValues() {
//        if(!getField().getRequirement().isRequired()) {
//            addValue(new TestValue("EMPTY", "", getRequirement(), !getField().getRequirement().isRequired()));
//        } else {
//            addValue(new TestValue("EMPTY", "", getRequirement(), !getField().getRequirement().isRequired()));
//        }
//    }

    /**
     * Method adds tricky values corresponding to data types:
     * Zero
     * Nan
     * Negative Infinity
     * Positive infinity
     *
     * If you need to add more value you can easily expand this method or ovveride it in your Class 
     */
    private void addTrickyValue() {
        if (getRequirement().getLeftBoundary() instanceof Number) {
            Object b1 = getRequirement().getLeftBoundary();
            Object b2 = getRequirement().getRightBoundary();
            boolean isZeroNeeded = false;
            if(((Number) b1).doubleValue() < 0 && ((Number) b2).doubleValue() > 0 ) {
                isZeroNeeded = true;
            }
            if (b1 instanceof Float) {
                addValue(new TestValue("NAN", new Float(Float.NaN), getRequirement(), false));
                addValue(new TestValue("NEGATIVE_INFINITY", new Float(Float.NEGATIVE_INFINITY), getRequirement(), false));
                addValue(new TestValue("POSITIVE_INFINITY", new Float(Float.POSITIVE_INFINITY), getRequirement(), false));
                if (isZeroNeeded) addValue(new TestValue("ZERO", new Float("0.00"), getRequirement(), true));
            }
            if (b1 instanceof Integer) {
                if (isZeroNeeded) addValue(new TestValue("ZERO", new Integer("0"), getRequirement(), true));
            }
            if (b1 instanceof Long) {
                if (isZeroNeeded) addValue(new TestValue("ZERO", new Long("0"), getRequirement(), true));
            }
            if (b1 instanceof Double) {
                addValue(new TestValue("NAN", new Double(Double.NaN), getRequirement(), false));
                addValue(new TestValue("NEGATIVE_INFINITY", new Double(Double.NEGATIVE_INFINITY), getRequirement(), false));
                addValue(new TestValue("POSITIVE_INFINITY", new Double(Double.POSITIVE_INFINITY), getRequirement(), false));
                if (isZeroNeeded) addValue(new TestValue("ZERO", new Double("0.00"), getRequirement(), true));
            }
        }
    }


    public boolean validateBorders() {
        return getGenerator().validate(getRequirement().getLeftBoundary(), getRequirement().getRightBoundary());
    }

}
