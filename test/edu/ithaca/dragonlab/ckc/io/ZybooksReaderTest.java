package edu.ithaca.dragonlab.ckc.io;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.ithaca.dragonlab.ckc.learningobject.LearningObject;
import edu.ithaca.dragonlab.ckc.learningobject.LearningObjectResponse;
import org.junit.Assert;
import org.junit.Test;
import edu.ithaca.dragonlab.ckc.learningobject.ManualGradedResponse;

/**
 * Created by rdebolt on 10/23/18.
 */

// make pull request from dev to commaTest
public class ZybooksReaderTest {
    ReaderTools toolBox = new ReaderTools();
    @Test
    public void titleCommasTest() {
        String titles = "this is, a title to test. this, will not work";
        List<String> myList = Arrays.asList("this is","a title to test. this","will not work");
        try {
            Assert.assertEquals(myList, toolBox.lineToList(titles));
        }
        catch(Exception e){
            e.printStackTrace();
            Assert.fail();
        }
    }
    @Test
    public void titleCommasTestTwo() {
        String titles = "another test, to fail again, lets see how this goes";
        List<String> myList = Arrays.asList("another test", "to fail again", "lets see how this goes");
        try {
            Assert.assertEquals(myList, toolBox.lineToList(titles));
        }
        catch(Exception e){
            e.printStackTrace();
            Assert.fail();
        }
    }
    @Test
    public void titleCommasTestThree() {
        String titles = "hello, a third test, maybe \"this one will pass\". Probably not";
        List<String> myList = Arrays.asList("hello", "a third test" , "maybe this one will pass. Probably not");
        try {
            Assert.assertEquals(myList, toolBox.lineToList(titles));
        }
        catch(Exception e){
            e.printStackTrace();
            Assert.fail();
        }
    }
    @Test
    public void titleStarTest () {
        String titles = "hello, a \"fourth\" test, *Breaks here*";
        List<String> myList = Arrays.asList("hello", "a fourth test" , "*Breaks here*");
        try {
            Assert.assertEquals(myList, toolBox.lineToList(titles));
        }
        catch(Exception e){
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void titleColonTest() {
        String titles = "Test 5: another break, \"maybe, maybe-not?\"";
        List<String> myList = Arrays.asList("Test 5: another break", "maybe, maybe-not?");
        try {
            Assert.assertEquals(myList, toolBox.lineToList(titles));
        }
        catch(Exception e){
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void createQuestionsTest() {
        String file = "test/testresources/io/assessmentSources/zybook-comp115-DataExample.csv";
        try {
            ZybooksReader readfile = new ZybooksReader(file);
            List<LearningObjectResponse> manualGradedResponseList = readfile.getManualGradedResponses();
            List<LearningObject> manualGradedLearningObjectList = readfile.getManualGradedLearningObjects();
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
            Assert.assertEquals(26, manualGradedLearningObjectList.size());
            Assert.assertEquals(16, manualGradedLearningObjectList.get(0).getResponses().size());
            //Making sure the first item in the ManualGradedResponses list is the first item in the first learning object of the learning object list
            Assert.assertEquals(manualGradedResponseList.get(0).calcKnowledgeEstimate(), manualGradedLearningObjectList.get(0).getResponses().get(0).calcKnowledgeEstimate(), 0);
            Assert.assertEquals(manualGradedResponseList.get(0).getUserId(), manualGradedLearningObjectList.get(0).getResponses().get(0).getUserId());
            Assert.assertEquals(manualGradedResponseList.get(0).getLearningObjectId(), manualGradedLearningObjectList.get(0).getResponses().get(0).getLearningObjectId());
            //Can't access these but they are tested above
            //Assert.assertEquals(manualGradedResponseList.get(0).getNonNormalizedScore(),manualGradedLearningObjectList.get(0).getResponses().get(0).getNonNormalizedScore(),0);
            //Assert.assertEquals(manualGradedResponseList.get(0).getMaxPossibleScore(),manualGradedLearningObjectList.get(0).getResponses().get(0).getMaxPossibleScore(),0);
        } catch (IOException e) {
            Assert.fail();
        }

    }
}