package edu.ithaca.dragonlab.ckc.io;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import edu.ithaca.dragonlab.ckc.learningobject.LearningObject;
import edu.ithaca.dragonlab.ckc.learningobject.LearningResource;

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
    private String resourceId;
    private Collection <LearningResource.Type> resourceTypes;
    private Collection<String> conceptIds;
    private double dataImportance;
    private double maxPossibleKnowledgeEstimate;

    public LearningResourceRecord(String resourceId, Collection<LearningResource.Type> resourceTypes, Collection<String> conceptIds, double maxPossibleKnowledgeEstimate, double dataImportance){
        this.resourceId = resourceId;
        this.resourceTypes = new ArrayList<>(resourceTypes);
        this.conceptIds = new ArrayList<>(conceptIds);
        this.dataImportance = dataImportance;
        this.maxPossibleKnowledgeEstimate = maxPossibleKnowledgeEstimate;
    }

    public LearningResourceRecord(LearningObject assessment){
        this( assessment.getId(), Arrays.asList(LearningResource.Type.ASSESSMENT), new ArrayList<>(), assessment.getDataImportance(), assessment.getMaxPossibleKnowledgeEstimate());
    }

    // default constructor for JSON
    public LearningResourceRecord(){
        this("", new ArrayList<>(), new ArrayList<>(), 1, 0);
    }

    //should not be needed because it is used for writing out after reading csv, and we already should have maxKnowledgeEstimate
//    public LearningResourceRecord(String learningObject, double maxPossibleKnowledgeEstimate){
//        this.resourceId = learningObject;
//        conceptIds = new ArrayList<>();
//        this.dataImportance = 1;
//        this.maxPossibleKnowledgeEstimate = maxPossibleKnowledgeEstimate;
//    }


    public static List<LearningResourceRecord> buildListFromJson(String fullFileName) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        List<LearningResourceRecord> LORLList = mapper.readValue(new File(fullFileName), new TypeReference<List<LearningResourceRecord>>(){});
        return LORLList;
    }

    public static List<LearningResourceRecord> createLRecordsFromAssessments(Collection<LearningObject> learningObjects){
        List<LearningResourceRecord> lolrList = new ArrayList<LearningResourceRecord>();
        for(LearningObject learningObject: learningObjects){
            lolrList.add( new LearningResourceRecord(learningObject));
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

    public String getResourceId(){ return this.resourceId; }
    public Collection<String> getConceptIds(){ return this.conceptIds; }
    public double getDataImportance(){ return this.dataImportance; }
    public double getMaxPossibleKnowledgeEstimate(){ return this.maxPossibleKnowledgeEstimate;}
    public void setResourceId(String lo){this.resourceId = lo;}
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
        this.resourceTypes = resourceTypes;
    }

    public String toString(){
        String out = "(Learning Resource ID: " + this.resourceId + " Concept IDs: ";
        for (String id : conceptIds){
            out+=id + ", ";
        }
        //remove last comma and space
        out = out.substring(0, out.length()-2);
        out+=")";
        return  out;
    }
}
