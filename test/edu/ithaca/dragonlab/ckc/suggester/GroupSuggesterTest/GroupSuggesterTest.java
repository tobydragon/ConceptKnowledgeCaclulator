package edu.ithaca.dragonlab.ckc.suggester.GroupSuggesterTest;

import edu.ithaca.dragonlab.ckc.ConceptKnowledgeCalculator;
import edu.ithaca.dragonlab.ckc.ConceptKnowledgeCalculatorAPI;
import edu.ithaca.dragonlab.ckc.conceptgraph.CohortConceptGraphs;
import edu.ithaca.dragonlab.ckc.conceptgraph.ConceptGraph;
import edu.ithaca.dragonlab.ckc.suggester.GroupSuggester.*;
//import edu.ithaca.dragonlab.ckc.suggester.GroupSuggester.BySize;
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
        GroupSuggester sug = new GroupSuggester();

        List<Group> groupings1 = sug.getGroupList(graphs);

        //groups of 2
        List<Group> groupings = sug.grouping(groupings1, 2, 2, null);
        Assert.assertEquals(groupings.size(), 2);
        Assert.assertEquals(groupings.get(0).getSize(), 2);
        Assert.assertEquals(groupings.get(1).getSize(), 3);

        //groups of three
        List<Group> groupings2 = sug.grouping(groupings1, 3, 2 , null);
        Assert.assertEquals(groupings2.size(), 2);
        Assert.assertEquals(groupings2.get(0).getSize(), 3);
        Assert.assertEquals(groupings2.get(1).getSize(), 2);


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
        GroupSuggester sug = new GroupSuggester();

        List<Group> groupings1 = sug.getGroupList(graphs);
        //groups of 2
        List<Group> groupings = sug.grouping(groupings1, 2, 2, null );
        Assert.assertEquals(groupings.size(), 1);
        Assert.assertEquals(groupings.get(0).getSize(),1);

        //groups of three
        List<Group> groupings2 = sug.grouping(groupings1, 3, 2 , null);
        Assert.assertEquals(groupings2.size(), 1);
        Assert.assertEquals(groupings2.get(0).getSize(),1);
    }

    @Test
    public void randomTest() {
        ConceptKnowledgeCalculatorAPI ckc = null;

        try {
            ckc = new ConceptKnowledgeCalculator("test/testresources/ManuallyCreated/researchConceptGraph.json", "test/testresources/ManuallyCreated/researchResource2.json", "test/testresources/ManuallyCreated/researchAssessment2.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }

        GroupSuggester sug = new GroupSuggester();

        //testing with more than one list of groups
        List<Group> actualGroupings = new ArrayList<>();

        Group group = new Group();
        group.addMember("mia", new ConceptGraph());
        group.addMember("don", new ConceptGraph());
        group.addMember("bob", new ConceptGraph());
        actualGroupings.add(group);

        Group group2 = new Group();
        group2.addMember("kayli",new ConceptGraph());
        group2.addMember("dan", new ConceptGraph());
        actualGroupings.add(group2);

        List<Group> groupings2 = sug.grouping(actualGroupings, 2, 2, null );

        Assert.assertEquals(groupings2.size(), 2);
        Assert.assertEquals(groupings2.get(0).getSize(),2);
        Assert.assertEquals(groupings2.get(1).getSize(),3);

//        for(Group name: groupings2){
//            System.out.println(name);
//
//        }
    }




//    @Test
//    public void conceptTest(){
//        ConceptKnowledgeCalculatorAPI ckc = null;
//
//        try {
//            ckc = new ConceptKnowledgeCalculator("test/testresources/ManuallyCreated/researchConceptGraph.json", "test/testresources/ManuallyCreated/researchResource2.json", "test/testresources/ManuallyCreated/researchAssessment2.csv");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        CohortConceptGraphs graphs = ckc.getCohortConceptGraphs();
//        Assert.assertNotEquals(graphs, null);
//
//
//        GroupSuggester obj = new Concept(2);
//
//
//        Map<String, ConceptGraph> userMap = obj.getUserMap(graphs);
//
//        List<Map<String, ConceptGraph>> groupings1 = new ArrayList<>();
//        groupings1.add(userMap);
//
//        //groups of two
//        List<Map<String, ConceptGraph>> groupings = obj.suggestGroup(groupings1);
//
//
//
//
//    }


    @Test
    public void bucketTest() {
        ConceptKnowledgeCalculatorAPI ckc = null;

        try {
            ckc = new ConceptKnowledgeCalculator("test/testresources/ManuallyCreated/researchConceptGraph.json", "test/testresources/ManuallyCreated/researchResource2.json", "test/testresources/ManuallyCreated/researchAssessment2.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }

        CohortConceptGraphs graphs = ckc.getCohortConceptGraphs();
        Assert.assertNotEquals(graphs, null);

        List<List<Integer>> ranges = new ArrayList<>();
        List<Integer> temp = new ArrayList<>();
        temp.add(0);
        temp.add(50);
        List<Integer> temp2 = new ArrayList<>();
        temp2.add(51);
        temp2.add(80);
        List<Integer> temp3 = new ArrayList<>();
        temp3.add(81);
        temp3.add(100);
        ranges.add(temp);
        ranges.add(temp2);
        ranges.add(temp3);


        try {
            GroupSuggester sug = new GroupSuggester();

            List<Group> groupings1 = sug.getGroupList(graphs);
            //groups of 2
            List<Group> groupings = sug.grouping(groupings1, 2, 0 , ranges);

            Assert.assertEquals(groupings.size(), 3);
            Assert.assertEquals(groupings.get(0).getSize(),0 );

            Assert.assertEquals(groupings.get(1).getSize(), 2);
            Assert.assertEquals(groupings.get(1).contains("s2"), true);
            Assert.assertEquals(groupings.get(1).contains("s3"),true);

            Assert.assertEquals(groupings.get(2).getSize(), 3);
            Assert.assertEquals(groupings.get(2).contains("s4"), true);
            Assert.assertEquals(groupings.get(2).contains("s5"), true);
            Assert.assertEquals(groupings.get(2).contains("s1"),true);


//            //testing with more than one list of groups
            List<Group> actualGroupings = new ArrayList<>();

            Group group = new Group();

            group.addMember("mia", groupings.get(1).getGraph("s3"));
            group.addMember("don", groupings.get(2).getGraph("s4"));
            group.addMember("bob", groupings.get(1).getGraph("s2"));
            group.addMember("alena", groupings.get(1).getGraph("s3"));
            actualGroupings.add(group);

            Group group2 = new Group();
            group2.addMember("kayli", groupings.get(2).getGraph("s5"));
            group2.addMember("dan", groupings.get(1).getGraph("s2"));
            actualGroupings.add(group2);

            List<Group> groupings2 = sug.grouping(actualGroupings, 2, 0 , ranges);

            Assert.assertEquals(groupings2.size(), 6);
            Assert.assertEquals(groupings2.get(0).getSize(),0 );

            Assert.assertEquals(groupings2.get(1).getSize(),3 );
            Assert.assertEquals(groupings2.get(1).contains("mia"), true);
            Assert.assertEquals(groupings2.get(1).contains("bob"), true);
            Assert.assertEquals(groupings2.get(1).contains("alena"), true);

            Assert.assertEquals(groupings2.get(2).getSize(),1);
            Assert.assertEquals(groupings2.get(2).contains("don"), true);

            Assert.assertEquals(groupings2.get(3).getSize(),0);

            Assert.assertEquals(groupings2.get(4).getSize(),1);
            Assert.assertEquals(groupings2.get(4).contains("dan"), true);

            Assert.assertEquals(groupings2.get(5).getSize(),1);
            Assert.assertEquals(groupings2.get(5).contains("kayli"), true);
        } catch (Exception e) {
            e.printStackTrace();
        }





    }
//
//
//    @Test
//    public void suggestionGroupTestOddStudents() {
//        ConceptKnowledgeCalculatorAPI ckc = null;
//
//        try {
//
//            ckc = new ConceptKnowledgeCalculator("test/testresources/ManuallyCreated/researchConceptGraph.json", "test/testresources/ManuallyCreated/researchResource2.json", "test/testresources/ManuallyCreated/researchAssessment2.csv");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        CohortConceptGraphs graphs = ckc.getCohortConceptGraphs();
//        Assert.assertNotEquals(graphs, null);
//
//
//        GroupSuggester obj = new ResourceGroupSuggester();
//
//        //groups of two
//        List<List<String>> groupings = obj.suggestGroup(graphs, 2);
//
//        Assert.assertEquals(groupings.size(),3);
//
//        Assert.assertEquals(groupings.get(0).size(), 3);
//        Assert.assertEquals(groupings.get(1).size(), 3);
//
//        Assert.assertEquals(groupings.get(0).get(0),"s4" );
//        Assert.assertEquals(groupings.get(0).get(1),"s5" );
//        Assert.assertEquals(groupings.get(0).get(2),"something challenging" );
//
//        Assert.assertEquals(groupings.get(1).get(0),"s3" );
//        Assert.assertEquals(groupings.get(1).get(1),"s2" );
//        Assert.assertEquals(groupings.get(1).get(2),"What are values are accessed by?" );
//
//        Assert.assertEquals(groupings.get(2).get(0),"s1" );
//        Assert.assertEquals(groupings.get(2).get(1),"No other students" );
//
//
//        List<List<String>> groupings2 = obj.suggestGroup(graphs, 3);
//
//        Assert.assertEquals(groupings2.size(),2);
//        Assert.assertEquals(groupings2.get(0).size(), 4);
//        Assert.assertEquals(groupings2.get(0).get(0), "s4");
//        Assert.assertEquals(groupings2.get(0).get(1), "s5");
//        Assert.assertEquals(groupings2.get(0).get(2), "s1");
//        Assert.assertEquals(groupings2.get(0).get(3), "something challenging");
//
//        Assert.assertEquals(groupings2.get(1).size(), 3);
//        Assert.assertEquals(groupings2.get(1).get(0), "s3");
//        Assert.assertEquals(groupings2.get(1).get(1), "s2");
//        Assert.assertEquals(groupings2.get(1).get(2), "No other students");
//
//    }
//
//    @Test
//    public void suggestionGroupTestEvenStudents() {
//        ConceptKnowledgeCalculatorAPI ckc = null;
//
//        try {
//            ckc = new ConceptKnowledgeCalculator("test/testresources/ManuallyCreated/researchConceptGraph.json", "test/testresources/ManuallyCreated/researchResource1.json", "test/testresources/ManuallyCreated/researchAssessment1.csv");
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        CohortConceptGraphs graphs = ckc.getCohortConceptGraphs();
//        Assert.assertNotEquals(graphs, null);
//
//
//        GroupSuggester obj = new ResourceGroupSuggester();
//
//        //groups of two
//        List<List<String>> groupings = obj.suggestGroup(graphs, 2);
//
//        Assert.assertEquals(groupings.size(),3);
//        Assert.assertEquals(groupings.get(0).get(0), "s3");
//        Assert.assertEquals(groupings.get(0).get(1), "s5");
//        Assert.assertEquals(groupings.get(1).get(0), "s1");
//        Assert.assertEquals(groupings.get(1).get(1), "s2");
//        Assert.assertEquals(groupings.get(2).get(0), "s6");
//        Assert.assertEquals(groupings.get(2).get(1), "s4");
//
//        List<List<String>> groupings3 = obj.suggestGroup(graphs, 3);
//        Assert.assertEquals(groupings3.size(),3);
//        Assert.assertEquals(groupings3.get(0).get(0), "s3");
//        Assert.assertEquals(groupings3.get(0).get(1), "s5");
//        Assert.assertEquals(groupings3.get(1).get(0), "s1");
//        Assert.assertEquals(groupings3.get(1).get(1), "s2");
//        Assert.assertEquals(groupings3.get(2).get(0), "s4");
//        Assert.assertEquals(groupings3.get(2).get(1), "s6");
//
//
//        ResourceNCubeGroupSuggester obj2 = new ResourceNCubeGroupSuggester();
//        List<List<String>> groupings2 = obj2.suggestGroup(graphs, 3);
//
//        Assert.assertEquals(groupings2.size(),2);
//
//        Assert.assertEquals(groupings2.get(0).size(), 4);
//        Assert.assertEquals(groupings2.get(0).get(0), "s3");
//        Assert.assertEquals(groupings2.get(0).get(1), "s5");
//        Assert.assertEquals(groupings2.get(0).get(2), "s2");
//        Assert.assertEquals(groupings2.get(0).get(3), "How are while loops and booleans related?");
//
//        Assert.assertEquals(groupings2.get(1).size(), 4);
//        Assert.assertEquals(groupings2.get(1).get(0), "s1");
//        Assert.assertEquals(groupings2.get(1).get(1), "s6");
//        Assert.assertEquals(groupings2.get(1).get(2), "s4");
//        Assert.assertEquals(groupings2.get(1).get(3), "BySize pairing");
//
//
//    }
//
//    @Test
//    public void suggestionGroupTestLessLeftOverStudents() {
//        ConceptKnowledgeCalculatorAPI ckc = null;
//
//        try {
//            ckc = new ConceptKnowledgeCalculator("test/testresources/ManuallyCreated/basicRealisticConceptGraph.json", "test/testresources/ManuallyCreated/basicRealisticResource.json", "test/testresources/ManuallyCreated/basicRealisticAssessment.csv");
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        CohortConceptGraphs graphs = ckc.getCohortConceptGraphs();
//        Assert.assertNotEquals(graphs, null);
//
//
//        GroupSuggester obj = new ResourceGroupSuggester();
//
//        //groups of two
//        List<List<String>> groupings = obj.suggestGroup(graphs, 2);
//
//        Assert.assertEquals(groupings.size(),1);
//        Assert.assertEquals(groupings.get(0).size(), 2);
//        Assert.assertEquals(groupings.get(0).get(0), "bspinache1");
//        Assert.assertEquals(groupings.get(0).get(1), "No other students");
//
//
//        List<List<String>> groupings2 = obj.suggestGroup(graphs, 3);
//        Assert.assertEquals(groupings2.size(), 1);
//        Assert.assertEquals(groupings2.get(0).size(), 3);
//        Assert.assertEquals(groupings2.get(0).get(0), "bspinache1");
//        Assert.assertEquals(groupings2.get(0).get(1), "No other students");
//        Assert.assertEquals(groupings2.get(0).get(2), "No other students");
//
//
//        ResourceNCubeGroupSuggester obj2 = new ResourceNCubeGroupSuggester();
//
//        List<List<String>> groupings3 = obj2.suggestGroup(graphs, 3);
//        Assert.assertEquals(groupings3.size(), 1);
//        Assert.assertEquals(groupings3.get(0).size(), 2);
//        Assert.assertEquals(groupings3.get(0).get(0), "bspinache1");
//    }
//
//
//    @Test
//    public void graphSumTest(){
//        ConceptKnowledgeCalculatorAPI ckc = null;
//
//        try {
//            ckc = new ConceptKnowledgeCalculator("test/testresources/ManuallyCreated/researchConceptGraph.json", "test/testresources/ManuallyCreated/researchResource1.json", "test/testresources/ManuallyCreated/researchAssessment1.csv");
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        CohortConceptGraphs graphs = ckc.getCohortConceptGraphs();
//        Assert.assertNotEquals(graphs, null);
//        Bucket sug = null;
//        try {
//            sug = new Bucket(new ArrayList<>());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        ConceptGraph usergraph = graphs.getUserGraph("s5");
//
//        double sum = sug.calcSum(usergraph, "Values and Data Types");
//        Assert.assertEquals(sum, 3.3575,  DataUtil.OK_FLOAT_MARGIN);
//
//        double sum2 = sug.calcSum(usergraph, "all");
//        Assert.assertEquals(sum2, 12.87208,  DataUtil.OK_FLOAT_MARGIN);
//
//
//
//        ConceptGraph usergraph2 = graphs.getUserGraph("s4");
//        double sum3 = sug.calcSum(usergraph2, "all");
//        Assert.assertEquals(sum3, 1.0,  DataUtil.OK_FLOAT_MARGIN);
//
//
//
//    }
//
//    @Test
//    public void conceptDiffGroupSuggestiongetcalcDiffTest(){
//        ConceptKnowledgeCalculatorAPI ckc = null;
//
//        try {
//            ckc = new ConceptKnowledgeCalculator("test/testresources/ManuallyCreated/researchConceptGraph.json", "test/testresources/ManuallyCreated/researchResource1.json", "test/testresources/ManuallyCreated/researchAssessment1.csv");
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        CohortConceptGraphs graphs = ckc.getCohortConceptGraphs();
//        Assert.assertNotEquals(graphs, null);
//        ConceptDiffGroupSuggester sug = new ConceptDiffGroupSuggester();
//
//        ConceptGraph usergraph = graphs.getUserGraph("s6");
//        ConceptGraph usergraph2 = graphs.getUserGraph("s1");
//
//
//        double sum = sug.calcDiff(usergraph, usergraph2, "Values and Data Types");
//        Assert.assertEquals(sum, 0.5025,  DataUtil.OK_FLOAT_MARGIN);
//
//        double sum2 = sug.calcDiff(usergraph, usergraph2, "all");
//        Assert.assertEquals(sum2, 2.0866,  DataUtil.OK_FLOAT_MARGIN);
//    }
//
//
//    @Test
//    public void ConceptDiffGroupSuggestionEvenStudents(){
//        ConceptKnowledgeCalculatorAPI ckc = null;
//
//        try {
//            ckc = new ConceptKnowledgeCalculator("test/testresources/ManuallyCreated/researchConceptGraph.json", "test/testresources/ManuallyCreated/researchResource1.json", "test/testresources/ManuallyCreated/researchAssessment1.csv");
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        CohortConceptGraphs graphs = ckc.getCohortConceptGraphs();
//        Assert.assertNotEquals(graphs, null);
//
//        ConceptDiffGroupSuggester sug = new ConceptDiffGroupSuggester();
//
//        List<List<String>> groupings = sug.suggestGroup(graphs, 2, "all");
//
//
//        Assert.assertEquals(groupings.size(),3 );
//        Assert.assertEquals(groupings.get(0).size(), 2);
//        Assert.assertEquals(groupings.get(1).size(), 2);
//        Assert.assertEquals(groupings.get(2).size(), 2);
//
//        Assert.assertEquals(groupings.get(0).get(0), "s3");
//        Assert.assertEquals(groupings.get(0).get(1), "s5");
//
//        Assert.assertEquals(groupings.get(1).get(0), "s6");
//        Assert.assertEquals(groupings.get(1).get(1), "s2");
//
//
//        Assert.assertEquals(groupings.get(2).get(0), "s4");
//        Assert.assertEquals(groupings.get(2).get(1), "s1");
//
//
//        List<List<String>> groupings2 = sug.suggestGroup(graphs, 2, "Expressions and Statements");
//
//        Assert.assertEquals(groupings2.size(),3 );
//        Assert.assertEquals(groupings2.get(0).size(), 2);
//        Assert.assertEquals(groupings2.get(1).size(), 2);
//        Assert.assertEquals(groupings2.get(2).size(), 2);
//
//        Assert.assertEquals(groupings2.get(0).get(0), "s5");
//        Assert.assertEquals(groupings2.get(0).get(1), "s1");
//
//        Assert.assertEquals(groupings2.get(1).get(0), "s3");
//        Assert.assertEquals(groupings2.get(1).get(1), "s6");
//
//
//        Assert.assertEquals(groupings2.get(2).get(0), "s4");
//        Assert.assertEquals(groupings2.get(2).get(1), "s2");
//
//
//        List<List<String>> grouping3 = sug.suggestGroup(graphs, 3, "all");
//
//        Assert.assertEquals(grouping3.size(),2 );
//        Assert.assertEquals(grouping3.get(0).size(), 3);
//        Assert.assertEquals(grouping3.get(1).size(), 3);
//
//        Assert.assertEquals(grouping3.get(0).get(0), "s3");
//        Assert.assertEquals(grouping3.get(0).get(1), "s5");
//        Assert.assertEquals(grouping3.get(0).get(2), "s1");
//
//        Assert.assertEquals(grouping3.get(1).get(0), "s6");
//        Assert.assertEquals(grouping3.get(1).get(1), "s2");
//        Assert.assertEquals(grouping3.get(1).get(2), "s4");
//
//    }
//
//
//    @Test
//    public void ConceptDiffGroupSuggestionLessStudents() {
//        ConceptKnowledgeCalculatorAPI ckc = null;
//
//        try {
//            ckc = new ConceptKnowledgeCalculator("test/testresources/ManuallyCreated/simpleConceptGraph.json", "test/testresources/ManuallyCreated/simpleResource.json", "test/testresources/ManuallyCreated/simpleAssessment.csv");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        CohortConceptGraphs graphs = ckc.getCohortConceptGraphs();
//        Assert.assertNotEquals(graphs, null);
//
//
//        ConceptDiffGroupSuggester sug = new ConceptDiffGroupSuggester();
//
//        List<List<String>> groupings = sug.suggestGroup(graphs, 2, "all");
//        Assert.assertEquals(groupings.size(), 1);
//        Assert.assertEquals(groupings.get(0).size(), 1);
//
//
//        List<List<String>> grouping3 = sug.suggestGroup(graphs, 3, "all");
//
//        Assert.assertEquals(grouping3.size(), 1);
//        Assert.assertEquals(grouping3.get(0).size(), 1);
//
//        Assert.assertEquals(grouping3.get(0).get(0), "bspinache1");
//
//
//    }
//
//
//    @Test
//    public void ConceptDiffGroupSuggestionOddStudents(){
//        ConceptKnowledgeCalculatorAPI ckc = null;
//
//        try {
//            ckc = new ConceptKnowledgeCalculator("test/testresources/ManuallyCreated/researchConceptGraph.json", "test/testresources/ManuallyCreated/researchResource2.json", "test/testresources/ManuallyCreated/researchAssessment2.csv");
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        CohortConceptGraphs graphs = ckc.getCohortConceptGraphs();
//        Assert.assertNotEquals(graphs, null);
//
//        ConceptDiffGroupSuggester sug = new ConceptDiffGroupSuggester();
//
//        List<List<String>> groupings = sug.suggestGroup(graphs, 2, "all");
//
//
//        Assert.assertEquals(groupings.size(),3 );
//        Assert.assertEquals(groupings.get(0).size(), 2);
//        Assert.assertEquals(groupings.get(1).size(), 2);
//        Assert.assertEquals(groupings.get(2).size(), 1);
//
//        Assert.assertEquals(groupings.get(0).get(0), "s4");
//        Assert.assertEquals(groupings.get(0).get(1), "s5");
//
//        Assert.assertEquals(groupings.get(1).get(0), "s3");
//        Assert.assertEquals(groupings.get(1).get(1), "s2");
//
//
//        Assert.assertEquals(groupings.get(2).get(0), "s1");
//
//
//
//
//        List<List<String>> grouping3 = sug.suggestGroup(graphs, 3, "all");
//
//        Assert.assertEquals(grouping3.size(),2 );
//        Assert.assertEquals(grouping3.get(0).size(), 3);
//        Assert.assertEquals(grouping3.get(1).size(), 2);
//
//        Assert.assertEquals(grouping3.get(0).get(0), "s4");
//        Assert.assertEquals(grouping3.get(0).get(1), "s5");
//        Assert.assertEquals(grouping3.get(0).get(2), "s3");
//
//        Assert.assertEquals(grouping3.get(1).get(0), "s2");
//        Assert.assertEquals(grouping3.get(1).get(1), "s1");
//
//    }
//
//
//
//    @Test
//    public void groupTestRealData() {
//        ConceptKnowledgeCalculatorAPI ckc = null;
//
//        try {
//            ckc = new ConceptKnowledgeCalculator("resources/comp220/comp220Graph.json","resources/comp220/comp220Resources.json","localresources/comp220/comp220ExampleDataPortion.csv");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        CohortConceptGraphs graphs = ckc.getCohortConceptGraphs();
//        Assert.assertNotEquals(graphs, null);
//
//
////        GroupSuggester obj = new RandomGroupSuggester();
////
////        List<List<String>> groupings2 = obj.suggestGroup(graphs, 2);
////        Assert.assertEquals(groupings2.size(), 19);
////
//////
////        List<List<String>> groupings = obj.suggestGroup(graphs, 3);
////        Assert.assertEquals(groupings.size(), 13);
////
////
////
////        GroupSuggester group = new ResourceNCubeGroupSuggester();
////
////        List<List<String>> groupings3 = group.suggestGroup(graphs, 3);
////        Assert.assertEquals(groupings3.size(), 13);
////
////
////
////        List<List<String>> groupings4 = group.suggestGroup(graphs, 2);
////        Assert.assertEquals(groupings4.size(), 19);
////
////
////
////        ResourceNCubeGroupSuggester teams = new ResourceNCubeGroupSuggester();
////        List<List<String>> grouping1 = teams.suggestGroup(graphs, 3);
////        Assert.assertEquals(grouping1.size(), 13);
////
////
////        List<List<String>> grouping = teams.suggestGroup(graphs, 2);
////        Assert.assertEquals(grouping.size(), 19);
////
////
////
////        GraphSumGroupSuggester sug = new GraphSumGroupSuggester();
////
////        List<List<String>> groupings5 = sug.suggestGroup(graphs, 2, "all");
////        Assert.assertEquals(groupings5.size(), 19);
////
////
////        List<List<String>> grouping6 = sug.suggestGroup(graphs, 3, "all");
////
////        Assert.assertEquals(grouping6.size(), 13);
////
//
//        ConceptDiffGroupSuggester sug2 = new ConceptDiffGroupSuggester();
//
////        List<List<String>> groupings7 = sug2.suggestGroup(graphs, 2, "all");
////        Assert.assertEquals(groupings7.size(), 19);
//
//
//        List<List<String>> grouping8 = sug2.suggestGroup(graphs, 3, "all");
//        Assert.assertEquals(grouping8.size(), 13);
//
//
//    }


}
