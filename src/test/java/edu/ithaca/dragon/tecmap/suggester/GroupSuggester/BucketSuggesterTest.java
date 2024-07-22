package edu.ithaca.dragon.tecmap.suggester.GroupSuggester;

import com.opencsv.exceptions.CsvException;
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

import static org.junit.jupiter.api.Assertions.*;

public class BucketSuggesterTest {

    private Map<String, ConceptGraph> graphs;

    @BeforeEach
    public void setup() throws IOException, CsvException {
        ConceptGraph graph = new ConceptGraph(ConceptGraphRecord.buildFromJson(Settings.TEST_RESOURCE_DIR + "ManuallyCreated/researchConceptGraph.json"));
        List<AssessmentItemResponse> assessmentItemResponses = AssessmentItemResponse.createAssessmentItemResponses(Arrays.asList(Settings.TEST_RESOURCE_DIR + "ManuallyCreated/researchAssessment2.csv"));
        List<LearningResourceRecord> links = LearningResourceRecord.createLearningResourceRecordsFromJsonFiles(Arrays.asList(Settings.TEST_RESOURCE_DIR + "ManuallyCreated/researchResource2.json"));
        graph.addLearningResourcesFromRecords(links);
        graphs = new CohortConceptGraphs(graph, assessmentItemResponses).getUserToGraph();
    }

    @Test
    public void bucketTest1() {
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

            assertEquals(groupings.size(), 3);
            assertEquals(groupings.get(0).getSize(), 0);
            assertEquals(groupings.get(0).getRationale(), "  ,Bucket: 0 - 50");

            assertEquals(groupings.get(1).getSize(), 2);
            assertEquals(groupings.get(1).getRationale(), "  ,Bucket: 51 - 80");
            assertEquals(groupings.get(1).contains("s3"), true);
            assertEquals(groupings.get(1).contains("s2"), true);

            assertEquals(groupings.get(2).getSize(), 3);
            assertEquals(groupings.get(2).getRationale(), "  ,Bucket: 81 - 100");
            assertEquals(groupings.get(2).contains("s4"), true);
            assertEquals(groupings.get(2).contains("s5"), true);
            assertEquals(groupings.get(2).contains("s1"), true);

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

    }

    @Test
    public void bucketTest2() {
        assertNotEquals(graphs, null);

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
}
