package edu.ithaca.dragon.tecmap.prediction;

import edu.ithaca.dragon.tecmap.conceptgraph.ConceptGraph;
import edu.ithaca.dragon.tecmap.conceptgraph.eval.KnowledgeEstimateMatrix;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItem;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItemResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LearningSetSelector {

    /**
     * Gets the list of assessments to learn from given the matrix and the studentId and include the assessmentToLearn
     * @param matrix
     * @param studentIdToDecideSet
     * @param assessmentToPredict
     * @return list of strings containing the assessmentIds to be learned
     */
    public static List<String> getBaseLearningSet(KnowledgeEstimateMatrix matrix, String studentIdToDecideSet, String assessmentToPredict) {
        List<String> learningSet = new ArrayList<>();

        //Only includes assessments that have responses for the given student
        for (AssessmentItem item : matrix.getObjList()) {
            for (AssessmentItemResponse response : item.getResponses()) {
                if (response.getUserId().equals(studentIdToDecideSet)) {
                    learningSet.add(item.getId());
                }
            }
        }

        //List of learningSet should always contain the assessmentToPredict (with data or without)
        if (!learningSet.contains(assessmentToPredict)) {
            learningSet.add(assessmentToPredict);
        }
        return learningSet;
    }

    /**
     * Gets the list of assessments to learn from the graph structure given in the concept graph, studentId given, and the assessmentToLearn
     * @param graph
     * @param studentIdToDecideSet
     * @param assessmentToPredict
     * @return list of strings containing the assessmentIds to be learned dictated by graph structure given
     */
    public static List<String> getGraphLearningSet(ConceptGraph graph, String studentIdToDecideSet, String assessmentToPredict) throws IOException {


        List<String> learningSet = new ArrayList<>();

        //List of learningSet should always contain the assessmentToPredict (with data or without)
        if (!learningSet.contains(assessmentToPredict)) {
            learningSet.add(assessmentToPredict);
        }
        return learningSet;
    }

}
