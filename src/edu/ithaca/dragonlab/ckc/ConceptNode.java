
package edu.ithaca.dragonlab.ckc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.ithaca.dragonlab.ckc.io.LinkRecord;
import edu.ithaca.dragonlab.ckc.learningobject.LearningObject;

public class ConceptNode {
	private static final String symbol = "-";

	String id; 		//unique to each node
	String label; 	//can be the same as id, or different if you want displayed different
	private double knowledgeEstimate;
	private double knowledgePrediction;
	private double knowledgeDistanceFromAvg;

	Map<String, LearningObject> learningObjectMap;
	List<ConceptNode> children;

	private int numParents; //TODO: remove?

	//need default constructor for jackson
	public ConceptNode() {
		this.id = null;
		this.label = null;
		children = new ArrayList<>();
		learningObjectMap = new HashMap<>();
		numParents = 0;
		knowledgePrediction = 0;
		knowledgeEstimate = 0;
		knowledgeDistanceFromAvg = 0;
	}

	public ConceptNode(String id, String label) {
		this();
		this.id = id;
		this.label = label;
	}

	//use label as ID if there is no label given
	public ConceptNode(String id){
		this(id, id);
	}
	
	public void addChild(ConceptNode child){
		children.add(child);
	}

	public double calcActualComp() {
		if (getChildren().size() == 0) {
			//TODO: calculate from learningObjects considering weights
		} else {
			//TODO: calculate from children and learningObjects
		}
		return 0;
	}

	public void addToNodesAndLinksLists(List<ConceptNode> nodes, List<LinkRecord> links){
		//if I am not in the list
		if(!nodes.contains(this)){
			//add me to the nodes list
			nodes.add(this);
			for(ConceptNode child : this.children){
				//recurse call on children
				child.addToNodesAndLinksLists(nodes,links);
				//add the links between me and my children to link list
				links.add(new LinkRecord(this.getID(),child.getID()));
			}
		}
	}

	public void addLearningObject(LearningObject learningObject) {
			learningObjectMap.put(learningObject.getId(), learningObject);
	}

	//////////////////  TREE CONVERSION  //////////////////////
	public ConceptNode makeTree(HashMap<String, List<String>> multCopies){
		ConceptNode nodeCopy;
		List<String> nodeCopies = multCopies.get(this.getLabel());
		if(nodeCopies == null){
			nodeCopy = new ConceptNode(makeName(this.getLabel()), this.getLabel());
		/*
		nodeCopy.setKnowledgeEstimate(this.knowledgeEstimate);
		nodeCopy.setKnowledgePrediction(this.knowledgePrediction);
		nodeCopy.setKnowledgeDistanceFromAvg(this.knowledgeDistanceFromAvg);
		*/
			nodeCopies = new ArrayList<String>();
			nodeCopies.add(nodeCopy.getID());
			multCopies.put(nodeCopy.getLabel(), nodeCopies);

		}else{
			String prevName = nodeCopies.get(nodeCopies.size()-1);
			nodeCopy = new ConceptNode(makeName(prevName), this.getLabel());
			//TODO: does it matter that these values are not being copied?
			/*
		nodeCopy.setKnowledgeEstimate(this.knowledgeEstimate);
		nodeCopy.setKnowledgePrediction(this.knowledgePrediction);
		nodeCopy.setKnowledgeDistanceFromAvg(this.knowledgeDistanceFromAvg);
		*/
			nodeCopies.add(nodeCopy.getID());
			multCopies.put(nodeCopy.getLabel(), nodeCopies);
		}

		try{
			for(ConceptNode origChild : this.getChildren()){
				ConceptNode childCopy = origChild.makeTree(multCopies);
				nodeCopy.addChild(childCopy);
			}
		}catch(NullPointerException e){
			System.out.println("Broke on this node: "+this.getID());
		}

		return nodeCopy;
	}

	/***
	 * makes next name based on prevName, everything before "-" symbol is the name and the number after it will iterate
	 * @param prevName
	 * @return String next name
	 */
	public static String makeName(String prevName) {
		if(prevName != "" && prevName != null){
			String[] nameList = prevName.split(symbol);
			String name = "";
			for(int i = 0; i < nameList.length-1; i++){
				name += nameList[i];
			}
			int num = 0;

			if(nameList.length <= 1){
				String fullName = nameList[0] + "-1";
				return fullName;
			}else{
				try{
					String numString = nameList[nameList.length-1];
					num = Integer.parseInt(numString);
					num += 1;
				}catch(NumberFormatException e){
					name += nameList[nameList.length-1];
					num = 1;
				}
			}


			String fullName = name+"-"+num;
			return fullName;
		}else{
			return "";
		}
	}

	//////////////////  TO STRING  //////////////////////
	public String toString(String indent) {
		String stringToReturn = indent + getLabel() +  "\t actual: " + getKnowledgeEstimate() + " pred: " + getKnowledgePrediction();
		for (ConceptNode child :getChildren()){
			stringToReturn += child.toString(indent + "\t");
		}
		// String stringToReturn = "Concept: " + this.getLabel() + " ID: " + this.getID() + "\n";
		return stringToReturn;
	}

	public String toString(){
		return toString("\n");
	}

	//////////////////  GETTERS and SETTERS  //////////////////////
	@JsonIgnore
	public List<ConceptNode> getChildren(){
		return children;
	}

	public String getID(){
		return id;
	}

	public String getLabel() {
		return label;
	}

	public double getKnowledgeEstimate() {
		return Math.round(knowledgeEstimate *100.0)/100.0;
	}

	public void setKnowledgeEstimate(double knowledgeEstimate) {
		this.knowledgeEstimate = knowledgeEstimate;
	}

	public void calcDistanceFromAvg(double avgCalc){
		this.knowledgeDistanceFromAvg = this.knowledgeEstimate - avgCalc;
	}

	public void setKnowledgeDistanceFromAvg(double setTo){
		this.knowledgeDistanceFromAvg = setTo;
	}

	public double getKnowledgeDistanceFromAvg(){
		return knowledgeDistanceFromAvg;
	}

	public double getKnowledgePrediction() {
		return Math.round(knowledgePrediction *100.0)/100.0;
	}

	public void setKnowledgePrediction(double knowledgePrediction) {
		this.knowledgePrediction = knowledgePrediction;
	}

	public int getNumParents() {
		return numParents;
	}

	public void setNumParents(int numParents) {
		this.numParents = numParents;
	}

    public Map<String, LearningObject> getLearningObjectMap() {
        return learningObjectMap;
    }
}

	
	

