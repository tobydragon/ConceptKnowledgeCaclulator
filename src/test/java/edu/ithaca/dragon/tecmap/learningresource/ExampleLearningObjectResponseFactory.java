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

        responses.add(new AssessmentItemResponse("student1", "_PracticeProblem1","Q1", 1));
        responses.add(new AssessmentItemResponse("student1", "_PracticeProblem2","Q2", 1));
        responses.add(new AssessmentItemResponse("student1", "_PracticeProblem3","Q3", 1));
        responses.add(new AssessmentItemResponse("student1", "_PracticeProblem4","Q4", 1));
        responses.add(new AssessmentItemResponse("student1", "_PracticeProblem5","Q5", 1));
        responses.add(new AssessmentItemResponse("student1", "_PracticeProblem6","Q6", 1));


        responses.add(new AssessmentItemResponse("student2", "_PracticeProblem1","Q1", 1));
        responses.add(new AssessmentItemResponse("student2", "_PracticeProblem2","Q2", 1));
        responses.add(new AssessmentItemResponse("student2", "_PracticeProblem3","Q3", 1));
        responses.add(new AssessmentItemResponse("student2", "_PracticeProblem4","Q4", 0));
        responses.add(new AssessmentItemResponse("student2", "_PracticeProblem5","Q5", 0));
        responses.add(new AssessmentItemResponse("student2", "_PracticeProblem6","Q6", 0));


        responses.add(new AssessmentItemResponse("student3", "_PracticeProblem1","Q1", 1));
        responses.add(new AssessmentItemResponse("student3", "_PracticeProblem2","Q2", 1));
        responses.add(new AssessmentItemResponse("student3", "_PracticeProblem3","Q3", 0));
        responses.add(new AssessmentItemResponse("student3", "_PracticeProblem4","Q4", 0));

        return responses;
    }

    public static Map<String, AssessmentItem> makeSimpleLearningObjectMap(){
        Map<String, AssessmentItem> los = new HashMap<>();

        los.put("Q1", new AssessmentItem("_PracticeProblem1","Q1"));
        los.put("Q2", new AssessmentItem("_PracticeProblem2","Q2"));
        los.put("Q3", new AssessmentItem("_PracticeProblem3","Q3"));
        los.put("Q4", new AssessmentItem("_PracticeProblem4","Q4"));
        los.put("Q5", new AssessmentItem("_PracticeProblem5","Q5"));
        los.put("Q6", new AssessmentItem("_PracticeProblem6","Q6"));

        for (AssessmentItemResponse resp : makeSimpleResponses()){
            los.get(resp.getAssessmentItemText()).addResponse(resp);
        }
        return los;
    }
}

