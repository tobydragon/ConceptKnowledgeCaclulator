package edu.ithaca.dragon.tecmap.prediction;

import edu.ithaca.dragon.tecmap.conceptgraph.ConceptGraph;
import edu.ithaca.dragon.tecmap.conceptgraph.eval.KnowledgeEstimateMatrix;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItem;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItemResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BaseLearningSetSelector implements LearningSetSelector {

    public BaseLearningSetSelector() {
    }

    @Override
    public List<String> getLearningSet(ConceptGraph graph, String studentIdToDecideSet, String assessmentToPredict) throws IOException {
        List<AssessmentItem> assessmentItems = new ArrayList<>();
        assessmentItems.addAll(graph.getAssessmentItemMap().values());
        KnowledgeEstimateMatrix graphMatrix = new KnowledgeEstimateMatrix(assessmentItems);

        List<String> learningSet = new ArrayList<>();

        //Only includes assessments that have responses for the given student
        for (AssessmentItem item : graphMatrix.getObjList()) {
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
