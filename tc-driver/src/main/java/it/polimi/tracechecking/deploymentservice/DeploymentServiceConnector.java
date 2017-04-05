package it.polimi.tracechecking.deploymentservice;

import it.polimi.tracechecking.common.model.ComputeNode;
import it.polimi.tracechecking.common.model.DIA;
import it.polimi.tracechecking.common.model.DIAElement;
import it.polimi.tracechecking.common.model.StorageSystem;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DeploymentServiceConnector {

    private static List<VmsCluster> clusters = new ArrayList<VmsCluster>();
    private static String address;
	private static String token;

    public void connect(String contactPoint, Integer port) {
        try {
            address = contactPoint;
            URL obj = new URL(new URL(contactPoint), "auth/get-token/");
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // Setting basic post request
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            String postJsonData = "{\"username\":\"dice\",\"password\":\"qqfTOxUbEnmX55jOBqGC\"}";

            // Send post request
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(postJsonData);
            wr.flush();
            wr.close();

            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + obj.toString());
            System.out.println("Post Data : " + postJsonData);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String output;
            StringBuffer response = new StringBuffer();

            while ((output = in.readLine()) != null) {
                response.append(output);
            }
            in.close();

            //printing result from response
            JSONObject Jobj = new JSONObject(response.toString());
            System.out.println(Jobj.toString(1));
            token = Jobj.getString("token");
        } catch (Exception e) {
			e.printStackTrace();
		}

    }

    public VmsCluster getCluster(String clusterId) {
        try {
            //TODO: waiting for APIs update...

            URL obj = new URL(new URL(address), "containers/" + clusterId + "/nodes");
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            // Setting basic post request
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Authorization", "Token " + token);

            //Sending request
            int responseCode = con.getResponseCode();

            //Reading response
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String output;
            StringBuffer response = new StringBuffer();
            while ((output = in.readLine()) != null) {
                response.append(output);
            }
            in.close();

            //printing result from response
            System.out.println("\nSending 'POST' request to URL : " + obj.toString());
            System.out.println("Response Code : " + responseCode);
            JSONArray Jobj = new JSONArray(response.toString());
            System.out.println(Jobj.toString(1));

        } catch (IOException e) {
            e.printStackTrace();
        }
        VmsCluster cluster = new VmsCluster(clusterId);
        //TODO: set addresses list
        //cluster.setAddresses(List<String> addresses);
        return cluster;
    }

    public List<VmsCluster> getClusters(DIA dia) {
        List<VmsCluster> toReturn = new ArrayList<VmsCluster>();

        for (DIAElement e : dia.getElements()) {
            if (e instanceof ComputeNode || e instanceof StorageSystem) {
                toReturn.add(this.getCluster(e.getId()));
            }
        }

        return toReturn;
    }

    public void close() {

    }
}