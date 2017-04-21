package it.polimi.tracechecking.driver;

import it.polimi.tracechecking.common.ModelLoader;
import it.polimi.tracechecking.common.exception.DIAElementNotFoundException;
import it.polimi.tracechecking.common.model.ComputeNode;
import it.polimi.tracechecking.common.model.DIA;
import it.polimi.tracechecking.common.model.Permission;
import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Launcher {

    private static final Logger logger = LoggerFactory.getLogger(Launcher.class);
    private final DIA dia;
    private List<CheckingClock> cks = new ArrayList<CheckingClock>();
    private Map<ComputeNode, CheckingClock> map = new HashMap<ComputeNode, CheckingClock>();
    private Map<ComputeNode, List<Permission>> ComputeNodePermissionMap = new HashMap<ComputeNode, List<Permission>>();

    public Launcher(String diaString) {
        DIA dia1;
        try {
            dia1 = ModelLoader.loadInputModel(diaString);
            Integer periodInMinutes = dia1.getIntervalBetweenChecks();
            String outputDir = Config.getProperty(Config.PATH_TO_OUTPUT);
            ComputeNodePermissionMap = dia1.getComputeNodesWithPermission();
            for (ComputeNode c : ComputeNodePermissionMap.keySet()) {
                //Prepare dirs for each ComputeNode
                String pathToFormoulae = outputDir + File.separator + c.getId() + File.separator + "formulae";
                String computeNodeDir = outputDir + File.separator + c.getId();
                File formulaeFile = new File(pathToFormoulae);
                formulaeFile.mkdirs();

                for (Permission p : ComputeNodePermissionMap.get(c)) {
                    //Put each formula in a file
                    List<String> lines = Arrays.asList(p.getAsociatedMtlFormula());
                    Files.write(Paths.get(pathToFormoulae + File.separator + "formula" + (ComputeNodePermissionMap.get(c).indexOf(p) + 1)), lines, Charset.forName("UTF-8"));

                }
                CheckingClock cc = new CheckingClock(periodInMinutes, c.getPathToTrace(), pathToFormoulae, computeNodeDir + File.separator + "output");
                map.put(c, cc);
            }
        } catch (IOException e) {
            e.printStackTrace();
            dia1 = null;
        } catch (DIAElementNotFoundException e) {
            e.printStackTrace();
            dia1 = null;
        }
        this.dia = dia1;
    }

    public static void main(String args[]) {
        Config.loadConfig("/home/filippo/IdeaProjects/trace-checking-service/conf/config.properties");
        BasicConfigurator.configure();
        //TODO: read http://stackoverflow.com/questions/12532339/no-appenders-could-be-found-for-loggerlog4j
        //for better configuration :)
        try {

            Launcher l = new Launcher(Utils.readFile("/home/filippo/IdeaProjects/trace-checking-service/conf/model.yml", StandardCharsets.UTF_8));
            System.out.println(l.getResults(l.dia.getComputeNodes().get(0)));
            Thread.sleep(4000);
            //l.cancel();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public Map<String, Boolean> getResults(ComputeNode c) {
        Map<String, Boolean> results = new HashMap<String, Boolean>();
        Integer i = 1;
        for (Permission p : ComputeNodePermissionMap.get(c)) {
            String formula = p.getAsociatedMtlFormula();
            if(map.get(c).getViolations(c, i) == 1)
                results.put(p.getId(), true);
            else
                results.put(p.getId(), false);
            i++;
        }
        return results;
    }

    public void cancel() { //STOP spark job
        for (CheckingClock ck : map.values()) {
            ck.cancel();
        }
    }

    public DIA getDia() {
        return dia;
    }
}