package it.polimi.tracechecking.driver;

import it.polimi.tracechecking.common.ModelLoader;
import it.polimi.tracechecking.common.model.DIA;
import it.polimi.tracechecking.common.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Launcher {

    private static final Logger logger = LoggerFactory.getLogger(Launcher.class);
    private final Model model;
    private List<CheckingClock> cks = new ArrayList<CheckingClock>();
    private Map<DIA, CheckingClock> map = new HashMap<DIA, CheckingClock>();
    // private Map<ComputeNode, List<Permission>> ComputeNodePermissionMap = new HashMap<ComputeNode, List<Permission>>();

    public Launcher(String modelString) {
        Model model1;//DIA dia1;
        try {
            model1 = ModelLoader.loadInputModel(modelString);
            String outputDir = Config.getProperty(Config.PATH_TO_OUTPUT);
            //ComputeNodePermissionMap = dia1.getComputeNodesWithPermission();
            for (DIA d : model1.getDIAs()) {
                //Prepare dirs for each DIA
                String pathToFormoulae = outputDir + File.separator + d.getDiaName() + File.separator + "formulae";
                String outputFile = outputDir + File.separator + d.getDiaName() + File.separator + "output";
                File formulaeFile = new File(pathToFormoulae);
                formulaeFile.mkdirs();

                    //Put each formula in a file
                // List<String> lines = Arrays.asList(p.getAsociatedMtlFormula());
                Files.write(Paths.get(pathToFormoulae + File.separator + "formula_" + d.getDiaName()), d.getMtlFormulae(), Charset.forName("UTF-8"));


                CheckingClock cc = new CheckingClock(d.getIntervalBetweenChecks(), "/" + d.getDiaName(), pathToFormoulae, outputFile + File.separator + "output");
                map.put(d, cc);
            }
        } catch (IOException e) {
            e.printStackTrace();
            model1 = null;
        }
        this.model = model1;
    }

    /*
        public static void main(String args[]) {
            Config.loadConfig("/home/filippo/IdeaProjects/trace-checking-service/conf/config.properties");
            BasicConfigurator.configure();
            //TODO: read http://stackoverflow.com/questions/12532339/no-appenders-could-be-found-for-loggerlog4j
            //for better configuration :)
            try {

                Launcher l = new Launcher(Utils.readFile("/home/filippo/IdeaProjects/trace-checking-service/conf/model.yml", StandardCharsets.UTF_8));
                System.out.println(l.getResults(l.dia.getComputeNodes().get(0)));
                Thread.sleep(4000);
                //l.cancel();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }

        }
    */
    public Map<String, Boolean> getResults(DIA c) {
        Map<String, Boolean> results = new HashMap<String, Boolean>();
        Integer i = 1;
        for (String s : c.getMtlFormulae()) {
            String formula = s;
            if(map.get(c).getViolations(c, i) == 1)
                results.put(s, false);
            else
                results.put(s, true);
            i++;
        }
        return results;
    }

    public void cancel() { //STOP spark job
        for (CheckingClock ck : map.values()) {
            ck.cancel();
        }
    }

    public Model getModel() {
        return model;
    }
}