/**
 * Description
 *
 * @version (VCS$Id:$)
 */
package com.gramant.jtr;

import org.jdom.output.XMLOutputter;
import org.jdom.output.Format;
import org.jdom.Element;
import org.jdom.Document;
import org.jdom.ProcessingInstruction;
import org.jdom.Content;

import java.io.IOException;
import java.io.FileOutputStream;
import java.io.File;
import java.util.List;
import java.util.Date;
import java.util.Arrays;
import java.text.SimpleDateFormat;

import com.gramant.jtr.log.Step;


public class ResultsWriter {

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final String PRECONDITION = "precondition";
    public static final String POSTCONDITION = "postcondition";
    public static final String RUN = "run";
    public static final String SUITE = "suite";
    public static final String TEST_GROUP = "test_group";
    public static final String TEST = "test";
    public static final String METHOD = "method";
    public static final String SUITE_RESULTS = "suite_results";
    public static final String TEST_RESULTS = "test_results";
    public static final String METHOD_RESULTS = "method_results";

    private String resultsFolder;
    private Document suiteResultsDocument;
    private Document runResultsDocument;
    private String currentFileName;
//    private String indexFileName;

    private String xsltPath;
    private static ResultsWriter ourInstance = new ResultsWriter();

    public static ResultsWriter getInstance() {
        return ourInstance;
    }

    private ResultsWriter() {
        Element root = new Element(RUN);
        this.xsltPath = JTR.getProperty("xslt.path");
        ProcessingInstruction instruction = new ProcessingInstruction("xml-stylesheet", "type='text/xsl' href='" + getXsltPath() + "run.xsl'");
        runResultsDocument = new Document();
        runResultsDocument.addContent(instruction);
        runResultsDocument.setRootElement(root);
        if (JTR.options.getOption("testtype").getValue()!=null) {
            runResultsDocument.getRootElement().setAttribute("type", JTR.options.getOption("testtype").getValue());
        }
//        this.indexFileName = getResultsFolder()+ File.separator + "run.xml";
    }

    public Element addSuite(Suite suite) throws IOException {
        currentFileName = getResultsFolder() + File.separator + suite.getName() + ".xml";
        Element root = new Element(SUITE);
        root.setAttribute("name", suite.getName());
        root.setAttribute("start", DATE_FORMAT.format(new Date()));
        root.setAttribute("num_of_groups", String.valueOf(suite.getVisibleTestList().size()));
        ProcessingInstruction instruction = new ProcessingInstruction("xml-stylesheet", "type='text/xsl' href='" + getXsltPath() + "suite.xsl'");
        suiteResultsDocument = new Document();
        suiteResultsDocument.addContent(instruction);
        suiteResultsDocument.setRootElement(root);
        writeSuiteFile();
        runResultsDocument.getRootElement().addContent((Content) root.clone());
        writeIndexFile();
        return suiteResultsDocument.getRootElement();
    }

    public Element addNewElement(Element parentElement, String tagName) throws IOException {
        return addNewElement(parentElement, tagName, null);
    }

    public Element addNewElement(Element parentElement, String tagName, String nameAttribute) throws IOException {
        Element element = new Element(tagName);
        element.setAttribute("start", DATE_FORMAT.format(new Date()));
        if (nameAttribute != null)
            element.setAttribute("name", nameAttribute);
        parentElement.addContent(element);
        writeSuiteFile();
        return element;
    }

    public Element addTestElement(Element parentElement, Object testObject) throws IOException {
        Element element = new Element(TEST);
        element.setAttribute("start", DATE_FORMAT.format(new Date()));
        if (testObject != null)
            element.setAttribute("test_data", (testObject instanceof Object[] ? Arrays.toString((Object[]) testObject) : testObject.toString()));
        parentElement.addContent(element);
        writeSuiteFile();
        return element;
    }

    public Element addTestGroupElement(Element parentElement, Test test) throws IOException {
        Element element = new Element(TEST_GROUP);
        element.setAttribute("start", DATE_FORMAT.format(new Date()));
        if (test.getId() != null)
            element.setAttribute("id", test.getId());
        if (test.getName() != null)
            element.setAttribute("name", test.getName());
        if (test.getGroup() != null)
            element.setAttribute("group", test.getGroup());
        element.setAttribute("hidden", ""+test.isHidden());
        parentElement.addContent(element);
        writeSuiteFile();
        return element;
    }

    public void addElementResult(Element element, TestResult result, List<Step> steps) throws IOException {
        element.setAttribute("end", DATE_FORMAT.format(new Date()));
        element.setAttribute("result", result.name());
        for (int i = 0; i < steps.size(); i++) {
            Element stepElement = new Element("step");
            Step step = steps.get(i);
            if (step.getAction() != null) {
                Element actionElement = new Element("action");
                actionElement.setText(step.getAction());
                stepElement.addContent(actionElement);
            }
            if (step.getResult() != null) {
                for (int j = 0; j < step.getResult().size(); j++) {
                    Element actionElement = new Element("result");
                    actionElement.setText(step.getResult().get(j).toString());
                    stepElement.addContent(actionElement);
                }
            }
            if (step.getErrorResult() != null) {
                for (int j = 0; j < step.getErrorResult().size(); j++) {
                    Element actionElement = new Element("error");
                    actionElement.setText(step.getErrorResult().get(j).toString());
                    stepElement.addContent(actionElement);
                }
            }
            if (step.getScreenShot() != null) {
                Element scrElement = new Element("screen-shot");
                scrElement.setText(step.getScreenShot().getPath());
                scrElement.setAttribute("title", step.getScreenShot().getTitle());
                stepElement.addContent(scrElement);
            }
            element.addContent(stepElement);
        }
        writeSuiteFile();
        if (element.getName().equals(SUITE)) {
            List<Element> suiteElements = runResultsDocument.getRootElement().getChildren(SUITE);
            Element suite = suiteElements.get(suiteElements.size() - 1);
            suite.setAttribute("end", DATE_FORMAT.format(new Date()));
            suite.setAttribute("result", result.name());
            writeIndexFile();
        }
    }

    private void writeSuiteFile() throws IOException {
        write(currentFileName, suiteResultsDocument);
    }


    private void writeIndexFile() throws IOException {
        write(getResultsFolder()+ File.separator + "run.xml", runResultsDocument);
    }

           
    private static void write(String fileName, Document document) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(fileName);
        XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
        outputter.output(document, fileOutputStream);
        fileOutputStream.close();
    }


    public String getXsltPath() {
        String linuxFileSeparator = "/";
        String xslt = xsltPath != null && !xsltPath.equals("") ? xsltPath : "";
        if (xslt.indexOf(File.separator) != -1) {
            xslt  = xslt.endsWith(File.separator) ? xslt : xslt + File.separator;
        } else  if (xslt.indexOf(linuxFileSeparator) != -1) {
            xslt  = xslt.endsWith(linuxFileSeparator) ? xslt : xslt + linuxFileSeparator;
        }

        return xslt;
    }


    public String getResultsFolder() {
        return resultsFolder;
    }

    public void setResultsFolder(String resultsFolder) {
        this.resultsFolder = resultsFolder;
        File resultFolder = new File(resultsFolder);
        if (!resultFolder.exists()) {
            resultFolder.mkdir();
        }
    }
}