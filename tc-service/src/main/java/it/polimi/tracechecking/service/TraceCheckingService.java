package it.polimi.tracechecking.service;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.hadoop.yarn.exceptions.ApplicationNotFoundException;
import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.polimi.tracechecking.common.model.ComputeNode;
import it.polimi.tracechecking.driver.Config;
import it.polimi.tracechecking.driver.Launcher;;

@Path("/trace-checking-service")
public class TraceCheckingService {
    static Logger log = LoggerFactory.getLogger(TraceCheckingService.class);

    private Map<Application, Launcher> applicationDrivers;

    public TraceCheckingService(String propertiesFile) {

        log.info("Starting Trace-Checking-Service");

        Config.loadConfig(propertiesFile);
        BasicConfigurator.configure();
        this.applicationDrivers = new HashMap<Application, Launcher>();

    }

    @POST
    @Path("/application")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public Application startTracing(String applicationModel) {

        Launcher diaDriver = new Launcher(applicationModel);

        Application app = new Application(UUID.randomUUID().toString());

        applicationDrivers.put(app, diaDriver);

        return app;

    }

    @GET
    @Path("/{id}/application")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Map<String, Integer>> getTracingResults(@PathParam("id") String applicationId) {

        Map<String, Map<String, Integer>> toReturn = new HashMap<String, Map<String, Integer>>();

        Launcher diaLauncher = applicationDrivers.get(applicationId);

        for (ComputeNode c : diaLauncher.getDia().getComputeNodes()) {

            toReturn.put(c.getId(), diaLauncher.getResults(c));

        }

        return toReturn;
    }

    @DELETE
    @Path("/{id}/application")
    @Produces(MediaType.APPLICATION_JSON)
    public Application stopTracing(@PathParam("id") String applicationId) {

        Application toStop;

        try {
            toStop = this.getApplicationById(applicationId);
            applicationDrivers.get(toStop).cancel();
            // applicationDrivers.remove(toStop);
            toStop.setStatus(ApplicationStatus.STOPPED);
            return toStop;
        } catch (ApplicationNotFoundException e) {
            log.error(e.getMessage());
            return new Application(applicationId, ApplicationStatus.NOT_FOUND);
        }

    }

    private Application getApplicationById(String appId) throws ApplicationNotFoundException {

        for (Application app : this.applicationDrivers.keySet()) {
            if (app.getApplicationId().equals(appId))
                return app;
        }

        throw new ApplicationNotFoundException("Application with id " + appId + " is currently not registerd.");
    }

}
