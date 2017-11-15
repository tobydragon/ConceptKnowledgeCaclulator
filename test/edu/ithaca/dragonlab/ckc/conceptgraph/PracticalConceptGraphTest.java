package edu.ithaca.dragonlab.ckc.conceptgraph;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ithaca.dragonlab.ckc.io.*;
import edu.ithaca.dragonlab.ckc.util.DataUtil;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.List;

/**
 * Created by willsuchanek on 5/2/17.
 */
public class PracticalConceptGraphTest {
    public static final String TEST_DIR = "test/testresources/practicalExamples/";

    @Test
    public void basicRealisticConceptGraphTest(){
        ObjectMapper graphMapper = new ObjectMapper();

        graphMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try{
        CSVReader csvReader = new SakaiReader(TEST_DIR+"basicRealisticAssessment.csv");


            ConceptGraphRecord graphRecord = ConceptGraphRecord.buildFromJson(TEST_DIR+"mediumRealisticConceptGraph.json");

            List<LearningObjectLinkRecord> LOLRlist = LearningObjectLinkRecord.buildListFromJson(TEST_DIR + "mediumRealisticResource.json");
            ConceptGraph graph = new ConceptGraph(graphRecord, LOLRlist, csvReader.getManualGradedResponses());

            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(new File("test/testresources/practicalExamples/SystemCreated/blankRealisticExample.json"), TreeConverter.makeTreeCopy(graph).buildConceptGraphRecord());

            graph.calcDataImportance();
            graph.calcKnowledgeEstimates();

            Assert.assertEquals("Intro CS", graph.findNodeById("Intro CS").getID());
            Assert.assertEquals(5,graph.findNodeById("Boolean").getLearningObjectMap().size());
            Assert.assertEquals(15,graph.getLearningObjectMap().size());

            ConceptGraphRecord tree = TreeConverter.makeTreeCopy(graph).buildConceptGraphRecord();
            //Object to JSON in file
            mapper = new ObjectMapper();
            mapper.writeValue(new File("test/testresources/practicalExamples/SystemCreated/basicRealisticExample.json"), tree);


        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Test
    public void basicRealisticTest(){
        ObjectMapper graphMapper = new ObjectMapper();

        graphMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try{
        CSVReader csvReader = new SakaiReader("test/testresources/ManuallyCreated/basicRealisticAssessment.csv");


            ConceptGraphRecord graphRecord = ConceptGraphRecord.buildFromJson("test/testresources/ManuallyCreated/basicRealisticConceptGraph.json");

            List<LearningObjectLinkRecord> LOLRlist = LearningObjectLinkRecord.buildListFromJson("test/testresources/ManuallyCreated/basicRealisticResource.json");
            ConceptGraph graph = new ConceptGraph(graphRecord, LOLRlist);

            CohortConceptGraphs gcg = new CohortConceptGraphs(graph,csvReader.getManualGradedResponses());

            ConceptGraph testGraph = gcg.getAvgGraph();


            Assert.assertEquals("Intro CS", testGraph.findNodeById("Intro CS").getID());
            Assert.assertEquals(7, testGraph.findNodeById("Boolean").getLearningObjectMap().size());
            Assert.assertEquals(15,testGraph.getLearningObjectMap().size());


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
        CSVReader csvReader = new SakaiReader(TEST_DIR+"advancedRealisticAssessment.csv");


            ConceptGraphRecord graphRecord = ConceptGraphRecord.buildFromJson(TEST_DIR+"mediumRealisticConceptGraph.json");

            List<LearningObjectLinkRecord> LOLRlist = LearningObjectLinkRecord.buildListFromJson(TEST_DIR + "mediumRealisticResource.json");
            ConceptGraph graph = new ConceptGraph(graphRecord, LOLRlist);

            CohortConceptGraphs gcg = new CohortConceptGraphs(graph,csvReader.getManualGradedResponses());
            graph.calcDataImportance();
            graph.calcKnowledgeEstimates();


            Assert.assertEquals("Intro CS", graph.findNodeById("Intro CS").getID());
            Assert.assertEquals(5, graph.findNodeById("Boolean").getLearningObjectMap().size());
            Assert.assertEquals(15,graph.getLearningObjectMap().size());




            ObjectMapper mapper = new ObjectMapper();
            ConceptGraphRecord tree = TreeConverter.makeTreeCopy(graph).buildConceptGraphRecord();
            //Object to JSON in file
            mapper.writeValue(new File("test/testresources/practicalExamples/SystemCreated/advancedRealisticExample.json"), tree);

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Test
    public void SingleStudentRealisticConceptGraphTest(){
        ObjectMapper graphMapper = new ObjectMapper();

        graphMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try{
        CSVReader csvReader = new SakaiReader(TEST_DIR+"singleStudentRealisticAssessment.csv");

            ConceptGraphRecord graphRecord = ConceptGraphRecord.buildFromJson(TEST_DIR+"mediumRealisticConceptGraph.json");
            List<LearningObjectLinkRecord> LOLRlist = LearningObjectLinkRecord.buildListFromJson(TEST_DIR+"mediumRealisticResource.json");
            ConceptGraph graph = new ConceptGraph(graphRecord, LOLRlist, csvReader.getManualGradedResponses());
            graph.calcDataImportance();
            graph.calcKnowledgeEstimates();


            Assert.assertEquals("Intro CS", graph.findNodeById("Intro CS").getID());
            Assert.assertEquals(5, graph.findNodeById("Boolean").getLearningObjectMap().size());
            Assert.assertEquals(15, graph.getLearningObjectMap().size());
            Assert.assertEquals(-.5,graph.findNodeById("Sequence Types").getKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);
            Assert.assertEquals(-0.545,graph.findNodeById("For Loop").getKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);
            Assert.assertEquals(.346,graph.findNodeById("Loops").getKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);



            ConceptGraphRecord tree = TreeConverter.makeTreeCopy(graph).buildConceptGraphRecord();
            ObjectMapper mapper = new ObjectMapper();
            //Object to JSON in file
            mapper.writeValue(new File("test/testresources/practicalExamples/SystemCreated/singleStudentRealisticExample.json"), tree);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
