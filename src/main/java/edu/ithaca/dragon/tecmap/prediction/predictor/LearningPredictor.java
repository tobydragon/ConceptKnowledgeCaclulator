package edu.ithaca.dragon.tecmap.prediction.predictor;

import edu.ithaca.dragon.tecmap.learningresource.ContinuousAssessmentMatrix;

import java.util.List;

public interface LearningPredictor extends Predictor {

    /**
     * Trains the predictor given the rawTrainingData
     * @param rawTrainingData
     * @param assessmentToLearn
     * @param assignmentsToLearnWith
     */
    void learnSet(ContinuousAssessmentMatrix rawTrainingData, String assessmentToLearn, List<String> assignmentsToLearnWith) throws RuntimeException;

    /**
     * Predictor forgets and unlearns everything
     */
    void reset();

}
