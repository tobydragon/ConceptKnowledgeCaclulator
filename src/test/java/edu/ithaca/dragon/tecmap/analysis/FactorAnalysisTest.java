package edu.ithaca.dragon.tecmap.analysis;

import edu.ithaca.dragon.tecmap.Settings;
import edu.ithaca.dragon.tecmap.SuggestingTecmap;
import edu.ithaca.dragon.tecmap.SuggestingTecmapAPI;
import edu.ithaca.dragon.tecmap.Tecmap;
import edu.ithaca.dragon.tecmap.conceptgraph.ConceptGraph;
import edu.ithaca.dragon.tecmap.data.TecmapDatastore;
import edu.ithaca.dragon.tecmap.data.TecmapFileDatastore;
import edu.ithaca.dragon.tecmap.io.record.ContinuousMatrixRecord;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItem;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static edu.ithaca.dragon.tecmap.analysis.FactorAnalysis.calculateExploratoryMatrix;
import static edu.ithaca.dragon.tecmap.tecmapstate.TecmapState.assessmentAdded;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by Benjamin on 9/18/2018.
 */
public class FactorAnalysisTest {

    @Test
    public void calculateExploratoryMatrixTest() {
        try {
            TecmapDatastore tecmapDatastore = TecmapFileDatastore.buildFromJsonFile(Settings.DEFAULT_TEST_DATASTORE_PATH);
            SuggestingTecmapAPI analysisExample = tecmapDatastore.retrieveTecmapForId("AnalysisExample");
            //SuggestingTecmapAPI notConnectedExample = tecmapDatastore.retrieveTecmapForId("Cs1ExampleAssessmentAdded");

            ConceptGraph acg = analysisExample.getAverageConceptGraph();
            Map<String, AssessmentItem> assessmentItemMap = acg.getAssessmentItemMap();
            List<AssessmentItem> assessmentItems = new ArrayList<>(assessmentItemMap.values());
            ContinuousMatrixRecord gradeMatrix = new ContinuousMatrixRecord(assessmentItems);

            ContinuousMatrixRecord factorMatrix = calculateExploratoryMatrix(gradeMatrix);
            double[][] data = factorMatrix.getDataMatrix();

            assertEquals(data[0][0], .8, .2);

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Test
    public void calculateConfirmatoryMatrixTest() throws Exception{

    }

}
