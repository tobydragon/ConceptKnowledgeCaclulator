package edu.ithaca.dragon.tecmap.io.reader;

import edu.ithaca.dragon.tecmap.Settings;
import edu.ithaca.dragon.tecmap.learningobject.AssessmentItem;
import edu.ithaca.dragon.tecmap.learningobject.AssessmentItemResponse;
import edu.ithaca.dragon.tecmap.learningobject.ManualGradedResponse;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

/**
 * Created by willsuchanek on 3/6/17.
 */

// make pull request from dev to commaTest
public class CSVReaderTest {
    @Test
    public void createQuestionsTest() {
        String file = Settings.TEST_RESOURCE_DIR + "ManuallyCreated/complexRealisticAssessment.csv";
        try {
            SakaiReader readfile = new SakaiReader(file);
            List<AssessmentItemResponse> manualGradedResponseList = readfile.getManualGradedResponses();
            List<AssessmentItem> manualGradedAssessmentItemList = readfile.getManualGradedLearningObjects();
            //testing title entries from the csv files

            //Testing for first entry in the CSV
            Assert.assertEquals(25 * 9, manualGradedResponseList.size());
            ManualGradedResponse testQ = new ManualGradedResponse("Week 8 Exercises", 6, 6, "stu1");
            Assert.assertEquals(testQ.calcKnowledgeEstimate(), manualGradedResponseList.get(0).calcKnowledgeEstimate(), 0);
            Assert.assertEquals(testQ.getNonNormalizedScore(), ((ManualGradedResponse) manualGradedResponseList.get(0)).getNonNormalizedScore(), 0);
            Assert.assertEquals(testQ.getMaxPossibleScore(), ((ManualGradedResponse) manualGradedResponseList.get(0)).getMaxPossibleScore(), 0);
            Assert.assertEquals(testQ.getUserId(), manualGradedResponseList.get(0).getUserId());
            Assert.assertEquals(testQ.getLearningObjectId(), manualGradedResponseList.get(0).getLearningObjectId());

            //Testing for last entry in CSV
            ManualGradedResponse testQ2 = new ManualGradedResponse("Lab 3: Function Practice (House Paint Calculator)", 10, 10, "stu25");
            int lastIndex = manualGradedResponseList.size() - 1;
            Assert.assertEquals(testQ2.calcKnowledgeEstimate(), manualGradedResponseList.get(lastIndex).calcKnowledgeEstimate(), 0);
            Assert.assertEquals(testQ2.getNonNormalizedScore(), ((ManualGradedResponse) manualGradedResponseList.get(lastIndex)).getNonNormalizedScore(), 0);
            Assert.assertEquals(testQ2.getMaxPossibleScore(), ((ManualGradedResponse) manualGradedResponseList.get(lastIndex)).getMaxPossibleScore(), 0);
            Assert.assertEquals(testQ2.getUserId(), manualGradedResponseList.get(lastIndex).getUserId());
            Assert.assertEquals(testQ2.getLearningObjectId(), manualGradedResponseList.get(lastIndex).getLearningObjectId());

            //Testing for the Learning Objects
            Assert.assertEquals(9, manualGradedAssessmentItemList.size());
            Assert.assertEquals(25, manualGradedAssessmentItemList.get(0).getResponses().size());
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
