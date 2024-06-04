package edu.ithaca.dragon.tecmap.suggester.GroupSuggester;

//import edu.ithaca.dragon.tecmap.legacy.ConceptKnowledgeCalculator;
//import edu.ithaca.dragon.tecmap.legacy.ConceptKnowledgeCalculatorAPI;
import edu.ithaca.dragon.tecmap.Settings;
import edu.ithaca.dragon.tecmap.conceptgraph.CohortConceptGraphs;
import edu.ithaca.dragon.tecmap.conceptgraph.ConceptGraph;
import edu.ithaca.dragon.tecmap.io.record.ConceptGraphRecord;
import edu.ithaca.dragon.tecmap.io.record.LearningResourceRecord;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItemResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Created by mkimmitchell on 7/31/17.
 */
public class GroupSuggesterTest {

    private Map<String, ConceptGraph> simpleGraphs;
    private Map<String, ConceptGraph> researchGraphs;

    @BeforeEach
    public void setup() throws IOException {
        ConceptGraph graph = new ConceptGraph(ConceptGraphRecord.buildFromJson(Settings.TEST_RESOURCE_DIR + "ManuallyCreated/simpleConceptGraphTest.json"));
        List<AssessmentItemResponse> assessmentItemResponses = AssessmentItemResponse.createAssessmentItemResponses(Arrays.asList(Settings.TEST_RESOURCE_DIR + "ManuallyCreated/simpleAssessmentTest.csv"));
        List<LearningResourceRecord> links = LearningResourceRecord.createLearningResourceRecordsFromJsonFiles(Arrays.asList(Settings.TEST_RESOURCE_DIR + "ManuallyCreated/simpleResourceTest.json"));
        graph.addLearningResourcesFromRecords(links);
        simpleGraphs = new CohortConceptGraphs(graph, assessmentItemResponses).getUserToGraph();

        ConceptGraph graph2 = new ConceptGraph(ConceptGraphRecord.buildFromJson(Settings.TEST_RESOURCE_DIR + "ManuallyCreated/researchConceptGraph.json"));
        List<AssessmentItemResponse> assessmentItemResponses2 = AssessmentItemResponse.createAssessmentItemResponses(Arrays.asList(Settings.TEST_RESOURCE_DIR + "ManuallyCreated/researchAssessment2.csv"));
        List<LearningResourceRecord> links2 = LearningResourceRecord.createLearningResourceRecordsFromJsonFiles(Arrays.asList(Settings.TEST_RESOURCE_DIR + "ManuallyCreated/researchResource2.json"));
        graph2.addLearningResourcesFromRecords(links2);
        researchGraphs = new CohortConceptGraphs(graph2, assessmentItemResponses2).getUserToGraph();
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

        assertEquals(groupings.size(), 2);

        assertEquals(groupings.get(0).getSize(), 2);
        assertEquals(groupings.get(0).getRationale(), "   ,Concept: B ,Complementary Knowledge");
        assertEquals(groupings.get(0).getStudentNames().get(0), "s3");
        assertEquals(groupings.get(0).getStudentNames().get(1), "s4");

        assertEquals(groupings.get(1).getSize(), 3);
        assertEquals(groupings.get(1).getRationale(), "   ,Concept: B ,Complementary Knowledge  ,Extra Members");
        assertEquals(groupings.get(1).getStudentNames().get(0), "s5");
        assertEquals(groupings.get(1).getStudentNames().get(1), "s1");
        assertEquals(groupings.get(1).getStudentNames().get(2), "s2");

    }

    @Test
    public void compKNowTEST2() {

        assertNotEquals(researchGraphs, null);
        GroupSuggester sug = new GroupSuggester();

        List<Group> groupings1 = sug.getGroupList(researchGraphs);

        List<Suggester> suggesterList = new ArrayList<>();
        suggesterList.add(new ConceptSuggester());
        suggesterList.add(new ComplementaryKnowledgeSuggester());


        //groups of 2
        List<Group> groupings = sug.grouping(groupings1, 2,suggesterList );
        //no jigsaw suggesters therefore it's just bysize

        assertEquals(groupings.size(), 2);
        assertEquals(groupings.get(0).getSize(), 2);
        assertEquals(groupings.get(0).getRationale(), "   ,Concept: Booleans ,Complementary Knowledge");
        assertEquals(groupings.get(0).getStudentNames().get(0), "s3");
        assertEquals(groupings.get(0).getStudentNames().get(1), "s4");
        assertEquals(groupings.get(1).getSize(), 3);
        assertEquals(groupings.get(1).getRationale(), "   ,Concept: Booleans ,Complementary Knowledge  ,Extra Members");
        assertEquals(groupings.get(1).getStudentNames().get(0), "s5");
        assertEquals(groupings.get(1).getStudentNames().get(1), "s1");
        assertEquals(groupings.get(1).getStudentNames().get(2), "s2");
//

    }

    @Test
    public void randomBySizeTest() {
        assertNotEquals(researchGraphs, null);
        GroupSuggester sug = new GroupSuggester();

        List<Group> groupings1 = sug.getGroupList(researchGraphs);

        List<Suggester> suggesterList = new ArrayList<>();
        suggesterList.add(new BySizeSuggester(2,true));

        //groups of 2
        List<Group> groupings = sug.grouping(groupings1, 2,suggesterList );

        assertEquals(groupings.size(), 2);
        assertEquals(groupings.get(0).getRationale(), "  , Random");
        assertEquals(groupings.get(0).getSize(), 2);
        assertEquals(groupings.get(1).getSize(), 3);
        assertEquals(groupings.get(1).getRationale(), "  , Random  ,Extra Members");


        List<Suggester> suggesterList2 = new ArrayList<>();
        suggesterList2.add(new BySizeSuggester(3,true));
        //groups of three
        List<Group> groupings2 = sug.grouping(groupings1, 3, suggesterList2);
        assertEquals(groupings2.size(), 2);
        assertEquals(groupings2.get(0).getSize(), 3);
        assertEquals(groupings2.get(0).getRationale(), "  , Random");
        assertEquals(groupings2.get(1).getSize(), 2);
        assertEquals(groupings2.get(1).getRationale(), "");
    }

    @Test
    public void randomBySizeLessStudentsTest() {
        assertNotEquals(simpleGraphs, null);
        GroupSuggester sug = new GroupSuggester();
        List<Group> groupings1 = sug.getGroupList(simpleGraphs);

        List<Suggester> suggesterList = new ArrayList<>();
        suggesterList.add(new BySizeSuggester(2,true));

        //groups of 2
        List<Group> groupings = sug.grouping(groupings1, 2,suggesterList );

        //TODO: TD these were set to expect 1, but that doesnt' seem right, should be looked at closer
        assertEquals(2, groupings.size());
        assertEquals(2, groupings.get(0).getSize());
        assertEquals(groupings.get(0).getRationale(), "  , Random");

        List<Suggester> suggesterList2 = new ArrayList<>();
        suggesterList2.add(new BySizeSuggester(2,true));

        //groups of 3
        List<Group> groupings2 = sug.grouping(groupings1, 3,suggesterList2 );
        assertEquals(groupings2.get(0).getRationale(), "  , Random");

        //TODO: TD these were set to expect 1, but that doesnt' seem right, should be looked at closer
        assertEquals(2, groupings2.size());
        assertEquals(2, groupings2.get(0).getSize());
    }


    @Test
    public void bySizeGroupTestLessStudents() {

        assertNotEquals(researchGraphs, null);
        GroupSuggester sug = new GroupSuggester();

        List<Group> groupings1 = sug.getGroupList(researchGraphs);

        //groups of 2
        List<Suggester> suggesterList = new ArrayList<>();
        suggesterList.add(new BySizeSuggester(2,false));

        List<Group> groupings = sug.grouping(groupings1, 2, suggesterList);
        assertEquals(groupings.size(), 2);

        List<String> one = groupings.get(0).getStudentNames();
        assertEquals(groupings.get(0).getRationale(), "  ,By Size: 2");

        assertEquals(one.get(0),"s3");
        assertEquals(one.get(1),"s4");

        List<String> two = groupings.get(1).getStudentNames();
        assertEquals(groupings.get(1).getRationale(),"  ,By Size: 2  ,Extra Members");
        assertEquals(two.get(0),"s5");
        assertEquals(two.get(1),"s1");
        assertEquals(two.get(2),"s2");


        //groups of three
        List<Suggester> suggesterList2 = new ArrayList<>();
        suggesterList2.add(new BySizeSuggester(3,false));

        List<Group> groupings2 = sug.grouping(groupings1, 3, suggesterList2);
        assertEquals(groupings2.size(), 2);


        List<String> three = groupings2.get(0).getStudentNames();
        assertEquals(groupings2.get(0).getRationale(), "  ,By Size: 3");
        assertEquals(three.get(0),"s3");
        assertEquals(three.get(1),"s4");
        assertEquals(three.get(2),"s5");

        List<String> four = groupings2.get(1).getStudentNames();
        assertEquals(groupings2.get(1).getRationale(), "");
        assertEquals(four.get(0),"s1");
        assertEquals(four.get(1),"s2");


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
        assertEquals(groupings2.get(0).getRationale(), "  , Random");
        assertEquals(groupings2.size(), 2);
        assertEquals(groupings2.get(0).getSize(),2);
        assertEquals(groupings2.get(1).getSize(),3);
        assertEquals(groupings2.get(1).getRationale(), "  , Random  ,Extra Members");

    }




    @Test
    public void conceptTest(){
        //This assumes the suggested concepts by having each student's list of suggested concepts printed out to the screen
        assertNotEquals(researchGraphs, null);
        GroupSuggester sug = new GroupSuggester();

        List<Group> groupings1 = sug.getGroupList(researchGraphs);

//        groups of 2
        List<Suggester> suggesterList = new ArrayList<>();
        suggesterList.add(new ConceptSuggester());

        List<Group> groupings = sug.grouping(groupings1, 2, suggesterList);

        List<String> three = groupings.get(0).getStudentNames();
        assertEquals(groupings.get(0).getRationale(), "  ,Concept: no suggestions");
        assertEquals(three.size(), 3);
        assertEquals(three.get(0),"s4");
        assertEquals(three.get(1),"s5");
        assertEquals(three.get(2),"s1");



        //testing with more than one list of groups
        List<Group> actualGroupings = new ArrayList<>();

        Group group = new Group();
        group.addMember("mia", researchGraphs.get("s1"));
        group.addMember("don", researchGraphs.get("s3"));
        group.addMember("bob", researchGraphs.get("s1"));
        actualGroupings.add(group);

        Group group2 = new Group();
        group2.addMember("kayli", researchGraphs.get("s3"));
        group2.addMember("dan", researchGraphs.get("s5"));
        actualGroupings.add(group2);

        List<Suggester> suggesterList2 = new ArrayList<>();
        suggesterList2.add(new ConceptSuggester());

        List<Group> groupings2 = sug.grouping(actualGroupings, 2, suggesterList2);

        assertEquals(groupings2.size(), 4);
        assertEquals(groupings2.get(0).getSize(),2);
        assertEquals(groupings2.get(0).getRationale(), "  ,Concept: no suggestions");
        assertEquals(groupings2.get(1).getSize(),1);
        assertEquals(groupings2.get(1).getRationale(), "  ,Concept: Booleans");
        assertEquals(groupings2.get(2).getSize(),1);
        assertEquals(groupings2.get(2).getRationale(), "  ,Concept: no suggestions");
        assertEquals(groupings2.get(3).getSize(),1);
        assertEquals(groupings2.get(3).getRationale(), "  ,Concept: Booleans");


    }


    @Test
    public void bucketTest() {
        assertNotEquals(researchGraphs, null);

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

            assertEquals(groupings.size(), 3);
            assertEquals(groupings.get(0).getSize(),0 );
            assertEquals(groupings.get(0).getRationale(), "  ,Bucket: 0 - 50");

            assertEquals(groupings.get(1).getSize(), 2);
            assertEquals(groupings.get(1).getRationale(), "  ,Bucket: 51 - 80");
            assertEquals(groupings.get(1).contains("s2"), true);
            assertEquals(groupings.get(1).contains("s3"),true);

            assertEquals(groupings.get(2).getSize(), 3);
            assertEquals(groupings.get(2).getRationale(), "  ,Bucket: 81 - 100");
            assertEquals(groupings.get(2).contains("s4"), true);
            assertEquals(groupings.get(2).contains("s5"), true);
            assertEquals(groupings.get(2).contains("s1"),true);


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

            assertEquals(groupings2.size(), 6);
            assertEquals(groupings2.get(0).getSize(),0 );
            assertEquals(groupings.get(0).getRationale(), "  ,Bucket: 0 - 50");


            assertEquals(groupings2.get(1).getSize(),3 );
            assertEquals(groupings2.get(1).getRationale(), "  ,Bucket: 51 - 80");
            assertEquals(groupings2.get(1).contains("mia"), true);
            assertEquals(groupings2.get(1).contains("bob"), true);
            assertEquals(groupings2.get(1).contains("alena"), true);

            assertEquals(groupings2.get(2).getSize(),1);
            assertEquals(groupings2.get(2).getRationale(), "  ,Bucket: 81 - 100");
            assertEquals(groupings2.get(2).contains("don"), true);

            assertEquals(groupings2.get(3).getSize(),0);
            assertEquals(groupings2.get(3).getRationale(), "  ,Bucket: 0 - 50");

            assertEquals(groupings2.get(4).getSize(),1);
            assertEquals(groupings2.get(4).getRationale(), "  ,Bucket: 51 - 80");
            assertEquals(groupings2.get(4).contains("dan"), true);

            assertEquals(groupings2.get(5).getSize(),1);
            assertEquals(groupings2.get(5).getRationale(), "  ,Bucket: 81 - 100");
            assertEquals(groupings2.get(5).contains("kayli"), true);
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

            List<Group> actualGroupings = new ArrayList<>();
            Group group = new Group();
            group.addMember("mia", researchGraphs.get("s3"));
            group.addMember("don", researchGraphs.get("s4"));
            group.addMember("bob", researchGraphs.get("s2"));
            group.addMember("alena", researchGraphs.get("s3"));
            group.addMember("kayli", researchGraphs.get("s5"));
            group.addMember("dan", researchGraphs.get("s2"));
            actualGroupings.add(group);

            List<Suggester> suggesterList = new ArrayList<>();
            suggesterList.add(new BucketSuggester(ranges));
            suggesterList.add(new ConceptSuggester());
            suggesterList.add(new BySizeSuggester(2, false));

            List<Group> groupings1 = sug.grouping(actualGroupings, 2, suggesterList);

            assertEquals(groupings1.size(), 3);
            assertEquals(groupings1.get(0).getSize(), 2);
            assertEquals(groupings1.get(0).getRationale(), "    ,Bucket: 51 - 80 ,Concept: Booleans ,By Size: 2");
            assertEquals(groupings1.get(1).getSize(), 2);
            assertEquals(groupings1.get(1).getRationale(), "    ,Bucket: 51 - 80 ,Concept: Booleans ,By Size: 2");
            assertEquals(groupings1.get(2).getSize(), 2);
            assertEquals(groupings1.get(2).getRationale(), "    ,Bucket: 81 - 100 ,Concept: no suggestions ,By Size: 2");


        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
