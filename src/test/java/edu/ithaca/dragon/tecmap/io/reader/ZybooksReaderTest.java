package edu.ithaca.dragon.tecmap.io.reader;

import edu.ithaca.dragon.tecmap.Settings;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItem;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItemResponse;
import edu.ithaca.dragon.tecmap.learningresource.ManualGradedResponse;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by rdebolt on 10/23/18.
 */

// make pull request from dev to commaTest
public class ZybooksReaderTest {
    //ReaderTools toolBox = new ReaderTools();

    @Test
    public void FullNameTest(){
        String file = Settings.TEST_RESOURCE_DIR + "io/assessmentSources/zybook-comp115-DataExample.csv";
        List<String> name = new ArrayList<>(Arrays.asList("DeBolt", "Ryan", "12", "100", "95"));
        String returnName = "DeBolt Ryan1";
        List<String> nameList = new ArrayList<>(Arrays.asList("DeBolt Ryan", "Suchanek Will", "Dragon Toby"));
        try{
            ZybooksReader readfile = new ZybooksReader(file);
            Assert.assertEquals(returnName, readfile.makeFullName(name, nameList));
        }
        catch (Exception e){
            e.printStackTrace();
            Assert.fail();
        }
    }
    @Test
    public void FullNameTestnoConflict(){
        String file = Settings.TEST_RESOURCE_DIR + "io/assessmentSources/zybook-comp115-DataExample.csv";
        List<String> name = new ArrayList<>(Arrays.asList("DeBolt", "Ryan", "12", "100", "95"));
        String returnName = "DeBolt Ryan";
        List<String> nameList = new ArrayList<>(Arrays.asList("Lane Nicole", "Suchanek Will", "Dragon Toby"));
        try{
            ZybooksReader readfile = new ZybooksReader(file);
            Assert.assertEquals(returnName, readfile.makeFullName(name, nameList));
        }
        catch (Exception e){
            e.printStackTrace();
            Assert.fail();
        }
    }
    @Test
    public void FullNameTestTwoConflicts(){
        String file = Settings.TEST_RESOURCE_DIR + "io/assessmentSources/zybook-comp115-DataExample.csv";
        List<String> name = new ArrayList<>(Arrays.asList("DeBolt", "Ryan", "12", "100", "95"));
        String returnName = "DeBolt Ryan2";
        List<String> nameList = new ArrayList<>(Arrays.asList("DeBolt Ryan", "DeBolt Ryan1", "Suchanek Will", "Dragon Toby"));
        try{
            ZybooksReader readfile = new ZybooksReader(file);
            Assert.assertEquals(returnName, readfile.makeFullName(name, nameList));
        }
        catch (Exception e){
            e.printStackTrace();
            Assert.fail();
        }
    }
    @Test
    public void createQuestionsTest() {
        String file = Settings.TEST_RESOURCE_DIR + "io/assessmentSources/zybook-comp115-DataExample.csv";
        try {
            ZybooksReader readfile = new ZybooksReader(file);
            List<AssessmentItemResponse> manualGradedResponseList = readfile.getManualGradedResponses();
            List<AssessmentItem> manualGradedAssessmentItemList = readfile.getManualGradedLearningObjects();
            //testing title entries from the csv files

            //Testing for first entry in the CSV
            Assert.assertEquals(16 * 26, manualGradedResponseList.size());
            ManualGradedResponse testQ = new ManualGradedResponse("Participation total", 148, 78.37837838, "lname01 fname01");
            Assert.assertEquals(testQ.calcKnowledgeEstimate(), manualGradedResponseList.get(0).calcKnowledgeEstimate(), 0);
            Assert.assertEquals(testQ.getNonNormalizedScore(), ((ManualGradedResponse) manualGradedResponseList.get(0)).getNonNormalizedScore(), 0);
            Assert.assertEquals(testQ.getMaxPossibleScore(), ((ManualGradedResponse) manualGradedResponseList.get(0)).getMaxPossibleScore(), 0);
            Assert.assertEquals(testQ.getUserId(), manualGradedResponseList.get(0).getUserId());
            Assert.assertEquals(testQ.getLearningObjectId(), manualGradedResponseList.get(0).getLearningObjectId());

            //Testing for last entry in CSV
            ManualGradedResponse testQ2 = new ManualGradedResponse("1.12 - Challenge", 0, 0, "lname16 fname16");
            int lastIndex = manualGradedResponseList.size() - 1;
            Assert.assertEquals(testQ2.calcKnowledgeEstimate(), manualGradedResponseList.get(lastIndex).calcKnowledgeEstimate(), 0);
            Assert.assertEquals(testQ2.getNonNormalizedScore(), ((ManualGradedResponse) manualGradedResponseList.get(lastIndex)).getNonNormalizedScore(), 0);
            Assert.assertEquals(testQ2.getMaxPossibleScore(), ((ManualGradedResponse) manualGradedResponseList.get(lastIndex)).getMaxPossibleScore(), 0);
            Assert.assertEquals(testQ2.getUserId(), manualGradedResponseList.get(lastIndex).getUserId());
            Assert.assertEquals(testQ2.getLearningObjectId(), manualGradedResponseList.get(lastIndex).getLearningObjectId());

            //Testing for the Learning Objects
            Assert.assertEquals(26, manualGradedAssessmentItemList.size());
            Assert.assertEquals(16, manualGradedAssessmentItemList.get(0).getResponses().size());
            //Making sure the first item in the ManualGradedResponses list is the first item in the first learning object of the learning object list
            Assert.assertEquals(manualGradedResponseList.get(0).calcKnowledgeEstimate(), manualGradedAssessmentItemList.get(0).getResponses().get(0).calcKnowledgeEstimate(), 0);
            Assert.assertEquals(manualGradedResponseList.get(0).getUserId(), manualGradedAssessmentItemList.get(0).getResponses().get(0).getUserId());
            Assert.assertEquals(manualGradedResponseList.get(0).getLearningObjectId(), manualGradedAssessmentItemList.get(0).getResponses().get(0).getLearningObjectId());
            //Can't access these but they are tested above
            //Assert.assertEquals(manualGradedResponseList.get(0).getNonNormalizedScore(),manualGradedAssessmentItemList.get(0).getResponses().get(0).getNonNormalizedScore(),0);
            //Assert.assertEquals(manualGradedResponseList.get(0).getMaxPossibleScore(),manualGradedAssessmentItemList.get(0).getResponses().get(0).getMaxPossibleScore(),0);
        } catch (IOException e) {
            Assert.fail();
        }

    }
}