package edu.ithaca.dragon.tecmap.learningresource;

import edu.ithaca.dragon.tecmap.io.record.LearningResourceRecord;

import java.util.*;

public class LearningResource {

    protected Collection<LearningResourceType> types;
    private String id;

    public LearningResource(String id, Collection<LearningResourceType> types){
        this.id = id;
        this.types = types;
    }

    public LearningResource(String id, LearningResourceType type){
        this(id,  new ArrayList<>(Arrays.asList(type)));
    }

    public LearningResource(LearningResource material){
        this(material.getId(), material.types);
    }

    public LearningResource(LearningResourceRecord record){
        this(record.getLearningResourceId(), record.getResourceTypes());
    }

    public String getId() {
        return id;
    }

    public static Map<String, LearningResource> deepCopyLearningResourceMap(Map<String, LearningResource> mapToCopy){
        Map<String, LearningResource> newMap = new HashMap<>();
        for (Map.Entry<String, LearningResource> entryToCopy : mapToCopy.entrySet()){
            newMap.put(entryToCopy.getKey(), new LearningResource(entryToCopy.getValue()));
        }
        return newMap;
    }

}
