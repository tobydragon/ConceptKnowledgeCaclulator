package edu.ithaca.dragon.tecmap.io.reader;

import edu.ithaca.dragon.tecmap.Settings;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItem;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItemResponse;
import edu.ithaca.dragon.tecmap.learningresource.ManualGradedResponse;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Created by rdebolt on 10/23/18.
 */

// make pull request from dev to commaTest
public class ZybooksReaderTest {

    @Test
    public void createQuestionsTest() {
        String file = Settings.TEST_RESOURCE_DIR + "io/assessmentSources/zybook-comp115-DataExample.csv";
        try {
            ZybooksReader readfile = new ZybooksReader(file);
            List<AssessmentItemResponse> manualGradedResponseList = readfile.getManualGradedResponses();
            List<AssessmentItem> manualGradedColumnItemList = readfile.getManualGradedLearningObjects();
            //testing title entries from the csv files

            //Testing for first entry in the CSV
            assertEquals(16 * 26, manualGradedResponseList.size());
            ManualGradedResponse testQ = new ManualGradedResponse("Participation total", 148, 78.37837838, "lname01 fname01");
            assertEquals(testQ.calcKnowledgeEstimate(), manualGradedResponseList.get(0).calcKnowledgeEstimate(), 0);
            assertEquals(testQ.getNonNormalizedScore(), ((ManualGradedResponse) manualGradedResponseList.get(0)).getNonNormalizedScore(), 0);
            assertEquals(testQ.getMaxPossibleScore(), ((ManualGradedResponse) manualGradedResponseList.get(0)).getMaxPossibleScore(), 0);
            assertEquals(testQ.getUserId(), manualGradedResponseList.get(0).getUserId());
            assertEquals(testQ.getLearningObjectId(), manualGradedResponseList.get(0).getLearningObjectId());

            //Testing for last entry in CSV
            ManualGradedResponse testQ2 = new ManualGradedResponse("1.12 - Challenge", 0, 0, "lname16 fname16");
            int lastIndex = manualGradedResponseList.size() - 1;
            assertEquals(testQ2.calcKnowledgeEstimate(), manualGradedResponseList.get(lastIndex).calcKnowledgeEstimate(), 0);
            assertEquals(testQ2.getNonNormalizedScore(), ((ManualGradedResponse) manualGradedResponseList.get(lastIndex)).getNonNormalizedScore(), 0);
            assertEquals(testQ2.getMaxPossibleScore(), ((ManualGradedResponse) manualGradedResponseList.get(lastIndex)).getMaxPossibleScore(), 0);
            assertEquals(testQ2.getUserId(), manualGradedResponseList.get(lastIndex).getUserId());
            assertEquals(testQ2.getLearningObjectId(), manualGradedResponseList.get(lastIndex).getLearningObjectId());

            //Testing for the Learning Objects
            assertEquals(26, manualGradedColumnItemList.size());
            assertEquals(16, manualGradedColumnItemList.get(0).getResponses().size());
            //Making sure the first item in the ManualGradedResponses list is the first item in the first learning object of the learning object list
            assertEquals(manualGradedResponseList.get(0).calcKnowledgeEstimate(), manualGradedColumnItemList.get(0).getResponses().get(0).calcKnowledgeEstimate(), 0);
            assertEquals(manualGradedResponseList.get(0).getUserId(), manualGradedColumnItemList.get(0).getResponses().get(0).getUserId());
            assertEquals(manualGradedResponseList.get(0).getLearningObjectId(), manualGradedColumnItemList.get(0).getResponses().get(0).getLearningObjectId());
            //Can't access these but they are tested above
            //assertEquals(manualGradedResponseList.get(0).getNonNormalizedScore(),manualGradedColumnItemList.get(0).getResponses().get(0).getNonNormalizedScore(),0);
            //assertEquals(manualGradedResponseList.get(0).getMaxPossibleScore(),manualGradedColumnItemList.get(0).getResponses().get(0).getMaxPossibleScore(),0);
        } catch (IOException e) {
            fail();
        }

    }
}