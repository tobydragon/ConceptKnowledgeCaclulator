package edu.ithaca.dragonlab.ckc.learningobject;

import java.util.*;

/**
 * Created by tdragon on 2/14/17.
 */
public interface LearningObjectResponse {

    public String getLearningObjectId();

    public double calcKnowledgeEstimate();


    //TODO: should this be user Ids? Should there be more than one possible user to a response?
    public String getUserId();

    //TODO: this needs to be tested
    static Map<String, List<LearningObjectResponse>> getUserResponseMap(List<LearningObjectResponse> responses) {
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
}
