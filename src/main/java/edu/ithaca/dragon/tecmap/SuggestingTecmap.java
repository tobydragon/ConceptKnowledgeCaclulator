package edu.ithaca.dragon.tecmap;

import edu.ithaca.dragon.tecmap.suggester.OrganizedLearningResourceSuggestions;
import edu.ithaca.dragon.tecmap.tecmapstate.AssessmentConnectedState;
import edu.ithaca.dragon.tecmap.tecmapstate.TecmapState;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SuggestingTecmap extends Tecmap implements SuggestingTecmapAPI{

    public SuggestingTecmap(String structureFileName, List<String> resourceConnectionFiles, List<String> assessmentFilenames) throws IOException {
        super(structureFileName, resourceConnectionFiles, assessmentFilenames);
    }

    public List<String> suggestConceptsForUser(String userID){
//        if (state instanceof AssessmentConnectedState){
//
//        }
//        else {
            return new ArrayList<>();
//        }
    }  //calcIndividualConceptNodesSuggestions​

    public OrganizedLearningResourceSuggestions suggestResourcesForUser (String userId){
        return null;
    } // //calcIndividualGraphSuggestions

    public OrganizedLearningResourceSuggestions suggestResourcesForSpecificConceptForUser(String userId, String conceptId){
        return null;
    } //calcIndividualSpecificConceptSuggestions
}
