package edu.ithaca.dragon.tecmap.tecmapstate;

import edu.ithaca.dragon.tecmap.ui.TecmapUserAction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum TecmapState {
    OnlyGraphStructureState,
    LinksNoAssessment,
    AssessmentNoLinks,
    AssessmentLinked;

    public static <LinkRecordType, AssessmentRecordType> TecmapState checkAvailableState(List<LinkRecordType> links, List<AssessmentRecordType> assessmentItemResponses) {
        if (assessmentItemResponses == null || assessmentItemResponses.size() < 1){
            if (links == null || links.size() < 1){
                return TecmapState.OnlyGraphStructureState;
            }
            else {
                return TecmapState.LinksNoAssessment;
            }
        }
        else if (links == null || links.size() < 1){
            return TecmapState.AssessmentNoLinks;
        }
        else {
            return TecmapState.AssessmentLinked;
        }
    }

    public List<TecmapUserAction> getAvailableActions(){
        if (this == OnlyGraphStructureState){
            return(new ArrayList<>(Collections.singletonList(TecmapUserAction.structureTree)));
        }
        else if (this == AssessmentNoLinks || this == LinksNoAssessment){
            return(new ArrayList<>(Arrays.asList(TecmapUserAction.structureTree, TecmapUserAction.connectResources)));
        }
        else if (this == AssessmentLinked){
            return(new ArrayList<>(Arrays.asList(TecmapUserAction.structureTree, TecmapUserAction.connectResources, TecmapUserAction.cohortTree)));
        }
        else{
            throw new RuntimeException("Unrecognized state: " + this.toString());
        }
    }
}
