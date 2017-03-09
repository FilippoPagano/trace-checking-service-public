package it.polimi.tracechecking.common.model;

import java.util.ArrayList;
import java.util.List;

public class DIA {
    
    private String diaName;
    
    private List<DIAElement> elements;
    
    public DIA(){
    }

    public String getDiaName() {
        return diaName;
    }

    public void setDiaName(String diaName) {
        this.diaName = diaName;
    }

    public List<DIAElement> getElements() {
        return elements;
    }

    public void setElements(List<DIAElement> elements) {
        this.elements = elements;
    }
    
    public List<ComputeNode> getComputeNodes(){
        
        List<ComputeNode> toReturn = new ArrayList<ComputeNode>();
        
        for(DIAElement e: this.elements){
            if(e instanceof ComputeNode){
                toReturn.add((ComputeNode) e);
            }
        }
        
        return toReturn;
    }
    
    public List<StorageSystem> getStorageSystems(){
        
        List<StorageSystem> toReturn = new ArrayList<StorageSystem>();
        
        for(DIAElement e: this.elements){
            if(e instanceof StorageSystem){
                toReturn.add((StorageSystem) e);
            }
        }
        
        return toReturn;
    }
    
    public List<Dataset> getDatasets(){
        
        List<Dataset> toReturn = new ArrayList<Dataset>();
        
        for(DIAElement e: this.elements){
            if(e instanceof Dataset){
                toReturn.add((Dataset) e);
            }
        }
        
        return toReturn;
    }
    
    public List<Permission> getPermissions(){
        
        List<Permission> toReturn = new ArrayList<Permission>();
        
        for(DIAElement e: this.elements){
            if(e instanceof Permission){
                toReturn.add((Permission) e);
            }
        }
        
        return toReturn;
    }
    
    public Permission getPermission(String permissionId){
        for(Permission p: this.getPermissions()){
            if(permissionId.equals(p.getId())){
                return p;
            }
        }
        
        return null;
    }
    
    public Dataset getDataset(String datasetId){
        for(Dataset d: this.getDatasets()){
            if(datasetId.equals(d.getId())){
                return d;
            }
        }
        
        return null;
    }
    
    public ComputeNode getComputeNode(String computeNodeId){
        for(ComputeNode c: this.getComputeNodes()){
            if(computeNodeId.equals(c.getId())){
                return c;
            }
        }
        
        return null;
    }
    
    public StorageSystem getStorageSystem(String storageSystemId){
        for(StorageSystem s: this.getStorageSystems()){
            if(storageSystemId.equals(s.getId())){
                return s;
            }
        }
        
        return null;
    }
    
    public Dataset getDatasetFromAttribute(String attributeId){
        for(Dataset d: this.getDatasets()){
            for(Attribute a: d.getAttributes()){
                if(a.getId().equals(attributeId)){
                    return d;
                }
            }
        }
        
        return null;
    }
    
    public List<StorageSystem> getStorageSystemWithPermission(){
        
        List<StorageSystem> toReturn = new ArrayList<StorageSystem>();
        
        for(Permission p: this.getPermissions()){
            if(p.getProtectionLevel().equals("attribute")){
                toReturn.add(this.getStorageSystem(this.getDatasetFromAttribute(p.getProtectedElement()).getProvidedBy()));
            }else{
                toReturn.add(this.getStorageSystem(this.getDataset(p.getProtectedElement()).getProvidedBy()));
            }
        }
        
        return toReturn;
    }
    

}
