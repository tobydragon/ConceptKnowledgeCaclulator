package edu.ithaca.dragon.tecmap.prediction;

import ch.netzwerg.paleo.DataFrame;
import edu.ithaca.dragon.tecmap.conceptgraph.eval.KnowledgeEstimateMatrix;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimplePredictor implements Predictor {

    /**
     * Classifies the assessmentData based on a raw average
     * AT-RISK defined at 0.78 or below
     * @param assessmentData
     * @return
     */
    private String classify(Collection<Double> assessmentData) {
        double total = 0.0;
        for (Double grade : assessmentData) {
            total += grade;
        }
        //Calc the average
        double avg = total/((double) assessmentData.size());
        if (avg <= Predictor.ESTIMATE_THRESHOLD) {
            return "AT-RISK";
        } else {
            return "OK";
        }
    }

    /**
     * Classifies the data based on the raw testing matrix you give it, should not have a grade for what you are predicting
     * @param rawTestingData in the form of KnowledgeEstimateMatrix
     * @param assignmentsForClassification list of what assessment columns should be used in learning (should all be doubles, not the categorical variable)
     * TESTING DATA MUST BE MANIPULATED IN ORDER TO GET ROWS FOR THE BAYES CLASSIFY METHOD
     * @return Map of String to String (Student id -> Classification) MAY CHANGE
     */
    public Map<String, String> classifySet(KnowledgeEstimateMatrix rawTestingData, List<String> assignmentsForClassification) {
        //Get a dataframe from the rawTestingData
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
