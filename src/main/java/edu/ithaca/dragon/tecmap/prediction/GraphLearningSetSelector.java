package edu.ithaca.dragon.tecmap.prediction;

import edu.ithaca.dragon.tecmap.conceptgraph.ConceptGraph;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GraphLearningSetSelector extends BaseLearningSetSelector {

    public GraphLearningSetSelector() {
    }

    @Override
    public List<String> getLearningSet(ConceptGraph graph, String studentIdToDecideSet, String assessmentToPredict) throws IOException {
        List<String> defaultSet = super.getLearningSet(graph, studentIdToDecideSet, assessmentToPredict);

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
