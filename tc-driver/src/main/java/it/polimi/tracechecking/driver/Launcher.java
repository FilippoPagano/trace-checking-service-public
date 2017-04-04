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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Launcher {

    private static final Logger logger = LoggerFactory.getLogger(Launcher.class);
    private static List<CheckingClock> cks = new ArrayList<CheckingClock>();
    private static DIA dia;
    private Map<ComputeNode, CheckingClock> map = new HashMap<ComputeNode, CheckingClock>();
    private Map<ComputeNode, List<Permission>> ComputeNodePermissionMap = new HashMap<ComputeNode, List<Permission>>();

    public Launcher(String App) {
        try {
            if (dia == null) dia = ModelLoader.loadInputModel(App);
            Integer t = Integer.parseInt(Config.getProperty(Config.INTERVAL_BETWEEN_TC_RUN));
            String outputDir = Config.getProperty(Config.PATH_TO_OUTPUT);
            ComputeNodePermissionMap = dia.getComputeNodesWithPermission();
            for (ComputeNode c : ComputeNodePermissionMap.keySet()) {
                String pathToFormoulae = outputDir + File.separator + c.getId() + File.separator + "formulae";
                String computeNodeDir = outputDir + File.separator + c.getId();
                File theDir = new File(pathToFormoulae);
                theDir.mkdirs();

                for (Permission p : ComputeNodePermissionMap.get(c)
                        ) {
                    List<String> lines = Arrays.asList(p.getAsociatedMtlFormula());
                    Files.write(Paths.get(pathToFormoulae + File.separator + "formula" + (ComputeNodePermissionMap.get(c).indexOf(p) + 1)), lines, Charset.forName("UTF-8"));

                }
                CheckingClock cc = new CheckingClock(t, c.getPathToTrace(), pathToFormoulae, computeNodeDir + File.separator + "output");
                map.put(c, cc);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (DIAElementNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Launcher() {
        try {
            if (dia == null) dia = ModelLoader.loadInputModelFromFile(Config.getProperty(Config.PATH_TO_MODEL));
            Integer t = Integer.parseInt(Config.getProperty(Config.INTERVAL_BETWEEN_TC_RUN));
            String outputDir = Config.getProperty(Config.PATH_TO_OUTPUT);
            ComputeNodePermissionMap = dia.getComputeNodesWithPermission();
            for (ComputeNode c : ComputeNodePermissionMap.keySet()) {
                String pathToFormoulae = outputDir + File.separator + c.getId() + File.separator + "formulae";
                String computeNodeDir = outputDir + File.separator + c.getId();
                File theDir = new File(pathToFormoulae);
                theDir.mkdirs();

                for (Permission p : ComputeNodePermissionMap.get(c)
                        ) {
                    List<String> lines = Arrays.asList(p.getAsociatedMtlFormula());
                    Files.write(Paths.get(pathToFormoulae + File.separator + "formula" + (ComputeNodePermissionMap.get(c).indexOf(p) + 1)), lines, Charset.forName("UTF-8"));

                }
                CheckingClock cc = new CheckingClock(t, c.getPathToTrace(), pathToFormoulae, computeNodeDir + File.separator + "output");
                map.put(c, cc);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (DIAElementNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {
        Config.loadConfig("/home/filippo/IdeaProjects/trace-checking-service/conf/config.properties");
        BasicConfigurator.configure();
        //TODO: read http://stackoverflow.com/questions/12532339/no-appenders-could-be-found-for-loggerlog4j
        //for better configuration :)

        Launcher l = new Launcher();
        try {
            if (dia == null) dia = ModelLoader.loadInputModelFromFile(Config.getProperty(Config.PATH_TO_MODEL));
            System.out.println(l.getResults(dia.getComputeNodes().get(0)));
            l.cancel();
        } catch (IOException e) {
            e.printStackTrace();
        }

/*
            DIA dia = ModelLoader.loadInputModelFromFile(Config.getProperty(Config.PATH_TO_MODEL));
            Integer t = Integer.parseInt(Config.getProperty(Config.INTERVAL_BETWEEN_TC_RUN));
            String outputDir = Config.getProperty(Config.PATH_TO_OUTPUT);
            for (Permission p : dia.getPermissions()){
                String formula = p.getAsociatedMtlFormula();
                if (!formula.isEmpty()){
                    ComputeNode c = dia.getComputeNode(p.getUserCluster());
                    CheckingClock cc = new CheckingClock(t,c.getPathToTrace(),formula,outputDir);
                   cks.add(cc);
                }
            }
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (CheckingClock cc : cks) {
                System.out.println(cc.getViolations());
                cc.cancel();

            }

 /*
            dia.getComputeNodes().get(0).getPathToTrace();
            dia.getPermissions().get(0);
            StorageSystem storage = new StorageSystem();
            dia.getStorageSystem("");
            new CheckingClock(Integer.parseInt(Config.getProperty(Config.INTERVAL_BETWEEN_TC_RUN)),
                    storage,
                    dia.getPermissions());
           /* for  (ComputeNode c : dia.getComputeNodes()){

            }*/

            // TO-DO: add initialization and launching of
            // DeploymentServiceConnector

            /*
             * DeploymentServiceConnector ds = new DeploymentServiceConnector();
             * ds.connect("127.0.0.1", 8081); List<VmsCluster> clusters =
             * ds.getClusters(dia); ds.close();
             */
/*
            try {
                for (StorageSystem s : dia.getStorageSystemWithPermission()) {
                    if(s.getManaged()){
                        if(s.getTargetTech().equals(StorageSystemType.CASSANDRA)) {
                            new CheckingClock(Integer.parseInt(Config.getProperty(Config.INTERVAL_BETWEEN_TC_RUN)), s,
                                    dia.getStorageSystemPermissions(s));
                        }
                        else if(!s.getTargetTech().equals(StorageSystemType.CASSANDRA)){
                            logger.info("Storage platform " + s.getTargetTech() + "is currently not supported");
                        }
                    }
                }
            } catch (NumberFormatException | DIAElementNotFoundException e) {
                logger.error(e.getMessage());
                throw new IllegalStateException(e.getMessage(), e.getCause());
            }
            */

    }

    public Map<String, Integer> getResults(ComputeNode c) {
        Map<String, Integer> results = new HashMap<String, Integer>();
        Integer i = 1;
        for (Permission p : ComputeNodePermissionMap.get(c)) {
            String formula = p.getAsociatedMtlFormula();
            results.put(formula, map.get(c).getViolations(c, i));
            i++;
        }
        return results;
    }

    public void cancel() {
        for (CheckingClock ck : map.values()) {
            ck.cancel();
        }
    }

}
