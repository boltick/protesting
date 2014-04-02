package org.protesting.jft.tcgen.testsuite;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.protesting.jft.testsuite.TestCase;
import org.protesting.jft.testsuite.TestField;
import org.protesting.jft.utils.ParseHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.Iterator;
import java.util.List;

/**
 *
 * User: abulat
 * Date: 19.02.2008
 * Time: 21:30:53
 */
public class TestSuiteXml extends TestSuite {

    private Document dom;

    public TestSuiteXml(List testCases, File testSuiteFile) {
        super(testCases, testSuiteFile);
        this.dom = ParseHelper.getNewDocument();
    }


    protected void createTestSuite() {
        if (!testCases.isEmpty()) {
            Element rootEle = dom.createElement(((TestCase)testCases.get(0)).getId().split("_")[0].toLowerCase());
            dom.appendChild(rootEle);

            Iterator it  = getTestCases().iterator();
            while(it.hasNext()) {
                TestCase test = (TestCase)it.next();
                Element testCase = createTestCaseElement(test);
                rootEle.appendChild(testCase);
            }
        }
    }

    private Element createTestCaseElement(TestCase testCase) {

        Element testCaseElement = dom.createElement(testCase.getId().toLowerCase());

        boolean isPassed = true;
        for (int i = 0; i < testCase.getFields().size(); i++) {
            TestField field = (TestField) testCase.getFields().get(i);
            // create modified field name
            Element fieldElement = dom.createElement(field.getName().replaceFirst("[^a-zA-Z]*", "").toLowerCase());

            Text reqElement = dom.createTextNode(field.getValue().getValue().toString());
            fieldElement.appendChild(reqElement);
            if (isPassed && !field.getValue().isOk()) {
                isPassed = false;
            }

            testCaseElement.appendChild(fieldElement);
        }

        return testCaseElement;
    }


    protected void printToFile() {
        try {
            OutputFormat format = new OutputFormat(dom);
            format.setIndenting(true);

            XMLSerializer serializer = new XMLSerializer(
                    new FileOutputStream(getTestSuiteFile()), format);

            serializer.serialize(dom);

        } catch(IOException ie) {
            ie.printStackTrace();
        }
    }

    protected OutputStream printToStream() {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Transformer transfor = TransformerFactory.newInstance().newTransformer();
            Source src = new DOMSource(dom);
            Result result = new StreamResult(outputStream);
            transfor.transform(src, result);

            return outputStream;
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        return null;
    }


    public Document getDom() {
        return dom;
    }

    public void setDom(Document dom) {
        this.dom = dom;
    }
}