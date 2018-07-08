package edu.ithaca.dragon.tecmap;

import edu.ithaca.dragon.tecmap.conceptgraph.ConceptGraph;
import edu.ithaca.dragon.tecmap.io.record.CohortConceptGraphsRecord;
import edu.ithaca.dragon.tecmap.io.record.ConceptGraphRecord;
import edu.ithaca.dragon.tecmap.io.record.LearningResourceRecord;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItem;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItemResponse;
import edu.ithaca.dragon.tecmap.tecmapstate.AssessmentAddedState;
import edu.ithaca.dragon.tecmap.tecmapstate.AssessmentConnectedState;
import edu.ithaca.dragon.tecmap.tecmapstate.NoAssessmentState;
import edu.ithaca.dragon.tecmap.tecmapstate.TecmapState;

import java.util.ArrayList;
import java.util.List;

public class Tecmap implements TecmapAPI {

    protected NoAssessmentState state;

    /**
     *
     * @param structureGraph
     * @param links a list of LearningResourceRecords, or null if data is not connected to the graph
     * @param assessmentItemsStructureList a list of AssessmentItems to copy for the structure of the graph
     * @param assessmentItemResponses a list of AssessmentItemResponses containing all data (not connectes to the structureList above)
     */
    public Tecmap(ConceptGraph structureGraph,  List<LearningResourceRecord> links, List<AssessmentItem> assessmentItemsStructureList, List<AssessmentItemResponse> assessmentItemResponses) {
        TecmapState stateEnum = TecmapState.checkAvailableState(links, assessmentItemResponses);
        if (stateEnum == TecmapState.noAssessment){
            state = new NoAssessmentState(structureGraph);
        }
        else if (stateEnum == TecmapState.assessmentAdded){
            state = new AssessmentAddedState(structureGraph, assessmentItemsStructureList, assessmentItemResponses);
        }
        else if (stateEnum == TecmapState.assessmentConnected){
            state = new AssessmentConnectedState(structureGraph, links, assessmentItemsStructureList, assessmentItemResponses);
        }
        else {
            throw new RuntimeException("State not recognized, cannot build");
        }

    }

    @Override
    public ConceptGraphRecord createStructureTree() {
        return state.createStructureTree();
    }

    @Override
    public List<String> conceptIdList() {
        return state.conceptIdList();
    }

    @Override
    public List<LearningResourceRecord> currentLearningResourceRecords() {
        if (state instanceof AssessmentConnectedState){
            return ((AssessmentConnectedState)state).getResourceRecordLinks();
        }
        else if (state instanceof AssessmentAddedState) {
            return ((AssessmentAddedState)state).createBlankLearningResourceRecordsFromAssessment();
        }
        else {
            return new ArrayList<>();
        }
    }

    @Override
    public CohortConceptGraphsRecord createCohortTree() {
        if (state instanceof AssessmentConnectedState) {
            return ((AssessmentConnectedState)state).createCohortTree();
        }
        else {
            return null;
        }
    }

    @Override
    public TecmapState getCurrentState() {
        if (state instanceof AssessmentConnectedState) {
            return TecmapState.assessmentConnected;
        }
        else if (state instanceof AssessmentAddedState) {
            return TecmapState.assessmentAdded;
        }
        else {
            return TecmapState.noAssessment;
        }
    }


}
