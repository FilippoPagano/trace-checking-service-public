package it.polimi.tracechecking.driver;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.polimi.tracechecking.common.ModelLoader;
import it.polimi.tracechecking.common.model.DIA;
import it.polimi.tracechecking.common.model.DIAElement;
import it.polimi.tracechecking.common.model.StorageSystem;
import it.polimi.tracechecking.common.model.StorageSystemType;
import it.polimi.tracechecking.deploymentservice.DeploymentServiceConnector;
import it.polimi.tracechecking.common.exception.DIAElementNotFoundException;

public class Launcher {

    private static final Logger logger = LoggerFactory.getLogger(Launcher.class);

    public static void main(String args[]) {
        Config.loadConfig("/Users/michele/workspace/cassandra-utils/conf/config.properties");

        try {
            DIA dia = ModelLoader.loadInputModelFromFile(Config.getProperty(Config.PATH_TO_MODEL));

            // TO-DO: add initialization and launching of
            // DeploymentServiceConnector

            /*
             * DeploymentServiceConnector ds = new DeploymentServiceConnector();
             * ds.connect("127.0.0.1", 8081); List<VmsCluster> clusters =
             * ds.getClusters(dia); ds.close();
             */

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

        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e.getCause());
        }
    }

}
