package it.polimi.cassandra.utils.common;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Tag;

import it.polimi.cassandra.utils.common.model.Attribute;
import it.polimi.cassandra.utils.common.model.ComputeNode;
import it.polimi.cassandra.utils.common.model.DIA;
import it.polimi.cassandra.utils.common.model.DIAElement;
import it.polimi.cassandra.utils.common.model.Dataset;
import it.polimi.cassandra.utils.common.model.Permission;
import it.polimi.cassandra.utils.common.model.StorageSystem;
import it.polimi.cassandra.utils.common.model.User;

public class ModelLoader {
    
    private static final Constructor modelConstructor = buildModelConstructor();
    
    public static void main(String[] args){
        try {
            DIA dia = ModelLoader.loadInputModelFromFile("/Users/michele/workspace/cassandra-utils/conf/initializer-example.yml");
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
