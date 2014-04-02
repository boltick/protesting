package org.protesting.atfwk;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.Logger;

/**
 * ATFwkPage is a Page Object pattern implementation. It provides a number
 * of service methods to work with page and its static and dynamic content.
 *
 * User: ab83625
 * Date: 10.06.2009
 * To change this template use File | Settings | File Templates.
 */
public class ATFwkPage extends ATFwkObjectWithProperties {

    public static final Logger LOG = Logger.getLogger(ATFwkPage.class);

    private String currentURL;

    protected ATFwkPage(String currentURL) {
        this.currentURL = currentURL;
        getPageContext().setCurrentPage(this);
        getPageContext().getBrowser().waitForPageToLoad();
        LOG.debug(this.getClass().getName() + " is opened");
    }

    protected ATFwkWebBrowser getChildBrowser(int index) {
        return getPageContext().getChildBrowser(index);
    }

    protected ATFwkWebBrowser getChildBrowser() {
        // TODO implement using of multi-children browsers
        return getPageContext().getChildBrowser();
    }

    /**
     * Method for getting the current page URL
     * @return page URL as a String
     */
    public String getCurrentPageURL() {
        if(!currentURL.isEmpty()) {
            return currentURL;
        } else {
            return getBrowser().getUrl();
        }
    }

    /**
     * Get page title method
     * @return page title as a String
     */
    public String getTitle() {
        return getBrowser().getTitle();
    }

    public void close() {
        getBrowser().close();
    }

    /**
     * Refreshes the page and waiting for page to load
     */
    public void refresh() {
        getBrowser().refresh();
        getBrowser().waitForPageToLoad();
    }

    /**
     * Implementation of the URL assertion method.
     *
     * throws IllegalStateException in case if current URL differs from the URL defined in page object
     * throws ParsePageException in case if URL is malformed
     */
    protected void assertUrlPath() {
        try {
            URL pageUrl = new URL(getBrowser().getUrl());
            if(!getCurrentPageURL().equals(pageUrl.getPath())) {
                if(!pageUrl.getPath().startsWith(getCurrentPageURL())) {
                    throw new IllegalStateException("Wrong URL path is opened: {"+pageUrl.getPath()+"} instead of {"+getCurrentPageURL()+"}");
//                    LOG.assertFail("Wrong URL path is opened: {"+pageUrl.getPath()+"} instead of {"+getCurrentPageURL()+"}");
                }
                LOG.warn("Not a full Page URLs matches: URL path is opened: {" + pageUrl.getPath() + "} instead of {" + getCurrentPageURL() + "}");
            }
        } catch (MalformedURLException e) {
            throw new ParsePageException("Error occurred while parsing page URL " + getBrowser().getUrl(), e);
        }
        LOG.info("Page with url " + getBrowser().getUrl() + " is opened");
    }

}
