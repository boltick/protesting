package com.gramant.jtr.reporting;

/**
 * Created by IntelliJ IDEA.
 * User: ab83625
 * Date: 06.05.2010
 * To change this template use File | Settings | File Templates.
 */
public class ReportItem {

    private int index;
    private String name;
    private String path;
    private String logPath;


    public ReportItem(int index, String name, String path, String log) {
        this.index = index;
        this.name = name;
        this.path = path;
        this.logPath = log;
    }

    public ReportItem(int index, String name, String path) {
        this.index = index;
        this.name = name;
        this.path = path;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


    public String getLogPath() {
        return logPath;
    }

    public void setLogPath(String logPath) {
        this.logPath = logPath;
    }

    public String toXmlString() {
        if (logPath!=null) {
            return "<report index='" + getIndex() + "' name='" + getName() + "' path='"+getPath()+"' log='" + getLogPath() +"' />";
        } else {
            return "<report index='" + getIndex() + "' name='" + getName() + "' path='"+getPath()+"' />";
        }
    }
}
