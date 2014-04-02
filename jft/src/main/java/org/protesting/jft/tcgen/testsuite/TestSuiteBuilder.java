package org.protesting.jft.tcgen.testsuite;

import java.io.File;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * User: ab83625
 * Date: 25.03.2008
 * Time: 11:25:28
 */
public class TestSuiteBuilder {

    private static final String [] SUPPORTED_OUTPUT_MODES = {"xml", "v1-xml", "csv", "property", "testcase"};

    public static void buildTestSuite(List testCases, String suiteFile, String mode) {

        String className = TestSuiteBuilder.class.getPackage() + ".TestSuite" + getName(mode);

        Class suiteClass = null;
        File file = new File(suiteFile);
        try {
            suiteClass = Class.forName(className.split(" ")[1]);
            Object[] args = new Object[] { testCases ,  file };
            Class[] types = new Class[] { List.class, file.getClass()};
            TestSuite suite = (TestSuite) suiteClass.getConstructor(types).newInstance(args);
            Method m = suite.getClass().getMethod("create", new Class[]{});
            m.invoke(suite, new Object[]{});
        } catch (ClassNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (NoSuchMethodException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IllegalAccessException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InvocationTargetException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InstantiationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    public static OutputStream buildTestSuiteStream(List testCases, String suiteFile, String mode) {

        String className = TestSuiteBuilder.class.getPackage() + ".TestSuite" + getName(mode);

        Class suiteClass = null;
        File file = new File(suiteFile);
        try {
            suiteClass = Class.forName(className.split(" ")[1]);
            Object[] args = new Object[] { testCases ,  file };
            Class[] types = new Class[] { List.class, file.getClass()};
            TestSuite suite = (TestSuite) suiteClass.getConstructor(types).newInstance(args);
            Method m = suite.getClass().getMethod("createStream", new Class[]{});
            return (OutputStream) m.invoke(suite, new Object[]{});
        } catch (ClassNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (NoSuchMethodException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IllegalAccessException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InvocationTargetException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InstantiationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }

    public static boolean isSupportedMode(String mode) {
        return Arrays.asList(SUPPORTED_OUTPUT_MODES).contains(mode.toLowerCase());
    }


    private static String getName(String mode) {
        String [] modeArray = mode.split("-");
        String modifiedMode = "";
        for (int i = 0; i < modeArray.length; i++) {
            modifiedMode = modifiedMode + modeArray[i].substring(0, 1).toUpperCase() + modeArray[i].substring(1, modeArray[i].length());
        }
        return modifiedMode; 

    }

}
