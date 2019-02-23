package edu.ithaca.dragon.tecmap.learningresource;

import edu.ithaca.dragon.tecmap.io.record.LearningMaterialRecord;
import edu.ithaca.dragon.tecmap.io.record.LearningResourceRecord;

import java.util.*;

public class LearningMaterial  {

    protected Collection<LearningResourceType> types;
    private String id;
    private String content;
    private String url;
    private Map<String, Integer> tagsMap;

    public LearningMaterial(LearningMaterialRecord record){
        this.id = record.getId();
        this.types = record.getTypes();
        this.content = record.getContent();
        this.tagsMap = record.getTagsMap();
        this.url = record.getUrl();
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

    public LearningMaterial (String id, Collection<LearningResourceType> types){
        this(id, types, "");
    }

    public LearningMaterial (String id, Collection<LearningResourceType> types, String content){
        this(id, types, content, new ArrayList<>());
    }

    public LearningMaterial (String id, Collection<LearningResourceType> types, String content, Collection<String> tags){
        this(id, types, content, tags, "");
    }

    public LearningMaterial (String id, Collection<LearningResourceType> types, String content, Collection<String> tags, String url){
        this.id = id;
        this.types = types;
        this.content = content;
        this.tagsMap = new HashMap<>();
        this.url = url;
        addTagsMap(tags);
    }

    public String getId() { return id; }
    public String getContent() { return content; }
    public Collection<LearningResourceType> getLearningResourceType() { return types; }
    public Map<String, Integer> getTagsMap() { return tagsMap ; }
    public String getUrl() { return url; }

    /**
     * @return - keys from the tagsMap Map. These keys correspond to the strings in the tagsMap.
     * Used only by by the toString method for formatting purposes
     */
    public List<String> createTagKeys() {

        List<String> tagMapKeys = new ArrayList<>();

        Iterator it = this.tagsMap.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry pair = (Map.Entry)it.next();
            tagMapKeys.add(pair.getKey().toString());
        }

        return tagMapKeys;
    }

    public void addTagsMap(Collection<String> tags){
        for (String tag : tags){
            if (this.tagsMap.containsKey(tag)){
                this.tagsMap.replace(tag, this.tagsMap.get(tag) + 1);
            } else {
                this.tagsMap.put(tag, 1);
            }
        }
    }

    public String toString(){
        return "ID: " + this.id + " Content: " + this.content + " Tags: " + createTagKeys().toString();
    }


    public static Map<String, LearningMaterial> deepCopyLearningMaterialMap(Map<String, LearningMaterial> mapToCopy){
        Map<String, LearningMaterial> newMap = new HashMap<>();
        for (Map.Entry<String, LearningMaterial> entryToCopy : mapToCopy.entrySet()){
            newMap.put(entryToCopy.getKey(), new LearningMaterial(entryToCopy.getValue()));
        }
        return newMap;
    }

}
