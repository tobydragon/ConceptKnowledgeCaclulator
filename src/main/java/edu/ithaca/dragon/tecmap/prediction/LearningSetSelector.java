package edu.ithaca.dragon.tecmap.prediction;

import edu.ithaca.dragon.tecmap.conceptgraph.ConceptGraph;

import java.io.IOException;
import java.util.List;

public interface LearningSetSelector {

    /**
     * Gets the list of assessmentIds based on the given student, and the graph provided
     * @param graph
     * @param studentIdToDecideSet
     * @param assessmentToPredict
     * @return
     * @throws IOException
     */
    List<String> getLearningSet(ConceptGraph graph, String studentIdToDecideSet, String assessmentToPredict) throws IOException;

}
