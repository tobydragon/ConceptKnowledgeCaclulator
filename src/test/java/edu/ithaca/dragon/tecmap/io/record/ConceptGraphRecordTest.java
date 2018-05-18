package edu.ithaca.dragon.tecmap.io.record;

import edu.ithaca.dragon.tecmap.Settings;
import edu.ithaca.dragon.tecmap.conceptgraph.ConceptGraph;
import edu.ithaca.dragon.tecmap.conceptgraph.ConceptNode;
import edu.ithaca.dragon.tecmap.conceptgraph.ExampleConceptGraphFactory;
import edu.ithaca.dragon.tecmap.conceptgraph.ExampleConceptGraphRecordFactory;
import edu.ithaca.dragon.tecmap.io.record.ConceptGraphRecord;
import edu.ithaca.dragon.tecmap.io.record.ConceptRecord;
import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

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

            Assert.assertThat(toFile.getConcepts(), is(fromFile.getConcepts()));
            Assert.assertThat(toFile.getLinks(), is(fromFile.getLinks()));

            file = TEST_DIR + "superComplex.json";
            toFile = ExampleConceptGraphRecordFactory.makeSuperComplex();
            toFile.writeToJson(file);
            fromFile = ConceptGraphRecord.buildFromJson(file);

            Assert.assertThat(toFile.getConcepts(), is(fromFile.getConcepts()));
            Assert.assertThat(toFile.getLinks(), is(fromFile.getLinks()));
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
            Assert.assertEquals(node.getLearningObjectMap().size(), conceptRecord.getResourceSummaries().size());
        }
    }

}