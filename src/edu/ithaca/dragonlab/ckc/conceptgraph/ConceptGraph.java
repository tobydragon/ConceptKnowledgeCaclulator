package edu.ithaca.dragonlab.ckc.conceptgraph;

import edu.ithaca.dragonlab.ckc.io.*;
import edu.ithaca.dragonlab.ckc.learningobject.LearningObject;
import edu.ithaca.dragonlab.ckc.learningobject.LearningObjectResponse;
import edu.ithaca.dragonlab.ckc.suggester.LearningObjectSuggester;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.List;


public class ConceptGraph {
    private static final Logger logger = LogManager.getLogger(ConceptGraph.class);
    public static final Integer DIVISION_FACTOR = 2;


	String name;
	List<ConceptNode> roots;

	//This information will be duplicated, nodes and learning objects will be found within other nodes,
    // but also can be quickly looked up here (also allows checks for duplicates).
	Map<String, LearningObject> learningObjectMap;
	Map<String, ConceptNode> nodeMap;


    public ConceptGraph(ConceptGraphRecord structureDef){
        this.name = structureDef.getName();
        buildStructureFromGraphRecord(structureDef);
        this.learningObjectMap = new HashMap<>();
    }

    public ConceptGraph(ConceptGraphRecord structureDef, List<LearningObjectLinkRecord> lolRecords){
        this(structureDef);
        addLearningObjectsFromLearningObjectLinkRecords(lolRecords);
    }

    public ConceptGraph(ConceptGraphRecord structureDef, List<LearningObjectLinkRecord> lolRecords, List<LearningObjectResponse> learningObjectsResponses){
        this(structureDef, lolRecords);
        addLearningObjectResponses(learningObjectsResponses);
    }

    /**
     * Copy constructor that creates an entire copy but with a different name
     * @param other a graph to copy
     * @param newName the new name of the graph
     */
	public ConceptGraph(ConceptGraph other, String newName){
	    this.name = newName;
		this.roots = new ArrayList<>();
        nodeMap = new HashMap<>();
        learningObjectMap = new HashMap<>();

        //recursively copy entire graph
        for (ConceptNode otherRoot : other.roots) {
            ConceptNode newRoot = new ConceptNode(otherRoot, nodeMap, learningObjectMap);
            nodeMap.put(newRoot.getID(), newRoot);
            this.roots.add(newRoot);
        }
    }

    /**
     * used only by TreeConverter, does not have proper learningObjectMap or nodeMap
     * @param rootsIn
     */
    public ConceptGraph(List<ConceptNode> rootsIn, String name){
        this.name = name;
        learningObjectMap = new HashMap<>();
        nodeMap = new HashMap<>();
        this.roots = rootsIn;
    }


    /**
     * Initializes a graph structure only (no learningObjects)
     * @param graphRecord
     * @post sets, roots and nodeMap, completely overwrite any previous graph structure
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
     * Takes a Learning Object and a list of concept Id's it connects to. Adds the learning object to the graph and to the corresponding concepts
     * If the learning Object already exists in the graph that is recorded in the logger and nothing happens
     * @param toLink - the learning object that is going to be linked to the incoming concept IDs (cannot already be part of the graph)
     * @param conceptIds - list of strings of the concept IDs the learning object will be linked to
     * @post   the learningObject is added to the graph's map, and to all associated concept's maps
     * @return the number of concepts the learning object was added to, or -1 if the learning object already exists
     */
    public int linkLearningObjects(LearningObject toLink, List<String> conceptIds){
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
                logger.warn("Authoring Warning: Concept '"+id+"' is not in your concept map. "+toLink.getId()+" was not added to the map under the concept ID "+id);
            }
        }
        return numAdded;
    }

    /**
     * creates learningObjects and links them to concepts based on a list of learningObjectLinkRecords
     * @param learningObjectLinkRecords - list of learningObjectLinkRecords
     */
    public void addLearningObjectsFromLearningObjectLinkRecords(List<LearningObjectLinkRecord> learningObjectLinkRecords){

        for (LearningObjectLinkRecord record: learningObjectLinkRecords){
            linkLearningObjects(new LearningObject(record), record.getConceptIds());
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

    //clearer name

    /**
     *Creates a boolean based off of if the node is an ancestor and if the children nodes have high estimates.
     *if the node isn't an ancestor OR the compare node is high, then false (and you can do add the conceptNode)
     * therefore if the node is an ancestor and the child compare node has a low knowledgeEstimate, then true is flagged and you don't add the conceptNode
     * if false, then it can be added to the list
     * @param node
     * @return boolean
     */
	public boolean canIgnoreNode(ConceptNode node) {
		boolean isAnc = false;
		for (String key : nodeMap.keySet()) {
			ConceptNode compareNode = nodeMap.get(key);
			boolean ances =  node.isAncestorOf(compareNode);
            if (ances && compareNode.getKnowledgeEstimate()<LearningObjectSuggester.max){
                isAnc=true;
				break;
			}
		}
		return  isAnc;
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

    public List<ConceptNode> getRoots() {
        return roots;
    }

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
}
