package it.polimi.tracechecking.common.model;

public class StorageSystem extends DIAElement {

    private StorageSystemType targetTech;
    
	public StorageSystemType getTargetTech() {
        return targetTech;
    }

    public void setTargetTech(StorageSystemType targetTech) {
        this.targetTech = targetTech;
    }

	public StorageSystem(){
		
	}

}
