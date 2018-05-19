package edu.ithaca.dragon.tecmap.learningresource;

import edu.ithaca.dragon.tecmap.util.GraphConstants;

/**
 * Created by willsuchanek on 3/6/17.
 */
public class ManualGradedResponse extends AssessmentItemResponse {

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
