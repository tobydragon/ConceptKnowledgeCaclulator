package edu.ithaca.dragonlab.ckc.conceptgraph;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ithaca.dragonlab.ckc.io.LearningObjectLinkRecord;
import edu.ithaca.dragonlab.ckc.learningobject.LearningObjectResponse;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupConceptGraphs {
	ConceptGraph averageGraph;
	Map<String, ConceptGraph> userToGraph;
	List<NamedGraph> allNamedGraphs;

	/**
	 * Takes a graph (including LearningObjectLinks)
	 * @param structureGraph
	 * @param summaries
	 */
	public GroupConceptGraphs(ConceptGraph structureGraph, List<LearningObjectResponse> summaries){
		
		averageGraph = new ConceptGraph(structureGraph, "Average Graph");
		averageGraph.addSummariesToGraph(summaries);
		averageGraph.calcKnowledgeEstimates();
		//averageGraph.calcPredictedScores();
		
		Map<String, List<LearningObjectResponse>> userIdToResponses = LearningObjectResponse.getUserResponseMap(summaries);
		userToGraph = new HashMap<>();
		
		for(String user: userIdToResponses.keySet()){
			ConceptGraph structureCopy = new ConceptGraph(structureGraph, user);
			structureCopy.addSummariesToGraph(userIdToResponses.get(user));
			structureCopy.calcKnowledgeEstimates();
			structureCopy.calcPredictedScores();
			userToGraph.put(user, structureCopy);
		}
		calcDistanceFromAvg();
		buildNamedGraph();
	}
	
	public GroupConceptGraphs(String filename,ConceptGraph structureGraph,List<LearningObjectResponse> summaries){
		this(structureGraph,summaries);
		namedGraphToJSON(filename);
	}
	
	public void calcDistanceFromAvg(){
		//TODO: This should be owkring on nodes, not records
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
	
	public List<NamedGraph> getAllNamedGraphs(){
		return this.allNamedGraphs;
	}
	
	public int userCount(){
		int count = 0;
		for(String user: userToGraph.keySet()){
			count++;
		}
		return count;
	}
	
	public ConceptGraph getUserGraph(String user){
		return userToGraph.get(user);
	}
	
	@JsonIgnore
	public List<ConceptGraph> getAllGraphs(){
		List<ConceptGraph> allGraphs = new ArrayList<ConceptGraph>();
		for(String user: userToGraph.keySet()){
			allGraphs.add(userToGraph.get(user));
		}
		
		return allGraphs;
	}
	
	public Map<String, ConceptGraph> getUserToGraphMap(){
		return userToGraph;
	}
	
	public void buildNamedGraph(){
		allNamedGraphs = new ArrayList<NamedGraph>();
		NamedGraph avgGraph = new NamedGraph("Average Graph", averageGraph);
		allNamedGraphs.add(avgGraph);
		for(String user: userToGraph.keySet()){
			
			NamedGraph temp = new NamedGraph(user, getUserGraph(user));
			allNamedGraphs.add(temp);
		}
	}
	
	public void namedGraphToJSON(String filename){
		ObjectMapper mapper = new ObjectMapper();
		
		try {
			//writes JSON to file
			mapper.writeValue(new File(filename+".json"), allNamedGraphs);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public ConceptGraph getAvgGraph() {
		return averageGraph;
	}
}
