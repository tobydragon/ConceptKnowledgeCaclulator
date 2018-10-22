package edu.ithaca.dragon.tecmap.tecmapstate;

import edu.ithaca.dragon.tecmap.conceptgraph.CohortConceptGraphs;
import edu.ithaca.dragon.tecmap.conceptgraph.ConceptGraph;
import edu.ithaca.dragon.tecmap.io.record.CohortConceptGraphsRecord;
import edu.ithaca.dragon.tecmap.io.record.LearningResourceRecord;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItem;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItemResponse;

import java.util.List;

public class AssessmentConnectedState extends AssessmentAddedState {

    private CohortConceptGraphs cohortConceptGraphs;
    //these link records are also represented within the graph as the connections from concepts to resources
    private List<LearningResourceRecord> links;

    /**
     *
     * @param structureGraph
     * @param links
     * @param columnItemsStructureList
     * @param assessmentItemResponses
     * @param linksNeedToBeAdded if coming from a ResourcesNoAssessmentState, links have already been added
     */
    public AssessmentConnectedState(ConceptGraph structureGraph,
                                    List<LearningResourceRecord> links,
                                    List<AssessmentItem> columnItemsStructureList,
                                    List<AssessmentItemResponse> assessmentItemResponses,
                                    boolean linksNeedToBeAdded) {
        super(structureGraph, columnItemsStructureList, assessmentItemResponses);
        this.links = links;
        if (linksNeedToBeAdded) {
            structureGraph.addLearningResourcesFromRecords(links);
        }
        cohortConceptGraphs = new CohortConceptGraphs(structureGraph, assessmentItemResponses);
    }

    /**
     *
     * @param structureGraph
     * @param links
     * @param columnItemsStructureList
     * @param assessmentItemResponses
     * @post links will be added to structureGraph before cohortGraphs are made
     */
    public AssessmentConnectedState(ConceptGraph structureGraph,
                                    List<LearningResourceRecord> links,
                                    List<AssessmentItem> columnItemsStructureList,
                                    List<AssessmentItemResponse> assessmentItemResponses) {
        this(structureGraph, links, columnItemsStructureList, assessmentItemResponses, true);
    }

    public CohortConceptGraphsRecord createCohortTree(){
        return cohortConceptGraphs.buildCohortConceptTreeRecord();
    }

    public ConceptGraph getGraphForUser(String userId){
        return cohortConceptGraphs.getUserGraph(userId);
    }

    public ConceptGraph getAverageGraph() {
        return cohortConceptGraphs.getAvgGraph();
    }

    public CohortConceptGraphs getCohortConceptGraphs() {return cohortConceptGraphs;}

    public List<LearningResourceRecord> getResourceRecordLinks() {
        return links;
    }
}
