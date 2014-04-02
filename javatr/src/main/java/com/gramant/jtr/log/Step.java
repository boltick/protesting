/*
 * Step
 *
 * Copyright (c) 2007 Gramant. All Rights Reserved
 */
package com.gramant.jtr.log;

import java.util.List;
import java.util.ArrayList;

/**
 * Step object
 *
 * @version (VCS$Id:$)
 */
public class Step {
    private String action;
    private List <String> result;
    private List <String> errorResult;
    private ScreenShot screenShot;

    public Step(String action) {
        this.action = action;
        this.result = new ArrayList<String>();
        this.errorResult = new ArrayList<String>();
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public List getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result.add(result);
    }

    public List getErrorResult() {
        return errorResult;
    }

    public void setErrorResult(String errorResult) {
        this.errorResult.add(errorResult);
    }

    public void setScreenShot(ScreenShot screenShot) {
        this.screenShot = screenShot;
    }

    public ScreenShot getScreenShot() {
        return screenShot;
    }
}
