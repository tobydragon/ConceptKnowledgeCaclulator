package edu.ithaca.dragonlab.ckc.io;

import edu.ithaca.dragonlab.ckc.conceptgraph.CohortConceptGraphs;
import edu.ithaca.dragonlab.ckc.conceptgraph.ExampleConceptGraphFactory;
import edu.ithaca.dragonlab.ckc.learningobject.ExampleLearningObjectResponseFactory;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;

/**
 * Created by home on 5/20/17.
 */
public class CohortConceptGraphsRecordTest {
    public static final String TEST_DIR = "test/testresources/io/";

    @Test
    public void CohortConceptGraphsRecordTest(){
        try {
            CohortConceptGraphs graphs = new CohortConceptGraphs(ExampleConceptGraphFactory.makeSimpleStructureAndLearningObjects(), ExampleLearningObjectResponseFactory.makeSimpleResponses());
            CohortConceptGraphsRecord toFile = graphs.buildCohortConceptGraphsRecord();
            String file = TEST_DIR + "simpleCohort.json";
            toFile.writeToJson(file);

            CohortConceptGraphsRecord fromFile = CohortConceptGraphsRecord.buildFromJson(file);

            Assert.assertThat(toFile.getGraphRecords(), is(fromFile.getGraphRecords()));
        }
        catch (IOException e){
            e.printStackTrace();
            Assert.fail();
        }
    }

    public static void main(String[] args){
        try {
            CohortConceptGraphs graphs = new CohortConceptGraphs(ExampleConceptGraphFactory.makeSimpleStructureAndLearningObjects(), ExampleLearningObjectResponseFactory.makeSimpleResponses());
            CohortConceptGraphsRecord toFile = graphs.buildCohortConceptTreeRecord();
            String file = TEST_DIR + "simpleCohortTree.json";
            toFile.writeToJson(file);

            CohortConceptGraphsRecord fromFile = CohortConceptGraphsRecord.buildFromJson(file);

            Assert.assertThat(toFile.getGraphRecords(), is(fromFile.getGraphRecords()));
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}