package edu.ithaca.dragon.tecmap.io.record;

import edu.ithaca.dragon.tecmap.Settings;
import edu.ithaca.dragon.tecmap.conceptgraph.ConceptGraph;
import edu.ithaca.dragon.tecmap.conceptgraph.ConceptNode;
import edu.ithaca.dragon.tecmap.conceptgraph.ExampleConceptGraphFactory;
import edu.ithaca.dragon.tecmap.conceptgraph.ExampleConceptGraphRecordFactory;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by tdragon on 5/3/17.
 */
public class ConceptGraphRecordTest {
    public static final String TEST_DIR = Settings.TEST_RESOURCE_DIR + "practicalExamples/SystemCreated/";


    @Test
    public void buildFromJsonTest() throws Exception {
        try {
            String file = TEST_DIR + "simple.json";
            ConceptGraphRecord toFile = ExampleConceptGraphRecordFactory.makeSimple();
            toFile.writeToJson(file);

            ConceptGraphRecord fromFile = ConceptGraphRecord.buildFromJson(file);

            assertThat(toFile.getConcepts(), is(fromFile.getConcepts()));
            assertThat(toFile.getLinks(), is(fromFile.getLinks()));

            file = TEST_DIR + "superComplex.json";
            toFile = ExampleConceptGraphRecordFactory.makeSuperComplex();
            toFile.writeToJson(file);
            fromFile = ConceptGraphRecord.buildFromJson(file);

            assertThat(toFile.getConcepts(), is(fromFile.getConcepts()));
            assertThat(toFile.getLinks(), is(fromFile.getLinks()));
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void buildFromGraphWResourcesTest(){
        ConceptGraph graph = ExampleConceptGraphFactory.makeSimpleCompleteWithData();
        ConceptGraphRecord graphRecord = graph.buildConceptGraphRecord();
        //Test for resources
        for (ConceptRecord conceptRecord: graphRecord.getConcepts()){
            ConceptNode node = graph.findNodeById(conceptRecord.getId());
            assertEquals(node.getAssessmentItemMap().size(), conceptRecord.getResourceSummaries().size());
        }
    }

}