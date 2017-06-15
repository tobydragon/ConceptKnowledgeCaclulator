package edu.ithaca.dragonlab.ckc.conceptgraph;

import edu.ithaca.dragonlab.ckc.io.CSVOutputter;
import edu.ithaca.dragonlab.ckc.io.CSVReader;
import edu.ithaca.dragonlab.ckc.learningobject.ExampleLearningObjectResponseFactory;
import edu.ithaca.dragonlab.ckc.learningobject.LearningObject;
import edu.ithaca.dragonlab.ckc.learningobject.LearningObjectResponse;
import org.junit.Assert;
import org.junit.Assert.*;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by bleblanc2 on 6/13/17.
 */
public class MatrixCreatorTest {

    @Test
    public void createMatrixTest(){
        String file = "test/testresources/SmallDataCSVExample.csv";
        CSVReader data = new CSVReader(file);
        ArrayList<LearningObject> gotoMatrix = data.getManualGradedLearningObjects();
        MatrixCreator newMatrix = new MatrixCreator(gotoMatrix);
        double[][] myMatrix = newMatrix.getStructure();
        double[] arr1 = new double[9];
        double[] arr2 = new double[9];
        double[] arr3 = new double[9];
        for(int i=0; i<9; i++) {
            arr1[i]= myMatrix[i][0];
            for(int j=0; i<9; j++){
                System.out.println(arr1[j]);
            }
        }
        for(int i=0; i<9; i++) {
            arr2[i]= myMatrix[i][0];
            for(int j=0; i<9; j++){
                System.out.println(arr1[j]);
            }
        }
        for(int i=0; i<9; i++) {
            arr3[i]= myMatrix[i][0];
            for(int j=0; i<9; j++){
                System.out.println(arr1[j]);
            }
        }

        double[] exArr1 = new double[]{1, 1, 0.815, 0.7, 0, 0, 1, 1, 1};
        double[] exArr2 = new double[]{0, 1, 0.85, 1, 0, 0.9, 1, 1, 1};
        double[] exArr3 = new double[]{1, 1, 0.98, 1, 0.975, 1, 1, 1, 1};

        Assert.assertArrayEquals(exArr1, arr1, 0);
        Assert.assertArrayEquals(exArr2, arr2, 0);
        Assert.assertArrayEquals(exArr3, arr3, 0);


    }

}

