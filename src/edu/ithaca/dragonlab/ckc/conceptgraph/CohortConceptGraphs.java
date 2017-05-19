package edu.ithaca.dragonlab.ckc.conceptgraph;

import edu.ithaca.dragonlab.ckc.learningobject.LearningObjectResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CohortConceptGraphs {
	ConceptGraph averageGraph;
	Map<String, ConceptGraph> userToGraph;

	/**
	 * Takes a graph (including LearningObjectLinks)
	 * @param structureGraph
	 * @param summaries
	 */
	public CohortConceptGraphs(ConceptGraph structureGraph, List<LearningObjectResponse> summaries){
		
		averageGraph = new ConceptGraph(structureGraph, "Average Graph");
		averageGraph.addSummariesToGraph(summaries);
		averageGraph.calcDataImportance();
		averageGraph.calcKnowledgeEstimates();
		//averageGraph.calcPredictedScores();
		
		Map<String, List<LearningObjectResponse>> userIdToResponses = LearningObjectResponse.getUserResponseMap(summaries);
		userToGraph = new HashMap<>();
		
		for(String user: userIdToResponses.keySet()){
			ConceptGraph structureCopy = new ConceptGraph(structureGraph, user);
			structureCopy.addSummariesToGraph(userIdToResponses.get(user));
			structureCopy.calcDataImportance();
			structureCopy.calcKnowledgeEstimates();
			structureCopy.calcPredictedScores();
			userToGraph.put(user, structureCopy);
		}
		calcDistanceFromAvg();
	}
	
	public CohortConceptGraphs(String filename, ConceptGraph structureGraph, List<LearningObjectResponse> summaries){
		this(structureGraph,summaries);
		//TODO: call CohortConceptGraphsRecord
	}
	
	public void calcDistanceFromAvg(){
		//TODO: This should be working on nodes, not records
//		ConceptGraphRecord avgLinks = averageGraph.buildNodesAndLinks();
//		for(String user: userToGraph.keySet()){
//			ConceptGraphRecord tempLinks = userToGraph.get(user).buildNodesAndLinks();
//
//			for(ConceptRecord tempNode: tempLinks.getConcepts()){
//				for(ConceptRecord avgNode: avgLinks.getConcepts()){
//					if(tempNode.getId().equals(avgNode.getId())){
//						double avgCalc = avgNode.getKnowledgeEstimate();
//						tempNode.calcDistanceFromAvg(avgCalc);
//					}
//				}
//			}
//		}
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
}
