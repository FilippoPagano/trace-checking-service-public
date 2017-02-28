package it.polimi.cassandra.utils.common.model;

public class StorageSystem extends DIANode{
	
    public Boolean getManaged() {
        return managed;
    }

    public void setManaged(Boolean managed) {
        this.managed = managed;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    private StorageSystemType targetTech;

    private Boolean managed;
    
	public StorageSystemType getTargetTech() {
        return targetTech;
    }

    public void setTargetTech(StorageSystemType targetTech) {
        this.targetTech = targetTech;
    }

    private String clusterContactPoint;
    
    private String userId;

    private String password;

	
	public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private Integer clusterContactPort;

	public String getClusterContactPoint() {
		return clusterContactPoint;
	}

	public Integer getClusterContactPort() {
		return clusterContactPort;
	}

	public void setClusterContactPoint(String clusterContactPoint) {
		this.clusterContactPoint = clusterContactPoint;
	}

	public void setClusterContactPort(Integer clusterContactPort) {
		this.clusterContactPort = clusterContactPort;
	}

	public StorageSystem(){
		
	}

}
