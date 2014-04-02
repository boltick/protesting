package org.protesting.jft.testsuite;

import org.protesting.jft.form.Form;
import org.protesting.jft.form.fields.Field;
import org.protesting.jft.generator.AbstractValueGenerator;
import org.protesting.jft.generator.Generator;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Class is used for generation of different types of test cases.
 *
 * User: ab83625
 * Date: 29.02.2008
 * Time: 9:55:19
 */
public class TestCaseGenerator extends AbstractTestCaseGenerator {

    private static Log logger = LogFactory.getLog(TestCaseGenerator.class);

    private int max = -1;
    private Form form;
    private List testCases;
    private List values;

    public TestCaseGenerator(Form form) {
        this.form = form;
    }

    private void init() {
        this.max = -1;
        this.testCases = new ArrayList();
        this.values = new ArrayList();
    }

    public List getRawTestCases() {
        logger.debug("Negative Cases Generation - started");
        init();

        List raw = new ArrayList();

        List testFieldList = new ArrayList();
        for (int i = 0; i < form.getFieldsList().size(); i++) {
            Field field = (Field) form.getFieldsList().get(i);
            TestField testField = new TestField();
            testField.setName(field.getName());
            testField.setType(field.getType());

            AbstractValueGenerator generatorForRequirement = Generator.getValueGenerator(field);
            setValues(generatorForRequirement.getRowValues());
            raw.addAll(values);
            testFieldList.add(testField);
            init();
        }

        return new ArrayList();

    }

    public List getPositiveOneByOne() {
        logger.debug("Positive Cases Generation - started");
        init();

        List testFieldList = new ArrayList();
        for (int i = 0; i < form.getFieldsList().size(); i++) {
            Field field = (Field) form.getFieldsList().get(i);
            TestField testField = new TestField();
            testField.setName(field.getName());
            testField.setType(field.getType());

            // todo to verify linked list support implementation
            if (field.getLinkedFieldName() == null || (field.getLinkedFieldName() != null && field.getLinkedFieldName().equals("")) ) {
                AbstractValueGenerator generatorForRequirement = Generator.getValueGenerator(field);
                setValues(generatorForRequirement.getAllPositive());
            } else {
                setValues(new ArrayList());
            }
            testFieldList.add(testField);
        }

        int counter = 0;
        for (int i = 0; i < getValues().size(); i++) {
            List negativeValuesList = (List) getValues().get(i);

            for (int j = 0; j < negativeValuesList.size(); j++) {
                TestValue negativeValue = (TestValue) negativeValuesList.get(j);
                String failedDescription = null;
                List testCaseFields = new ArrayList();
                for (int k = 0; k < testFieldList.size(); k++) {
                    TestField testField = (TestField) testFieldList.get(k);

                    TestField tempField = new TestField();
                    tempField.setName(testField.getName());
                    tempField.setType(testField.getType());

                    // linked fields suport
                    //----
                    List positiveValuesList = null;
                    if (isLinked(testField.getName())) {
                        int linkedId = getTestFieldId(testField.getName());
                        TestField field = (TestField) testCaseFields.get(linkedId);
                        tempField.setValue(field.getValue());
                        testCaseFields.add(tempField);
                        continue;
                    }
                    //----

                    positiveValuesList = (List) getValues().get(k);

                    TestValue testValue = null;
                    if (k == i) {
                        testValue = negativeValue;
                        failedDescription = "Field " + tempField.getName() + ": Value is " + testValue.getDescription();
                    } else {
                        testValue = (TestValue) positiveValuesList.get(0);
                    }

                    tempField.setValue(testValue);
                    testCaseFields.add(tempField);
                }
                TestCase testCase = new TestCase();
                testCase.setFields(testCaseFields);
                testCase.setName("Form "+form.getName() + ": " + failedDescription);
                testCase.setType("POSITIVE");
                addUniqueTestCase(testCase);
                counter++;
            }
        }
        logger.debug("Number of generated Cases is - " + counter);
        logger.debug("Positive Cases Generation - finished");

        return testCases;
    }


    public List getNegativeOneByOne() {
        logger.debug("Negative Cases Generation - started");
        init();

        List negative = new ArrayList();
        List positive = new ArrayList();

        List testFieldList = new ArrayList();
        for (int i = 0; i < form.getFieldsList().size(); i++) {
            Field field = (Field) form.getFieldsList().get(i);
            TestField testField = new TestField();
            testField.setName(field.getName());
            testField.setType(field.getType());

            AbstractValueGenerator generatorForRequirement = Generator.getValueGenerator(field);
            setValues(generatorForRequirement.getAllPositive());
            positive.addAll(values);
            init();
            setValues(generatorForRequirement.getAllNegative());
            negative.addAll(values);
            init();
            testFieldList.add(testField);
        }



        int counter = 0;
        for (int i = 0; i < negative.size(); i++) {
            List negativeValuesList = (List) negative.get(i);

            for (int j = 0; j < negativeValuesList.size(); j++) {
                TestValue negativeValue = (TestValue) negativeValuesList.get(j);
                String failedDescription = null;
                List testCaseFields = new ArrayList();
                for (int k = 0; k < testFieldList.size(); k++) {
                    TestField testField = (TestField) testFieldList.get(k);

                    TestField tempField = new TestField();
                    tempField.setName(testField.getName());
                    tempField.setType(testField.getType());

                    // todo linked fields support
                    // linked fields suport
                    //----
//                    List positiveValuesList = null;
//                    if (isLinked(testField.getName())) {
//                        int linkedId = getTestFieldId(testField.getName());
//                        TestField field = (TestField) testCaseFields.get(linkedId);
//                        tempField.setValue(field.getValue());
//                        testCaseFields.add(tempField);
//                        continue;
//                    }
                    //----

                    List positiveValuesList = (List) positive.get(k);

                    TestValue testValue = null;
                    if (k == i) {
                        testValue = negativeValue;
                        failedDescription = "Field " + tempField.getName() + ": Value is " + testValue.getDescription();
                    } else {
                        testValue = (TestValue) positiveValuesList.get(0);
                    }

                    tempField.setValue(testValue);
                    testCaseFields.add(tempField);
                }
                TestCase testCase = new TestCase();
                testCase.setFields(testCaseFields);
                testCase.setName("Form "+form.getName() + ": " + failedDescription);
                testCase.setType("NEGATIVE");
                addUniqueTestCase(testCase);
                counter++;
            }
        }

        logger.debug("Number of generated Cases is - " + counter);
        logger.debug("Negative Cases Generation - finished");

        return testCases;
    }


    public List getPositiveOneGo() {
        logger.debug("POSITIVE OneGo Cases Generation - stared");
        init();

        List testFieldList = new ArrayList();
        for (int i = 0; i < form.getFieldsList().size(); i++) {
            Field field = (Field) form.getFieldsList().get(i);
            TestField testField = new TestField();
            testField.setName(field.getName());
            testField.setType(field.getType());

            if (field.getLinkedFieldName() == null || (field.getLinkedFieldName() != null && field.getLinkedFieldName().equals("")) ) {
                AbstractValueGenerator generatorForRequirement = Generator.getValueGenerator(field);
                setValues(generatorForRequirement.getAllPositive());
            } else {
                setValues(new ArrayList());
            }

            testFieldList.add(testField);
        }

        int counter = 0;
        for (int j = 0; j < max; j++) {
            List testCaseFields = new ArrayList();
            for (int i = 0; i < testFieldList.size(); i++) {
                TestField field = (TestField) testFieldList.get(i);

                TestField tempField = new TestField();
                tempField.setName(field.getName());
                tempField.setType(field.getType());

                // todo need to be checked - linked fields
                // linked fields suport
                    //----
                    List fieldValueList = null;
                    if (isLinked(field.getName())) {
                        int linkedId = getTestFieldId(field.getName());
                        TestField testField = (TestField) testCaseFields.get(linkedId);
                        tempField.setValue(testField.getValue());
                        testCaseFields.add(tempField);
                        continue;
                    }
                    //----

                fieldValueList = (List) getValues().get(i);
                TestValue testValue = null;
                if (fieldValueList.size() == 1) {
                    testValue = (TestValue) fieldValueList.get(0);
                } else if (fieldValueList.size() > j) {
                    testValue = (TestValue) fieldValueList.get(j);
                } else {
                    testValue = (TestValue) fieldValueList.get(RandomUtils.nextInt(fieldValueList.size()));
                }
                tempField.setValue(testValue);
                testCaseFields.add(tempField);
            }

            TestCase testCase = new TestCase();
            testCase.setFields(testCaseFields);
            testCase.setName(form.getName() + " tests");
            testCase.setType("POSITIVE");
            addUniqueTestCase(testCase);
            counter++;
        }
        logger.debug("Number of generated Cases is - " + counter);
        logger.debug("POSITIVE OneGo Cases Generation - finished");

        return testCases;
    }


    public List getNegativeOneGo() {
        logger.debug("Negative Cases OneGo Generation - stared");
        init();

        List testFieldList = new ArrayList();
        for (int i = 0; i < form.getFieldsList().size(); i++) {
            Field field = (Field) form.getFieldsList().get(i);
            TestField testField = new TestField();
            testField.setName(field.getName());
            testField.setType(field.getType());

            AbstractValueGenerator generatorForRequirement = Generator.getValueGenerator(field);
            setValues(generatorForRequirement.getAllNegative());
            testFieldList.add(testField);
        }

        int counter = 0;
        for (int j = 0; j < max; j++) {
            List testCaseFields = new ArrayList();
            for (int i = 0; i < testFieldList.size(); i++) {
                TestField field = (TestField) testFieldList.get(i);

                TestField tempField = new TestField();
                tempField.setName(field.getName());
                tempField.setType(field.getType());

                // todo need to be checked - linked fields
                // linked fields suport
                    //----
                    List fieldValueList = null;
                    if (isLinked(field.getName())) {
                        int linkedId = getTestFieldId(field.getName());
                        TestField testField = (TestField) testCaseFields.get(linkedId);
                        tempField.setValue(testField.getValue());
                        testCaseFields.add(tempField);
                        continue;
                    }
                    //----

                fieldValueList = (List) getValues().get(i);
                TestValue testValue = null;
                if (fieldValueList.size() == 1) {
                    testValue = (TestValue) fieldValueList.get(0);
                } else if (fieldValueList.size() > j) {
                    testValue = (TestValue) fieldValueList.get(j);
                } else if (!fieldValueList.isEmpty()) {
                    testValue = (TestValue) fieldValueList.get(RandomUtils.nextInt(fieldValueList.size()));
                } else {
                    logger.debug("Field ["+field.getName()+"]: NEGATIVE value is not generated, so POSITIVE is using");
                    Field helpField = getFormFieldByName(field.getName());
                    AbstractValueGenerator generatorForRequirement = Generator.getValueGenerator(helpField);
                    List helpValues = generatorForRequirement.getAllPositive();
                    testValue = (TestValue) helpValues.get(RandomUtils.nextInt(helpValues.size()));
                }

                tempField.setValue(testValue);
                testCaseFields.add(tempField);
            }

            TestCase testCase = new TestCase();
            testCase.setFields(testCaseFields);
            testCase.setName(form.getName() + " tests");
            testCase.setType("NEGATIVE");
            addUniqueTestCase(testCase);
            counter++;
        }
        logger.debug("Number of generated Cases is - " + counter);
        logger.debug("Negative Cases OneGo Generation - finished");

        return testCases;
    }


    private void addUniqueTestCase(TestCase testCase) {
        for (int j = 0; j < testCases.size(); j++) {
            TestCase aCaseRunner = (TestCase) testCases.get(j);
            int counter = 0;
            for (int k = 0; k < aCaseRunner.getFields().size(); k++) {
                TestField testField1 = (TestField) aCaseRunner.getFields().get(k);
                TestField testField2 = (TestField) testCase.getFields().get(k);

                if (testField1.getValue() == null && testField2.getValue() == null) {
                    System.out.println("tf1: " + testField1.getValue());
                    System.out.println("tf2: " + testField2.getValue());
                    counter++;
                } else {
                    if (testField1.getValue().getValue().equals(testField2.getValue().getValue())) {
                        counter++;
                    }
                }
            }
            if (counter == aCaseRunner.getFields().size()) {
                logger.debug("Dupplicated Case for TC ["+aCaseRunner.getId()+ "] is rejected: " + testCase);
                return;
            }
        }

        int pos = testCases.size()+1;
        testCase.setId(form.getName()+"_"+pos);
        testCases.add(testCase);
        logger.debug("Test Case is accepted: " + testCase);
    }


    private void setValues(List values) {
        if (max == -1) {
            max = values.size();
        }
        if (max < values.size()) {
            max = values.size();
        }

        this.values.add(values);
    }

    private List getValues() {
        return values;
    }


    public Form getForm() {
        return form;
    }

    private Field getFormFieldByName(String name) {
        return getForm().getFieldByName(name);
    }


    private int getTestFieldId(String name) {
        for (int i = 0; i < form.getFieldsList().size(); i++) {
            Field field = (Field) form.getFieldsList().get(i);
            if (field.getName().equalsIgnoreCase(name)) {
                int tempId = getTestFieldId(field.getLinkedFieldName());
                if (tempId > -1) {
                    return getTestFieldId(field.getLinkedFieldName());
                }
                return i;
            }
        }
        return -1;
    }

    private boolean isLinked(String name) {
        for (int i = 0; i < form.getFieldsList().size(); i++) {
            Field field = (Field) form.getFieldsList().get(i);
            if (field.getName().equalsIgnoreCase(name)) {
                return !(field.getLinkedFieldName() == null || (field.getLinkedFieldName() != null && field.getLinkedFieldName().equals("")) );
            }
        }
        return false;
    }

}
