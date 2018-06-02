package edu.ithaca.dragon.tecmap;

import edu.ithaca.dragon.tecmap.io.record.CohortConceptGraphsRecord;
import edu.ithaca.dragon.tecmap.io.record.ConceptGraphRecord;
import edu.ithaca.dragon.tecmap.io.record.LearningResourceRecord;
import edu.ithaca.dragon.tecmap.tecmapstate.AssessmentAddedState;
import edu.ithaca.dragon.tecmap.tecmapstate.AssessmentConnectedState;
import edu.ithaca.dragon.tecmap.tecmapstate.NoAssessmentState;
import edu.ithaca.dragon.tecmap.tecmapstate.TecmapState;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Tecmap implements TecmapAPI {

    private NoAssessmentState state;

    public Tecmap(String structureFileName) throws IOException {
        this(structureFileName, null, null);
    }

    public Tecmap(String structureFileName, List<String> assessmentFilenames) throws IOException {
        this(structureFileName, null, assessmentFilenames);
    }

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
