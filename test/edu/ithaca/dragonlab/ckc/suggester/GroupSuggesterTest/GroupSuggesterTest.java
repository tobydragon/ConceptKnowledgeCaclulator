package edu.ithaca.dragonlab.ckc.suggester.GroupSuggesterTest;

import edu.ithaca.dragonlab.ckc.ConceptKnowledgeCalculator;
import edu.ithaca.dragonlab.ckc.ConceptKnowledgeCalculatorAPI;
import edu.ithaca.dragonlab.ckc.conceptgraph.CohortConceptGraphs;
import edu.ithaca.dragonlab.ckc.suggester.GroupSuggester.GroupSuggester;
import edu.ithaca.dragonlab.ckc.suggester.GroupSuggester.randomGroupSuggestion;
import edu.ithaca.dragonlab.ckc.suggester.GroupSuggester.suggestionGroupSuggestion;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

/**
 * Created by mkimmitchell on 7/31/17.
 */
public class GroupSuggesterTest {


    @Test
    public void randomGroupTestOddStudents() {
        ConceptKnowledgeCalculatorAPI ckc = null;

        try {
            ckc = new ConceptKnowledgeCalculator("test/testresources/ManuallyCreated/researchConceptGraph.json", "test/testresources/ManuallyCreated/researchResource2.json", "test/testresources/ManuallyCreated/researchAssessment2.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }

        CohortConceptGraphs graphs = ckc.getCohortConceptGraphs();
        Assert.assertNotEquals(graphs, null);


        GroupSuggester obj = new randomGroupSuggestion();

        //groups of two
        List<List<String>> groupings = obj.suggestGroup(graphs, 2);

        Assert.assertEquals(groupings.size(), 2);
        Assert.assertEquals(groupings.get(0).size(),2);
        Assert.assertEquals(groupings.get(1).size(), 1);
        Assert.assertNotEquals(groupings.get(0).get(0), groupings.get(0).get(1), groupings.get(1).get(0));

        //groups of three
        List<List<String>> groupings2 = obj.suggestGroup(graphs, 3);

        Assert.assertEquals(groupings2.size(), 1);
        Assert.assertEquals(groupings2.get(0).size(),3);
        Assert.assertNotEquals(groupings2.get(0).get(0), groupings2.get(0).get(1), groupings2.get(0).get(2));


    }


    @Test
    public void randomGroupTestLessStudents() {
        ConceptKnowledgeCalculatorAPI ckc = null;

        try {
            ckc = new ConceptKnowledgeCalculator("test/testresources/ManuallyCreated/simpleConceptGraph.json", "test/testresources/ManuallyCreated/simpleResource.json", "test/testresources/ManuallyCreated/simpleAssessment.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }

        CohortConceptGraphs graphs = ckc.getCohortConceptGraphs();
        Assert.assertNotEquals(graphs, null);


        GroupSuggester obj = new randomGroupSuggestion();

        //groups of two
        List<List<String>> groupings = obj.suggestGroup(graphs, 2);

        Assert.assertEquals(groupings.size(), 1);
        Assert.assertEquals(groupings.get(0).size(),1);


//        //groups of three
        List<List<String>> groupings2 = obj.suggestGroup(graphs, 3);

        Assert.assertEquals(groupings2.size(), 1);
        Assert.assertEquals(groupings2.get(0).size(),1);


    }



    @Test
    public void randomGroupTestEvenStudents() {
        ConceptKnowledgeCalculatorAPI ckc = null;

        try {
            ckc = new ConceptKnowledgeCalculator("test/testresources/ManuallyCreated/researchConceptGraph.json", "test/testresources/ManuallyCreated/researchResource1.json", "test/testresources/ManuallyCreated/researchAssessment1.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }

        CohortConceptGraphs graphs = ckc.getCohortConceptGraphs();
        Assert.assertNotEquals(graphs, null);


        GroupSuggester obj = new randomGroupSuggestion();

        //groups of two
        List<List<String>> groupings = obj.suggestGroup(graphs, 2);

        Assert.assertEquals(groupings.size(), 3);
        Assert.assertEquals(groupings.get(0).size(),2);
        Assert.assertEquals(groupings.get(1).size(), 2);
        Assert.assertEquals(groupings.get(2).size(), 2);

        List<String> test = new ArrayList<>();
        test.add(groupings.get(0).get(0));
        test.add( groupings.get(0).get(1));
        test.add(groupings.get(1).get(0));
        test.add( groupings.get(1).get(1));
        test.add(groupings.get(2).get(0));
        test.add(groupings.get(2).get(1));

        Assert.assertEquals(test.size(),6);
        Assert.assertEquals(test.contains("s1"), true);
        Assert.assertEquals(test.contains("s2"), true);
        Assert.assertEquals(test.contains("s3"), true);
        Assert.assertEquals(test.contains("s4"), true);
        Assert.assertEquals(test.contains("s5"), true);
        Assert.assertEquals(test.contains("s6"), true);

        //groups of three
        List<List<String>> groupings2 = obj.suggestGroup(graphs, 3);

        Assert.assertEquals(groupings2.size(), 2);
        Assert.assertEquals(groupings2.get(0).size(),3);
        Assert.assertEquals(groupings2.get(1).size(),3);
        Assert.assertNotEquals(groupings2.get(0).get(0), groupings2.get(0).get(1), groupings2.get(0).get(2));
        Assert.assertNotEquals(groupings2.get(1).get(0), groupings2.get(1).get(1), groupings2.get(1).get(2));


//        for(List<String> lists : groupings2){
//            System.out.println(lists);
//        }

    }


    @Test
    public void randomGroupTestRealData() {
        ConceptKnowledgeCalculatorAPI ckc = null;


        //            new ConsoleUI

        try {
            ckc = new ConceptKnowledgeCalculator("resources/comp220/comp220Graph.json","resources/comp220/comp220Resources.json","localresources/comp220/comp220ExampleDataPortion.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }

        CohortConceptGraphs graphs = ckc.getCohortConceptGraphs();
        Assert.assertNotEquals(graphs, null);


        GroupSuggester obj = new randomGroupSuggestion();

        //groups of two
        List<List<String>> groupings = obj.suggestGroup(graphs, 3);


        Assert.assertEquals(groupings.size(), 13);


    }



    @Test
    public void suggestionGroupTestOddStudents() {
        ConceptKnowledgeCalculatorAPI ckc = null;

        try {
            ckc = new ConceptKnowledgeCalculator("test/testresources/ManuallyCreated/researchConceptGraph.json", "test/testresources/ManuallyCreated/researchResource2.json", "test/testresources/ManuallyCreated/researchAssessment2.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }

        CohortConceptGraphs graphs = ckc.getCohortConceptGraphs();
        Assert.assertNotEquals(graphs, null);


        GroupSuggester obj = new suggestionGroupSuggestion();

        //groups of two
        List<List<String>> groupings = obj.suggestGroup(graphs, 2);

    }

}
