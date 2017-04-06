package it.polimi.tracechecking.service;

public class Application {

    private String applicationId;

    private ApplicationStatus status;

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    public Application(String id) {
        this.applicationId = id;
        this.status = ApplicationStatus.ACTIVE;
    }

    public Application(String id, ApplicationStatus status) {
        this.applicationId = id;
        this.status = status;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

}