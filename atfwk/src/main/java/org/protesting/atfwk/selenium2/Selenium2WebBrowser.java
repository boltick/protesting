package org.protesting.atfwk.selenium2;

import org.apache.log4j.Logger;
import org.protesting.atfwk.*;
import org.protesting.atfwk.trust.XTrustProvider;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.openqa.selenium.support.ui.*;
import org.protesting.atfwk.win32.WinUtils;

import javax.net.ssl.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * ATFwk implementation of the Selenium WebDriver browserDriver.
 * Tested with IE and Chrome
 *
 * Author: Alexey Bulat
 * Date: 08/02/13
 */
public class Selenium2WebBrowser implements ATFwkWebBrowser {

    public static final Logger LOG = Logger.getLogger(Selenium2WebBrowser.class);

    private long waitingTimeout;
    private WebDriver webDriver;
    private WebDriverWait waiting;
    private String host;
    private int port;
    private ATFwkAction action;
    private ATFwkVerifier verifier;

    private BrowserDriver browserDriver;
    private String currentWindowHandler;
    private String parentWindowHandler;


    public Selenium2WebBrowser(BrowserDriver browserDriver, String host) {
        this(browserDriver, host, "");
    }

    private Selenium2WebBrowser(BrowserDriver browserDriver, String host, String browserProfilePath) {
        this(browserDriver, host, 5555, browserProfilePath);
    }

    private Selenium2WebBrowser(BrowserDriver browserDriverType, String host, int port, String browserProfilePath) {
        this.host = host;
        this.port = port;
        this.browserDriver = browserDriverType;
        this.waitingTimeout = 25;
        this.action = new Action();
        this.verifier = new Verifier();
    }

    /**
     * private constructor for popup windows
     * @param browserDriver type of browser driver
     * @param webDriver current webDriver
     */
    private Selenium2WebBrowser(BrowserDriver browserDriver, WebDriver webDriver) {
        this(browserDriver, "");
        this.parentWindowHandler = webDriver.getWindowHandle();
        this.currentWindowHandler = getLastWindowHandler(webDriver);
        this.webDriver = webDriver.switchTo().window(currentWindowHandler);
        this.waiting = new WebDriverWait(this.webDriver, waitingTimeout, 2000);
        this.host = webDriver.getCurrentUrl();
    }


    /**
     * Initialize and start the Internet Explorer driver
     */
    public void start() {
        LOG.info("Start Selenium browserDriver");
        webDriver = browserDriver.getDriver();
        this.currentWindowHandler = this.webDriver.getWindowHandles().toArray()[0].toString();
        this.waiting = new WebDriverWait(this.webDriver, waitingTimeout, 1000);
    }

    /**
     * Closing current window
     */
    public void close() {
        try {
            bringToFront();
            if(webDriver== null || ((RemoteWebDriver) webDriver ).getSessionId() == null)  {
                LOG.info("BrowserDriver is already closed");
                return;
            }
            LOG.info("Close Selenium/WebDriver browserDriver: " + webDriver.getTitle());

            switch (webDriver.getWindowHandles().size())  {
                case 1:
                    webDriver.close();
                    break;
                default:
                    if(currentWindowHandler!=null) {
                        webDriver.switchTo().window(currentWindowHandler);
                        LOG.info("Selenium/WebDriver browserDriver {"+currentWindowHandler+"} is active. Title = " + webDriver.getTitle());
                        webDriver.close();
                        if(parentWindowHandler != null) {
                            LOG.info("Switch to parent browserDriver {" + parentWindowHandler+"}");
                            webDriver.switchTo().window(parentWindowHandler);
                            currentWindowHandler = parentWindowHandler;
                            parentWindowHandler = null;
                        }
                    } else {
                        webDriver.close();
                    }
            }
            LOG.debug("Closed");

        } catch (UnreachableBrowserException ube) {
            LOG.error("BrowserDriver was unexpectedly closed", ube);
        }
    }

    /**
     * Opens the provided URL
     * @param url address to be opened
     */
    public void openURL(String url) {
        LOG.info("Open page with URL=" + host + url);
        String newUrl = host;
        if(url.startsWith(host)) {
            newUrl = url;
        } else {
            newUrl += url;
        }
        webDriver.get(newUrl);
        LOG.debug("Page with URL=" + host + url + " is opened");
    }

    /**
     * Getting the current URL
     * @return current URL
     */
    public URL getCurrentURL() {
        try {
            return new URL(webDriver.getCurrentUrl());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        throw new ActionExecutionException("Error getting URL.");
    }

    private String getLastWindowHandler() {
        Set<String> windows = webDriver.getWindowHandles();
        int windowsNumber = windows.size();
        return windows.toArray()[windowsNumber-1].toString();
    }

    private String getLastWindowHandler(WebDriver webDriver) {
        Set<String> windows = webDriver.getWindowHandles();
        int windowCount = windows.size();
        windows.remove(parentWindowHandler);
        LOG.debug("Number of opened windows (excluding parent) = " + windowCount);
        return windows.toArray()[windows.size() - 1].toString();
    }

    private String getCurrentWindowHandler() {
        return currentWindowHandler;
    }

    /**
     * Getting the parent webdriver window handler
     * @return parent window handler as a String
     */
    private String getParentWindowHandler() {
        return parentWindowHandler;
    }

    /**
     * Get Window title
     * @return window title as String
     */
    public String getTitle() {
        return webDriver.getTitle();
    }

    /**
     * Getting the current  URL
     * @return current URL as String
     */
    public String getUrl() {
        return webDriver.getCurrentUrl();
    }

    private Alert getAlert() {
        waitForAlert();
        return webDriver.switchTo().alert();
    }

    /**
     * Method gets Text message from the alert window
     * @return text message as a String
     */
    public String getAlertText() {
        waiting.until(ExpectedConditions.alertIsPresent());
        String text = getAlert().getText();
        LOG.debug("Alert {"+text+"} is opened");
        return text;
    }

    /**
     * Method closes alert message by confirming it
     */
    public void confirmAlert() {
        LOG.info("Accept alert");
        getAlert().accept();
        waitFor(3);
        LOG.debug("Alert is accepted");
    }

    /**
     * Method closes alert message
     */
    public void closeAlert() {
        LOG.info("Close alert");
        getAlert().dismiss();
        waitFor(3);
        LOG.debug("Alert is closed");
    }

    /**
     * Method is calling to wait for page to load. It simply waits until the document.readyState = complete
     * Works only in IE browserDriver
     */
    public void waitForPageToLoad() {
        LOG.info("Wait for page to load");
        boolean isLoaded = false;
        int counter = 1;
        while (!isLoaded) {
            LOG.info("wait attempt " + counter);
            try {
                isLoaded = ((JavascriptExecutor)webDriver).executeScript("return document.readyState").equals("complete");
            } catch (WebDriverException e) {
                isLoaded = false;
                LOG.warn(e);
            }
            waitFor(1);
            LOG.info("Page loading is not complete...");
            counter++;
            if (counter >= getWaitingTimeout()) {
                LOG.fatal("Timeout... Page is not loaded after " + getWaitingTimeout() + " " + TimeUnit.SECONDS.name());
                throw new ObjectNotFoundException("Timeout... Page is not loaded");
            }
        }
        LOG.debug("Done");
    }

    /**
     * Method is waiting for visibility of the specified element
     * @param finder   finder object of the element
     * @param locator   locator of teh element
     */
    public void waitForElementBy(ATFwkFinder finder, String locator) {
        try {
            WebElement element = null;
            int counter = 1;
            while (element==null) {
                LOG.info("wait attempt " + counter);
                element = waiting.until(ExpectedConditions.visibilityOfElementLocated(getBy(finder, locator)));
                counter++;
                if(counter > getWaitingTimeout()) {
                    LOG.warn("Element {"+locator+"} is not found after +" + getWaitingTimeout() + " sec");
                    return;
                }
            }
        } catch (WebDriverException e) {
            LOG.warn("Hmm... Wait for element results in error!", e);
        }
    }

    /**
     * Method waits for Alert appearance
     */
    public void waitForAlert() {
        waiting.until(ExpectedConditions.alertIsPresent());
    }


    public void refresh() {
        webDriver.navigate().refresh();
    }

    /**
     * Brings the current window to front
     */
    public void bringToFront() {
        if(webDriver==null) {
            throw new ActionExecutionException("Webdriver is null");
        }
        if(!webDriver.getWindowHandle().equals(getCurrentWindowHandler())) {
            webDriver.switchTo().window(getCurrentWindowHandler());
        }
        LOG.debug("Window "+webDriver.getWindowHandle()+" is active");
    }

    /**
     * Brings the specified window to front
     * @param windowIndex the index of the window
     */
    public void bringToFront(int windowIndex) {
        if(!webDriver.getWindowHandle().equals(webDriver.getWindowHandles().toArray()[windowIndex].toString())) {
            webDriver.switchTo().window(webDriver.getWindowHandles().toArray()[windowIndex].toString());
            LOG.debug("Active window is switched to {"+webDriver.getWindowHandle()+"} title=" + webDriver.getTitle());
        } else {
            LOG.debug("Window {"+webDriver.getWindowHandle()+"} is active. Title="+ webDriver.getTitle());
        }

    }

    /**
     * Get the child window
     * @return  the ATFwkWebBrowser object of the child window
     */
    public ATFwkWebBrowser getChildBrowser() {
        LOG.info("Create a child browserDriver object");
        return new Selenium2WebBrowser(getBrowserDriver(), webDriver);
    }
    /**
     * Get the specified child window
     * @param index index of the window
     * @return  the ATFwkWebBrowser object of the specified child window
     *
     */
    public ATFwkWebBrowser getChildBrowser(int index) {
        throw new NotImplementedException();
    }

    /**
     * Verify that window exists
     * @return true if the current window exists in the set of webDriver.getWindowHandles()
     */
    public boolean exists() {
        return webDriver.getWindowHandles().contains(currentWindowHandler);
//        return webDriver.switchTo().window(currentWindowHandler).getTitle() != null;
    }

    /**
     * Capture the screen
     * @param name name of the screen-shot
     */
    public void getScreenCapture(String name) {
        ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
    }

    /**
     * Get browser driver as an Object
     * @return the BrowserDriver Object
     */
    public Object getBrowserObject() {
        return webDriver;
    }

    public boolean print() {
        throw new NotImplementedException("Print method in not implemented");
    }

    /**
     * Getting the page source
     * @return the page source as a String
     */
    public String getPageSource() {
        return webDriver.getPageSource();
    }

    private int getElementsCountBy(ATFwkFinder finder, String locator) {
        List<WebElement> webElementList = getElementsListBy(finder, locator);

        return webElementList != null ? webElementList.size() : 0;
    }


    public void waitForAjax() {
        throw new NotImplementedException("Method is not implemented yet");
//        ((JavascriptExecutor) webDriver).executeScript("return jQuery.active == 0;");
    }

    /**
     * Wait for a defined number of seconds
     * @param seconds timeout duration in seconds
     */
    public void waitFor(long seconds) {
        WinUtils.delay(seconds * 1000);
//        waiting.withTimeout(seconds, TimeUnit.SECONDS);
//        webDriver.manage().timeouts().implicitlyWait(seconds, TimeUnit.SECONDS);
    }

    /**
     * Downloading file from the web (it works only with http)
     * todo: https implementation
     * @param location location of the file
     * @param path path to the file
     * @return true if file is saved
     */
    public boolean saveToFile(String location, String path) {
        LOG.debug("Download file {" + location + "} into the {" + path + "}");

        System.getProperties().setProperty("javax.security.auth.useSubjectCredsOnly", "false");
        System.getProperties().setProperty("java.security.auth.login.config", "krb-client.login.config");
        System.setProperty("sun.security.ssl.allowUnsafeRenegotiation", "true");
        String cookies = webDriver.manage().getCookies().toArray()[0].toString();

        URL url;
        try {

            XTrustProvider.install();

            HostnameVerifier hv = new HostnameVerifier() {
                public boolean verify(String urlHostName, SSLSession session) {
                    LOG.warn("Warning: URL Host: " + urlHostName
                            + " vs. " + session.getPeerHost());
                    return true;
                }
            };
            XTrustProvider.trustAllHttpsCertificates();
            HttpsURLConnection.setDefaultHostnameVerifier(hv);

            url = new URL(location);
            URLConnection conn = url.openConnection();
            HttpsURLConnection httpConn = (HttpsURLConnection) conn;
            httpConn.setAllowUserInteraction(false);
            httpConn.setConnectTimeout(1000);
            httpConn.setReadTimeout(1000);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("GET");
            httpConn.setRequestProperty("Cookie", cookies);
            httpConn.connect();
            int response = httpConn.getResponseCode();
            InputStream in = httpConn.getInputStream();
            if (response == HttpURLConnection.HTTP_OK) {
                in = httpConn.getInputStream();
            }
            FileOutputStream myFileOutputStream = new FileOutputStream(path);
            byte buf[]=new byte[1024];
            int len;
            while((len=in.read(buf)) > 0)
                myFileOutputStream.write(buf,0,len);
            myFileOutputStream.close();
        }  catch (Exception e) {
            LOG.warn("Error while downloading file",e);
            return false;
        }
        LOG.info("File is downloaded");
        return true;

    }


    private void waitForElementBy(ATFwkFinder finder, String locator, long timeout) {
        int counter = 1;
        while (counter >= timeout) {
            LOG.info("wait attempt " + counter);
            if (getElementsCountBy(finder, locator) > 0) {
                return;
            }
            if (counter >= timeout) {
                LOG.fatal("Timeout... Element "+ finder.getFinderName()+"="+locator+" not found");
                throw new ObjectNotFoundException("Timeout... Element is not found");
            }
            waitFor(1);
            counter++;
        }
        throw new NotImplementedException();
    }


    /**
     * Move mous over the specified by XPATH element
     * @param finder finder for the element
     * @param locator locator of the element
     */
    public void moveMouseOver(ATFwkFinder finder, String locator) {
        WebElement element = getElementBy(finder, locator);
        (new Actions(webDriver)).moveToElement(element);
    }

    /**
     * Maximize window
     */
    public void maximize() {
        webDriver.manage().window().maximize();
    }

    /**
     * Open previously loaded page.
     */
    public void goBack() {
        webDriver.navigate().back();
    }

    protected BrowserDriver getBrowserDriver() {
        return browserDriver;
    }

    /**
     * Get page source as row text
     * @return page source as a String
     */
    public String getRowText() {
        return webDriver.findElement(By.tagName("html")).getText();
    }


    /**
     * Fire javascript event on the specified element
     * @param finder finder for the element
     * @param locator element's locator
     * @param event javascript event
     */
    public void fireJSEvent(ATFwkFinder finder, String locator, String event) {
        WebElement webElement = getElementBy(finder, locator);
        ((JavascriptExecutor) webElement).executeScript(event);
    }

    private long getWaitingTimeout() {
        return waitingTimeout;
    }

    public int getPort() {
        return port;
    }

    private String getLocatorForImageLoading(String where, String what) {
        return where + "='" + what.replaceAll("'", "\\\\'") + "'";
    }

    private List<WebElement> getElementsListBy(ATFwkFinder finder, String value) {
        List<WebElement> elementList = webDriver.findElements(getBy(finder, value));
        if(elementList != null && elementList.size() != 0) {
            return elementList;
        }
        return new ArrayList<WebElement>();
    }

    private By getBy(ATFwkFinder finder, String value) {
        switch(finder) {
            case ID: {
                return By.id(value);
            }
            case NAME: {
                return By.name(value);
            }
            case CLASS: {
                return By.className(value);
            }
            case TAG: {
                return By.tagName(value);
            }
            case CSS: {
                return By.cssSelector(value);
            }
            case XPATH: {
                return By.xpath(value);
            }
            case LINK_TEXT: {
                return By.linkText(value);
            }
            case PARTIAL_LINK_TEXT: {
                return By.partialLinkText(value);
            }
            default: {
                return By.id(value);
            }
        }
    }


    WebElement getElementBy(ATFwkFinder finder, String value) {
        List<WebElement> webElements = getElementsListBy(finder, value);
        if(webElements.size()==0) {
            return null;
        }
        return getElementsListBy(finder, value).get(0);
    }


    public void setWaitingTimeout(long timeout) {
        waitingTimeout = timeout;
        waiting.withTimeout(timeout, TimeUnit.SECONDS);
    }

    /**
     * Shut down the webdriver browserDriver
     */
    public void quit() {
        try {
            webDriver.quit();
        } catch (UnreachableBrowserException ube) {
            LOG.error("BrowserDriver was unexpectedly closed", ube);
        } finally {
            webDriver = null;
        }
    }


    private void highlight(WebElement element) {
        for (int i = 0; i < 2; i++) {
            JavascriptExecutor js = (JavascriptExecutor) webDriver;
            js.executeScript("arguments[0].setAttribute('style', arguments[1]);",
                    element, "color: yellow; border: 2px solid yellow;");
            js.executeScript("arguments[0].setAttribute('style', arguments[1]);",
                    element, "color: transparent; border: 2px solid transparent;");
        }
    }

    private boolean isChecked(WebElement checkbox) {
        String checkedValue = checkbox.getAttribute("checked");
        return checkedValue != null;
    }

    /**
     * Get ATFwkAction object to call basic events on the page
     * @return ATFwkAction object
     */
    public ATFwkAction call() {
        return this.action;
    }

    /**
     * Get ATFwkVerifier object to call basic verifications
     * @return ATFwkVerifier
     */
    public ATFwkVerifier verify() {
        return this.verifier;
    }

    /**
     * Inner Action class implementation
     */
    class Action implements ATFwkAction {

        public void attachFile(ATFwkFinder finder, String locator, String path) {
            LOG.info("Attach file {"+path+"} into {"+locator+"}");
            WebElement element = getElementBy(finder, locator);
            element.sendKeys(path);
            LOG.debug("File is attached");
        }



        public boolean clickBy(ATFwkFinder finder, String locator) {
            LOG.info("Click the {"+locator+"}");
            WebElement element = getElementBy(finder, locator);
            boolean isOk = click(element);
            if(isOk) {
                LOG.debug("Click is OK");
                return true;
            }
            LOG.debug("Element {"+locator+"} is not found");
            return false;
        }


        private boolean click (WebElement element) {
            if (element!=null) {
                highlight(element);
                new Actions(webDriver).moveToElement(element).click().build().perform(); // set focus
                return true;
            }
            return false;
        }


        public boolean pushBy(ATFwkFinder finder, String locator) {
            return clickBy(finder, locator);
        }


        public boolean setBy(ATFwkFinder finder, String locator, int index) {
            LOG.info("Set radio button {"+locator+"} index " + index);
            List<WebElement> radioGroup = getElementsListBy(finder, locator);
            try {
                click(radioGroup.get(index));
                LOG.debug("Radio button is set = " +  radioGroup.get(index).getAttribute("checked"));
            } catch (Exception e) {
                LOG.error(e);
                return false;
            }
            return true;
        }


        public boolean setBy(ATFwkFinder finder, String locator, String value) {
            LOG.info("Set radio button {"+locator+"} = " + value);
            List<WebElement> radioGroup = getElementsListBy(finder, locator);
            try {
                for (WebElement next : radioGroup) {
                    if(next.getAttribute("value").equalsIgnoreCase(value)) {
                        click(next);
                        LOG.debug("Radio button is set.");
                        return true;
                    }
                }
            } catch (Exception e) {
                LOG.error(e);
                return false;
            }
            return false;
        }


        public boolean checkBy(ATFwkFinder finder, String locator) {
            LOG.info("Tick the checkbox {"+locator+"}");
            WebElement checkbox = getElementsListBy(finder, locator).get(0);
            if(checkbox!=null) {
                if(!isChecked(checkbox)) {
                    click(checkbox);
                    LOG.debug("Tick is " + checkbox.getAttribute("checked"));
                    return true;
                }
            }
            return false;
        }


        public boolean uncheckBy(ATFwkFinder finder, String locator) {
            WebElement checkbox = getElementsListBy(finder, locator).get(0);
            if(checkbox!=null) {
                if(isChecked(checkbox)) {
                    click(checkbox);
                    return true;
                }
            }
            return false;
        }


        public boolean selectBy(ATFwkFinder finder, String locator, int index) {
            LOG.info("Select item {"+index+"} in combobox {"+locator+"}");
            if(index>=0) {
                Select select = getSelectElement(finder, locator);
                if (select!=null) {
                    select.selectByIndex(index);
                    LOG.debug("Item is selected");
                    return true;
                }
            }
            LOG.debug("Item {"+index+"} is not selected");
            return false;
        }


        public boolean selectBy(ATFwkFinder finder, String locator, String[] optionTextArray) {
            LOG.info("Select options in combobox {"+locator+"}");
            Select select = getSelectElement(finder, locator);
            if (select != null) {
                select.deselectAll();
                for (String option : optionTextArray) {
                    select.selectByVisibleText(option);
                    LOG.debug("Item {" + option + "} is selected");
                }
                return true;
            }
            return false;
        }


        public Map<String, String> getOptionsMap(ATFwkFinder finder, String selectLocator) {
            Select select = getSelectElement(finder, selectLocator);
            List<WebElement> options = select.getOptions();
            HashMap<String, String > optionsMap = new HashMap<String, String>();
            for (WebElement option : options) {
                optionsMap.put(option.getText(), option.getAttribute("value"));
            }
            return optionsMap;
        }


        public boolean selectBy(ATFwkFinder finder, String locator, String optionText) {
            LOG.info("Select option {"+ optionText +"} in combobox {"+locator+"}");
            Select select = getSelectElement(finder, locator);
            if (select!=null) {
                String firstSelected = select.getFirstSelectedOption().getText();
                if(firstSelected.equals(optionText)) {
                    LOG.debug("Option is already selected");
                    return true;
                } else {
                    try {
                        select.selectByVisibleText(optionText);
                        LOG.debug("Option is selected");
                        return true;
                    } catch(NoSuchElementException nsee) {
                        LOG.warn("Option does not exist");
                    }
                }
            }
            LOG.debug("Cannot get combobox");
            return false;
        }


        public String getSelectedValueBy(ATFwkFinder finder, String locator) {
            LOG.info("Get selected value from {" + locator + "}");
            Select select = getSelectElement(finder, locator);
            if(select!=null) {
                String value = select.getFirstSelectedOption().getText();
                LOG.debug("Retrieved value is {" + value + "}");
                return value;
            }
            LOG.debug("There are no values selected");
            return "";
        }


        public List<String> getSelectedValuesBy(ATFwkFinder finder, String locator) {
            LOG.info("Get selected value from {" + locator + "}");
            Select select = getSelectElement(finder, locator);
            List<String> selectedOptionsStr = new ArrayList<String>();
            if(select!=null) {
                List<WebElement> selectedOptions = select.getAllSelectedOptions();
                if(selectedOptions.size() != 0) {
                    LOG.debug("Value is retrieved");
                    for (WebElement element : selectedOptions) {
                        selectedOptionsStr.add(element.getText());
                    }
                }
            }
            return selectedOptionsStr;
        }

        private Select getSelectElement(ATFwkFinder finder, String locator) {
            WebElement selectElement = getElementBy(finder, locator);
            if(selectElement!=null && selectElement.isEnabled()) {
                return new Select(selectElement);
            }
            return null;
        }

        public boolean typeTextBy(ATFwkFinder finder, String locator, String value) {
            LOG.info("Type text {"+value+"} into {"+locator+"}");
            WebElement input = getElementBy(finder, locator);
            if(!input.isEnabled()) {
                LOG.debug("Input {"+locator+"} is disabled");
                return false;
            }
            input.clear();
            if(value!=null && value.length()!=0) {
                input.sendKeys(value);
                LOG.debug("Text {"+value+"} is typed");
            } else {
                LOG.debug("Nothing is typed because value is {"+value+"}");
            }
            return true;
        }


        public String getTextBy(ATFwkFinder finder, String locator) {
            LOG.info("Get text from element {" + locator + "}");
            List<WebElement> elementsListBy = getElementsListBy(finder, locator);
            if(elementsListBy!=null) {
                StringBuilder text = new StringBuilder();
                for (WebElement element : elementsListBy) {
                    text.append(element.getText()).append("\r\n");
                }
                LOG.debug("Text {" + text + "} is found");
                return text.toString().trim();
            }
            LOG.debug("Element is not found");
            return "";

        }


        public String getValueBy(ATFwkFinder finder, String locator) {
            LOG.info("Get value from {" + locator + "}");
            WebElement element = getElementBy(finder, locator);
            String value = "";
            if(element.isEnabled()) {
                value = element.getAttribute("value");
                LOG.debug("Value {" + value + "} is retrieved");
                return value;
            }
            LOG.debug("Element is no enabled");
            return value;
        }

        public String getSelectedRadioButtonValueBy(ATFwkFinder finder, String locator) {
            LOG.info("Get value from radio {" + locator + "}");
            List<WebElement> setElementList = getElementsListBy(finder, locator);
            String value = "";
            for (WebElement radioButton : setElementList) {
                if (radioButton.isDisplayed() && radioButton.isEnabled() && radioButton.isSelected()) {
                    value = radioButton.getAttribute("value");
                    break;
                }
            }
            LOG.debug("Value {" + value + "} is retrieved");
            return value;
        }


        public List<String> getSelectOptionsListBy(ATFwkFinder finder, String locator) {
            LOG.info("Get all options from combobox {" + locator + "}");
            Select select = getSelectElement(finder, locator);
            List<String> optionsStr = new ArrayList<String>();
            if(select!=null) {
                List<WebElement> options = select.getOptions();
                for (WebElement element : options) {
                    optionsStr.add(element.getText());
                }
            }
            LOG.debug("Retrieved options are: " + optionsStr);
            return optionsStr;
        }



        public int getRadioGroupSizeBy(ATFwkFinder finder, String locator) {
            return getNumberOfElements(finder, locator);
        }


        public int getNumberOfElements(ATFwkFinder finder, String locator) {
            return getElementsListBy(finder, locator).size();
        }


        public ATFwkTable getTableBy(ATFwkFinder finder, String locator, int[] keyDataArr) {
            LOG.info("Get data from the table {" + locator + "}");
            WebElement table = getElementBy(finder, locator);
            return table.isDisplayed() ? new ATFwkTable(getTableHeader(table, keyDataArr), getTableData(table, keyDataArr)) : null;
        }


        public String getTableFieldBy(ATFwkFinder finder, String tableLocator, int rowIndex, int columnIndex) {
            LOG.info("Get table field ["+rowIndex+","+columnIndex+"]");
            WebElement row = getTableRow(finder, tableLocator, rowIndex);
            List<WebElement> fields = row.findElements(By.cssSelector("td"));
            if(fields.size()!=0 && fields.size() > columnIndex) {
                String text = fields.get(columnIndex).getText();
                if(0 == text.length() && fields.get(columnIndex).getTagName().equals("img")) {
                    text = fields.get(columnIndex).getAttribute("alt");
                }
                LOG.debug("Received value is {"+text+"}");
                return text;
            }
            throw new ParsePageException("Field ["+rowIndex+","+columnIndex+"] is not retrieved");
        }

        public int getTableRowsCount(ATFwkFinder finder, String tableLocator) {
            WebElement table = getElementBy(finder, tableLocator);
            return table.findElements(By.cssSelector("tbody tr")).size();
        }

        public ATFwkTable getTableBy(ATFwkFinder finder, String locator) {
            return getTableBy(finder, locator, null);
        }

        private WebElement getTableRow(ATFwkFinder finder, String tableLocator, int rowIndex) {
            LOG.info("Get table row");
            WebElement table = getElementBy(finder, tableLocator);
            return table!=null ? table.findElements(By.cssSelector("tbody tr")).get(rowIndex) : null;
        }


        public ATFwkTableRow getTableRowBy(ATFwkFinder finder, String tableLocator, int rowIndex) {
            LOG.info("Get table row");
            List<String> rowData = new ArrayList<String>();
            WebElement table = getElementBy(finder, tableLocator);

            WebElement row = table!=null ? table.findElements(By.cssSelector("tbody tr")).get(rowIndex) : null;

            if (row!=null) {
                List<WebElement> cols = row.findElements(By.cssSelector("td"));
                for (WebElement field : cols) {
                    String text = field.getText();
                    if (0 == text.length() && field.getTagName().equals("img")) {
                        text = field.getAttribute("alt");
                    }
                    rowData.add(text);
                }
            }
            return new ATFwkTableRow(rowData);
        }


        public List<ATFwkTableRow> getTableColumnBy(ATFwkFinder finder, String tableLocator, int columnIndex) {
            return getTableData(finder, tableLocator, new int []{columnIndex});
        }

        private List<ATFwkTableRow> getTableData(WebElement table, int [] keyHeaderArr) {
            List<WebElement> tr = table.findElements(By.cssSelector("tbody tr"));
            List<ATFwkTableRow> tableRows = new ArrayList<ATFwkTableRow>();
            for (WebElement row : tr) {
                LOG.info("Parse tr");
                List<WebElement> td = row.findElements(By.cssSelector("td"));
                List<String> rowDataList = new ArrayList<String>();
                byte id = 0;
                // todo refactoring!!!!
                for (WebElement next : td) {
                    if(keyHeaderArr==null || Arrays.binarySearch(keyHeaderArr, id) >= 0) {
                        LOG.info("Get td of the table");
                        String text = next.getText();
                        LOG.debug("td text = {"+text+"}");
                        if(0 == text.length() && next.getTagName().equals("img")) {
                            rowDataList.add(next.getAttribute("alt"));
                            continue;
                        }
                        rowDataList.add(text);
                    } else {
                        rowDataList.add("--");
                    }
                    id++;
                }
                ATFwkTableRow data = new ATFwkTableRow(rowDataList);
                if(!data.isEmpty()) {
                    tableRows.add(data);
                }
                LOG.debug("tr is parsed");
            }
            LOG.debug("Table data is received");
            return tableRows;
        }

        private List<ATFwkTableRow> getTableData(ATFwkFinder finder, String locator, int [] keyHeaderArr) {
            LOG.info("Get key table data");
            WebElement table = getElementBy(finder, locator);
            return getTableData(table, keyHeaderArr);

        }

        private ATFwkTableHeader getTableHeader(WebElement table, int [] keyHeaderArr) {
            List<WebElement> headerDataList = table.findElements(By.cssSelector("thead th"));
            List<ATFwkTableHeaderItem> columns = new ArrayList<ATFwkTableHeaderItem>();
            byte id = 0;
            byte sortType = 0;
            byte sortKeyId = 0;
            // todo refactoring!!!!
            for (WebElement columnName : headerDataList) {
                if(keyHeaderArr==null || Arrays.binarySearch(keyHeaderArr, id) >= 0) {
                    List<WebElement> sortingImages = columnName.findElements(By.tagName("img"));
                    if(sortingImages.size()>0) {
                        if (sortingImages.get(0).getAttribute("src").contains("Ascending")) {
                            sortType = 0;
                        } else if (sortingImages.get(0).getAttribute("src").contains("Descending")){
                            sortType = 1;
                        }
                        sortKeyId = id;
                    }
                    columns.add(new ATFwkTableHeaderItem(id, columnName.getText()));
                } else {
                    columns.add(new ATFwkTableHeaderItem(id, null));
                }
                id++;
            }
            ATFwkTableHeader tableHeader = new ATFwkTableHeader(columns, sortKeyId, sortType);
            LOG.debug("Header is parsed");
            return tableHeader;
        }

        public ATFwkTableHeader getTableHeader(ATFwkFinder finder, String locator, int [] keyHeaderArr) {
            LOG.info("Get key header data");
            WebElement table = getElementBy(finder, locator);
            return getTableHeader(table, keyHeaderArr);
        }

    }


    class Verifier implements ATFwkVerifier {

        public boolean isTextPresent(String text) {
            if(text.length()==0) {
                LOG.warn("Requested text string is empty");
                return true;
            }
            LOG.info("Verify text presence {"+text+"}");
            WebElement bodyTag = webDriver.findElement(By.tagName("body"));

            if (bodyTag.getText().contains(text.replaceAll("\\?", "\\\\?"))) {
                LOG.debug("Text is present");
                return true;
            }
            LOG.debug("Text is NOT present");
            return false;
        }

        public boolean isTextPresent(String text, boolean doKeepSilence) {
            boolean isPresent = isTextPresent(text);
            if (doKeepSilence && isPresent) {
                return isPresent;
            }
            return false;
        }

        public boolean isElementPresent(ATFwkFinder finder, String locator) {
            WebElement element = getElementBy(finder, locator);
            return element != null && element.isDisplayed();
        }


        public boolean isImageLoadedBy(ATFwkFinder finder, String locator) {
            WebElement image = getElementBy(finder, locator);
            return image.isDisplayed();
        }

        public boolean isImageLoaded(String attrName, String attrValue) {
            List<WebElement> allImages = webDriver.findElements(By.xpath("//img[@" + getLocatorForImageLoading(attrName, attrValue) + "]"));
            return allImages.size() != 0 && allImages.get(0).isDisplayed();
        }

        public boolean isImageLoaded(String name, String value, boolean doKeepSilence) {
            try {
                boolean isPresent = isImageLoaded(name, value);
                if(doKeepSilence && isPresent) {
                    return isPresent;
                }
                LOG.warn("Image ["+value+"] is not found");
            } catch (Exception e) {
                LOG.warn("Error occurred while looking for the image {"+value+"}", e);
            }
            return false;
        }


        public boolean isCheckedBy(ATFwkFinder finder, String locator) {
            LOG.info("Verify is checkbox {"+locator+"} checked");
            WebElement checkbox = getElementBy(finder, locator);
            boolean isChecked = isChecked(checkbox);
            LOG.debug("Checked is " + isChecked);
            return isChecked;
        }
    }

}
