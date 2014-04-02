package org.protesting.atfwk;

/**
 * ATFwkTableHeaderItem class
 * Author: Alexey Bulat
 * Date: 21.01.2011
 */
public class ATFwkTableHeaderItem {

    private byte id;
    private String name;
    private boolean isSorted;
    private boolean isSortable;
    private byte sortType;


    public ATFwkTableHeaderItem(byte id, String name) {
        this.id = id;
        this.name = name;
    }

    public ATFwkTableHeaderItem(byte id, String name, byte sortType) {
        this.id = id;
        this.name = name;
        this.sortType = sortType;
        this.isSorted = true;
    }


    public byte getId() {
        return id;
    }

    public void setId(byte id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte getSortType() {
        return sortType;
    }

    public void setSortType(byte sortType) {
        this.sortType = sortType;
    }


    public boolean isSorted() {
        return isSorted;
    }

    public void setSorted(boolean sorted) {
        isSorted = sorted;
    }


    public boolean isSortable() {
        return isSortable;
    }

    public void setSortable(boolean sortable) {
        isSortable = sortable;
    }
}
