package it.polimi.tracechecking.common.model;

import java.util.List;

public class ComputeNode extends DIANode {

    private ComputeTechType targetTech;
    
    private String owner;
    
    private List<String> inputDatasets;
    
    private List<String> outputDatasets;
    
    private List<String> outputStorages;
    
    public ComputeTechType getTargetTech() {
        return targetTech;
    }

    public void setTargetTech(ComputeTechType targetTech) {
        this.targetTech = targetTech;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public List<String> getInputDatasets() {
        return inputDatasets;
    }

    public void setInputDatasets(List<String> inputDatasets) {
        this.inputDatasets = inputDatasets;
    }

    public List<String> getOutputDatasets() {
        return outputDatasets;
    }

    public void setOutputDatasets(List<String> outputDatasets) {
        this.outputDatasets = outputDatasets;
    }

    public List<String> getOutputStorages() {
        return outputStorages;
    }

    public void setOutputStorages(List<String> outputStorages) {
        this.outputStorages = outputStorages;
    }
    
}
