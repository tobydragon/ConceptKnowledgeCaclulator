package edu.ithaca.dragonlab.ckc;

import edu.ithaca.dragonlab.ckc.ui.ConsoleUI;

public class ConceptKnowledgeCalculatorMain {

    public static void main(String[] args) {
//        ConceptKnowledgeCalculator.Mode startMode = ConceptKnowledgeCalculator.Mode.STRUCTUREGRAPH;
        ConceptKnowledgeCalculator.Mode startMode = ConceptKnowledgeCalculator.Mode.COHORTGRAPH;


        if (startMode == ConceptKnowledgeCalculator.Mode.STRUCTUREGRAPH) {
            new ConsoleUI("resources/comp220/comp220Graph.json");
        } else if (startMode == ConceptKnowledgeCalculator.Mode.COHORTGRAPH) {
            new ConsoleUI("test/testresources/basicRealisticExampleConceptGraphOneStudent.json",
                    "test/testresources/basicRealisticExampleLOLRecordOneStudent.json",
                    "test/testresources/basicRealisticExampleGradeBook2.csv");

//            new ConsoleUI("resources/comp220/comp220Graph.json",
//                    "resources/comp220/comp220Resources.json",
//                    "localresources/comp220/comp220ExampleDataPortion.csv");
        } else {
            throw new RuntimeException("Unrecognized starting mode, program cannot execute");
        }
    }
}
