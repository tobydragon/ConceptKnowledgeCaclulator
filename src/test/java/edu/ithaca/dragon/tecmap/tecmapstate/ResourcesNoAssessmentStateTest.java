package edu.ithaca.dragon.tecmap.tecmapstate;

import edu.ithaca.dragon.tecmap.Settings;
import edu.ithaca.dragon.tecmap.conceptgraph.ConceptGraph;
import edu.ithaca.dragon.tecmap.io.Json;
import edu.ithaca.dragon.tecmap.io.record.ConceptGraphRecord;
import edu.ithaca.dragon.tecmap.io.record.LearningResourceRecord;
import edu.ithaca.dragon.tecmap.tecmapExamples.Cs1ExampleJsonStrings;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;


class ResourcesNoAssessmentStateTest {

    @Test
    public void getResourceRecordLinksTest() throws IOException {
        ResourcesNoAssessmentState state = new ResourcesNoAssessmentState(
                new ConceptGraph(ConceptGraphRecord.buildFromJson(Settings.TEST_RESOURCE_DIR + "tecmapExamples/Cs1ExampleGraph.json")),
                LearningResourceRecord.createLearningResourceRecordsFromJsonFiles(new ArrayList<>(Arrays.asList(Settings.TEST_RESOURCE_DIR + "tecmapExamples/Cs1ExampleResources.json")))
        );

        assertEquals(Cs1ExampleJsonStrings.resourcesConnectedString, Json.toJsonString(state.getResourceRecordLinks()));
        assertEquals(Cs1ExampleJsonStrings.structureAsTreeString, Json.toJsonString(state.createStructureTree()));
    }

}