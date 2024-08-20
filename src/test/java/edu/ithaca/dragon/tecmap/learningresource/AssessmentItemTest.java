package edu.ithaca.dragon.tecmap.learningresource;

import edu.ithaca.dragon.tecmap.Settings;
import edu.ithaca.dragon.tecmap.TecmapAPI;
import edu.ithaca.dragon.tecmap.conceptgraph.ConceptGraph;
import edu.ithaca.dragon.tecmap.conceptgraph.ConceptNode;
import edu.ithaca.dragon.tecmap.data.TecmapDatastore;
import edu.ithaca.dragon.tecmap.data.TecmapFileDatastore;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.*;

public class AssessmentItemTest {
    @Test
    public void lorEqualsTest(){
        AssessmentItemResponse lor0 = new AssessmentItemResponse("student1", "_PracticeProblem1", "Q1", 1);
        AssessmentItemResponse lor1 = new AssessmentItemResponse("student1", "_PracticeProblem1", "Q1", 1);
        AssessmentItemResponse lor2 = new AssessmentItemResponse("student1", "_PracticeProblem2", "Q2", 1);
        AssessmentItemResponse lor3 = new AssessmentItemResponse("student2", "_PracticeProblem2", "Q2", 1);
        AssessmentItemResponse lor4 = new AssessmentItemResponse("student3", "_PracticeProblem2", "Q2", 0);

        assertEquals(true, lor0.equals(lor1));
        assertEquals(false, lor0.equals(lor2));
        assertEquals(false, lor0.equals(lor3));
        assertEquals(false, lor0.equals(lor4));
        assertEquals(false, lor3.equals(lor4));
    }

    @Test
    public void loEqualsTest(){
        AssessmentItem lo1 = new AssessmentItem("_PracticeProblem1", "Q1");
        lo1.addResponse(new AssessmentItemResponse("student1", "_PracticeProblem1", "Q1", 1));
        lo1.addResponse(new AssessmentItemResponse("student1", "_PracticeProblem1", "Q1", 1));

        AssessmentItem lo2 = new AssessmentItem("_PracticeProblem1", "Q1");
        lo2.addResponse(new AssessmentItemResponse("student1", "_PracticeProblem1", "Q1", 1));
        lo2.addResponse(new AssessmentItemResponse("student1", "_PracticeProblem1", "Q1", 1));

        AssessmentItem lo3 = new AssessmentItem("_PracticeProblem2", "Q2");
        lo3.addResponse(new AssessmentItemResponse("student1", "_PracticeProblem2", "Q2", 1));
        lo3.addResponse(new AssessmentItemResponse("student1", "_PracticeProblem2", "Q2", 1));

        assertEquals(true, lo1.equals(lo2));
        assertEquals(false, lo1.equals(lo3));
    }

    @Test
    public void deepCopyLearningObjectMapTest(){
        Map<String, AssessmentItem> toCopy = ExampleLearningObjectResponseFactory.makeSimpleLearningObjectMap();
        Map<String, AssessmentItem> newMap = AssessmentItem.deepCopyLearningObjectMap(toCopy);

        for (AssessmentItem orig : toCopy.values()){
            AssessmentItem copy = newMap.get(orig.getText());
            assertEquals(orig, copy);
            assertNotSame(orig, copy);
        }

        //probably redundant with above, but just being safe
        assertNotSame(toCopy, newMap);
        assertEquals(toCopy, newMap);
    }

    @Test
    public void buildListFromAssessmentItemResponses() {
        List<AssessmentItemResponse> responseList = new ArrayList<>();
        responseList.add(new AssessmentItemResponse("s01", "_PracticeProblem1", "AI1", 1));
        responseList.add(new AssessmentItemResponse("s02", "_PracticeProblem1", "AI1", 1));
        responseList.add(new AssessmentItemResponse("s01", "_PracticeProblem2", "AI2", 2));
        responseList.add(new AssessmentItemResponse("s02", "_PracticeProblem2", "AI2", 2));
        Map<String, Double> maxKnowledgeEstimatesForAssessments = new HashMap<>();
        maxKnowledgeEstimatesForAssessments.put("AI1", 1.0);
        maxKnowledgeEstimatesForAssessments.put("AI2", 2.0);

        List<AssessmentItem> columnItems = AssessmentItem.buildListFromAssessmentItemResponses(responseList, maxKnowledgeEstimatesForAssessments);
        assertEquals(2, columnItems.size());
        List<String> expectedcolumnItems = Arrays.asList("AI1", "AI2");
        List<String> conceptIds3 = columnItems.stream().map(AssessmentItem::getText).toList();
        assertThat(conceptIds3, containsInAnyOrder(expectedcolumnItems.toArray()));
    }

    @Test
    public void getAssessmentCopyWithoutResponsesTest() {
        try {
            TecmapDatastore tecmapDatastore = TecmapFileDatastore.buildFromJsonFile(Settings.DEFAULT_TEST_DATASTORE_PATH);
            TecmapAPI analysisExample = tecmapDatastore.retrieveTecmapForId("DocExample");
            ConceptGraph acg = analysisExample.getAverageConceptGraph();
            Map<String, AssessmentItem> assessmentItemMap = acg.getAssessmentItemMap();
            List<AssessmentItem> assessmentItems = new ArrayList<>(assessmentItemMap.values());
            List<AssessmentItem> copyWithoutResponses = AssessmentItem.getAssessmentCopyWithoutResponses(assessmentItems);

            assertEquals(assessmentItems.size(), copyWithoutResponses.size());
            //The original should have responses
            for(AssessmentItem assessment : assessmentItems){
                if(assessment.getResponses().size() == 0){
                    fail();
                }
            }
            //The copy should not have responses
            for(AssessmentItem blankAssessment : copyWithoutResponses){
                if(blankAssessment.getResponses().size() != 0){
                    fail();
                }
            }
        }catch (Exception e){
            fail();
            e.printStackTrace();
        }
    }

    @Test
    public void getItemResponsesFromAssessmentListTest(){
        try{
            TecmapDatastore tecmapDatastore = TecmapFileDatastore.buildFromJsonFile(Settings.DEFAULT_TEST_DATASTORE_PATH);
            TecmapAPI analysisExample = tecmapDatastore.retrieveTecmapForId("DocExample");
            ConceptGraph acg = analysisExample.getAverageConceptGraph();
            Map<String, AssessmentItem> assessmentItemMap = acg.getAssessmentItemMap();
            List<AssessmentItem> assessmentItems = new ArrayList<>(assessmentItemMap.values());

            List<AssessmentItemResponse> responses = AssessmentItem.getItemResponsesFromAssessmentList(assessmentItems);
            assertEquals(9000, responses.size());


        }catch (Exception e){
            fail();
            e.printStackTrace();
        }
    }
}
