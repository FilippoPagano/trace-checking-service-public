package it.polimi.tracechecking.initializer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

import it.polimi.tracechecking.common.ModelLoader;
import it.polimi.tracechecking.common.model.DIA;
import it.polimi.tracechecking.common.model.DIAElement;
import it.polimi.tracechecking.common.model.Dataset;

public class CassandraInitializer {

    private static final Logger logger = LoggerFactory.getLogger(CassandraInitializer.class);

    public static void main(String[] args) throws IOException {
        CassandraDriver cassandra = new CassandraDriver();
        DIA dia = ModelLoader
                .loadInputModelFromFile("/Users/michele/workspace/cassandra-utils/conf/model.yml");
        Cluster cluster;

        Session session;

        logger.info("127.0.0.1" + " " + 9042);
        cluster = Cluster.builder().addContactPoint("127.0.0.1").withPort(9042).withCredentials("mik", "polimi")
                .build();

        logger.info("Connected to cluster: {}", cluster.getMetadata().getClusterName());
        logger.info("Connected to cluster: {}", cluster.getMetadata().getClusterName());

        session = cluster.connect();

        /*
         * for (DIAElement table : dia.getElements()) { if (table instanceof
         * Dataset) { Dataset s = (Dataset) table;
         * session.execute("CREATE KEYSPACE IF NOT EXISTS " + s.getId() +
         * "_keyspace" + " WITH replication " +
         * "= {'class':'SimpleStrategy', 'replication_factor':" +
         * s.getReplicationFactor() + "};");
         * 
         * System.out.println(session.toString());
         * 
         * String createTableStatement = "CREATE TABLE IF NOT EXISTS " +
         * s.getId() + "_keyspace." + s.getId() + " (";
         * 
         * Iterator<Attribute> attrsIter = s.getAttributes().iterator();
         * Attribute attr;
         * 
         * while (attrsIter.hasNext()) { attr = attrsIter.next();
         * 
         * createTableStatement = createTableStatement.concat(attr.getId() + " "
         * + attr.getType());
         * 
         * if (attr.getIsKey()) { createTableStatement =
         * createTableStatement.concat(" PRIMARY KEY"); }
         * 
         * if (attrsIter.hasNext()) { createTableStatement =
         * createTableStatement.concat(","); } }
         * 
         * createTableStatement = createTableStatement.concat(");");
         * 
         * session.execute(createTableStatement);
         * 
         * session.close(); cluster.close();
         * 
         * } }
         * 
         */

        String insertStatement;
        String line = "";
        String cvsSplitBy = ",";
        for (DIAElement table : dia.getElements()) {
            if (table instanceof Dataset) {
                Dataset s = (Dataset) table;
                try (BufferedReader br = new BufferedReader(
                        new FileReader("/Users/michele/workspace/cassandra-utils/conf/sampleMedicalData.csv"))) {

                    while ((line = br.readLine()) != null) {

                        // use comma as separator
                        String[] row = line.split(cvsSplitBy);

                        insertStatement = "INSERT INTO " + s.getId() + "_keyspace." + s.getId() + " (";

                        Iterator<String> attrsIter = s.getAttributeNames().iterator();

                        while (attrsIter.hasNext()) {
                            insertStatement = insertStatement.concat(attrsIter.next());

                            if (attrsIter.hasNext()) {
                                insertStatement = insertStatement.concat(",");
                            }
                        }

                        insertStatement = insertStatement.concat(") " + "VALUES (");

                        for (int j = 0; j < row.length; j++) {
                            if (j < row.length - 1) {
                                if (s.getAttributes().get(j).getType().equals("text")) {
                                    insertStatement = insertStatement.concat("'" + row[j] + "'" + ",");
                                } else {
                                    insertStatement = insertStatement.concat(row[j] + ",");
                                }
                            } else {
                                if (s.getAttributes().get(j).getType().equals("text")) {
                                    insertStatement = insertStatement.concat("'" + row[j] + "'" + ");");
                                } else {
                                    insertStatement = insertStatement.concat(row[j] + ");");
                                }
                            }
                        }
                        session.execute(insertStatement);
                        
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        
        session.close(); cluster.close();

    }
}
