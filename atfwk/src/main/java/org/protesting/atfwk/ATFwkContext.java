package org.protesting.atfwk;

import org.apache.log4j.Logger;

import java.util.Date;

/**
 * ATFwkContext class
 * Author: Alexey Bulat
 * Date: 16.04.2010
 */
public class ATFwkContext {

    public static final Logger LOG = Logger.getLogger(ATFwkContext.class);
    /**
     * In current implementation resources directory is hardcoded: resources
     *
     * <b>Placeholder ${LOCATOR}</b>:
     * LOCATOR = package + class name
     * LOCATOR example: <i>org.testapp.pages.ActionServerControllerPage</i>
     */
    private static final String RESOURCE_PATH = "resources/${LOCATOR}.properties";

    private static ATFwkContext context;

    private ATFwkWebBrowser browser;
    private ATFwkPropertiesStorage propertiesStorage;
    private ATFwkPage currentPage;
    private String contextID;

    ATFwkContext() {
        init();
    }

    public synchronized static ATFwkContext initInstance(ATFwkContext newContext) {
        LOG.info("Init new instance");
        context = newContext;
        return context;
    }

    public synchronized static ATFwkContext initInstance(ATFwkWebBrowser browser) {
        LOG.info("Init context instance for the browser: " + browser);
        context = new ATFwkContext();
        context.setBrowser(browser);
        context.getBrowser().start();
        context.getBrowser().maximize();
        return context;
    }

    public synchronized static ATFwkContext getInstance() {
        if (context == null) {
            return null;
        }
        return context;
    }

    public ATFwkWebBrowser getBrowser() {
        if (browser != null) {
            return browser;
        }
        throw new ObjectNotFoundException("ATFwkWebBrowser is not initialized");
    }

    public ATFwkWebBrowser getChildBrowser(int index) {
        ATFwkWebBrowser childBrowser = getBrowser().getChildBrowser(index);
        if (childBrowser != null) {
            return childBrowser;
        }
        return null;
    }

    public ATFwkWebBrowser getChildBrowser() {
        return getBrowser().getChildBrowser();
    }

    private void init() {
        this.setContextID(Long.toString(new Date().getTime()) + (int)(Math.random() * 10000));
        this.setPropertiesStorage(ATFwkPropertiesStorage.getInstance(getContextID()));
    }

    private void setBrowser(ATFwkWebBrowser browser) {
        this.browser = browser;
    }

    protected String getResourcesPath(String name) {
        // cut the Subclass
        if(name.indexOf("$") > 0) {
            name = name.substring(0, name.indexOf("$"));
        }

        return RESOURCE_PATH.replaceAll("\\$\\{LOCATOR\\}", name.replaceAll("\\.", "/"));
    }


    private void setPropertiesStorage(ATFwkPropertiesStorage propertiesStorage) {
        this.propertiesStorage = propertiesStorage;
    }

    private void setContextID(String contextID) {
        this.contextID = contextID;
    }

    /**
     * Getting the Id of Context
     * @return Returns the <b>bID</b> of <b>Context</b> in which the particular browser was started
     */
    public String getContextID() {
        return contextID;
    }

    /**
     * Getting the ATFwkPropertiesStorage object
     * @return ATFwkPropertiesStorage
     */
    protected ATFwkPropertiesStorage getPropertiesStorage() {
        return propertiesStorage;
    }

    /**
     * Getting the current page object
     * @return current page object
     */
    public ATFwkPage getCurrentPage() {
        return currentPage;
    }

    protected void setCurrentPage(ATFwkPage currentPage) {
        this.currentPage = currentPage;
    }

    /**
     * Closing all instances of browsers and current page object
     */
    public void closeAll() {
        LOG.debug("Closing all browsers");
        browser.close();
        browser.quit();
        currentPage = null;
    }

    /**
     * Killing the browsers, page objects, propertiesStorage and the context
     */
    public void kill() {
        closeAll();
        LOG.debug("Killing working context");
        propertiesStorage = null;
        contextID = null;
        context = null;
        browser = null;
    }
}
