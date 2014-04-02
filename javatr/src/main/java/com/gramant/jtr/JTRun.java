package com.gramant.jtr;

import com.gramant.jtr.log.ResultsLogger;
import com.gramant.jtr.reporting.ReportsProcessor;
import org.apache.commons.cli.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Java test runner with final report generation and uploading to the defined reporting server
 *
 * User: ab83625
 * Date: 07.05.2010
 * To change this template use File | Settings | File Templates.
 */
public class JTRun {

    protected static final ResultsLogger logger = (ResultsLogger) ResultsLogger.getLogger(JTRun.class);
    
    private static Properties properties;

    static Option help = new Option("help", "print this message");
    static Option props = OptionBuilder.withArgName("filepath")
            .hasArg()
            .withDescription("global properties file")
            .create("props");
    static Option suites = OptionBuilder.withArgName("filepath")
            .hasArg()
            .withDescription("suites list file")
            .create("suites");
    static Option output = OptionBuilder.withArgName("output")
            .hasArg()
            .withDescription("output results folder")
            .create("output");
    static Option testtype = OptionBuilder.withArgName("testtype")
            .hasArg()
            .withDescription("define test type: smoke, functional, regression. If empty than all tests will be run")
            .create("testtype");

    static Option run = new Option("run", "print this message");
    static Option reindex = new Option("reindex", "print this message");

    static Options options = new Options();

    static {
        options.addOption(help);
        options.addOption(props);
        options.addOption(reindex);
        options.addOption(output);
        options.addOption(suites);
        options.addOption(testtype);
    }

    public static void main(String[] args) throws IOException {
        ArrayList processedSuites = execWithCommandArgs(args);

        File zippedResultFile = zipResultFiles(processedSuites, getSuiteName());

        if (getProperty("report.server")==null || (getProperty("report.server")!=null && !Boolean.parseBoolean(getProperty("report.server")))) {
            return;
        }

        if (getProperty("report.server.path")!= null && !getProperty("report.server.path").equals("")) {
            UploadHelper uploader =
                    new UploadHelper(
                            getProperty("report.transfer.protocol").equals("") ? UploadHelper.FILE_PROTOCOL : Integer.parseInt(getProperty("report.transfer.protocol")),
                            getProperty("report.server.host"),
                            getProperty("report.server.user"),
                            getProperty("report.server.password"));

            uploader.upload(zippedResultFile.getAbsolutePath(), getProperty("report.server.todo.path"));
        }
        reindex();
    }


    private static String getSuiteName() {
        String undef = "UNDEF";
        String office = properties.getProperty("office.name") != null ? properties.getProperty("office.name")+"_" : "";
        String environmentType = properties.getProperty("environment.type") != null ? properties.getProperty("environment.type") : undef;
        String appEnvironmentType = properties.getProperty("app.environment.type") != null ? properties.getProperty("app.environment.type") : undef;

        String environment = !environmentType.equals(undef) ?  environmentType : undef;
        environment += appEnvironmentType.equals(undef) ? "" : "_"+appEnvironmentType;

        String temp = suites.getValue();
        temp.replaceAll("\\\\", "/");
        String [] parts = temp.split("/");
        temp = parts[parts.length-1];

        String type = options.getOption("testtype").getValue() != null ? options.getOption("testtype").getValue() : temp.substring(0, temp.indexOf("."));

        return (office + environment + "_" + type).toUpperCase();
    }

    private static void reindex() throws IOException {
        ReportsProcessor processor = new ReportsProcessor(getProperty("report.server.path"));
        File [] todo = processor.getTodoFiles();
        logger.debug("Start reindex process");
        logger.info("Start reading files");
        for (int i = 0; i < todo.length; i++) {
            File file = todo[i];
            logger.info("Reading file: " + file.getAbsolutePath());
            processor.process(file);
        }
        logger.info("Stop reading files");
        if (todo.length != 0) {
            processor.reindexReports();
        }
        logger.debug("End reindex process");
    }

    private static ArrayList execWithCommandArgs(String[] args) throws IOException {
        PosixParser parser = new PosixParser();
        CommandLine line = null;
        try {
            // parse the command line arguments
            line = parser.parse(options, args);
        } catch (ParseException exp ) {
            // oops, something went wrong
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("jtr", options);
            System.exit(0);
        }
        if (line.hasOption(help.getOpt())) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("jtr", options);
            System.exit(0);
        }
        if (line.hasOption(props.getOpt())) {
            properties = new Properties();
            String files_path = line.getOptionValue(props.getOpt());
            files_path = files_path.substring(0, files_path.lastIndexOf("/"));
            properties.load(new FileInputStream(line.getOptionValue(props.getOpt())));
        }
        if (line.hasOption(reindex.getOpt())) {
            reindex();
            System.exit(0);
        }
        if (line.hasOption(suites.getOpt())) {
            JTR.init(args);
            JTR jtr = JTR.getInstance();
            List files = ListReader.getSuitesFiles(jtr.getSuites().getValue());
            jtr.start(files);
            return jtr.getProcessedSuites();
        }
        System.exit(0);
        return null;
    }


    public static File zipResultFiles(ArrayList suiteFileList, String name) throws IOException {
        logger.info("Prepare zip file");
        String path = output.getValue() == null ? "results" : output.getValue();
        int buffer = 2048;
        String tempZipFile = path + File.separator + name + "--"+ new Date().getTime() + ".zip" ;
        File outZipFile = new File(tempZipFile);

        File inPath = new File(path);
        File [] fileList = new File [suiteFileList.size() + 2 + logger.getScreenShotList().size()];
        int fileIndex = 0;
        for (Iterator iterator = suiteFileList.iterator(); iterator.hasNext();) {
            String suiteXmlPath = (String) iterator.next();
            Suite suite = JTR.parseXml(suiteXmlPath);
            if(suite == null) { continue; }
            fileList[fileIndex] = new File(path + File.separator + suite.getName() + ".xml");
            fileIndex++;
        }
        if (fileIndex == 0) {
            logger.errorResult("There are no test results to be processed");
            System.exit(0);
        }
        fileList[fileIndex++] = new File(path + File.separator + JTRun.getProperty("test.run.index.file"));
        if (!JTRun.getProperty("test.run.log.file").equals("")) {
            fileList[fileIndex++] = new File(path + File.separator + JTRun.getProperty("test.run.log.file"));
        }

        for (int i = 0; i < logger.getScreenShotList().size(); i++) {
             fileList[fileIndex++] = new File(path + File.separator + (logger.getScreenShotList().get(i)).getPath());
        }
        fileIndex++;
        // Create a buffer for reading the files
        byte [] buf = new byte[buffer];
        // Create the ZIP file
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outZipFile));

        // Compress the files
        for (int i=0; i < fileIndex-1; i++) {
            if (fileList[i].isDirectory()) continue;
            FileInputStream in = new FileInputStream(fileList[i]);
            // Add ZIP entry to output stream.
            out.putNextEntry(new ZipEntry(fileList[i].getName()));
            // Transfer bytes from the file to the ZIP file
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            // Complete the entry
            out.closeEntry();
            in.close();
        }
        // Complete the ZIP file
        out.close();
        logger.info("zip file "+ outZipFile.getName()+" is created");
        return outZipFile;
    }


    public static String getProperty(String key) {
        if (properties == null) {
            logger.warn("!!! Properties are not initialized !!!");
            return "";
        }
        return properties.getProperty(key) != null ? properties.getProperty(key).trim() : "";
    }

}
