package it.polimi.tracechecking.service;

import it.polimi.tracechecking.common.model.ComputeNode;
import it.polimi.tracechecking.driver.Config;
import it.polimi.tracechecking.driver.Launcher;
import it.polimi.tracechecking.driver.hdfsLogger;
import org.apache.hadoop.yarn.exceptions.ApplicationNotFoundException;
import org.apache.log4j.BasicConfigurator;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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

    @POST
    @Path("/log")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
    public String logEvent(String eventToLog) {

        hdfsLogger hdfsLogger = new hdfsLogger();
        hdfsLogger.write(eventToLog);
        JSONObject ret = new JSONObject();
        ret.put("result", "ok");
        return ret.toString();
    }

    @POST
    @Path("/loadLogFile")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
    public String logEventFile(String input) {
        JSONObject jsonObj = new JSONObject(input);
        String logFilePath = jsonObj.getString("logFileName");
        String logFileText = jsonObj.getString("logFileText");
        hdfsLogger hdfsLogger = new hdfsLogger();
        hdfsLogger.writeFile(logFilePath, logFileText);
        JSONObject ret = new JSONObject();
        ret.put("result", "ok");
        return ret.toString();
    }

    @GET
    @Path("/{id}/application")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Map<String, Boolean>> getTracingResults(@PathParam("id") String applicationId) {

        Map<String, Map<String, Boolean>> toReturn = new HashMap<String, Map<String, Boolean>>();
        try {
            Launcher diaLauncher = applicationDrivers.get(this.getApplicationById(applicationId));

        for (ComputeNode c : diaLauncher.getDia().getComputeNodes()) {

            toReturn.put(c.getId(), diaLauncher.getResults(c));

        }
        } catch (ApplicationNotFoundException e) {
            log.error(e.getMessage());
            return toReturn;
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
