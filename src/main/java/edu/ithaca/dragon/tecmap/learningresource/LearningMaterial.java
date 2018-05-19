package edu.ithaca.dragon.tecmap.learningresource;

import edu.ithaca.dragon.tecmap.io.record.LearningResourceRecord;

import java.util.*;

public class LearningMaterial  {

    protected Collection<LearningResourceType> types;
    private String id;

    public LearningMaterial (String id, Collection<LearningResourceType> types){
        this.id = id;
        this.types = types;
    }

    public LearningMaterial(String id, LearningResourceType type){
        this(id,  new ArrayList<>(Arrays.asList(type)));
    }

    public LearningMaterial(LearningMaterial material){
        this(material.getId(), material.types);
    }

    public LearningMaterial(LearningResourceRecord record){
        this(record.getLearningResourceId(), record.getResourceTypes());
    }

    public String getId() {
        return id;
    }

    public static Map<String, LearningMaterial> deepCopyLearningMaterialMap(Map<String, LearningMaterial> mapToCopy){
        Map<String, LearningMaterial> newMap = new HashMap<>();
        for (Map.Entry<String, LearningMaterial> entryToCopy : mapToCopy.entrySet()){
            newMap.put(entryToCopy.getKey(), new LearningMaterial(entryToCopy.getValue()));
        }
        return newMap;
    }

}
