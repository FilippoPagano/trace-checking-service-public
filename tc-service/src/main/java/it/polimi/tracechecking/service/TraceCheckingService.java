package it.polimi.tracechecking.service;

import it.polimi.tracechecking.common.model.DIA;
import it.polimi.tracechecking.driver.Config;
import it.polimi.tracechecking.driver.Launcher;
import it.polimi.tracechecking.driver.hdfsLogger;
import org.apache.hadoop.yarn.exceptions.ApplicationNotFoundException;
import org.apache.log4j.BasicConfigurator;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.*;

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
    @Path("/{id}/loadLogFile")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
    public String logEventFile(@PathParam("id") String applicationId, String input) {
        JSONObject jsonObj = new JSONObject(input);
        String logFileText = jsonObj.getString("logFileText");
        hdfsLogger.writeNewFile(applicationId, logFileText);
        JSONObject ret = new JSONObject();
        ret.put("result", "ok");
        return ret.toString();
    }

    @POST
    @Path("/{id}/log")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
    public String logEvent(@PathParam("id") String applicationId, String input) {
        JSONObject jsonObj = new JSONObject(input);
        String eventToLog = jsonObj.getString("eventToLog");
        hdfsLogger.write(applicationId, eventToLog);
        JSONObject ret = new JSONObject();
        ret.put("result", "ok");
        return ret.toString();
    }

    @POST
    @Path("/{id}/logMessage")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
    public String logMessage(@PathParam("id") String applicationId, String input) {
        JSONObject jsonObj = new JSONObject(input);
        String messageID = jsonObj.getString("messageID");
        String sender = jsonObj.getString("sender");
        String senderRole = jsonObj.getString("senderRole");
        String receiver = jsonObj.getString("receiver");
        String receiverRole = jsonObj.getString("receiverRole");
        String messageSubject = jsonObj.getString("messageSubject");
        String messageSubjectRole = jsonObj.getString("messageSubjectRole");
        List<String> formulae = new ArrayList<String>();
        JSONArray jsonMainArr = jsonObj.getJSONArray("formulae");

        String eventToLog = "message(" + messageID +
                "); in_role(" + sender + "," + senderRole +
                "); in_role(" + receiver + "," + receiverRole +
                "); in_role(" + messageSubject + "," + messageSubjectRole;
        if (jsonMainArr != null) {
            for (int i = 0; i < jsonMainArr.length(); i++) {
                eventToLog = eventToLog + "); contains(" + messageID + ", " + messageSubject + ", " + jsonMainArr.getString(i);
            }
        }
        /*for (String formula : jsonMainArr.toList())
            eventToLog = eventToLog + "); contains(" + messageID + "" + messageSubject+ formula;*/
        eventToLog = eventToLog + "); send (" + sender + ", " + receiver + ", " + messageID + ")";
        hdfsLogger.write(applicationId, eventToLog);
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

        for (DIA c : diaLauncher.getModel().getDIAs()) {

            toReturn.put(c.getDiaName(), diaLauncher.getResults(c));

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

        throw new ApplicationNotFoundException("Application with id " + appId + " is currently not registered.");
    }

}
