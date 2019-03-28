package edu.ithaca.dragon.tecmap;

import edu.ithaca.dragon.tecmap.conceptgraph.ConceptGraph;
import edu.ithaca.dragon.tecmap.io.record.CohortConceptGraphsRecord;
import edu.ithaca.dragon.tecmap.io.record.ConceptGraphRecord;
import edu.ithaca.dragon.tecmap.io.record.LearningMaterialRecord;
import edu.ithaca.dragon.tecmap.io.record.LearningResourceRecord;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItem;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItemResponse;
import edu.ithaca.dragon.tecmap.tecmapstate.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static edu.ithaca.dragon.tecmap.io.record.LearningMaterialRecord.jsonToLearningMaterialRecords;

public class Tecmap implements TecmapAPI {

    protected OnlyGraphStructureState state; //All states inherit from OnlyGraphStructureState, it could be any child type

    /**
     *
     * @param structureGraph
     * @param links a list of LearningResourceRecords, or null if data is not connected to the graph
     * @param assessmentItemsStructureList a list of AssessmentItems to copy for the structure of the graph
     * @param assessmentItemResponses a list of AssessmentItemResponses containing all data (not connected to the structureList above)
     */
    public Tecmap(ConceptGraph structureGraph, List<LearningResourceRecord> links, List<AssessmentItem> assessmentItemsStructureList, List<AssessmentItemResponse> assessmentItemResponses) {
        TecmapState stateEnum = TecmapState.checkAvailableState(links, assessmentItemResponses);
        if (stateEnum == TecmapState.OnlyGraphStructureState){
            state = new OnlyGraphStructureState(structureGraph);
        }
        else if (stateEnum == TecmapState.LinksNoAssessment){
            state = new LinksNoAssessmentState(structureGraph, links);
        }
        else if (stateEnum == TecmapState.AssessmentNoLinks){
            state = new AssessmentNoLinksState(structureGraph, assessmentItemsStructureList, assessmentItemResponses);
        }
        else if (stateEnum == TecmapState.AssessmentLinked){
            state = new AssessmentLinkedState(structureGraph, links, assessmentItemsStructureList, assessmentItemResponses);
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
        if (state instanceof AssessmentLinkedState){
            return ((AssessmentLinkedState)state).getResourceRecordLinks();
        }
        else if (state instanceof LinksNoAssessmentState) {
            return ((LinksNoAssessmentState)state).getResourceRecordLinks();
        }
        else if (state instanceof AssessmentNoLinksState) {
            return ((AssessmentNoLinksState)state).createBlankLearningResourceRecordsFromAssessment();
        }
        else if (state instanceof OnlyGraphStructureState){
            return new ArrayList<>();
        }
        else {
            throw new RuntimeException("State not recognized");
        }
    }

    @Override
    public CohortConceptGraphsRecord createCohortTree() {
        if (state instanceof AssessmentLinkedState) {
            return ((AssessmentLinkedState)state).createCohortTree();
        }
        else {
            return null;
        }
    }

    @Override
    public ConceptGraph getAverageConceptGraph() {
        if (state instanceof AssessmentLinkedState) {
            return ((AssessmentLinkedState)state).getAverageGraph();
        } else {
            return null;
        }
    }

    public ConceptGraph getConceptGraphForUser(String userId){
        if (state instanceof AssessmentLinkedState) {
            return ((AssessmentLinkedState)state).getGraphForUser(userId);
        } else {
            return null;
        }
    }

    public Map<String, ConceptGraph> getUserToConceptGraphMap(){
        if (state instanceof AssessmentLinkedState) {
            return ((AssessmentLinkedState)state).getCohortConceptGraphs().getUserToGraph();
        } else {
            return null;
        }
    }

    @Override
    public TecmapState getCurrentState() {
        if (state instanceof AssessmentLinkedState) {
            return TecmapState.AssessmentLinked;
        }
        else if (state instanceof AssessmentNoLinksState) {
            return TecmapState.AssessmentNoLinks;
        }
        else if (state instanceof LinksNoAssessmentState) {
            return TecmapState.LinksNoAssessment;
        }
        else {
            return TecmapState.OnlyGraphStructureState;
        }
    }

    @Override
    public List<LearningMaterialRecord> retrieveLearningMaterialRecords() {
        try {
            return jsonToLearningMaterialRecords("src/test/resources/datastore/Cs1Example/Cs1ExampleLearningMaterial.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<LearningMaterialRecord>();
    }


}
