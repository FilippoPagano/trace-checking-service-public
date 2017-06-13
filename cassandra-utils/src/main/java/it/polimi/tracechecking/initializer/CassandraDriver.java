package it.polimi.tracechecking.initializer;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

public class CassandraDriver {

    private static final Logger logger = LoggerFactory.getLogger(CassandraDriver.class);

    private Cluster cluster;

    private Session session;

    public void connect(String contactPoint, Integer port, String user, String password) {

        logger.info(contactPoint + " " + port);
        cluster = Cluster.builder().addContactPoint(contactPoint).withPort(port.intValue())
                .withCredentials(user, password).build();

        logger.info("Connected to cluster: {}", cluster.getMetadata().getClusterName());
        logger.info("Connected to cluster: {}", cluster.getMetadata().getClusterName());

        session = cluster.connect();
    }

    public void createSchema(Dataset table) {

        session.execute("CREATE KEYSPACE IF NOT EXISTS " + table.getId() + "_keyspace" + " WITH replication "
                + "= {'class':'SimpleStrategy', 'replication_factor':" + table.getReplicationFactor() + "};");

        String createTableStatement = "CREATE TABLE IF NOT EXISTS " + table.getId() + "_keyspace." + table.getId()
                + " (";

        Iterator<Attribute> attrsIter = table.getAttributes().iterator();
        Attribute attr;

        while (attrsIter.hasNext()) {
            attr = attrsIter.next();

            createTableStatement = createTableStatement.concat(attr.getId() + " " + attr.getType());

            if (attr.getIsKey()) {
                createTableStatement = createTableStatement.concat(" PRIMARY KEY");
            }

            if (attrsIter.hasNext()) {
                createTableStatement = createTableStatement.concat(",");
            }
        }

        createTableStatement = createTableStatement.concat(");");

        session.execute(createTableStatement);

    }

    public void loadData(String keyspace, Dataset table, String csvDataFile) {
        String insertStatement;
        String line = "";
        String cvsSplitBy = ",";

        try (BufferedReader br = new BufferedReader(new FileReader(csvDataFile))) {

            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] row = line.split(cvsSplitBy);

                insertStatement = "INSERT INTO " + keyspace + "." + table.getId() + " (";

                Iterator<String> attrsIter = table.getAttributeNames().iterator();

                while (attrsIter.hasNext()) {
                    insertStatement = insertStatement.concat(attrsIter.next());

                    if (attrsIter.hasNext()) {
                        insertStatement = insertStatement.concat(",");
                    }
                }

                insertStatement = insertStatement.concat(") " + "VALUES (");

                for (int j = 0; j < row.length; j++) {
                    if (j < row.length - 1) {
                        if (table.getAttributes().get(j).getType().equals("text")) {
                            insertStatement = insertStatement.concat("'" + row[j] + "'" + ",");
                        } else {
                            insertStatement = insertStatement.concat(row[j] + ",");
                        }
                    } else {
                        if (table.getAttributes().get(j).getType().equals("text")) {
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

    public ResultSet retrieveCassandraTable(String keyspace, String table) {

        return session.execute("SELECT * FROM " + keyspace + "." + table + ";");
    }

    public void close() {
        session.close();
        cluster.close();
    }

}