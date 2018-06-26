package edu.ithaca.dragon.tecmap.prediction;

import edu.ithaca.dragon.tecmap.conceptgraph.eval.KnowledgeEstimateMatrix;

import java.util.List;
import java.util.Map;

public interface Predictor {

    /**
     * Trains the predictor given the rawTrainingData
     * @param rawTrainingData
     * @param categoryAssessmentId
     * @param assignmentsToLearnWith
     */
    void learnSet(KnowledgeEstimateMatrix rawTrainingData, String categoryAssessmentId, List<String> assignmentsToLearnWith);

    /**
     * Predictor forgets and unlearns everything
     */
    void reset();

    /**
     * Classifies a set of testing data that is given
     * @param rawTestingData
     * @param assignmentsForClassification
     * @return Map of studentIds -> classification category
     */
    Map<String, String> classifySet(KnowledgeEstimateMatrix rawTestingData, List<String> assignmentsForClassification);
}
