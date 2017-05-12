package edu.ithaca.dragonlab.ckc.conceptgraph;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ithaca.dragonlab.ckc.io.*;
import edu.ithaca.dragonlab.ckc.util.DataUtil;
import edu.ithaca.dragonlab.ckc.util.ErrorUtil;
import edu.ithaca.dragonlab.ckc.util.GraphConstants;
import jdk.nashorn.internal.ir.debug.JSONWriter;
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
        CSVReader csvReader = new CSVReader(TEST_DIR+"basicRealisticExampleGradeBook.csv");

        try {
            ConceptGraphRecord graphRecord = ConceptGraphRecord.buildFromJson(TEST_DIR+"realisticExampleConceptGraph.json");

            List<LearningObjectLinkRecord> LOLRlist = JsonImportExport.LOLRFromRecords(TEST_DIR + "realisticExampleLOLRecord.json");
            ConceptGraph graph = new ConceptGraph(graphRecord, LOLRlist, csvReader.getManualGradedResponses());

            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(new File("test/testresources/practicalExamples/blankRealisticExample.json"), graph.graphToTree().buildNodesAndLinks());

            graph.calcDataImportance();
            graph.calcKnowledgeEstimates();

            Assert.assertEquals("Intro CS", graph.findNodeById("Intro CS").getID());
            Assert.assertEquals(5,graph.findNodeById("Boolean").getLearningObjectMap().size());
            Assert.assertEquals(15,graph.getLearningObjectMap().size());

            ConceptGraphRecord tree = graph.graphToTree().buildNodesAndLinks();
            //Object to JSON in file
            mapper = new ObjectMapper();
            mapper.writeValue(new File("test/testresources/practicalExamples/basicRealisticExample.json"), tree);


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void advancedRealisticConceptGraphTest(){
        ObjectMapper graphMapper = new ObjectMapper();

        graphMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        CSVReader csvReader = new CSVReader(TEST_DIR+"advancedRealisticExampleGradeBook.csv");

        try {
            ConceptGraphRecord graphRecord = ConceptGraphRecord.buildFromJson(TEST_DIR+"realisticExampleConceptGraph.json");

            List<LearningObjectLinkRecord> LOLRlist = JsonImportExport.LOLRFromRecords(TEST_DIR + "realisticExampleLOLRecord.json");
            ConceptGraph graph = new ConceptGraph(graphRecord, LOLRlist);

            GroupConceptGraphs gcg = new GroupConceptGraphs(graph,csvReader.getManualGradedResponses());
            graph.calcDataImportance();
            graph.calcKnowledgeEstimates();


            Assert.assertEquals("Intro CS", graph.findNodeById("Intro CS").getID());
            Assert.assertEquals(5, graph.findNodeById("Boolean").getLearningObjectMap().size());
            Assert.assertEquals(15,graph.getLearningObjectMap().size());

            ObjectMapper mapper = new ObjectMapper();
            ConceptGraphRecord tree = graph.graphToTree().buildNodesAndLinks();
            //Object to JSON in file
            mapper.writeValue(new File("test/testresources/practicalExamples/advancedRealisticExample.json"), tree);
            mapper.writeValue(new File("test/testresources/practicalExamples/groupConceptGraphAdvancedRealisticExample.json"), gcg.getAllNamedGraphs());

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Test
    public void SingleStudentRealisticConceptGraphTest(){
        ObjectMapper graphMapper = new ObjectMapper();

        graphMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        CSVReader csvReader = new CSVReader(TEST_DIR+"SingleStudentRealisticExampleGradeBook.csv");

        try {
            /*
            ConceptGraphRecord graphRecord = ConceptGraphRecord.buildFromJson(TEST_DIR+"realisticExampleConceptGraph.json");

            List<LearningObjectLinkRecord> LOLRlist = JsonImportExport.LOLRFromRecords(TEST_DIR+"realisticExampleLOLRecord.json");
            ConceptGraph graph = new ConceptGraph(graphRecord,csvReader.getManualGradedLearningObjects(), LOLRlist);
            */
            ConceptGraphRecord graphRecord = ConceptGraphRecord.buildFromJson(TEST_DIR+"realisticExampleConceptGraph.json");
            List<LearningObjectLinkRecord> LOLRlist = JsonImportExport.LOLRFromRecords(TEST_DIR+"realisticExampleLOLRecord.json");
            ConceptGraph graph = new ConceptGraph(graphRecord, LOLRlist, csvReader.getManualGradedResponses());
            graph.calcDataImportance();
            graph.calcKnowledgeEstimates();


            Assert.assertEquals("Intro CS", graph.findNodeById("Intro CS").getID());
            Assert.assertEquals(5, graph.findNodeById("Boolean").getLearningObjectMap().size());
            Assert.assertEquals(15, graph.getLearningObjectMap().size());
            Assert.assertEquals(-.5,graph.findNodeById("Sequence Types").getKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);
            Assert.assertEquals(-0.545,graph.findNodeById("For Loop").getKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);
            Assert.assertEquals(.346,graph.findNodeById("Loops").getKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);



            ConceptGraphRecord tree = graph.graphToTree().buildNodesAndLinks();
            ObjectMapper mapper = new ObjectMapper();
            //Object to JSON in file
            mapper.writeValue(new File("test/testresources/practicalExamples/singleStudentRealisticExample.json"), tree);
        }catch (Exception e){
            e.printStackTrace();
        }

    }





}
