package it.polimi.tracechecking.common;

import it.polimi.tracechecking.common.model.*;
import org.apache.commons.io.FileUtils;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Tag;

import java.io.File;
import java.io.IOException;

public class ModelLoader {
    
    private static final Constructor modelConstructor = buildModelConstructor();
    
    public static void main(String[] args){
        try {
            DIA dia = ModelLoader.loadInputModelFromFile("/home/filippo/IdeaProjects/trace-checking-service/conf/model.yml");
            System.out.println(dia.getDiaName() + "\n");
            
            for(DIAElement e : dia.getElements()){
                System.out.println(e.getId());
                if(e instanceof Permission){
                    System.out.println(((Permission) e).getValidityStartTime().toString());
                    System.out.println(((Permission) e).getValidityEndTime().toString());
                    System.out.println(((Permission) e).getActionType().toString());
                }
                if(e instanceof StorageSystem){
                    System.out.println(((StorageSystem) e).getTargetTech().toString());
                }
                if(e instanceof ComputeNode){
                    System.out.println(((ComputeNode) e).getTargetTech().toString());
                }
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    
    public static DIA loadInputModelFromFile(String pathToInputModel)  throws IOException {

        Yaml yaml = new Yaml(modelConstructor);

        String content = FileUtils.readFileToString(new File(pathToInputModel), "UTF-8");
        
        return yaml.loadAs(content, DIA.class);
    }

    public static DIA loadInputModel(String model) throws IOException {

        Yaml yaml = new Yaml(modelConstructor);

        return yaml.loadAs(model, DIA.class);
    }
    
    private static Constructor buildModelConstructor(){
        Constructor constructor = new Constructor(DIA.class);
        
        TypeDescription diaDesc = new TypeDescription(DIA.class);
        diaDesc.putListPropertyType("elements", DIAElement.class);

        TypeDescription storageSystemDesc = new TypeDescription(StorageSystem.class, new Tag("!storageNode"));
        
        TypeDescription computeNodeDesc = new TypeDescription(ComputeNode.class, new Tag("!computeNode"));

        TypeDescription tableDesc = new TypeDescription(Dataset.class, new Tag("!dataset"));
        tableDesc.putListPropertyType("attributes", Attribute.class);

        TypeDescription userDesc = new TypeDescription(User.class, new Tag("!user"));
        
        TypeDescription permissionDesc = new TypeDescription(Permission.class, new Tag("!permission"));

        constructor.addTypeDescription(diaDesc);
        constructor.addTypeDescription(storageSystemDesc);
        constructor.addTypeDescription(computeNodeDesc);
        constructor.addTypeDescription(tableDesc);
        constructor.addTypeDescription(userDesc);        
        constructor.addTypeDescription(permissionDesc);
        
        return constructor;
    }
}
