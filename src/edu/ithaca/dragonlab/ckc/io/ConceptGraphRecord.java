package edu.ithaca.dragonlab.ckc.io;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// Represents a ConceptGraph when serializing
//also used to denote LearningObjects and their relation to the ConceptGraph ( see @ExampleLearningObjectFactory )
public class ConceptGraphRecord {

	private List<ConceptRecord> concepts;
	private List<LinkRecord> links;

	public static ConceptGraphRecord buildFromJson(String filename) throws IOException{
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		ConceptGraphRecord lists = mapper.readValue(new File(filename), ConceptGraphRecord.class);
		return lists;
	}

	public void writeToJson(String filename) throws IOException{
		ObjectMapper mapper = new ObjectMapper();
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
		mapper.writeValue(new File(filename), this);

	}

	//needed for json creation
	public ConceptGraphRecord(){
		this.concepts = new ArrayList<>();
		this.links = new ArrayList<>();
	}

	public ConceptGraphRecord(List<ConceptRecord> nodesIn, List<LinkRecord> linksIn){
		this.concepts = nodesIn;
		this.links = linksIn;
	}
	
	public List<ConceptRecord> getConcepts(){
		return concepts;
	}
	
	public List<LinkRecord> getLinks(){
		return links;
	}
	
	public void setConcepts(List<ConceptRecord> nodesIn){
		this.concepts = nodesIn;
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
		
		for(ConceptRecord node : concepts){
			nodeString += node.getId()+ "\n";
		}
		
		combinedString = linkString + "\n\n\n" + nodeString;

		return combinedString;
	}
}
