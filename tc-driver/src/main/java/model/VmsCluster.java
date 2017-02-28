package model;

import java.util.ArrayList;
import java.util.List;

public class VmsCluster {
    
    private String clusterId;
    
    private List<String> addresses;
    
    public VmsCluster(String clusterId){
        this.clusterId = clusterId;
        this.addresses = new ArrayList<String>();
    }

    public String getClusterId() {
        return clusterId;
    }

    public void setClusterId(String clusterId) {
        this.clusterId = clusterId;
    }

    public List<String> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<String> addresses) {
        this.addresses = addresses;
    }
    
    public void addVm(String vmAddress){
        this.addresses.add(vmAddress);
    }

}
