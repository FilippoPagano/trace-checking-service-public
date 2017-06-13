package it.polimi.tracechecking.common;

import it.polimi.tracechecking.common.model.DIA;
import it.polimi.tracechecking.common.model.Model;
import org.apache.commons.io.FileUtils;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Tag;

import java.io.File;
import java.io.IOException;

public class ModelLoader {
    
    private static final Constructor modelConstructor = buildModelConstructor();

    public static Model loadInputModelFromFile(String pathToInputModel) throws IOException {

        Yaml yaml = new Yaml(modelConstructor);

        String content = FileUtils.readFileToString(new File(pathToInputModel), "UTF-8");

        return yaml.loadAs(content, Model.class);
    }

    public static Model loadInputModel(String model) throws IOException {

        Yaml yaml = new Yaml(modelConstructor);

        return yaml.loadAs(model, Model.class);
    }
    
    private static Constructor buildModelConstructor(){
        Constructor constructor = new Constructor(Model.class);

        TypeDescription modelDesc = new TypeDescription(Model.class);
        modelDesc.putListPropertyType("DIAs", DIA.class);

        TypeDescription diaDesc = new TypeDescription(DIA.class, new Tag("!DIA"));

        constructor.addTypeDescription(modelDesc);
        constructor.addTypeDescription(diaDesc);

        return constructor;
    }

    public static void main(String[] args) {
        Model m = null;
        try {
            m = loadInputModelFromFile("/home/filippo/IdeaProjects/trace-checking-service/conf/model.yml");
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (DIA d : m.getDIAs()) {
            System.out.println(d.getDiaName());
        }
    }
}
