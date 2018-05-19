package edu.ithaca.dragon.tecmap.conceptgraph;

import edu.ithaca.dragon.tecmap.io.record.CohortConceptGraphsRecord;
import edu.ithaca.dragon.tecmap.io.record.ConceptGraphRecord;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItemResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CohortConceptGraphs {
	public Logger logger = LogManager.getLogger(this.getClass());

	ConceptGraph averageGraph;
	Map<String, ConceptGraph> userToGraph;

	/**
	 * Takes a graph (including LearningObjectLinks)
	 * @param structureGraph
	 * @param summaries
	 */
	public CohortConceptGraphs(ConceptGraph structureGraph, List<AssessmentItemResponse> summaries){
		if (structureGraph.responsesCount()>0){
			logger.warn("StructureGraph contains responses that will be copied to all CohortGraphs");
		}
		averageGraph = new ConceptGraph(structureGraph, "Average Graph");
//		averageGraph.calcPredictedScores();

		Map<String, List<AssessmentItemResponse>> userIdToResponses = AssessmentItemResponse.getUserResponseMap(summaries);
		userToGraph = new HashMap<>();

		for(String user: userIdToResponses.keySet()){
			ConceptGraph structureCopy = new ConceptGraph(structureGraph, user);
			structureCopy.addLearningObjectResponses(userIdToResponses.get(user));
			structureCopy.calcDataImportance();
			structureCopy.calcKnowledgeEstimates();
			structureCopy.calcPredictedScores();
			userToGraph.put(user, structureCopy);
		}

		//Add data to average Graph after all other graphs have been created.
		averageGraph.addLearningObjectResponses(summaries);
		averageGraph.calcKnowledgeEstimates();

		calcDistanceFromAvg();

	}
	
	public CohortConceptGraphs(String filename, ConceptGraph structureGraph, List<AssessmentItemResponse> summaries){
		this(structureGraph,summaries);
		//TODO: call CohortConceptGraphsRecord
	}
	
	public void calcDistanceFromAvg(){
		for (ConceptGraph graph : userToGraph.values()){
			graph.setDistFromAvg(averageGraph);
		}
	}

	public CohortConceptGraphsRecord buildCohortConceptGraphsRecord(){
		List<ConceptGraphRecord> graphRecords = new ArrayList<>();
		graphRecords.add(averageGraph.buildConceptGraphRecord());
		for (ConceptGraph graph : userToGraph.values()){
			graphRecords.add(graph.buildConceptGraphRecord());
		}
		return new CohortConceptGraphsRecord(graphRecords);
	}

	public CohortConceptGraphsRecord buildCohortConceptTreeRecord(){
		List<ConceptGraphRecord> graphRecords = new ArrayList<>();
		ConceptGraph tree = TreeConverter.makeTreeCopy(averageGraph);
		graphRecords.add(tree.buildConceptGraphRecord());
		for (ConceptGraph graph : userToGraph.values()){
			graphRecords.add(TreeConverter.makeTreeCopy(graph).buildConceptGraphRecord());
		}
		return new CohortConceptGraphsRecord(graphRecords);
	}

	public int getUserCount(){
		return userToGraph.size();
	}
	
	public ConceptGraph getUserGraph(String user){
		return userToGraph.get(user);
	}

	public ConceptGraph getAvgGraph() {
		return averageGraph;
	}

	public Map<String, ConceptGraph> getUserToGraph() { return userToGraph; }


}
