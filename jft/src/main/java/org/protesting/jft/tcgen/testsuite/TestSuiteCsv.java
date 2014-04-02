package org.protesting.jft.tcgen.testsuite;

import org.protesting.jft.testsuite.TestCase;
import org.protesting.jft.testsuite.TestField;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


/**
 * User: ab83625
 * Date: 25.03.2008
 * Time: 10:40:03
 */
public class TestSuiteCsv extends TestSuite {

    protected List fileList;
    protected StringBuffer sb;

    public TestSuiteCsv(List testCases, File testSuiteFile) {
        super(testCases, testSuiteFile);
        this.fileList = new ArrayList();
    }

    protected void createTestSuite() {
        logger.debug("Create Test suite Csv- init");
        this.sb = new StringBuffer();
        for (int i = 0; i < getTestCases().size(); i++) {
            TestCase test = (TestCase) getTestCases().get(i);
            sb.append("TEST CASE ID").append(";");
            for (int j = 0; j < test.getFields().size(); j++) {
                TestField testField = (TestField) test.getFields().get(j);                                                                                  
                sb.append(testField.getName()).append(";");
            }
            sb.append(System.getProperty("line.separator"));
            break;
        }
        fileList.add(sb.toString());
        for (int i = 0; i < getTestCases().size(); i++) {
            TestCase test = (TestCase) getTestCases().get(i);
            String testStr = test.toString();
            fileList.add(testStr);
            sb.append(testStr);
        }
        logger.debug("Create Test suite Csv - finished");
    }


    protected void printToFile(){
        FileWriter fstream = null;
        try {
            fstream = new FileWriter(getTestSuiteFile());
            BufferedWriter out = new BufferedWriter(fstream);
            for (int i = 0; i < fileList.size(); i++) {
                String s = (String) fileList.get(i);
                out.write(s);
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    protected OutputStream printToStream() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            out.write(getSb().toString().getBytes("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out;
    }

    private StringBuffer getSb() {
        return this.sb;
    }

}
