package edu.ithaca.dragon.tecmap.tecmapstate;

import edu.ithaca.dragon.tecmap.Settings;
import edu.ithaca.dragon.tecmap.conceptgraph.ConceptGraph;
import edu.ithaca.dragon.tecmap.io.Json;
import edu.ithaca.dragon.tecmap.io.record.ConceptGraphRecord;
import edu.ithaca.dragon.tecmap.io.record.LearningResourceRecord;
import edu.ithaca.dragon.tecmap.tecmapExamples.Cs1ExampleJsonStrings;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;


class LinksNoAssessmentStateTest {

    @Test
    public void getResourceRecordLinksTest() throws IOException {
        LinksNoAssessmentState state = new LinksNoAssessmentState(
                new ConceptGraph(ConceptGraphRecord.buildFromJson(Settings.DEFAULT_TEST_DATASTORE_PATH + "Cs1Example/Cs1ExampleGraph.json")),
                LearningResourceRecord.createLearningResourceRecordsFromJsonFile(Settings.DEFAULT_TEST_DATASTORE_PATH + "Cs1Example/Cs1ExampleResources.json")
        );

        assertEquals(Cs1ExampleJsonStrings.resourcesConnectedString, Json.toJsonString(state.getResourceRecordLinks()));
        assertEquals(Cs1ExampleJsonStrings.structureWithResourceConnectionsAsTree, Json.toJsonString(state.createStructureTree()));
    }

}