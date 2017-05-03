package edu.ithaca.dragonlab.ckc.io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ithaca.dragonlab.ckc.conceptgraph.ConceptNode;

// Represents a ConceptGraph when serializing
//also used to denote LearningObjects and their relation to the ConceptGraph ( see @ExampleLearningObjectFactory )
public class ConceptGraphRecordOld {
	
	private List<ConceptNode> nodes;
	private List<LinkRecord> links;
	
	public static ConceptGraphRecordOld buildfromJson(String filename) throws IOException{
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		ConceptGraphRecordOld lists = mapper.readValue(new File(filename), ConceptGraphRecordOld.class);
		return lists;
	}
	
	public void writeToJson(String filename) throws IOException{
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(new File(filename), this);
		
	}

	//needed for json creation
	public ConceptGraphRecordOld(){
		this.nodes = new ArrayList<>();
		this.links = new ArrayList<>();
	}
	
	public ConceptGraphRecordOld(List<ConceptNode> nodesIn, List<LinkRecord> linksIn){
		this.nodes = nodesIn;
		this.links = linksIn;
	}
	
	public List<ConceptNode> getNodes(){
		return nodes;
	}
	
	public List<LinkRecord> getLinks(){
		return links;
	}
	
	public void setNodes(List<ConceptNode> nodesIn){
		this.nodes = nodesIn;
	}
	
	public void setLinks(List<LinkRecord> linksIn){
		this.links = linksIn;
	}
	
	public String toString(){
		
		String linkString = "";
		String nodeString = "";
		String combinedString;
		
		for(LinkRecord link : links){
			linkString += "Parent: " + link.getParent() + "\n" + 
					"Child: " + link.getChild() + "\n\n";
		}
		
		for(ConceptNode node : nodes){
			nodeString += node.getID()+ "\n";
		}
		
		combinedString = linkString + "\n\n\n" + nodeString;

		return combinedString;
	}
	
//	public Map<String, ConceptNode> buildNodeMap(){
//		Map<String, ConceptNode> nodeMap = new HashMap<>();
//		for(ConceptNode node : nodes){
//			nodeMap.put(node.getID(), node);
//		}
//		return nodeMap;
//	}
}
