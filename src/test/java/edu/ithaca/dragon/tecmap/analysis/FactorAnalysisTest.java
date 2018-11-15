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
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import javax.validation.constraints.AssertFalse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static edu.ithaca.dragon.tecmap.analysis.FactorAnalysis.calculateExploratoryMatrix;
import static edu.ithaca.dragon.tecmap.analysis.FactorAnalysis.newCalculateExploratoryMatrix;
import static edu.ithaca.dragon.tecmap.tecmapstate.TecmapState.assessmentAdded;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by Benjamin on 9/18/2018.
 */
public class FactorAnalysisTest {

    @Test
    public void newCalculateExploratoryMatrixTest() {
        try {
            TecmapDatastore tecmapDatastore = TecmapFileDatastore.buildFromJsonFile(Settings.DEFAULT_TEST_DATASTORE_PATH);
            SuggestingTecmapAPI analysisExample = tecmapDatastore.retrieveTecmapForId("AnalysisExample");
            //SuggestingTecmapAPI notConnectedExample = tecmapDatastore.retrieveTecmapForId("Cs1ExampleAssessmentAdded");

            ConceptGraph acg = analysisExample.getAverageConceptGraph();
            Map<String, AssessmentItem> assessmentItemMap = acg.getAssessmentItemMap();
            List<AssessmentItem> assessmentItems = new ArrayList<>(assessmentItemMap.values());
            ContinuousMatrixRecord assessmentMatrix = new ContinuousMatrixRecord(assessmentItems);

            double[][] testGradeMatrix = assessmentMatrix.getDataMatrix();

            //System.out.println(testGradeMatrix[0][0]);
            //System.out.println(testGradeMatrix[1][0]);
            //System.out.println(testGradeMatrix[0][1]);

            /*
            int rows = testGradeMatrix.length;
            int cols = testGradeMatrix[0].length;
            for(int i = 0; i<rows; i++)
            {
                for(int j = 0; j<cols; j++)
                {
                    System.out.print(testGradeMatrix[i][j] + "  ");
                }
                System.out.println();
            }
            */

            ContinuousMatrixRecord factorMatrix = FactorAnalysis.newCalculateExploratoryMatrix(assessmentMatrix);
            double[][] data = factorMatrix.getDataMatrix();


            assertEquals(.134, data[0][0], .05);

        }catch (Exception e){
            Assert.fail();
            e.printStackTrace();
        }

    }

    @Test
    public void calculateConfirmatoryMatrixTest() throws Exception{

    }

}
