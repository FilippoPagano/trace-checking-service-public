package it.polimi.cassandra.utils.common.model;

import java.util.Date;

public class Permission extends DIAElement  {
    
    private String mtlProperty;
    
    public String getMtlProperty() {
        return mtlProperty;
    }

    public void setMtlProperty(String mtlProperty) {
        this.mtlProperty = mtlProperty;
    }

    public String getUserCluster() {
        return userCluster;
    }

    public void setUserCluster(String userCluster) {
        this.userCluster = userCluster;
    }

    private String protectedElement;
    
    private String userCluster;
    
    private String protectionLevel;
    
    public String getProtectionLevel() {
        return protectionLevel;
    }

    public void setProtectionLevel(String protectionLevel) {
        this.protectionLevel = protectionLevel;
    }

    private ActionType actionType;
    
    public String getProtectedElement() {
        return protectedElement;
    }

    public void setProtectedElement(String protectedElement) {
        this.protectedElement = protectedElement;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

    public Date getValidityStartTime() {
        return validityStartTime;
    }

    public void setValidityStartTime(Date validityStartTime) {
        this.validityStartTime = validityStartTime;
    }

    public Date getValidityEndTime() {
        return validityEndTime;
    }

    public void setValidityEndTime(Date validityEndTime) {
        this.validityEndTime = validityEndTime;
    }

    private Date validityStartTime;
    
    private Date validityEndTime;

}
