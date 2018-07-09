package edu.ithaca.dragon.tecmap.prediction;

import edu.ithaca.dragon.tecmap.learningresource.ContinuousAssessmentMatrix;

import java.util.List;
import java.util.Map;

public interface Predictor {

    //Use when making a numeric column discrete
    public static final double ESTIMATE_THRESHOLD = 0.78;

    /**
     * Classifies a set of testing data that is given
     * @param rawTestingData
     * @param assignmentsForClassification
     * @return Map of studentIds -> classification category
     */
    Map<String, String> classifySet(ContinuousAssessmentMatrix rawTestingData, List<String> assignmentsForClassification);
}
