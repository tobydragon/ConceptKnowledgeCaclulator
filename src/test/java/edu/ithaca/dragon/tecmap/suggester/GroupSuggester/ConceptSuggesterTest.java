package edu.ithaca.dragon.tecmap.suggester.GroupSuggester;

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

public class ConceptSuggesterTest {

    private Map<String, ConceptGraph> researchGraphs;

    @BeforeEach
    public void setup() throws IOException {
        ConceptGraph graph1 = new ConceptGraph(ConceptGraphRecord.buildFromJson(Settings.TEST_RESOURCE_DIR + "ManuallyCreated/researchConceptGraph.json"));
        List<AssessmentItemResponse> assessmentItemResponses1 = AssessmentItemResponse.createAssessmentItemResponses(Arrays.asList(Settings.TEST_RESOURCE_DIR + "ManuallyCreated/researchAssessment2.csv"));
        List<LearningResourceRecord> links1 = LearningResourceRecord.createLearningResourceRecordsFromJsonFiles(Arrays.asList(Settings.TEST_RESOURCE_DIR + "ManuallyCreated/researchResource2.json"));
        graph1.addLearningResourcesFromRecords(links1);
        researchGraphs = new CohortConceptGraphs(graph1, assessmentItemResponses1).getUserToGraph();
    }

    @Test
    public void suggestGroupTest(){
        List<Group> list = GroupSuggester.getGroupList(researchGraphs);

        List<Group> actualGroupings = new ArrayList<>();

        Group group = new Group();
        group.addMember("mia", researchGraphs.get("s1"));
        group.addMember("don", researchGraphs.get("s3"));
        group.addMember("bob", researchGraphs.get("s1"));
        group.addMember("kayli", researchGraphs.get("s3"));
        group.addMember("dan", researchGraphs.get("s5"));
        actualGroupings.add(group);

        ConceptSuggester sug  = new ConceptSuggester();
        List<Group> groupings2 = sug.suggestGroup(actualGroupings.get(0), new Group());

        assertEquals(groupings2.size(), 2);

        assertEquals(groupings2.get(0).getSize(),2);
        assertEquals(groupings2.get(0).getStudentNames().get(0),"kayli");
        assertEquals(groupings2.get(0).getStudentNames().get(1),"don");

        assertEquals(groupings2.get(1).getSize(),3);
        assertEquals(groupings2.get(1).getStudentNames().get(0),"dan");
        assertEquals(groupings2.get(1).getStudentNames().get(1),"mia");
        assertEquals(groupings2.get(1).getStudentNames().get(2),"bob");

        //        groups of 2
        List<Group> groupings = sug.suggestGroup(list.get(0), new Group());

        assertEquals(groupings.get(0).getSize(),2);
        List<String> two = groupings.get(0).getStudentNames();
        assertEquals(two.get(0),"s3");
        assertEquals(two.get(1),"s2");
        assertEquals(groupings.get(0).getRationale(), "  ,Concept: For Loops");

        assertEquals(groupings.get(1).getSize(), 3);
        List<String> three = groupings.get(1).getStudentNames();
        assertEquals(three.get(0),"s4");
        assertEquals(three.get(1),"s5");
        assertEquals(three.get(2),"s1");
        assertEquals(groupings.get(1).getRationale(), "  ,Concept: no suggestions");
    }

    @Test
    public void conceptSuggesterTest(){
        //This assumes the suggested concepts by having each student's list of suggested concepts printed out to the screen
        assertNotEquals(researchGraphs, null);
        GroupSuggester sug = new GroupSuggester();

        List<Group> groupings1 = sug.getGroupList(researchGraphs);

//        groups of 2
        List<Suggester> suggesterList = new ArrayList<>();
        suggesterList.add(new ConceptSuggester());

        List<Group> groupings = sug.grouping(groupings1, 2, suggesterList);

        List<String> group0 = groupings.get(0).getStudentNames();
        assertEquals(groupings.get(0).getRationale(), "  ,Concept: For Loops");
        assertEquals(group0.size(), 2);
        assertEquals(group0.get(0),"s3");
        assertEquals(group0.get(1),"s2");

        List<String> group1 = groupings.get(1).getStudentNames();
        assertEquals(groupings.get(1).getRationale(), "  ,Concept: no suggestions");
        assertEquals(group1.size(), 3);
        assertEquals(group1.get(0),"s4");
        assertEquals(group1.get(1),"s5");
        assertEquals(group1.get(2),"s1");
    }


    @Test
    void conceptSuggesterMoreThan1GroupTest() {
        //        testing with more than one list of groups
        GroupSuggester sug = new GroupSuggester();
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

        assertEquals(groupings2.get(0).getSize(),1);
        List<String> newGroup0 = groupings2.get(0).getStudentNames();
        assertEquals(newGroup0.get(0),"don");
        assertEquals(groupings2.get(0).getRationale(), "  ,Concept: For Loops");

        assertEquals(groupings2.get(1).getSize(),2);
        List<String> newGroup1 = groupings2.get(1).getStudentNames();
        assertEquals(newGroup1.get(0),"mia");
        assertEquals(newGroup1.get(1),"bob");
        assertEquals(groupings2.get(1).getRationale(), "  ,Concept: no suggestions");

        assertEquals(groupings2.get(2).getSize(),1);
        List<String> newGroup2 = groupings2.get(2).getStudentNames();
        assertEquals(newGroup2.get(0),"kayli");
        assertEquals(groupings2.get(0).getRationale(), "  ,Concept: For Loops");

        assertEquals(groupings2.get(3).getSize(),1);
        List<String> newGroup3 = groupings2.get(3).getStudentNames();
        assertEquals(newGroup3.get(0),"dan");
        assertEquals(groupings2.get(1).getRationale(), "  ,Concept: no suggestions");

    }
}
