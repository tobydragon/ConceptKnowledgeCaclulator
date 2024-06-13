package edu.ithaca.dragon.tecmap.comp220Testing;

import edu.ithaca.dragon.tecmap.Settings;
import edu.ithaca.dragon.tecmap.conceptgraph.CohortConceptGraphs;
import edu.ithaca.dragon.tecmap.conceptgraph.ConceptGraph;
import edu.ithaca.dragon.tecmap.conceptgraph.ConceptNode;
import edu.ithaca.dragon.tecmap.io.reader.SakaiReader;
import edu.ithaca.dragon.tecmap.io.reader.TecmapCSVReader;
import edu.ithaca.dragon.tecmap.io.record.ConceptGraphRecord;
import edu.ithaca.dragon.tecmap.io.record.LearningResourceRecord;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItemResponse;
import edu.ithaca.dragon.tecmap.suggester.ConceptGraphSuggesterLibrary;
import edu.ithaca.dragon.tecmap.suggester.LearningResourceSuggestion;
import edu.ithaca.dragon.tecmap.suggester.OrganizedLearningResourceSuggestions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Comp220Testing {

    @Test
    public void comp220Test() throws IOException {

        //create the graph structure to be copied for each user
        ConceptGraphRecord structureRecord = ConceptGraphRecord.buildFromJson(Settings.TEST_RESOURCE_DIR + "comp220_Summer2024/graph.json");
        List<LearningResourceRecord> linkRecord = LearningResourceRecord.createLearningResourceRecordsFromJsonFile(Settings.TEST_RESOURCE_DIR + "comp220_Summer2024/learningMaterials.json");
        ConceptGraph graph = new ConceptGraph(structureRecord, linkRecord);

        //create the data to be used to create and populate the graph copies
        TecmapCSVReader tecmapCsvReader = new SakaiReader(Settings.TEST_RESOURCE_DIR + "comp220_Summer2024/assessmentGrades.csv");
        List<AssessmentItemResponse> assessments = tecmapCsvReader.getManualGradedResponses();

        //create the average and individual graphs
        CohortConceptGraphs cohortConceptGraphs = new CohortConceptGraphs(graph, assessments);

        ConceptGraph userGraph = cohortConceptGraphs.getUserGraph("s03");
        List<ConceptNode> concepts = ConceptGraphSuggesterLibrary.suggestConcepts(userGraph);
//        System.out.println(concepts);

        assertEquals(concepts.size(), 9);
        List<String> expectedConcepts = Arrays.asList("Chained Hash Tables", "Empirical", "Stack", "BigO", "Queue", "Binary Trees", "Map", "Theoretical", "List");
        List<String> conceptIds = concepts.stream().map(ConceptNode::getID).toList();
        assertThat(conceptIds, containsInAnyOrder(expectedConcepts.toArray()));

        OrganizedLearningResourceSuggestions res = new OrganizedLearningResourceSuggestions(userGraph, concepts);

        List<LearningResourceSuggestion> incompleteTest = res.incompleteList;
        List<LearningResourceSuggestion> wrongTest = res.wrongList;
        System.out.println(wrongTest);

        // tests should not fail but are failed
        assertEquals(incompleteTest.size(), 0);
        List<String> expectedWrongList = Arrays.asList("Q1", "Q2", "Q2", "Q2", "Q3", "Q4", "Q5", "Q5", "Q5", "Q6", "Q6", "Q6");
        List<String> wronglist = wrongTest.stream().map(LearningResourceSuggestion::getId).toList();
        assertThat(wronglist, containsInAnyOrder(expectedWrongList.toArray()));


    }
}
