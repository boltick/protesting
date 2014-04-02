package org.protesting.atfwk;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * ATFwkTable class
 * Author: Alexey Bulat
 * Date: 29.06.2010
 */
public class ATFwkTable {

    public static final int ASCENDING = 0;
    public static final int DESCENDING = 1;

    public static final String [] SORTING_TYPES = {"ASCENDING", "DESCENDING"};

    private ATFwkTableHeader header;
    private int sortKeyId;
    private int sortType;
    private List <ATFwkTableRow> rows;


    public ATFwkTable(ATFwkTableHeader header, List <ATFwkTableRow> row) {
        this.header = header;
        this.sortKeyId = header.getSortedKeyId();
        this.sortType = header.getSortType();
        this.rows = row;
    }

    public ATFwkTableHeader getHeader() {
        return header;
    }



    public void setHeader(ATFwkTableHeader header) {
        this.header = header;
    }

    public int getRowCount() {
        return rows.size();
    }

    public boolean isEmpty() {
        return !(rows!=null && rows.size() >= 0);
    }

    public List <ATFwkTableRow> getRows() {
        return rows;
    }

    public ATFwkTableRow getRow(int i) {
        return rows.get(i);
    }


    public String getData(int rowNr, int colNr) {
        return rows.get(rowNr).get(colNr);
    }

    public int getSortKeyId() {
        return sortKeyId;
    }

    public void setSortKeyId(int sortKeyId) {
        this.sortKeyId = sortKeyId;
    }

    public int getSortType() {
        return sortType;
    }

    public void setSortType(int sortType) {
        this.sortType = sortType;
    }


    public void setSortableColumns(String[] sortableColumnNames) {
        for (int i = 0; i < header.getColumnNameList().size(); i++) {
            ATFwkTableHeaderItem item = header.getColumnNameList().get(i);
            Arrays.sort(sortableColumnNames);
            if(Arrays.binarySearch(sortableColumnNames, header.getColumn(i)) >= 0) {
                item.setSortable(true);
            }
        }
    }


    public List <ATFwkTableHeaderItem> getSortableColumns() {
        List <ATFwkTableHeaderItem> sortableColumns = new ArrayList<ATFwkTableHeaderItem>();
        for (int i = 0; i < header.getColumnNameList().size(); i++) {
            ATFwkTableHeaderItem item = header.getColumnNameList().get(i);
            if (item.isSortable()) sortableColumns.add(item);
        }
        return sortableColumns;
    }

    public ATFwkTable getSortedTable(int sortColumnID, int type) {
        ATFwkTable sortedTable = new ATFwkTable(getHeader(), getRows());
        sortedTable.setSortKeyId(sortColumnID);
        sortedTable.setSortType(type);

        int d = sortedTable.getRowCount();
        while (d!=1) {
            d = d/2;
            for (int j = d; j < sortedTable.getRowCount(); j++) {
                for (int k = j; k < sortedTable.getRowCount(); k=k+d) {
                    switch(type) { // @todo - improve
                        case DESCENDING : {
                            if (compareRows(sortedTable.getRow(j-d), sortedTable.getRow(k), sortColumnID) < 0) {
                                ATFwkTableRow temp = sortedTable.getRows().get(k);
                                sortedTable.getRows().set(k, sortedTable.getRows().get(j-d));
                                sortedTable.getRows().set(j-d, temp);
                            }
                            break;
                        }
                        case ASCENDING: {
                            if (compareRows(sortedTable.getRow(j-d), sortedTable.getRow(k), sortColumnID) > 0) {
                                ATFwkTableRow temp = sortedTable.getRows().get(k);
                                sortedTable.getRows().set(k, sortedTable.getRows().get(j-d));
                                sortedTable.getRows().set(j-d, temp);
                            }

                        }
                    }
                }
            }
        }
        return sortedTable;
    }


    public int compare(ATFwkTable table) {
        for (int i = 0; i < table.getRows().size(); i++) {
            if (table.getRows().get(i).compareTo(this.getRows().get(i)) < 0) {
                return -1;
            }
        }
        return 1;
    }

    public int compare(ATFwkTable table, int columnId) {
        for (int i = 0; i < table.getRows().size(); i++) {
            if (table.getRows().get(i).compareTo(this.getRows().get(i), columnId) < 0) {
                return -1;
            }
        }
        return 1;
    }

    public int compareRows(ATFwkTableRow row1, ATFwkTableRow row2, int columnID) {
        return row1.compareTo(row2, columnID);
    }


    public List<String> getColumnAsList(int index) {
        List<String> columnList = new ArrayList<String>();
        for (ATFwkTableRow row : rows) {
            String columnValue = row.get(index);
            columnList.add(columnValue);
        }
        return columnList;
    }

    public static void main(String[] args) {
        ArrayList<ATFwkTableHeaderItem> header = new ArrayList<ATFwkTableHeaderItem>();
        header.add(new ATFwkTableHeaderItem((byte)0, "test1"));
        header.add(new ATFwkTableHeaderItem((byte)1, "test2"));
        header.add(new ATFwkTableHeaderItem((byte)2, "test3"));
        ArrayList<ATFwkTableRow> rows = new ArrayList<ATFwkTableRow>();
        String [] arr1 = new String[]{"a1", "a2", "a3"};
        String [] arr2 = new String[]{"c1", "c2", "c3"};
        String [] arr3 = new String[]{"b1", "b2", "b3"};
        rows.add(new ATFwkTableRow(Arrays.asList(arr1)));
        rows.add(new ATFwkTableRow(Arrays.asList(arr2)));
        rows.add(new ATFwkTableRow(Arrays.asList(arr3)));
        ATFwkTable fwkTable = new ATFwkTable(new ATFwkTableHeader(header, (byte)1, (byte)0), rows);

        ATFwkTable sortedTable = fwkTable.getSortedTable(2, 1);
        System.out.println("");
    }

}
