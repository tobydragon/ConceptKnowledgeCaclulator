package edu.ithaca.dragonlab.ckc.learningobject;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tdragon
 *         2/19/17.
 */
public class ExampleLearningObjectResponseFactory {

    //3 students, 5 questions: 1 stud all right, one half right, one half right and missing 2 questions
    public static List<LearningObjectResponse> makeSimpleResponses(){
        List<LearningObjectResponse>  responses = new ArrayList<>();

        responses.add(new LearningObjectResponse("student1", "Q1", 1));
        responses.add(new LearningObjectResponse("student1", "Q2", 1));
        responses.add(new LearningObjectResponse("student1", "Q3", 1));
        responses.add(new LearningObjectResponse("student1", "Q4", 1));
        responses.add(new LearningObjectResponse("student1", "Q5", 1));
        responses.add(new LearningObjectResponse("student1", "Q6", 1));


        responses.add(new LearningObjectResponse("student2", "Q1", 1));
        responses.add(new LearningObjectResponse("student2", "Q2", 1));
        responses.add(new LearningObjectResponse("student2", "Q3", 1));
        responses.add(new LearningObjectResponse("student2", "Q4", 0));
        responses.add(new LearningObjectResponse("student2", "Q5", 0));
        responses.add(new LearningObjectResponse("student2", "Q6", 0));


        responses.add(new LearningObjectResponse("student3", "Q1", 1));
        responses.add(new LearningObjectResponse("student3", "Q2", 1));
        responses.add(new LearningObjectResponse("student3", "Q3", 0));
        responses.add(new LearningObjectResponse("student3", "Q4", 0));

        return responses;
    }


    @Test
    public void lorEqualsTest(){
        LearningObjectResponse lor0 = new LearningObjectResponse("student1", "Q1", 1);
        LearningObjectResponse lor1 = new LearningObjectResponse("student1", "Q1", 1);
        LearningObjectResponse lor2 = new LearningObjectResponse("student1", "Q2", 1);
        LearningObjectResponse lor3 = new LearningObjectResponse("student2", "Q2", 1);
        LearningObjectResponse lor4 = new LearningObjectResponse("student3", "Q2", 0);

        Assert.assertEquals(true, lor0.equals(lor1));
        Assert.assertEquals(false, lor0.equals(lor2));
        Assert.assertEquals(false, lor0.equals(lor3));
        Assert.assertEquals(false, lor0.equals(lor4));
        Assert.assertEquals(false, lor3.equals(lor4));
    }

    @Test
    public void loEqualsTest(){
        LearningObject lo1 = new LearningObject("Q1");
        lo1.addResponse(new LearningObjectResponse("student1", "Q1", 1));
        lo1.addResponse(new LearningObjectResponse("student1", "Q1", 1));

        LearningObject lo2 = new LearningObject("Q1");
        lo2.addResponse(new LearningObjectResponse("student1", "Q1", 1));
        lo2.addResponse(new LearningObjectResponse("student1", "Q1", 1));

        LearningObject lo3 = new LearningObject("Q2");
        lo3.addResponse(new LearningObjectResponse("student1", "Q2", 1));
        lo3.addResponse(new LearningObjectResponse("student1", "Q2", 1));

        Assert.assertEquals(true, lo1.equals(lo2));
        Assert.assertEquals(false, lo1.equals(lo3));
    }
}

