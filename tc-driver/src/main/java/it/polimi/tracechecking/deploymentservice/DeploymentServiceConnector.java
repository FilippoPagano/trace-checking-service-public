package it.polimi.tracechecking.deploymentservice;

import java.util.ArrayList;
import java.util.List;

import it.polimi.tracechecking.common.model.ComputeNode;
import it.polimi.tracechecking.common.model.DIA;
import it.polimi.tracechecking.common.model.DIAElement;
import it.polimi.tracechecking.common.model.StorageSystem;

public class DeploymentServiceConnector {
    
    private static List<VmsCluster> clusters = new ArrayList<VmsCluster>();

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
