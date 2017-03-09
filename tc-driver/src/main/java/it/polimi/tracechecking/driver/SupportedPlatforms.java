package it.polimi.tracechecking.driver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SupportedPlatforms {

    public static final List<String> platformList = Collections.unmodifiableList(new ArrayList<String>() {
        {
            add("Cassandra");
        }
    });
    
    public static Boolean isSupported(String platform){
        return platformList.contains(platform);
    }
}
