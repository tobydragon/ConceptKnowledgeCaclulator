package edu.ithaca.dragonlab.ckc.suggester;

import edu.ithaca.dragonlab.ckc.conceptgraph.ConceptGraph;
import edu.ithaca.dragonlab.ckc.conceptgraph.ExampleConceptGraphFactory;
import edu.ithaca.dragonlab.ckc.conceptgraph.ExampleConceptGraphRecordFactory;
import edu.ithaca.dragonlab.ckc.io.LearningObjectLinkRecord;
import edu.ithaca.dragonlab.ckc.learningobject.ExampleLearningObjectLinkRecordFactory;
import edu.ithaca.dragonlab.ckc.learningobject.ExampleLearningObjectResponseFactory;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by home on 5/20/17.
 */
public class LearningObjectSuggesterTest {

    @Test
    public void SuggestedConceptNodeMapSimpleTest() {
        ConceptGraph orig = ExampleConceptGraphFactory.simpleTestGraphTest();

        //what it is
        HashMap<String, List<LearningObjectSuggestion>> objectSuggestionMap = LearningObjectSuggester.buildSuggestionMap(orig,1);

        //what it should be
        List<LearningObjectSuggestion> testList3 = new ArrayList<>();
        Assert.assertEquals(1, objectSuggestionMap.size());
        Assert.assertEquals(testList3, objectSuggestionMap.get("C"));

    }


    @Test
    public void SuggestedConceptNodeMapWillOneStudentTest() {
        ConceptGraph orig= ExampleConceptGraphFactory.willExampleConceptGraphTestOneStudent();
        //what it is
        HashMap<String, List<LearningObjectSuggestion>> objectSuggestionMap = LearningObjectSuggester.buildSuggestionMap(orig,1);

        Assert.assertEquals(2, objectSuggestionMap.size());
        Assert.assertEquals(objectSuggestionMap.get("If Statement").get(1).getId(), "Q3");
        Assert.assertEquals(objectSuggestionMap.get("If Statement").get(0).getId(), "Q10");
        Assert.assertEquals(objectSuggestionMap.get("While Loop").get(0).getId(), "Q10");

    }

    @Test
    public void suggestedOrderBuildLearningObjectListTest(){
        List<LearningObjectLinkRecord> myList = ExampleLearningObjectLinkRecordFactory.makeSimpleLOLRecords();
        myList.add(new LearningObjectLinkRecord("Q10", Arrays.asList("A"),1));
        ConceptGraph orig = new ConceptGraph(ExampleConceptGraphRecordFactory.makeSimple(),
                myList, ExampleLearningObjectResponseFactory.makeSimpleResponses());

//        //from A nodes
        HashMap<String, Integer> testCompareA = new HashMap<String, Integer>();
        testCompareA.put("Q3",2);
        testCompareA.put("Q4",2);
        testCompareA.put("Q5",2);
        testCompareA.put("Q6",2);
        testCompareA.put("Q1",1);
        testCompareA.put("Q2",1);
        testCompareA.put("Q10",1);

        HashMap<String, Integer> learningSummaryFromA = orig.buildLearningObjectSummaryList("A");
        //makes sure that buildLearningObjectSummaryList works
        Assert.assertEquals(testCompareA,learningSummaryFromA);

        //build the sugggested learning object list
        List<LearningObjectSuggestion> suggestedList = LearningObjectSuggester.buildLearningObjectSuggestionList(learningSummaryFromA, orig.getLearningObjectMap());


        //this is ordered based on "level"
        List<LearningObjectSuggestion> suggestListTest = new ArrayList<>();
        suggestListTest.add(new LearningObjectSuggestion("Q3", 2, LearningObjectSuggestion.Level.RIGHT) );
        suggestListTest.add(new LearningObjectSuggestion("Q1", 1, LearningObjectSuggestion.Level.RIGHT) );
        suggestListTest.add(new LearningObjectSuggestion("Q2", 1, LearningObjectSuggestion.Level.RIGHT) );
        suggestListTest.add(new LearningObjectSuggestion("Q4", 2, LearningObjectSuggestion.Level.WRONG) );
        suggestListTest.add(new LearningObjectSuggestion("Q5", 2, LearningObjectSuggestion.Level.WRONG) );
        suggestListTest.add(new LearningObjectSuggestion("Q6", 2, LearningObjectSuggestion.Level.WRONG) );
        suggestListTest.add(new LearningObjectSuggestion("Q10", 1, LearningObjectSuggestion.Level.INCOMPLETE) );


        //orders the list based off of "level"
        LearningObjectSuggester.sortSuggestions(suggestedList);

//        //who should call orderedList

        for (int i =0; i<suggestedList.size(); i++){

            Assert.assertEquals(suggestedList.get(i).getId(),suggestListTest.get(i).getId());
            Assert.assertEquals(suggestedList.get(i).getPathNum(),suggestListTest.get(i).getPathNum());
            Assert.assertEquals(suggestedList.get(i).getLevel(),suggestListTest.get(i).getLevel());

        }

    }
}
