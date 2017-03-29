package it.polimi.tracechecking.driver;

import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import it.polimi.tracechecking.common.model.ComputeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.polimi.tracechecking.common.model.DIAElement;
import it.polimi.tracechecking.common.model.Permission;
import it.polimi.tracechecking.common.model.StorageSystem;

public class CheckingClock {
    
    private Timer timer;
    private static final Logger journal = LoggerFactory
            .getLogger(CheckingClock.class);

    public CheckingClock(int timeStepDurationInMinutes, String pathToTraces, String formula, String pathToOutput){
        timer = new Timer();
        journal.info("Here is the adaptation clock!");

        timer.scheduleAtFixedRate(new MTLLauncher(pathToTraces, formula, pathToOutput), 1000, timeStepDurationInMinutes*60*1000);
    };
    class MTLLauncher extends TimerTask{
        private String toControl;
        private String formula;
        private String pathToOutput;
       public MTLLauncher(String pathToTraces, String formula, String pathToOutput){
           this.toControl = pathToTraces;
           this.formula = formula;
           this.pathToOutput = pathToOutput;
       }
       public void run(){
           System.out.println("Started 1");
           TraceChecker tc = new TraceChecker();
           tc.checkTrace(toControl, formula, pathToOutput);

       }
    };

}
