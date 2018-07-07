package edu.ithaca.dragon.tecmap.tecmapstate;

import edu.ithaca.dragon.tecmap.conceptgraph.CohortConceptGraphs;
import edu.ithaca.dragon.tecmap.conceptgraph.ConceptGraph;
import edu.ithaca.dragon.tecmap.io.record.CohortConceptGraphsRecord;
import edu.ithaca.dragon.tecmap.io.record.LearningResourceRecord;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItem;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItemResponse;

import java.io.IOException;
import java.util.List;

public class AssessmentConnectedState extends AssessmentAddedState {

    private CohortConceptGraphs cohortConceptGraphs;
    private List<LearningResourceRecord> links;


    public AssessmentConnectedState(ConceptGraph structureGraph,
                                    List<LearningResourceRecord> links,
                                    List<AssessmentItem> assessmentItemsStructureList,
                                    List<AssessmentItemResponse> assessmentItemResponses) {
        super(structureGraph, assessmentItemsStructureList, assessmentItemResponses);
        this.links = links;
        structureGraph.addLearningResourcesFromRecords(links);
        cohortConceptGraphs = new CohortConceptGraphs(structureGraph, assessmentItemResponses);
    }

    public CohortConceptGraphsRecord createCohortTree(){
        return cohortConceptGraphs.buildCohortConceptTreeRecord();
    }

    public ConceptGraph getGraphForUser(String userId){
        return cohortConceptGraphs.getUserGraph(userId);
    }

    public CohortConceptGraphs getCohortConceptGraphs() {return cohortConceptGraphs;}

}
