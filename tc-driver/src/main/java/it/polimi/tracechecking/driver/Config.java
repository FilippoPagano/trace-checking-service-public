package it.polimi.tracechecking.driver;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    
    public static final String INTERVAL_BETWEEN_TC_RUN = "interval_between_tc_run";
    public static final String PATH_TO_MODEL = "path_to_model";
    
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
