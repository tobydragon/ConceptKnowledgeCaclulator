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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Tecmap implements TecmapAPI {

    protected NoAssessmentState state;


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

    /**
     * creates a tecmap in the appropriate state, depending on what files are given
     * @param structureFileName must be a valid json file for structureGraph structure
     * @param resourceConnectionFiles can be a list of filenames, or null if there are no resource connections
     * @param assessmentFilenames can be a list of filenames, or null if there are no assessments
     * @throws IOException
     */
    public Tecmap(String structureFileName, List<String> resourceConnectionFiles, List<String> assessmentFilenames) throws IOException {
        TecmapState stateEnum = TecmapState.checkAvailableState(resourceConnectionFiles, assessmentFilenames);
        if (stateEnum == TecmapState.noAssessment){
            state = new NoAssessmentState(structureFileName);
        }
        else if (stateEnum == TecmapState.assessmentAdded){
            state = new AssessmentAddedState(structureFileName, assessmentFilenames);
        }
        else if (stateEnum == TecmapState.assessmentConnected){
            state = new AssessmentConnectedState(structureFileName, resourceConnectionFiles, assessmentFilenames);
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
    public List<String> createConceptIdListToPrint() {
        return state.createConceptIdListToPrint();
    }

    @Override
    public List<LearningResourceRecord> createBlankLearningResourceRecordsFromAssessment() {
        if (state instanceof AssessmentAddedState) {
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
