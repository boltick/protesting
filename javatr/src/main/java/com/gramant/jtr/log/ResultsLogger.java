/*
 * ResultsLogger
 *
 * Copyright (c) 2007 Gramant. All Rights Reserved
 */
package com.gramant.jtr.log;

import com.gramant.jtr.ResultsWriter;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Java test runner log4j Logger extension.
 *
 * @version (VCS$Id:$)
 */
public class ResultsLogger extends Logger {

    private static ResultsLoggerFactory loggerFactory = new ResultsLoggerFactory();
    static String FQCN = ResultsLogger.class.getName() + ".";

    static List <ScreenShot> screenShotList = new ArrayList<ScreenShot>();

    protected ResultsLogger(String name) {
        super(name);
    }


    public static Logger getLogger(String name) {
        return Logger.getLogger(name, loggerFactory);
    }


    public static Logger getLogger(Class clazz) {
        return Logger.getLogger(clazz.getName(), loggerFactory);
    }


    public void action(String action) {
        loggerFactory.action(action);
        super.log(FQCN, Level.INFO, action, null);
    }


    public void result(String result) {
        loggerFactory.result(result);
        super.log(FQCN, Level.INFO, result, null);
    }

    public void resultPassed() {
        loggerFactory.result("PASSED");
        super.log(FQCN, Level.INFO, "PASSED", null);
    }

    public void assertFail(String result) {
//        errorResult(result, captureScreen("FAIL"));
        throw new AssertionError(result);
    }

    public void assertFail(Exception e) {
//        errorResult(e.getLocalizedMessage(), captureScreen("FAIL"));
        throw new AssertionError(e.getLocalizedMessage());
    }

    public void assertFail(String result, Exception e) {
//        errorResult(result, captureScreen("FAIL"), e);
        throw new AssertionError(result);
    }

    public void assertFail() {
//        errorResult("FAILED");
        throw new AssertionError("FAILED");
    }


    /**
     * Add all strings in array to result of current action
     *
     * @param results - array with Strings
     */
    public void result(String[] results) {
        for (String result : results) {
            result(result);
        }
    }


    /**
     * Add all strings in list to result of current action
     *
     * @param results - list with Strings
     */
    public void result(List results) {
        for (Object result : results) {
            result((String) result);
        }
    }


    public void errorResult(String errorResult) {
        errorResult(errorResult, captureScreen("ERROR"));
    }


    public void errorResult(String errorResult, Exception e) {
        e.printStackTrace();
        errorResult(errorResult);
    }


    private void errorResultMessage(String errorResult) {
        loggerFactory.errorResult(errorResult);
        super.log(FQCN, Level.ERROR, errorResult, null);
    }


    public void errorResult(String errorResult, ScreenShot screenShot) {
        errorResultMessage(errorResult);
        errorResultScreenshot(screenShot);
    }

    
    private void errorResultScreenshot(ScreenShot screenShot) {
        loggerFactory.addScreenShot(screenShot);
        super.log(FQCN, Level.ERROR, screenShot.getPath(), null);
    }

    
    
    private void errorResult(String errorResult, ScreenShot screenShot, Exception e) {
        e.printStackTrace();
        errorResult(errorResult + ": " + e.getLocalizedMessage(), screenShot);
    }

    /**
     * Add all strings in array to error-result of current action
     *
     * @param errorResults - array with Strings
     */
    public void errorResult(String[] errorResults) {
        for (String errorResult : errorResults) {
            errorResult(errorResult);
        }
    }


    /**
     * Add all strings in list to error-result of current action
     *
     * @param errorResults - list with Strings
     */
    public void errorResult(List errorResults) {
        for (Object errorResult : errorResults) {
            errorResult((String) errorResult);
        }
    }


    public void clearStepList() {
        loggerFactory.clearStepList();
    }


    public List popStepList() {
        return loggerFactory.popStepList();
    }

    /**
     * Capture the screen-shot
     *
     * @param suffix screen-shot name
     * @return ScreenShot object
     */
    private ScreenShot captureScreen(String suffix) {
        File screenShotFile = new File(ResultsWriter.getInstance().getResultsFolder() + File.separatorChar + new Date().getTime()+"_" + suffix + ".png");
        try {
            BufferedImage image = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
            ImageIO.write(image, "png", screenShotFile);
        } catch (AWTException e) {
            e.printStackTrace();
            super.log(FQCN, Level.ERROR, "Error getting the screen-shot", e);
        } catch (IOException e) {
            e.printStackTrace();
            super.log(FQCN, Level.ERROR, "Error getting the screen-shot", e);
        }
        ScreenShot screenShot = new ScreenShot(suffix + " Screen-Shot", screenShotFile.getName());
        screenShotList.add(screenShot);
        return screenShot;
    }

    public static List<ScreenShot> getScreenShotList() {
        return screenShotList;
    }
}
