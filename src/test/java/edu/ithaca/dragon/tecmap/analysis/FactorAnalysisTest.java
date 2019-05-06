package edu.ithaca.dragon.tecmap.analysis;

import edu.ithaca.dragon.tecmap.Settings;
import edu.ithaca.dragon.tecmap.TecmapAPI;
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
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by Benjamin on 9/18/2018.
 */
public class FactorAnalysisTest {

    @Test
    public void calculateExploratoryMatrixTest() throws IOException {
        TecmapDatastore tecmapDatastore = TecmapFileDatastore.buildFromJsonFile(Settings.DEFAULT_TEST_DATASTORE_PATH);
        TecmapAPI analysisExample = tecmapDatastore.retrieveTecmapForId("DocExample");

        ConceptGraph acg = analysisExample.getAverageConceptGraph();
        Map<String, AssessmentItem> assessmentItemMap = acg.getAssessmentItemMap();
        List<AssessmentItem> assessmentItems = new ArrayList<>(assessmentItemMap.values());
        ContinuousMatrixRecord assessmentMatrix = new ContinuousMatrixRecord(assessmentItems);


        ContinuousMatrixRecord factorMatrix = FactorAnalysis.calculateExploratoryMatrix(assessmentMatrix);
        double[][] data = factorMatrix.getDataMatrix();
        List<String> expectedList = new ArrayList<>();
        expectedList.add("Factor 1");
        expectedList.add("Factor 2");
        expectedList.add("Factor 3");

        //Print Matrix
        /*
        System.out.println();
        int rows = data.length;
        int cols = data[0].length;
        for(int i = 0; i<rows; i++)
        {
            for(int j = 0; j<cols; j++)
            {
                System.out.print(data[i][j] + "  ");
            }
            System.out.println();
        }
        */

        List<String> assessmentIds = new ArrayList<>(assessmentItemMap.keySet());
        assertEquals(-.12, data[0][0], .05);
        //Multiple factors
        assertEquals(-.02, data[2][0], .01);
        //dimensions of matrix
        assertEquals(3, data.length);
        assertEquals(9, data[0].length);
        assertEquals(assessmentIds, factorMatrix.getAssessmentIds());
        assertEquals(expectedList, factorMatrix.getRowIds());
    }

    @Test
    public void displayExploratoryGraphTest()throws Exception{
        TecmapDatastore tecmapDatastore = TecmapFileDatastore.buildFromJsonFile(Settings.DEFAULT_TEST_DATASTORE_PATH);
        TecmapAPI analysisExample = tecmapDatastore.retrieveTecmapForId("DocExample");

        ConceptGraph acg = analysisExample.getAverageConceptGraph();
        Map<String, AssessmentItem> assessmentItemMap = acg.getAssessmentItemMap();
        List<AssessmentItem> assessmentItems = new ArrayList<>(assessmentItemMap.values());
        ContinuousMatrixRecord assessmentMatrix = new ContinuousMatrixRecord(assessmentItems);

        FactorAnalysis.displayExploratoryGraph(assessmentMatrix);
        //System.in.read(); ???
    }

    @Test
    public void calculateConfirmatoryMatrixTest() throws Exception{
        TecmapDatastore tecmapDatastore = TecmapFileDatastore.buildFromJsonFile(Settings.DEFAULT_TEST_DATASTORE_PATH);
        TecmapAPI analysisExample = tecmapDatastore.retrieveTecmapForId("DocExample");

        ConceptGraph acg = analysisExample.getAverageConceptGraph();
        ContinuousMatrixRecord confirmatoryFactorMatrix = FactorAnalysis.calculateConfirmatoryMatrix(acg);

        double[][] factorMatrix = confirmatoryFactorMatrix.getDataMatrix();

        assertEquals(.79, factorMatrix[0][0], .01);
        List<String> factors = confirmatoryFactorMatrix.getAssessmentIds();


        List<String> factorList = new ArrayList<String>();
        Collection<String> nodeCollection = acg.getAllNodeIds();
        for(String node : nodeCollection){
            factorList.add(node);
        }
        Map<String, AssessmentItem> assessmentItemMap = acg.getAssessmentItemMap();
        List<String> assessmentIds = new ArrayList<>(assessmentItemMap.keySet());
        assertEquals(factorList, confirmatoryFactorMatrix.getRowIds());
        assertEquals(assessmentIds, confirmatoryFactorMatrix.getAssessmentIds());
        /*
        //Print: List of factors (rows), list of assessments (columns), factor matrix
        System.out.println("Factors in rows: " + confirmatoryFactorMatrix.getRowIds());
        System.out.println("Assessments in columns: " + confirmatoryFactorMatrix.getAssessmentIds());
        int rows = factorMatrix.length;
        int cols = factorMatrix[0].length;
        for(int i = 0; i<rows; i++)
        {
            for(int j = 0; j<cols; j++)
            {
                System.out.print(factorMatrix[i][j] + "  ");
            }
            System.out.println();
        }
*/

    }

    @Test
    public void modelMakerTest(){
        try {
            TecmapDatastore tecmapDatastore = TecmapFileDatastore.buildFromJsonFile(Settings.DEFAULT_TEST_DATASTORE_PATH);
            TecmapAPI analysisExample = tecmapDatastore.retrieveTecmapForId("DocExample");

            ConceptGraph acg = analysisExample.getAverageConceptGraph();

            List<String> factorList = new ArrayList<String>();
            Collection<String> nodeCollection = acg.getAllNodeIds();
            for(String node : nodeCollection){
                factorList.add(node);
            }


            String modelString = FactorAnalysis.modelMaker(acg, factorList);

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
            TecmapAPI analysisExample = tecmapDatastore.retrieveTecmapForId("DocExample");

            ConceptGraph acg = analysisExample.getAverageConceptGraph();
            List<String> factorList = new ArrayList<String>();
            Collection<String> nodeCollection = acg.getAllNodeIds();
            for(String node : nodeCollection){
                factorList.add(node);
            }
            String modelString = FactorAnalysis.modelMaker(acg, factorList);

            FactorAnalysis.modelToFile(modelString);

            String filePath = "src/main/resources/confirmatoryModelFiles/autoCreated/model.txt";

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
