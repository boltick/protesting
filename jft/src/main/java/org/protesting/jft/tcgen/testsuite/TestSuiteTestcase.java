package org.protesting.jft.tcgen.testsuite;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.protesting.jft.testsuite.TestCase;
import org.protesting.jft.testsuite.TestField;
import org.w3c.dom.Element;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 *
 * User: abulat
 * Date: 19.02.2008
 * Time: 21:30:53
 */
public class TestSuiteTestcase extends TestSuiteXml {
    private static final String XML_TEST_SUITE = "test-suite";

    public TestSuiteTestcase(List testCases, File testSuiteFile) {
        super(testCases, testSuiteFile);
    }


    protected void createTestSuite(){
        Element rootEle = getDom().createElement(XML_TEST_SUITE);
        getDom().appendChild(rootEle);
          
        Iterator it  = getTestCases().iterator();
        while(it.hasNext()) {
            TestCase test = (TestCase)it.next();
            Element testCase = createTestCaseElement(test);
            rootEle.appendChild(testCase);
        }

    }

    private Element createTestCaseElement(TestCase testCase){

        Element testCaseElement = getDom().createElement(TestCaseXMLConst.TEST_CASE);
        testCaseElement.setAttribute(TestCaseXMLConst.ID, testCase.getId());
        testCaseElement.setAttribute(TestCaseXMLConst.NAME, testCase.getName());
        testCaseElement.setAttribute(TestCaseXMLConst.TYPE, testCase.getType());

        boolean isPassed = true;
        for (int i = 0; i < testCase.getFields().size(); i++) {
            TestField field = (TestField) testCase.getFields().get(i);

            Element fieldElement = getDom().createElement(TestCaseXMLConst.TEST_CASE_FIELD);
            fieldElement.setAttribute(TestCaseXMLConst.NAME, field.getName());
            fieldElement.setAttribute(TestCaseXMLConst.TYPE, field.getType());
            fieldElement.setAttribute(TestCaseXMLConst.VALUE, field.getValue().getValue().toString());
            field.getValue().getRequirement();
            Element reqElement = getDom().createElement(TestCaseXMLConst.FIELD_REQUIREMENT);
            reqElement.setAttribute(TestCaseXMLConst.ID, field.getValue().getRequirement().getReqID());
            reqElement.setAttribute(TestCaseXMLConst.FIELD_REQUIREMENT_IS_OK, String.valueOf(field.getValue().isOk()));
            reqElement.setAttribute(TestCaseXMLConst.FIELD_REQUIREMENT_INFO, field.getValue().getDescription());
            fieldElement.appendChild(reqElement);
            if (isPassed && !field.getValue().isOk()) {
                isPassed = false;
            }

            testCaseElement.appendChild(fieldElement);
        }

        return testCaseElement;
    }

    protected void printToFile(){
        try {
            OutputFormat format = new OutputFormat(getDom());
            format.setIndenting(true);


            XMLSerializer serializer = new XMLSerializer(
                    new FileOutputStream(getTestSuiteFile()), format);

            ContentHandler contentHandler = serializer.asContentHandler();
            contentHandler.startDocument();
            contentHandler.processingInstruction("xml-stylesheet", "type='text/xsl' href='test-suite.xsl'");

            serializer.serialize(getDom());

        } catch(IOException ie) {
            ie.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

}