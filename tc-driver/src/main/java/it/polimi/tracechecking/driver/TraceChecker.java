package it.polimi.tracechecking.driver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polimi.tracechecking.common.model.Permission;

public class TraceChecker {
    
    public Map<String, Boolean> checkTrace(String pathToEventsFile, List<Permission> permissions){
        Map<String, Boolean> result = new HashMap<String, Boolean>();
        
        for(Permission p: permissions){
            result.put(p.getId(), false);
        }
        return result;
    }

}
