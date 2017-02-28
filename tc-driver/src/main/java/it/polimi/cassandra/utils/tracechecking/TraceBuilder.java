package it.polimi.cassandra.utils.tracechecking;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;

import it.polimi.cassandra.utils.common.CassandraDriver;

public class TraceBuilder {

    public static void main(String[] args) {

    }

    public String createTraceCheckingLogFromCassandraTracing(String cassandraClusterContactPoint,
            Integer cassandraClusterContactPort, String user, String password) {
        CassandraDriver driver = new CassandraDriver();

        driver.connect(cassandraClusterContactPoint, cassandraClusterContactPort, user, password);

        ResultSet traces = driver.retrieveCassandraTable("system_traces", "sessions");
        
        driver.close();
        FileWriter fw;
        Map<String, String> parameters;
        String path = "cassandra_traces.his";
        
        try {
            fw = new FileWriter(path);
            for (Row r : traces) {
                parameters = r.getMap("parameters", String.class, String.class);
                if(parameters.get("query") != null){
                    if (parameters.get("query").contains("INSERT")) {
                        fw.write(r.getTimestamp("started_at").getTime() + " ; " + r.getInet("client").getHostAddress()
                                + "_write\n");
                    } else if (parameters.get("query").contains("SELECT")) {
                        fw.write(r.getTimestamp("started_at").getTime() + " ; " + r.getInet("client").getHostAddress()
                                + "_read\n");
                    }   
                }
            }
            fw.close();
            
            return path;
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e.getCause());
        }
        
    }
}
