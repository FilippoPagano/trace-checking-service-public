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
public class hdfsLogger {
    public void write(String content) {
        Configuration config = new Configuration();
        config.set("dfs.client.block.write.replace-datanode-on-failure.policy", "NEVER"); //magic

        FileSystem fs;
        try {
            fs = FileSystem.get(new URI("hdfs://localhost:9000"), config);
            Path filenamePath = new Path("/user/filippo/trace1");
            if (!fs.exists(filenamePath)) {
                fs.create(filenamePath, true);
                fs.close();
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
}
