package edu.ithaca.dragonlab.ckc.io;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by willsuchanek on 4/10/17.
 */
public class LearningObjectLinkRecord {
    private String learningObject;
    private List<String> conceptIds;

    public LearningObjectLinkRecord(String lo, List<String> conceptIds){
        this.learningObject = lo;
        this.conceptIds = conceptIds;
    }

    public String getLearningObject(){ return this.learningObject; }
    public List<String> getConceptIds(){ return this.conceptIds; }

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
