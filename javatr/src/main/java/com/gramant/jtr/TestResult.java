package com.gramant.jtr;

/**
 * Description
 *
 * @version (VCS$Id:$)
 */
public enum TestResult {
    PASSED,FAILED,SKIPPED;

    public TestResult and(TestResult testResult) {
        if ((this == FAILED) || (testResult == FAILED))
            return FAILED;
        if ((this == PASSED) || (testResult == PASSED))
            return PASSED;
        return SKIPPED;
    }
}
