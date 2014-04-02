/*
 * SuiteXmlParser
 *
 * Copyright (c) 2007 Gramant. All Rights Reserved
 */
package com.gramant.jtr;

import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import java.util.List;
import java.io.File;
import java.io.IOException;

/**
 * Description
 *
 * @version (VCS$Id:$)
 */
public class SuiteXmlParser {
    private String filePath;

    public SuiteXmlParser(String filePath) {
        this.filePath = filePath;
    }

    public Suite getSuite() throws JDOMException, IOException {
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(new File(filePath));
        Element suiteElement = doc.getRootElement();
        Suite suite = new Suite();
        suite.setId(suiteElement.getAttributeValue("id"));
        suite.setName(suiteElement.getAttributeValue("name"));
        suite.setClassName(suiteElement.getAttributeValue("class"));
        List<Element> testList = suiteElement.getChildren("test");
        for (Element testElement : testList) {
            Test test = new Test();
            if (JTR.options.getOption("testtype")!=null && JTR.options.getOption("testtype").getValue()!=null) {
                if(testElement.getAttributeValue("type")!=null && testElement.getAttributeValue("type").length()!=0) {
                    if(!JTR.options.getOption("testtype").getValue().equalsIgnoreCase(testElement.getAttributeValue("type")) & !testElement.getAttributeValue("type").equalsIgnoreCase("postcondition")) { continue; }
                }
            }
            test.setId(testElement.getAttributeValue("id"));
            test.setName(testElement.getAttributeValue("name"));
            test.setGroup(testElement.getAttributeValue("group"));
            test.setType(testElement.getAttributeValue("type"));
            if(testElement.getAttribute("hidden") != null) {
                test.setHidden(testElement.getAttribute("hidden").getBooleanValue());
            }
            suite.addTest(test);
        }
        if(suite.getTestList().size()==0) { return null; }
        return suite;
    }


    public static void main(String[] args) throws JDOMException, IOException, ParseException {
        args = "-testtype smoke".split(" ");
        PosixParser parser = new PosixParser() ;
        parser.parse(JTR.options, args);
        SuiteXmlParser suiteXmlParser = new SuiteXmlParser("E:\\svn_repo\\SANDBOX\\autotest\\olf-test\\trunk\\bin\\target\\olf-test-standalone-1.0.0\\config\\suites\\smoke\\action_server.xml");
        Suite suite = suiteXmlParser.getSuite();
        System.out.println(suite);
    }

}
