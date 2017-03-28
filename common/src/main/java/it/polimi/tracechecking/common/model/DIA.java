package it.polimi.tracechecking.common.model;

import java.util.ArrayList;
import java.util.List;

import it.polimi.tracechecking.common.exception.DIAElementNotFoundException;

public class DIA {

    private String diaName;

    private List<DIAElement> elements;

    public DIA() {
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

    public List<ComputeNode> getComputeNodes() {

        List<ComputeNode> toReturn = new ArrayList<ComputeNode>();

        for (DIAElement e : this.elements) {
            if (e instanceof ComputeNode) {
                toReturn.add((ComputeNode) e);
            }
        }

        return toReturn;
    }

    public List<StorageSystem> getStorageSystems() {

        List<StorageSystem> toReturn = new ArrayList<StorageSystem>();

        for (DIAElement e : this.elements) {
            if (e instanceof StorageSystem) {
                toReturn.add((StorageSystem) e);
            }
        }

        return toReturn;
    }

    public List<Dataset> getDatasets() {

        List<Dataset> toReturn = new ArrayList<Dataset>();

        for (DIAElement e : this.elements) {
            if (e instanceof Dataset) {
                toReturn.add((Dataset) e);
            }
        }

        return toReturn;
    }

    public List<Permission> getPermissions() {

        List<Permission> toReturn = new ArrayList<Permission>();

        for (DIAElement e : this.elements) {
            if (e instanceof Permission) {
                toReturn.add((Permission) e);
            }
        }

        return toReturn;
    }

    public Permission getPermission(String permissionId) {
        for (Permission p : this.getPermissions()) {
            if (permissionId.equals(p.getId())) {
                return p;
            }
        }

        return null;
    }

    public List<Permission> getStorageSystemPermissions(StorageSystem element) throws DIAElementNotFoundException {
        
        List<Permission> toReturn = new ArrayList<Permission>();

        for (Permission p : this.getPermissions()) {
            if (this.getDiaElement(p.getProtectedElement()) instanceof Attribute) {
                if (this.getDatasetFromAttribute(p.getProtectedElement()).getProvidedBy().equals(element)) {
                    toReturn.add(p);
                }
            } else if (this.getDiaElement(p.getProtectedElement()) instanceof Dataset) {
                Dataset tmp = (Dataset) this.getDiaElement(p.getProtectedElement());
                if(tmp.getProvidedBy().equals(element)){
                    toReturn.add(p);
                }
            } else if (this.getDiaElement(p.getProtectedElement()) instanceof StorageSystem) {
                StorageSystem tmp = (StorageSystem) this.getDiaElement(p.getProtectedElement());
                if(tmp.equals(element)){
                    toReturn.add(p);
                }
            }
        }
        
        return toReturn;
    }

    public Dataset getDataset(String datasetId) {
        for (Dataset d : this.getDatasets()) {
            if (datasetId.equals(d.getId())) {
                return d;
            }
        }

        return null;
    }

    public ComputeNode getComputeNode(String computeNodeId) {
        for (ComputeNode c : this.getComputeNodes()) {
            if (computeNodeId.equals(c.getId())) {
                return c;
            }
        }

        return null;
    }

    public StorageSystem getStorageSystem(String storageSystemId) {
        for (StorageSystem s : this.getStorageSystems()) {
            if (storageSystemId.equals(s.getId())) {
                return s;
            }
        }

        return null;
    }

    public Dataset getDatasetFromAttribute(String attributeId) {
        for (Dataset d : this.getDatasets()) {
            for (Attribute a : d.getAttributes()) {
                if (a.getId().equals(attributeId)) {
                    return d;
                }
            }
        }

        return null;
    }

    public List<StorageSystem> getStorageSystemWithPermission() throws DIAElementNotFoundException {

        List<StorageSystem> toReturn = new ArrayList<StorageSystem>();

        for (Permission p : this.getPermissions()) {
            if (this.getDiaElement(p.getProtectedElement()) instanceof Attribute) {
                toReturn.add(
                        this.getStorageSystem(this.getDatasetFromAttribute(p.getProtectedElement()).getProvidedBy()));
            } else if (this.getDiaElement(p.getProtectedElement()) instanceof Dataset) {
                toReturn.add(this.getStorageSystem(this.getDataset(p.getProtectedElement()).getProvidedBy()));
            } else if (this.getDiaElement(p.getProtectedElement()) instanceof StorageSystem) {
                toReturn.add(this.getStorageSystem(p.getProtectedElement()));
            }
        }

        return toReturn;
    }
    public List<ComputeNode> getComputeNodesWithPermission() throws DIAElementNotFoundException {

        List<ComputeNode> toReturn = new ArrayList<ComputeNode>();

        for (Permission p : this.getPermissions()) {
            if (this.getDiaElement(p.getUserCluster()) instanceof ComputeNode) {
                toReturn.add(
                        this.getComputeNode(p.getUserCluster()));
            }         }

        return toReturn;
    }

    public DIAElement getDiaElement(String elementId) throws DIAElementNotFoundException {
        for (DIAElement e : this.elements) {
            if (e.getId().equals(elementId))
                return e;
        }

        throw new DIAElementNotFoundException("DIAElement with id: " + elementId + "has not been found.");

    }

}
