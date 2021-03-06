package it.polimi.tracechecking.driver;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    
    public static final String PATH_TO_OUTPUT = "path_to_output";
    public static final String JAVA_HOME = "java_home";
    public static final String SPARK_HOME = "spark_home";
    public static final String PATH_TO_APP = "path_to_app";
    public static final String SPARK_HOST = "spark_host";
    public static final String HDFS_HOST = "hdfs_host";
    public static final String HDFS_PORT = "hdfs_port";
    private static Properties prop = new Properties();
    private static Boolean isLoaded = false;

    public static void loadConfig(String pathToConfigFile) {
        InputStream input = null;

        try {

            input = new FileInputStream(pathToConfigFile);

            prop.load(input);
            
            isLoaded = true;

        } catch (IOException ex) {
            throw new RuntimeException(ex.getMessage(), ex.getCause());
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public static String getProperty(String propertyName){
        if(isLoaded)
            return prop.getProperty(propertyName);
        else
            throw new RuntimeException("Trying to get the property '" + propertyName + "' but properties have not been loaded.");
    }
}

