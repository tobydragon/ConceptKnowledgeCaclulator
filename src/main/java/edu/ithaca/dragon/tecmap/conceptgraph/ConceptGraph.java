package edu.ithaca.dragon.tecmap.conceptgraph;

import edu.ithaca.dragon.tecmap.io.record.ConceptGraphRecord;
import edu.ithaca.dragon.tecmap.io.record.ConceptRecord;
import edu.ithaca.dragon.tecmap.io.record.LearningResourceRecord;
import edu.ithaca.dragon.tecmap.io.record.LinkRecord;
import edu.ithaca.dragon.tecmap.learningresource.ColumnItem;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItemResponse;
import edu.ithaca.dragon.tecmap.learningresource.LearningMaterial;
import edu.ithaca.dragon.tecmap.learningresource.LearningResourceType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;


public class  ConceptGraph {
    private static final Logger logger = LogManager.getLogger(ConceptGraph.class);

	private String name;
	private List<ConceptNode> roots;

	//This information is a duplicate, nodes and learning objects will be found within other nodes,
    // but also can be quickly looked up here (also allows checks for duplicates).
	private Map<String, ConceptNode> nodeMap;
    private Map<String, ColumnItem> assessmentItemMap;
    private Map<String, LearningMaterial> learningMaterialMap;


    public ConceptGraph(ConceptGraphRecord structureDef){
        this.name = structureDef.getName();
        buildStructureFromGraphRecord(structureDef);
        this.assessmentItemMap = new HashMap<>();
        this.learningMaterialMap = new HashMap<>();
    }

    public ConceptGraph(ConceptGraphRecord structureDef, List<LearningResourceRecord> lolRecords){
        this(structureDef);
        addLearningResourcesFromRecords(lolRecords);
    }

    public ConceptGraph(ConceptGraphRecord structureDef, List<LearningResourceRecord> lolRecords, List<AssessmentItemResponse> AssessmentItemResponses){
        this(structureDef, lolRecords);
        addAssessmentItemResponses(AssessmentItemResponses);
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
     * Use this copy constructor (with a copied learning map, see ColumnItem.copyLearningObjectMap)
     * when you want a copy of the graph including a copy of the LearningObjects and LearningObjectResponses
     * e.g., when you are making a tree copy
     * @param other a graph to copy
     * @param newName the new name of the graph
     * @param loMap a map of learning objects, which can be in any state of populated/not populated. This function will
     *              connect the existing learning obejcts, or add any new ones necessary
     */
    public ConceptGraph(ConceptGraph other, String newName, HashMap<String, ColumnItem> loMap, HashMap<String, LearningMaterial> lmMap){
        this.name = newName;
        this.roots = new ArrayList<>();
        nodeMap = new HashMap<>();
        assessmentItemMap = loMap;
        learningMaterialMap = lmMap;

        //recursively copy entire graph
        for (ConceptNode otherRoot : other.roots) {
            ConceptNode newRoot = new ConceptNode(otherRoot, nodeMap, assessmentItemMap, learningMaterialMap);
            nodeMap.put(newRoot.getID(), newRoot);
            this.roots.add(newRoot);
        }
    }

    /**
     * Copies only graph structure, ** DOES NOT have proper assessmentItemMap or nodeMap **
     * Used only by TreeConverter
     * @param rootsIn
     */
    public ConceptGraph(List<ConceptNode> rootsIn, String name, Map<String, ColumnItem> AssessmentItemMap, Map<String, LearningMaterial> learningMaterialMap, Map<String, ConceptNode> nodeMap){
        this.name = name;
        this.assessmentItemMap = AssessmentItemMap;
        this.learningMaterialMap = learningMaterialMap;
        this.nodeMap = nodeMap;
        this.roots = rootsIn;
    }

    /**
     * Initializes a graph only (no assessments or materials)
     * @param graphRecord
     * @post sets roots and nodeMap, completely overwrite any previous graph
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
     * initializes nodes' child lists
     * @pre nodes must already exist for all ids listed in links (need to call buildStructureFromGraphRecord first)
     * @param links
     * @post this graph's ConceptNodes are altered to include the new links
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
     * adds materials and assessments to the graph
     * @param learningResourceRecords
     * @post all given resources are added to either the materials or the assessments maps for the appropriate nodes
     */
    public void addLearningResourcesFromRecords(List<LearningResourceRecord> learningResourceRecords){
        for (LearningResourceRecord record: learningResourceRecords){
            //set defaults if there aren't any resource types
            if (record.getResourceTypes().size() == 0){
                record.setResourceTypes(LearningResourceType.getDefaultResourceTypes());
            }

            //Create duplicate objects (a material and an assessment) if one resource is both
            if (record.isType(LearningResourceType.ASSESSMENT)){
                linkAssessmentItem(new ColumnItem(record), record.getConceptIds());
            }
            if (record.isType(LearningResourceType.INFORMATION) || record.isType(LearningResourceType.PRACTICE)){
                //since we've already added possibly an assessment for this record, remove it (if it were there) so the list can be used to create the material directly from the list
                record.getResourceTypes().remove(LearningResourceType.ASSESSMENT);
                linkLearningMaterials(new LearningMaterial(record), record.getConceptIds());
            }
        }
    }

    /**
     * Takes an ColumnItem and a list of Concept Id's it connects to. Adds the ColumnItem to the graph and to the corresponding concepts
     * If the ColumnItem already exists in the graph then a warning is logged and nothing happens
     * @param toLink - the ColumnItem that is going to be linked to the incoming Concept IDs (cannot already be part of the graph)
     * @param conceptIds - list of strings of the Concept IDs the ColumnItem will be linked to
     * @post   the ColumnItem is added to the graph's map, and to all associated Concept's ColumnItem maps
     * @return the number of concepts the ColumnItem was added to, or -1 if the ColumnItem already exists
     */
    public int linkAssessmentItem (ColumnItem toLink, Collection<String> conceptIds){
        int numAdded = 0;
        if (assessmentItemMap.get(toLink.getId()) != null){
            logger.warn(toLink.getId()+" already exists in this graph. Nothing was added.");
            return -1;
        }
        assessmentItemMap.put(toLink.getId(), toLink);
        for (String id: conceptIds){
            if(nodeMap.get(id) != null){
                numAdded++;
                ConceptNode current = nodeMap.get(id);
                current.addAssessmentItem(toLink);
            } else{
                logger.warn("Authoring Warning: Concept '"+id+"' is not in your Concept map. "+toLink.getId()+" was not added to the map under the Concept ID "+id);
            }
        }
        return numAdded;
    }

    /**
     * Adds the LearningMaterial to the graph and to the corresponding concepts
     * If the LearningMaterial already exists in the graph then a warning is logged and nothing happens
     * @param toLink - the LearningMaterial that is going to be linked to the incoming Concept IDs (cannot already be part of the graph)
     * @param conceptIds - list of strings of the Concept IDs the LearningMaterial will be linked to
     * @post   the LearningMaterial is added to the graph's map, and to all associated Concept's ColumnItem maps
     * @return the number of concepts the LearningMaterial was added to, or -1 if the LearningMaterial already exists
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


    /**
     * connects AssessmentItemResponse to the appropriate ColumnItem in this graph
     * Can be called multiple times with different lists of responses
     * @param assessmentItemResponses
     */
    public void addAssessmentItemResponses(List<AssessmentItemResponse> assessmentItemResponses) {
        for (AssessmentItemResponse response : assessmentItemResponses){
            ColumnItem resource = assessmentItemMap.get(response.getLearningObjectId());
            if (resource != null){
                resource.addResponse(response);
            }
            else {
                logger.warn("No ColumnItem:" + response.getLearningObjectId() + " for response: " + response.toString());
                //TODO: maybe make a new list of unconnected learning objects???
            }
        }
    }

    /**
     * for each LearningMaterial, calculates the number of times that LearningMaterial is linked to a concept
     * @return a map from id -> count of connections to concepts
     */
    public Map<String, Integer> buildDirectConceptLinkCount(){
        Map<String, Integer> directConceptLinkCountMap = new HashMap<>();
        for (ConceptNode node: nodeMap.values()){
            for (LearningMaterial learningMaterial : node.getLearningMaterialMap().values()){
                if(directConceptLinkCountMap.containsKey(learningMaterial.getId())){
                    directConceptLinkCountMap.put(learningMaterial.getId(),directConceptLinkCountMap.get(learningMaterial.getId())+1);
                }else{
                    directConceptLinkCountMap.put(learningMaterial.getId(), 1);
                }
            }
        }
        return directConceptLinkCountMap;
    }

    /**
     * for each LearningMaterial, calculates the number of paths to that learning material from the given node
     * @return a map from id -> pathCount to the given conceptNode
     */
	public Map<String,Integer> buildLearningMaterialPathCount(String node) {

		ConceptNode findNode = findNodeById(node);
		if (findNode != null) {
			Map<String, Integer> idToPathCount = new HashMap<>();
			findNode.buildLearningMaterialPathCount(idToPathCount);
            return idToPathCount;
		}else{
			logger.warn("buildLearningMaterialPathCount:" + node + " is not found in the graph");
			return null;
		}
	}

    /**
     * creates a mapping of labels to all of the ids that share that label
     * @return a map of label -> list of ids for all labels present
     */
    public Map<String, List<String>> createSameLabelMap(){
        Map<String, List<String>> labelMap = new HashMap<>();
        for (ConceptNode curConcept : nodeMap.values()){
            List<String> currList = labelMap.get(curConcept.getLabel());
            if (currList == null){
                currList = new ArrayList<>();
                labelMap.put(curConcept.getLabel(), currList);
            }
            currList.add(curConcept.getID());
        }
        return labelMap;
    }


    /**
     * calculates the sum of all knowledge estimates for all nodes in the graph
     * @return the sum
     */
    public double calcTotalKnowledgeEstimate() {
        double ex = 0;
        for(ConceptNode roots: this.getRoots()){
            double total = roots.totalKnowledgeEstimateForThisAndAllDescendants(new ArrayList<>());
            ex+= total;

        }
        return ex;
    }

    /**
     * calculates the sum of all knowledge estimates for all nodes below a certain node in the graph
     * @param startingNodeId the id of the node to start with
     * @return the sum
     */
    public double calcTotalKnowledgeEstimate(String startingNodeId){
        ConceptNode node = this.findNodeById(startingNodeId);
        return node.totalKnowledgeEstimateForThisAndAllDescendants(new ArrayList<>());
    }

    public int responsesCount(){
	    int total = 0;
	    for (ColumnItem lo : assessmentItemMap.values()){
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

    /**
     * Gets list of assessmentItems that are below the highest occurrence of the given assessmentId for each root
     * @param assessmentId
     * @return
     */
	public List<String> getAssessmentsBelowAssessmentID(String assessmentId) {
        List<ConceptNode> nodesWithAssessmentId = getNodesWithAssessmentId(assessmentId);

        List<Integer> assessmentNodesPathLengths = new ArrayList<>();
        for (ConceptNode node : nodesWithAssessmentId) {
            assessmentNodesPathLengths.add(node.getLongestPathToLeaf());
        }

        if (assessmentNodesPathLengths.size() == 0) {
            return getAssessmentsBelowAssessmentID(assessmentId, 0);
        } else {
            return getAssessmentsBelowAssessmentID(assessmentId, Collections.max(assessmentNodesPathLengths));
        }
    }

    /**
     * Gets list of assessmentItems that are x steps below the highest occurrence of the given assessmentId for each root
     * @param assessmentId
     * @param stepsDown number of steps to take
     * @return List of assessments x steps down, null if there are negative stepsDown
     */
    public List<String> getAssessmentsBelowAssessmentID(String assessmentId, int stepsDown) {
        if (stepsDown < 0) {
            return null;
        }
        List<ConceptNode> nodesWithAssessmentId = getNodesWithAssessmentId(assessmentId);

        //Dictates that an assessmentId will only be included once
        Set<String> assessmentsBelow = new HashSet<>();
        for (ConceptNode node : nodesWithAssessmentId) {
            getAssessmentsBelowNode(assessmentsBelow, node, stepsDown);
            for (ColumnItem item : node.getAssessmentItemMap().values()) {
                if (!assessmentsBelow.contains(item.getId()) && !item.getId().equals(assessmentId)) {
                    assessmentsBelow.add(item.getId());
                }
            }
        }

        return new ArrayList<>(assessmentsBelow);
    }

    /**
     * Finds the nodes from the graph with the given assessmentId in its list of assessments
     * @param assessmentId
     * @return
     */
    public List<ConceptNode> getNodesWithAssessmentId(String assessmentId) {
        List<ConceptNode> nodesWithAssessmentId = new ArrayList<>();
        //Find the nodes with the given AssessmentId
        for (ConceptNode node : nodeMap.values()) {
            for (ColumnItem item : node.getAssessmentItemMap().values()) {
                if (item.getId().equals(assessmentId)) {
                    nodesWithAssessmentId.add(node);
                }
            }
        }
        return nodesWithAssessmentId;
    }

    /**
     * Returns a set of assessmentIds(dictating that each is only included once) x steps below a given node
     * Recursive function
     * @param assessmentList
     * @param startNode
     * @param stepsToTake
     * @return
     */
    public Set<String> getAssessmentsBelowNode(Set<String> assessmentList, ConceptNode startNode, int stepsToTake) {
        if (stepsToTake == 0) {
            return assessmentList;
        } else {
            for (ConceptNode child : startNode.getChildren()) {
                for (ColumnItem item : child.getAssessmentItemMap().values()) {
                    assessmentList.add(item.getId());
                }
                assessmentList = getAssessmentsBelowNode(assessmentList, child, stepsToTake-1);
            }
            return assessmentList;
        }
    }

    ////////////////////////////////////////////  Simple Functions    //////////////////////////////////////

    public ConceptNode findNodeById(String id){
	    return nodeMap.get(id);
	}

	public Collection<String> getAllNodeIds(){
		return nodeMap.keySet();
	}

    public Map<String, ColumnItem> getAssessmentItemMap() {
	    return assessmentItemMap;
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
}
