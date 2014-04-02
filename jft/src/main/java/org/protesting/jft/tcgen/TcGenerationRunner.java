package org.protesting.jft.tcgen;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.protesting.jft.config.Configurator;
import org.protesting.jft.config.GeneratorConfig;
import org.protesting.jft.form.Form;
import org.protesting.jft.config.TcGenConfigurator;
import org.protesting.jft.data.ParseFactory;
import org.protesting.jft.tcgen.testsuite.TestSuiteBuilder;
import org.protesting.jft.testsuite.TestCaseGenerator;
import sun.security.util.Resources;

import java.io.*;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 *
 * User: abulat
 * Date: 24.03.2008
 * Time: 11:52:00
 */
public class TcGenerationRunner{

    private static Log logger;

//    private static final String DEFAULT_JFT_PROPERTY_PATH = "conf/org.protesting.jft.property";
    private static final String JFT_PROPERTY_PARAMETER = "-p";
    private static final String JFT_FILEPROCESS_PARAMETER = "-f";

    private String output;
    private File [] inputFileList;
    private String [] caseTypes;

    private static DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    static {
//        try {
//            Configurator.init(Resources.getBundle("jft"));
//            Configurator.init(
//                    new File(Configurator.class.getClassLoader().getResource("jft.properties").toURI()));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        catch (URISyntaxException e) {
//            e.printStackTrace();
//        }
//        File logDir = new File("log");
//        if (!logDir.exists() && !logDir.isDirectory()) {
//            logDir.mkdir();
//        }
        logger = LogFactory.getLog(TcGenerationRunner.class);
    }

    public TcGenerationRunner() throws IOException {
        this((String) Configurator.getProperties().get(Configurator.JFT_CONFIG_PATH));
    }


    public TcGenerationRunner(String propertyFilePath, String filePath) throws IOException {
        TcGenConfigurator.initTcGen(new File(propertyFilePath));
        this.inputFileList = new File[]{new File(filePath)};
        this.output = TcGenConfigurator.getProperty(TcGenConfigurator.OUTPUT_DATA_SOURCE) == null ||
                TcGenConfigurator.getProperty(TcGenConfigurator.OUTPUT_DATA_SOURCE).equalsIgnoreCase("") ?
                "xml" : TcGenConfigurator.getProperty(TcGenConfigurator.OUTPUT_DATA_SOURCE);
        this.caseTypes = TcGenConfigurator.getProperty("test.case.type").split(",");
    }


    private TcGenerationRunner(String propertyFilePath) throws IOException {
        TcGenConfigurator.initTcGen(new File(propertyFilePath));
        File dir = null;
        try {
            dir = new File(
                    (new GeneratorConfig())
                            .getClass()
                            .getClassLoader()
                            .getResource(TcGenConfigurator.getProperty(TcGenConfigurator.INPUT_PATH)).toURI()
            );
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        this.inputFileList = dir.listFiles();
        if (inputFileList == null || inputFileList.length == 0) {
            logger.debug("input data folder is empty");
            System.exit(0);
        }
        this.output = TcGenConfigurator.getProperty(TcGenConfigurator.OUTPUT_DATA_SOURCE) == null ||
                TcGenConfigurator.getProperty(TcGenConfigurator.OUTPUT_DATA_SOURCE).equalsIgnoreCase("") ?
                "xml" : TcGenConfigurator.getProperty(TcGenConfigurator.OUTPUT_DATA_SOURCE);
        this.caseTypes = TcGenConfigurator.getProperty("test.case.type").split(",");
    }


    private static String getValueStartWith(String[] args, String startWith) {
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.startsWith(startWith)) {
                return arg.substring(2);
            }
        }
        return "";
    }


    public void generate() {
        try {
            logger.debug("Output data source ["+Configurator.getProperty(TcGenConfigurator.OUTPUT_DATA_SOURCE)+"]");
            if (TestSuiteBuilder.isSupportedMode(output)) {
                for (int i = 0; i < inputFileList.length; i++) {
                    new GeneratorThread(i).start();
                }
            } else {
                logger.error("output data source ["+Configurator.getProperty(TcGenConfigurator.OUTPUT_DATA_SOURCE)+"] is not recognized");
                throw new IllegalStateException("Unknown output data source: " + Configurator.getProperty(TcGenConfigurator.OUTPUT_DATA_SOURCE));
            }
        } catch (Exception e) {
            logger.error(e);
        }

    }

    private void copyfile(File srFile, File dtFile) {
        try{
            InputStream in = new FileInputStream(srFile);
            OutputStream out = new FileOutputStream(dtFile);

            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0){
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
            logger.debug("XSLT File copied");
        }
        catch(FileNotFoundException e){
            logger.error("XSLT File is not copied because of: ", e);
        }
        catch(IOException e){
            logger.error("XSLT File is not copied because of: ", e);
        }
    }


    private static String getUniquieSuiteFileName(String name) {
        File test = new File(name);
        String newName = "";
        if (test.exists()) {
            logger.warn("Duplicate test suite is going to be created!!!");

            DateFormat format = new SimpleDateFormat("yyyyMMddhhmmss");
            newName = name + "_" + format.format(new Date());
            String testName = getUniquieSuiteFileName(newName);
            if (!testName.equals("")) {
                System.out.println("> Duplicate test suite "+ name +" is going to be created");
                System.out.println("--> New Test Suite name is: " + newName);
                logger.warn("Test Suite ["+ name +"] file name is changed to: " + newName);
                name = newName;
            }

        }
        return name;
    }


    public File[] getInputFileList() {
        return inputFileList;
    }


    public String getOutput() {
        return output;
    }


    public String[] getCaseTypes() {
        return caseTypes;
    }

    /*
      javac -cp. jft.jar -pconf/org.protesting.jft.property
      javac -cp. jft.jar
    */
    public static void main(String[] args) throws IOException {
        TcGenerationRunner jft = null;
        if (args.length == 0) {
            Configurator.init(Resources.getBundle("jft"));
            jft = new TcGenerationRunner();
        } else {
            String propPath = getValueStartWith(args, JFT_PROPERTY_PARAMETER);
            String filePath = getValueStartWith(args, JFT_FILEPROCESS_PARAMETER);

            if (propPath.equals("") && filePath.equals("")) {
                jft = new TcGenerationRunner();
            } else
            if (!propPath.equals("") && !filePath.equals("")) {
                jft = new TcGenerationRunner(propPath, filePath);
            } else
            if (propPath.equals("") && !filePath.equals("")) {
                jft = new TcGenerationRunner((String) Configurator.getProperties().get(Configurator.JFT_CONFIG_PATH), filePath);
            } else
                jft = new TcGenerationRunner(propPath);
        }

        System.out.println("-------------------------------------------------");
        System.out.println("Test Case Generation is started at " + format.format(new Date()));
        System.out.println("Please wait...");
        jft.generate();
    }


    class GeneratorThread extends Thread {
        private int index;

        public GeneratorThread(int index) {
            this.index = index;
        }

        public void run() {
            File file = getInputFileList()[getIndex()];
            String filename = file.getName();
            String ext = filename.substring(filename.lastIndexOf('.')+1, filename.length());
            if(ext.equals(TcGenConfigurator.getProperty(TcGenConfigurator.INPUT_DATA_SOURCE))) {
                Form form = ParseFactory.getForm(file, TcGenConfigurator.getProperty(TcGenConfigurator.INPUT_DATA_SOURCE));
                if (form == null || form.getEntityType() != Form.FORM_ENTITY) {
                    return;
                }
                // todo file name construction has to be changed!!!
                boolean fl =  TcGenConfigurator.getProperty(TcGenConfigurator.OUTPUT_PATH) == null ||
                        TcGenConfigurator.getProperty(TcGenConfigurator.OUTPUT_PATH).equalsIgnoreCase("");

                String folder = fl ? System.getProperty("user.dir") : TcGenConfigurator.getProperty(TcGenConfigurator.OUTPUT_PATH);

                File direc = new File(getUniquieSuiteFileName(folder + File.separator +form.getName() + "_suite"));

                direc.mkdir();

                for (int i = 0; i < getCaseTypes().length; i++) {
                    String type = getCaseTypes()[i].trim();
                    String suitename;
                    // to support output type "propery":
                    if (!getOutput().equalsIgnoreCase("property")) {
                        if (getOutput().equalsIgnoreCase("testcase")) {
                            suitename = direc.getPath()
                                    + File.separator + form.getName()+"_"+type+"_testcase.xml";
                        } else if(getOutput().equalsIgnoreCase("v1-xml")) {
                            suitename = direc.getPath()
                                    + File.separator + form.getName()+"_"+type+"_ts.xml";
                        } else {
                            suitename = direc.getPath()
                                    + File.separator + form.getName()+"_"+type+"_ts."+getOutput();
                        }
                    } else {
                        suitename = direc.getPath()
                                + File.separator + form.getName()+"_ts."+getOutput();
                    }
                    TestCaseGenerator generator = new TestCaseGenerator(form);
                    List casesList = generator.getTestCases(type, Configurator.getProperty("test.case.generation.method"));

                    TestSuiteBuilder.buildTestSuite(casesList, suitename, getOutput());

                    if (getOutput().equalsIgnoreCase("testcase")) {
                        try {
                            File source = null;
                            source = new File(Configurator.class.getClassLoader()
                                    .getResource(Configurator.getProperty("test.suite.xslt")).toURI());
                            File target = new File(direc.getAbsolutePath()
                                    + File.separator
                                    + TcGenConfigurator.getProperty("test.suite.xslt"));

                            copyfile(source, target);
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }

                    }
                }
                System.out.println("> Test Suite "+ direc.getPath() +" is created at "+ format.format(new Date()));
            }
        }

        private int getIndex() {
            return index;
        }
    }

}
