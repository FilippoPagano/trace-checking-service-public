package it.polimi.cassandra.utils.common.model;

import java.util.ArrayList;
import java.util.List;

public class StorageSystem extends DIANode{
	
    private StorageSystemType targetTech;

    private Boolean managed;
    
	public StorageSystemType getTargetTech() {
        return targetTech;
    }

    public void setTargetTech(StorageSystemType targetTech) {
        this.targetTech = targetTech;
    }

    private String cassandraClusterContactPoint;
	
	private Integer cassandraClusterContactPort;

	public String getCassandraClusterContactPoint() {
		return cassandraClusterContactPoint;
	}

	public Integer getCassandraClusterContactPort() {
		return cassandraClusterContactPort;
	}

	public void setCassandraClusterContactPoint(String cassandraClusterContactPoint) {
		this.cassandraClusterContactPoint = cassandraClusterContactPoint;
	}

	public void setCassandraClusterContactPort(Integer cassandraClusterContactPort) {
		this.cassandraClusterContactPort = cassandraClusterContactPort;
	}

	public StorageSystem(){
		
	}

}
