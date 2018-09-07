package edu.ithaca.dragon.tecmap.conceptgraph;

import edu.ithaca.dragon.tecmap.Settings;
import edu.ithaca.dragon.tecmap.io.reader.CSVReader;
import edu.ithaca.dragon.tecmap.io.reader.SakaiReader;
import edu.ithaca.dragon.tecmap.io.record.CohortConceptGraphsRecord;
import edu.ithaca.dragon.tecmap.io.record.ConceptGraphRecord;
import edu.ithaca.dragon.tecmap.io.record.ConceptRecord;
import edu.ithaca.dragon.tecmap.io.record.LearningResourceRecord;
import edu.ithaca.dragon.tecmap.learningresource.ExampleLearningObjectLinkRecordFactory;
import edu.ithaca.dragon.tecmap.learningresource.ExampleLearningObjectResponseFactory;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItem;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItemResponse;
import edu.ithaca.dragon.tecmap.util.DataUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class CohortConceptGraphsTest {

	static Logger logger = LogManager.getLogger(ConceptGraphTest.class);

	@Test
	public void userCountTest(){
		ConceptGraph graph = ExampleConceptGraphFactory.makeSimpleStructure();
		CohortConceptGraphs group = new CohortConceptGraphs(graph, ExampleLearningObjectResponseFactory.makeSimpleResponses());
		Assert.assertEquals(3,group.getUserCount());
	}

	@Test
	public void addSummariesTest(){
        ConceptGraph graph = new ConceptGraph(ExampleConceptGraphRecordFactory.makeSimple(), ExampleLearningObjectLinkRecordFactory.makeSimpleLOLRecords());
        CohortConceptGraphs group = new CohortConceptGraphs(graph, ExampleLearningObjectResponseFactory.makeSimpleResponses());

        Assert.assertEquals(6,group.getAvgGraph().getAssessmentItemMap().size());
		Assert.assertEquals(6,group.getUserGraph("student1").getAssessmentItemMap().size());
        Assert.assertEquals(6,group.getUserGraph("student2").getAssessmentItemMap().size());
        Assert.assertEquals(6,group.getUserGraph("student3").getAssessmentItemMap().size());

        Assert.assertEquals(3,group.getAvgGraph().getAssessmentItemMap().get("Q1").getResponses().size());
        Assert.assertEquals(2,group.getAvgGraph().getAssessmentItemMap().get("Q5").getResponses().size());

        Assert.assertEquals(1,group.getUserGraph("student1").getAssessmentItemMap().get("Q1").getResponses().size());
        Assert.assertEquals(0,group.getUserGraph("student3").getAssessmentItemMap().get("Q5").getResponses().size());
    }

    //particular attention to what is copied in LearningObjects
    @Test
    public void testCreation(){
        ConceptGraph graph = ExampleConceptGraphFactory.makeSimpleStructureAndLearningObjects();
        CohortConceptGraphs group = new CohortConceptGraphs(graph, ExampleLearningObjectResponseFactory.makeSimpleResponses());

        //test that learningObjectResponses don't get mixed between users
        for (Map.Entry<String, ConceptGraph> entry : group.getUserToGraph().entrySet()){
            for (AssessmentItem assessmentItem : entry.getValue().getAssessmentItemMap().values()){
                for (AssessmentItemResponse response : assessmentItem.getResponses()){
                    Assert.assertEquals(entry.getKey(), response.getUserId());
                }
            }
        }

    }

    @Test
    public void calcKnowledgeEstimateTest() {
        ConceptGraph graph = ExampleConceptGraphFactory.makeSimpleStructureAndLearningObjects();
        CohortConceptGraphs group = new CohortConceptGraphs(graph, ExampleLearningObjectResponseFactory.makeSimpleResponses());

        Assert.assertEquals(0.5, group.getAvgGraph().findNodeById("C").getKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);
        Assert.assertEquals(0.6875, group.getAvgGraph().findNodeById("B").getKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);
        Assert.assertEquals(0.6158, group.getAvgGraph().findNodeById("A").getKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);

        Assert.assertEquals(1, group.getUserGraph("student1").findNodeById("A").getKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);
        Assert.assertEquals(1, group.getUserGraph("student1").findNodeById("B").getKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);
        Assert.assertEquals(1, group.getUserGraph("student1").findNodeById("C").getKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);

        Assert.assertEquals(0.4, group.getUserGraph("student2").findNodeById("A").getKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);
        Assert.assertEquals(0.5, group.getUserGraph("student2").findNodeById("B").getKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);
        Assert.assertEquals(0.25, group.getUserGraph("student2").findNodeById("C").getKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);

        Assert.assertEquals(0.333, group.getUserGraph("student3").findNodeById("A").getKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);
        Assert.assertEquals(0.5, group.getUserGraph("student3").findNodeById("B").getKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);
        Assert.assertEquals(0, group.getUserGraph("student3").findNodeById("C").getKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);
    }

	@Test
	public void calcDistFromAvgTest(){
        ConceptGraph graph = ExampleConceptGraphFactory.makeSimpleStructureAndLearningObjects();
        CohortConceptGraphs group = new CohortConceptGraphs(graph, ExampleLearningObjectResponseFactory.makeSimpleResponses());

		ConceptGraph user = group.getUserGraph("student1");
		Assert.assertEquals(0.3842, user.findNodeById("A").getKnowledgeDistanceFromAvg(), DataUtil.OK_FLOAT_MARGIN);
        Assert.assertEquals(0.3125, user.findNodeById("B").getKnowledgeDistanceFromAvg(),DataUtil.OK_FLOAT_MARGIN);
        Assert.assertEquals(0.5, user.findNodeById("C").getKnowledgeDistanceFromAvg(),DataUtil.OK_FLOAT_MARGIN);

        user = group.getUserGraph("student2");
        Assert.assertEquals(-0.2158, user.findNodeById("A").getKnowledgeDistanceFromAvg(), DataUtil.OK_FLOAT_MARGIN);
        Assert.assertEquals(-0.1875, user.findNodeById("B").getKnowledgeDistanceFromAvg(),DataUtil.OK_FLOAT_MARGIN);
        Assert.assertEquals(-0.25, user.findNodeById("C").getKnowledgeDistanceFromAvg(),DataUtil.OK_FLOAT_MARGIN);

        user = group.getUserGraph("student3");
        Assert.assertEquals(-0.2825, user.findNodeById("A").getKnowledgeDistanceFromAvg(), DataUtil.OK_FLOAT_MARGIN);
        Assert.assertEquals(-0.1875, user.findNodeById("B").getKnowledgeDistanceFromAvg(),DataUtil.OK_FLOAT_MARGIN);
        Assert.assertEquals(-0.5, user.findNodeById("C").getKnowledgeDistanceFromAvg(),DataUtil.OK_FLOAT_MARGIN);
	}

	@Test
    public void buildCohortConceptTreeRecordTest() {
        ConceptGraph graph = ExampleConceptGraphFactory.makeSimpleStructureAndLearningObjects();
        CohortConceptGraphs group = new CohortConceptGraphs(graph, ExampleLearningObjectResponseFactory.makeSimpleResponses());

        CohortConceptGraphsRecord record = group.buildCohortConceptTreeRecord();

    }


    private boolean strIsSubstringOfSomeEntry(String str, Collection<ConceptRecord> list){
	    for (ConceptRecord toCheck : list){
	        if (toCheck.getId().contains(str)){
	            return true;
            }
        }
        return false;
    }

    private void matchingIdsForTreeCopies(ConceptGraph orig, ConceptGraphRecord treeCopy){
        Collection<ConceptRecord> treeCopyIds = treeCopy.getConcepts();
        Collection<String> origIds = orig.getAllNodeIds();

        for (String origId : origIds){
            if ( ! strIsSubstringOfSomeEntry(origId, treeCopyIds)){
                Assert.fail("Tree copy does not contain any matching nodeIds for studentKnowledgeEstimates ID: " + origId +" - Not chekcing all may be missing more...");
            }
        }
    }

    @Test
    public void buildCohortConceptTreeRecordComplexTest() {
        try{
        CSVReader csvReader = new SakaiReader(Settings.TEST_RESOURCE_DIR + "ManuallyCreated/basicRealisticAssessment.csv");
            ConceptGraph  structure = new ConceptGraph(ConceptGraphRecord.buildFromJson(Settings.TEST_RESOURCE_DIR + "ManuallyCreated/basicRealisticConceptGraph.json"),
                    LearningResourceRecord.createLearningResourceRecordsFromJsonFile(Settings.TEST_RESOURCE_DIR + "ManuallyCreated/basicRealisticResource.json" ));
            CohortConceptGraphs group = new CohortConceptGraphs(structure, csvReader.getManualGradedResponses());

            CohortConceptGraphsRecord record = group.buildCohortConceptTreeRecord();
            matchingIdsForTreeCopies(group.getAvgGraph(), record.getGraphRecords().get(0));

        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail();
        }


    }


	//Written because bug came up where a learningObject in graph's map was different than that in node's map
	@Test
    public void onlyOneLearningObject(){
        try {
            CSVReader csvReader = new SakaiReader(Settings.TEST_RESOURCE_DIR + "ManuallyCreated/basicRealisticAssessment.csv");
            ConceptGraph graph = new ConceptGraph(ConceptGraphRecord.buildFromJson(Settings.TEST_RESOURCE_DIR + "ManuallyCreated/basicRealisticConceptGraph.json"),
                    LearningResourceRecord.createLearningResourceRecordsFromJsonFile(Settings.TEST_RESOURCE_DIR + "ManuallyCreated/basicRealisticResource.json"));
            CohortConceptGraphs gcg = new CohortConceptGraphs(graph,csvReader.getManualGradedResponses());
            ConceptGraph testGraph = gcg.getAvgGraph();

            ConceptNode groupNode = testGraph.findNodeById("Boolean");
            Assert.assertSame(testGraph.getAssessmentItemMap().get("Q9"), groupNode.getAssessmentItemMap().get("Q9"));
        }
        catch (Exception e){
            e.printStackTrace();
            Assert.fail();
        }

    }
	//checks for bug where the same data in a single graph and a cohort graph produced different results
	@Test
    public void calcKnowledgeEstimateSameInCohortAndConceptGraphsTest(){
        try {
            CSVReader csvReader = new SakaiReader(Settings.TEST_RESOURCE_DIR + "ManuallyCreated/basicRealisticAssessment.csv");

            ConceptGraph singleGraph = new ConceptGraph(ConceptGraphRecord.buildFromJson(Settings.TEST_RESOURCE_DIR + "ManuallyCreated/basicRealisticConceptGraph.json"),
                    LearningResourceRecord.createLearningResourceRecordsFromJsonFile(Settings.TEST_RESOURCE_DIR + "ManuallyCreated/basicRealisticResource.json"),
                    csvReader.getManualGradedResponses());
            singleGraph.calcKnowledgeEstimates();

            ConceptGraph graph = new ConceptGraph(ConceptGraphRecord.buildFromJson(Settings.TEST_RESOURCE_DIR + "ManuallyCreated/basicRealisticConceptGraph.json"),
                    LearningResourceRecord.createLearningResourceRecordsFromJsonFile(Settings.TEST_RESOURCE_DIR + "ManuallyCreated/basicRealisticResource.json"));
            CohortConceptGraphs gcg = new CohortConceptGraphs(graph,csvReader.getManualGradedResponses());
            ConceptGraph testGraph = gcg.getAvgGraph();

            ConceptNode singleNode = singleGraph.findNodeById("Boolean");
            ConceptNode groupNode = testGraph.findNodeById("Boolean");
            Assert.assertEquals(singleNode.getDataImportance(), groupNode.getDataImportance(), DataUtil.OK_FLOAT_MARGIN);
            Assert.assertEquals(singleNode.getKnowledgeEstimate(), groupNode.getKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);

        }
        catch (Exception e){
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void calcTotalKnowledgeEstimateTest() throws IOException {
        ConceptGraph graph = new ConceptGraph(ConceptGraphRecord.buildFromJson(Settings.TEST_RESOURCE_DIR + "ManuallyCreated/simpleConceptGraphTest.json"));
        List<AssessmentItemResponse> assessmentItemResponses = AssessmentItemResponse.createAssessmentItemResponses(Arrays.asList(Settings.TEST_RESOURCE_DIR + "ManuallyCreated/simpleAssessmentTest.csv"));
        List<LearningResourceRecord> links = LearningResourceRecord.createLearningResourceRecordsFromJsonFiles(Arrays.asList(Settings.TEST_RESOURCE_DIR + "ManuallyCreated/simpleResourceTest.json"));
        graph.addLearningResourcesFromRecords(links);
        CohortConceptGraphs graphs = new CohortConceptGraphs(graph, assessmentItemResponses);

        ConceptGraph gr = graphs.getUserGraph("s1");
        Assert.assertEquals(1.7999999999999998, gr.calcTotalKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);

        ConceptGraph gr2 = graphs.getUserGraph("s2");
        Assert.assertEquals(1.7999999999999998, gr2.calcTotalKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);


        ConceptGraph gr3 = graphs.getUserGraph("s3");
        Assert.assertEquals(1.7999999999999998, gr3.calcTotalKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);

        ConceptGraph gr4 = graphs.getUserGraph("s4");
        Assert.assertEquals(1.7999999999999998, gr4.calcTotalKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);

    }




    @Test
    public void calcTotalKnowledgeEstimate2() throws IOException {
        ConceptGraph graph = new ConceptGraph(ConceptGraphRecord.buildFromJson(Settings.TEST_RESOURCE_DIR + "ManuallyCreated/researchConceptGraph.json"));
        List<AssessmentItemResponse> assessmentItemResponses = AssessmentItemResponse.createAssessmentItemResponses(Arrays.asList(Settings.TEST_RESOURCE_DIR + "ManuallyCreated/researchAssessment1.csv"));
        List<LearningResourceRecord> links = LearningResourceRecord.createLearningResourceRecordsFromJsonFiles(Arrays.asList(Settings.TEST_RESOURCE_DIR + "ManuallyCreated/researchResource1.json"));
        graph.addLearningResourcesFromRecords(links);
        CohortConceptGraphs graphs = new CohortConceptGraphs(graph, assessmentItemResponses);

        ConceptGraph gr = graphs.getUserGraph("s1");
        Assert.assertEquals(13.580, gr.calcTotalKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);

        ConceptGraph gr2 = graphs.getUserGraph("s2");
        Assert.assertEquals(11.477, gr2.calcTotalKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);


        ConceptGraph gr3 = graphs.getUserGraph("s3");
        Assert.assertEquals(12.8326, gr3.calcTotalKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);

        ConceptGraph gr4 = graphs.getUserGraph("s4");
        Assert.assertEquals(1.0, gr4.calcTotalKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);


        ConceptGraph gr5 = graphs.getUserGraph("s5");
        Assert.assertEquals(12.872, gr5.calcTotalKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);


        ConceptGraph gr6 = graphs.getUserGraph("s6");
        Assert.assertEquals(12.7884, gr6.calcTotalKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);


        ConceptGraph gr7 = graphs.getUserGraph("s7");
        Assert.assertEquals(10.953, gr7.calcTotalKnowledgeEstimate(), DataUtil.OK_FLOAT_MARGIN);


    }

}



	