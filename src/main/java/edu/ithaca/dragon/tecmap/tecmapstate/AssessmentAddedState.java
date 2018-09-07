package edu.ithaca.dragon.tecmap.tecmapstate;

import edu.ithaca.dragon.tecmap.conceptgraph.ConceptGraph;
import edu.ithaca.dragon.tecmap.io.record.LearningResourceRecord;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItem;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItemResponse;

import java.util.List;

public class AssessmentAddedState extends NoAssessmentState {

    // Since these AssessmentItems will be used for the structure graph (which will be copied for each user),
    // the assessmentItemsStructureList should not be connected to the assessmentItemResponses within this object.
    // There will be one copy of each of these assessmentItems for each user in their own graph, and only that data
    // from the AssessmentItemResponses will be linked to that copy
    private List<AssessmentItem> assessmentItemsStructureList;
    //these responses are independent of the items above
    protected List<AssessmentItemResponse> assessmentItemResponses;

    /**
     *
     * @param structureGraph
     * @param assessmentItemsStructureList a list of assessmentItems (with no connected data) that represent all assessmentItems that will be associated with the structureGraph
     * @param assessmentItemResponses a list of assessmentItemResponses that represent all data for the tecmap.
     *                                Note: data should not be already connected to assessmentItemsStructureList (this will happen when the data is connected to the StructureGraph
     */
    public AssessmentAddedState(ConceptGraph structureGraph, List<AssessmentItem> assessmentItemsStructureList, List<AssessmentItemResponse> assessmentItemResponses) {
        super(structureGraph);
        this.assessmentItemsStructureList = assessmentItemsStructureList;
        this.assessmentItemResponses = assessmentItemResponses;
    }

    public List<LearningResourceRecord> createBlankLearningResourceRecordsFromAssessment() {
        return LearningResourceRecord.createLearningResourceRecordsFromAssessmentItems(assessmentItemsStructureList);
    }

}
