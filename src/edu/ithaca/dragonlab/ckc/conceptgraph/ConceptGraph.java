package edu.ithaca.dragonlab.ckc.conceptgraph;

import edu.ithaca.dragonlab.ckc.io.ConceptGraphRecord;
import edu.ithaca.dragonlab.ckc.io.LearningObjectLinkRecord;
import edu.ithaca.dragonlab.ckc.io.LinkRecord;
import edu.ithaca.dragonlab.ckc.learningobject.LearningObject;
import edu.ithaca.dragonlab.ckc.learningobject.LearningObjectResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;


public class ConceptGraph {
    private static final Logger logger = LogManager.getLogger(ConceptGraph.class);
	public static final Integer DIVISION_FACTOR = 2;

	List<ConceptNode> roots;

	//This information will be duplicated, links will be found in nodes, but also here.
	Map<String, LearningObject> learningObjectMap;
	Map<String, ConceptNode> nodeMap;
	
	//TODO: This seems to use and change the same nodes. Calling this constructor twice would break things...
	public ConceptGraph(ConceptGraphRecord structureDef){
		buildObjectFromNodesAndLinks(structureDef);
	}

	public ConceptGraph(ConceptGraph other){
		this.roots = new ArrayList<>();
        nodeMap = new HashMap<>();
        learningObjectMap = new HashMap<>();

		//recursively copy entire graph
		for (ConceptNode otherRoot : other.roots){
		    ConceptNode newRoot = new ConceptNode(otherRoot, nodeMap, learningObjectMap);
		    nodeMap.put(newRoot.getID(), newRoot);
		    this.roots.add(newRoot);
        }
	}

	public void buildObjectFromNodesAndLinks(ConceptGraphRecord nodeAndLinks){
		List<ConceptNode> nodes =nodeAndLinks.getNodes();
		List<LinkRecord> links = nodeAndLinks.getLinks();

		this.roots = findRoots(nodes, links);
		this.nodeMap = addChildren(nodes, links);
		this.learningObjectMap = new HashMap<>();
	}

	//TODO: When book code is integrated
//	public ConceptGraph(Book b, ConceptGraphRecord lists){
//		List<ConceptNode> nodes = lists.getNodes();
//		List<LinkRecord> links = lists.getLinks();
//
//		List<LinkRecord> newLinks = b.buildTagLinks();
//		for(LinkRecord link : newLinks){
//			links.add(link);
//		}
//
//		this.roots = findRoots(nodes, links);
//
//		addChildren(nodes, links);
//	}

	/*
	 *Takes in a book, starts at the root, then goes through each level (chapters, sub chapters, questions) and creates
	 *a node for each concept, and adds it as a child.
	 */
	//TODO: fix this once the book is brought in
//	public ConceptGraph(Book b){
//		ConceptNode root = new ConceptNode(b);
//
//		//get the list of chapters of the book
//		//create a new node for each chapter
//		List<Chapter> chapters = b.getChapters();
//		for (Chapter chap : chapters){
//			ConceptNode chapNode = new ConceptNode(chap);
//			root.addChild(chapNode);
//
//			//get the list of sub chapters for each chapter
//			//create a new node for each sub chapter
//			List<SubChapter> subChaps = chap.getSubChapters();
//
//			for (SubChapter subChap : subChaps){
//				ConceptNode subChapNode = new ConceptNode(subChap);
//				chapNode.addChild(subChapNode);
//
//				//get the list of questions for each sub chapter
//				//create a new node for each question
//				List<Question> questions = subChap.getQuestions();
//				for(Question ques : questions){
//					ConceptNode quesNode = new ConceptNode(ques);
//					subChapNode.addChild(quesNode);
//				}
//			}
//		}
//		this.roots = new ArrayList<ConceptNode>();
//		this.roots.add(root);
//	}
	
	public ConceptGraph(List<ConceptNode> rootsIn){
        learningObjectMap = new HashMap<>();
        this.roots = rootsIn;
	}

    public void addLearningObjects(ConceptGraphRecord learningObjectDef){
        for (ConceptNode node : learningObjectDef.getNodes()){
            learningObjectMap.put(node.getID(), new LearningObject(node.getID()));
        }
        for (LinkRecord link : learningObjectDef.getLinks()){
            ConceptNode node = nodeMap.get(link.getParent());
            if (node != null){
                node.addLearningObject(learningObjectMap.get(link.getChild()));
            }
            else {
                logger.warn("Adding a Learning Object:" + link.getChild() + " has no matching node in the graph:" + link.getParent());
            }
        }
    }

    public void addSummariesToGraph(List<LearningObjectResponse> summaries){
        for (LearningObjectResponse response : summaries) {
            LearningObject learningObject = learningObjectMap.get(response.getLearningObjectId());
            if (learningObject != null) {
                learningObject.addResponse(response);
            } else {
                logger.warn("No learning object:" + response.getLearningObjectId() + " for response: " + response.toString());
                //TODO: maybe make a new list of unconnected learning objects???
            }
        }
	}	

	private Map<String, ConceptNode> addChildren(List<ConceptNode> nodes, List<LinkRecord> links){
		HashMap<String, ConceptNode> fullNodesMap = new HashMap<>();
		for( ConceptNode currNode : nodes){
			fullNodesMap.put(currNode.getID(), currNode);
		}
		
		for( LinkRecord currLink : links){
			ConceptNode currParent = fullNodesMap.get(currLink.getParent());
			if(currParent == null){
				logger.warn("In ConceptGraph.addChildren(): " + currLink.getParent() + " node not found in nodes list for link " + currLink);
			}
			else{
				ConceptNode currChild = fullNodesMap.get(currLink.getChild());
				if(currChild == null){
					logger.warn("In ConceptGraph.addChildren(): " + currLink.getChild()+" node not found in nodes list for link " + currLink);
				}
				else{
					currParent.addChild(currChild);
				}
			}
			
		}
		return fullNodesMap;
	}
	
 	private List<ConceptNode> findRoots(List<ConceptNode> nodes, List<LinkRecord> links) {
		List<ConceptNode> runningTotal = new ArrayList<>();
		for (ConceptNode node : nodes) {
			runningTotal.add(node);
		}
		for (LinkRecord link : links) {
			for(ConceptNode node : nodes){
	 			if(node.getID().equals(link.getChild())){
	 				runningTotal.remove(node);
	 			}
	 		}
		}
		return runningTotal;
	}
	
	public String toString(){
		ConceptGraphRecord thisGraph = this.buildNodesAndLinks();
		return "Nodes:\n"+thisGraph.getNodes()+"\nLinks:\n"+thisGraph.getLinks();
		
		//return roots.toString();
		
	}
		
	public ConceptGraphRecord buildNodesAndLinks() {
		List<ConceptNode> tempNodes = new ArrayList<ConceptNode>();
		List<LinkRecord> tempLinks = new ArrayList<LinkRecord>();
		for(ConceptNode currRoot : this.roots){
			currRoot.addToNodesAndLinksLists(tempNodes,tempLinks);
		}
		ConceptGraphRecord outputLists = new ConceptGraphRecord(tempNodes, tempLinks);
		return outputLists;
	}
		

	public void calcKnowledgeEstimates(){
		for(ConceptNode root : this.roots){
			root.calcKnowledgeEstimate();
		}
	}

	public void calcPredictedScores() {
		for(ConceptNode root : this.roots){
			calcPredictedScores(root);
		}
		
	}
	
	//TODO: currentRoot.getKnowledgeEstimate used to be this.root.getKnowledgeEstimate, analyze how this changed things
	private void calcPredictedScores(ConceptNode currentRoot) {
		calcPredictedScores(currentRoot, currentRoot.getKnowledgeEstimate(), currentRoot);
	}
	
	// pre order traversal
	private static void calcPredictedScores(ConceptNode current, double passedDown, ConceptNode currentRoot) {
		
		// simple check for if we"re dealing with the root, which has its own rule
		if (current == currentRoot) {
			current.setKnowledgePrediction(current.getKnowledgeEstimate());
		} else {
			current.setNumParents(current.getNumParents() + 1);
			
			// Calculating the new predicted, take the the old predicted with the weight it has based off of the number of parents
			// calculate the new pred from the new information passed down and the adding it to old pred
			double oldPred = current.getKnowledgePrediction() * (1.0 - (1.0/current.getNumParents()));
			double newPred = (passedDown * (1.0/current.getNumParents())) + oldPred;
			
			current.setKnowledgePrediction(newPred);
		}
		
		for (ConceptNode child : current.getChildren()) {
			if (current.getKnowledgeEstimate() == 0) {
				
				calcPredictedScores(child, current.getKnowledgePrediction() / DIVISION_FACTOR, currentRoot);
			} else {
				calcPredictedScores(child, current.getKnowledgeEstimate(), currentRoot);
			}
		}
		
	}

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
				linkLearningObject(toLink,id);
			} else{
				logger.warn("Authoring Warning: Concept '"+id+"' is not in your concept map. "+toLink.getId()+" was not added to the map under the concept ID "+id);
			}
		}
		return numAdded;
	}

	// Returns true if the linking of learning object to conceptId was successful, false if not (concept doesn't exist)
	//Also checks to see if it the Learning Object ID is already in the map and if it is, it makes sure it is the exact same learning object
	// to avoid the issue of trying to add another learning object of the same name with different information
	private void linkLearningObject(LearningObject toLink, String conceptId){

		ConceptNode current = nodeMap.get(conceptId);
		current.addLearningObject(toLink);

	}

	/*public void addLearningObjectsFromLearningObjectLinkRecords(List<LearningObject> learningObjects, List<LearningObjectLinkRecord> learningObjectLinkRecords){
		for (LearningObjectLinkRecord record: learningObjectLinkRecords){
			LearningObject current;
			for (LearningObject object: learningObjects){
				if (object.getId().equals(record.getLearningObject())){
					current = object;
				}
			}
		}
	}*/
	
	public ConceptGraph graphToTree(){
		List<ConceptNode> newRoots = new ArrayList<>();
		HashMap<String, List<String>> initMultCopies = new HashMap<String, List<String>>();
		for(ConceptNode root : this.roots){
			newRoots.add(root.makeTree(initMultCopies));
		}
		
		return new ConceptGraph(newRoots);
		
	}
	
	public ConceptNode findNodeById(String id){
		return nodeMap.get(id);
	}

    public Map<String, LearningObject> getLearningObjectMap() {
        return learningObjectMap;
    }
}
