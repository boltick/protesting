package org.protesting.atfwk;

import java.net.URL;

/**
 * ATFwkWebBrowser interface
 * Author: Alexey Bulat
 * Date: 08.05.2009
 */
public interface ATFwkWebBrowser {

    void start();

    void close();

    void openURL(String s);

    URL getCurrentURL();

    String getTitle();

    String getUrl();

    public void maximize();

    public void bringToFront();

    public void bringToFront(int windowIndex);

    public void getScreenCapture(String s);

    public boolean exists();

    public void goBack();

    public String getRowText();

    public void refresh();

    public ATFwkWebBrowser getChildBrowser();

    public ATFwkWebBrowser getChildBrowser(int i);

    public String getAlertText();

    public void confirmAlert();

    public void closeAlert();

    public boolean saveToFile(String location, String path);

    public void waitForPageToLoad();

    public void waitForAjax();

    public void waitForElementBy(ATFwkFinder finder, String locator);

    public void waitForAlert();

    public void waitFor(long seconds);

    public ATFwkAction call();

    public ATFwkVerifier verify();

    public void setWaitingTimeout(long timeout);

    void quit();

}
