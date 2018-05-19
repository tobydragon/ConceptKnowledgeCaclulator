package edu.ithaca.dragon.tecmap.suggester.GroupSuggester;

import edu.ithaca.dragon.tecmap.ConceptKnowledgeCalculator;
import edu.ithaca.dragon.tecmap.ConceptKnowledgeCalculatorAPI;
import edu.ithaca.dragon.tecmap.Settings;
import edu.ithaca.dragon.tecmap.conceptgraph.CohortConceptGraphs;
import edu.ithaca.dragon.tecmap.conceptgraph.ConceptGraph;
import edu.ithaca.dragon.tecmap.conceptgraph.ConceptNode;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ComplementaryKnowledgeSuggesterTest {

    @Test
    void isComplementary() {
        ConceptKnowledgeCalculatorAPI ckc = null;

        try {
            ckc = new ConceptKnowledgeCalculator(Settings.TEST_RESOURCE_DIR + "ManuallyCreated/simpleConceptGraphTest.json", Settings.TEST_RESOURCE_DIR + "ManuallyCreated/simpleResourceTest.json", Settings.TEST_RESOURCE_DIR + "ManuallyCreated/simpleAssessmentTest.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }

        CohortConceptGraphs graphs = ckc.getCohortConceptGraphs();

        ConceptGraph gr = graphs.getUserGraph("s1");
        ConceptNode node1 = gr.findNodeById("A");

        ConceptGraph gr2 = graphs.getUserGraph("s2");
        ConceptNode node2 = gr2.findNodeById("A");

        ConceptGraph gr3 = graphs.getUserGraph("s3");
        ConceptNode node3 = gr3.findNodeById("A");

        ConceptGraph gr4 = graphs.getUserGraph("s4");
        ConceptNode node4 = gr4.findNodeById("A");

        try {
            Assert.assertEquals(ComplementaryKnowledgeSuggester.isComplementary(node1, node2), true);
            Assert.assertEquals(ComplementaryKnowledgeSuggester.isComplementary(node1, node3), false);
            Assert.assertEquals(ComplementaryKnowledgeSuggester.isComplementary(node1, node4), false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}