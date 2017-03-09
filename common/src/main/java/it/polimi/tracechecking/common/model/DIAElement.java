package it.polimi.tracechecking.common.model;

public abstract class DIAElement {

    private String id;
    
    private String clusterContactPoint;
    
    private Integer clusterContactPort;
    
    private String userId;

    private String password;
    
    private Boolean managed;
    
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
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


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

   
}
