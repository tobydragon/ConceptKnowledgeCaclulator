package edu.ithaca.dragon.tecmap.io.record;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItem;
import edu.ithaca.dragon.tecmap.learningresource.LearningResourceType;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

/**
 * Created by willsuchanek on 4/10/17.
 */
public class LearningResourceRecord {
    @JsonIgnore
    private String id;
    private Collection <LearningResourceType> resourceTypes;
    private Collection<String> conceptIds;
    private double dataImportance;
    private double maxPossibleKnowledgeEstimate;
    private String text;

//    public LearningResourceRecord(Collection<LearningResourceType> resourceTypes, Collection<String> conceptIds,
//                                  double maxPossibleKnowledgeEstimate, double dataImportance, String text){
    @JsonCreator
    public LearningResourceRecord(
            @JsonProperty("resourceTypes") Collection<LearningResourceType> resourceTypes,
            @JsonProperty("conceptIds") Collection<String> conceptIds,
            @JsonProperty("maxPossibleKnowledgeEstimate") double maxPossibleKnowledgeEstimate,
            @JsonProperty("dataImportance") double dataImportance,
            @JsonProperty("text") String text){
        this.resourceTypes = (resourceTypes != null) ? new ArrayList<>(resourceTypes) : new ArrayList<>();
        this.conceptIds = (conceptIds != null) ? new ArrayList<>(conceptIds) : new ArrayList<>();
        this.dataImportance = dataImportance;
        this.maxPossibleKnowledgeEstimate = maxPossibleKnowledgeEstimate;
        this.text = text;
        this.id = generateUniqueID(this.conceptIds);
    }

    public LearningResourceRecord(AssessmentItem assessment){
        this(LearningResourceType.getDefaultResourceTypes() , new ArrayList<>(), assessment.getMaxPossibleKnowledgeEstimate(), assessment.getDataImportance(), assessment.getText());
    }

    // default constructor for JSON
    public LearningResourceRecord(String text){
        this(new ArrayList<>(), new ArrayList<>(), 1, 0, text);
    }

    public static List<LearningResourceRecord> createLearningResourceRecordsFromJsonFiles(List<String> filenames) throws IOException {
        List<LearningResourceRecord> linkRecord = new ArrayList<>();
        for (String rFiles : filenames){
            List<LearningResourceRecord> temp = LearningResourceRecord.createLearningResourceRecordsFromJsonFile(rFiles);
            linkRecord.addAll(temp);
        }
        return linkRecord;
    }

    public static List<LearningResourceRecord> createLearningResourceRecordsFromJsonFile(String fullFileName) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.readValue(new File(fullFileName), new TypeReference<List<LearningResourceRecord>>(){});
    }

    public static List<LearningResourceRecord> createLearningResourceRecordsFromAssessmentItems(Collection<AssessmentItem> columnItems){
        List<LearningResourceRecord> lrrList = new ArrayList<LearningResourceRecord>();
        for(AssessmentItem columnItem : columnItems){
            lrrList.add( new LearningResourceRecord(columnItem));
        }
        return lrrList;
    }

    public static void resourceRecordsToJSON(Collection<LearningResourceRecord> lolrList, String filename)throws IOException{
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.writeValue(new File(filename), lolrList);
    }

    public boolean isType(LearningResourceType typeToCheck){
        return resourceTypes.contains(typeToCheck);
    }

    public String getId(){ return this.id; }
    public Collection<String> getConceptIds(){ return conceptIds; }
    public double getDataImportance(){ return this.dataImportance; }
    public double getMaxPossibleKnowledgeEstimate(){ return this.maxPossibleKnowledgeEstimate;}
    public void setId(String lo){this.id = lo;}
    public void setConceptIds(List<String> conceptIds){this.conceptIds=conceptIds;}
    public void setDataImportance(double dataImportance){this.dataImportance=dataImportance;}
    public void setMaxPossibleKnowledgeEstimate(double maxPossibleKnowledgeEstimate) {this.maxPossibleKnowledgeEstimate = maxPossibleKnowledgeEstimate;}
    public void setText(String text){this.text = text;}
    public String getText(){return this.text;}

    public void addConceptId(String conceptId){
        conceptIds.add(conceptId);
    }

    public Collection<LearningResourceType> getResourceTypes() {
        return resourceTypes;
    }

    public void setResourceTypes(Collection<LearningResourceType> resourceTypes) {
        this.resourceTypes = new ArrayList<>(resourceTypes);
    }

    public static String generateUniqueID(Collection<String> conceptIds) {
        Random random = new Random();
        int num = 1 + random.nextInt(10000);
        StringBuilder concepts = new StringBuilder();
        if (conceptIds != null) {
            for (String conceptId : conceptIds) {
                concepts.append(conceptId.trim());
            }
            return concepts + "_PracticeProblem" + num;
        }
        return "_PracticeProblem" + num;
    }

    public String toString(){
        String out = "(Learning Resource Text: " + this.text + " Concept IDs: ";
        for (String id : conceptIds){
            out+=id + ", ";
        }
        //remove last comma and space
        out = out.substring(0, out.length()-2);
        out+=")";
        return out;
    }

}
