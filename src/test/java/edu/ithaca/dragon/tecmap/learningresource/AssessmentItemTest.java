package edu.ithaca.dragon.tecmap.learningresource;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AssessmentItemTest {
    @Test
    public void lorEqualsTest(){
        AssessmentItemResponse lor0 = new AssessmentItemResponse("student1", "Q1", 1);
        AssessmentItemResponse lor1 = new AssessmentItemResponse("student1", "Q1", 1);
        AssessmentItemResponse lor2 = new AssessmentItemResponse("student1", "Q2", 1);
        AssessmentItemResponse lor3 = new AssessmentItemResponse("student2", "Q2", 1);
        AssessmentItemResponse lor4 = new AssessmentItemResponse("student3", "Q2", 0);

        Assert.assertEquals(true, lor0.equals(lor1));
        Assert.assertEquals(false, lor0.equals(lor2));
        Assert.assertEquals(false, lor0.equals(lor3));
        Assert.assertEquals(false, lor0.equals(lor4));
        Assert.assertEquals(false, lor3.equals(lor4));
    }

    @Test
    public void loEqualsTest(){
        AssessmentItem lo1 = new AssessmentItem("Q1");
        lo1.addResponse(new AssessmentItemResponse("student1", "Q1", 1));
        lo1.addResponse(new AssessmentItemResponse("student1", "Q1", 1));

        AssessmentItem lo2 = new AssessmentItem("Q1");
        lo2.addResponse(new AssessmentItemResponse("student1", "Q1", 1));
        lo2.addResponse(new AssessmentItemResponse("student1", "Q1", 1));

        AssessmentItem lo3 = new AssessmentItem("Q2");
        lo3.addResponse(new AssessmentItemResponse("student1", "Q2", 1));
        lo3.addResponse(new AssessmentItemResponse("student1", "Q2", 1));

        Assert.assertEquals(true, lo1.equals(lo2));
        Assert.assertEquals(false, lo1.equals(lo3));
    }

    @Test
    public void deepCopyLearningObjectMapTest(){
        Map<String, AssessmentItem> toCopy = ExampleLearningObjectResponseFactory.makeSimpleLearningObjectMap();
        Map<String, AssessmentItem> newMap = AssessmentItem.deepCopyLearningObjectMap(toCopy);

        for (AssessmentItem orig : toCopy.values()){
            AssessmentItem copy = newMap.get(orig.getId());
            Assert.assertEquals(orig, copy);
            Assert.assertFalse(orig == copy);
        }

        //probably redundant with above, but just being safe
        Assert.assertFalse(toCopy == newMap);
        Assert.assertEquals(toCopy, newMap);
    }

    @Test
    public void buildListFromAssessmentItemResponses() {
        List<AssessmentItemResponse> responseList = new ArrayList<>();
        responseList.add(new AssessmentItemResponse("s01", "AI1", 1));
        responseList.add(new AssessmentItemResponse("s02", "AI1", 1));
        responseList.add(new AssessmentItemResponse("s01", "AI2", 2));
        responseList.add(new AssessmentItemResponse("s02", "AI2", 2));
        Map<String, Double> maxKnowledgeEstimatesForAssessments = new HashMap<>();
        maxKnowledgeEstimatesForAssessments.put("AI1", 1.0);
        maxKnowledgeEstimatesForAssessments.put("AI2", 2.0);

        List<AssessmentItem> assessmentItems = AssessmentItem.buildListFromAssessmentItemResponses(responseList, maxKnowledgeEstimatesForAssessments);
        assertEquals(2, assessmentItems.size());
        assertEquals("AI1", assessmentItems.get(0).getId());
        assertEquals("AI2", assessmentItems.get(1).getId());
    }
}
