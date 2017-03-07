package edu.ithaca.dragonlab.ckc.io;
import java.util.ArrayList;

import edu.ithaca.dragonlab.ckc.learningobject.LearningObject;
import org.junit.Assert;
import org.junit.Test;
import edu.ithaca.dragonlab.ckc.learningobject.ManualGradedResponse;

/**
 * Created by willsuchanek on 3/6/17.
 */
public class CSVReaderTest {
    @Test
    public void createQuestionsTest(){
        String file = "test/testresources/DataCSVExample.csv";
        CSVReader readfile = new CSVReader(file);
        ArrayList<ManualGradedResponse> manualGradedResponseList = readfile.getManualGradedResponses();
        ArrayList<LearningObject> manualGradedLearningObjectList = readfile.getManualGradedLearningObjects();

        //Testing for first entry in the CSV
        Assert.assertEquals(25 * 9, manualGradedResponseList.size());
        ManualGradedResponse testQ = new ManualGradedResponse("Week 8 Exercises",6,6,"stu1");
        Assert.assertEquals(testQ.calcKnowledgeEstimate(), manualGradedResponseList.get(0).calcKnowledgeEstimate(),0);
        Assert.assertEquals(testQ.getNonNormalizedScore(), manualGradedResponseList.get(0).getNonNormalizedScore(),0);
        Assert.assertEquals(testQ.getMaxPossibleScore(), manualGradedResponseList.get(0).getMaxPossibleScore(),0);
        Assert.assertEquals(testQ.getUserId(), manualGradedResponseList.get(0).getUserId());
        Assert.assertEquals(testQ.getLearningObjectId(), manualGradedResponseList.get(0).getLearningObjectId());

        //Testing for last entry in CSV
        ManualGradedResponse testQ2 = new ManualGradedResponse("Lab 3: Function Practice (House Paint Calculator)",10,10,"stu25");
        int lastIndex = manualGradedResponseList.size()-1;
        Assert.assertEquals(testQ2.calcKnowledgeEstimate(),manualGradedResponseList.get(lastIndex).calcKnowledgeEstimate(),0);
        Assert.assertEquals(testQ2.getNonNormalizedScore(),manualGradedResponseList.get(lastIndex).getNonNormalizedScore(),0);
        Assert.assertEquals(testQ2.getMaxPossibleScore(),manualGradedResponseList.get(lastIndex).getMaxPossibleScore(),0);
        Assert.assertEquals(testQ2.getUserId(),manualGradedResponseList.get(lastIndex).getUserId());
        Assert.assertEquals(testQ2.getLearningObjectId(),manualGradedResponseList.get(lastIndex).getLearningObjectId());

        //Testing for the Learning Objects
        Assert.assertEquals(9, manualGradedLearningObjectList.size());
        Assert.assertEquals(25, manualGradedLearningObjectList.get(0).getResponses().size());
        //Making sure the first item in the ManualGradedResponses list is the first item in the first learning object of the learning object list
        Assert.assertEquals(manualGradedResponseList.get(0).calcKnowledgeEstimate(),manualGradedLearningObjectList.get(0).getResponses().get(0).calcKnowledgeEstimate(),0);
        Assert.assertEquals(manualGradedResponseList.get(0).getUserId(),manualGradedLearningObjectList.get(0).getResponses().get(0).getUserId());
        Assert.assertEquals(manualGradedResponseList.get(0).getLearningObjectId(),manualGradedLearningObjectList.get(0).getResponses().get(0).getLearningObjectId());
        //Can't access these but they are tested above
        //Assert.assertEquals(manualGradedResponseList.get(0).getNonNormalizedScore(),manualGradedLearningObjectList.get(0).getResponses().get(0).getNonNormalizedScore(),0);
        //Assert.assertEquals(manualGradedResponseList.get(0).getMaxPossibleScore(),manualGradedLearningObjectList.get(0).getResponses().get(0).getMaxPossibleScore(),0);
    }
}
