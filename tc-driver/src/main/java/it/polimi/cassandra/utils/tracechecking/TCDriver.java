package it.polimi.cassandra.utils.tracechecking;

import java.io.IOException;
import java.util.List;
import it.polimi.cassandra.utils.common.ModelLoader;
import it.polimi.cassandra.utils.common.model.DIA;
import it.polimi.cassandra.utils.common.model.StorageSystem;

public class TCDriver {

    public static void main(String args[]){
        Config.loadConfig("/Users/michele/workspace/cassandra-utils/conf/config.properties");

        try {
            DIA dia = ModelLoader.loadInputModelFromFile(Config.getProperty(Config.PATH_TO_MODEL));

            DeploymentServiceConnector ds = new DeploymentServiceConnector();
            ds.connect("127.0.0.1", 8081);
            List<VmsCluster> clusters = ds.getClusters(dia);
            ds.close();
            
            // in questo prototipo lanciamo direttamente un tcdriver per il nodo due in quanto assumiamo che
            // sia un nodo cassandra e che esista un permesso su questo nodo. Ovviamente va generalizzato
            StorageSystem cassandra = (StorageSystem) dia.getElements().get(0);
            
            for(StorageSystem s: dia.getStorageSystemWithPermission()){
                System.out.println(s.getId());
                new CheckingClock(Integer.parseInt(Config.getProperty(Config.INTERVAL_BETWEEN_TC_RUN)), s, dia.getPermissions());
            }

        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e.getCause());
        }
    }

}
