package edu.ithaca.dragon.tecmap.prediction;

import edu.ithaca.dragon.tecmap.learningresource.ContinuousAssessmentMatrix;
import edu.ithaca.dragon.tecmap.learningresource.GradeDiscreteGroupings;

import java.util.*;

public class SimplePredictor implements Predictor {

    private GradeDiscreteGroupings atriskGroupings;
    private List<String> groups;
    private List<Integer> pointBreak;

    public SimplePredictor(GradeDiscreteGroupings atriskGroupings) {
        groups = atriskGroupings.getGroups();
        pointBreak = atriskGroupings.getPointBreaks();
    }

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
        String classification = null;
        for (int i = 0; i < pointBreak.size(); i++) {
            if (avg*100 >= pointBreak.get(i) && classification == null) {
                classification = groups.get(i);
            }
            if (classification == null) {
                classification = groups.get(pointBreak.size());
            }
        }
        return classification;
    }

    /**
     * Classifies the data based on the raw testing matrix you give it, should not have a grade for what you are predicting
     * @param rawTestingData in the form of KnowledgeEstimateMatrix
     * @param assessmentsForClassification list of what assessment columns should be used in learning (should all be doubles, not the categorical variable)
     * TESTING DATA MUST BE MANIPULATED IN ORDER TO GET ROWS FOR THE BAYES CLASSIFY METHOD
     * @return Map of String to String (Student id -> Classification) MAY CHANGE
     */
    public Map<String, String> classifySet(ContinuousAssessmentMatrix rawTestingData, List<String> assessmentsForClassification) {
        double[][] studentGrades = rawTestingData.getStudentAssessmentGrades();
        List<String> studentIds = rawTestingData.getStudentIds();
        List<String> assessmentIds = rawTestingData.getAssessmentIds();
        Map<String, String> classifications = new HashMap<>();

        for (String student : studentIds) {
            int studentIndex = studentIds.indexOf(student);
            Collection<Double> studentData = new ArrayList<>();
            for (String assessment : assessmentsForClassification) {
                studentData.add(studentGrades[assessmentIds.indexOf(assessment)][studentIndex]);
            }
            classifications.put(student, classify(studentData));
        }
        return classifications;
    }

}
