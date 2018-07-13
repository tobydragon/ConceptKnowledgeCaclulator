
package edu.ithaca.dragon.tecmap.conceptgraph;

import edu.ithaca.dragon.tecmap.io.record.ConceptRecord;
import edu.ithaca.dragon.tecmap.io.record.LinkRecord;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItem;
import edu.ithaca.dragon.tecmap.learningresource.LearningMaterial;

import java.util.*;


public class ConceptNode {

	private String id; 		//unique to each node
	private String label; 	//can be the same as id, or different if you want displayed different
	private double knowledgeEstimate;
	private double knowledgeDistanceFromAvg;
	private double dataImportance;

	private Map<String, AssessmentItem> learningObjectMap;  //These same LearningObjects might also be held by other nodes
    private Map<String, LearningMaterial> learningMaterialMap;  //These same LearningObjects might also be held by other nodes

	List<ConceptNode> children;

	/**
	 * constructor to build a node from a Concept record
	 * @param conceptRecord to build from
	 */
	public ConceptNode(ConceptRecord conceptRecord) {
		this.id = conceptRecord.getId();
		if (conceptRecord.getLabel() != null && (! "".equals(conceptRecord.getLabel()) )) {
            this.label = conceptRecord.getLabel();
        }
        else {
		    this.label = this.id;
        }
        this.knowledgeEstimate = conceptRecord.getKnowledgeEstimate();
		this.knowledgeDistanceFromAvg = conceptRecord.getKnowledgeDistFromAvg();
		this.dataImportance = conceptRecord.getDataImportance();

        this.children = new ArrayList<>();
        this.learningObjectMap = new HashMap<>();
        this.learningMaterialMap = new HashMap<>();
	}

	/**
	 *  Recursive copy constructor to copy entire sub-graph, used with ConceptGraph copy constructor
     *  maps from the graph level are needed to ensure we only make copies of things once, even if the node occurs mutliple times
     *  the maps are not altered
	 * @param nodeToCopy
     * @param graphNodeMap the map of all nodes in the current graph that this node will be a part of
     * @param graphLearningObjectMap the current assessmentItemMap for the graph that this node will be a part of
     * @param graphLearningMaterialMap  the current learningMaterialMap for the graph that this node will be a part of
     * @post all contents of this new node are set to new copies of the data from , and all children are also copied
	 */
	public ConceptNode(ConceptNode nodeToCopy, Map<String, ConceptNode> graphNodeMap, Map<String, AssessmentItem> graphLearningObjectMap, Map<String, LearningMaterial> graphLearningMaterialMap){
		copyContents(nodeToCopy, graphLearningObjectMap, graphLearningMaterialMap);

		//recursively copy children
		for (ConceptNode otherChild : nodeToCopy.children){
			//if this node has already been copied (due to graph studentKnowledgeEstimates), just link it in new graph studentKnowledgeEstimates
			ConceptNode alreadyCopiedNode = graphNodeMap.get(otherChild.getID());
			if (alreadyCopiedNode != null){
				this.addChild(alreadyCopiedNode);
			}
			else {
				ConceptNode newChild = new ConceptNode(otherChild, graphNodeMap, graphLearningObjectMap, graphLearningMaterialMap);
				graphNodeMap.put(newChild.getID(), newChild);
				this.addChild(newChild);
			}
		}
	}

	/**
	 * Copy constructor used to copy a single ConceptNode and give it a new name, it does not recurse
	 * Used when another entity will recursively create children (e.g., TreeConverter.makeTreeNodeCopy)
     * *  maps from the graph level are needed to ensure we only make copies of things once, even if the node occurs mutliple times
     *  the maps are not altered
     * @param nodeToCopy
     * @param graphLearningObjectMap the current assessmentItemMap for the graph that this node will be a part of
     * @param graphLearningMaterialMap  the current learningMaterialMap for the graph that this node will be a part of
     * @post all contents of this new node are set to new copies of the data from nodeToCopy
     */
	public ConceptNode(String newId, ConceptNode nodeToCopy, Map<String, AssessmentItem> graphLearningObjectMap, Map<String, LearningMaterial> graphLearningMaterialMap){
		copyContents(nodeToCopy, graphLearningObjectMap, graphLearningMaterialMap);
        this.id = newId;
	}

    /**
     * copies all contents of another node into this node
     * @param nodeToCopy the node to copy contents from
     * @param graphLearningObjectMap the current assessmentItemMap for the graph that this node is a part of
     *                               this is needed to make sure we only copy learningObjects once
     * @param graphLearningMaterialMap  the current learningMaterialMap for the graph that this node is a part of
     *                                  this is needed to make sure we only copy learningObjects
     * @post all contents of this node are set to new copies of the data from nodeToCopy
     */
	private void copyContents(ConceptNode nodeToCopy, Map<String, AssessmentItem> graphLearningObjectMap, Map<String, LearningMaterial> graphLearningMaterialMap){
        this.id = nodeToCopy.id;
	    this.label = nodeToCopy.label;
        this.knowledgeEstimate = nodeToCopy.knowledgeEstimate;
        this.knowledgeDistanceFromAvg = nodeToCopy.knowledgeDistanceFromAvg;
        this.dataImportance = nodeToCopy.dataImportance;

        //Complicated because it is a graph, so it should only copy LearningObjects if they haven't already been created
        this.learningObjectMap = new HashMap<>();
        for (Map.Entry<String, AssessmentItem> entry: nodeToCopy.learningObjectMap.entrySet()){

            //check the map first to see if that learningObject has already been created
            AssessmentItem newAssessmentItem = graphLearningObjectMap.get(entry.getKey());
            if (newAssessmentItem == null) {
                newAssessmentItem = new AssessmentItem(entry.getValue());
                graphLearningObjectMap.put(entry.getKey(), newAssessmentItem);
            }
            this.learningObjectMap.put(entry.getKey(), newAssessmentItem);
        }
        this.learningMaterialMap = new HashMap<>();
        for (Map.Entry<String, LearningMaterial> entry: nodeToCopy.learningMaterialMap.entrySet()){

            //check the map first to see if that learningMaterial has already been created
            LearningMaterial newLearningObject = graphLearningMaterialMap.get(entry.getKey());
            if (newLearningObject == null) {
                newLearningObject = new LearningMaterial(entry.getValue());
                graphLearningMaterialMap.put(entry.getKey(), newLearningObject);
            }
            this.learningMaterialMap.put(entry.getKey(), newLearningObject);
        }
        this.children = new ArrayList<>();
    }

    /**
     * sums the total of all knowledge estimates including this node and all below it that haven't already been included
     * @param nodesAlreadyIncluded a list of nodes already included in the count
     * @post this node is added to nodesAlreadyIncluded, if it wasn't in the list already
     * @return the sum of all knoweldge estimates for this node and all children that weren't in nodesAlreadyIncluded
     */
	public double totalKnowledgeEstimateForThisAndAllDescendants(List<String> nodesAlreadyIncluded) {
        if (nodesAlreadyIncluded.contains(this.getID())) {
            return 0;
        } else {
            nodesAlreadyIncluded.add(this.getID());
            if (this.children.size() == 0) {
                return (this.getKnowledgeEstimate());

            } else {
                double sum = 0;
                for (ConceptNode child : this.children) {
                    sum += child.totalKnowledgeEstimateForThisAndAllDescendants(nodesAlreadyIncluded);

				}
                return (this.getKnowledgeEstimate() + sum);
            }
        }
    }

    /**
     * for each LearningMaterial, calculates the number of paths to that learning material from the given node
     * @param idToPathCount a map of id -> countOfPaths to be added to recursively (empty when recursion starts)
     * @post the map with this node and all children with updated counts
     */
	public void buildLearningMaterialPathCount(Map <String, Integer> idToPathCount){
        for (LearningMaterial material : this.learningMaterialMap.values()) {
			if (idToPathCount.containsKey(material.getId())){
                idToPathCount.put(material.getId() ,idToPathCount.get(material.getId())+1);
			}else{
                idToPathCount.put(material.getId(), 1);
			}
		}
		//go to each of the children and call on child so that the child node's learning objects will be added.
		for(ConceptNode child: children){
			child.buildLearningMaterialPathCount(idToPathCount);
		}
	}

    /**
    checks to see if the this ConceptNode is an ancestor (parent, grandparent, ect.) to the parameter possibleDescendant
    @param possibleDescendant node that may be below the current node
    @return true of the possibleDescendant node is somewhere below this node
     */
	public boolean isAncestorOf(ConceptNode possibleDescendant){
		boolean isAncestor =false;
        if (this.children.contains(possibleDescendant)){
            isAncestor = true;
        }else {
            for (ConceptNode child : children) {
                if(child.isAncestorOf(possibleDescendant)){
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
		for(AssessmentItem assessmentItem : learningObjectMap.values()){
			tempDI+= assessmentItem.getDataImportance();
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
		for (AssessmentItem assessmentItem : learningObjectMap.values()){
			currentConceptEstimate += assessmentItem.calcKnowledgeEstimate()* assessmentItem.getDataImportance();
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
	 * starting from a node, gets longest distance to a leaf
	 * @return that length
	 */
	public int getLongestPathToLeaf() {
		if (this.children.isEmpty()) {  //ie. a leaf
			return 0;
		} else {
			List<Integer> childrenPaths = new ArrayList<>();
			for (ConceptNode child : children) {
				childrenPaths.add(1 + child.getLongestPathToLeaf());
			}
			return Collections.max(childrenPaths);
		}
	}

	//////////////////  Simple Methods  //////////////////////
	public void addAssessmentItem(AssessmentItem assessmentItem) {
		learningObjectMap.put(assessmentItem.getId(), assessmentItem);
	}

	public void addLearningMaterial(LearningMaterial learningObject) {
		learningMaterialMap.put(learningObject.getId(), learningObject);
	}

	public void addChild(ConceptNode child){
		children.add(child);
	}

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

	public double getDataImportance() { return dataImportance; }

    public Map<String, AssessmentItem> getAssessmentItemMap() {
        return learningObjectMap;
    }

    public Map<String, LearningMaterial> getLearningMaterialMap() {
        return learningMaterialMap;
    }

	public String toString(String indent) {
		String stringToReturn = indent + getLabel() +  "\t actual: " + getKnowledgeEstimate();
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

	
	

