package edu.ithaca.dragon.tecmap.suggester.GroupSuggester;

//import edu.ithaca.dragon.tecmap.legacy.ConceptKnowledgeCalculator;
//import edu.ithaca.dragon.tecmap.legacy.ConceptKnowledgeCalculatorAPI;
import edu.ithaca.dragon.tecmap.Settings;
import edu.ithaca.dragon.tecmap.conceptgraph.CohortConceptGraphs;
import edu.ithaca.dragon.tecmap.conceptgraph.ConceptGraph;
import edu.ithaca.dragon.tecmap.io.record.ConceptGraphRecord;
import edu.ithaca.dragon.tecmap.io.record.LearningResourceRecord;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItemResponse;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by mkimmitchell on 11/2/17.
 */
public class SuggesterTest {

    private CohortConceptGraphs graphs;

    @BeforeEach
    public void setup() throws IOException {
        ConceptGraph graph2 = new ConceptGraph(ConceptGraphRecord.buildFromJson(Settings.TEST_RESOURCE_DIR + "ManuallyCreated/researchConceptGraph.json"));
        List<AssessmentItemResponse> assessmentItemResponses2 = AssessmentItemResponse.createAssessmentItemResponses(Arrays.asList(Settings.TEST_RESOURCE_DIR + "ManuallyCreated/researchAssessment2.csv"));
        List<LearningResourceRecord> links2 = LearningResourceRecord.createLearningResourceRecordsFromJsonFiles(Arrays.asList(Settings.TEST_RESOURCE_DIR + "ManuallyCreated/researchResource2.json"));
        graph2.addLearningResourcesFromRecords(links2);
        graphs = new CohortConceptGraphs(graph2, assessmentItemResponses2);
    }


    @Test
    public void bySizeRandomTest() {
        //because this doesn't handle the extra members, they will not be tested here
        Assert.assertNotEquals(graphs, null);

        BySizeSuggester sug  = new BySizeSuggester(2, true);
        List<Group> list = GroupSuggester.getGroupList(graphs);
        List<Group> groupings = sug.suggestGroup(list.get(0), new Group());

        //groups of 2
        Assert.assertEquals(groupings.size(), 2);
        Assert.assertEquals(groupings.get(0).getSize(), 2);
        Assert.assertEquals(groupings.get(0).getRationale(), "  , Random");
        Assert.assertEquals(groupings.get(1).getSize(), 2);
        Assert.assertEquals(groupings.get(1).getRationale(), "  , Random");


        //groups of three
        BySizeSuggester sug2  = new BySizeSuggester(3, true);
        List<Group> groupings2 = sug2.suggestGroup(list.get(0), new Group() );
        Assert.assertEquals(groupings2.size(), 1);
        Assert.assertEquals(groupings2.get(0).getSize(), 3);
        Assert.assertEquals(groupings.get(0).getRationale(), "  , Random");


    }


    @Test
    public void randomBySizeLessStudentsSimpleTest() {
        List<Group> list = GroupSuggester.getGroupList(graphs);

        //groups of 2
        BySizeSuggester sug  = new BySizeSuggester(2, true);
        List<Group> groupings = sug.suggestGroup(list.get(0), new Group());
//        for(Group gr: groupings){
//            System.out.println(gr.toString(0));
//        }

        //TODO: TD this was expecting 1, but it seems like it should be expecting 2
        Assert.assertEquals(2, groupings.size());
        Assert.assertEquals(2, groupings.get(0).getSize());


        //groups of 3
        BySizeSuggester sug2  = new BySizeSuggester(3, true);
        List<Group> groupings2 = sug2.suggestGroup(list.get(0), new Group());
        Assert.assertEquals(groupings2.size(), 1);
        //TODO: TD this was expecting 1, but it seems like it should be expecting 3
        Assert.assertEquals(3, groupings2.get(0).getSize());
//        Assert.assertEquals(groupings2.get(0).getRationale(), "");
//                for(Group gr: groupings2){
//            System.out.println(gr.toString(0));
//        }
    }

    @Test
    public void bySizeGroupTestLessStudentsOrderedMediumTest() {
        List<Group> list = GroupSuggester.getGroupList(graphs);

        //groups of 2
        BySizeSuggester sug  = new BySizeSuggester(2, false);
        List<Group> groupings = sug.suggestGroup(list.get(0), new Group());
        Assert.assertEquals(groupings.size(), 2);
        Assert.assertEquals(groupings.get(0).getRationale(), "  ,By Size: 2");
        List<String> one = groupings.get(0).getStudentNames();
        Assert.assertEquals(one.get(0),"s3");
        Assert.assertEquals(one.get(1),"s4");

        Assert.assertEquals(groupings.get(1).getRationale(), "  ,By Size: 2");
        List<String> two = groupings.get(1).getStudentNames();
        Assert.assertEquals(two.get(0),"s5");
        Assert.assertEquals(two.get(1),"s1");


        //groups of 3
        BySizeSuggester sug2  = new BySizeSuggester(3, false);
        List<Group> groupings2 = sug2.suggestGroup(list.get(0), new Group());
        Assert.assertEquals(groupings2.size(), 1);

        List<String> three = groupings2.get(0).getStudentNames();
        Assert.assertEquals(groupings2.get(0).getRationale(), "  ,By Size: "+3);
        Assert.assertEquals(three.get(0),"s3");
        Assert.assertEquals(three.get(1),"s4");
        Assert.assertEquals(three.get(2),"s5");

    }


    @Test
    public void conceptTest(){
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
        Assert.assertEquals(groupings2.get(0).getSize(),3);
        Assert.assertEquals(groupings2.get(0).getStudentNames().get(0),"dan");
        Assert.assertEquals(groupings2.get(0).getStudentNames().get(1),"mia");
        Assert.assertEquals(groupings2.get(0).getStudentNames().get(2),"bob");

        Assert.assertEquals(groupings2.get(1).getSize(),2);
        Assert.assertEquals(groupings2.get(1).getStudentNames().get(0),"kayli");
        Assert.assertEquals(groupings2.get(1).getStudentNames().get(1),"don");


        //        groups of 2
        List<Suggester> suggesterList = new ArrayList<>();
        suggesterList.add(new ConceptSuggester());

        List<Group> groupings = sug.suggestGroup(list.get(0), new Group());
        Assert.assertEquals(groupings.get(0).getRationale(), "  ,Concept: no suggestions");
        List<String> three = groupings.get(0).getStudentNames();
        Assert.assertEquals(three.size(), 3);
        Assert.assertEquals(three.get(0),"s4");
        Assert.assertEquals(three.get(1),"s5");
        Assert.assertEquals(three.get(2),"s1");
    }



    @Test
    public void bucketTest() {
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


            List<Group> list = GroupSuggester.getGroupList(graphs);
            //groups of 2
            BucketSuggester sug = new BucketSuggester(ranges);
            List<Group> groupings = sug.suggestGroup(list.get(0), new Group());

            Assert.assertEquals(groupings.size(), 3);
            Assert.assertEquals(groupings.get(0).getSize(), 0);
            Assert.assertEquals(groupings.get(0).getRationale(), "  ,Bucket: 0 - 50");

            Assert.assertEquals(groupings.get(1).getSize(), 2);
            Assert.assertEquals(groupings.get(1).getRationale(), "  ,Bucket: 51 - 80");
            Assert.assertEquals(groupings.get(1).contains("s3"), true);
            Assert.assertEquals(groupings.get(1).contains("s2"), true);

            Assert.assertEquals(groupings.get(2).getSize(), 3);
            Assert.assertEquals(groupings.get(2).getRationale(), "  ,Bucket: 81 - 100");
            Assert.assertEquals(groupings.get(2).contains("s4"), true);
            Assert.assertEquals(groupings.get(2).contains("s5"), true);
            Assert.assertEquals(groupings.get(2).contains("s1"), true);

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }

    }

}
