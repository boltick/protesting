/*
 * JTR
 *
 * Copyright (c) 2007 Gramant. All Rights Reserved
 */
package com.gramant.jtr;

import org.jdom.JDOMException;
import org.jdom.Element;
import org.apache.commons.cli.*;
import org.apache.commons.lang.ArrayUtils;

import java.util.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Constructor;

import com.gramant.jtr.annotation.DataGenerator;
import com.gramant.jtr.annotation.Test;
import com.gramant.jtr.annotation.TestPrecondition;
import com.gramant.jtr.annotation.TestPostcondition;
import com.gramant.jtr.log.ResultsLogger;

/**
 * Singleton JTR runner
 * Java test runner with final report generation
 * @author Alexey Sadykov, Alexey Bulat
 * @version (VCS$Id:$)
 */
public class JTR {


    protected static final ResultsLogger LOG = (ResultsLogger) ResultsLogger.getLogger(JTR.class);

    private static Properties properties;
    private static JTR jtr;

    static Option help = new Option("help", "print this message");
    static Option runPreconditionXml = OptionBuilder.withArgName("filepath")
            .hasArg()
            .withDescription("run precondition xml file")
            .create("rprexml");
    static Option props = OptionBuilder.withArgName("filepath")
            .hasArg()
            .withDescription("global properties file")
            .create("props");
    static Option suites = OptionBuilder.withArgName("filepath")
            .hasArg()
            .withDescription("suites list file")
            .create("suites");
    static Option output = OptionBuilder.withArgName("output")
            .hasArg()
            .withDescription("output results folder")
            .create("output");
    static Option testtype = OptionBuilder.withArgName("testtype")
            .hasArg()
            .withDescription("define test type: smoke, functional, regression. If empty than all tests will be run")
            .create("testtype");
    static Options options = new Options();

    private ArrayList <Object> processedSuites;

    static {
        options.addOption(help);
        options.addOption(runPreconditionXml);
        options.addOption(props);
        options.addOption(output);
        options.addOption(suites);
        options.addOption(testtype);
    }

    private JTR(String[] args) throws IOException {
        PosixParser parser = new PosixParser();
        CommandLine line = null;
        try {
            // parse the command line arguments
            line = parser.parse(options, args);
        } catch (ParseException exp ) {
            // oops, something went wrong
            LOG.error("Parsing failed.  Reason: " + exp.getMessage());
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("jtr", options);
            exp.printStackTrace();
            System.exit(0);
        }
        if (line.hasOption(help.getOpt())) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("jtr", options);
            System.exit(0);
        }
        if (line.hasOption(props.getOpt())) {
//            properties = new Properties();
            String files_path = line.getOptionValue(props.getOpt());
            files_path = files_path.substring(0, files_path.lastIndexOf("/"));
            properties.load(new FileInputStream(line.getOptionValue(props.getOpt())));
        }
        if (line.hasOption(testtype.getOpt())) {
            LOG.info("Tests with type " + testtype.getValue() + " will be run");
        }
        String folder = line.hasOption(output.getOpt()) ? line.getOptionValue(output.getOpt()) : "results";
        ResultsWriter.getInstance().setResultsFolder(folder);
        if (line.hasOption(runPreconditionXml.getOpt())) {
            String suiteXmlPath = line.getOptionValue(runPreconditionXml.getOpt());
            Suite preSuite = parseXml(suiteXmlPath);
            if (preSuite == null) {
                return;
            }
            Element element = ResultsWriter.getInstance().addSuite(preSuite);
            try {
                TestResult preSuiteResult = runSuite(preSuite, element);
                if (preSuiteResult == TestResult.FAILED) {
                    LOG.error("Precondition run result is FAILED");
                    System.exit(0);
                }
            } catch (ClassNotFoundException e) {
                LOG.errorResult(e.getMessage() + ". Incorrect class name in suite " + preSuite.getClassName());
                ResultsWriter.getInstance().addElementResult(element, TestResult.FAILED, LOG.popStepList());
                System.exit(0);
            }
        }
        if (!line.hasOption(suites.getOpt())) {
            LOG.error("Suite's " + suites.getArgName() + " is mandatory");
            System.exit(0);
        }
    }


    public static void main(String[] args) throws IOException {
        init(args);
        JTR jtr = getInstance();

        List files = ListReader.getSuitesFiles(jtr.getSuites().getValue());
        jtr.start(files);

    }

    public static void init(String[] args) {
        properties = new Properties();
        try {
            jtr = new JTR(args);
        } catch (IOException e) {
            jtr = null;
        }
    }

    public static JTR getInstance() {
        if (jtr == null) {
            LOG.errorResult("JTR is not initialized");
            System.exit(0);
        }
        return jtr;
    }


    public static JTR getInstance(String[] args) {
        try {
            properties = new Properties();
            return new JTR(args);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }



    public void start(List files) throws IOException {
        processedSuites = new ArrayList();
        for (Object aListSet : files) {
            String suiteXmlPath = (String) aListSet;
            Suite suite = parseXml(suiteXmlPath);
            processedSuites.add(aListSet);
            if (suite == null)
                continue;
            Element element = ResultsWriter.getInstance().addSuite(suite);
            try {
                runSuite(suite, element);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                LOG.errorResult(e.getMessage() + ". Incorrect class name in suite " + suite.getClassName());
                ResultsWriter.getInstance().addElementResult(element, TestResult.FAILED, LOG.popStepList());
            }
        }
    }

    public static Suite parseXml(String suiteXmlPath) throws IOException {
        SuiteXmlParser suiteXmlParser = new SuiteXmlParser(suiteXmlPath);
        Suite suite = null;
        try {
            suite = suiteXmlParser.getSuite();
        } catch (JDOMException e) {
            String fileName = suiteXmlPath.substring(suiteXmlPath.lastIndexOf("/"));
            Element element = ResultsWriter.getInstance().addSuite(new Suite(fileName));
            LOG.errorResult(e.getMessage() + ". Problems with parsing " + suiteXmlPath);
            ResultsWriter.getInstance().addElementResult(element, TestResult.FAILED, LOG.popStepList());
        } catch (IOException e) {
            String fileName = suiteXmlPath.substring(suiteXmlPath.lastIndexOf("/"));
            Element element = ResultsWriter.getInstance().addSuite(new Suite(fileName));
            LOG.errorResult(e.getMessage() + ". Problems with IO " + suiteXmlPath);
            ResultsWriter.getInstance().addElementResult(element, TestResult.FAILED, LOG.popStepList());
        }
        return suite;
    }



    private TestResult runSuite(final Suite suite, Element suiteElement) throws ClassNotFoundException, IOException {
        final Class suiteClass = Class.forName(suite.getClassName());
        final TestResult[] suiteResult = {TestResult.PASSED};

        try {
            Method setupMethod = suiteClass.getMethod("suiteSetup");
            suiteResult[0] = runStaticMethod(suiteElement, suiteClass, setupMethod, ResultsWriter.PRECONDITION);
            if (suiteResult[0] == TestResult.FAILED) {
                ResultsWriter.getInstance().addElementResult(suiteElement, suiteResult[0], LOG.popStepList());
                return suiteResult[0];
            }
        } catch (NoSuchMethodException e) {
        }
        final Element suiteResultsElement = ResultsWriter.getInstance().addNewElement(suiteElement, ResultsWriter.SUITE_RESULTS);
        final List<com.gramant.jtr.Test> tests = suite.getTestList();
        for (int i = 0; i < tests.size(); i++) {
            final com.gramant.jtr.Test test = tests.get(i);
            LOG.info("Run test group " + test.getGroup());
            final Element testGroupElement = ResultsWriter.getInstance().addTestGroupElement(suiteResultsElement, test);
            String group = test.getGroup();
            final List<Method> testMethods = getTestMethods(suiteClass, group);
            if (testMethods.isEmpty()) {
                LOG.errorResult("Test methods are not detected where group = " + group);
                ResultsWriter.getInstance().addElementResult(testGroupElement, TestResult.FAILED, LOG.popStepList());
                break;
            }

            Method dataGeneratorMethod = getDataGeneratorMethod(suiteClass, group);

            final List objects;
            if (dataGeneratorMethod == null) {
                objects = Collections.singletonList(null);
            } else {
                try {
                    LOG.info("Run data generator method: run method " + dataGeneratorMethod.getName() + " of class " + suiteClass.getName());
                    objects = (List) dataGeneratorMethod.invoke(null, null);
                } catch (InvocationTargetException e) {
                    LOG.errorResult(e.getTargetException().toString());
                    ResultsWriter.getInstance().addElementResult(testGroupElement, TestResult.FAILED, LOG.popStepList());
                    break;
                } catch (IllegalAccessException e) {
                    LOG.errorResult(e.getMessage() + ". Method " + dataGeneratorMethod.getName() + " is not public");
                    ResultsWriter.getInstance().addElementResult(testGroupElement, TestResult.FAILED, LOG.popStepList());
                    break;
                }
            }

            final Method preconditionMethod = getPreconditionMethod(suiteClass, group);
            final Method postconditionMethod = getPostconditionMethod(suiteClass, group);
            final TestResult[] testGroupResult = {TestResult.PASSED};

            for (int j = 0; j < objects.size(); j++) {
                Object testData = objects.get(j);
                Element testElement = ResultsWriter.getInstance().addTestElement(testGroupElement, testData);
                Element testPreconditionElement = ResultsWriter.getInstance().addNewElement(testElement, ResultsWriter.PRECONDITION);
                AbstractSuite testObject = createObject(suiteClass, testData);
//                if (testObject == null) {
//                    testGroupResult = TestResult.FAILED;
//                    continue;
//                }
                TestResult preconditionMethodResult = TestResult.PASSED;
                if (preconditionMethod != null) {
                    LOG.info("Run test precondition: run method " + preconditionMethod.getName() + " of class " + suiteClass.getName());
                    preconditionMethodResult = runMethod(testPreconditionElement, testObject, preconditionMethod);
                }
                ResultsWriter.getInstance().addElementResult(testPreconditionElement, preconditionMethodResult, LOG.popStepList());
                if (preconditionMethodResult == TestResult.FAILED | preconditionMethodResult == TestResult.SKIPPED) {
                    testGroupResult[0] = preconditionMethodResult;
                    // copypast nah
                    if (postconditionMethod != null) {
                        LOG.info("Run test postcondition: run method " + postconditionMethod.getName() + " of class " + suiteClass.getName());
                        Element testPostconditionElement = ResultsWriter.getInstance().addNewElement(testElement, ResultsWriter.POSTCONDITION, test.getName());
                        TestResult postconditionMethodResult = runMethod(testPostconditionElement, testObject, postconditionMethod);
                        ResultsWriter.getInstance().addElementResult(testPostconditionElement, postconditionMethodResult, LOG.popStepList());
                    }
                    continue;
                }
                Element testResultsElement = ResultsWriter.getInstance().addNewElement(testElement, ResultsWriter.TEST_RESULTS);
                testResultsElement.setAttribute("num_of_methods", String.valueOf(testMethods.size()));
                TestResult testResult = null;
                boolean hasFailed = false;
                boolean hasPassed = false;
                for (Method testMethod : testMethods) {
                    LOG.info("Run test method: run method " + testMethod.getName() + " of class " + suiteClass.getName());
                    TestResult methodResult = runMethod(testResultsElement, testObject, testMethod);
//                    testGroupResult[0] = methodResult;
                    if (methodResult == TestResult.FAILED) {
                        hasFailed = true;
                        if (!testMethod.getAnnotation(Test.class).runIfPreviousMethodFailed()) {
                            testResult = TestResult.FAILED;
                            break;
                        }
                    }
                    if ((methodResult == TestResult.SKIPPED) && !testMethod.getAnnotation(Test.class).runIfPreviousMethodSkipped()) {
                        testResult = TestResult.SKIPPED;
                        break;
                    }
                    if (methodResult == TestResult.PASSED)
                        hasPassed = true;
                }
                if (testResult == null) {
                    if (hasFailed)
                        testResult = TestResult.FAILED;
                    else {
                        if (hasPassed)
                            testResult = TestResult.PASSED;
                        else
                            testResult = TestResult.SKIPPED;
                    }
                }
                if (testResult != TestResult.PASSED)
                    testGroupResult[0] = testResult;
                ResultsWriter.getInstance().addElementResult(testResultsElement, testResult, LOG.popStepList());
                if (postconditionMethod != null) {
                    LOG.info("Run test postcondition: run method " + postconditionMethod.getName() + " of class " + suiteClass.getName());
                    Element testPostconditionElement = ResultsWriter.getInstance().addNewElement(testElement, ResultsWriter.POSTCONDITION, test.getName());
                    TestResult postconditionMethodResult = runMethod(testPostconditionElement, testObject, postconditionMethod);
                    ResultsWriter.getInstance().addElementResult(testPostconditionElement, postconditionMethodResult, LOG.popStepList());
                }
                ResultsWriter.getInstance().addElementResult(testElement, testResult, LOG.popStepList());
            }

            if (testGroupResult[0] != TestResult.PASSED)
                suiteResult[0] = TestResult.FAILED;
            ResultsWriter.getInstance().addElementResult(testGroupElement, testGroupResult[0], LOG.popStepList());
        }

        ResultsWriter.getInstance().addElementResult(suiteResultsElement, suiteResult[0], LOG.popStepList());
        try {
            Method setupMethod = suiteClass.getMethod("suiteFinish");
            TestResult suiteFinishResult = runStaticMethod(suiteElement, suiteClass, setupMethod, ResultsWriter.POSTCONDITION);
            if (suiteFinishResult == TestResult.FAILED)
                suiteResult[0] = TestResult.FAILED;
        } catch (NoSuchMethodException e) {
        }
        ResultsWriter.getInstance().addElementResult(suiteElement, suiteResult[0], LOG.popStepList());
        return suiteResult[0];
    }

    private TestResult runMethod(Element testResultsElement, Object object, Method testMethod) throws IOException {
        Element methodElement = ResultsWriter.getInstance().addNewElement(testResultsElement, ResultsWriter.METHOD_RESULTS, testMethod.getName());
        TestResult testResult = TestResult.FAILED;
        try {
            LOG.action("Run test method " + testMethod.getName());
            if (testMethod.getReturnType() == TestResult.class)
                testResult = (TestResult) testMethod.invoke(object);
            else {
                if (object != null) {
                    testMethod.invoke(object);
                    testResult = TestResult.PASSED;
                } else {
                    testResult = TestResult.FAILED;
                }
            }
        } catch (InvocationTargetException e) {
//            LOG.error(e, e);
            LOG.errorResult(e.getTargetException().toString(), e);
        } catch (IllegalAccessException e) {
            LOG.errorResult(e.getMessage() + ". Method " + testMethod.getName() + " is not public");
        } finally {
            ResultsWriter.getInstance().addElementResult(methodElement, testResult, LOG.popStepList());
        }
        return testResult;
    }

    private TestResult runStaticMethod(Element suiteElement, Class suiteClass, Method staticMethod, String methodType) throws IOException {
        LOG.info("Run suite " + methodType + ": run method " + staticMethod.getName() + " of class " + suiteClass.getName());
        Element preconditionElement = ResultsWriter.getInstance().addNewElement(suiteElement, methodType);
        TestResult testResult = TestResult.FAILED;
        try {
            testResult = (TestResult) staticMethod.invoke(null, null);
        } catch (InvocationTargetException e) {
            LOG.errorResult(e.getTargetException().toString());
        } catch (IllegalAccessException e) {
            LOG.errorResult(e.getMessage() + ". Method " + staticMethod.getName() + " is not public");
        }
        ResultsWriter.getInstance().addElementResult(preconditionElement, testResult, LOG.popStepList());
        return testResult;
    }

    private AbstractSuite createObject(Class suiteClass, Object data) throws IOException {
        LOG.info("Create test object" + (data != null ? " where test data: " + data : ""));
        Constructor constructor;
        AbstractSuite createdObject = null;
        try {
            constructor = suiteClass.getConstructor(new Class[]{Object.class});
        } catch (NoSuchMethodException e) {
            LOG.errorResult(e.getMessage() + ". Constructor doesn't exist in class " + suiteClass.getName());
            return createdObject;
        }
        try {
            createdObject = (AbstractSuite) constructor.newInstance(data);
        } catch (InstantiationException e) {
            LOG.errorResult(e.getMessage());
        } catch (IllegalAccessException e) {
            LOG.errorResult(e.getMessage() + ". Constructor is not public");
        } catch (InvocationTargetException e) {
            LOG.errorResult(e.getTargetException().toString());
            e.printStackTrace();
        }
        return createdObject;
    }

    private Method getMethod(Class suiteClass, String group, Class aClass) {
        Method[] methods = suiteClass.getDeclaredMethods();
        for (Method method : methods) {
            Object obj = method.getAnnotation(aClass);
            if (obj != null) {
                Method [] annoMethods = obj.getClass().getDeclaredMethods();
                for (int i = 0; i < annoMethods.length; i++) {
                    Method annoMethod = annoMethods[i];
                    if (annoMethod.getName().equalsIgnoreCase("groups")) {
                        method.setAccessible(true);
                        try {
                            annoMethod.setAccessible(true);
                            Object res = annoMethod.invoke(obj);
                            if (res != null) {
                                String [] groups = (String []) res;
                                if (ArrayUtils.contains(groups, group)) {
                                    return method;
                                }
                            }
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        }
                    }
                }
            }
        }
        return null;
    }

    private Method getDataGeneratorMethod(Class suiteClass, String group) {
        return getMethod(suiteClass,group, DataGenerator.class);
    }

    private Method getPreconditionMethod(Class suiteClass, String group) {
        return getMethod(suiteClass,group, TestPrecondition.class);
    }

    private Method getPostconditionMethod(Class suiteClass, String group) {
        return getMethod(suiteClass,group, TestPostcondition.class);
    }

    private List<Method> getTestMethods(Class suiteClass, String group) {
        List<Method> testMethods = new ArrayList<Method>();
        Method[] methods = suiteClass.getMethods();
        for (Method method : methods) {
            Test test = method.getAnnotation(Test.class);
            if ((test != null) && (ArrayUtils.contains(test.groups(), group)))
                testMethods.add(method);
        }
        List<Method> testMethodsSorted = new ArrayList<Method>(testMethods.size());
        Method parentMethod = null;
        while ((parentMethod = getMethod(parentMethod, testMethods)) != null) {
            testMethodsSorted.add(parentMethod);
        }
        return testMethodsSorted;
    }

    private Method getMethod(Method parentMethod, List<Method> testMethods) {
        for (Method method : testMethods) {
            Test test = method.getAnnotation(Test.class);
            if (parentMethod == null) {
                if (test.parentMethod().equals(""))
                    return method;
            } else {
                if (test.parentMethod().equals(parentMethod.getName()))
                    return method;
            }
        }
        return null;
    }


    public static String getProperty(String key) {
        if (properties!=null)
            return properties.getProperty(key)!=null ? properties.getProperty(key).trim() : "";
        else
            return "";
    }

    public static void setProperties(Properties properties) {
        JTR.properties = properties;
    }


    public Option getSuites() {
        return suites;
    }


    public ArrayList<Object> getProcessedSuites() {
        return processedSuites;
    }
}
