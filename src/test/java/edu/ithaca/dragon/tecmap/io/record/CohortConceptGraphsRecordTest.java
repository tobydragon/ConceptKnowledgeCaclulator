package edu.ithaca.dragon.tecmap.io.record;

import edu.ithaca.dragon.tecmap.Settings;
import edu.ithaca.dragon.tecmap.conceptgraph.CohortConceptGraphs;
import edu.ithaca.dragon.tecmap.conceptgraph.ExampleConceptGraphFactory;
import edu.ithaca.dragon.tecmap.learningresource.ExampleLearningObjectResponseFactory;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Created by home on 5/20/17.
 */
public class CohortConceptGraphsRecordTest {
    public static final String TEST_DIR = Settings.TEST_RESOURCE_DIR + "practicalExamples/SystemCreated/";

    @Test
    public void CohortConceptGraphsRecordTest(){
        try {
            CohortConceptGraphs graphs = new CohortConceptGraphs(ExampleConceptGraphFactory.makeSimpleStructureAndLearningObjects(), ExampleLearningObjectResponseFactory.makeSimpleResponses());
            CohortConceptGraphsRecord toFile = graphs.buildCohortConceptGraphsRecord();
            String file = TEST_DIR + "simpleCohort.json";
            toFile.writeToJson(file);

            CohortConceptGraphsRecord fromFile = CohortConceptGraphsRecord.buildFromJson(file);

            assertThat(toFile.getGraphRecords(), is(fromFile.getGraphRecords()));
        }
        catch (IOException e){
            e.printStackTrace();
            fail();
        }
    }

    public static void main(String[] args){
        try {
            CohortConceptGraphs graphs = new CohortConceptGraphs(ExampleConceptGraphFactory.makeSimpleStructureAndLearningObjects(), ExampleLearningObjectResponseFactory.makeSimpleResponses());
            CohortConceptGraphsRecord toFile = graphs.buildCohortConceptTreeRecord();
            String file = TEST_DIR + "simpleCohortTree.json";
            toFile.writeToJson(file);

            CohortConceptGraphsRecord fromFile = CohortConceptGraphsRecord.buildFromJson(file);

            assertThat(toFile.getGraphRecords(), is(fromFile.getGraphRecords()));
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}