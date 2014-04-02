package org.protesting.atfwk.selenium2;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

/**
 * Enumeration for the Selenium Web Drivers.
 * User: ab83625
 * Date: 03/12/13
 * To change this template use File | Settings | File Templates.
 */
public enum BrowserDriver {

    IE(0),
    FIREFOX(1),
    CHROME(2);

    private int index;

    BrowserDriver(int browserIndex) {
        this.index = browserIndex;
    }

    /**
     * Get webDriver
     * @return RemoteWebDriver
     */
    public RemoteWebDriver getDriver() {
        switch(index) {
            case 1: {
                return new FirefoxDriver(new FirefoxProfile());
            }
            case 2: {
                return new ChromeDriver();
            }
                default: {
                return new InternetExplorerDriver(InternetExplorerDriverService.createDefaultService(), DesiredCapabilities.internetExplorer());
            }
        }
    }

}
