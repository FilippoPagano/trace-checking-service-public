package it.polimi.tracechecking.driver;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by Filippo on 01/06/17.
 */
public final class hdfsLogger {
    private hdfsLogger() {
    }

    public static void makeDir(String path) {
        Configuration config = new Configuration();
        config.set("dfs.client.block.write.replace-datanode-on-failure.policy", "NEVER"); //magic

        FileSystem fs;
        try {
            fs = FileSystem.get(new URI("hdfs://localhost:9000"), config);
            Path filenamePath = new Path(path);
            if (!fs.exists(filenamePath)) {
                fs.mkdirs(filenamePath);
                fs.close();
                fs = FileSystem.get(new URI("hdfs://localhost:9000"), config);
            }

            fs.close();
        } catch (IOException | URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void write(String content) {
        Configuration config = new Configuration();
        config.set("dfs.client.block.write.replace-datanode-on-failure.policy", "NEVER"); //magic

        FileSystem fs;
        try {
            fs = FileSystem.get(new URI("hdfs://localhost:9000"), config);
            Path filenamePath = new Path("/user/filippo/trace1");
            if (!fs.exists(filenamePath)) {
                fs.create(filenamePath, true);
                fs.close();
                fs = FileSystem.get(new URI("hdfs://localhost:9000"), config);
            }

            BufferedWriter br = new BufferedWriter(new OutputStreamWriter(fs.append(filenamePath)));
            br.write(content + "\n");
            br.close();
            fs.close();
        } catch (IOException | URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void write(String appId, String content) {
        Configuration config = new Configuration();
        config.set("dfs.client.block.write.replace-datanode-on-failure.policy", "NEVER"); //magic

        FileSystem fs;
        try {
            fs = FileSystem.get(new URI("hdfs://localhost:9000"), config);
            Path filenamePath = new Path("/" + appId + "/trace1");
            if (!fs.exists(filenamePath)) {
                fs.create(filenamePath, true);
                fs.close();
                fs = FileSystem.get(new URI("hdfs://localhost:9000"), config);
            }

            BufferedWriter br = new BufferedWriter(new OutputStreamWriter(fs.append(filenamePath)));
            br.write(content + "\n");
            br.close();
            fs.close();
        } catch (IOException | URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void writeFile(String path, String text) {
        Configuration config = new Configuration();
        config.set("dfs.client.block.write.replace-datanode-on-failure.policy", "NEVER"); //magic

        FileSystem fs;
        try {
            fs = FileSystem.get(new URI("hdfs://localhost:9000"), config);
            Path filenamePath = new Path(path);
            if (!fs.exists(filenamePath)) {
                fs.create(filenamePath, true);
                fs.close();
                fs = FileSystem.get(new URI("hdfs://localhost:9000"), config);
            } else {
                //TODO: raise exception or overwrite?
                //Right now it just appends
                //to overwrite: fs.create(filenamePath, true);
            }

            BufferedWriter br = new BufferedWriter(new OutputStreamWriter(fs.append(filenamePath)));
            br.write(text + "\n");
            br.close();
            fs.close();
        } catch (IOException | URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void writeNewFile(String applicationId, String logFileText) {
        Configuration config = new Configuration();
        config.set("dfs.client.block.write.replace-datanode-on-failure.policy", "NEVER"); //magic

        FileSystem fs;
        try {
            fs = FileSystem.get(new URI("hdfs://localhost:9000"), config);
            Integer i = 1;
            Path tentativePath = new Path("/" + applicationId + "/trace" + i);

            while (fs.exists(tentativePath)) {
                i++;
                tentativePath = new Path("/" + applicationId + "/trace" + i);
            }
            Path filenamePath = tentativePath;
            if (!fs.exists(filenamePath)) {
                fs.create(filenamePath, true);
                fs.close();
                fs = FileSystem.get(new URI("hdfs://localhost:9000"), config);
            } else {
                //TODO: raise exception or overwrite?
                //Right now it just appends
                //to overwrite: fs.create(filenamePath, true);
            }

            BufferedWriter br = new BufferedWriter(new OutputStreamWriter(fs.append(filenamePath)));
            br.write(logFileText + "\n");
            br.close();
            fs.close();
        } catch (IOException | URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
