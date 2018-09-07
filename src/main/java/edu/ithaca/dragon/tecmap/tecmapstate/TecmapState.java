package edu.ithaca.dragon.tecmap.tecmapstate;

import edu.ithaca.dragon.tecmap.io.record.LearningResourceRecord;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItemResponse;
import edu.ithaca.dragon.tecmap.ui.TecmapUserAction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum TecmapState {
    noAssessment,
    resourcesNoAssessment,
    assessmentAdded,
    assessmentConnected;

    public static <LinkRecordType, AssessmentRecordType> TecmapState checkAvailableState(List<LinkRecordType> links, List<AssessmentRecordType> assessmentItemResponses) {
        if (assessmentItemResponses == null || assessmentItemResponses.size() < 1){
            if (links == null || links.size() < 1){
                return TecmapState.noAssessment;
            }
            else {
                return TecmapState.resourcesNoAssessment;
            }
        }
        else if (links == null || links.size() < 1){
            return TecmapState.assessmentAdded;
        }
        else {
            return TecmapState.assessmentConnected;
        }
    }

    public List<TecmapUserAction> getAvailableActions(){
        if (this == noAssessment){
            return(new ArrayList<>(Collections.singletonList(TecmapUserAction.structureTree)));
        }
        else if (this == assessmentAdded || this == resourcesNoAssessment){
            return(new ArrayList<>(Arrays.asList(TecmapUserAction.structureTree, TecmapUserAction.connectResources)));
        }
        else if (this == assessmentConnected){
            return(new ArrayList<>(Arrays.asList(TecmapUserAction.structureTree, TecmapUserAction.connectResources, TecmapUserAction.cohortTree)));
        }
        else{
            throw new RuntimeException("Unrecognized state: " + this.toString());
        }
    }
}
