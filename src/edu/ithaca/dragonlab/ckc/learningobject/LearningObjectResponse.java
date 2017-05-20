package edu.ithaca.dragonlab.ckc.learningobject;

import java.util.*;

/**
 * @author tdragon
 * 2/14/17.
 */
public class LearningObjectResponse {

    private String userId;
    private String learningObjectId;
    private double knowledgeEstimate;

    public LearningObjectResponse(String userId, String learningObjectId, double knowledgeEstimate) {
        this.userId = userId;
        this.learningObjectId = learningObjectId;
        this.knowledgeEstimate = knowledgeEstimate;
    }

    public LearningObjectResponse (LearningObjectResponse other){
        this.userId = other.userId;
        this.learningObjectId = other.learningObjectId;
        this.knowledgeEstimate = other.knowledgeEstimate;
    }

    //TODO: this needs to be tested
    public static Map<String, List<LearningObjectResponse>> getUserResponseMap(List<LearningObjectResponse> responses) {
        Map<String, List<LearningObjectResponse>> userIdToResponses = new TreeMap<>();
        for(LearningObjectResponse response : responses){
            List<LearningObjectResponse> userResponses = userIdToResponses.get(response.getUserId());
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
}
