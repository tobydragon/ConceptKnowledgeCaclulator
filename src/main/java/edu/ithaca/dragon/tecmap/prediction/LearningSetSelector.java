package edu.ithaca.dragon.tecmap.prediction;

import edu.ithaca.dragon.tecmap.conceptgraph.ConceptGraph;

import java.io.IOException;
import java.util.List;

public interface LearningSetSelector {

    List<String> getLearningSet(ConceptGraph graph, String studentIdToDecideSet, String assessmentToPredict) throws IOException;

}
