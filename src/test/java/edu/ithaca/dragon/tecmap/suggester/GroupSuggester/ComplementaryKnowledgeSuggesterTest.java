package edu.ithaca.dragon.tecmap.suggester.GroupSuggester;

import edu.ithaca.dragon.tecmap.Settings;
import edu.ithaca.dragon.tecmap.conceptgraph.CohortConceptGraphs;
import edu.ithaca.dragon.tecmap.conceptgraph.ConceptGraph;
import edu.ithaca.dragon.tecmap.conceptgraph.ConceptNode;
import edu.ithaca.dragon.tecmap.io.record.ConceptGraphRecord;
import edu.ithaca.dragon.tecmap.io.record.LearningResourceRecord;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItemResponse;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ComplementaryKnowledgeSuggesterTest {

    @Test
    void isComplementary() throws IOException {
        ConceptGraph graph = new ConceptGraph(ConceptGraphRecord.buildFromJson(Settings.TEST_RESOURCE_DIR + "ManuallyCreated/simpleConceptGraphTest.json"));
        List<AssessmentItemResponse> assessmentItemResponses = AssessmentItemResponse.createAssessmentItemResponses(Arrays.asList(Settings.TEST_RESOURCE_DIR + "ManuallyCreated/simpleAssessmentTest.csv"));
        List<LearningResourceRecord> links = LearningResourceRecord.createLearningResourceRecordsFromJsonFiles(Arrays.asList(Settings.TEST_RESOURCE_DIR + "ManuallyCreated/simpleResourceTest.json"));
        graph.addLearningResourcesFromRecords(links);
        CohortConceptGraphs graphs = new CohortConceptGraphs(graph, assessmentItemResponses);

        ConceptGraph gr = graphs.getUserGraph("s1");
        ConceptNode node1 = gr.findNodeById("A");

        ConceptGraph gr2 = graphs.getUserGraph("s2");
        ConceptNode node2 = gr2.findNodeById("A");

        ConceptGraph gr3 = graphs.getUserGraph("s3");
        ConceptNode node3 = gr3.findNodeById("A");

        ConceptGraph gr4 = graphs.getUserGraph("s4");
        ConceptNode node4 = gr4.findNodeById("A");

        try {
            assertEquals(ComplementaryKnowledgeSuggester.isComplementary(node1, node2), true);
            assertEquals(ComplementaryKnowledgeSuggester.isComplementary(node1, node3), false);
            assertEquals(ComplementaryKnowledgeSuggester.isComplementary(node1, node4), false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}