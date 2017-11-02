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
        List<Group> groupings = sug.grouping(groupings1, 2, 2, null, true);
        Assert.assertEquals(groupings.size(), 2);
        Assert.assertEquals(groupings.get(0).getSize(), 2);
        Assert.assertEquals(groupings.get(1).getSize(), 3);

        //groups of three
        List<Group> groupings2 = sug.grouping(groupings1, 3, 2 , null, true);
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
        List<Group> groupings = sug.grouping(groupings1, 2, 2, null ,true);
        Assert.assertEquals(groupings.size(), 1);
        Assert.assertEquals(groupings.get(0).getSize(),1);

        //groups of three
        List<Group> groupings2 = sug.grouping(groupings1, 3, 2 , null, true);
        Assert.assertEquals(groupings2.size(), 1);
        Assert.assertEquals(groupings2.get(0).getSize(),1);
    }


    @Test
    public void bySizeGroupTestLessStudents() {
        ConceptKnowledgeCalculatorAPI ckc = null;

        try {
            ckc = new ConceptKnowledgeCalculator("test/testresources/ManuallyCreated/researchConceptGraph.json", "test/testresources/ManuallyCreated/researchResource2.json", "test/testresources/ManuallyCreated/researchAssessment2.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }

        CohortConceptGraphs graphs = ckc.getCohortConceptGraphs();
        Assert.assertNotEquals(graphs, null);
        GroupSuggester sug = new GroupSuggester();

        Map<String, ConceptGraph> mapGraph = graphs.getUserToGraph();

        List<Group> groupings1 = sug.getGroupList(graphs);

        //groups of 2
        List<Group> groupings = sug.grouping(groupings1, 2, 2, null, false);
        Assert.assertEquals(groupings.size(), 2);

        List<String> one = groupings.get(0).getStudentNames();

        Assert.assertEquals(one.get(0),"s3");
        Assert.assertEquals(one.get(1),"s4");

        List<String> two = groupings.get(1).getStudentNames();
        Assert.assertEquals(two.get(0),"s5");
        Assert.assertEquals(two.get(1),"s1");
        Assert.assertEquals(two.get(2),"s2");


        //groups of three
        List<Group> groupings2 = sug.grouping(groupings1, 3, 2 , null, false);
        Assert.assertEquals(groupings2.size(), 2);


        List<String> three = groupings2.get(0).getStudentNames();

        Assert.assertEquals(three.get(0),"s3");
        Assert.assertEquals(three.get(1),"s4");
        Assert.assertEquals(three.get(2),"s5");

        List<String> four = groupings2.get(1).getStudentNames();
        Assert.assertEquals(four.get(0),"s1");
        Assert.assertEquals(four.get(1),"s2");


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

        List<Group> groupings2 = sug.grouping(actualGroupings, 2, 2, null , true);

        Assert.assertEquals(groupings2.size(), 2);
        Assert.assertEquals(groupings2.get(0).getSize(),2);
        Assert.assertEquals(groupings2.get(1).getSize(),3);

//        for(Group name: groupings2){
//            System.out.println(name);
//
//        }
    }




    @Test
    public void conceptTest(){
        ConceptKnowledgeCalculatorAPI ckc = null;

        try {
            ckc = new ConceptKnowledgeCalculator("test/testresources/ManuallyCreated/researchConceptGraph.json", "test/testresources/ManuallyCreated/researchResource2.json", "test/testresources/ManuallyCreated/researchAssessment2.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }

        CohortConceptGraphs graphs = ckc.getCohortConceptGraphs();
        Assert.assertNotEquals(graphs, null);
        GroupSuggester sug = new GroupSuggester();

        Map<String, ConceptGraph> mapGraph = graphs.getUserToGraph();

        List<Group> groupings1 = sug.getGroupList(graphs);

        //groups of 2
        List<Group> groupings = sug.grouping(groupings1, 2, 1, null, false);






    }


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
            List<Group> groupings = sug.grouping(groupings1, 2, 0 , ranges, false);

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

            List<Group> groupings2 = sug.grouping(actualGroupings, 2, 0 , ranges, false);

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

}
