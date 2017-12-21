package edu.ithaca.dragonlab.ckc.conceptgraph;

import edu.ithaca.dragonlab.ckc.io.*;
import edu.ithaca.dragonlab.ckc.learningobject.LearningMaterial;
import edu.ithaca.dragonlab.ckc.learningobject.LearningObject;
import edu.ithaca.dragonlab.ckc.learningobject.LearningObjectResponse;
import edu.ithaca.dragonlab.ckc.learningobject.LearningResource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.List;


public class  ConceptGraph {
    private static final Logger logger = LogManager.getLogger(ConceptGraph.class);
    public static final Integer DIVISION_FACTOR = 2;


	String name;
	List<ConceptNode> roots;

	//This information will be duplicated, nodes and learning objects will be found within other nodes,
    // but also can be quickly looked up here (also allows checks for duplicates).
	Map<String, ConceptNode> nodeMap;
    Map<String, LearningObject> learningObjectMap;
    Map<String, LearningMaterial> learningMaterialMap;


    public ConceptGraph(ConceptGraphRecord structureDef){
        this.name = structureDef.getName();
        buildStructureFromGraphRecord(structureDef);
        this.learningObjectMap = new HashMap<>();
        this.learningMaterialMap = new HashMap<>();
    }

    public ConceptGraph(ConceptGraphRecord structureDef, List<LearningResourceRecord> lolRecords){
        this(structureDef);
        addLearningResourcesFromRecords(lolRecords);
    }

    public ConceptGraph(ConceptGraphRecord structureDef, List<LearningResourceRecord> lolRecords, List<LearningObjectResponse> learningObjectsResponses){
        this(structureDef, lolRecords);
        addLearningObjectResponses(learningObjectsResponses);
    }

    /**
     * Copy constructor that creates an entire copy but with a different name
     * Use this copy constructor when you want all new learning objects created (not copied from other graph), e.g., when
     * copying the structure of a graph without the data
     * @param other a graph to copy
     * @param newName the new name of the graph
     */
    public ConceptGraph(ConceptGraph other, String newName){
        this(other, newName, new HashMap<>(), new HashMap<>());
    }

    /**
     * Copy constructor that creates an entire copy but with a different name
     * Use this copy constructor (with a copied learning map, see LearningObject.copyLearningObjectMap)
     * when you want a copy of the graph including a copy of the LearningObjects and LearningObjectResponses
     * e.g., when you are making a tree copy
     * @param other a graph to copy
     * @param newName the new name of the graph
     * @param loMap a map of learning objects, which can be in any state of populated/not populated. This function will
     *              connect the existing learning obejcts, or add any new ones necessary
     */
    public ConceptGraph(ConceptGraph other, String newName, HashMap<String, LearningObject> loMap, HashMap<String, LearningMaterial> lmMap){
        this.name = newName;
        this.roots = new ArrayList<>();
        nodeMap = new HashMap<>();
        learningObjectMap = loMap;
        learningMaterialMap = lmMap;

        //recursively copy entire graph
        for (ConceptNode otherRoot : other.roots) {
            ConceptNode newRoot = new ConceptNode(otherRoot, nodeMap, learningObjectMap, learningMaterialMap);
            nodeMap.put(newRoot.getID(), newRoot);
            this.roots.add(newRoot);
        }
    }

    /**
     * used only by TreeConverter, does not have proper learningObjectMap or nodeMap
     * @param rootsIn
     */
    public ConceptGraph(List<ConceptNode> rootsIn, String name, Map<String, LearningObject> learningObjectMap, Map<String, LearningMaterial> learningMaterialMap, Map<String, ConceptNode> nodeMap){
        this.name = name;
        this.learningObjectMap = learningObjectMap;
        this.learningMaterialMap = learningMaterialMap;
        this.nodeMap = nodeMap;
        this.roots = rootsIn;
    }

    public ConceptGraph(){
        this.name = " ";
        learningObjectMap = new HashMap<>();
        learningMaterialMap = new HashMap<>();
        nodeMap= new HashMap<>();
        this.roots= new LinkedList<>();
    }

    /**
     * Initializes a graph studentKnowledgeEstimates only (no learningObjects)
     * @param graphRecord
     * @post sets, roots and nodeMap, completely overwrite any previous graph studentKnowledgeEstimates
     */
    private void buildStructureFromGraphRecord(ConceptGraphRecord graphRecord){
        this.roots = new ArrayList<>();
        this.nodeMap = new HashMap<>();

        List<ConceptRecord> rootRecords = graphRecord.findRoots();

        for (ConceptRecord conceptRecord :graphRecord.getConcepts() ){
            ConceptNode newNode = new ConceptNode(conceptRecord);
            nodeMap.put(newNode.getID(), newNode);
            if (rootRecords.contains(conceptRecord)) {
                roots.add(newNode);
            }
        }

        linkConceptsToEachOther(graphRecord.getLinks());
    }

    /**
     * initializes nodes child lists
     * @pre nodes must already exist for all ids listed in links
     * @param links
     * @post the ConceptNodes themselves are altered to include the new links
     */
    private void linkConceptsToEachOther(List<LinkRecord> links){
        for( LinkRecord currLink : links){
            ConceptNode currParent = nodeMap.get(currLink.getParent());
            if(currParent == null){
                logger.warn("In ConceptGraph.addChildren(): " + currLink.getParent() + " node not found in nodes list for link " + currLink);
            }
            else{
                ConceptNode currChild = nodeMap.get(currLink.getChild());
                if(currChild == null){
                    logger.warn("In ConceptGraph.addChildren(): " + currLink.getChild()+" node not found in nodes list for link " + currLink);
                }
                else{
                    currParent.addChild(currChild);
                }
            }

        }
    }

    /**
     * Takes a Learning Object and a list of Concept Id's it connects to. Adds the learning object to the graph and to the corresponding concepts
     * If the learning Object already exists in the graph that is recorded in the logger and nothing happens
     * @param toLink - the learning object that is going to be linked to the incoming Concept IDs (cannot already be part of the graph)
     * @param conceptIds - list of strings of the Concept IDs the learning object will be linked to
     * @post   the learningObject is added to the graph's map, and to all associated Concept's maps
     * @return the number of concepts the learning object was added to, or -1 if the learning object already exists
     */
    public int linkLearningObjects(LearningObject toLink, Collection<String> conceptIds){
        int numAdded = 0;
        if (learningObjectMap.get(toLink.getId()) != null){
            logger.warn(toLink.getId()+" already exists in this graph. Nothing was added.");
            return -1;
        }
        learningObjectMap.put(toLink.getId(), toLink);
        for (String id: conceptIds){
            if(nodeMap.get(id) != null){
                numAdded++;
                ConceptNode current = nodeMap.get(id);
                current.addLearningObject(toLink);
            } else{
                logger.warn("Authoring Warning: Concept '"+id+"' is not in your Concept map. "+toLink.getId()+" was not added to the map under the Concept ID "+id);
            }
        }
        return numAdded;
    }

    /**
     * Takes a Learning Object and a list of Concept Id's it connects to. Adds the learning object to the graph and to the corresponding concepts
     * If the learning Object already exists in the graph that is recorded in the logger and nothing happens
     * @param toLink - the learning object that is going to be linked to the incoming Concept IDs (cannot already be part of the graph)
     * @param conceptIds - list of strings of the Concept IDs the learning object will be linked to
     * @post   the learningObject is added to the graph's map, and to all associated Concept's maps
     * @return the number of concepts the learning object was added to, or -1 if the learning object already exists
     */
    public int linkLearningMaterials(LearningMaterial toLink, Collection<String> conceptIds){
        int numAdded = 0;
        if (learningMaterialMap.get(toLink.getId()) != null){
            logger.warn(toLink.getId()+" already exists in this graph. Nothing was added.");
            return -1;
        }
        learningMaterialMap.put(toLink.getId(), toLink);
        for (String id: conceptIds){
            if(nodeMap.get(id) != null){
                numAdded++;
                ConceptNode current = nodeMap.get(id);
                current.addLearningMaterial(toLink);
            } else{
                logger.warn("Authoring Warning: Concept '"+id+"' is not in your Concept map. "+toLink.getId()+" was not added to the map under the Concept ID "+id);
            }
        }
        return numAdded;
    }

    public void addLearningResourcesFromRecords(List<LearningResourceRecord> learningObjectLinkRecords){

        for (LearningResourceRecord record: learningObjectLinkRecords){
            linkLearningObjects(new LearningObject(record), record.getConceptIds());

            //set defaults if there aren't any resources
            if (record.getResourceTypes().size() == 0){
                record.setResourceTypes(LearningResource.DEFAULT_RESOURCE_TYPES);
            }
            //may create duplicate records if one resource is both an assessment and a material
            if (record.isType(LearningResource.Type.ASSESSMENT)){
                linkLearningObjects(new LearningObject(record), record.getConceptIds());
            }
            if (record.isType(LearningResource.Type.INFORMATION) || record.isType(LearningResource.Type.PRACTICE)){
                //since we've already added possibly an assessment for this record, remove it (if it were there) so the list can be used to create the material directly from the list
                record.getResourceTypes().remove(LearningResource.Type.ASSESSMENT);
                linkLearningMaterials(new LearningMaterial(record), record.getConceptIds());
            }
        }
    }


    /**
     * connects LearningObjectResponse to the appropriate LearningObject in this graph
     * Can be called multiple times with different lists of responses
     * @param learningObjectsResponses
     */
    public void addLearningObjectResponses(List<LearningObjectResponse> learningObjectsResponses) {
        for (LearningObjectResponse response : learningObjectsResponses){
            LearningObject resource = learningObjectMap.get(response.getLearningObjectId());
            if (resource != null){
                resource.addResponse(response);
            }
            else {
                logger.warn("No learning object:" + response.getLearningObjectId() + " for response: " + response.toString());
                //TODO: maybe make a new list of unconnected learning objects???
            }
        }
    }


    /**
     * updates a list of the suggested Concept node list so that there are ancestors of nodes already in the list.
     * You need to iterate nodes to add and then call this function.
     * @param node
     * @param suggestedList
     */
    public static void updateSuggestionList (ConceptNode node, List<ConceptNode> suggestedList){
        //exclude nodes that are ancestors of the node already in the suggestion list
        //when we suggest a descendant. remove any ancestors of that node from the list.

        boolean nodeIsAnc = false;
        List<ConceptNode> ancesList= new ArrayList<>();
        for(ConceptNode ancNode: suggestedList){
            if (node.isAncestorOf(ancNode)){
                nodeIsAnc=true;
                break;
            }
        }

        if(!nodeIsAnc) { //if the node is a descendant
            for (ConceptNode trackNode : suggestedList) {
                if (trackNode.isAncestorOf(node)) {
                    ancesList.add(trackNode);
                }
            }
            suggestedList.removeAll(ancesList);
            suggestedList.add(node);
        }
    }

    public double calcTotalKnowledgeEstimate( String startingSubject){
        if(startingSubject.equals("all")){
            double ex = 0;
            for(ConceptNode roots: this.getRoots()){
                double total = roots.countTotalKnowledgeEstimate(new ArrayList<>());
                ex+= total;

            }
            return ex;
        }else{

            ConceptNode node = this.findNodeById(startingSubject);
            return node.countTotalKnowledgeEstimate(new ArrayList<>());
        }
    }


    //TODO: should use materials, not assessments (I think)
    public HashMap<String, Integer> buildDirectConceptLinkCount(){
        HashMap<String, Integer> directConceptLinkCountMap = new HashMap<>();

                //resources , Concept links

        ////These same LearningObjects might also be held by other nodes
        for (ConceptNode node: nodeMap.values()){


            for (LearningObject lo : node.learningObjectMap.values()){

                if(directConceptLinkCountMap.containsKey(lo.getId())){
                    directConceptLinkCountMap.put(lo.getId(),directConceptLinkCountMap.get(lo.getId())+1);

                }else{
                    directConceptLinkCountMap.put(lo.getId(), 1);
                }

            }

        }

        return directConceptLinkCountMap;
    }

    /**
    *Finds where to start building the summaryList via the parameter and creates a empty hashmap to pass with it to buildLearningObjectSummaryList
    *@param node name that is used to find a ConceptNode
    *@return filled hashmap from other buildLearningobjectSummaryList.
     */
	public HashMap<String,Integer> buildLearningObjectSummaryList(String node) {

		ConceptNode findNode = findNodeById(node);
		if (findNode != null) {
			HashMap<String, Integer> learningObjectSummary = new HashMap<>();
			findNode.buildLearningObjectSummaryList(learningObjectSummary);

            return learningObjectSummary;
		}else{
			logger.warn("Building learningObjectSummaryList:" + node + " is not found in the graph");
			return null;
		}
	}

	////////////////////////////////////////////  Simple Functions    //////////////////////////////////////
    public int responsesCount(){
	    int total = 0;
	    for (LearningObject lo : learningObjectMap.values()){
	        total += lo.getResponses().size();
        }
        return total;
    }


    public void setDistFromAvg(ConceptGraph avgGraph){
        for (ConceptNode nodeToSet : nodeMap.values()){
            nodeToSet.calcDistanceFromAvg(avgGraph.findNodeById(nodeToSet.getID()).getKnowledgeEstimate());
        }
    }

	public ConceptGraphRecord buildConceptGraphRecord() {
		List<ConceptRecord> tempNodes = new ArrayList<>();
		List<LinkRecord> tempLinks = new ArrayList<>();
		for(ConceptNode currRoot : this.roots){
			currRoot.addToRecords(tempNodes,tempLinks);
		}
		ConceptGraphRecord graphRecord = new ConceptGraphRecord(name, tempNodes, tempLinks);
		return graphRecord;
	}

	public void calcKnowledgeEstimates(){
        calcDataImportance();
        for(ConceptNode root : this.roots){
            root.calcKnowledgeEstimate();
		}
	}

	public void calcDataImportance(){
        for(ConceptNode root : this.roots){
			root.calcDataImportance();
		}
	}

	public void calcPredictedScores() {
		for(ConceptNode root : this.roots){
			calcPredictedScores(root);
		}
		
	}
	
	public ConceptNode findNodeById(String id){
	    return nodeMap.get(id);
	}

	public Collection<String> getAllNodeIds(){
		return nodeMap.keySet();
	}
    public Map<String, LearningObject> getLearningObjectMap() {
	    return learningObjectMap;
    }
    public Map<String, LearningMaterial> getLearningMaterialMap() {
        return learningMaterialMap;
    }

    public List<ConceptNode> getRoots() {return roots;}

    public String getName(){
	    return name;
    }

    public String toString(){
        ConceptGraphRecord thisGraph = this.buildConceptGraphRecord();
        return "Nodes:\n"+thisGraph.getConcepts()+"\nLinks:\n"+thisGraph.getLinks();
    }


    //TODO update this defunct code
    //currentRoot.getKnowledgeEstimate used to be this.root.getKnowledgeEstimate, analyze how this changed things
    private void calcPredictedScores(ConceptNode currentRoot) {
        calcPredictedScores(currentRoot, currentRoot.getKnowledgeEstimate(), currentRoot);
    }

    // pre order traversal
    private static void calcPredictedScores(ConceptNode current, double passedDown, ConceptNode currentRoot) {

        // simple check for if we"re dealing with the root, which has its own rule
        if (current == currentRoot) {
            current.setKnowledgePrediction(current.getKnowledgeEstimate());
        } else {
//            current.setNumParents(current.getNumParents() + 1);

            // Calculating the new predicted, take the the old predicted with the weight it has based off of the number of parents
            // calculate the new pred from the new information passed down and the adding it to old pred
//            double oldPred = current.getKnowledgePrediction() * (1.0 - (1.0/current.getNumParents()));
//            double newPred = (passedDown * (1.0/current.getNumParents())) + oldPred;

//            current.setKnowledgePrediction(newPred);
        }

        for (ConceptNode child : current.getChildren()) {
            if (current.getKnowledgeEstimate() == 0) {

                calcPredictedScores(child, current.getKnowledgePrediction() / DIVISION_FACTOR, currentRoot);
            } else {
                calcPredictedScores(child, current.getKnowledgeEstimate(), currentRoot);
            }
        }

    }

    public Map<String, List<String>> createSameLabelMap(){
        Map<String, List<String>> labelMap = new HashMap<>();
        for (ConceptNode curConcept : nodeMap.values()){
            List<String> currList = labelMap.get(curConcept.getLabel());
            if (currList == null){
                currList = new ArrayList<>();
                labelMap.put(curConcept.getLabel(), currList);
            }
            currList.add(curConcept.id);
        }
        return labelMap;
    }



}
