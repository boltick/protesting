package org.protesting.atfwk;

import java.net.URL;
import java.util.List;

/**
 * Base class for GUI User actions
 * User: AB83625
 * Date: 07/03/13
 * To change this template use File | Settings | File Templates.
 */
public class ATFwkUserAction {

    protected static ATFwkPage getCurrentPage() {
        return ATFwkContext.getInstance().getCurrentPage();
    }

    public static String getCurrentPageUrlAsString() {
        return getCurrentPage().getCurrentPageURL();
    }

    public static URL getCurrentUrl() {
        return ATFwkContext.getInstance().getBrowser().getCurrentURL();
    }


    /**
     * Inner assertion class implements simple assertion methods
     */
    public static class Check {

        public static boolean isEmptyString(String text) {
            return text.length() == 0;
        }

        public static boolean isEmptyList(List list) {
            return list.size() == 0;
        }

        public static boolean areEqual(Object o, Object b) {
            return o.equals(b);
        }

        public static boolean isTextPresent(String text) {
            return getCurrentPage().getBrowser().verify().isTextPresent(text);
        }

        public static boolean isImageLoaded(String attrName, String attrValue) {
            return getCurrentPage().getBrowser().verify().isImageLoaded(attrName, attrValue);
        }

        public static boolean isNull(Object o) {
            return o == null;
        }

        public static boolean contains(List<Object> list, Object o) {
            return list.contains(o);
        }

        public static boolean containsAll(List<String> list1, List<String> list2) {
            return list1.containsAll(list2);
        }
    }

}
