package edu.ithaca.dragon.tecmap.prediction;

import edu.ithaca.dragon.tecmap.conceptgraph.ConceptGraph;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItem;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItemResponse;
import edu.ithaca.dragon.tecmap.learningresource.ContinuousAssessmentMatrix;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NoStructureLearningSetSelector implements LearningSetSelector {

    public NoStructureLearningSetSelector() {
    }

    /**
     * Gets the list of learning assessments based on the assessmentToPredict, and the assessments available for the given student
     * @param graph
     * @param studentIdToDecideSet
     * @param assessmentToPredict
     * @return
     * @throws IOException
     */
    @Override
    public List<String> getLearningSet(ConceptGraph graph, String studentIdToDecideSet, String assessmentToPredict) throws IOException {
        List<AssessmentItem> assessmentItems = new ArrayList<>();
        assessmentItems.addAll(graph.getAssessmentItemMap().values());
        ContinuousAssessmentMatrix graphMatrix = new ContinuousAssessmentMatrix(assessmentItems);

        List<String> learningSet = new ArrayList<>();

        //Only includes assessments that have responses for the given student
        for (AssessmentItem item : graphMatrix.getAssessmentItems()) {
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
}
