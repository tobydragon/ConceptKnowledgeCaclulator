package edu.ithaca.dragon.tecmap.learningobject;

import edu.ithaca.dragon.tecmap.util.DataUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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


    public boolean equals(Object other){
        if(other == null){
            return false;
        }
        if(!LearningObjectResponse.class.isAssignableFrom(other.getClass())){
            return false;
        }
        LearningObjectResponse otherNode = (LearningObjectResponse) other;
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
}
