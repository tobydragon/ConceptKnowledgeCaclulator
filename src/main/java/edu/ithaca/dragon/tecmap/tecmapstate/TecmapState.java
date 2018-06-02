package edu.ithaca.dragon.tecmap.tecmapstate;

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
}
