package org.protesting.jft.testsuite;

import java.util.List;

/**
 * User: ab83625
 * Date: 04.06.2008
 * Time: 13:06:15
 */
public abstract class AbstractTestCaseGenerator {

    public static final String METHOD_ONE_BY_ONE = "oneByOne";
    public static final String METHOD_ONE_GO = "oneGo";
    public static final String TYPE_POSITIVE = "positive";
    public static final String TYPE_NEGATIVE = "negative";

    public abstract List getPositiveOneByOne();

    public abstract List getNegativeOneByOne();

    public abstract List getPositiveOneGo();

    public abstract List getNegativeOneGo();


    public List getTestCases(String type, String method) {

        if (method.equalsIgnoreCase(METHOD_ONE_GO)) {
            if (type.equalsIgnoreCase(TYPE_POSITIVE)) {
                return getPositiveOneGo();
            }
            if (type.equalsIgnoreCase(TYPE_NEGATIVE)) {
                return getNegativeOneGo();
            }

        } else if (method.equalsIgnoreCase(METHOD_ONE_BY_ONE)) {
            if (type.equalsIgnoreCase(TYPE_POSITIVE)) {
                return getPositiveOneByOne();
            }
            if (type.equalsIgnoreCase(TYPE_NEGATIVE)) {
                return getNegativeOneByOne();
            }

        }
        throw new IllegalStateException("Incorrect generation type or method is defined");
    }



}
