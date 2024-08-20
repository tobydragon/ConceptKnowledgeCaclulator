package edu.ithaca.dragon.tecmap.learningresource;

import edu.ithaca.dragon.tecmap.io.record.LearningResourceRecord;
import edu.ithaca.dragon.tecmap.util.DataUtil;

import java.util.*;

/**
 * @author tdragon
 * 2/14/17.
 */

public class AssessmentItem {

    String id;
    String text;
    List<AssessmentItemResponse> responses;
    double maxPossibleKnowledgeEstimate;

    public AssessmentItem(String id, String text){
        this (id, text,1);
    }

    public AssessmentItem(String id, String text, double maxPossibleKnowledgeEstimate){
        this.id = id;
        this.text = text;
        this.responses = new ArrayList<>();
        this.maxPossibleKnowledgeEstimate = maxPossibleKnowledgeEstimate;
    }

    public AssessmentItem(AssessmentItem other){
        this.id = other.id;
        this.text = other.text;
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
        this.id = record.getId();
        this.text = record.getText();
        this.maxPossibleKnowledgeEstimate = record.getMaxPossibleKnowledgeEstimate();
        this.responses = new ArrayList<>();
    }

    public void addResponse(AssessmentItemResponse response){
        if (text.equals(response.getAssessmentItemText())) {
            responses.add(response);
        }
        else{
            throw new IllegalArgumentException("AIR text: "+ response.getAssessmentItemText()
                    + " does not match AssessmentItemText: " + text);
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

    public List<AssessmentItemResponse> getResponses() {
        return responses;
    }

    public String getText() {
        return text;
    }

    public String getId() {return id;}

    public boolean equals(Object other){
        if(other == null){
            return false;
        }
        if(!AssessmentItem.class.isAssignableFrom(other.getClass())){
            return false;
        }
        AssessmentItem assessmentItem = (AssessmentItem) other;
        if(this.text.equals(assessmentItem.text) && this.responses.equals(assessmentItem.responses)){
            return true;
        } else {
            return false;
        }
    }

    public String toString(){
        return "AI Text: " + text + "\t" + "maxPossKnowEst: " + maxPossibleKnowledgeEstimate + "\t" + responses.toString();
    }

    public String getSummaryString(){
        return getText() + "   Est: " + DataUtil.format(calcKnowledgeEstimate()) + "  Imp: " + DataUtil.format(getDataImportance()) + "  ResponseCount: " + getResponses().size();
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
            String assessmentId = response.getAssessmentItemId();
            String assessmentText = response.getAssessmentItemText();
            if (assessments.containsKey(assessmentId)) {
                assessments.get(assessmentId).addResponse(response);
            } else {
                double maxKE = maxKnowledgeEstimates.get(assessmentText);
                AssessmentItem newAssessment = new AssessmentItem(assessmentId, assessmentText, maxKE);
                newAssessment.addResponse(response);
                assessments.put(assessmentId, newAssessment);
            }
        }
        return new ArrayList<>(assessments.values());
    }

    public static List<AssessmentItemResponse> getItemResponsesFromAssessmentList(List<AssessmentItem> assessmentItems){
        List<AssessmentItemResponse> assessmentItemResponses = new ArrayList<>();
        for(AssessmentItem assessment : assessmentItems){
            List<AssessmentItemResponse> responses = assessment.getResponses();
            for(AssessmentItemResponse response : responses){
                assessmentItemResponses.add(response);
            }
        }
        return assessmentItemResponses;
    }

    public static List<AssessmentItem> getAssessmentCopyWithoutResponses(List<AssessmentItem> assessmentItems){
        List<AssessmentItem> assessmentsListNoResponses = new ArrayList<>();
        for(AssessmentItem assessment : assessmentItems){
            AssessmentItem newAssessment = new AssessmentItem(assessment.getId(), assessment.getText(), assessment.getMaxPossibleKnowledgeEstimate());
            assessmentsListNoResponses.add(newAssessment);
        }
        return assessmentsListNoResponses;
    }

}
