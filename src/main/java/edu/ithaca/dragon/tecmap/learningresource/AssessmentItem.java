package edu.ithaca.dragon.tecmap.learningresource;

import edu.ithaca.dragon.tecmap.io.record.LearningResourceRecord;
import edu.ithaca.dragon.tecmap.util.DataUtil;

import java.util.*;

/**
 * @uathor tdragon
 * 2/14/17.
 */
public class AssessmentItem {

    String id;
    List<AssessmentItemResponse> responses;
    double maxPossibleKnowledgeEstimate;

    public AssessmentItem(String id){
        this (id, 1);
    }

    public AssessmentItem(String id, double maxPossibleKnowledgeEstimate){
        this.id = id;
        this.responses = new ArrayList<>();
        this.maxPossibleKnowledgeEstimate = maxPossibleKnowledgeEstimate;
    }

    public AssessmentItem(AssessmentItem other){
        this.id = other.id;
        this.maxPossibleKnowledgeEstimate = other.maxPossibleKnowledgeEstimate;
        this.responses = new ArrayList<>();
        for (AssessmentItemResponse response : other.responses){
            this.responses.add(new AssessmentItemResponse(response));
        }
    }

//    public AssessmentItem(LearningObjectLinkRecord record) {
//        this.id = record.getLearningObject();
//        this.responses = new ArrayList<>();
//        this.maxPossibleKnowledgeEstimate = 1;
//    }

    //temprary fucntion to get htings working before switching to LearningResourceRecords
    public AssessmentItem(LearningResourceRecord record){
        this.id = record.getLearningResourceId();
        this.maxPossibleKnowledgeEstimate = record.getMaxPossibleKnowledgeEstimate();
        this.responses = new ArrayList<>();
    }

    public void addResponse(AssessmentItemResponse response){
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
        for (AssessmentItemResponse response : responses){
            estimate += response.calcKnowledgeEstimate();
        }
        if (responses.size() > 0){

            estimate /= responses.size();
        }
        return estimate;
    }

    public static Map<String, AssessmentItem> deepCopyLearningObjectMap(Map<String, AssessmentItem> mapToCopy){
        Map<String, AssessmentItem> newMap = new HashMap<>();
        for (Map.Entry<String, AssessmentItem> entryToCopy : mapToCopy.entrySet()){
            newMap.put(entryToCopy.getKey(), new AssessmentItem(entryToCopy.getValue()));
        }
        return newMap;
    }


    public void setMaxPossibleKnowledgeEstimate(double max){ this.maxPossibleKnowledgeEstimate = max; }

    public double getMaxPossibleKnowledgeEstimate()  {return maxPossibleKnowledgeEstimate; }

    public String getId() {
        return id;
    }

    public List<AssessmentItemResponse> getResponses() {
        return responses;
    }

    public boolean equals(Object other){
        if(other == null){
            return false;
        }
        if(!AssessmentItem.class.isAssignableFrom(other.getClass())){
            return false;
        }
        AssessmentItem otherNode = (AssessmentItem) other;
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

    public void setMatchingKnowledgeEstimates(Collection<AssessmentItem> columnItems, Map<String, AssessmentItem> loMap){
        List<AssessmentItem> loList= new ArrayList<AssessmentItem>(loMap.values());
        for(AssessmentItem fromList: columnItems){
            for(AssessmentItem toList: loList){
                if(fromList == toList){
                    toList.setMaxPossibleKnowledgeEstimate(fromList.getMaxPossibleKnowledgeEstimate());
                }
            }
        }
    }

    public static List<AssessmentItem> buildListFromAssessmentItemResponses(List<AssessmentItemResponse> responses, Map<String, Double> maxKnowledgeEstimates) {
        Map<String, AssessmentItem> assessments = new HashMap<>();
        for (AssessmentItemResponse response : responses) {
            String assessmentId = response.getLearningObjectId();
            if (assessments.containsKey(assessmentId)) {
                assessments.get(assessmentId).addResponse(response);
            } else {
                double maxKE = maxKnowledgeEstimates.get(assessmentId);
                AssessmentItem newAssessment = new AssessmentItem(assessmentId, maxKE);
                newAssessment.addResponse(response);
                assessments.put(assessmentId, newAssessment);
            }
        }
        return new ArrayList<>(assessments.values());
    }

}
