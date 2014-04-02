/*
 * Sample
 *
 * Copyright (c) 2007 Gramant. All Rights Reserved
 */
package com.gramant.jtr;

import java.util.List;
import java.util.ArrayList;

import com.gramant.jtr.annotation.Test;
import com.gramant.jtr.annotation.DataGenerator;

/**
 * Description
 *
 * @version (VCS$Id:$)
 */
public class Sample extends AbstractSuite {

    public Sample(Object testObject) {
        super(testObject);
        LOG.action("SETUP TEST");
    }

    public static TestResult suiteSetup() {
        LOG.action("SETUP SUITE");
        return TestResult.PASSED;
    }

    @DataGenerator (groups = {"aaa"})
    public static List dataGenerator() {
        LOG.action("Data generation");
        List values = new ArrayList();
        values.add("AAAA");
        values.add("BBBB");
        return values;
    }

    @Test (groups = {"aaa"}, firstMethod = true)
    public TestResult test1() {
        LOG.action(testObject + " test1 ");
        LOG.result(testObject + " test1 ");
        return TestResult.PASSED;
    }

    @Test (groups = {"aaa"}, parentMethod = "test1")
    public TestResult test2() {
        LOG.action(testObject + " test2 ");
        LOG.result(testObject + " test2 ");
        return TestResult.FAILED;
    }

    @Test (groups = {"test1"}, firstMethod = true)
    public TestResult test3() {
        LOG.action(testObject + " test3 ");
        LOG.result(testObject + " test3 ");
        return TestResult.SKIPPED;
    }
}
