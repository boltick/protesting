/*
 * AbstractSuite
 *
 * Copyright (c) 2007 Gramant. All Rights Reserved
 */
package com.gramant.jtr;

import com.gramant.jtr.log.ResultsLogger;

/**
 * Description
 *
 * @version (VCS$Id:$)
 */
public abstract class AbstractSuite {
    public static final ResultsLogger LOG = (ResultsLogger) ResultsLogger.getLogger(AbstractSuite.class);
    protected Object testObject;

    public AbstractSuite(Object testObject) {
        this.testObject = testObject;
    }

    public AbstractSuite() {}

    public static TestResult compare(Object obj1, Object obj2) {
        if ((obj1 == null) && (obj2 == null))
            return TestResult.PASSED;
        if (((obj1 == null) && (obj2 != null)) || ((obj1 != null) && (obj2 == null)))
            return TestResult.FAILED;
        if (obj1.equals(obj2))
            return TestResult.PASSED;
        else
            return TestResult.FAILED;
    }
}
