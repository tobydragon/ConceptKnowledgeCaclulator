package edu.ithaca.dragon.tecmap.comp220Testing;

import com.opencsv.exceptions.CsvException;
import edu.ithaca.dragon.tecmap.Settings;
import edu.ithaca.dragon.tecmap.conceptgraph.CohortConceptGraphs;
import edu.ithaca.dragon.tecmap.conceptgraph.ConceptGraph;
import edu.ithaca.dragon.tecmap.conceptgraph.ConceptNode;
import edu.ithaca.dragon.tecmap.io.reader.*;
import edu.ithaca.dragon.tecmap.io.record.ConceptGraphRecord;
import edu.ithaca.dragon.tecmap.io.record.LearningResourceRecord;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItemResponse;
import edu.ithaca.dragon.tecmap.suggester.ConceptGraphSuggesterLibrary;
import edu.ithaca.dragon.tecmap.suggester.LearningResourceSuggestion;
import edu.ithaca.dragon.tecmap.suggester.OrganizedLearningResourceSuggestions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Comp220Testing {

    private CohortConceptGraphs cohortConceptGraphs;

    @BeforeEach
    public void setup() throws IOException, CsvException {
        //create the graph structure to be copied for each user
        ConceptGraphRecord structureRecord = ConceptGraphRecord.buildFromJson(Settings.TEST_RESOURCE_DIR + "comp220_Summer2024/graph.json");
        List<LearningResourceRecord> linkRecord = LearningResourceRecord.createLearningResourceRecordsFromJsonFile(Settings.TEST_RESOURCE_DIR + "comp220_Summer2024/learningResources.json");
        ConceptGraph graph = new ConceptGraph(structureRecord, linkRecord);

        //create the data to be used to create and populate the graph copies
        List<String[]> rows = CsvFileLibrary.parseRowsFromFile(Settings.TEST_RESOURCE_DIR + "comp220_Summer2024/assessmentGrades.csv");
        List<CsvProcessor> processors = new ArrayList<>();
        processors.add(new CanvasConverter());
        TecmapCSVReader tecmapCsvReader = new CanvasReader(rows, processors);
        List<AssessmentItemResponse> assessments = tecmapCsvReader.getManualGradedResponses();

        //create the average and individual graphs
        cohortConceptGraphs = new CohortConceptGraphs(graph, assessments);
    }

    @Test
    public void learningResourceSuggestionTest() {
        ConceptGraph userGraph3 = cohortConceptGraphs.getUserGraph("s03");
        List<ConceptNode> concepts3 = ConceptGraphSuggesterLibrary.suggestConcepts(userGraph3);

        assertEquals(concepts3.size(), 9);
        List<String> expectedConcepts3 = Arrays.asList("Chained Hash Tables", "Empirical", "Stack", "BigO", "Queue", "Binary Trees", "Map", "Theoretical", "List");
        List<String> conceptIds3 = concepts3.stream().map(ConceptNode::getID).toList();
        assertThat(conceptIds3, containsInAnyOrder(expectedConcepts3.toArray()));

        OrganizedLearningResourceSuggestions res3 = new OrganizedLearningResourceSuggestions(userGraph3, concepts3);

        List<LearningResourceSuggestion> incompleteTest3 = res3.incompleteList;
        List<LearningResourceSuggestion> wrongTest3 = res3.wrongList;
//        System.out.println(wrongTest3);

        assertEquals(incompleteTest3.size(), 0);
        // not suggest Q1 because Q1 connected to Array which is not on the suggested concept list
        List<String> expectedWrongList3 = Arrays.asList("Q2", "Q2", "Q2", "Q3", "Q4", "Q5", "Q5", "Q5", "Q6", "Q6", "Q6");
        List<String> wronglist3 = wrongTest3.stream().map(LearningResourceSuggestion::getId).toList();
        assertThat(wronglist3, containsInAnyOrder(expectedWrongList3.toArray()));
    }

}
