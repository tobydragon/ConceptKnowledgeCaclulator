package edu.ithaca.dragon.tecmap.learningresource;

import edu.ithaca.dragon.tecmap.io.record.LearningResourceRecord;
import edu.ithaca.dragon.tecmap.util.DataUtil;

import java.util.*;

/**
 * @uathor tdragon
 * 2/14/17.
 */
public class ColumnItem {

    String id;
    List<AssessmentItemResponse> responses;
    double maxPossibleKnowledgeEstimate;

    public ColumnItem(String id){
        this (id, 1);
    }

    public ColumnItem(String id, double maxPossibleKnowledgeEstimate){
        this.id = id;
        this.responses = new ArrayList<>();
        this.maxPossibleKnowledgeEstimate = maxPossibleKnowledgeEstimate;
    }

    public ColumnItem(ColumnItem other){
        this.id = other.id;
        this.maxPossibleKnowledgeEstimate = other.maxPossibleKnowledgeEstimate;
        this.responses = new ArrayList<>();
        for (AssessmentItemResponse response : other.responses){
            this.responses.add(new AssessmentItemResponse(response));
        }
    }

//    public ColumnItem(LearningObjectLinkRecord record) {
//        this.id = record.getLearningObject();
//        this.responses = new ArrayList<>();
//        this.maxPossibleKnowledgeEstimate = 1;
//    }

    //temprary fucntion to get htings working before switching to LearningResourceRecords
    public ColumnItem(LearningResourceRecord record){
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

    public static Map<String, ColumnItem> deepCopyLearningObjectMap(Map<String, ColumnItem> mapToCopy){
        Map<String, ColumnItem> newMap = new HashMap<>();
        for (Map.Entry<String, ColumnItem> entryToCopy : mapToCopy.entrySet()){
            newMap.put(entryToCopy.getKey(), new ColumnItem(entryToCopy.getValue()));
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
        if(!ColumnItem.class.isAssignableFrom(other.getClass())){
            return false;
        }
        ColumnItem otherNode = (ColumnItem) other;
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

    public void setMatchingKnowledgeEstimates(Collection<ColumnItem> columnItems, Map<String, ColumnItem> loMap){
        List<ColumnItem> loList= new ArrayList<ColumnItem>(loMap.values());
        for(ColumnItem fromList: columnItems){
            for(ColumnItem toList: loList){
                if(fromList == toList){
                    toList.setMaxPossibleKnowledgeEstimate(fromList.getMaxPossibleKnowledgeEstimate());
                }
            }
        }
    }

    public static List<ColumnItem> buildListFromAssessmentItemResponses(List<AssessmentItemResponse> responses, Map<String, Double> maxKnowledgeEstimates) {
        Map<String, ColumnItem> assessments = new HashMap<>();
        for (AssessmentItemResponse response : responses) {
            String assessmentId = response.getLearningObjectId();
            if (assessments.containsKey(assessmentId)) {
                assessments.get(assessmentId).addResponse(response);
            } else {
                double maxKE = maxKnowledgeEstimates.get(assessmentId);
                ColumnItem newAssessment = new ColumnItem(assessmentId, maxKE);
                newAssessment.addResponse(response);
                assessments.put(assessmentId, newAssessment);
            }
        }
        return new ArrayList<>(assessments.values());
    }

}
