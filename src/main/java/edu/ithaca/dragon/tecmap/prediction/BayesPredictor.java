package edu.ithaca.dragon.tecmap.prediction;

import ch.netzwerg.paleo.DataFrame;
import de.daslaboratorium.machinelearning.classifier.Classifier;
import de.daslaboratorium.machinelearning.classifier.bayes.BayesClassifier;
import edu.ithaca.dragon.tecmap.conceptgraph.eval.KnowledgeEstimateMatrix;
import edu.ithaca.dragon.tecmap.learningresource.GradeDiscreteGroupings;
import io.vavr.Tuple2;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BayesPredictor implements LearningPredictor {

    private Classifier<Double, String> bayesClassifier;
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
    private void learn(String category, Collection<Double> grades) {
        bayesClassifier.learn(category, grades);
    }

    /**
     * Trains the data based on the raw data matrix you give it
     * @param rawTrainingData in the form of KnowledgeEstimateMatrix
     * @param assessmentToLearn string of the assessment you want to learn on (will be the categorical variable), should have doubles as grade type
     * @param assignmentsToLearnWith list of what columns should be used in learning (must include the assessmentToLearn)
     * TRAINING DATA MUST BE MANIPULATED IN ORDER TO USE THE BAYES LEARN METHOD
     */
    public void learnSet(KnowledgeEstimateMatrix rawTrainingData, String assessmentToLearn, List<String> assignmentsToLearnWith) {
        if (!assignmentsToLearnWith.contains(assessmentToLearn)) {
            throw new RuntimeException("Assignments to Learn Must include assessmentToLearn");
        }
        //Get a dataframe from the raw training data
        DataFrame rawTrainingDataframe = DataFrameFunctions.toDataFrame(rawTrainingData, assignmentsToLearnWith);

        //Get discretized column using assessmentToLearn param
        DataFrame discretizedTrainingDataframe = DataFrameFunctions.discretizeGradeColumn(rawTrainingDataframe, assessmentToLearn);

        //Get rows from the discretized dataframe
        Map<String, Tuple2<String, Collection<Double>>> trainingRows = DataFrameFunctions.getRowsToLearn(discretizedTrainingDataframe, assessmentToLearn);

        //For each row, call learn
        for (Tuple2<String, Collection<Double>> row: trainingRows.values()) {
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
    public String classify(Collection<Double> testData) {
        return bayesClassifier.classify(testData).getCategory();
    }

    /**
     * Classifies the data based on the raw testing matrix you give it, should not have a grade for what you are predicting
     * @param rawTestingData in the form of KnowledgeEstimateMatrix
     * @param assignmentsForClassification list of what assessment columns should be used in learning
     * TESTING DATA MUST BE MANIPULATED IN ORDER TO GET ROWS FOR THE BAYES CLASSIFY METHOD
     * @return Map of String to String (Student id -> Classification) MAY CHANGE
     */
    public Map<String, String> classifySet(KnowledgeEstimateMatrix rawTestingData, List<String> assignmentsForClassification) {
        //Get a dataframe from the raw testing data
        DataFrame rawTestingDataframe = DataFrameFunctions.toDataFrame(rawTestingData, assignmentsForClassification);

        //Get rows from the dataframe (studentId -> grades)
        Map<String, Collection<Double>> testingRows = DataFrameFunctions.getRowsToTest(rawTestingDataframe);

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
