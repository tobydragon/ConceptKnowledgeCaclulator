package edu.ithaca.dragon.tecmap.conceptgraph;

import edu.ithaca.dragon.tecmap.Settings;
import edu.ithaca.dragon.tecmap.io.*;
import edu.ithaca.dragon.tecmap.learningobject.ExampleLearningObjectLinkRecordFactory;
import edu.ithaca.dragon.tecmap.learningobject.ExampleLearningObjectResponseFactory;
import edu.ithaca.dragon.tecmap.learningobject.LearningObject;
import edu.ithaca.dragon.tecmap.learningobject.LearningObjectResponse;
import edu.ithaca.dragon.tecmap.util.DataUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Collection;
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

        Assert.assertEquals(6,group.getAvgGraph().getLearningObjectMap().size());
		Assert.assertEquals(6,group.getUserGraph("student1").getLearningObjectMap().size());
        Assert.assertEquals(6,group.getUserGraph("student2").getLearningObjectMap().size());
        Assert.assertEquals(6,group.getUserGraph("student3").getLearningObjectMap().size());

        Assert.assertEquals(3,group.getAvgGraph().getLearningObjectMap().get("Q1").getResponses().size());
        Assert.assertEquals(2,group.getAvgGraph().getLearningObjectMap().get("Q5").getResponses().size());

        Assert.assertEquals(1,group.getUserGraph("student1").getLearningObjectMap().get("Q1").getResponses().size());
        Assert.assertEquals(0,group.getUserGraph("student3").getLearningObjectMap().get("Q5").getResponses().size());
    }

    //particular attention to what is copied in LearningObjects
    @Test
    public void testCreation(){
        ConceptGraph graph = ExampleConceptGraphFactory.makeSimpleStructureAndLearningObjects();
        CohortConceptGraphs group = new CohortConceptGraphs(graph, ExampleLearningObjectResponseFactory.makeSimpleResponses());

        //test that learningObjectResponses don't get mixed between users
        for (Map.Entry<String, ConceptGraph> entry : group.getUserToGraph().entrySet()){
            for (LearningObject learningObject: entry.getValue().getLearningObjectMap().values()){
                for (LearningObjectResponse response : learningObject.getResponses()){
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
                    LearningResourceRecord.buildListFromJson(Settings.TEST_RESOURCE_DIR + "ManuallyCreated/basicRealisticResource.json" ));
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
                    LearningResourceRecord.buildListFromJson(Settings.TEST_RESOURCE_DIR + "ManuallyCreated/basicRealisticResource.json"));
            CohortConceptGraphs gcg = new CohortConceptGraphs(graph,csvReader.getManualGradedResponses());
            ConceptGraph testGraph = gcg.getAvgGraph();

            ConceptNode groupNode = testGraph.findNodeById("Boolean");
            Assert.assertSame(testGraph.getLearningObjectMap().get("Q9"), groupNode.getLearningObjectMap().get("Q9"));
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
                    LearningResourceRecord.buildListFromJson(Settings.TEST_RESOURCE_DIR + "ManuallyCreated/basicRealisticResource.json"),
                    csvReader.getManualGradedResponses());
            singleGraph.calcKnowledgeEstimates();

            ConceptGraph graph = new ConceptGraph(ConceptGraphRecord.buildFromJson(Settings.TEST_RESOURCE_DIR + "ManuallyCreated/basicRealisticConceptGraph.json"),
                    LearningResourceRecord.buildListFromJson(Settings.TEST_RESOURCE_DIR + "ManuallyCreated/basicRealisticResource.json"));
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


}



	