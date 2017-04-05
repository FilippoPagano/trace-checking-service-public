package it.polimi.tracechecking.driver;

import it.polimi.tracechecking.common.model.ComputeNode;
import org.apache.spark.launcher.SparkLauncher;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TraceChecker {

    private Process proc;

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
                    "hdfs://" + hdfsHost + ":" + hdfsPort + pathToEventsFile, // eg
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
            e.printStackTrace();
        }
    }

    public static Integer getViolations(ComputeNode c, Integer formulaIndex) {
        List<String> resultsContainer = new ArrayList<String>();
        Integer i = 0;
        try {
            File pathToOutput = new File(Config.getProperty(Config.PATH_TO_OUTPUT) + File.separator + c.getId());
            Utils.searchFiles(pathToOutput, "false", resultsContainer);
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

    public static void main(String args[]) {

        try {

            Config.loadConfig("/home/filippo/IdeaProjects/trace-checking-service/conf/config.properties");
            System.out.println(Config.getProperty(Config.PATH_TO_OUTPUT));
            // checkTrace("hdfs://localhost:9000/user/fil/trace1", "/home/filippo/Scrivania/formula1", Config.getProperty(Config.PATH_TO_OUTPUT));
            // checkTrace("hdfs://localhost:9000/user/fil/trace1", "/home/filippo/Scrivania/formula1", "/home/filippo/Scrivania/output");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void destroy() {
        if (proc != null) proc.destroy();
    }

}
