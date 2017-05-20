package edu.ithaca.dragonlab.ckc.io;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import edu.ithaca.dragonlab.ckc.util.DataUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// Represents a ConceptGraph when serializing
//also used to denote LearningObjects and their relation to the ConceptGraph ( see @ExampleLearningObjectFactory )
public class ConceptGraphRecord {

	private String name;
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
		this.name = null;
		this.concepts = null;
		this.links = null;
	}

	public ConceptGraphRecord(String name, List<ConceptRecord> nodesIn, List<LinkRecord> linksIn){
	    this.name = name;
		this.concepts = nodesIn;
		this.links = linksIn;
	}

	/**
	 * Finds all root ConceptRecords (ones that have no parents in this graph)
	 * @return a list of references to all root ConceptRecords (could allow side effects)
	 */
	public List<ConceptRecord> findRoots() {
		//Shallow copy
		List<ConceptRecord> runningTotal = new ArrayList<>(getConcepts());

		for (LinkRecord link : links) {
			for(ConceptRecord record : getConcepts()){
				if(record.getId().equals(link.getChild())){
					runningTotal.remove(record);
				}
			}
		}
		return runningTotal;
	}

	public String getName(){
		return name;
	}

	public List<ConceptRecord> getConcepts(){
		return concepts;
	}
	
	public List<LinkRecord> getLinks(){
		return links;
	}

	public void setName(String name){ this.name = name;}
	
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

	public boolean equals(Object other){
		if(other == null){
			return false;
		}
		if(!ConceptGraphRecord.class.isAssignableFrom(other.getClass())){
			return false;
		}
		ConceptGraphRecord otherGraphRecord = (ConceptGraphRecord) other;
		if (this.name.equals(otherGraphRecord.name)) {
			if (this.concepts.size() == otherGraphRecord.concepts.size()) {
				for (int i = 0; i < this.concepts.size(); i++) {
					if (!this.concepts.get(i).equals(otherGraphRecord.concepts.get(i))) {
						return false;
					}
				}
				if (this.links.size() == otherGraphRecord.links.size()) {
					for (int i = 0; i < this.links.size(); i++) {
						if (!this.links.get(i).equals(otherGraphRecord.links.get(i))) {
							return false;
						}
					}
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		}
		else {
			return false;
		}
	}
}
