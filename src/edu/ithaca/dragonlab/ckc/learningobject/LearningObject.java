package edu.ithaca.dragonlab.ckc.learningobject;

import java.util.ArrayList;
import java.util.List;

/**
 * @uathor tdragon
 * 2/14/17.
 */
public class LearningObject {

    String id;
    List<LearningObjectResponse> responses;

    public LearningObject(String id){
        this.id = id;
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

    //TODO: should this be differnt than averaging, how about data weight??
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

    public String getId() {
        return id;
    }

    public List<LearningObjectResponse> getResponses() {
        return responses;
    }

}
