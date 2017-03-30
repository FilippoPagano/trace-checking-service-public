package it.polimi.tracechecking.service;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.polimi.tracechecking.common.ModelLoader;
import it.polimi.tracechecking.common.model.ComputeNode;
import it.polimi.tracechecking.common.model.DIA;
import it.polimi.tracechecking.common.model.Permission;
import it.polimi.tracechecking.driver.CheckingClock;
import it.polimi.tracechecking.driver.Config;

@Path("/")
public class TraceCheckingService {
    static Logger log = LoggerFactory.getLogger(TraceCheckingService.class);

    public TraceCheckingService(String propertiesFile) {
        
        log.info("Starting Trace-Checking-Service");
        
        Config.loadConfig(propertiesFile);
        BasicConfigurator.configure();


    }

    @POST
    @Path("/trace-checking-service/v1/application")
    //@Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
    public void startApplicationTracing(String applicationModel) {
        try {

            DIA dia = ModelLoader.loadInputModelFromFile(Config.getProperty(Config.PATH_TO_MODEL));

            Integer t = Integer.parseInt(Config.getProperty(Config.INTERVAL_BETWEEN_TC_RUN));
            for (Permission p : dia.getPermissions()){
                String formula = p.getAsociatedMtlFormula();
                if (!formula.isEmpty()){
                    ComputeNode c = dia.getComputeNode(p.getUserCluster());
                    new CheckingClock(t,c.getPathToTrace(),formula,Config.getProperty(Config.PATH_TO_OUTPUT));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e.getCause());
        }
    }

}
