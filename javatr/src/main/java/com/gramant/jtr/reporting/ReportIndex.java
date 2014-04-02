package com.gramant.jtr.reporting;

import com.gramant.jtr.JTRun;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: ab83625
 * Date: 06.05.2010
 * To change this template use File | Settings | File Templates.
 */
public class ReportIndex {

    private String xslPath;
    private String suiteName;
    private ArrayList<ReportItem> items;


    public ReportIndex(String suiteName) {
        this.suiteName = suiteName;
        this.items = new ArrayList <ReportItem> ();
        this.xslPath = JTRun.getProperty("xslt.path");
    }


    public String getSuiteName() {
        return suiteName;
    }

    public void setSuiteName(String suiteName) {
        this.suiteName = suiteName;
    }


    public void addItem(ReportItem item) {
        this.items.add(item);
    }

    public ArrayList<ReportItem> getItems() {
        return items;
    }

    public String getXslPath() {
        return xslPath==null ? "": xslPath;
    }

    public void setXslPath(String xslPath) {
        this.xslPath = xslPath;
    }

    private StringBuffer getXmlContent() {
        StringBuffer xmlContent = new StringBuffer();

        xmlContent.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append("\r\n");
        xmlContent.append("<?xml-stylesheet type='text/xsl' href='" + getXslPath() + "report.xsl'?>").append("\r\n");

        xmlContent.append("<suite name=\""+ getSuiteName() + "\">").append("\r\n");

        for (int i = 0; i < getItems().size(); i++) {
            ReportItem reportItem = getItems().get(i);
            xmlContent.append("\t").append(reportItem.toXmlString()).append("\r\n");
        }

        xmlContent.append("</suite>");

        return xmlContent;
    }


    public void save(String fileName) {
        FileOutputStream  fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(fileName);
            StringBuffer xml = getXmlContent();
            fileOutputStream.write(xml.toString().getBytes());
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            fileOutputStream = null;
        }
    }


}
