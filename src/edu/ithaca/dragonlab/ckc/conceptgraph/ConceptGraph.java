package edu.ithaca.dragonlab.ckc.conceptgraph;

import edu.ithaca.dragonlab.ckc.io.*;
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

	public ConceptGraph(ConceptGraphRecord structureDef){
		buildStructureFromGraphRecord(structureDef);
		this.learningObjectMap = new HashMap<>();
	}

	public ConceptGraph(ConceptGraphRecord structureDef, List<LearningObjectLinkRecord> lolRecords, List<LearningObjectResponse> learningObjectsResponses){
	    this(structureDef);
	    addLearningObjectsFromLearningObjectLinkRecords(lolRecords);
	    addLearningObjectResponses(learningObjectsResponses);
    }

    public void addLearningObjectResponses(List<LearningObjectResponse> learningObjectsResponses) {
        for (LearningObjectResponse response : learningObjectsResponses){
            LearningObject resource = learningObjectMap.get(response.getLearningObjectId());
            if (resource != null){
                resource.addResponse(response);
            }
            else {
                logger.warn("No matching learning object for response" + response);
            }
        }
    }

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
	 * Creates all connections listed in @links between the nodes that already exist
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

	//TODO: When book code is integrated
//	public ConceptGraph(Book b, ConceptGraphRecordOld lists){
//		List<ConceptNode> nodes = lists.getConcepts();
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

//    public void addLearningObjects(ConceptGraphRecordOld learningObjectDef){
//        //learningObjects might need to be created or they might already exist
//		for (ConceptNode learningObjectRecord : learningObjectDef.getNodes()){
//            LearningObject learningObject = learningObjectMap.get(learningObjectRecord.getID());
//			if (learningObject == null) {
//				learningObjectMap.put(learningObjectRecord.getID(), new LearningObject(learningObjectRecord.getID()));
//			}
//        }
//
//        for (LinkRecord link : learningObjectDef.getLinks()){
//            ConceptNode node = nodeMap.get(link.getParent());
//            if (node != null){
//            	LearningObject learningObject = learningObjectMap.get(link.getChild());
//            	if (learningObject != null){
//					node.addLearningObject(learningObject);
//				}
//                else {
//					logger.warn("Adding a Learning Object:" + link.getChild() + " has no matching learning object definition:" + link.getChild());
//				}
//            }
//            else {
//                logger.warn("Adding a Learning Object:" + link.getChild() + " has no matching node in the graph:" + link.getParent());
//            }
//        }
//    }

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


	
	public String toString(){
		ConceptGraphRecord thisGraph = this.buildNodesAndLinks();
		return "Nodes:\n"+thisGraph.getConcepts()+"\nLinks:\n"+thisGraph.getLinks();
		
		//return roots.toString();
		
	}
		
	public ConceptGraphRecord buildNodesAndLinks() {
		List<ConceptRecord> tempNodes = new ArrayList<>();
		List<LinkRecord> tempLinks = new ArrayList<>();
		for(ConceptNode currRoot : this.roots){
			currRoot.addToNodesAndLinksLists(tempNodes,tempLinks);
		}
		ConceptGraphRecord graphRecord = new ConceptGraphRecord(tempNodes, tempLinks);
		return graphRecord;
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

	/**
	 * Takes a Learning Object and a list of concept Id's it connects to. Adds the learning object to the corresponding concepts
	 * If a concept doesn't exist it is logged in the logger
	 * If the learning Object already exists in the graph that is recorded in the logger and nothing happens
	 * @param toLink - the learning object that is going to be linked to the incoming concept IDs
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
				linkLearningObject(toLink,id);
			} else{
				logger.warn("Authoring Warning: Concept '"+id+"' is not in your concept map. "+toLink.getId()+" was not added to the map under the concept ID "+id);
			}
		}
		return numAdded;
	}


	/**
	 * Links a learning object to a concept in the node map
	 * @param toLink - learning object being linked to concept
	 * @param conceptId - ID of the concept the learning object will be linked to
	 */
	private void linkLearningObject(LearningObject toLink, String conceptId){
		ConceptNode current = nodeMap.get(conceptId);
		current.addLearningObject(toLink);
	}

	/**
	 * creates learningObjects and links them to concepts based on a list of learningObjectLinkRecords
	 * @param learningObjectLinkRecords - list of learningObjectLinkRecords
	 */
	public void addLearningObjectsFromLearningObjectLinkRecords(List<LearningObjectLinkRecord> learningObjectLinkRecords){

		for (LearningObjectLinkRecord record: learningObjectLinkRecords){
			LearningObject learningObject = new LearningObject(record);

            linkLearningObjects(learningObject, record.getConceptIds());
		}
	}
	
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
