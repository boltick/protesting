package org.protesting.jft.tcgen.testsuite;

import org.protesting.jft.testsuite.TestCase;
import org.protesting.jft.testsuite.TestField;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;


/**
 * User: ab83625
 * Date: 01.07.2008
 * Time: 11:15:28
 */
public class TestSuiteProperty extends TestSuite {
    public static String value_separator = ", ";

    private Properties prop;

    public TestSuiteProperty(List testCases, File testSuiteFile) {
        super(testCases, testSuiteFile);
        this.prop = new Properties();
    }

    protected void createTestSuite() {
        logger.debug("Create Test suite Property - init");
        try {
            if (getTestSuiteFile().exists()) {
                prop.load(new FileInputStream(getTestSuiteFile()));
            }
        } catch (IOException e) {
            logger.error("Error reading output property file: " + getTestSuiteFile().getPath(), e);
        }

        if (getTestCases().size()<=0) {
            return;
        }

        for (int i = 0; i < getTestCases().size(); i++) {
            TestCase aCase = (TestCase) getTestCases().get(i);
            for (int j = 0; j < aCase.getFields().size(); j++) {
                TestField field = (TestField) aCase.getFields().get(j);
                String type = field.getValue().isOk() ? "positive": "negative";
                addValue(field.getName()+"_"+type, field.getValue().getValue().toString());
            }
        }
        logger.debug("Create Test suite Property - finished");
    }


    protected void printToFile() {
        try {
            FileOutputStream fos = new FileOutputStream(getTestSuiteFile());
            prop.store(fos, " Data provided by org.protesting.jft data generation tool.(C) ProTesting.Ru");
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void addValue(String key, String value) {
        if (prop.containsKey(key)) {
            String temp = prop.getProperty(key);
            // fixed to be verified
            int foundIndex = Arrays.binarySearch(temp.split(value_separator), value);
            if (foundIndex < 0) {
                value = temp + value_separator + value;
            } else {
                return;
            }
        }
        prop.setProperty(key, value);
    }

    protected OutputStream printToStream() {
        OutputStream out = new ByteArrayOutputStream();
        try {
            prop.store(out, "");
            return out;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void setValueSeparator(String separator) {
        value_separator = separator;
    }

}
