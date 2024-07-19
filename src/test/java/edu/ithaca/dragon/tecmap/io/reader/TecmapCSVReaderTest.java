package edu.ithaca.dragon.tecmap.io.reader;

import com.opencsv.exceptions.CsvException;
import edu.ithaca.dragon.tecmap.Settings;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItem;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItemResponse;
import edu.ithaca.dragon.tecmap.learningresource.ManualGradedResponse;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Created by willsuchanek on 3/6/17.
 */

// make pull request from dev to commaTest
public class TecmapCSVReaderTest {
    @Test
    public void createQuestionsTest() throws IOException, CsvException {
        // sakai format
        String file = Settings.TEST_RESOURCE_DIR + "ManuallyCreated/complexRealisticAssessment.csv";
        List<String[]> rows = CsvFileLibrary.parseRowsFromFile(file);
        List<CsvProcessor> processors = new ArrayList<>();
        processors.add(new CanvasConverter());
        CanvasReader readfile = new CanvasReader(rows, processors);
        List<AssessmentItemResponse> manualGradedResponseList = readfile.getManualGradedResponses();
        List<AssessmentItem> manualGradedColumnItemList = readfile.getManualGradedLearningObjects();
        //testing title entries from the csv files

        //Testing for first entry in the CSV
        assertEquals(25 * 9, manualGradedResponseList.size());
        ManualGradedResponse testQ = new ManualGradedResponse("Week 8 Exercises", 6, 6, "stu1");
        assertEquals(testQ.calcKnowledgeEstimate(), manualGradedResponseList.get(0).calcKnowledgeEstimate(), 0);
        assertEquals(testQ.getNonNormalizedScore(), ((ManualGradedResponse) manualGradedResponseList.get(0)).getNonNormalizedScore(), 0);
        assertEquals(testQ.getMaxPossibleScore(), ((ManualGradedResponse) manualGradedResponseList.get(0)).getMaxPossibleScore(), 0);
        assertEquals(testQ.getUserId(), manualGradedResponseList.get(0).getUserId());
        assertEquals(testQ.getAssessmentItemId(), manualGradedResponseList.get(0).getAssessmentItemId());

        //Testing for last entry in CSV
        ManualGradedResponse testQ2 = new ManualGradedResponse("Lab 3: Function Practice (House Paint Calculator)", 10, 10, "stu25");
        int lastIndex = manualGradedResponseList.size() - 1;
        assertEquals(testQ2.calcKnowledgeEstimate(), manualGradedResponseList.get(lastIndex).calcKnowledgeEstimate(), 0);
        assertEquals(testQ2.getNonNormalizedScore(), ((ManualGradedResponse) manualGradedResponseList.get(lastIndex)).getNonNormalizedScore(), 0);
        assertEquals(testQ2.getMaxPossibleScore(), ((ManualGradedResponse) manualGradedResponseList.get(lastIndex)).getMaxPossibleScore(), 0);
        assertEquals(testQ2.getUserId(), manualGradedResponseList.get(lastIndex).getUserId());
        assertEquals(testQ2.getAssessmentItemId(), manualGradedResponseList.get(lastIndex).getAssessmentItemId());

        //Testing for the Learning Objects
        assertEquals(9, manualGradedColumnItemList.size());
        assertEquals(25, manualGradedColumnItemList.get(0).getResponses().size());
        //Making sure the first item in the ManualGradedResponses list is the first item in the first learning object of the learning object list
        assertEquals(manualGradedResponseList.get(0).calcKnowledgeEstimate(), manualGradedColumnItemList.get(0).getResponses().get(0).calcKnowledgeEstimate(), 0);
        assertEquals(manualGradedResponseList.get(0).getUserId(), manualGradedColumnItemList.get(0).getResponses().get(0).getUserId());
        assertEquals(manualGradedResponseList.get(0).getAssessmentItemId(), manualGradedColumnItemList.get(0).getResponses().get(0).getAssessmentItemId());
        //Can't access these but they are tested above
        //assertEquals(manualGradedResponseList.get(0).getNonNormalizedScore(),manualGradedColumnItemList.get(0).getResponses().get(0).getNonNormalizedScore(),0);
        //assertEquals(manualGradedResponseList.get(0).getMaxPossibleScore(),manualGradedColumnItemList.get(0).getResponses().get(0).getMaxPossibleScore(),0);
    }
}
