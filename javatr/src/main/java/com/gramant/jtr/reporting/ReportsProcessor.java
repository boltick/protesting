package com.gramant.jtr.reporting;

import com.gramant.jtr.JTRun;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by IntelliJ IDEA.
 * User: ab83625
 * Date: 06.05.2010
 * To change this template use File | Settings | File Templates.
 */
public class ReportsProcessor {

    private static final String TODO_FIELS_LOCATION = "todo";
    private static final String PROCESSED_FILES_LOCATION= "processed";
    private static final String REPORTS_LOCATION= "reports";

    private String testRunIndexFilename;
    private String testRunLogFilename;
    private String reportIndexFilename;

    private String srvRoot;

    public ReportsProcessor(String srvRoot) {
        this.srvRoot = srvRoot;
        this.testRunIndexFilename = JTRun.getProperty("test.run.index.file");
        this.testRunLogFilename = JTRun.getProperty("test.run.log.file");
        this.reportIndexFilename = JTRun.getProperty("report.server.index.file");
    }


    public File[] getTodoFiles() {
        File todo = new File(getSrvRoot() + TODO_FIELS_LOCATION);
        return todo.listFiles() != null ? todo.listFiles() : new File[]{};
    }

    public String getSrvRoot() {
        return srvRoot;
    }

    public void setSrvRoot(String srvRoot) {
        this.srvRoot = srvRoot;
    }


    public synchronized void process(File file) throws IOException {
        ZipFile zipFile = new ZipFile(file);
        Enumeration enumeration = zipFile.entries();
        String outputDirectory = prepareReportsStorage(file.getName());
        while (enumeration.hasMoreElements()) {
            ZipEntry entry = (ZipEntry) enumeration.nextElement();
            if(entry.isDirectory()) {
                (new File(entry.getName())).mkdir();
                continue;
            }

            copyInputStream(
                    zipFile.getInputStream(entry),
                    new BufferedOutputStream(new FileOutputStream(outputDirectory + File.separator + entry.getName())));
        }
        zipFile.close();
        boolean isRenamed = file.renameTo(new File(file.getAbsolutePath().replaceAll(TODO_FIELS_LOCATION, PROCESSED_FILES_LOCATION)));
        if (!isRenamed) { 
            file.delete();
        }

    }

    private synchronized String prepareReportsStorage(String name) {
        String [] path = name.replaceAll(".zip", "").split("--");

        String suiteName = srvRoot + REPORTS_LOCATION + File.separator +  path[0] + File.separator;
        String timestamp = suiteName + path[1];
        SimpleDateFormat format = new SimpleDateFormat("ddMMyyyy-HHmmss");
        String temp = format.format(new Date(Long.parseLong(path[1])));

        File suite = new File(suiteName);
        if (!suite.exists()) {
            suite.mkdir();
        }

        String [] output = temp.split("-");
        String dateDir = suiteName + output[0];
        File outDir = new File(dateDir);
        if (!outDir.exists()) {
            outDir.mkdir();
        }

        String timeDir = dateDir  + File.separator + output[1];
        outDir = new File(timeDir);
        if (!outDir.exists()) {
            outDir.mkdir();
        }

        return outDir.getAbsolutePath();
    }

    public synchronized void copyInputStream(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[2048];
        int len;

        while((len = in.read(buffer)) >= 0)
            out.write(buffer, 0, len);

        in.close();
        out.close();
    }

    public void reindexReports() {
        File suites = new File(getSrvRoot() + REPORTS_LOCATION);
        if (!suites.exists()) {
            throw new IllegalStateException("Path does not exist: " + suites.getAbsolutePath() );
        }
        ReportIndex rootReportIndex = new ReportIndex("Suites");
        File [] list = suites.listFiles();

        for (int i = 0; i < list.length; i++) {
            File suiteDirFile = list[i];
            rootReportIndex.addItem(new ReportItem(i+1, suiteDirFile.getName(), "./" + REPORTS_LOCATION + "/" +suiteDirFile.getName()+"/"+ reportIndexFilename));
            File root = new File(getSrvRoot() + REPORTS_LOCATION + "/" +suiteDirFile.getName() );

            File [] dateDirArray = root.listFiles();
            ReportIndex dateIndex = new ReportIndex(root.getName());
            for (int j = 0; j < dateDirArray.length; j++) {
                File dateDirFile = dateDirArray[j];
                if (!dateDirFile.isDirectory()) continue;
                String name = null;
                try {
                    name = new SimpleDateFormat("dd/MMM/yyyy").format(new SimpleDateFormat("ddMMyyyy").parse(dateDirFile.getName()));
                } catch (ParseException e) {
                    name = dateDirFile.getName();
                }
                dateIndex.addItem(new ReportItem(j+1, name, "./"+dateDirFile.getName()+"/"+ reportIndexFilename));

                String timeIndexPath = root.getAbsolutePath() + File.separator + dateDirFile.getName();
                ReportIndex timeIndex = new ReportIndex(root.getName() + " > Date: " + name);
                File [] timeDirArray = new File(timeIndexPath).listFiles();
                for (int k = 0; k < timeDirArray.length; k++) {
                    File timeDirFile = timeDirArray[k];
                    if (!timeDirFile.isDirectory()) continue;
                    try {
                        name = new SimpleDateFormat("HH:mm:ss").format(new SimpleDateFormat("HHmmss").parse(timeDirFile.getName()));
                    } catch (ParseException e) {
                        name = timeDirFile.getName();
                    }
                    timeIndex.addItem(new ReportItem(k+1, name, "./"+timeDirFile.getName()+"/" + testRunIndexFilename, "./"+timeDirFile.getName()+"/" + testRunLogFilename));
                }
                 timeIndex.save(timeIndexPath + File.separator + reportIndexFilename);

            }
            dateIndex.save(root.getAbsolutePath() + File.separator + reportIndexFilename);
        }
        rootReportIndex.save(getSrvRoot() + reportIndexFilename);
    }


    public String getTestRunIndexFilename() {
        return testRunIndexFilename;
    }

    public void setTestRunIndexFilename(String testRunIndexFilename) {
        this.testRunIndexFilename = testRunIndexFilename;
    }

    public String getReportIndexFilename() {
        return reportIndexFilename;
    }

    public void setReportIndexFilename(String reportIndexFilename) {
        this.reportIndexFilename = reportIndexFilename;
    }
}
