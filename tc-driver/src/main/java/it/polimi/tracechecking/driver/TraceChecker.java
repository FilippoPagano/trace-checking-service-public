package it.polimi.tracechecking.driver;

import it.polimi.tracechecking.common.model.ComputeNode;
import it.polimi.tracechecking.common.model.Permission;
import org.apache.spark.launcher.SparkLauncher;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TraceChecker {

    Process proc ;

    public static String getViolations(){
        String outputDir = Config.getProperty(Config.PATH_TO_OUTPUT);
        outputDir = outputDir.substring(0,outputDir.indexOf("output"));
        File dir = new File(outputDir);
        File [] files = dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.startsWith("output");
            }
        });
        StringBuilder sb = new StringBuilder();
        for (File xmlfile : files) {
            BufferedReader br = null;
            try {
                br = new BufferedReader(new FileReader(xmlfile));
                String line = br.readLine();
                while (line != null) {
                    sb.append(line);
                    sb.append(System.lineSeparator());
                    line = br.readLine();
                }
            } catch (Exception e) {
                e.printStackTrace();
        }      finally {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
        String everything = sb.toString();
        return everything;
        }

    public static void checkTrace(String pathToEventsFile, String pathToFormulaeFile, String pathToOutputFile) {
        try {

            final String sparkHome = Config.getProperty(Config.SPARK_HOME);
            final String javaHome = Config.getProperty(Config.JAVA_HOME);
            final String appResource = Config.getProperty(Config.PATH_TO_APP); //i'd rather put it in resources idk
            final String mainClass = "it.polimi.krstic.MTLMapReduce.SparkHistoryCheck";
            final String hdfsHost = Config.getProperty(Config.HDFS_HOST);
            final String hdfsPort = Config.getProperty(Config.HDFS_PORT);
            //
            // parameters passed to the SparkFriendRecommendation
            final String[] appArgs = new String[]{
                    // "--arg",
                    "hdfs://" + hdfsHost +":"+ hdfsPort + pathToEventsFile, // eg
                    // "hdfs://localhost:9000/user/fil/trace1"

                    // "--arg",
                   pathToFormulaeFile, // eg
                    // "/home/filippo/Scrivania/formula1",

                    // "--arg"
                    pathToOutputFile// eg "/home/filippo/Scrivania/output"
            };
            //
            //
            SparkLauncher spark = new SparkLauncher().setVerbose(true).setSparkHome(sparkHome)
                    .setAppResource(appResource) // "/my/app.jar"
                    .setJavaHome(javaHome)
                    .setMainClass(mainClass) // "my.spark.app.Main"
                    .setMaster(Config.getProperty(Config.SPARK_HOST)).setConf(SparkLauncher.DRIVER_MEMORY, "1g")
                    .setConf(SparkLauncher.EXECUTOR_CORES, "1").setConf(SparkLauncher.EXECUTOR_MEMORY, "1g")
                    .addAppArgs(appArgs);
            //
            // Launches a sub-process that will start the configured Spark
            // application.
            Process proc = spark.launch();
          //  proc.destroy();

            //
            InputStreamReaderRunnable inputStreamReaderRunnable = new InputStreamReaderRunnable(proc.getInputStream(),
                    "input");
            Thread inputThread = new Thread(inputStreamReaderRunnable, "LogStreamReader input");
            inputThread.start();
            //
            InputStreamReaderRunnable errorStreamReaderRunnable = new InputStreamReaderRunnable(proc.getErrorStream(),
                    "error");
            Thread errorThread = new Thread(errorStreamReaderRunnable, "LogStreamReader error");
            errorThread.start();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {

        try {
            //     Config.loadConfig("//home/filippo/IdeaProjects/trace-checking-service/conf/config.properties");
            //      Config.getProperty("path_to_model");
            //      DIA dia = ModelLoader.loadInputModelFromFile(Config.getProperty("path_to_model"));
            //     System.out.println( dia.getPermissions().get(0).getAsociatedMtlFormula());

            //	connect("",0);


            Config.loadConfig("/home/filippo/IdeaProjects/trace-checking-service/conf/config.properties");
            System.out.println(Config.getProperty(Config.PATH_TO_OUTPUT));
           // checkTrace("hdfs://localhost:9000/user/fil/trace1", "/home/filippo/Scrivania/formula1", Config.getProperty(Config.PATH_TO_OUTPUT));
           // checkTrace("hdfs://localhost:9000/user/fil/trace1", "/home/filippo/Scrivania/formula1", "/home/filippo/Scrivania/output");

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public Map<String, Boolean> checkTrace(String pathToEventsFile, List<Permission> permissions) {
        Map<String, Boolean> result = new HashMap<String, Boolean>();

        for (Permission p : permissions) {
            result.put(p.getId(), false);
        }
        return result;
    }

    /*public Integer getViolations(String formula){
        List<String> resultsContainer = new ArrayList<String>();
        List<String> pathsToFormula = new ArrayList<String>();
        Integer i = 0;
        try {
            File pathToFormulae = new File(Config.getProperty(Config.PATH_TO_OUTPUT)+File.separator+"formulae");
            searchFiles(pathToFormulae, formula, pathsToFormula);
            File pathToOutput = new File(Config.getProperty(Config.PATH_TO_OUTPUT));
            searchFiles(pathToOutput,"false",resultsContainer);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        for (String path :resultsContainer ) {
           // if (path.contains(pathsToFormula.get(0)))
            //
            i++; //TODO not correct, returns empty set
            // I want to increment for every result of the specified formula
        }
        return i;
    }*/
    public Integer getViolations(ComputeNode c, Integer formulaIndex) {
        List<String> resultsContainer = new ArrayList<String>();
        Integer i = 0;
        try {
            File pathToOutput = new File(Config.getProperty(Config.PATH_TO_OUTPUT) + File.separator + c.getId());
            searchFiles(pathToOutput, "false", resultsContainer);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        for (String path : resultsContainer) {
            String result = "0";
            Pattern p = Pattern.compile("[0-9]+$");
            Matcher m = p.matcher(path);
            if (m.find()) {
                result = m.group();
            }

            if (Integer.parseInt(result) == formulaIndex) i++;
        }

        return i;
    }

    private List<String> searchFiles(File file, String pattern, List<String> result) throws FileNotFoundException {

        if (!file.isDirectory()) {
            throw new IllegalArgumentException("file has to be a directory");
        }

        if (result == null) {
            result = new ArrayList<String>();
        }

        File[] files = file.listFiles();

        if (files != null) {
            for (File currentFile : files) {
                if (currentFile.isDirectory()) {
                    searchFiles(currentFile, pattern, result);
                } else {
                    Scanner scanner = new Scanner(currentFile);
                    if (scanner.findWithinHorizon(pattern, 0) != null) {
                        result.add(currentFile.getName());
                    }
                    scanner.close();
                }
            }
        }
        return result;
    }

    //*
    public void destroy() {
        if (proc != null) proc.destroy();
    }
//*/
}
