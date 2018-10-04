package edu.ithaca.dragon.tecmap.learningresource;

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
    public static List<AssessmentItemResponse> makeSimpleResponses(){
        List<AssessmentItemResponse>  responses = new ArrayList<>();

        responses.add(new AssessmentItemResponse("student1", "Q1", 1));
        responses.add(new AssessmentItemResponse("student1", "Q2", 1));
        responses.add(new AssessmentItemResponse("student1", "Q3", 1));
        responses.add(new AssessmentItemResponse("student1", "Q4", 1));
        responses.add(new AssessmentItemResponse("student1", "Q5", 1));
        responses.add(new AssessmentItemResponse("student1", "Q6", 1));


        responses.add(new AssessmentItemResponse("student2", "Q1", 1));
        responses.add(new AssessmentItemResponse("student2", "Q2", 1));
        responses.add(new AssessmentItemResponse("student2", "Q3", 1));
        responses.add(new AssessmentItemResponse("student2", "Q4", 0));
        responses.add(new AssessmentItemResponse("student2", "Q5", 0));
        responses.add(new AssessmentItemResponse("student2", "Q6", 0));


        responses.add(new AssessmentItemResponse("student3", "Q1", 1));
        responses.add(new AssessmentItemResponse("student3", "Q2", 1));
        responses.add(new AssessmentItemResponse("student3", "Q3", 0));
        responses.add(new AssessmentItemResponse("student3", "Q4", 0));

        return responses;
    }

    public static Map<String, ColumnItem> makeSimpleLearningObjectMap(){
        Map<String, ColumnItem> los = new HashMap<>();

        los.put("Q1", new ColumnItem("Q1"));
        los.put("Q2", new ColumnItem("Q2"));
        los.put("Q3", new ColumnItem("Q3"));
        los.put("Q4", new ColumnItem("Q4"));
        los.put("Q5", new ColumnItem("Q5"));
        los.put("Q6", new ColumnItem("Q6"));

        for (AssessmentItemResponse resp : makeSimpleResponses()){
            los.get(resp.getLearningObjectId()).addResponse(resp);
        }
        return los;
    }
}

