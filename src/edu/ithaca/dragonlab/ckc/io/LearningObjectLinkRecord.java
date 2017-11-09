package edu.ithaca.dragonlab.ckc.io;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import edu.ithaca.dragonlab.ckc.conceptgraph.CohortConceptGraphs;
import edu.ithaca.dragonlab.ckc.conceptgraph.ConceptGraph;
import edu.ithaca.dragonlab.ckc.learningobject.LearningObject;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by willsuchanek on 4/10/17.
 */
public class LearningObjectLinkRecord {
    private String learningObject;
    private List<String> conceptIds;
    private double dataImportance;
    private double maxPossibleKnowledgeEstimate;

    public LearningObjectLinkRecord(String learningObject, List<String> conceptIds){
        this.learningObject = learningObject;
        this.conceptIds = conceptIds;
        this.dataImportance = 1;
        this.maxPossibleKnowledgeEstimate = maxPossibleKnowledgeEstimate;
    }

    public LearningObjectLinkRecord(String learningObject, double maxPossibleKnowledgeEstimate){
        this.learningObject = learningObject;
        conceptIds = new ArrayList<>();
        this.dataImportance = 1;
        this.maxPossibleKnowledgeEstimate = maxPossibleKnowledgeEstimate;
    }

    public LearningObjectLinkRecord(){
        learningObject = "";
        conceptIds = new ArrayList<>();
        this.dataImportance = 1;
        this.maxPossibleKnowledgeEstimate = maxPossibleKnowledgeEstimate;
    }

    public static List<LearningObjectLinkRecord> buildListFromJson(String fullFileName) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        List<LearningObjectLinkRecord> LORLList = mapper.readValue(new File(fullFileName), new TypeReference<List<LearningObjectLinkRecord>>(){});
        return LORLList;
    }


    /**
     * takes a collection of LearningObjects and uses a max possible knowledge estimate to create a list of LOLRs
     * Currently Used for writing these out to a file
     * @param learningObjects
     * @param maxPossibleKnowledgeEstimate
     * @return a list of LOLRs
     */
    public static List<LearningObjectLinkRecord> createLearningObjectLinkRecords(Collection<LearningObject> learningObjects, double maxPossibleKnowledgeEstimate){
        List<LearningObjectLinkRecord> lolrList = new ArrayList<LearningObjectLinkRecord>();
        for(LearningObject learningObject: learningObjects){
            //TODO: should be sending in max points as well, which should come from learningObject from csv
            lolrList.add( new LearningObjectLinkRecord(learningObject.getId(), maxPossibleKnowledgeEstimate));
        }
        return lolrList;
    }

    public static void lolrToJSON(List<LearningObjectLinkRecord> lolrList, String filename)throws IOException{
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.writeValue(new File(filename), lolrList);

    }



    public String getLearningObject(){ return this.learningObject; }
    public List<String> getConceptIds(){ return this.conceptIds; }
    public double getDataImportance(){ return this.dataImportance; }
    public void setLearningObject(String lo){this.learningObject = lo;}
    public void setConceptIds(List<String> conceptIds){this.conceptIds=conceptIds;}
    public void setDataImportance(double dataImportance){this.dataImportance=dataImportance;}
    public void setMaxPossibleKnowledgeEstimate(double maxPossibleKnowledgeEstimate) {this.maxPossibleKnowledgeEstimate = maxPossibleKnowledgeEstimate;}

    public void addConceptId(String conceptId){
        this.conceptIds.add(conceptId);
    }

    public String toString(){
        String out = "(Learning Object ID: " + this.learningObject + " Concept IDs: ";
        for (int i = 0; i <this.conceptIds.size(); i++){
            out+=this.conceptIds.get(i);
            if (i <this.conceptIds.size()-1){
                out+=", ";
            }
        }
        out+=")\n";
        return  out;
    }
}
