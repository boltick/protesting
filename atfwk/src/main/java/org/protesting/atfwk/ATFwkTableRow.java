package org.protesting.atfwk;

import java.util.List;
import java.util.ArrayList;

/**
 * ATFwkTableRow class
 * User: ab83625
 * Date: 18.01.2011
 * To change this template use File | Settings | File Templates.
 */
public class ATFwkTableRow {

    protected List<String> rowDataList;

    public ATFwkTableRow() {
        this.rowDataList = new ArrayList<String>();
    }

    public ATFwkTableRow(List<String> rowDataList) {
        this();
        this.rowDataList = rowDataList;
    }

    public int compareTo(ATFwkTableRow row) {
        for (int i = 0; i < rowDataList.size(); i++) {
            if (!rowDataList.get(i).equals(row.get(i))) {
                return -1;
            }
        }
        return 1;
    }

    public int compareTo(ATFwkTableRow row, int columnId) {
        // todo improve
        String str1 = this.rowDataList.get(columnId).toLowerCase();
        String str2 = row.rowDataList.get(columnId).toLowerCase();
        return str1.compareTo(str2);
    }

    public String get(int index) {
        return rowDataList.get(index);
    }

    protected void set(int columnId, String data) {
        rowDataList.add(columnId, data);
    }


    public List<String> getRowDataList() {
        return rowDataList;
    }

    public boolean isEmpty() {
        return rowDataList.size() == 0;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ATFwkTableRow row = (ATFwkTableRow) o;

        return !(rowDataList != null ? !rowDataList.equals(row.rowDataList) : row.rowDataList != null);

    }

    public int hashCode() {
        return (rowDataList != null ? rowDataList.hashCode() : 0);
    }

    @Override
    public String toString() {
        return "ATFwkTableRow{" +
                "rowDataList=" + rowDataList +
                '}';
    }
}
