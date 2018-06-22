package edu.ithaca.dragon.tecmap.prediction;

import com.github.chen0040.data.frame.DataFrame;
import de.daslaboratorium.machinelearning.classifier.Classifier;
import de.daslaboratorium.machinelearning.classifier.bayes.BayesClassifier;
import edu.ithaca.dragon.tecmap.conceptgraph.eval.KnowledgeEstimateMatrix;

import java.util.Collection;
import java.util.Map;

public class BayesPredictor implements Predictor {

    private Classifier<Double, String> bayesClassifier;

    /**
     * Default constructor
     * Sets memory capacity to 500 as a default
     */
    public BayesPredictor() {
        bayesClassifier = new BayesClassifier<>();
        bayesClassifier.setMemoryCapacity(500);
    }

    /**
     * Converts KnowledgeEstimateMatrix into a Dataframe
     * @param rawData
     * @return DataFrame that is easier to use
     */
    public DataFrame toDataFrame(KnowledgeEstimateMatrix rawData) {
        return null;
    }

    /**
     * Resets and unlearns everything, resets category count
     * TESTED ON PACKAGE BUILD, NOT NATIVELY
     */
    public void reset() {
        bayesClassifier.reset();
    }

    /**
     * Learns given the necessary data, category that assignment belongs to based on features given
     * @param category
     * @param grades
     * TESTED ON PACKAGE BUILD, NOT NATIVELY
     */
    public void learn(String category, Collection<Double> grades) {
        bayesClassifier.learn(category, grades);
    }

    /**
     * Trains the data based on the raw data matrix you give it
     * @param rawTrainingData in the form of KnowledgeEstimateMatrix
     * TRAINING DATA MUST BE MANIPULATED IN ORDER TO USE THE BAYES LEARN METHOD
     */
    public void learnSet(KnowledgeEstimateMatrix rawTrainingData) {

    }

    /**
     * classifies a single "row" of data in the form of a collection
     * @param testData a "row" of doubles(grades) for a single student
     * Uses Bayes Classify Method
     * TESTED ON PACKAGE BUILD, NOT NATIVELY
     * @return String classification for a student
     */
    private String classify(Collection<Double> testData) {
        return bayesClassifier.classify(testData).getCategory();
    }

    /**
     * Classifies the data based on the raw testing matrix you give it
     * @param rawTestingData in the form of KnowledgeEstimateMatrix
     * TESTING DATA MUST BE MANIPULATED IN ORDER TO GET ROWS FOR THE NATIVE CLASSIFY METHOD
     * @return Map of String to String (Student id -> Classification) MAY CHANGE
     */
    public Map<String, String> classifySet(KnowledgeEstimateMatrix rawTestingData) {
        return null;
    }
}
