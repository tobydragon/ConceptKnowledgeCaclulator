package edu.ithaca.dragon.tecmap.conceptgraph;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ithaca.dragon.tecmap.Settings;
import edu.ithaca.dragon.tecmap.io.reader.SakaiReader;
import edu.ithaca.dragon.tecmap.io.reader.TecmapCSVReader;
import edu.ithaca.dragon.tecmap.io.record.ConceptGraphRecord;
import edu.ithaca.dragon.tecmap.io.record.LearningResourceRecord;
import edu.ithaca.dragon.tecmap.util.DataUtil;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.List;

/**
 * Created by willsuchanek on 5/2/17.
 */
public class PracticalConceptGraphTest {
    public static final String TEST_DIR = Settings.TEST_RESOURCE_DIR + "practicalExamples/";

    @Test
    public void basicRealisticConceptGraphTest(){
        ObjectMapper graphMapper = new ObjectMapper();

        graphMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try{
        TecmapCSVReader tecmapCsvReader = new SakaiReader(TEST_DIR+"basicRealisticAssessment.csv");


            ConceptGraphRecord graphRecord = ConceptGraphRecord.buildFromJson(TEST_DIR+"mediumRealisticConceptGraph.json");

            List<LearningResourceRecord> LOLRlist = LearningResourceRecord.createLearningResourceRecordsFromJsonFile(TEST_DIR + "mediumRealisticResource.json");
            ConceptGraph graph = new ConceptGraph(graphRecord, LOLRlist, tecmapCsvReader.getManualGradedResponses());

            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(new File(Settings.TEST_RESOURCE_DIR + "practicalExamples/SystemCreated/blankRealisticExample.json"), TreeConverter.makeTreeCopy(graph).buildConceptGraphRecord());

            graph.calcDataImportance();
            graph.calcKnowledgeEstimates();

            Assert.assertEquals("Intro CS", graph.findNodeById("Intro CS").getID());
            Assert.assertEquals(5,graph.findNodeById("Boolean").getAssessmentItemMap().size());
            Assert.assertEquals(15,graph.getAssessmentItemMap().size());

            ConceptGraphRecord tree = TreeConverter.makeTreeCopy(graph).buildConceptGraphRecord();
            //Object to JSON in file
            mapper = new ObjectMapper();
            mapper.writeValue(new File(Settings.TEST_RESOURCE_DIR + "practicalExamples/SystemCreated/basicRealisticExample.json"), tree);


        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Test
    public void basicRealisticTest(){
        ObjectMapper graphMapper = new ObjectMapper();

        graphMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try{
        TecmapCSVReader tecmapCsvReader = new SakaiReader(Settings.TEST_RESOURCE_DIR + "ManuallyCreated/basicRealisticAssessment.csv");


            ConceptGraphRecord graphRecord = ConceptGraphRecord.buildFromJson(Settings.TEST_RESOURCE_DIR + "ManuallyCreated/basicRealisticConceptGraph.json");

            List<LearningResourceRecord> LOLRlist = LearningResourceRecord.createLearningResourceRecordsFromJsonFile(Settings.TEST_RESOURCE_DIR + "ManuallyCreated/basicRealisticResource.json");
            ConceptGraph graph = new ConceptGraph(graphRecord, LOLRlist);

            CohortConceptGraphs gcg = new CohortConceptGraphs(graph, tecmapCsvReader.getManualGradedResponses());

            ConceptGraph testGraph = gcg.getAvgGraph();


            Assert.assertEquals("Intro CS", testGraph.findNodeById("Intro CS").getID());
            Assert.assertEquals(7, testGraph.findNodeById("Boolean").getAssessmentItemMap().size());
            Assert.assertEquals(15,testGraph.getAssessmentItemMap().size());


            Assert.assertEquals(0.806, testGraph.findNodeById("Boolean").getKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);
            Assert.assertEquals(0.783090, testGraph.findNodeById("Boolean Expressions").getKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);
            Assert.assertEquals(0.746, testGraph.findNodeById("If Statement").getKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);
            Assert.assertEquals(0.722, testGraph.findNodeById("While Loop").getKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);
            Assert.assertEquals(0.566, testGraph.findNodeById("Counting").getKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);
            Assert.assertEquals(0.575, testGraph.findNodeById("For Loop").getKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);


        }catch (Exception e){
            e.printStackTrace();
        }

    }



    @Test
    public void advancedRealisticConceptGraphTest(){
        ObjectMapper graphMapper = new ObjectMapper();

        graphMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try{
        TecmapCSVReader tecmapCsvReader = new SakaiReader(TEST_DIR+"advancedRealisticAssessment.csv");


            ConceptGraphRecord graphRecord = ConceptGraphRecord.buildFromJson(TEST_DIR+"mediumRealisticConceptGraph.json");

            List<LearningResourceRecord> LOLRlist = LearningResourceRecord.createLearningResourceRecordsFromJsonFile(TEST_DIR + "mediumRealisticResource.json");
            ConceptGraph graph = new ConceptGraph(graphRecord, LOLRlist);

            CohortConceptGraphs gcg = new CohortConceptGraphs(graph, tecmapCsvReader.getManualGradedResponses());
            graph.calcDataImportance();
            graph.calcKnowledgeEstimates();


            Assert.assertEquals("Intro CS", graph.findNodeById("Intro CS").getID());
            Assert.assertEquals(5, graph.findNodeById("Boolean").getAssessmentItemMap().size());
            Assert.assertEquals(15,graph.getAssessmentItemMap().size());




            ObjectMapper mapper = new ObjectMapper();
            ConceptGraphRecord tree = TreeConverter.makeTreeCopy(graph).buildConceptGraphRecord();
            //Object to JSON in file
            mapper.writeValue(new File(Settings.TEST_RESOURCE_DIR + "practicalExamples/SystemCreated/advancedRealisticExample.json"), tree);

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Test
    public void SingleStudentRealisticConceptGraphTest(){
        ObjectMapper graphMapper = new ObjectMapper();

        graphMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try{
        TecmapCSVReader tecmapCsvReader = new SakaiReader(TEST_DIR+"singleStudentRealisticAssessment.csv");

            ConceptGraphRecord graphRecord = ConceptGraphRecord.buildFromJson(TEST_DIR+"mediumRealisticConceptGraph.json");
            List<LearningResourceRecord> LOLRlist = LearningResourceRecord.createLearningResourceRecordsFromJsonFile(TEST_DIR+"mediumRealisticResource.json");
            ConceptGraph graph = new ConceptGraph(graphRecord, LOLRlist, tecmapCsvReader.getManualGradedResponses());
            graph.calcDataImportance();
            graph.calcKnowledgeEstimates();


            Assert.assertEquals("Intro CS", graph.findNodeById("Intro CS").getID());
            Assert.assertEquals(5, graph.findNodeById("Boolean").getAssessmentItemMap().size());
            Assert.assertEquals(15, graph.getAssessmentItemMap().size());
            Assert.assertEquals(-.5,graph.findNodeById("Sequence Types").getKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);
            Assert.assertEquals(-0.545,graph.findNodeById("For Loop").getKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);
            Assert.assertEquals(.346,graph.findNodeById("Loops").getKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);



            ConceptGraphRecord tree = TreeConverter.makeTreeCopy(graph).buildConceptGraphRecord();
            ObjectMapper mapper = new ObjectMapper();
            //Object to JSON in file
            mapper.writeValue(new File(Settings.TEST_RESOURCE_DIR + "practicalExamples/SystemCreated/singleStudentRealisticExample.json"), tree);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
