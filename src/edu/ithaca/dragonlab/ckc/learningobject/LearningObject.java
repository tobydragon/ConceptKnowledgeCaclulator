package edu.ithaca.dragonlab.ckc.learningobject;

import java.util.List;

/**
 * Created by tdragon on 2/14/17.
 */
public class LearningObject {

    String id;
    List<LearningObjectResponse> responses;

    public boolean addResponse(LearningObjectResponse response){
        if (id.equals(response.getLearningObjectId())){
            responses.add(response);
            return true;
        }
        else {
            System.err.println("ERROR: wrong learning object for this response");
            return false;
        }
    }

    public double calcKnowledgeEstimate(){
        //TODO: calculate just by averaging?
        return 0;

    }


}
