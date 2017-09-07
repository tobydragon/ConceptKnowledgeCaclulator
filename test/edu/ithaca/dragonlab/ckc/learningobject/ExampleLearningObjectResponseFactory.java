package edu.ithaca.dragonlab.ckc.learningobject;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public static Map<String, LearningObject> makeSimpleLearningObjectMap(){
        Map<String, LearningObject> los = new HashMap<>();

        los.put("Q1", new LearningObject("Q1"));
        los.put("Q2", new LearningObject("Q2"));
        los.put("Q3", new LearningObject("Q3"));
        los.put("Q4", new LearningObject("Q4"));
        los.put("Q5", new LearningObject("Q5"));
        los.put("Q6", new LearningObject("Q6"));

        for (LearningObjectResponse resp : makeSimpleResponses()){
            los.get(resp.getLearningObjectId()).addResponse(resp);
        }
        return los;
    }
}

