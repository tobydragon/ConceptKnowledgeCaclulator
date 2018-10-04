package edu.ithaca.dragon.tecmap.io.record;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import edu.ithaca.dragon.tecmap.learningresource.ColumnItem;
import edu.ithaca.dragon.tecmap.learningresource.LearningResourceType;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by willsuchanek on 4/10/17.
 */
public class LearningResourceRecord {

    private String learningResourceId;
    private Collection <LearningResourceType> resourceTypes;
    private Collection<String> conceptIds;
    private double dataImportance;
    private double maxPossibleKnowledgeEstimate;

    public LearningResourceRecord(String learningResourceId, Collection<LearningResourceType> resourceTypes, Collection<String> conceptIds, double maxPossibleKnowledgeEstimate, double dataImportance){
        this.learningResourceId = learningResourceId;
        this.resourceTypes = new ArrayList<>(resourceTypes);
        this.conceptIds = new ArrayList<>(conceptIds);
        this.dataImportance = dataImportance;
        this.maxPossibleKnowledgeEstimate = maxPossibleKnowledgeEstimate;
    }

    public LearningResourceRecord(ColumnItem assessment){
        this( assessment.getId(), LearningResourceType.getDefaultResourceTypes() , new ArrayList<>(), assessment.getMaxPossibleKnowledgeEstimate(), assessment.getDataImportance());
    }

    // default constructor for JSON
    public LearningResourceRecord(){
        this("", new ArrayList<>(), new ArrayList<>(), 1, 0);
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

    public static List<LearningResourceRecord> createLearningResourceRecordsFromAssessmentItems(Collection<ColumnItem> columnItems){
        List<LearningResourceRecord> lolrList = new ArrayList<LearningResourceRecord>();
        for(ColumnItem columnItem : columnItems){
            lolrList.add( new LearningResourceRecord(columnItem));
        }
        return lolrList;
    }

    public static void resourceRecordsToJSON(Collection<LearningResourceRecord> lolrList, String filename)throws IOException{
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.writeValue(new File(filename), lolrList);
    }

    public boolean isType(LearningResourceType typeToCheck){
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

    public Collection<LearningResourceType> getResourceTypes() {
        return resourceTypes;
    }

    public void setResourceTypes(Collection<LearningResourceType> resourceTypes) {
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
