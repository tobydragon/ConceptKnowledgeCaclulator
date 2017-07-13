package edu.ithaca.dragonlab.ckc.io;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ithaca.dragonlab.ckc.learningobject.LearningObject;
import edu.ithaca.dragonlab.ckc.util.DataUtil;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static edu.ithaca.dragonlab.ckc.io.LearningObjectLinkRecord.createLearningObjectLinkRecords;

/**
 * Created by willsuchanek on 4/10/17.
 */
public class LearningObjectLinkRecordTest {
    @Test
    public void addConceptIDTest(){
        ArrayList<String> concepts = new ArrayList<>();
        concepts.add("concept 1");
        String id = "id 1";
        LearningObjectLinkRecord loObject = new LearningObjectLinkRecord(id,concepts);
        Assert.assertEquals("concept 1",loObject.getConceptIds().get(0));
        Assert.assertEquals(1,loObject.getConceptIds().size());
        Assert.assertEquals("id 1", loObject.getLearningObject());
        loObject.addConceptId("concept 2");
        Assert.assertEquals("concept 2", loObject.getConceptIds().get(1));
        Assert.assertEquals(2,loObject.getConceptIds().size());
    }
    @Test
    public void toStringTest(){
        ArrayList<String> concepts = new ArrayList<>();
        concepts.add("concept 1");
        concepts.add("concept 2");
        String id = "id 1";
        LearningObjectLinkRecord loObject = new LearningObjectLinkRecord(id,concepts);
        Assert.assertEquals("(Learning Object ID: id 1 Concept IDs: concept 1, concept 2)\n",loObject.toString());
    }
    @Test
    public void toJsonTest(){
        ArrayList<String> concepts = new ArrayList<>();
        concepts.add("concept 1");
        concepts.add("concept 2");
        String id = "id 1";
        LearningObjectLinkRecord loObject = new LearningObjectLinkRecord(id,concepts);

        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(new File("test/testresources/io/recordToJson.json"), loObject);
        }catch (Exception e){
            e.printStackTrace();
        }

        mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            LearningObjectLinkRecord  record = mapper.readValue(new File("test/testresources/io/recordToJson.json"), LearningObjectLinkRecord.class);

            Assert.assertEquals("id 1", record.getLearningObject());
            Assert.assertEquals(2, record.getConceptIds().size());
            Assert.assertEquals("concept 2", record.getConceptIds().get(1));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void readSimpleFromFile(){
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);


            List<LearningObjectLinkRecord> list = LearningObjectLinkRecord.buildListFromJson("test/testresources/ManuallyCreated/simpleResource.json");

            Assert.assertEquals(6, list.size());
            Assert.assertEquals("Q1", list.get(0).getLearningObject());
            Assert.assertEquals(1, list.get(0).getConceptIds().size());
            Assert.assertEquals("C", list.get(2).getConceptIds().get(0));
            Assert.assertEquals(1, list.get(0).getDataImportance(), DataUtil.OK_FLOAT_MARGIN);
            Assert.assertEquals(1, list.get(1).getDataImportance(), DataUtil.OK_FLOAT_MARGIN);
            Assert.assertEquals(.3, list.get(2).getDataImportance(), DataUtil.OK_FLOAT_MARGIN);
        }
        catch(Exception e){
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void createLearningObjectLinkRecordsTest(){
        try {
            CSVReader test = new CSVReader("test/testresources/ManuallyCreated/complexRealisticAssessment.csv");
            Collection<LearningObject> list = test.getManualGradedLearningObjects();
            List<LearningObject> list2 = test.getManualGradedLearningObjects();
            List<LearningObjectLinkRecord> lolrList = LearningObjectLinkRecord.createLearningObjectLinkRecords(list, 1);
            List<String> resultString = new ArrayList<String>();
            for (LearningObjectLinkRecord lolr : lolrList) {
                resultString.add(lolr.getLearningObject());
            }

            List<String> list2string = new ArrayList<String>();
            for (LearningObject lo : list2) {
                list2string.add(lo.getId());
            }
            Assert.assertEquals(list2string.toString(), resultString.toString());
        }catch (IOException e){
            e.printStackTrace();
            Assert.fail();
        }
    }


    /**
    @Test
    public void lolrToTextTest(){
        String testfile = "test/testresources/io/DataCSVExample.csv";
        CSVReader test = new CSVReader(testfile);
        Collection<LearningObject> list = test.getManualGradedLearningObjects();
        List<LearningObject> list2 = test.getManualGradedLearningObjects();
        List<LearningObjectLinkRecord> lolrList = LearningObjectLinkRecord.createLearningObjectLinkRecords(list, 1);
        Path txtfile = LearningObjectLinkRecord.lolrToTxt(lolrList.get(0), "test.txt");


    }
    */
}
