package edu.ithaca.dragonlab.ckc.learningobject;

import edu.ithaca.dragonlab.ckc.io.LearningResourceRecord;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class LearningMaterial extends LearningResource {

    public LearningMaterial(String id, Collection<Type> types){
        super(id, types);
    }

    public LearningMaterial(String id, Type type){
        super(id, Arrays.asList(type));
    }

    public LearningMaterial(LearningMaterial record){
        super(record.getId(), record.types);
    }

    public LearningMaterial(LearningResourceRecord record){
        super(record.getLearningResourceId(), record.getResourceTypes());
    }

    public static Map<String, LearningMaterial> deepCopyLearningMaterialMap(Map<String, LearningMaterial> mapToCopy){
        Map<String, LearningMaterial> newMap = new HashMap<>();
        for (Map.Entry<String, LearningMaterial> entryToCopy : mapToCopy.entrySet()){
            newMap.put(entryToCopy.getKey(), new LearningMaterial(entryToCopy.getValue()));
        }
        return newMap;
    }

}
