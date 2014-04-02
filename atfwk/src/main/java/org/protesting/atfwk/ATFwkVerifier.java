package org.protesting.atfwk;

/**
 * ATFwkVerifier interface
 * User: AB83625
 * Date: 25/02/13
 * To change this template use File | Settings | File Templates.
 */
public interface ATFwkVerifier {

    boolean isTextPresent(String text);

    boolean isTextPresent(String text, boolean doKeepSilence);

    public boolean isElementPresent(ATFwkFinder finder, String locator);

    boolean isImageLoadedBy(ATFwkFinder finder, String locator);

    boolean isImageLoaded(String attrName, String attrValue);

    boolean isImageLoaded(String attrName, String attrValue, boolean doKeepSilence);

    boolean isCheckedBy(ATFwkFinder finder, String locator);
}
