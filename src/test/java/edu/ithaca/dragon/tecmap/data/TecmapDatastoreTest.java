package edu.ithaca.dragon.tecmap.data;

import edu.ithaca.dragon.tecmap.Settings;
import edu.ithaca.dragon.tecmap.TecmapAPI;
import edu.ithaca.dragon.tecmap.io.Json;
import edu.ithaca.dragon.tecmap.io.record.ConceptGraphRecord;
import edu.ithaca.dragon.tecmap.tecmapExamples.Cs1ExampleJsonStrings;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class TecmapDatastoreTest {

    @Test
    void retrieveTecmapForId() throws IOException {
        TecmapFileDatastore ds = TecmapFileDatastore.buildFromJsonFile(Settings.DEFAULT_TEST_DATASTORE_FILE);


        assertNull(ds.retrieveTecmapForId("noSuchId"));
        assertNull(ds.retrieveTecmapForId("BadPaths"));
        //check a valid TecmapAPI
        TecmapAPI cs1ExampleMap = ds.retrieveTecmapForId("Cs1Example");
        assertEquals(Cs1ExampleJsonStrings.structureWithResourceConnectionsAsTree, cs1ExampleMap.createStructureTree().toJsonString());
        Collection<String> twoAssessmentsConnectedConcepts = cs1ExampleMap.createConceptIdListToPrint();
        assertEquals(Cs1ExampleJsonStrings.allConceptsString, twoAssessmentsConnectedConcepts.toString());
        List<ConceptGraphRecord> records =  cs1ExampleMap.createCohortTree().getGraphRecords();
        assertEquals(4, records.size());
        for (ConceptGraphRecord record : records){
            //Bart
            if (record.getName().equals("s02")){
                //System.out.println(Json.toJsonString(record));
                assertEquals(Cs1ExampleJsonStrings.bartDataTree, Json.toJsonString(record));
            }
        }

    }
}