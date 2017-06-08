package edu.ithaca.dragonlab.ckc.ui;

import edu.ithaca.dragonlab.ckc.ConceptKnowledgeCalculator;
import edu.ithaca.dragonlab.ckc.ConceptKnowledgeCalculatorAPI;
import edu.ithaca.dragonlab.ckc.conceptgraph.ConceptGraph;

/**
 * Created by tdragon on 6/8/17.
 */
public class ConsoleUI {
    ConceptKnowledgeCalculatorAPI ckc;

    public ConsoleUI(String structureFilename, String resourceFilename, String assessmentFilename){
        try {
            ckc = new ConceptKnowledgeCalculator(structureFilename, resourceFilename, assessmentFilename);
        }
        catch (Exception e){
            System.out.println("Unable to load default files, please choose files manually.");
            ckc = new ConceptKnowledgeCalculator();
        }
        run();
    }

    public void run(){
        System.out.println("Current graphs:\t" + ckc.getCohortGraphsUrl());

        //TODO: Make a menu that allows you to change data, request different kinds of suggestions, or quit
    }
}
