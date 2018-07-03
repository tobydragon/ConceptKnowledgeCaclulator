package edu.ithaca.dragon.tecmap.prediction;

import edu.ithaca.dragon.tecmap.conceptgraph.eval.KnowledgeEstimateMatrix;

import java.util.List;

public interface LearningPredictor extends Predictor {

    /**
     * Trains the predictor given the rawTrainingData
     * @param rawTrainingData
     * @param assessmentToLearn
     * @param assignmentsToLearnWith
     */
    void learnSet(KnowledgeEstimateMatrix rawTrainingData, String assessmentToLearn, List<String> assignmentsToLearnWith) throws RuntimeException;

    /**
     * Predictor forgets and unlearns everything
     */
    void reset();

}
