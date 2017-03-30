package it.polimi.tracechecking.service;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.dropwizard.Configuration;

public class TraceCheckingServiceConfiguration extends Configuration {


    @NotEmpty
    private String propertiesFilePath;

    @JsonProperty("propertiesFilePath")
    public String getPropertiesFilePath() {
        return propertiesFilePath;
    }

    @JsonProperty("propertiesFilePath")
    public void setPropertiesFilePath(String propertiesFilePath) {
        this.propertiesFilePath = propertiesFilePath;
    }

}
