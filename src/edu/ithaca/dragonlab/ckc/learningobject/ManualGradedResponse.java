package edu.ithaca.dragonlab.ckc.learningobject;

import edu.ithaca.dragonlab.ckc.util.GraphConstants;

/**
 * Created by willsuchanek on 3/6/17.
 */
public class ManualGradedResponse extends LearningObjectResponse {

    double maxPossibleScore;
    double studentScore;

    public ManualGradedResponse(String learningObjectId, double max, double studentScore, String userId){

        super(userId,learningObjectId,studentScore/max);


        this.maxPossibleScore = max;
        this.studentScore = studentScore;
    }


    public double getDataImportance() {
        return GraphConstants.HAND_GRADED_QUESTIONS_WEIGHT;
    }

    public double getNonNormalizedScore(){
        return studentScore;
    }

    public double getMaxPossibleScore(){
        return maxPossibleScore;
    }
}
