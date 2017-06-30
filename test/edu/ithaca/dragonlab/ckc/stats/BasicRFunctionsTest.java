package edu.ithaca.dragonlab.ckc.stats;

import edu.ithaca.dragonlab.ckc.conceptgraph.KnowledgeEstimateMatrix;
import edu.ithaca.dragonlab.ckc.learningobject.LearningObject;
import org.junit.Test;
import org.junit.Assert;
import edu.ithaca.dragonlab.ckc.io.CSVReader;
import stats.BasicRFunctions;

import java.util.ArrayList;

/**
 * Created by bleblanc2 on 6/19/17.
 */
public class BasicRFunctionsTest {

/**
    @Test
    public void LearningObjectAvgTest(){
        String file = "test/testresources/SmallDataCSVExample.csv";
        CSVReader data = new CSVReader(file);
        ArrayList<LearningObject> gotoMatrix = data.getManualGradedLearningObjects();
        KnowledgeEstimateMatrix newMatrix = new KnowledgeEstimateMatrix(gotoMatrix);
        double avg = BasicRFunctions.LearningObjectAvg(newMatrix, 2);
        Assert.assertEquals(0.88166, avg, 0.001);
        avg = BasicRFunctions.LearningObjectAvg(newMatrix, 0);
        Assert.assertEquals(.666, avg, 0.001);
        avg = BasicRFunctions.LearningObjectAvg(newMatrix, 8);
        Assert.assertEquals(1, avg, 0.001);
    }

    @Test
    public void studentKnowledgeEstAvgTest(){
        String file = "test/testresources/SmallDataCSVExample.csv";
        CSVReader data = new CSVReader(file);
        ArrayList<LearningObject> gotoMatrix = data.getManualGradedLearningObjects();
        KnowledgeEstimateMatrix newMatrix = new KnowledgeEstimateMatrix(gotoMatrix);
        double avg = BasicRFunctions.StudentKnowledgeEstAvg(newMatrix, 0);
        Assert.assertEquals(.724, avg, 0.001);
        avg = BasicRFunctions.StudentKnowledgeEstAvg(newMatrix, 1);
        Assert.assertEquals(.75, avg, 0.001);
        avg = BasicRFunctions.StudentKnowledgeEstAvg(newMatrix, 2);
        Assert.assertEquals(.995, avg, 0.001);
    }
    */
}
