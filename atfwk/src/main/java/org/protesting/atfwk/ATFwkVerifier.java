package org.protesting.atfwk;

/**
 * ATFwkVerifier interface
 * Author: Alexey Bulat
 * Date: 25/02/13
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
