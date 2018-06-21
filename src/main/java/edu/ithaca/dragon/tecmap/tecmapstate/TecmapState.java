package edu.ithaca.dragon.tecmap.tecmapstate;

import edu.ithaca.dragon.tecmap.ui.TecmapUserAction;

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


    public List<TecmapUserAction> getAvailableActions(){
        if (this == noAssessment){
            return(new ArrayList<>(Collections.singletonList(TecmapUserAction.structureTree)));
        }
        else if (this == assessmentAdded){
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
