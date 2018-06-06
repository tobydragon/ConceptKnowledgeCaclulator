package edu.ithaca.dragon.tecmap.tecmapstate;

import edu.ithaca.dragon.tecmap.TecmapAction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum TecmapState {
    noAssessment,
    assessmentAdded,
    assessmentConnected;

    public static TecmapState checkAvailableState(List<String> resourceFiles, List<String> assessmentFiles) {
        if (assessmentFiles == null || assessmentFiles.size() < 1){
            return TecmapState.noAssessment;
        }
        else if (resourceFiles == null || resourceFiles.size() < 1){
            return TecmapState.assessmentAdded;
        }
        else {
            return TecmapState.assessmentConnected;
        }
    }


    public List<TecmapAction> getAvailableActions(){
        if (this == noAssessment){
            return(new ArrayList<>(Collections.singletonList(TecmapAction.structureTree)));
        }
        else if (this == assessmentAdded){
            return(new ArrayList<>(Collections.singletonList(TecmapAction.structureTree)));
        }
        else if (this == assessmentConnected){
            return(new ArrayList<>(Arrays.asList(TecmapAction.structureTree, TecmapAction.cohortTree)));
        }
        else{
            throw new RuntimeException("Unrecognized state: " + this.toString());
        }
    }
}
