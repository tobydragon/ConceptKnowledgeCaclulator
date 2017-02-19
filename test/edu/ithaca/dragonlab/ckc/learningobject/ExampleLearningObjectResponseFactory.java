package edu.ithaca.dragonlab.ckc.learningobject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tdragon
 *         2/19/17.
 */
public class ExampleLearningObjectResponseFactory {

    //TODO: fix all tests that use this.
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
}

