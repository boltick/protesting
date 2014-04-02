/*
 * Suite
 *
 * Copyright (c) 2007 Gramant. All Rights Reserved
 */
package com.gramant.jtr;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

/**
 * Description
 *
 * @version (VCS$Id:$)
 */
public class Suite {
    private String id;
    private String name;
    private String className;
    private List<Test> testList = new ArrayList();

    public Suite() {
    }

    public Suite(String name) {
        this.name = name;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Test> getTestList() {
        return testList;
    }

    public List<Test> getVisibleTestList() {
        List<Test> visibleTestList = new ArrayList<Test>();
        for (Iterator<Test> iterator = testList.iterator(); iterator.hasNext(); ) {
            Test next = iterator.next();
            if(!next.isHidden()) visibleTestList.add(next);
        }
        return visibleTestList;
    }

    public void setTestList(List<Test> testList) {
        this.testList = testList;
    }

    public void addTest(Test test) {
        testList.add(test);
    }
}
