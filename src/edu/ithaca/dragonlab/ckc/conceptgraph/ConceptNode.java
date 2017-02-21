
package edu.ithaca.dragonlab.ckc.conceptgraph;

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

    public ConceptNode(ConceptNode other){
	    this.id = other.id;
        this.label = other.label;
        this.knowledgeEstimate = other.knowledgeEstimate;
        this.knowledgePrediction = other.knowledgePrediction;
        this.knowledgeDistanceFromAvg = other.knowledgeDistanceFromAvg;

        this.learningObjectMap = new HashMap<>();
        for (Map.Entry<String, LearningObject> entry: other.getLearningObjectMap().entrySet()){
            this.learningObjectMap.put(entry.getKey(), new LearningObject(entry.getValue()));
        }

        this.children = new ArrayList<>();
        for (ConceptNode otherChild : other.children){
            this.addChild( new ConceptNode (otherChild));
        }

        this.numParents = other.numParents;
    }

    //called after copy has been made, recursively adds all nodes and leaningObjects to respective maps
    public void populateMaps(Map<String, ConceptNode> nodeMap, Map<String, LearningObject> graphLearningObjectMap) {
        nodeMap.put(this.getID(), this);
        for (LearningObject learningObject : this.learningObjectMap.values()){
            graphLearningObjectMap.put(learningObject.getId(), learningObject);
        }
        for (ConceptNode child : children){
            child.populateMaps(nodeMap, graphLearningObjectMap);
        }
    }
	
	public void addChild(ConceptNode child){
		children.add(child);
	}

	public void calcActualComp() {
        //TODO: take dataImportance into consideration

        //calculate value for this current concept
        double currentConceptEstimate = 0;
        for (LearningObject learningObject : learningObjectMap.values()){
            currentConceptEstimate += learningObject.calcKnowledgeEstimate();
        }
        if (learningObjectMap.size() > 0) {
            currentConceptEstimate /= learningObjectMap.size();
        }

        //calculate estimate from children
        double childrenEstimate = 0;
        for (ConceptNode child : children){
            child.calcActualComp();
            childrenEstimate += child.getKnowledgeEstimate();
        }
        if (children.size() > 0) {
            childrenEstimate /= children.size();
        }

        //TODO: this is probably not right, needs more thought
        //TODO: if ever putting data on non-leaf nodes, this needs revisiting
        //if you have both, average them
        if (currentConceptEstimate > 0 && childrenEstimate > 0){
            this.knowledgeEstimate = (currentConceptEstimate+childrenEstimate)/2;
        }
        else if (currentConceptEstimate > 0 ){
            this.knowledgeEstimate = currentConceptEstimate;
        }
        else {
            this.knowledgeEstimate = childrenEstimate;
        }
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
		//if there are no copies, make a new list to store all the copies and add it to the map
		if(nodeCopies == null){
			nodeCopy = new ConceptNode(makeName(this.getLabel()), this.getLabel());
			nodeCopies = new ArrayList<>();
            multCopies.put(nodeCopy.getLabel(), nodeCopies);
		//else get the previous name from the list and make a new name from it
		}else{
			String prevName = nodeCopies.get(nodeCopies.size()-1);
			nodeCopy = new ConceptNode(makeName(prevName), this.getLabel());
		}
		//add the new name to the list
        nodeCopies.add(nodeCopy.getID());

		//set all the data from the node copy
        nodeCopy.setKnowledgeEstimate(this.knowledgeEstimate);
        nodeCopy.setKnowledgePrediction(this.knowledgePrediction);
        nodeCopy.setKnowledgeDistanceFromAvg(this.knowledgeDistanceFromAvg);

        for(ConceptNode origChild : this.getChildren()){
            ConceptNode childCopy = origChild.makeTree(multCopies);
            nodeCopy.addChild(childCopy);
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

	
	

