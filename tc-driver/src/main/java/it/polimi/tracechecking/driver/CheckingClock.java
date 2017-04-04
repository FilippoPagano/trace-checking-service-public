package it.polimi.tracechecking.driver;

import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CheckingClock {
    
    private Timer timer;
    private MTLLauncher task;
    private static final Logger journal = LoggerFactory
            .getLogger(CheckingClock.class);

    public CheckingClock(int timeStepDurationInMinutes, String pathToTraces, String formula, String pathToOutput){
        timer = new Timer();
        journal.info("Here is the adaptation clock!");
        task = new MTLLauncher(pathToTraces, formula, pathToOutput);
        timer.scheduleAtFixedRate(task, 1000, timeStepDurationInMinutes*60*1000);
    };
    public void cancel(){
        task.destroy();
        timer.cancel();

        task.cancel();

    }

    class MTLLauncher extends TimerTask{
        private String toControl;
        private String formula;
        private String pathToOutput;
        TraceChecker tc = new TraceChecker();
       public MTLLauncher(String pathToTraces, String formula, String pathToOutput){
           this.toControl = pathToTraces;
           this.formula = formula;
           this.pathToOutput = pathToOutput;
       }
       public void run(){
           System.out.println("Started 1");
           tc.checkTrace(toControl, formula, pathToOutput);

       }
       public void destroy(){
           tc.destroy();
       }

    };

}
