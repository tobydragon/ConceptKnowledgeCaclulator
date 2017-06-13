package edu.ithaca.dragonlab.ckc.conceptgraph;

import edu.ithaca.dragonlab.ckc.io.CSVOutputter;
import edu.ithaca.dragonlab.ckc.learningobject.ExampleLearningObjectResponseFactory;
import edu.ithaca.dragonlab.ckc.learningobject.LearningObject;
import edu.ithaca.dragonlab.ckc.learningobject.LearningObjectResponse;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Created by bleblanc2 on 6/13/17.
 */
public class MatrixCreatorTest {

    @Test
    public class StringToMatrixTest(){
        List<LearningObjectResponse> responses = ExampleLearningObjectResponseFactory.makeSimpleResponses();
        CSVOutputter outputter = new CSVOutputter(responses);

        String matrixString = outputter.makeCSV();
        double[][] expectedMatrix = new double[3][6];
        Assert.assertEquals(expectedMatrix, MatrixCreator.createMatrix(matrixString));

    }

}

