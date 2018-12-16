package edu.ithaca.dragon.tecmap.analysis;

import edu.ithaca.dragon.tecmap.Settings;
import edu.ithaca.dragon.tecmap.SuggestingTecmapAPI;
import edu.ithaca.dragon.tecmap.conceptgraph.ConceptGraph;
import edu.ithaca.dragon.tecmap.data.TecmapDatastore;
import edu.ithaca.dragon.tecmap.data.TecmapFileDatastore;
import edu.ithaca.dragon.tecmap.io.record.*;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItem;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FactorAnalysisResultSuggestorTest {

    @Test
    public void learningResourcesFromExploratoryFactorMatrixRecordTest(){
        try{
            TecmapDatastore tecmapDatastore = TecmapFileDatastore.buildFromJsonFile(Settings.DEFAULT_TEST_DATASTORE_PATH);
            SuggestingTecmapAPI analysisExample = tecmapDatastore.retrieveTecmapForId("DocExample");
            //SuggestingTecmapAPI notConnectedExample = tecmapDatastore.retrieveTecmapForId("Cs1ExampleAssessmentAdded");

            ConceptGraph acg = analysisExample.getAverageConceptGraph();
            Map<String, AssessmentItem> assessmentItemMap = acg.getAssessmentItemMap();
            List<AssessmentItem> assessmentItems = new ArrayList<>(assessmentItemMap.values());
            ContinuousMatrixRecord assessmentMatrix = new ContinuousMatrixRecord(assessmentItems);

            ContinuousMatrixRecord factorMatrix = FactorAnalysis.calculateExploratoryMatrix(assessmentMatrix);

            List<LearningResourceRecord> lrrList = FactorAnalysisResultSuggestor.learningResourcesFromExploratoryFactorMatrixRecord(factorMatrix);

            //LRR emptyRecord should have no connections to factors so the conceptIds should be empty
            LearningResourceRecord emptyRecord = lrrList.get(0);

            assertEquals(assessmentItems.get(0).getId(), emptyRecord.getLearningResourceId());
            assertEquals(0, emptyRecord.getConceptIds().size());

            //LRR fullRecord should have all 3 connections to factors so the conceptID should have [Factor 1, Factor 2, Factor 3]
            LearningResourceRecord fullRecord = lrrList.get(2);
            assertEquals((assessmentItems.get(2).getId()), fullRecord.getLearningResourceId());
            assertEquals(3, fullRecord.getConceptIds().size());
            if((!fullRecord.getConceptIds().contains("Factor 1")) || (!fullRecord.getConceptIds().contains("Factor 2")) || (!fullRecord.getConceptIds().contains("Factor 3"))){
                Assert.fail();
            }



        }catch (Exception e){
            Assert.fail();
            e.printStackTrace();
        }
    }

    @Test
    public void conceptGraphRecordFromExploratoryMatrixRecordTest(){
        try{

            String testGraphName = "TestConceptGraphRecordName";

            TecmapDatastore tecmapDatastore = TecmapFileDatastore.buildFromJsonFile(Settings.DEFAULT_TEST_DATASTORE_PATH);
            SuggestingTecmapAPI analysisExample = tecmapDatastore.retrieveTecmapForId("DocExample");
            //SuggestingTecmapAPI notConnectedExample = tecmapDatastore.retrieveTecmapForId("Cs1ExampleAssessmentAdded");

            ConceptGraph acg = analysisExample.getAverageConceptGraph();
            Map<String, AssessmentItem> assessmentItemMap = acg.getAssessmentItemMap();
            List<AssessmentItem> assessmentItems = new ArrayList<>(assessmentItemMap.values());

            ContinuousMatrixRecord assessmentMatrix = new ContinuousMatrixRecord(assessmentItems);
            ContinuousMatrixRecord factorMatrix = FactorAnalysis.calculateExploratoryMatrix(assessmentMatrix);

            ConceptGraphRecord conceptGraphRecord = FactorAnalysisResultSuggestor.conceptGraphFromExploratoryMatrixRecord(factorMatrix, testGraphName);

            //Tests for LinkRecord
            assertEquals(testGraphName, conceptGraphRecord.getName());
            List<LinkRecord> linksList = conceptGraphRecord.getLinks();
            assertEquals(0, linksList.size());


            //Tests for Concepts

            assertEquals(9, conceptGraphRecord.getConcepts().size());


        }catch (Exception e){
            Assert.fail();
            e.printStackTrace();
        }
    }

    @Test
    public void createGraphAndResourceResourceFromAssessmentTest(){
        try {
            TecmapDatastore tecmapDatastore = TecmapFileDatastore.buildFromJsonFile(Settings.DEFAULT_TEST_DATASTORE_PATH);
            //TODO: This crashes when running a tecmap without a resourcefile Also test does not show DocExample works. It just does not crash
            SuggestingTecmapAPI analysisExample = tecmapDatastore.retrieveTecmapForId("DocExample");
            ConceptGraph acg = analysisExample.getAverageConceptGraph();
            Map<String, AssessmentItem> assessmentItemMap = acg.getAssessmentItemMap();
            List<AssessmentItem> assessmentItems = new ArrayList<>(assessmentItemMap.values());

            FactorAnalysisResultSuggestor.createGraphAndResourceFromAssessment(assessmentItems);

        }catch (Exception e){
            Assert.fail();
            e.printStackTrace();
        }
    }
}
