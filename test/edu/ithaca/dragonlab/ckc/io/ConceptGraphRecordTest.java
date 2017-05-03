package edu.ithaca.dragonlab.ckc.io;

import edu.ithaca.dragonlab.ckc.conceptgraph.ExampleConceptGraphRecordFactory;
import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

/**
 * Created by tdragon on 5/3/17.
 */
public class ConceptGraphRecordTest {
    public static final String TEST_DIR = "test/testresources/io/";


    @Test
    public void buildFromJsonSimpleTest() throws Exception {
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



}