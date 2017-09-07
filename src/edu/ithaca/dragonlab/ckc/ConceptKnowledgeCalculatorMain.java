package edu.ithaca.dragonlab.ckc;

import edu.ithaca.dragonlab.ckc.ui.ConsoleUI;

public class ConceptKnowledgeCalculatorMain {

    public static void main(String[] args) {
        ConceptKnowledgeCalculator.Mode startMode = ConceptKnowledgeCalculator.Mode.STRUCTUREGRAPH;
//        ConceptKnowledgeCalculator.Mode startMode = ConceptKnowledgeCalculator.Mode.COHORTGRAPH;


        if (startMode == ConceptKnowledgeCalculator.Mode.STRUCTUREGRAPH) {
//            new ConsoleUI("resources/exampleGraph.json");
//            new ConsoleUI("resources/comp220/comp220Graph.json");
//            new ConsoleUI("resources/comp115/comp115Graph.json");
//            new ConsoleUI("resources/comp171/comp171Graph-PaulNate-V1.json");
            new ConsoleUI("resources/comp110/comp110Graph.json");

        } else if (startMode == ConceptKnowledgeCalculator.Mode.COHORTGRAPH) {

//            new ConsoleUI("test/testresources/ManuallyCreated/basicRealisticConceptGraph.json",
//                    "test/testresources/ManuallyCreated/basicRealisticResource.json",
//                    "test/testresources/ManuallyCreated/basicRealisticAssessment.csv");


//                        new ConsoleUI("test/testresources/ManuallyCreated/simpleConceptGraph.json",
//                    "test/testresources/ManuallyCreated/simpleResource.json",
//                    "test/testresources/ManuallyCreated/simpleAssessmentMoreUsers.csv");

//            new ConsoleUI("resources/comp220/comp220Graph.json",
//                    "resources/comp220/comp220Resources.json",
//                    "localresources/comp220/comp220ExampleDataPortion.csv");
//
//            new ConsoleUI("test/testresources/ManuallyCreated/researchConceptGraph.json",
//                    "test/testresources/ManuallyCreated/researchResource1.json",
//                    "test/testresources/ManuallyCreated/researchAssessment1.csv");

//            new ConsoleUI("test/testresources/ManuallyCreated/researchConceptGraph.json",
//                    "test/testresources/ManuallyCreated/researchResource2.json",
//                    "test/testresources/ManuallyCreated/researchAssessment2.csv");

//            new ConsoleUI("resources/comp115/comp115Graph.json",
//                    "resources/comp115/comp115Resources.json",
//                    "resources/comp115/comp115Assessment.csv");

            new ConsoleUI("resources/comp110/comp110Graph.json",
                    "resources/comp110/comp110Resources.json",
                    "resources/comp110/comp110Assessment.csv");

        } else {
            throw new RuntimeException("Unrecognized starting mode, program cannot execute");
        }
    }
}
