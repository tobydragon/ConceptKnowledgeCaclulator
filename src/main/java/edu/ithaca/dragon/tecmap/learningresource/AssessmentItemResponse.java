package edu.ithaca.dragon.tecmap.learningresource;

import edu.ithaca.dragon.tecmap.io.reader.CSVReader;
import edu.ithaca.dragon.tecmap.io.reader.SakaiReader;
import edu.ithaca.dragon.tecmap.util.DataUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author tdragon
 * 2/14/17.
 */
public class AssessmentItemResponse {

    private String userId;
    private String learningObjectId;
    private double knowledgeEstimate;

    public AssessmentItemResponse(String userId, String learningObjectId, double knowledgeEstimate) {
        this.userId = userId;
        this.learningObjectId = learningObjectId;
        this.knowledgeEstimate = knowledgeEstimate;
    }

    public AssessmentItemResponse(AssessmentItemResponse other){
        this.userId = other.userId;
        this.learningObjectId = other.learningObjectId;
        this.knowledgeEstimate = other.knowledgeEstimate;
    }

    //TODO: this needs to be tested
    public static Map<String, List<AssessmentItemResponse>> getUserResponseMap(List<AssessmentItemResponse> responses) {
        Map<String, List<AssessmentItemResponse>> userIdToResponses = new TreeMap<>();
        for(AssessmentItemResponse response : responses){
            List<AssessmentItemResponse> userResponses = userIdToResponses.get(response.getUserId());
            //If nothing has been added for this user, add a new map entry for that user
            if (userResponses == null){
                userResponses = new ArrayList<>();
                userIdToResponses.put(response.getUserId(), userResponses);
            }
            //add the response to the list (
            userResponses.add(response);
        }

        return userIdToResponses;
    }

    public String getLearningObjectId(){ return learningObjectId; }

    public double calcKnowledgeEstimate(){
        return knowledgeEstimate;
    }

    public String getUserId(){
        return userId;
    }


    public boolean equals(Object other){
        if(other == null){
            return false;
        }
        if(!AssessmentItemResponse.class.isAssignableFrom(other.getClass())){
            return false;
        }
        AssessmentItemResponse otherNode = (AssessmentItemResponse) other;
        if(this.userId.equals(otherNode.userId) && DataUtil.equalsDoubles(this.knowledgeEstimate, otherNode.knowledgeEstimate)
                && this.learningObjectId.equals(otherNode.learningObjectId)){
            return true;
        } else {
            return false;
        }
    }

    public String toString(){
        return getLearningObjectId() + "\tuser: "+ getUserId() + "\t est: "+ calcKnowledgeEstimate();
    }

    public static List<AssessmentItemResponse> createAssessmentItemResponses(List<String> assessmentFiles) throws IOException {
        List<AssessmentItemResponse> assessments = new ArrayList<>();
        for (String aname: assessmentFiles){
            CSVReader csvReader = new SakaiReader(aname);
            List<AssessmentItemResponse> temp = csvReader.getManualGradedResponses();
            assessments.addAll(temp);
        }
        return assessments;
    }
}
