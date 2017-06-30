package edu.ithaca.dragonlab.ckc.conceptgraph;

import edu.ithaca.dragonlab.ckc.io.CSVReader;
import edu.ithaca.dragonlab.ckc.learningobject.LearningObject;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by bleblanc2 on 6/13/17.
 */
public class KnowledgeEstimateMatrixTest {

    @Test
    public void createMatrixTest(){
        String file = "test/testresources/SmallDataCSVExample.csv";
        CSVReader data = new CSVReader(file);
        ArrayList<LearningObject> gotoMatrix = data.getManualGradedLearningObjects();
        KnowledgeEstimateMatrix newMatrix = new KnowledgeEstimateMatrix(gotoMatrix);
        double[][] myMatrix = newMatrix.getStudentKnowledgeEstimates();
        double[] arr1 = new double[9];
        double[] arr2 = new double[9];
        double[] arr3 = new double[9];


         for(int i = 0; i<9; i++) {
         arr1[i]= myMatrix[i][0];

         }
         for(int j=0; j<9; j++){
         //System.out.println(arr1[j]);
         }

         for(int i=0; i<9; i++) {
         arr2[i]= myMatrix[i][1];
         }
         for(int j=0; j<9; j++){
         //System.out.println(arr2[j]);
         }

         for(int i=0; i<9; i++) {
         arr3[i]= myMatrix[i][2];
         }
         for(int j=0; j<9; j++){
         //System.out.println(arr3[j]);
         }


        double[] exArr1 = new double[]{1, 1, 0.815, 0.7, 0, 0, 1, 1, 1};
        double[] exArr2 = new double[]{0, 1, 0.85, 1, 0, 0.9, 1, 1, 1};
        double[] exArr3 = new double[]{1, 1, 0.98, 1, 0.975, 1, 1, 1, 1};

        //KnowledgeEstimateMatrix Check
        Assert.assertArrayEquals(exArr1, arr1, 0);
        Assert.assertArrayEquals(exArr2, arr2, 0);
        Assert.assertArrayEquals(exArr3, arr3, 0);

        //userIdList Check
        List<String> actualString = new LinkedList<String>();
        actualString.add("stu1");
        actualString.add("stu2");
        actualString.add("stu3");
        Assert.assertEquals(actualString, newMatrix.getUserIdList());
    }

}

