package edu.ithaca.dragonlab.ckc.io;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ithaca.dragonlab.ckc.learningobject.LearningObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by willsuchanek on 4/10/17.
 */
public class LearningObjectLinkRecord {
    private String learningObject;
    private List<String> conceptIds;
    private double dataImportance;

    public LearningObjectLinkRecord(String learningObject, List<String> conceptIds){
        this.learningObject = learningObject;
        this.conceptIds = conceptIds;
        this.dataImportance = 1;
    }

    public LearningObjectLinkRecord(String learningObject, List<String> conceptIds, double dataImportance){
        this(learningObject,conceptIds);
        this.dataImportance = dataImportance;
    }

    public LearningObjectLinkRecord(){
        learningObject = "";
        conceptIds = new ArrayList<>();
        this.dataImportance = 1;
    }

    public static List<LearningObjectLinkRecord> buildListFromJson(String fullFileName) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        List<LearningObjectLinkRecord> LORLList = mapper.readValue(new File(fullFileName), new TypeReference<List<LearningObjectLinkRecord>>(){});
        return LORLList;
    }

    public String getLearningObject(){ return this.learningObject; }
    public List<String> getConceptIds(){ return this.conceptIds; }
    public double getDataImportance(){ return this.dataImportance; }
    public void setLearningObject(String lo){this.learningObject = lo;}
    public void setConceptIds(List<String> conceptIds){this.conceptIds=conceptIds;}
    public void setDataImportance(double dataImportance){this.dataImportance=dataImportance;}

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
