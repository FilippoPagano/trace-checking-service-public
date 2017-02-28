package it.polimi.cassandra.utils.tracechecking;

import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.polimi.cassandra.utils.common.model.Permission;
import it.polimi.cassandra.utils.common.model.StorageSystem;

public class CheckingClock {
    
    private Timer timer;
    private static final Logger journal = LoggerFactory
            .getLogger(CheckingClock.class);
    
    public CheckingClock(int timeStepDurationInMinutes, StorageSystem storage, List<Permission> permissions) {
        timer = new Timer();
        journal.info("Here is the adaptation clock!");
        

        timer.scheduleAtFixedRate(new ControllerLauncher(storage, permissions), 1000, timeStepDurationInMinutes*60*1000);

    }

    class ControllerLauncher extends TimerTask {
        
        private StorageSystem toControl;
        private List<Permission> permissions;
        
        public ControllerLauncher(StorageSystem storage, List<Permission> permissions){
            this.toControl = storage;
            this.permissions = permissions;
        }
       
        public void run() {
                        
            TraceBuilder lc = new TraceBuilder();
            
            String pathToTrace = lc.createTraceCheckingLogFromCassandraTracing(
                    toControl.getClusterContactPoint(),
                    toControl.getClusterContactPort(),
                    toControl.getUserId(),
                    toControl.getPassword());
            
            TraceChecker tc = new TraceChecker();
            
            Map<String, Boolean> checkingResult = tc.checkTrace(pathToTrace, permissions);
    
            for(String permission: checkingResult.keySet())
            {
                if(!checkingResult.get(permission)){
                    System.out.println("Permission " + permission + " violated.");
                }
            }
        }
    }

}
