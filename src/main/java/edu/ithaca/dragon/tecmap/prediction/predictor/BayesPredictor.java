package edu.ithaca.dragon.tecmap.prediction.predictor;

import ch.netzwerg.paleo.DataFrame;
import de.daslaboratorium.machinelearning.classifier.Classifier;
import de.daslaboratorium.machinelearning.classifier.bayes.BayesClassifier;
import edu.ithaca.dragon.tecmap.learningresource.ContinuousAssessmentMatrix;
import edu.ithaca.dragon.tecmap.learningresource.DiscreteAssessmentMatrix;
import edu.ithaca.dragon.tecmap.learningresource.GradeDiscreteGroupings;
import io.vavr.Tuple2;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BayesPredictor implements LearningPredictor {

    private Classifier<String, String> bayesClassifier;
    private GradeDiscreteGroupings defaultGroupings;
    private GradeDiscreteGroupings atriskGroupings;

    /**
     * Sets memory capacity to 500 as a default
     */
    public BayesPredictor(GradeDiscreteGroupings defaultGroupings, GradeDiscreteGroupings atriskGroupings) {
        bayesClassifier = new BayesClassifier<>();
        bayesClassifier.setMemoryCapacity(500);
        this.defaultGroupings = defaultGroupings;
        this.atriskGroupings = atriskGroupings;
    }

    /**
     * Resets and unlearns everything, resets category count
     * TESTED ON PACKAGE BUILD, NOT NATIVELY
     */
    public void reset() {
        bayesClassifier.reset();
        bayesClassifier.setMemoryCapacity(500);
    }

    /**
     * Learns given the necessary data, category that assignment belongs to based on features given
     * @param category
     * @param grades
     * TESTED ON PACKAGE BUILD, NOT NATIVELY
     */
    private void learn(String category, Collection<String> grades) {
        bayesClassifier.learn(category, grades);
    }

    /**
     * Trains data given a continuousAssessmentMatrix of raw numeric data
     * @param rawTrainingData
     * @param assessmentToLearn
     * @param assessmentsToLearnWith
     */
    public void learnSet(ContinuousAssessmentMatrix rawTrainingData, String assessmentToLearn, List<String> assessmentsToLearnWith) {
        if (!assessmentsToLearnWith.contains(assessmentToLearn)) {
            throw new RuntimeException("Assignments to Learn Must include assessmentToLearn");
        }
        DiscreteAssessmentMatrix discreteRawTrainingData = new DiscreteAssessmentMatrix(rawTrainingData.getAssessmentItems(), defaultGroupings, assessmentToLearn, atriskGroupings);
        //Get a dataframe from the raw training data
        DataFrame rawTrainingDataframe = DiscreteDataFrameFunctions.toDataFrame(discreteRawTrainingData, assessmentsToLearnWith);

        //Get rows from the discretized dataframe
        Map<String, Tuple2<String, Collection<String>>> trainingRows = DiscreteDataFrameFunctions.getRowsToLearn(rawTrainingDataframe, assessmentsToLearnWith, assessmentToLearn);

        //For each row, call learn
        for (Tuple2<String, Collection<String>> row: trainingRows.values()) {
            learn(row._1, row._2);
        }
    }

    /**
     * classifies a single "row" of data in the form of a collection
     * @param testData a "row" of doubles(grades) for a single student
     * Uses Bayes Classify Method
     * TESTED ON PACKAGE BUILD, NOT NATIVELY
     * @return String classification for a student
     */
    public String classify(Collection<String> testData) {
        return bayesClassifier.classify(testData).getCategory();
    }

    /**
     * Classifies data based on the continuousAssessmentMatrix given, should not have a grade for what you are predicting
     * @param rawTestingData
     * @param assessmentsForClassifications
     * @return
     */
    public Map<String, String> classifySet(ContinuousAssessmentMatrix rawTestingData, List<String> assessmentsForClassifications) {
        DiscreteAssessmentMatrix discreteTestingData = new DiscreteAssessmentMatrix(rawTestingData.getAssessmentItems(), defaultGroupings);
        //Get a dataframe from the raw testing data
        DataFrame rawTestingDataframe = DiscreteDataFrameFunctions.toDataFrame(discreteTestingData, assessmentsForClassifications);

        //Get rows from the dataframe (studentId -> grades)
        Map<String, Collection<String>> testingRows = DiscreteDataFrameFunctions.getRowsToTest(rawTestingDataframe, assessmentsForClassifications);

        //Get classifications for data
        Map<String, String> classifications = new HashMap<>();

        //For each row, call classify
        for (String studentId : testingRows.keySet()) {
            String classification = classify(testingRows.get(studentId));
            classifications.put(studentId, classification);
        }

        return classifications;
    }
}
