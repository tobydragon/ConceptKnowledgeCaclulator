
package edu.ithaca.dragonlab.ckc.conceptgraph;

import java.util.*;


import edu.ithaca.dragonlab.ckc.io.ConceptRecord;
import edu.ithaca.dragonlab.ckc.io.LinkRecord;
import edu.ithaca.dragonlab.ckc.learningobject.LearningObject;

public class ConceptNode {

	String id; 		//unique to each node
	String label; 	//can be the same as id, or different if you want displayed different
	private double knowledgeEstimate;
	private double knowledgePrediction;
	private double knowledgeDistanceFromAvg;
	private double dataImportance;

	Map<String, LearningObject> learningObjectMap;  //These same LearningObjects might also be held by other nodes
	List<ConceptNode> children;

	/**
	 * builds a node from a concept record
	 * @param conceptRecord
	 */
	public ConceptNode(ConceptRecord conceptRecord) {
		this.id = conceptRecord.getId();
		if (conceptRecord.getLabel() != null && conceptRecord.getLabel() != "") {
            this.label = conceptRecord.getLabel();
        }
        else {
		    this.label = this.id;
        }
        this.knowledgePrediction = conceptRecord.getKnowledgePrediction();
        this.knowledgeEstimate = conceptRecord.getKnowledgeEstimate();
		this.knowledgeDistanceFromAvg = conceptRecord.getKnowledgeDistFromAvg();
		this.dataImportance = conceptRecord.getDataImportance();

        this.children = new ArrayList<>();
        this.learningObjectMap = new HashMap<>();
	}

	/**
	 * Copy constructor used with ConceptGraph copy constructor, so it also takes maps to be updated for the entire graph
	 * @param other
	 * @param graphNodeMap
	 * @param graphLearningObjectMap
	 */
	public ConceptNode(ConceptNode other, Map<String, ConceptNode> graphNodeMap, Map<String, LearningObject> graphLearningObjectMap){
		this.id = other.id;
		this.label = other.label;
		this.knowledgeEstimate = other.knowledgeEstimate;
		this.knowledgePrediction = other.knowledgePrediction;
		this.knowledgeDistanceFromAvg = other.knowledgeDistanceFromAvg;
		this.dataImportance = other.dataImportance;

		//Complicated because it is a graph, so it should only recurse when a child hasn't already been created, which we can only tell from graphNodeMap

		this.learningObjectMap = new HashMap<>();
		for (Map.Entry<String, LearningObject> entry: other.getLearningObjectMap().entrySet()){

			//check the graphMap first to see if that learning object has already been created
			LearningObject newLearningObject = graphLearningObjectMap.get(entry.getKey());
			if (newLearningObject == null) {
				//if not, create it and add it to the graphMap
				newLearningObject = new LearningObject(entry.getValue());
				graphLearningObjectMap.put(entry.getKey(), newLearningObject);
			}
			this.learningObjectMap.put(entry.getKey(), newLearningObject);
		}

		this.children = new ArrayList<>();
		for (ConceptNode otherChild : other.children){
			//if this node has already been copied (due to graph studentKnowledgeEstimates), just link it in new graph studentKnowledgeEstimates
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
	}

	public ConceptNode(){
		this.id="";
		this.label="";
		knowledgeEstimate=0;
		knowledgePrediction=0;
		knowledgeDistanceFromAvg=0;
		dataImportance=0;
		learningObjectMap = new HashMap<>();
		children= new LinkedList<>();

	}



	public double countTotalKnowledgeEstimate( List<String> allNode) {
        if (allNode.contains(this.getID())) {
            return 0;
        } else {

            allNode.add(this.getID());

            if (this.children.size() == 0) {
                return (this.getKnowledgeEstimate());

            } else {
                double sum = 0;

                for (ConceptNode child : this.children) {
                    sum += child.countTotalKnowledgeEstimate(allNode);
                }

            return (this.getKnowledgeEstimate() + sum);
            }

        }

    }



	/**
     *fills up a hashmap with the LearningObjects IDs and the amount of ways to get to the learning object from the root (which is how importance is measured)
     *@param learningObjectSummary map>
     */
	public void buildLearningObjectSummaryList(HashMap <String, Integer> learningObjectSummary){

		//add the current questions.
		Iterator <String> itr = this.learningObjectMap.keySet().iterator();

		for (int i =0; i< this.learningObjectMap.size(); i++){
			//need to get each of the values in the learningObjectMap
			LearningObject label = this.learningObjectMap.get(itr.next());

			if (learningObjectSummary.containsKey(label.getId())){
                learningObjectSummary.put(label.getId() ,learningObjectSummary.get(label.getId())+1);

			}else{
                learningObjectSummary.put(label.getId(), 1);

			}
		}
		//go to each of the children and call on child so that the child node's learning objects will be added.
		for(ConceptNode child: children){
			child.buildLearningObjectSummaryList(learningObjectSummary);

		}
	}

    /**
    checks to see if the called on ConceptNode is a parents (grandparent, ect.) to the parameter
    @param possibleDescendent node
    @returns true of the called node is in the lineage of the parameter
     */
	public boolean isAncestorOf(ConceptNode possibleDescendent){
		boolean isAncestor =false;
        if (this.children.contains(possibleDescendent)){
            isAncestor = true;
        }else {
            for (ConceptNode child : children) {
                if(child.isAncestorOf(possibleDescendent)){
                    isAncestor=true;
                }
            }
        }
        return isAncestor;
    }

	/**
	 * recursively sets the dataImportance for this node and all descendants
	 */
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

            currentConceptEstimate +=  child.getKnowledgeEstimate()*child.getDataImportance();
		}

		if (dataImportance > 0){
			this.knowledgeEstimate = currentConceptEstimate / dataImportance;

        } else {
			//in this case, this node has no data and can't be estimated
			this.knowledgeEstimate = 0;
		}

	}

	/**
	 * recursively adds all concepts and links from this and all children
	 * @param concepts to add to
	 * @param links to add to
	 * @post all concepts and links at this node or below are added to the given lists
	 */
	public void addToRecords(List<ConceptRecord> concepts, List<LinkRecord> links){
		//if I am not in the list
		ConceptRecord newConceptRecord = new ConceptRecord(this);
		if(!concepts.contains(newConceptRecord)){
			//add me to the nodes list
			concepts.add(newConceptRecord);
			for(ConceptNode child : this.children){
				//recurse call on children
				child.addToRecords(concepts,links);
				//add the links between me and my children to link list
				links.add(new LinkRecord(this.getID(),child.getID()));
			}
		}
	}

	/**
	 * @param learningObject
	 */
	public void addLearningObject(LearningObject learningObject) {
			learningObjectMap.put(learningObject.getId(), learningObject);
	}

	public void addChild(ConceptNode child){
		children.add(child);
	}


	//////////////////  GETTERS and SETTERS  //////////////////////
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
		return knowledgeEstimate;
	}

	public void setKnowledgeEstimate(double knowledgeEstimate) {
		this.knowledgeEstimate = knowledgeEstimate;
	}

	public void calcDistanceFromAvg(double avgCalc){
		this.knowledgeDistanceFromAvg = this.knowledgeEstimate - avgCalc;
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

    public Map<String, LearningObject> getLearningObjectMap() {
        return learningObjectMap;
    }

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
}

	
	

