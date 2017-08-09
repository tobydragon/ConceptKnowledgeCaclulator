package edu.ithaca.dragonlab.ckc.learningobject;

import edu.ithaca.dragonlab.ckc.io.LearningObjectLinkRecord;
import edu.ithaca.dragonlab.ckc.util.DataUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @uathor tdragon
 * 2/14/17.
 */
public class LearningObject {

    String id;
    List<LearningObjectResponse> responses;
    double maxPossibleKnowledgeEstimate;

    public LearningObject(String id){
        this.id = id;
        this.responses = new ArrayList<>();
        this.maxPossibleKnowledgeEstimate = 1;
    }

    public LearningObject(LearningObject other){
        this.id = other.id;
        this.maxPossibleKnowledgeEstimate = other.maxPossibleKnowledgeEstimate;
        this.responses = new ArrayList<>();
        for (LearningObjectResponse response : responses){
            this.responses.add(new LearningObjectResponse(response));
        }
    }

    public LearningObject(LearningObjectLinkRecord record) {
        this.id = record.getLearningObject();
        this.responses = new ArrayList<>();
        this.maxPossibleKnowledgeEstimate = 1;
    }

    public void addResponse(LearningObjectResponse response){
        if (id.equals(response.getLearningObjectId())) {
            responses.add(response);
        }
        else{
            throw new IllegalArgumentException("Response object id:"+ response.getLearningObjectId()
                    + " does not match LearningObjectId:" + id);
        }
    }

    public double getDataImportance(){
        return responses.size();
    }

    public double calcKnowledgeEstimate(){
        double estimate = 0;
        for (LearningObjectResponse response : responses){
            estimate += response.calcKnowledgeEstimate();
        }
        if (responses.size() > 0){

            estimate /= responses.size();
        }
        return estimate;
    }


    public void setMaxPossibleKnowledgeEstimate(double max){ this.maxPossibleKnowledgeEstimate = max; }

    public double getMaxPossibleKnowledgeEstimate()  {return maxPossibleKnowledgeEstimate; }

    public String getId() {
        return id;
    }

    public List<LearningObjectResponse> getResponses() {
        return responses;
    }

    public boolean equals(Object other){
        if(other == null){
            return false;
        }
        if(!LearningObject.class.isAssignableFrom(other.getClass())){
            return false;
        }
        LearningObject otherNode = (LearningObject) other;
        if(this.id.equals(otherNode.id) && this.responses.equals(otherNode.responses)){
            return true;
        } else {
            return false;
        }
    }


}
