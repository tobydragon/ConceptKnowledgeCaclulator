
package edu.ithaca.dragonlab.ckc.conceptgraph;

import java.util.*;


import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.ithaca.dragonlab.ckc.io.ConceptRecord;
import edu.ithaca.dragonlab.ckc.io.LinkRecord;
import edu.ithaca.dragonlab.ckc.learningobject.LearningObject;

public class ConceptNode {
	private static final String symbol = "-";

	String id; 		//unique to each node
	String label; 	//can be the same as id, or different if you want displayed different
	private double knowledgeEstimate;
	private double knowledgePrediction;
	private double knowledgeDistanceFromAvg;
	private double dataImportance;

	Map<String, LearningObject> learningObjectMap;  //These learningObjects might also be held by other nodes
	List<ConceptNode> children;

	private int numParents; //TODO: remove?

	//TODO: remove default constructor and all jackson references, these are built through conceptRecords now
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
		dataImportance = 0;
	}

	public ConceptNode(ConceptRecord conceptRecord) {
		this.id = conceptRecord.getId();
		this.label = conceptRecord.getLabel();
		knowledgePrediction = conceptRecord.getKnowledgePrediction();
		knowledgeEstimate = conceptRecord.getKnowledgeEstimate();
		knowledgeDistanceFromAvg = conceptRecord.getKnowledgeDistFromAvg();

		children = new ArrayList<>();
		learningObjectMap = new HashMap<>();
		numParents = 0;
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



	public void buildLearningObjectSummaryList(HashMap <String, Integer> learningObjectSummary){

		//add the current questions.
		Iterator <String> itr = this.learningObjectMap.keySet().iterator();

		for (int i =0; i< this.learningObjectMap.size(); i++){
			//need to get each of the values in the learningObjectMap
			LearningObject label = this.learningObjectMap.get(itr.next());

			if (learningObjectSummary.containsKey(label.getId())){
                learningObjectSummary.put(label.getId() ,learningObjectSummary.get(label.getId())+1);

			}else{
                learningObjectSummary.put(label.getId() ,1);

			}

		}
		//go to each of the children and call on child so that the child node's learning objects will be added.
		for(ConceptNode child: children){
			child.buildLearningObjectSummaryList(learningObjectSummary);

		}
	}


    //find node()
    public ConceptNode findNode(String findConcept){

        if (this.id.equals(findConcept)){
            return this;
        }else{

            for (ConceptNode child: children){
                ConceptNode myNode = child.findNode(findConcept);
                if(myNode != null){
                    return myNode;
                }
            }
        }

        return null;

    }

	public boolean isAncestorOf(ConceptNode child){
		//is a descendant , ancestor
		//iterate through the tree and see if it's below it
        boolean flag =false;

        if (this.children.contains(child)){
            flag = true;
//            return true;
        }else {

            for (ConceptNode nchild : children) {
                if(nchild.isAncestorOf(child)){
                    flag=true;
                }
            }
        }
        return flag;
    }


	//Complicated because it is a graph, so it should only recurse when a child hasn't already been created, which we can only tell from graphNodeMap
    public ConceptNode(ConceptNode other, Map<String, ConceptNode> graphNodeMap, Map<String, LearningObject> graphLearningObjectMap){
	    this.id = other.id;
        this.label = other.label;
        this.knowledgeEstimate = other.knowledgeEstimate;
        this.knowledgePrediction = other.knowledgePrediction;
        this.knowledgeDistanceFromAvg = other.knowledgeDistanceFromAvg;

        this.learningObjectMap = new HashMap<>();
        for (Map.Entry<String, LearningObject> entry: other.getLearningObjectMap().entrySet()){
            LearningObject newLearningObject = new LearningObject(entry.getValue());
            this.learningObjectMap.put(entry.getKey(), newLearningObject);
            graphLearningObjectMap.put(entry.getKey(), newLearningObject);
        }

        this.children = new ArrayList<>();
        for (ConceptNode otherChild : other.children){
            //if this node has already been copied (due to graph structure), just link it in new graph structure
            ConceptNode alreadyCopiedNode = graphNodeMap.get(otherChild.getID());
            if (alreadyCopiedNode != null){
                this.addChild(alreadyCopiedNode);
            }
            else {
                ConceptNode newChild = new ConceptNode(otherChild, graphNodeMap, graphLearningObjectMap);
                graphNodeMap.put(newChild.getID(), newChild);
                this.addChild(newChild);
            }
        }

        this.numParents = other.numParents;
    }
	
	public void addChild(ConceptNode child){
		children.add(child);
	}

	public void calcDataImportance(){
		double tempDI=0;
		for(LearningObject learningObject: learningObjectMap.values()){
			tempDI+=learningObject.getDataImportance();
		}

		for (ConceptNode child: children){
			child.calcDataImportance();
			tempDI+=child.getDataImportance();
		}
		dataImportance = tempDI;
	}

	//TODO: something should ensure that calcDataImportance has already been called, or this doesn't work right...

	/**
	 * @pre calcDataImportance must have already been called
	 */
	public void calcKnowledgeEstimate() {
		//calculate estimate from learning objects directly connected to this node
        double currentConceptEstimate = 0;
		for (LearningObject learningObject : learningObjectMap.values()){
			currentConceptEstimate += learningObject.calcKnowledgeEstimate()*learningObject.getDataImportance();
		}

		//calculate estimate from children
		for (ConceptNode child : children){
			child.calcKnowledgeEstimate();
			currentConceptEstimate +=   child.getKnowledgeEstimate()*child.getDataImportance();
		}

		if (dataImportance > 0){
			this.knowledgeEstimate = currentConceptEstimate / dataImportance;
		} else {
			//in this case, this node has no data and can't be estimated
			this.knowledgeEstimate = 0;
		}

	}

	public void addToNodesAndLinksLists(List<ConceptRecord> concepts, List<LinkRecord> links){
		//if I am not in the list
		ConceptRecord newConceptRecord = new ConceptRecord(this);
		if(!concepts.contains(newConceptRecord)){
			//add me to the nodes list
			concepts.add(newConceptRecord);
			for(ConceptNode child : this.children){
				//recurse call on children
				child.addToNodesAndLinksLists(concepts,links);
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
		return Math.round(knowledgeEstimate *1000.0)/1000.0;
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
		return Math.round(knowledgePrediction *10000)/1000.0;
	}

	public void setKnowledgePrediction(double knowledgePrediction) {
		this.knowledgePrediction = knowledgePrediction;
	}

	public double getDataImportance() { return dataImportance; }

	public void setDataImportance(double dataImportance) {
		this.dataImportance = dataImportance;
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

	
	

