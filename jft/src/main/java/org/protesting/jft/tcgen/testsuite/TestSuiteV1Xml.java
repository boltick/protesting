package org.protesting.jft.tcgen.testsuite;

import org.protesting.jft.testsuite.TestCase;
import org.protesting.jft.testsuite.TestField;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * User: ab83625
 * Date: 01.07.2008
 * Time: 11:15:28
 */
public class TestSuiteV1Xml extends TestSuiteXml {
    public static String value_separator = ", ";

    public TestSuiteV1Xml(List testCases, File testSuiteFile) {
        super(testCases, testSuiteFile);
    }

    protected void createTestSuite() {
        logger.debug("Create Test suite Xml-v1 - started");
        Element rootEle = getDom().createElement(((TestCase)testCases.get(0)).getId().split("_")[0].toLowerCase());
        getDom().appendChild(rootEle);


        TestCase testCase = (TestCase) getTestCases().get(0);
        for (int i = 0; i < testCase.getFields().size(); i++) {
            TestField field = (TestField) testCase.getFields().get(i);
            Element fieldElement = getDom().createElement(field.getName().replaceFirst("[^a-zA-Z]*", "").toLowerCase());
            addDataValue(fieldElement, field); 
            rootEle.appendChild(fieldElement);
        }

        logger.debug("Create Test suite Xml-v1 - finished");
    }

    private void addDataValue(Element element, TestField field) {
        List checkList = new ArrayList();
        for (int i = 0; i < getTestCases().size(); i++) {
            TestCase aCase = (TestCase) getTestCases().get(i);
            String container = aCase.getValueByFieldName(field.getName()).toString();
            if (!checkList.contains(container)) {
                checkList.add(container);
                Element value = getDom().createElement("value");
                Text valueTxt = getDom().createTextNode(container);
                value.appendChild(valueTxt);
                element.appendChild(value);
            }
        }
    }

    protected void printToFile() {
        super.printToFile();
    }

}
