/*
 * TestMethod
 *
 * Copyright (c) 2007 Gramant. All Rights Reserved
 */
package com.gramant.jtr;

/**
 * Description
 *
 * @version (VCS$Id:$)
 */
public class TestMethod {
    private String name;
    private boolean runIfPreviousMethodFailed;
    private boolean runIfPreviousMethodSkipped;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isRunIfPreviousMethodFailed() {
        return runIfPreviousMethodFailed;
    }

    public void setRunIfPreviousMethodFailed(boolean runIfPreviousMethodFailed) {
        this.runIfPreviousMethodFailed = runIfPreviousMethodFailed;
    }

    public boolean isRunIfPreviousMethodSkipped() {
        return runIfPreviousMethodSkipped;
    }

    public void setRunIfPreviousMethodSkipped(boolean runIfPreviousMethodSkipped) {
        this.runIfPreviousMethodSkipped = runIfPreviousMethodSkipped;
    }
}
