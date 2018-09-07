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
 * Created by mkimmitchell on 7/31/17.
 */
public class GroupSuggesterTest {

    private CohortConceptGraphs simpleGraphs;
    private CohortConceptGraphs researchGraphs;

    @BeforeEach
    public void setup() throws IOException {
        ConceptGraph graph = new ConceptGraph(ConceptGraphRecord.buildFromJson(Settings.TEST_RESOURCE_DIR + "ManuallyCreated/simpleConceptGraphTest.json"));
        List<AssessmentItemResponse> assessmentItemResponses = AssessmentItemResponse.createAssessmentItemResponses(Arrays.asList(Settings.TEST_RESOURCE_DIR + "ManuallyCreated/simpleAssessmentTest.csv"));
        List<LearningResourceRecord> links = LearningResourceRecord.createLearningResourceRecordsFromJsonFiles(Arrays.asList(Settings.TEST_RESOURCE_DIR + "ManuallyCreated/simpleResourceTest.json"));
        graph.addLearningResourcesFromRecords(links);
        simpleGraphs = new CohortConceptGraphs(graph, assessmentItemResponses);

        ConceptGraph graph2 = new ConceptGraph(ConceptGraphRecord.buildFromJson(Settings.TEST_RESOURCE_DIR + "ManuallyCreated/researchConceptGraph.json"));
        List<AssessmentItemResponse> assessmentItemResponses2 = AssessmentItemResponse.createAssessmentItemResponses(Arrays.asList(Settings.TEST_RESOURCE_DIR + "ManuallyCreated/researchAssessment2.csv"));
        List<LearningResourceRecord> links2 = LearningResourceRecord.createLearningResourceRecordsFromJsonFiles(Arrays.asList(Settings.TEST_RESOURCE_DIR + "ManuallyCreated/researchResource2.json"));
        graph2.addLearningResourcesFromRecords(links2);
        researchGraphs = new CohortConceptGraphs(graph2, assessmentItemResponses2);
    }

    @Test
    public void complementaryKnowledgeTest() throws IOException{
        GroupSuggester sug = new GroupSuggester();

        List<Group> groupings1 = sug.getGroupList(simpleGraphs);

        List<Suggester> suggesterList = new ArrayList<>();
        suggesterList.add(new ConceptSuggester());
        suggesterList.add(new ComplementaryKnowledgeSuggester());


        //groups of 2
        List<Group> groupings = sug.grouping(groupings1, 2,suggesterList );

        Assert.assertEquals(groupings.size(), 2);

        Assert.assertEquals(groupings.get(0).getSize(), 2);
        Assert.assertEquals(groupings.get(0).getRationale(), "   ,Concept: B ,Complementary Knowledge");
        Assert.assertEquals(groupings.get(0).getStudentNames().get(0), "s3");
        Assert.assertEquals(groupings.get(0).getStudentNames().get(1), "s4");

        Assert.assertEquals(groupings.get(1).getSize(), 3);
        Assert.assertEquals(groupings.get(1).getRationale(), "   ,Concept: B ,Complementary Knowledge  ,Extra Members");
        Assert.assertEquals(groupings.get(1).getStudentNames().get(0), "s5");
        Assert.assertEquals(groupings.get(1).getStudentNames().get(1), "s1");
        Assert.assertEquals(groupings.get(1).getStudentNames().get(2), "s2");

    }

    @Test
    public void compKNowTEST2() {

        Assert.assertNotEquals(researchGraphs, null);
        GroupSuggester sug = new GroupSuggester();

        List<Group> groupings1 = sug.getGroupList(researchGraphs);

        List<Suggester> suggesterList = new ArrayList<>();
        suggesterList.add(new ConceptSuggester());
        suggesterList.add(new ComplementaryKnowledgeSuggester());


        //groups of 2
        List<Group> groupings = sug.grouping(groupings1, 2,suggesterList );
        //no jigsaw suggesters therefore it's just bysize

        Assert.assertEquals(groupings.size(), 2);
        Assert.assertEquals(groupings.get(0).getSize(), 2);
        Assert.assertEquals(groupings.get(0).getRationale(), "   ,Concept: Booleans ,Complementary Knowledge");
        Assert.assertEquals(groupings.get(0).getStudentNames().get(0), "s3");
        Assert.assertEquals(groupings.get(0).getStudentNames().get(1), "s4");
        Assert.assertEquals(groupings.get(1).getSize(), 3);
        Assert.assertEquals(groupings.get(1).getRationale(), "   ,Concept: Booleans ,Complementary Knowledge  ,Extra Members");
        Assert.assertEquals(groupings.get(1).getStudentNames().get(0), "s5");
        Assert.assertEquals(groupings.get(1).getStudentNames().get(1), "s1");
        Assert.assertEquals(groupings.get(1).getStudentNames().get(2), "s2");
//

    }

    @Test
    public void randomBySizeTest() {
        Assert.assertNotEquals(researchGraphs, null);
        GroupSuggester sug = new GroupSuggester();

        List<Group> groupings1 = sug.getGroupList(researchGraphs);

        List<Suggester> suggesterList = new ArrayList<>();
        suggesterList.add(new BySizeSuggester(2,true));

        //groups of 2
        List<Group> groupings = sug.grouping(groupings1, 2,suggesterList );

        Assert.assertEquals(groupings.size(), 2);
        Assert.assertEquals(groupings.get(0).getRationale(), "  , Random");
        Assert.assertEquals(groupings.get(0).getSize(), 2);
        Assert.assertEquals(groupings.get(1).getSize(), 3);
        Assert.assertEquals(groupings.get(1).getRationale(), "  , Random  ,Extra Members");


        List<Suggester> suggesterList2 = new ArrayList<>();
        suggesterList2.add(new BySizeSuggester(3,true));
        //groups of three
        List<Group> groupings2 = sug.grouping(groupings1, 3, suggesterList2);
        Assert.assertEquals(groupings2.size(), 2);
        Assert.assertEquals(groupings2.get(0).getSize(), 3);
        Assert.assertEquals(groupings2.get(0).getRationale(), "  , Random");
        Assert.assertEquals(groupings2.get(1).getSize(), 2);
        Assert.assertEquals(groupings2.get(1).getRationale(), "");
    }

    @Test
    public void randomBySizeLessStudentsTest() {
        Assert.assertNotEquals(simpleGraphs, null);
        GroupSuggester sug = new GroupSuggester();
        List<Group> groupings1 = sug.getGroupList(simpleGraphs);

        List<Suggester> suggesterList = new ArrayList<>();
        suggesterList.add(new BySizeSuggester(2,true));

        //groups of 2
        List<Group> groupings = sug.grouping(groupings1, 2,suggesterList );

        //TODO: TD these were set to expect 1, but that doesnt' seem right, should be looked at closer
        Assert.assertEquals(2, groupings.size());
        Assert.assertEquals(2, groupings.get(0).getSize());
        Assert.assertEquals(groupings.get(0).getRationale(), "  , Random");

        List<Suggester> suggesterList2 = new ArrayList<>();
        suggesterList2.add(new BySizeSuggester(2,true));

        //groups of 3
        List<Group> groupings2 = sug.grouping(groupings1, 3,suggesterList2 );
        Assert.assertEquals(groupings2.get(0).getRationale(), "  , Random");

        //TODO: TD these were set to expect 1, but that doesnt' seem right, should be looked at closer
        Assert.assertEquals(2, groupings2.size());
        Assert.assertEquals(2, groupings2.get(0).getSize());
    }


    @Test
    public void bySizeGroupTestLessStudents() {

        Assert.assertNotEquals(researchGraphs, null);
        GroupSuggester sug = new GroupSuggester();

        Map<String, ConceptGraph> mapGraph = researchGraphs.getUserToGraph();

        List<Group> groupings1 = sug.getGroupList(researchGraphs);

        //groups of 2
        List<Suggester> suggesterList = new ArrayList<>();
        suggesterList.add(new BySizeSuggester(2,false));

        List<Group> groupings = sug.grouping(groupings1, 2, suggesterList);
        Assert.assertEquals(groupings.size(), 2);

        List<String> one = groupings.get(0).getStudentNames();
        Assert.assertEquals(groupings.get(0).getRationale(), "  ,By Size: 2");

        Assert.assertEquals(one.get(0),"s3");
        Assert.assertEquals(one.get(1),"s4");

        List<String> two = groupings.get(1).getStudentNames();
        Assert.assertEquals(groupings.get(1).getRationale(),"  ,By Size: 2  ,Extra Members");
        Assert.assertEquals(two.get(0),"s5");
        Assert.assertEquals(two.get(1),"s1");
        Assert.assertEquals(two.get(2),"s2");


        //groups of three
        List<Suggester> suggesterList2 = new ArrayList<>();
        suggesterList2.add(new BySizeSuggester(3,false));

        List<Group> groupings2 = sug.grouping(groupings1, 3, suggesterList2);
        Assert.assertEquals(groupings2.size(), 2);


        List<String> three = groupings2.get(0).getStudentNames();
        Assert.assertEquals(groupings2.get(0).getRationale(), "  ,By Size: 3");
        Assert.assertEquals(three.get(0),"s3");
        Assert.assertEquals(three.get(1),"s4");
        Assert.assertEquals(three.get(2),"s5");

        List<String> four = groupings2.get(1).getStudentNames();
        Assert.assertEquals(groupings2.get(1).getRationale(), "");
        Assert.assertEquals(four.get(0),"s1");
        Assert.assertEquals(four.get(1),"s2");


    }

    @Test
    public void randomTest() {
        GroupSuggester sug = new GroupSuggester();

        //testing with more than one list of groups
        List<Group> actualGroupings = new ArrayList<>();

        Group group = new Group();
        group.addMember("mia", null);
        group.addMember("don", null);
        group.addMember("bob", null);
        actualGroupings.add(group);

        Group group2 = new Group();
        group2.addMember("kayli",null);
        group2.addMember("dan", null);
        actualGroupings.add(group2);

        List<Suggester> suggesterList = new ArrayList<>();
        suggesterList.add(new BySizeSuggester(2,true));


        List<Group> groupings2 = sug.grouping(actualGroupings, 2, suggesterList);
        Assert.assertEquals(groupings2.get(0).getRationale(), "  , Random");
        Assert.assertEquals(groupings2.size(), 2);
        Assert.assertEquals(groupings2.get(0).getSize(),2);
        Assert.assertEquals(groupings2.get(1).getSize(),3);
        Assert.assertEquals(groupings2.get(1).getRationale(), "  , Random  ,Extra Members");

    }




    @Test
    public void conceptTest(){
        //This assumes the suggested concepts by having each student's list of suggested concepts printed out to the screen
        Assert.assertNotEquals(researchGraphs, null);
        GroupSuggester sug = new GroupSuggester();

        Map<String, ConceptGraph> mapGraph = researchGraphs.getUserToGraph();

        List<Group> groupings1 = sug.getGroupList(researchGraphs);

//        groups of 2
        List<Suggester> suggesterList = new ArrayList<>();
        suggesterList.add(new ConceptSuggester());

        List<Group> groupings = sug.grouping(groupings1, 2, suggesterList);

        List<String> three = groupings.get(0).getStudentNames();
        Assert.assertEquals(groupings.get(0).getRationale(), "  ,Concept: no suggestions");
        Assert.assertEquals(three.size(), 3);
        Assert.assertEquals(three.get(0),"s4");
        Assert.assertEquals(three.get(1),"s5");
        Assert.assertEquals(three.get(2),"s1");



        //testing with more than one list of groups
        List<Group> actualGroupings = new ArrayList<>();

        Group group = new Group();
        group.addMember("mia", mapGraph.get("s1"));
        group.addMember("don", mapGraph.get("s3"));
        group.addMember("bob", mapGraph.get("s1"));
        actualGroupings.add(group);

        Group group2 = new Group();
        group2.addMember("kayli", mapGraph.get("s3"));
        group2.addMember("dan", mapGraph.get("s5"));
        actualGroupings.add(group2);

        List<Suggester> suggesterList2 = new ArrayList<>();
        suggesterList2.add(new ConceptSuggester());

        List<Group> groupings2 = sug.grouping(actualGroupings, 2, suggesterList2);

        Assert.assertEquals(groupings2.size(), 4);
        Assert.assertEquals(groupings2.get(0).getSize(),2);
        Assert.assertEquals(groupings2.get(0).getRationale(), "  ,Concept: no suggestions");
        Assert.assertEquals(groupings2.get(1).getSize(),1);
        Assert.assertEquals(groupings2.get(1).getRationale(), "  ,Concept: Booleans");
        Assert.assertEquals(groupings2.get(2).getSize(),1);
        Assert.assertEquals(groupings2.get(2).getRationale(), "  ,Concept: no suggestions");
        Assert.assertEquals(groupings2.get(3).getSize(),1);
        Assert.assertEquals(groupings2.get(3).getRationale(), "  ,Concept: Booleans");


    }


    @Test
    public void bucketTest() {
        Assert.assertNotEquals(researchGraphs, null);

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

            List<Group> groupings1 = sug.getGroupList(researchGraphs);
            //groups of 2
            List<Suggester> suggesterList = new ArrayList<>();
            suggesterList.add(new BucketSuggester(ranges));

            List<Group> groupings = sug.grouping(groupings1, 2, suggesterList);

            Assert.assertEquals(groupings.size(), 3);
            Assert.assertEquals(groupings.get(0).getSize(),0 );
            Assert.assertEquals(groupings.get(0).getRationale(), "  ,Bucket: 0 - 50");

            Assert.assertEquals(groupings.get(1).getSize(), 2);
            Assert.assertEquals(groupings.get(1).getRationale(), "  ,Bucket: 51 - 80");
            Assert.assertEquals(groupings.get(1).contains("s2"), true);
            Assert.assertEquals(groupings.get(1).contains("s3"),true);

            Assert.assertEquals(groupings.get(2).getSize(), 3);
            Assert.assertEquals(groupings.get(2).getRationale(), "  ,Bucket: 81 - 100");
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


            List<Suggester> suggesterList2 = new ArrayList<>();
            suggesterList2.add(new BucketSuggester(ranges));
            List<Group> groupings2 = sug.grouping(actualGroupings, 2, suggesterList2);

            Assert.assertEquals(groupings2.size(), 6);
            Assert.assertEquals(groupings2.get(0).getSize(),0 );
            Assert.assertEquals(groupings.get(0).getRationale(), "  ,Bucket: 0 - 50");


            Assert.assertEquals(groupings2.get(1).getSize(),3 );
            Assert.assertEquals(groupings2.get(1).getRationale(), "  ,Bucket: 51 - 80");
            Assert.assertEquals(groupings2.get(1).contains("mia"), true);
            Assert.assertEquals(groupings2.get(1).contains("bob"), true);
            Assert.assertEquals(groupings2.get(1).contains("alena"), true);

            Assert.assertEquals(groupings2.get(2).getSize(),1);
            Assert.assertEquals(groupings2.get(2).getRationale(), "  ,Bucket: 81 - 100");
            Assert.assertEquals(groupings2.get(2).contains("don"), true);

            Assert.assertEquals(groupings2.get(3).getSize(),0);
            Assert.assertEquals(groupings2.get(3).getRationale(), "  ,Bucket: 0 - 50");

            Assert.assertEquals(groupings2.get(4).getSize(),1);
            Assert.assertEquals(groupings2.get(4).getRationale(), "  ,Bucket: 51 - 80");
            Assert.assertEquals(groupings2.get(4).contains("dan"), true);

            Assert.assertEquals(groupings2.get(5).getSize(),1);
            Assert.assertEquals(groupings2.get(5).getRationale(), "  ,Bucket: 81 - 100");
            Assert.assertEquals(groupings2.get(5).contains("kayli"), true);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    @Test
    public void mixedSuggesterTest(){

        //set up for buckets
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

            List<Group> groupings = sug.getGroupList(researchGraphs);
            Map<String, ConceptGraph> mapGraph = researchGraphs.getUserToGraph();

            List<Group> actualGroupings = new ArrayList<>();
            Group group = new Group();
            group.addMember("mia", mapGraph.get("s3"));
            group.addMember("don", mapGraph.get("s4"));
            group.addMember("bob", mapGraph.get("s2"));
            group.addMember("alena", mapGraph.get("s3"));
            group.addMember("kayli", mapGraph.get("s5"));
            group.addMember("dan", mapGraph.get("s2"));
            actualGroupings.add(group);

            List<Suggester> suggesterList = new ArrayList<>();
            suggesterList.add(new BucketSuggester(ranges));
            suggesterList.add(new ConceptSuggester());
            suggesterList.add(new BySizeSuggester(2, false));

            List<Group> groupings1 = sug.grouping(actualGroupings, 2, suggesterList);

            Assert.assertEquals(groupings1.size(), 3);
            Assert.assertEquals(groupings1.get(0).getSize(), 2);
            Assert.assertEquals(groupings1.get(0).getRationale(), "    ,Bucket: 51 - 80 ,Concept: Booleans ,By Size: 2");
            Assert.assertEquals(groupings1.get(1).getSize(), 2);
            Assert.assertEquals(groupings1.get(1).getRationale(), "    ,Bucket: 51 - 80 ,Concept: Booleans ,By Size: 2");
            Assert.assertEquals(groupings1.get(2).getSize(), 2);
            Assert.assertEquals(groupings1.get(2).getRationale(), "    ,Bucket: 81 - 100 ,Concept: no suggestions ,By Size: 2");


        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
