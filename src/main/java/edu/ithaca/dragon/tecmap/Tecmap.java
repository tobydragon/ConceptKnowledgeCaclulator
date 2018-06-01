package edu.ithaca.dragon.tecmap;

import edu.ithaca.dragon.tecmap.io.record.CohortConceptGraphsRecord;
import edu.ithaca.dragon.tecmap.io.record.ConceptGraphRecord;
import edu.ithaca.dragon.tecmap.io.record.LearningResourceRecord;
import edu.ithaca.dragon.tecmap.tecmapstate.AssessmentAddedState;
import edu.ithaca.dragon.tecmap.tecmapstate.AssessmentConnectedState;
import edu.ithaca.dragon.tecmap.tecmapstate.NoAssessmentState;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Tecmap implements TecmapAPI {

    private NoAssessmentState state;

    public Tecmap(String structureFileName) throws IOException {
        state = new NoAssessmentState(structureFileName);
    }

    public Tecmap(String structureFileName, List<String> assessmentFilenames) throws IOException {
        state = new AssessmentAddedState(structureFileName, assessmentFilenames);
    }

    public Tecmap(String structureFileName, List<String> resourceConnectionFiles, List<String> assessmentFilenames) throws IOException {
        state = new AssessmentConnectedState(structureFileName, resourceConnectionFiles, assessmentFilenames);
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

}
