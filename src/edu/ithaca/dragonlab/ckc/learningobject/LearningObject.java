package edu.ithaca.dragonlab.ckc.learningobject;

import edu.ithaca.dragonlab.ckc.io.LearningObjectLinkRecord;
import edu.ithaca.dragonlab.ckc.io.LearningResourceRecord;
import edu.ithaca.dragonlab.ckc.util.DataUtil;

import java.util.*;

/**
 * @uathor tdragon
 * 2/14/17.
 */
public class LearningObject {

    String id;
    List<LearningObjectResponse> responses;
    double maxPossibleKnowledgeEstimate;

    public LearningObject(String id){
        this (id, 1);
    }

    public LearningObject(String id, double maxPossibleKnowledgeEstimate){
        this.id = id;
        this.responses = new ArrayList<>();
        this.maxPossibleKnowledgeEstimate = maxPossibleKnowledgeEstimate;
    }

    public LearningObject(LearningObject other){
        this.id = other.id;
        this.maxPossibleKnowledgeEstimate = other.maxPossibleKnowledgeEstimate;
        this.responses = new ArrayList<>();
        for (LearningObjectResponse response : other.responses){
            this.responses.add(new LearningObjectResponse(response));
        }
    }

    public LearningObject(LearningObjectLinkRecord record) {
        this.id = record.getLearningObject();
        this.responses = new ArrayList<>();
        this.maxPossibleKnowledgeEstimate = 1;
    }

    //temprary fucntion to get htings working before switching to LearningResourceRecords
    public LearningObject(LearningResourceRecord record){
        this.id = record.getResourceId();
        this.maxPossibleKnowledgeEstimate = record.getMaxPossibleKnowledgeEstimate();
        this.responses = new ArrayList<>();
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

    public static Map<String, LearningObject> deepCopyLearningObjectMap(Map<String, LearningObject> mapToCopy){
        Map<String, LearningObject> newMap = new HashMap<>();
        for (Map.Entry<String, LearningObject> entryToCopy : mapToCopy.entrySet()){
            newMap.put(entryToCopy.getKey(), new LearningObject(entryToCopy.getValue()));
        }
        return newMap;
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

    public String toString(){
        return "LO ID:" + id + "\t" + "maxPossKnowEst:" + maxPossibleKnowledgeEstimate + "\t" + responses.toString();
    }

    public String getSummaryString(){
        return getId() + "   Est:" + DataUtil.format(calcKnowledgeEstimate()) + "  Imp:" + DataUtil.format(getDataImportance()) + "  ResponseCount:" + getResponses().size();
    }

    public void setMatchingKnowledgeEstimates(Collection<LearningObject> learningObjects, Map<String, LearningObject> loMap){
        List<LearningObject> loList= new ArrayList<LearningObject>(loMap.values());
        for(LearningObject fromList: learningObjects){
            for(LearningObject toList: loList){
                if(fromList == toList){
                    toList.setMaxPossibleKnowledgeEstimate(fromList.getMaxPossibleKnowledgeEstimate());
                }
            }
        }
    }

}
