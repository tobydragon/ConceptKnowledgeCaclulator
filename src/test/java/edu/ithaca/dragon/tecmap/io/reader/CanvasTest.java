package edu.ithaca.dragon.tecmap.io.reader;

import com.opencsv.exceptions.CsvException;
import edu.ithaca.dragon.tecmap.Settings;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItem;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItemResponse;
import edu.ithaca.dragon.tecmap.learningresource.ManualGradedResponse;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class CanvasTest {

    @Test
    public void anonymizeTest() throws IOException, CsvException {
        List<String[]> newRows = CsvRepresentation.parseRowsFromFile(Settings.DEFAULT_TEST_DATASTORE_PATH+"CanvasFiles/CanvasGradeExample.csv");
        Anonymizer anonymizer = new Anonymizer(3);
        anonymizer.anonymize(newRows);

        List<String[]> origRows = CsvRepresentation.parseRowsFromFile(
                Settings.DEFAULT_TEST_DATASTORE_PATH + "CanvasFiles/CanvasGradeExample.csv");

        // check labels weren't disturbed
        for (int row = 0; row < anonymizer.getGradeStartColumnIndex(); row++) {
            for (int column = 0; column < origRows.get(0).length; column++) {
                assertEquals(origRows.get(row)[column], newRows.get(row)[column]);
            }
        }

        Map<String, String> real2anonId = anonymizer.getRealId2anonId();
        //make sure look up of orig matches new for all numbers
        for(String[] origRow: origRows){
            for (String[] newRow : newRows) {
                if (newRow[0].equals(real2anonId.get(origRow[0]))){
                    for (int i=4; i<newRow.length; i++){ // check if the grades are matched
                        if (!newRow[i].equals(origRow[i])){
                            fail("not matching values for new row:"+ newRow[0] + " and orig row:" + origRow[0]);
                        }
                    }


                }
            }
        }
        for (int row = 3; row < newRows.size() - 1; row++) {
            for (int column = row + 1; column < newRows.size(); column++) {
                assertNotEquals(newRows.get(row)[0], newRows.get(column)[0]); // check if anonName is unique (different from the rest)
                assertNotEquals(newRows.get(row)[1], newRows.get(column)[1]); // check if anonId is unique (different from the rest)
            }
        }
    }


    @Test
    void createQuestionsTest() throws IOException, CsvException {
        // need to convert from Canvas to Sakai label or use converted file
        CanvasReader file = new CanvasReader(Settings.DEFAULT_TEST_DATASTORE_PATH + "CanvasFiles/convertedCanvasGradeExample.csv");
        List<AssessmentItemResponse> manualGradedResponseList = file.getManualGradedResponses();
        List<AssessmentItem> manualGradedColumnItemList = file.getManualGradedLearningObjects();
        //testing title entries from the csv files
        //Testing for first entry in the CSV
        assertEquals(8 * 9, manualGradedResponseList.size());
        ManualGradedResponse testQ = new ManualGradedResponse("172-Task00-01-Reading-Python Review (227500)", 4, 4, "1");
        assertEquals(testQ.calcKnowledgeEstimate(), manualGradedResponseList.get(0).calcKnowledgeEstimate(), 0);
        assertEquals(testQ.getNonNormalizedScore(), ((ManualGradedResponse) manualGradedResponseList.get(0)).getNonNormalizedScore(), 0);
        assertEquals(testQ.getMaxPossibleScore(), ((ManualGradedResponse) manualGradedResponseList.get(0)).getMaxPossibleScore(), 0);
        assertEquals(testQ.getUserId(), manualGradedResponseList.get(0).getUserId());
        assertEquals(testQ.getLearningObjectId(), manualGradedResponseList.get(0).getLearningObjectId());

        //Testing for last entry in CSV
        ManualGradedResponse testQ2 = new ManualGradedResponse("172-Task01-04- Tracing Objects (230529)", 3, 3, "8");
        int lastIndex = manualGradedResponseList.size() - 1;
        assertEquals(testQ2.calcKnowledgeEstimate(), manualGradedResponseList.get(lastIndex).calcKnowledgeEstimate(), 0);
        assertEquals(testQ2.getNonNormalizedScore(), ((ManualGradedResponse) manualGradedResponseList.get(lastIndex)).getNonNormalizedScore(), 0);
        assertEquals(testQ2.getMaxPossibleScore(), ((ManualGradedResponse) manualGradedResponseList.get(lastIndex)).getMaxPossibleScore(), 0);
        assertEquals(testQ2.getUserId(), manualGradedResponseList.get(lastIndex).getUserId());
        assertEquals(testQ2.getLearningObjectId(), manualGradedResponseList.get(lastIndex).getLearningObjectId());

        //Testing for the Learning Objects
        assertEquals(9, manualGradedColumnItemList.size());
        assertEquals(8, manualGradedColumnItemList.get(0).getResponses().size());
        //Making sure the first item in the ManualGradedResponses list is the first item in the first learning object of the learning object list
        assertEquals(manualGradedResponseList.get(0).calcKnowledgeEstimate(), manualGradedColumnItemList.get(0).getResponses().get(0).calcKnowledgeEstimate(), 0);
        assertEquals(manualGradedResponseList.get(0).getUserId(), manualGradedColumnItemList.get(0).getResponses().get(0).getUserId());
        assertEquals(manualGradedResponseList.get(0).getLearningObjectId(), manualGradedColumnItemList.get(0).getResponses().get(0).getLearningObjectId());
    }


}
