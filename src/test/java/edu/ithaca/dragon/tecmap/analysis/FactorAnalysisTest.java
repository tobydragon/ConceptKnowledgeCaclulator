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


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static edu.ithaca.dragon.tecmap.analysis.FactorAnalysis.calculateExploratoryMatrix;
import static edu.ithaca.dragon.tecmap.analysis.FactorAnalysis.modelToFile;
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
            SuggestingTecmapAPI analysisExample = tecmapDatastore.retrieveTecmapForId("DocExample");
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


            assertEquals(-.12, data[0][0], .05);
            //Multiple factors
            assertEquals(.58, data[2][0], .02);

        }catch (Exception e){
            Assert.fail();
            e.printStackTrace();
        }

    }

    @Test
    public void calculateConfirmatoryMatrixTest() throws Exception{

    }

    @Test
    public void modelMakerTest(){
        try {
            TecmapDatastore tecmapDatastore = TecmapFileDatastore.buildFromJsonFile(Settings.DEFAULT_TEST_DATASTORE_PATH);
            SuggestingTecmapAPI analysisExample = tecmapDatastore.retrieveTecmapForId("DocExample");
            //SuggestingTecmapAPI notConnectedExample = tecmapDatastore.retrieveTecmapForId("Cs1ExampleAssessmentAdded");

            ConceptGraph acg = analysisExample.getAverageConceptGraph();
            String modelString = FactorAnalysis.modelMaker(acg);

            //System.out.println(modelString);

            String modelToExpect = "Precision -> GOLF, GOLFToPrecision, NA \n" +
                    "Precision -> CURL, CURLToPrecision, NA \n" +
                    "Precision -> BILLIARD, BILLIARDToPrecision, NA \n" +
                    "Misc -> X12MINTR, X12MINTRToMisc, NA \n" +
                    "Misc -> X2KROW, X2KROWToMisc, NA \n" +
                    "Misc -> X1500M, X1500MToMisc, NA \n" +
                    "Reaction Time -> BENCH, BENCHToReaction Time, NA \n" +
                    "Reaction Time -> PINBALL, PINBALLToReaction Time, NA \n" +
                    "Reaction Time -> MAXPUSHU, MAXPUSHUToReaction Time, NA";

            assertEquals(modelToExpect, modelString);


        }catch (Exception e){
            Assert.fail();
            e.printStackTrace();
        }
    }

    @Test
    public void modelToFileTest(){
        try {
            TecmapDatastore tecmapDatastore = TecmapFileDatastore.buildFromJsonFile(Settings.DEFAULT_TEST_DATASTORE_PATH);
            SuggestingTecmapAPI analysisExample = tecmapDatastore.retrieveTecmapForId("DocExample");
            //SuggestingTecmapAPI notConnectedExample = tecmapDatastore.retrieveTecmapForId("Cs1ExampleAssessmentAdded");

            ConceptGraph acg = analysisExample.getAverageConceptGraph();
            String modelString = FactorAnalysis.modelMaker(acg);

            FactorAnalysis.modelToFile(acg);

            String filePath = "/Users/bleblanc2/IdeaProjects/tecmap/src/test/resources/model/model.txt";

            StringBuilder contentBuilder = new StringBuilder();
            try (Stream<String> stream = Files.lines( Paths.get(filePath), StandardCharsets.UTF_8))
            {
                stream.forEach(s -> contentBuilder.append(s).append("\n"));
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            String modelToExpect = "Precision -> GOLF, GOLFToPrecision, NA \n" +
                    "Precision -> CURL, CURLToPrecision, NA \n" +
                    "Precision -> BILLIARD, BILLIARDToPrecision, NA \n" +
                    "Misc -> X12MINTR, X12MINTRToMisc, NA \n" +
                    "Misc -> X2KROW, X2KROWToMisc, NA \n" +
                    "Misc -> X1500M, X1500MToMisc, NA \n" +
                    "Reaction Time -> BENCH, BENCHToReaction Time, NA \n" +
                    "Reaction Time -> PINBALL, PINBALLToReaction Time, NA \n" +
                    "Reaction Time -> MAXPUSHU, MAXPUSHUToReaction Time, NA\n";

            assertEquals(modelToExpect, contentBuilder.toString());



        }catch (Exception e){
            Assert.fail();
            e.printStackTrace();
        }
    }


}
