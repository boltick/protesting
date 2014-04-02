package org.protesting.atfwk;

import java.util.List;
import java.util.Map;

/**
 * ATFwkAction interface
 * Author: Alexey Bulat
 * Date: 25/02/13
 */
public interface ATFwkAction {

    public void attachFile(ATFwkFinder finder, String locator, String path);

    public boolean clickBy(ATFwkFinder finder, String locator);

    public boolean pushBy(ATFwkFinder finder, String locator);

    public boolean setBy(ATFwkFinder finder, String locator, int index);

    public boolean setBy(ATFwkFinder finder, String locator, String value);

    public boolean checkBy(ATFwkFinder finder, String locator);

    public boolean uncheckBy(ATFwkFinder finder, String locator);

    public boolean selectBy(ATFwkFinder finder, String locator, int index);

    public boolean selectBy(ATFwkFinder finder, String locator, String optionText);

    public boolean selectBy(ATFwkFinder finder, String locator, String [] optionTextArray);

    public Map<String, String> getOptionsMap(ATFwkFinder finder, String selectLocator);

    public String getSelectedValueBy(ATFwkFinder finder, String selectList);

    public List<String> getSelectedValuesBy(ATFwkFinder finder, String locator);

    boolean typeTextBy(ATFwkFinder finder, String locator, String value);

    public String getTextBy(ATFwkFinder finder, String locator);

    public String getValueBy(ATFwkFinder finder, String locator);

    public ATFwkTableHeader getTableHeader(ATFwkFinder finder, String locator, int [] keyHeaderArr);

    public ATFwkTable getTableBy(ATFwkFinder finder, String locator, int [] keyDataArr);

    public String getTableFieldBy(ATFwkFinder finder, String tableLocator, int rowIndex, int columnIndex);

    public int getTableRowsCount(ATFwkFinder finder, String tableLocator);

    public ATFwkTable getTableBy(ATFwkFinder finder, String locator);

    public String getSelectedRadioButtonValueBy(ATFwkFinder finder, String locator);

    public List<String> getSelectOptionsListBy(ATFwkFinder finder, String locator);

    public int getRadioGroupSizeBy(ATFwkFinder finder, String locator);

    public int getNumberOfElements(ATFwkFinder finder, String locator);

    public ATFwkTableRow getTableRowBy(ATFwkFinder finder, String tableLocator, int index);

    public List<ATFwkTableRow> getTableColumnBy(ATFwkFinder finder, String tableLocator, int columnIndex);
}
