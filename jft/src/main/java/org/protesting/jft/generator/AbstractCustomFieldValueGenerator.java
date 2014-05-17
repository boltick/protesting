package org.protesting.jft.generator;

import org.protesting.jft.config.Configurator;
import org.protesting.jft.form.Template;
import org.protesting.jft.form.fields.CustomField;
import org.protesting.jft.form.fields.Field;
import org.protesting.jft.testsuite.AbstractTestCaseGenerator;
import org.protesting.jft.testsuite.TestCase;
import org.protesting.jft.testsuite.TestCaseGenerator;
import org.protesting.jft.testsuite.TestValue;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * User: ab83625
 * Date: 23.05.2008
 * Time: 16:04:38
 */
public abstract class AbstractCustomFieldValueGenerator extends AbstractValueGenerator {

    public AbstractCustomFieldValueGenerator(Field field) throws Exception {
        super(field);
    }

    public void generateRowValues() {
        String [] type = Configurator.getInstance().getProperty("test.case.type").split(",");

        Template templateForm = getCustomField().getTemplate();
        AbstractTestCaseGenerator generator = new TestCaseGenerator(templateForm);
        for (int i = 0; i < type.length; i++) {
            List casesList = generator.getTestCases(type[i].trim(), TestCaseGenerator.METHOD_ONE_BY_ONE);
            casesList.addAll(generator.getTestCases(type[i].trim(), TestCaseGenerator.METHOD_ONE_GO));
            for (int j = 0; j < casesList.size(); j++) {
                TestCase testCase = (TestCase) casesList.get(j);
                String value = "";
                String desc = "";

                Set set = templateForm.getConstructor().keySet();

                for (Iterator iterator = set.iterator(); iterator.hasNext();) {
                    String key = (String) iterator.next();
                    if (key.startsWith("chunk")) {
                        String name = templateForm.getConstructor().get(key).toString();
                        value = value + testCase.getValueByFieldName(name);
                        desc = desc + name +"="+ testCase.getDescriptionByFieldName(name) + ", ";
                    }
                    if (key.startsWith("separator")) {
                        value = value + templateForm.getConstructor().get(key).toString();
                    }
                }

                desc = desc.trim().substring(0, desc.length()-2);
                boolean toAddFlag = true;
                for (int k = 0; k < getRowValues().size(); k++) {
                    TestValue testValue = (TestValue) getRowValues().get(k);
                    if (testValue.getDescription().equals(desc)) {
                        logger.debug("Value with the same description already exists. Description="+desc);
                        toAddFlag = false;
                        break;
                    }
                }
                if (!toAddFlag) continue;
                getRowValues().add(new TestValue(desc, value, getField().getRequirement(), type[i].equalsIgnoreCase("POSITIVE")));
                logger.debug("Custom Value is generated: " + field.getName() + "=" + value);
            }
        }
    }
    
    public void addFormattedValues() {
        logger.debug("Formats are not used in checked generator");
    }

    public void addFilteredValues() {
        logger.debug("Filters are not used in checked generator");
    }

    public Object initGenerator() throws Exception {
        logger.debug("Generic Generators are not used in checked generator");
        return null;
    }

    protected CustomField getCustomField() {
        return (CustomField) getField();
    }
}
