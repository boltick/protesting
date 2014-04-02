/*
 * ResultsLoggerFactory
 *
 * Copyright (c) 2007 Gramant. All Rights Reserved
 */
package com.gramant.jtr.log;

import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Java test runner implemntation of the LoggerFactory
 *
 * @version (VCS$Id:$)
 */
public class ResultsLoggerFactory implements LoggerFactory {
    private List<Step> stepList;


    public ResultsLoggerFactory() {
        stepList = new ArrayList<Step>();
    }


    public Logger makeNewLoggerInstance(String name) {
        return new ResultsLogger(name);
    }


    public void action(String action) {
        Step step = new Step(action);
        stepList.add(step);
    }


    public void result(String result) {
        if ((stepList != null) && (stepList.size() != 0)) {
            Step lastStep = stepList.get(stepList.size() - 1);
            lastStep.setResult(result);
        } 
    }


    public void errorResult(String errorResult) {
        if ((stepList != null) && (stepList.size() != 0)) {
            Step lastStep = stepList.get(stepList.size() - 1);
            lastStep.setErrorResult(errorResult);
        }
    }

    public void addScreenShot(ScreenShot screenShot) {
        if ((stepList != null) && (stepList.size() != 0)) {
            Step lastStep = stepList.get(stepList.size() - 1);
            lastStep.setScreenShot(screenShot);
        }
    }


    public void clearStepList() {
        stepList.clear();
    }


    public List<Step> popStepList() {
        List<Step> tempList = new ArrayList<Step>(stepList);
        clearStepList();
        return tempList;
    }
}
