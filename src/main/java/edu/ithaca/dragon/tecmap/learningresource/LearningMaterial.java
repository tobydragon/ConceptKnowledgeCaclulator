package edu.ithaca.dragon.tecmap.learningresource;

import edu.ithaca.dragon.tecmap.io.record.LearningResourceRecord;
import lombok.Getter;

import java.util.*;

@Getter
public class LearningMaterial {

    protected Collection<LearningResourceType> types;
    private String id;
    private String text;


    public LearningMaterial(String id, Collection<LearningResourceType> types, String text){
        this.id = id;
        this.types = types;
        this.text = text;
    }
    public LearningMaterial(String id, LearningResourceType type, String text){
        this(id,  new ArrayList<>(Arrays.asList(type)), text);
    }

    public LearningMaterial(LearningMaterial resource){
        this(resource.getId(), resource.getTypes(), resource.getText());
    }

    public LearningMaterial(LearningResourceRecord record){
        this(record.getId(), record.getResourceTypes(), record.getText());
    }

    public static Map<String, LearningMaterial> deepCopyLearningMaterialMap(Map<String, LearningMaterial> mapToCopy){
        Map<String, LearningMaterial> newMap = new HashMap<>();
        for (Map.Entry<String, LearningMaterial> entryToCopy : mapToCopy.entrySet()){
            newMap.put(entryToCopy.getKey(), new LearningMaterial(entryToCopy.getValue()));
        }
        return newMap;
    }

}
