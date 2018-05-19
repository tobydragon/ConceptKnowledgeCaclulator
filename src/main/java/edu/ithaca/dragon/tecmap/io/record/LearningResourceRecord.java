package edu.ithaca.dragon.tecmap.io.record;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import edu.ithaca.dragon.tecmap.learningobject.AssessmentItem;
import edu.ithaca.dragon.tecmap.learningobject.LearningResource;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by willsuchanek on 4/10/17.
 */
public class LearningResourceRecord {

    private String learningResourceId;
    private Collection <LearningResource.Type> resourceTypes;
    private Collection<String> conceptIds;
    private double dataImportance;
    private double maxPossibleKnowledgeEstimate;

    public LearningResourceRecord(String learningResourceId, Collection<LearningResource.Type> resourceTypes, Collection<String> conceptIds, double maxPossibleKnowledgeEstimate, double dataImportance){
        this.learningResourceId = learningResourceId;
        this.resourceTypes = new ArrayList<>(resourceTypes);
        this.conceptIds = new ArrayList<>(conceptIds);
        this.dataImportance = dataImportance;
        this.maxPossibleKnowledgeEstimate = maxPossibleKnowledgeEstimate;
    }

    public LearningResourceRecord(AssessmentItem assessment){
        this( assessment.getId(), Arrays.asList(LearningResource.Type.ASSESSMENT), new ArrayList<>(), assessment.getDataImportance(), assessment.getMaxPossibleKnowledgeEstimate());
    }

    // default constructor for JSON
    public LearningResourceRecord(){
        this("", new ArrayList<>(), new ArrayList<>(), 1, 0);
    }


    public static List<LearningResourceRecord> buildListFromJson(String fullFileName) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.readValue(new File(fullFileName), new TypeReference<List<LearningResourceRecord>>(){});
    }

    public static List<LearningResourceRecord> createLRecordsFromAssessments(Collection<AssessmentItem> assessmentItems){
        List<LearningResourceRecord> lolrList = new ArrayList<LearningResourceRecord>();
        for(AssessmentItem assessmentItem : assessmentItems){
            lolrList.add( new LearningResourceRecord(assessmentItem));
        }
        return lolrList;
    }

    public static void resourceRecordsToJSON(Collection<LearningResourceRecord> lolrList, String filename)throws IOException{
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.writeValue(new File(filename), lolrList);
    }

    public boolean isType(LearningResource.Type typeToCheck){
        return resourceTypes.contains(typeToCheck);
    }

    public String getLearningResourceId(){ return this.learningResourceId; }
    public Collection<String> getConceptIds(){ return this.conceptIds; }
    public double getDataImportance(){ return this.dataImportance; }
    public double getMaxPossibleKnowledgeEstimate(){ return this.maxPossibleKnowledgeEstimate;}
    public void setLearningResourceId(String lo){this.learningResourceId = lo;}
    public void setConceptIds(List<String> conceptIds){this.conceptIds=conceptIds;}
    public void setDataImportance(double dataImportance){this.dataImportance=dataImportance;}
    public void setMaxPossibleKnowledgeEstimate(double maxPossibleKnowledgeEstimate) {this.maxPossibleKnowledgeEstimate = maxPossibleKnowledgeEstimate;}

    public void addConceptId(String conceptId){
        this.conceptIds.add(conceptId);
    }

    public Collection<LearningResource.Type> getResourceTypes() {
        return resourceTypes;
    }

    public void setResourceTypes(Collection<LearningResource.Type> resourceTypes) {
        this.resourceTypes = new ArrayList<>(resourceTypes);
    }

    public String toString(){
        String out = "(Learning Resource ID: " + this.learningResourceId + " Concept IDs: ";
        for (String id : conceptIds){
            out+=id + ", ";
        }
        //remove last comma and space
        out = out.substring(0, out.length()-2);
        out+=")";
        return  out;
    }
}
