package edu.ithaca.dragonlab.ckc;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class NodesAndIDLinks {
	
	private List<ConceptNode> nodes;
	private List<IDLink> links;
	
	public static NodesAndIDLinks buildfromJson(String filename) throws IOException{
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		NodesAndIDLinks lists = mapper.readValue(new File(filename), NodesAndIDLinks.class);
		return lists;
	}
	
	public void writeToJson(String filename) throws IOException{
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(new File(filename), this);
		
	}
	
	public NodesAndIDLinks(){}
	
	public NodesAndIDLinks(List<ConceptNode> nodesIn, List<IDLink> linksIn){
		this.nodes = nodesIn;
		this.links = linksIn;
	}
	
	public List<ConceptNode> getNodes(){
		return nodes;
	}
	
	public List<IDLink> getLinks(){
		return links;
	}
	
	public void setNodes(List<ConceptNode> nodesIn){
		this.nodes = nodesIn;
	}
	
	public void setLinks(List<IDLink> linksIn){
		this.links = linksIn;
	}
	
	public String toString(){
		
		String linkString = "";
		String nodeString = "";
		String combinedString = "";
		
		for(IDLink link : links){
			linkString += "Parent: " + link.getParent() + "\n" + 
					"Child: " + link.getChild() + "\n\n";
		}
		
		for(ConceptNode node : nodes){
			nodeString += node.getID()+ "\n";
		}
		
		combinedString = linkString + "\n\n\n" + nodeString;

		return combinedString;
	}
	
	public Map<String, ConceptNode> buildNodeMap(){
		Map<String, ConceptNode> nodeMap = new HashMap<String, ConceptNode>();
		for(ConceptNode node : nodes){
			nodeMap.put(node.getID(), node);
		}
		return nodeMap;
	}
}
