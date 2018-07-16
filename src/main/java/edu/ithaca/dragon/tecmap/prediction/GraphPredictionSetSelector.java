package edu.ithaca.dragon.tecmap.prediction;

import edu.ithaca.dragon.tecmap.conceptgraph.ConceptGraph;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GraphPredictionSetSelector extends NoStructurePredictionSetSelector {

    private ConceptGraph graph;

    public GraphPredictionSetSelector(ConceptGraph graph) {
        this.graph = graph;
    }

    /**
     * Uses the graph's list of assessments and calls the other form of this function with that list of assessments
     * @param studentIdToDecide
     * @param assessmentToPredict
     * @return
     */
    public List<String> getLearningSetForGivenStudent(String studentIdToDecide, String assessmentToPredict) {
        List<AssessmentItem> allAssessments = new ArrayList<>();
        allAssessments.addAll(this.graph.getAssessmentItemMap().values());
        return getLearningSetForGivenStudent(allAssessments, studentIdToDecide, assessmentToPredict);
    }

    /**
     * Gets the list of learning assessments based on the structure of the graph, the assessmentToPredict, and the assessments available
     * for the given student
     * @param allAssessments
     * @param studentIdToDecideSet
     * @param assessmentToPredict
     * @return
     * @throws IOException
     */
    @Override
    public List<String> getLearningSetForGivenStudent(List<AssessmentItem> allAssessments, String studentIdToDecideSet, String assessmentToPredict) {
        List<String> defaultSet = super.getLearningSetForGivenStudent(allAssessments, studentIdToDecideSet, assessmentToPredict);

        List<String> origlearningSet = graph.getAssessmentsBelowAssessmentID(assessmentToPredict);
        List<String> finalLearningSet = new ArrayList<>();

        for (String assessmentId : origlearningSet) {
            if (defaultSet.contains(assessmentId)) {
                finalLearningSet.add(assessmentId);
            }
        }

        //List of origlearningSet should always contain the assessmentToPredict (with data or without)
        if (!finalLearningSet.contains(assessmentToPredict)) {
            finalLearningSet.add(assessmentToPredict);
        }
        return finalLearningSet;
    }
}
