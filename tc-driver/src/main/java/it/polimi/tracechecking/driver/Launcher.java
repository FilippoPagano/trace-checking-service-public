package it.polimi.tracechecking.driver;

import it.polimi.tracechecking.common.ModelLoader;
import it.polimi.tracechecking.common.model.ComputeNode;
import it.polimi.tracechecking.common.model.DIA;
import it.polimi.tracechecking.common.model.Permission;
import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Launcher {

    private static final Logger logger = LoggerFactory.getLogger(Launcher.class);
    private List<CheckingClock> cks = new ArrayList<CheckingClock>();
    private DIA dia;
    private Map<ComputeNode, CheckingClock> map = new HashMap<ComputeNode, CheckingClock>();
    private Map<ComputeNode, List<Permission>> ComputeNodePermissionMap = new HashMap<ComputeNode, List<Permission>>();

    private Launcher(DIA dia) {

        try {
            if (this.dia == null) this.dia = dia;
            Integer periodInMinutes = Integer.parseInt(Config.getProperty(Config.INTERVAL_BETWEEN_TC_RUN));
            String outputDir = Config.getProperty(Config.PATH_TO_OUTPUT);
            ComputeNodePermissionMap = dia.getComputeNodesWithPermission();
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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Launcher CreateLauncher() {
        try {
            return new Launcher(ModelLoader.loadInputModelFromFile(Config.getProperty(Config.PATH_TO_MODEL)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Launcher CreateLauncher(String dia) {
        try {
            return new Launcher(ModelLoader.loadInputModel(dia));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String args[]) {
        Config.loadConfig("/home/filippo/IdeaProjects/trace-checking-service/conf/config.properties");
        BasicConfigurator.configure();
        //TODO: read http://stackoverflow.com/questions/12532339/no-appenders-could-be-found-for-loggerlog4j
        //for better configuration :)

        Launcher l = CreateLauncher();
        try {
            if (l.dia == null) l.dia = ModelLoader.loadInputModelFromFile(Config.getProperty(Config.PATH_TO_MODEL));
            System.out.println(l.getResults(l.dia.getComputeNodes().get(0)));
            Thread.sleep(4000);
            l.cancel();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public Map<String, Integer> getResults(ComputeNode c) { //GET # violations for every formula
        Map<String, Integer> results = new HashMap<String, Integer>();
        Integer i = 1;
        for (Permission p : ComputeNodePermissionMap.get(c)) {
            String formula = p.getAsociatedMtlFormula();
            results.put(formula, map.get(c).getViolations(c, i));
            i++;
        }
        return results;
    }

    public void cancel() { //STOP spark job
        for (CheckingClock ck : map.values()) {
            ck.cancel();
        }
    }
}