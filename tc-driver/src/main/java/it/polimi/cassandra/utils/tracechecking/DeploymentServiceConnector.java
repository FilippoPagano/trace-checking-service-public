package it.polimi.cassandra.utils.tracechecking;

import java.util.ArrayList;
import java.util.List;

import it.polimi.cassandra.utils.common.model.ComputeNode;
import it.polimi.cassandra.utils.common.model.DIA;
import it.polimi.cassandra.utils.common.model.DIAElement;
import it.polimi.cassandra.utils.common.model.StorageSystem;
import model.VmsCluster;

public class DeploymentServiceConnector {
    
    public void connect(String contactPoint, Integer port){
        
    }
    
    public VmsCluster getCluster(String clusterId){
        return null;
    }
    
    public List<VmsCluster> getClusters(DIA dia){
        List<VmsCluster> toReturn = new ArrayList<VmsCluster>();
        
        for(DIAElement e: dia.getElements()){
            if(e instanceof ComputeNode || e instanceof StorageSystem){
                toReturn.add(this.getCluster(e.getId()));
            }
        }
                
        return toReturn;
    }
    
    public void close() {
        
    }
}
