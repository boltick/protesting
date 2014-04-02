package org.protesting.atfwk.selenium2;

import org.apache.log4j.Logger;
import org.protesting.atfwk.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

/**
 * SeleniumTableList is an class designed to work with HTML tables currently displayed on the page.
 *
 * Author: Alexey Bulat
 * Date: 04/04/13
 */
public class SeleniumTableList {

    public static final Logger LOG = Logger.getLogger(Selenium2WebBrowser.class);

    private ATFwkFinder fwkFinder;
    private final String tableLocator;
    private int currentElement;
    private WebElement table;
    private List<WebElement> rows;
    private ATFwkTableRow tableRowElement;

    public SeleniumTableList(ATFwkFinder fwkFinder, String tableLocator) {
        this.currentElement = 0;
        this.tableLocator = tableLocator;
        this.fwkFinder = fwkFinder;
        LOG.debug("Table {"+getTableLocator()+"} is initialized");
    }

    public boolean isEmpty() {
        return getRows().size() == 1;
    }

    public boolean exists() {
        return getTable()!=null;
    }

    public boolean hasNext() {
        List<WebElement> rows = getRows();
        return rows.size()>(getCurrentElementIndex()+1) && rows.get(getCurrentElementIndex()+1)!=null && rows.get(getCurrentElementIndex()+1).isDisplayed();
    }

    public ATFwkTableRow next() {
        currentElement++;
        this.tableRowElement = rowToATFwkTableRow(getRows().get(getCurrentElementIndex()));
        return current();
    }

    public ATFwkTableRow current() {
        if(currentElement==0) {
            return next();
        }
//        this.tableRowElement = rowToATFwkTableRow(rows.get(getCurrentElementIndex()));
        return tableRowElement;
    }

    private ATFwkTableRow rowToATFwkTableRow(WebElement row) {
        List<String> rowData = new ArrayList<String>();
        try {
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
        } catch (Exception e) {
            LOG.warn("Error parsing element {"+currentElement+"}");
            throw new ParsePageException(e);
        }
        LOG.debug("Row is parsed: " + rowData);
        return new ATFwkTableRow(rowData);
    }

    public boolean containsText(String text) {
        for(WebElement row: getRows()) {
            List<WebElement> cols = row.findElements(By.cssSelector("td"));
            for (WebElement field : cols) {
                if(field.getText().equals(text)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean containsText(int columnIndex, String text) {
        for(WebElement row: getRows()) {
            List<WebElement> cols = row.findElements(By.cssSelector("td"));
            if(cols.get(columnIndex).getText().equals(text)) {
                return true;
            }
        }
        return false;
    }

    public boolean containsText(int rowIndex, int columnIndex, String text) {
        List<WebElement> cols = getRows().get(rowIndex).findElements(By.cssSelector("td"));
        return cols.get(columnIndex).getText().equals(text);
    }

    public int size() {
        return getRows().size() - 1;
    }

    public ATFwkFinder getFwkFinder() {
        return fwkFinder;
    }

    public String getTableLocator() {
        return tableLocator;
    }

    public int getCurrentElementIndex() {
        return currentElement;
    }


    private WebElement getTable() {
        if(table == null) {
            initTable();
        }
        return table;
    }

    private List<WebElement> getRows() {
        try {
            getTable().isDisplayed();
        } catch (Exception exF) {
            LOG.debug("Table rows are not displayed correctly: " + exF);
            exF.printStackTrace();
            try {
                initTable();
                initRows();
            } catch (Exception exS) {
                LOG.error("Table/Rows context is not initialized: " + exS);
                throw new ObjectNotFoundException("Table/Rows context is not initialized", exS);
            }
        }
        if(rows == null) {
            initRows();
        }
        return rows;
    }

   private void initTable() {
       try {
           this.table = ((Selenium2WebBrowser)(ATFwkContext.getInstance().getBrowser())).getElementBy(getFwkFinder(), getTableLocator());
       } catch (Exception exS) {
           LOG.error("Table is not initialized", exS);
           exS.printStackTrace();
           throw new ObjectNotFoundException("Table context is not initialized", exS);
       }
   }

   private void initRows() {
       try {
           this.rows = getTable().findElements(By.cssSelector("tbody tr"));
       } catch (Exception exS) {
           LOG.error("Rows are not initialized");
           exS.printStackTrace();
           throw new ObjectNotFoundException("Rows context is not initialized", exS);
       }
   }

}
