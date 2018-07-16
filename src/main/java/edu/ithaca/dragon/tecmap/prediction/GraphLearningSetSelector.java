package edu.ithaca.dragon.tecmap.prediction;

import edu.ithaca.dragon.tecmap.conceptgraph.ConceptGraph;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GraphLearningSetSelector extends NoStructureLearningSetSelector {

    private ConceptGraph graph;

    public GraphLearningSetSelector(ConceptGraph graph) {
        this.graph = graph;
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
    public List<String> getLearningSetForGivenStudent(List<AssessmentItem> allAssessments, String studentIdToDecideSet, String assessmentToPredict) throws IOException {
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
