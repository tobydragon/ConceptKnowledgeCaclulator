package edu.ithaca.dragon.tecmap;

import edu.ithaca.dragon.tecmap.conceptgraph.ConceptGraph;
import edu.ithaca.dragon.tecmap.conceptgraph.TreeConverter;
import edu.ithaca.dragon.tecmap.io.record.ConceptGraphRecord;
import edu.ithaca.dragon.tecmap.io.record.LearningResourceRecord;
import edu.ithaca.dragon.tecmap.tecmapstate.AssessmentItemsAddedState;
import edu.ithaca.dragon.tecmap.tecmapstate.BaseState;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Tecmap implements TecmapAPI {

    private BaseState state;

    public Tecmap(String structureFileName) throws IOException {
        state = new BaseState(structureFileName);
    }

    public Tecmap(String structureFileName, List<String> assessmentFilenames) throws IOException {
        state = new AssessmentItemsAddedState(structureFileName, assessmentFilenames);
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
        if (state instanceof AssessmentItemsAddedState) {
            return ((AssessmentItemsAddedState)state).createBlankLearningResourceRecordsFromAssessment();
        }
        else {
            return new ArrayList<>();
        }
    }

}
