package org.protesting.atfwk;

import java.util.List;

/**
 * ATFwkTableHeader class
 * Author: Alexey Bulat
 * Date: 08/03/13
 */
public class ATFwkTableHeader {

    private List<ATFwkTableHeaderItem> columnNameList;
    private byte sortedKeyId;
    private byte sortType;

    public ATFwkTableHeader(List<ATFwkTableHeaderItem> columnNameList, byte sortedKeyId, byte sortType) {
        this.columnNameList = columnNameList;
        this.sortedKeyId = sortedKeyId;
        this.sortType = sortType;
    }

    public byte getSortedKeyId() {
        return sortedKeyId;
    }

    public void setSortedKeyId(byte sortedKeyId) {
        this.sortedKeyId = sortedKeyId;
    }

    public byte getSortType() {
        return sortType;
    }

    public void setSortType(byte sortType) {
        this.sortType = sortType;
    }

    public List<ATFwkTableHeaderItem> getColumnNameList() {
        return columnNameList;
    }

    public void addColumnName(ATFwkTableHeaderItem column) {
        this.columnNameList.add(column);
    }

    public String getColumn(int id) {
        return columnNameList.get(id).getName();
    }

}
