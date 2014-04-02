package org.protesting.jft.generator;

import org.protesting.jft.testsuite.TestValue;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * User: ab83625
 * Date: 29.07.2008
 * Time: 9:17:47
 */
public class ValueGenerator {

    private static Log logger = LogFactory.getLog(Generator .class);

    private List rowValues;


    protected ValueGenerator() {
        this.rowValues = new ArrayList();
    }

    public List getAllPositive() {
        List positive = new ArrayList();
        for (int i = 0; i < rowValues.size(); i++) {
            TestValue value = (TestValue) rowValues.get(i);
            if(value.isOk()) {
                positive.add(value);
            }
        }
        return positive;
    }

    public List getAllNegative() {
        List negative = new ArrayList();
        for (int i = 0; i < rowValues.size(); i++) {
            TestValue value = (TestValue) rowValues.get(i);
            if(!value.isOk()) {
                negative.add(value);
            }
        }
        return negative;
    }

    public List getRowValues() {
        return rowValues;
    }

    public void setRowValues(List rowValues) {
        this.rowValues = rowValues;
    }

    protected void addValue(TestValue value) {
        boolean isExist = false;
        for (int i = 0; i < rowValues.size(); i++) {
            TestValue testValue = (TestValue) rowValues.get(i);
            if (testValue.getValue().equals(value.getValue())) {
                isExist = true;
            }
        }
        if (isExist) {
            logger.debug("Value [" + value.getValue() + "] already exists.");
            return;
        }
        this.rowValues.add(value);
    }



}
