package edu.ithaca.dragonlab.ckc.stats;

import edu.ithaca.dragonlab.ckc.conceptgraph.KnowledgeEstimateMatrix;
import edu.ithaca.dragonlab.ckc.io.CSVReader;
import edu.ithaca.dragonlab.ckc.learningobject.LearningObject;
import org.junit.Assert;
import org.junit.Test;
import stats.RFunctions;

import java.util.List;

/**
 * Created by bleblanc2 on 6/19/17.
 */
public class RFunctionsTest {

/**
    @Test
    public void LearningObjectAvgTest(){
        String file = "test/testresources/partialComplexRealitsticAssessment.csv";
        CSVReader data = new CSVReader(file);
        ArrayList<LearningObject> gotoMatrix = data.getManualGradedLearningObjects();
        KnowledgeEstimateMatrix newMatrix = new KnowledgeEstimateMatrix(gotoMatrix);
        double avg = RFunctions.LearningObjectAvg(newMatrix, 2);
        Assert.assertEquals(0.88166, avg, 0.001);
        avg = RFunctions.LearningObjectAvg(newMatrix, 0);
        Assert.assertEquals(.666, avg, 0.001);
        avg = RFunctions.LearningObjectAvg(newMatrix, 8);
        Assert.assertEquals(1, avg, 0.001);
    }

    @Test
    public void studentKnowledgeEstAvgTest(){
        String file = "test/testresources/partialComplexRealitsticAssessment.csv";
        CSVReader data = new CSVReader(file);
        ArrayList<LearningObject> gotoMatrix = data.getManualGradedLearningObjects();
        KnowledgeEstimateMatrix newMatrix = new KnowledgeEstimateMatrix(gotoMatrix);
        double avg = RFunctions.StudentKnowledgeEstAvg(newMatrix, 0);
        Assert.assertEquals(.724, avg, 0.001);
        avg = RFunctions.StudentKnowledgeEstAvg(newMatrix, 1);
        Assert.assertEquals(.75, avg, 0.001);
        avg = RFunctions.StudentKnowledgeEstAvg(newMatrix, 2);
        Assert.assertEquals(.995, avg, 0.001);
    }
    */


//TODO: needs to be finished

    @Test
    public void findFactorCountTest(){
        try {
            CSVReader data = new CSVReader("test/testresources/ManuallyCreated/complexRealisticAssessment.csv");
            List<LearningObject> gotoMatrix = data.getManualGradedLearningObjects();
            KnowledgeEstimateMatrix newMatrix = new KnowledgeEstimateMatrix(gotoMatrix);
            double factorCount = RFunctions.findFactorCount(newMatrix);
            Assert.assertEquals(2, factorCount, 0);
        }catch (Exception e){
            e.printStackTrace();
            Assert.fail();
        }
    }




}
