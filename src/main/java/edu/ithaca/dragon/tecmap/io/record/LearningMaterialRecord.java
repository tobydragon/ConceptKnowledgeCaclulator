package edu.ithaca.dragon.tecmap.io.record;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ithaca.dragon.tecmap.learningresource.LearningMaterial;
import edu.ithaca.dragon.tecmap.learningresource.LearningResourceType;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import static edu.ithaca.dragon.tecmap.io.Json.toJsonString;

public class LearningMaterialRecord {

    protected Collection<LearningResourceType> types;
    private String id;
    private String content;
    private String url;
    private Map<String, Integer> tagsMap;

    public LearningMaterialRecord(LearningMaterial learningMaterial) {
        this.types = learningMaterial.getLearningResourceType();
        this.id = learningMaterial.getId();
        this.content = learningMaterial.getContent();
        this.url = learningMaterial.getUrl();
        this.tagsMap = learningMaterial.getTagsMap();
    }

    public LearningMaterialRecord(String id, Collection<LearningResourceType> types, String content, Map<String, Integer> tagsMap, String url) {
        this.types = types;
        this.id = id;
        this.content = content;
        this.url = url;
        this.tagsMap = tagsMap;
    }

    public LearningMaterialRecord(String id, Collection<LearningResourceType> types, String content, Collection<String> tags, String url) {
        this.types = types;
        this.id = id;
        this.content = content;
        this.url = url;
        this.tagsMap = new HashMap<>();
        addTagsMap(tags);
    }

    /**
     * Default constructor used by the ObjectMapper when reading/writing to JSON
     */
    public LearningMaterialRecord() { this("", new ArrayList<>(), "", new HashMap<>(), "");}

    public Collection<LearningResourceType> getTypes() { return types; }
    public void setTypes(Collection<LearningResourceType> types) { this.types = types; }
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    public Map<String, Integer> getTagsMap() { return tagsMap; }
    public void setTagsMap(Map<String, Integer> tagsMap) { this.tagsMap = tagsMap; }

    /**
     * @return - keys from the tagsMap Map. These keys correspond to the strings in the tagsMap.
     * Used only by by the toString method for formatting purposes
     */
    public List<String> createTagsList() {

        List<String> tagMapKeys = new ArrayList<>();

        Iterator it = this.tagsMap.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry pair = (Map.Entry)it.next();
            tagMapKeys.add(pair.getKey().toString());
        }

        return tagMapKeys;
    }

    public String toString(){
        return "ID: " + this.id + " Content: " + this.content + " Tags: " + createTagsList().toString();
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

    public static List<LearningMaterialRecord> jsonToLearningMaterialRecords(String jsonFile) throws IOException {
        List<LearningMaterialRecord> records = fromJsonArrayFile(jsonFile);
        return records;
    }

    public static List<LearningMaterialRecord> fromJsonArrayFile(String filename) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.readValue(new File(filename), new TypeReference<List<LearningMaterialRecord>>(){});
    }

    public static void learningMaterialRecordsToJson(List<LearningMaterialRecord> learningMaterialsRecords, String jsonFile) throws IOException {

        FileWriter writer = null;
        try {
            writer = new FileWriter(jsonFile, false);
            writer.append("[");
        } catch (IOException e) {
            System.out.println("File or directory does not exist");
            e.printStackTrace();
        }

        for (int i = 0; i < learningMaterialsRecords.size(); i++){

            try {
                if (i != learningMaterialsRecords.size() - 1){
                    writer.append(toJsonString(learningMaterialsRecords.get(i)).concat(","));
                } else {
                    writer.append(toJsonString(learningMaterialsRecords.get(i)));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        writer.append("]");
        writer.flush();
        writer.close();
    }

    public boolean equals(Object other){

        if (other == null){
            return false;
        }

        if(!LearningMaterialRecord.class.isAssignableFrom(other.getClass())){
            return false;
        }

        LearningMaterialRecord otherLearningMaterialRecord = (LearningMaterialRecord) other;

        if (!this.id.equals(otherLearningMaterialRecord.id)) {
            return false;
        }

        if (!this.types.containsAll(otherLearningMaterialRecord.types)){
            return false;
        }

        if (!this.content.equals(otherLearningMaterialRecord.content)) {
            return false;
        }

        if (!this.url.equals(otherLearningMaterialRecord.url)) {
            return false;
        }

        if (!this.tagsMap.equals(otherLearningMaterialRecord.tagsMap)) {
            return false;
        }

        return true;
    }

}
