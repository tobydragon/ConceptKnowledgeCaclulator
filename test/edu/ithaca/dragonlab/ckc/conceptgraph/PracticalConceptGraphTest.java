package edu.ithaca.dragonlab.ckc.conceptgraph;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ithaca.dragonlab.ckc.io.*;
import org.junit.Test;

import java.util.List;

/**
 * Created by willsuchanek on 5/2/17.
 */
public class PracticalConceptGraphTest {
    @Test
    public void willExampleConceptGraphTest(){
        ObjectMapper graphMapper = new ObjectMapper();

        graphMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        CSVReader csvReader = new CSVReader("test/testresources/basicRealisticExampleGradeBook.csv");

        try {
            ConceptGraphRecord graphRecord = ConceptGraphRecord.buildFromJson("test/testresources/basicRealisticExampleConceptGraph.json");
            ConceptGraph graph = new ConceptGraph(graphRecord);
            List<LearningObjectLinkRecord> LOLRlist = JsonImportExport.LOLRFromRecords("test/testresources/basicRealisticExampleLOLRecord.json");
            graph.addLearningObjectsFromLearningObjectLinkRecords(csvReader.getManualGradedLearningObjects(), LOLRlist);
            graph.calcKnowledgeEstimates();
            //System.out.println(graph.toString());
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
