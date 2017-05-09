package edu.ithaca.dragonlab.ckc.conceptgraph;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ithaca.dragonlab.ckc.io.CSVReader;
import edu.ithaca.dragonlab.ckc.io.ConceptGraphRecord;
import edu.ithaca.dragonlab.ckc.io.JsonImportExport;
import edu.ithaca.dragonlab.ckc.io.LearningObjectLinkRecord;
import org.junit.Test;

import java.io.File;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by willsuchanek on 5/2/17.
 */
public class PracticalConceptGraphTest {
    @Test
    public static ConceptGraph willExampleConceptGraphTest(){
        ObjectMapper graphMapper = new ObjectMapper();

        graphMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        CSVReader csvReader = new CSVReader("test/testresources/basicRealisticExampleGradeBook.csv");

        try {
            ConceptGraphRecord graphRecord = ConceptGraphRecord.buildfromJson("test/testresources/basicRealisticExampleConceptGraph.json");
            ConceptGraph graph = new ConceptGraph(graphRecord);
            List<LearningObjectLinkRecord> LOLRlist = JsonImportExport.LOLRFromRecords("test/testresources/basicRealisticExampleLOLRecord.json");
            graph.addLearningObjectsFromLearningObjectLinkRecords(csvReader.getManualGradedLearningObjects(), LOLRlist);
            graph.calcKnowledgeEstimates();
            //System.out.println(graph.toString());
            return graph;

        }catch (Exception e){
            e.printStackTrace();
        }

        return null;

    }


    @Test
    public static ConceptGraph willExampleConceptGraphTestOneStudent(){
        ObjectMapper graphMapper = new ObjectMapper();

        graphMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        CSVReader csvReader = new CSVReader("test/testresources/basicRealisticExampleGradeBook2.csv");

        try {
            ConceptGraphRecord graphRecord = ConceptGraphRecord.buildfromJson("test/testresources/basicRealisticExampleConceptGraphOneStudent.json");
            ConceptGraph graph = new ConceptGraph(graphRecord);
            List<LearningObjectLinkRecord> LOLRlist = JsonImportExport.LOLRFromRecords("test/testresources/basicRealisticExampleLOLRecordOneStudent.json");
            graph.addLearningObjectsFromLearningObjectLinkRecords(csvReader.getManualGradedLearningObjects(), LOLRlist);
            graph.calcKnowledgeEstimates();
            //System.out.println(graph.toString());
            return graph;

        }catch (Exception e){
            e.printStackTrace();
        }

        return null;

    }



    @Test
    public static ConceptGraph simpleTestGraphTest() {

//    public static void simpleTestGraphTest() {
        ObjectMapper graphMapper = new ObjectMapper();

        graphMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        CSVReader csvReader = new CSVReader("test/testresources/simpleGraphTest.csv");

        try {
            ConceptGraphRecord graphRecord = ConceptGraphRecord.buildfromJson("test/testresources/simpleGraphTestConceptNodes.json");
            ConceptGraph graph = new ConceptGraph(graphRecord);
            List<LearningObjectLinkRecord> LOLRlist = JsonImportExport.LOLRFromRecords("test/testresources/simpleGraphTestLearningObjects.json");
            graph.addLearningObjectsFromLearningObjectLinkRecords(csvReader.getManualGradedLearningObjects(), LOLRlist);
            graph.calcKnowledgeEstimates();
            //            System.out.println(graph.toString());

            return graph;


        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }




}
