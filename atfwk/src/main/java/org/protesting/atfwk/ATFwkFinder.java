package org.protesting.atfwk;

/**
 * ATFwkFinder enumeration
 * User: AB83625
 * Date: 25/02/13
 * To change this template use File | Settings | File Templates.
 */
public enum ATFwkFinder {

    ID (0),
    NAME (1),
    CLASS (2),
    TAG (3),
    CSS (4),
    XPATH (5),
    LINK_TEXT (6),
    PARTIAL_LINK_TEXT (7);

    public static final String BY_ID_ATTR = "id";
    public static final String BY_NAME_ATTR = "name";
    public static final String BY_VALUE_ATTR = "value";
    public static final String BY_SRC_ATTR = "src";
    public static final String BY_XPATH_ATTR = "xpath";
    public static final String BY_HREF_ATTR = "href";
    public static final String BY_CLASS_ATTR = "class";

    public static final String [] BY_ARRAY = new String[]{"id", "name", "class", "tag", "css", "xpath", "link", "partial_link"};

    private final int finderIndex;

    ATFwkFinder(int finderIndex) {
        this.finderIndex = finderIndex;
    }

    public int getFinderIndex() {
        return finderIndex;
    }

    public String getFinderName() {
        return BY_ARRAY[finderIndex];
    }

}
