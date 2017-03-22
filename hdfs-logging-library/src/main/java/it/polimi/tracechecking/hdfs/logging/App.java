package it.polimi.tracechecking.hdfs.logging;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.launcher.SparkLauncher;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException, CompressorException, XMLStreamException
    {
        /* 
         * Code snippet used to test the writing into hdfs.
         * 
        Configuration config = new Configuration();     
        FileSystem fs;
        try {
            fs = FileSystem.get( new URI( "hdfs://localhost:9000" ), config );
            Path filenamePath = new Path("/micheleguerriero/traces/trace1");  
            BufferedWriter br=new BufferedWriter(new OutputStreamWriter(fs.append(filenamePath)));
            br.write("hello\n");
            br.write("bla\n");
            br.close();
            fs.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
        */
        
        //////////////////////////////
        
        /*
         * Code snippet used to test the launching of the MTLMapReduce spark job.
         * @Filippo, use this as the core of our wrapper for the trace checker.
         * @Filippo, work within the tc-driver module of the project and in the it.polimi.trachecking.driver package.
         * 
        final String javaHome = "/Library/Java/JavaVirtualMachines/jdk1.8.0_66.jdk/Contents/Home";
        final String sparkHome = "/Users/michele/tool/mtl-checker/spark-1.4.1-bin-hadoop2.6";
        final String appResource = "/Users/michele/tool/mtl-checker/MTLMapReduce-zip/scripts/MTL.jar";
        final String mainClass = "it.polimi.krstic.MTLMapReduce.SparkHistoryCheck";
        //
        // parameters passed to the  SparkFriendRecommendation
        final String[] appArgs = new String[]{
            //"--arg",
            "hdfs://localhost:9000/micheleguerriero/traces/trace1",
            
            //"--arg",
            "hdfs://localhost:9000/micheleguerriero/formulae/formula1",
            
            //"--arg",
            "hdfs://localhost:9000/micheleguerriero/output"
        };
        //
        //
        SparkLauncher spark = new SparkLauncher()
                .setVerbose(true)
                .setSparkHome(sparkHome)
                .setAppResource(appResource)    // "/my/app.jar"
                .setMainClass(mainClass)        // "my.spark.app.Main"
                .setMaster("spark://MacBook-Air-di-Luigi-2.local:7077")
                .setConf(SparkLauncher.DRIVER_MEMORY, "1g")
                .setConf(SparkLauncher.EXECUTOR_CORES, "2")
                .setConf(SparkLauncher.EXECUTOR_MEMORY, "2g")
                .addAppArgs(appArgs);
        //
        // Launches a sub-process that will start the configured Spark application.
        Process proc = spark.launch();
        //
        InputStreamReaderRunnable inputStreamReaderRunnable = new InputStreamReaderRunnable(proc.getInputStream(), "input");
        Thread inputThread = new Thread(inputStreamReaderRunnable, "LogStreamReader input");
        inputThread.start();
        //
        InputStreamReaderRunnable errorStreamReaderRunnable = new InputStreamReaderRunnable(proc.getErrorStream(), "error");
        Thread errorThread = new Thread(errorStreamReaderRunnable, "LogStreamReader error");
        errorThread.start();
        */

    }
}
