package it.polimi.tracechecking.driver;

import java.io.IOException;
import java.util.List;

import it.polimi.tracechecking.common.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.polimi.tracechecking.common.ModelLoader;
import it.polimi.tracechecking.deploymentservice.DeploymentServiceConnector;
import it.polimi.tracechecking.common.exception.DIAElementNotFoundException;
import org.tukaani.xz.check.Check;

public class Launcher {

    private static final Logger logger = LoggerFactory.getLogger(Launcher.class);



    public static void main(String args[]) {
        Config.loadConfig("/home/filippo/IdeaProjects/trace-checking-service/conf/config.properties");

        try {

            DIA dia = ModelLoader.loadInputModelFromFile(Config.getProperty(Config.PATH_TO_MODEL));
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
            Integer t = Integer.parseInt(Config.getProperty(Config.INTERVAL_BETWEEN_TC_RUN));
            for (Permission p : dia.getPermissions()){
                String formula = p.getAsociatedMtlFormula();
                if (!formula.isEmpty()){
                    ComputeNode c = dia.getComputeNode(p.getUserCluster());
                    new CheckingClock(t,c.getPathToTrace(),formula);
                }
            }
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
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e.getCause());
        }
    }

}
