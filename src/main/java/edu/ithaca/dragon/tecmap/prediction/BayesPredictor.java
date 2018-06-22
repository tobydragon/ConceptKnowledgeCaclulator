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
    private DataFrame toDataFrame(KnowledgeEstimateMatrix rawData) {
        return null;
    }

    /**
     * Trains the data based on the raw data matrix you give it
     * @param rawTrainingData in the form of KnowledgeEstimateMatrix
     * TRAINING DATA MUST BE MANIPULATED IN ORDER TO USE THE BAYES LEARN METHOD
     */
    public void learn(KnowledgeEstimateMatrix rawTrainingData) {

    }

    /**
     * classifies a single "row" of data in the form of a collection
     * @param testData a "row" of doubles(grades) for a single student
     * Uses Bayes Classify Method
     * @return String classification for a student
     */
    private String classify(Collection<Double> testData) {
        return null;
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
