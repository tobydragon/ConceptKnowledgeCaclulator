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

/**
 * Created by Benjamin on 9/18/2018.
 */
public class FactorAnalysisTest {

    @Test
    //TODO finish test. Confirm the structure of steps needed before calculatingExploratoryMatrix
    public void calculateExploratoryMatrixTest() throws Exception {
        TecmapDatastore tecmapDatastore = TecmapFileDatastore.buildFromJsonFile(Settings.DEFAULT_TEST_DATASTORE_PATH);
        SuggestingTecmapAPI analysisExample = tecmapDatastore.retrieveTecmapForId("AnalysisExample");
        //SuggestingTecmapAPI notConnectedExample = tecmapDatastore.retrieveTecmapForId("Cs1ExampleAssessmentAdded");

        ConceptGraph acg = analysisExample.getAverageConceptGraph();
        Map<String, AssessmentItem> assessmentItemMap= acg.getAssessmentItemMap();
        List<AssessmentItem> assessmentItems = new ArrayList<>(assessmentItemMap.values());
        ContinuousMatrixRecord gradeMatrix = new ContinuousMatrixRecord(assessmentItems);

        ContinuousMatrixRecord factorMatrix = calculateExploratoryMatrix(gradeMatrix);
        double[][] data = factorMatrix.getDataMatrix();


    }
}
