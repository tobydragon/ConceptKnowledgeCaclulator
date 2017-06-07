package edu.ithaca.dragonlab.ckc.suggester;

import edu.ithaca.dragonlab.ckc.conceptgraph.ConceptGraph;
import edu.ithaca.dragonlab.ckc.conceptgraph.ConceptNode;

import java.util.*;

/**
 * Created by Mia Kimmich Mitchell on 6/6/2017.
 */
public class SuggestionResource {

    HashMap<String, List<LearningObjectSuggestion>> masterMap;

    HashMap<String, List<LearningObjectSuggestion>> wrongMap;
    HashMap<String, List<LearningObjectSuggestion>> incompleteMap;

    public void fillWrongMap(ConceptGraph graph){
        wrongMap = LearningObjectSuggester.buildSuggestionMap(graph,0);
    }

    public void fillIncompleteMap(ConceptGraph graph){
        incompleteMap = LearningObjectSuggester.buildSuggestionMap(graph,1);
    }

    public void fillMasterMap(){

    }




    //using learningObjectSuggester.buildSuggestionMap()





}
