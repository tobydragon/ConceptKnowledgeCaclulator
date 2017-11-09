package edu.ithaca.dragonlab.ckc.suggester.GroupSuggesterTest;

import edu.ithaca.dragonlab.ckc.ConceptKnowledgeCalculator;
import edu.ithaca.dragonlab.ckc.ConceptKnowledgeCalculatorAPI;
import edu.ithaca.dragonlab.ckc.conceptgraph.CohortConceptGraphs;
import edu.ithaca.dragonlab.ckc.conceptgraph.ConceptGraph;
import edu.ithaca.dragonlab.ckc.suggester.GroupSuggester.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

/**
 * Created by mkimmitchell on 11/2/17.
 */
public class SuggesterTest {

    @Test
    public void bySizeRandomTest() {
        //because this doesn't handle the extra members, they will not be tested here

        ConceptKnowledgeCalculatorAPI ckc = null;

        try {
            ckc = new ConceptKnowledgeCalculator("test/testresources/ManuallyCreated/researchConceptGraph.json", "test/testresources/ManuallyCreated/researchResource2.json", "test/testresources/ManuallyCreated/researchAssessment2.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }

        CohortConceptGraphs graphs = ckc.getCohortConceptGraphs();
        Assert.assertNotEquals(graphs, null);

        BySizeSuggester sug  = new BySizeSuggester(2, true);
        List<Group> list = GroupSuggester.getGroupList(graphs);
        List<Group> groupings = sug.suggestGroup(list.get(0), new Group());

        //groups of 2
        Assert.assertEquals(groupings.size(), 2);
        Assert.assertEquals(groupings.get(0).getSize(), 2);
        Assert.assertEquals(groupings.get(1).getSize(), 2);

        //groups of three
        BySizeSuggester sug2  = new BySizeSuggester(3, true);
        List<Group> groupings2 = sug2.suggestGroup(list.get(0), new Group() );
        Assert.assertEquals(groupings2.size(), 1);
        Assert.assertEquals(groupings2.get(0).getSize(), 3);


//        for(Group gr: groupings2){
//            System.out.println(gr);
//        }
    }


    @Test
    public void randomBySizeLessStudentsSimpleTest() {
        ConceptKnowledgeCalculatorAPI ckc = null;

        try {
            ckc = new ConceptKnowledgeCalculator("test/testresources/ManuallyCreated/simpleConceptGraph.json", "test/testresources/ManuallyCreated/simpleResource.json", "test/testresources/ManuallyCreated/simpleAssessment.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
        CohortConceptGraphs graphs = ckc.getCohortConceptGraphs();
        Assert.assertNotEquals(graphs, null);

        List<Group> list = GroupSuggester.getGroupList(graphs);

        //groups of 2
        BySizeSuggester sug  = new BySizeSuggester(2, true);
        List<Group> groupings = sug.suggestGroup(list.get(0), new Group());

        Assert.assertEquals(groupings.size(), 1);
        Assert.assertEquals(groupings.get(0).getSize(),1);

        //groups of 3
        BySizeSuggester sug2  = new BySizeSuggester(3, true);
        List<Group> groupings2 = sug2.suggestGroup(list.get(0), new Group());
        Assert.assertEquals(groupings2.size(), 1);
        Assert.assertEquals(groupings2.get(0).getSize(),1);
    }

    @Test
    public void bySizeGroupTestLessStudentsOrderedMediumTest() {
        ConceptKnowledgeCalculatorAPI ckc = null;

        try {
            ckc = new ConceptKnowledgeCalculator("test/testresources/ManuallyCreated/researchConceptGraph.json", "test/testresources/ManuallyCreated/researchResource2.json", "test/testresources/ManuallyCreated/researchAssessment2.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }

        CohortConceptGraphs graphs = ckc.getCohortConceptGraphs();
        Assert.assertNotEquals(graphs, null);

        List<Group> list = GroupSuggester.getGroupList(graphs);

        //groups of 2
        BySizeSuggester sug  = new BySizeSuggester(2, false);
        List<Group> groupings = sug.suggestGroup(list.get(0), new Group());
        Assert.assertEquals(groupings.size(), 2);
        List<String> one = groupings.get(0).getStudentNames();

        Assert.assertEquals(one.get(0),"s3");
        Assert.assertEquals(one.get(1),"s4");

        List<String> two = groupings.get(1).getStudentNames();
        Assert.assertEquals(two.get(0),"s5");
        Assert.assertEquals(two.get(1),"s1");


        //groups of 3
        BySizeSuggester sug2  = new BySizeSuggester(3, false);
        List<Group> groupings2 = sug2.suggestGroup(list.get(0), new Group());
        Assert.assertEquals(groupings2.size(), 1);

        List<String> three = groupings2.get(0).getStudentNames();

        Assert.assertEquals(three.get(0),"s3");
        Assert.assertEquals(three.get(1),"s4");
        Assert.assertEquals(three.get(2),"s5");

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

        Map<String, ConceptGraph> mapGraph = graphs.getUserToGraph();
        List<Group> list = GroupSuggester.getGroupList(graphs);


        //testing with more than one list of groups
        List<Group> actualGroupings = new ArrayList<>();

        Group group = new Group();
        group.addMember("mia", mapGraph.get("s1"));
        group.addMember("don", mapGraph.get("s3"));
        group.addMember("bob", mapGraph.get("s1"));
        group.addMember("kayli", mapGraph.get("s3"));
        group.addMember("dan", mapGraph.get("s5"));
        actualGroupings.add(group);

        ConceptSuggester sug  = new ConceptSuggester();
        List<Group> groupings2 = sug.suggestGroup(actualGroupings.get(0), new Group());

        Assert.assertEquals(groupings2.size(), 2);
        Assert.assertEquals(groupings2.get(0).getSize(),2);
        Assert.assertEquals(groupings2.get(1).getSize(),3);



        //        groups of 2
        List<Suggester> suggesterList = new ArrayList<>();
        suggesterList.add(new ConceptSuggester());

        List<Group> groupings = sug.suggestGroup(list.get(0), new Group());

        List<String> three = groupings.get(0).getStudentNames();
        Assert.assertEquals(three.get(0),"s3");
        Assert.assertEquals(three.get(1),"s2");
        List<String> four = groupings.get(1).getStudentNames();
        Assert.assertEquals(four.get(0),"s4");
        Assert.assertEquals(four.get(1),"s5");
        Assert.assertEquals(four.get(2),"s1");

    }

}
