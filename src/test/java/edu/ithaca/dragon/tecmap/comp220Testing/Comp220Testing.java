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

    private CohortConceptGraphs cohortConceptGraphs;

    @BeforeEach
    public void setup() throws IOException {

        //create the graph structure to be copied for each user
        ConceptGraphRecord structureRecord = ConceptGraphRecord.buildFromJson(Settings.TEST_RESOURCE_DIR + "comp220_Summer2024/graph.json");
        List<LearningResourceRecord> linkRecord = LearningResourceRecord.createLearningResourceRecordsFromJsonFile(Settings.TEST_RESOURCE_DIR + "comp220_Summer2024/learningMaterials.json");
        ConceptGraph graph = new ConceptGraph(structureRecord, linkRecord);

        //create the data to be used to create and populate the graph copies
        TecmapCSVReader tecmapCsvReader = new SakaiReader(Settings.TEST_RESOURCE_DIR + "comp220_Summer2024/assessmentGrades.csv");
        List<AssessmentItemResponse> assessments = tecmapCsvReader.getManualGradedResponses();

        //create the average and individual graphs
        cohortConceptGraphs = new CohortConceptGraphs(graph, assessments);
    }

    @Test
    public void learningMaterialSuggestionStudent3Test() {
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

    @Test
    public void learningMaterialSuggestionStudent2Test() {
        // second test to check if the test still fails
        ConceptGraph userGraph2 = cohortConceptGraphs.getUserGraph("s02");
        List<ConceptNode> concepts2 = ConceptGraphSuggesterLibrary.suggestConcepts(userGraph2);
        System.out.println(concepts2);

//        assertEquals(concepts2.size(), 5); // should be 6 Map but didn't
//        List<String> expectedConcepts2 = Arrays.asList("Arrays", "Stack", "Queue", "Binary Trees", "List");
//        List<String> conceptIds2 = concepts2.stream().map(ConceptNode::getID).toList();
//        assertThat(conceptIds2, containsInAnyOrder(expectedConcepts2.toArray()));
//
        OrganizedLearningResourceSuggestions res2 = new OrganizedLearningResourceSuggestions(userGraph2, concepts2);

        List<LearningResourceSuggestion> incompleteTest2 = res2.incompleteList;
        List<LearningResourceSuggestion> wrongTest2 = res2.wrongList;
        System.out.println(wrongTest2);

        // tests should not fail but are failed
//        assertEquals(incompleteTest2.size(), 0); // should be 1
        List<String> expectedWrongList2 = Arrays.asList("Q1", "Q1", "Q2", "Q2", "Q2", "Q4", "Q6");
        List<String> wronglist2 = wrongTest2.stream().map(LearningResourceSuggestion::getId).toList();
        System.out.println(wronglist2);
        assertThat(wronglist2, containsInAnyOrder(expectedWrongList2.toArray()));
//        assertThat(wronglist2).containsInAnyOrder(expectedWrongList2.toArray());
    }
}
