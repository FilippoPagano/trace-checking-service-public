package it.polimi.tracechecking.driver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.polimi.tracechecking.common.model.DIA;

import java.util.Timer;
import java.util.TimerTask;

public class CheckingClock {
    
    private static final Logger journal = LoggerFactory
            .getLogger(CheckingClock.class);
    private Timer timer;
    private MTLLauncher task;
    private TraceChecker tc = new TraceChecker();

    public CheckingClock(int timeStepDurationInMinutes, String pathToTraces, String formula, String pathToOutput){
        timer = new Timer();
        journal.info("Here is the checking clock!");
        task = new MTLLauncher(pathToTraces, formula, pathToOutput);
        timer.scheduleAtFixedRate(task, 1000, timeStepDurationInMinutes*60*1000);
    }

    public void cancel(){
        task.destroy();
        timer.cancel();
        task.cancel();

    }


    public Integer getViolations(DIA c, Integer formulaIndex) {
        return TraceChecker.getViolations(c, formulaIndex);
    }

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
           TraceChecker.checkTrace(toControl, formula, pathToOutput);
           System.out.println("Started 1");
       }
       public void destroy(){
           tc.destroy();
       }

    }

}
