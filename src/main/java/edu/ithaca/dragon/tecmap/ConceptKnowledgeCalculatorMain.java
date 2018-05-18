package edu.ithaca.dragon.tecmap;

import edu.ithaca.dragon.tecmap.ui.ConsoleUI;

import java.util.Arrays;

public class ConceptKnowledgeCalculatorMain {

    public static void main(String[] args) {
        ConceptKnowledgeCalculator.Mode startMode = ConceptKnowledgeCalculator.Mode.STRUCTUREGRAPH;
//        ConceptKnowledgeCalculator.Mode startMode = ConceptKnowledgeCalculator.Mode.COHORTGRAPH;


        if (startMode == ConceptKnowledgeCalculator.Mode.STRUCTUREGRAPH) {
//            new ConsoleUI(Settings.RESOURCE_DIR + "exampleGraph.json");
//            new ConsoleUI(Settings.RESOURCE_DIR + "comp171/comp171Graph.json");
            new ConsoleUI(Settings.RESOURCE_DIR+"comp171/comp171GraphStudy.json");
//            new ConsoleUI(Settings.RESOURCE_DIR + "comp220/comp220Graph.json");
//            new ConsoleUI(Settings.RESOURCE_DIR + "comp115/comp115Graph.json");
//            new ConsoleUI(Settings.RESOURCE_DIR + "comp171/comp171Graph-PaulNate-V1.json");
//            new ConsoleUI(Settings.RESOURCE_DIR + "comp110/comp110Graph.json");
//            new ConsoleUI(Settings.RESOURCE_DIR + "comp105/comp105Graph.json");

        } else if (startMode == ConceptKnowledgeCalculator.Mode.COHORTGRAPH) {

//            new ConsoleUI(Settings.TEST_RESOURCE_DIR + "ManuallyCreated/basicRealisticConceptGraph.json",
//                    Settings.TEST_RESOURCE_DIR + "ManuallyCreated/basicRealisticResource.json",
//                    Settings.TEST_RESOURCE_DIR + "ManuallyCreated/basicRealisticAssessment.csv");


//                        new ConsoleUI(Settings.TEST_RESOURCE_DIR + "ManuallyCreated/simpleConceptGraph.json",
//                    Settings.TEST_RESOURCE_DIR + "ManuallyCreated/simpleResource.json",
//                    Settings.TEST_RESOURCE_DIR + "ManuallyCreated/simpleAssessmentMoreUsers.csv");

//            new ConsoleUI(Settings.RESOURCE_DIR + "comp220/comp220Graph.json",
//                    Settings.RESOURCE_DIR + "comp220/comp220Resources-e1.json",
//                    Settings.PRIVATE_RESOURCE_DIR +"/comp220/comp220-e1.csv");
//
            new ConsoleUI(Settings.TEST_RESOURCE_DIR + "ManuallyCreated/researchConceptGraph.json",
                    Settings.TEST_RESOURCE_DIR + "ManuallyCreated/researchResource1.json",
                    Settings.TEST_RESOURCE_DIR + "ManuallyCreated/researchAssessment1.csv");

//            new ConsoleUI(Settings.TEST_RESOURCE_DIR + "ManuallyCreated/researchConceptGraph.json",
//                    Settings.TEST_RESOURCE_DIR + "ManuallyCreated/researchResource2.json",
//                    Settings.TEST_RESOURCE_DIR + "ManuallyCreated/researchAssessment2.csv");

//            new ConsoleUI(Settings.RESOURCE_DIR + "comp115/comp115Graph.json",
//                    Settings.RESOURCE_DIR + "comp115/comp115Resources.json",
//                    Settings.PRIVATE_RESOURCE_DIR +"/comp115/comp115Exam3.csv");

//            new ConsoleUI(Settings.RESOURCE_DIR + "comp110/comp110Graph.json",
//                    Settings.RESOURCE_DIR + "comp110/comp110Resources.json",
//                    Settings.RESOURCE_DIR + "comp110/comp110Assessment.csv");
//
//            new ConsoleUI(Settings.RESOURCE_DIR + "comp105/comp105Graph.json",
//                    Settings.RESOURCE_DIR + "comp105/comp105Resources.json",
//                    Settings.PRIVATE_RESOURCE_DIR +"/comp105/COMP105-gradebook.csv");

//            new ConsoleUI(Arrays.asList(Settings.RESOURCE_DIR + "comp220/comp220Graph.json"),
//                    Arrays.asList(Settings.RESOURCE_DIR + "comp220/comp220Resources-courseWork.json",
//                            Settings.RESOURCE_DIR + "comp220/comp220Resources-e1.json",
//                            Settings.RESOURCE_DIR + "comp220/comp220Resources-e2.json",
//                            Settings.RESOURCE_DIR + "comp220/comp220Resources-finalExam.json"),
//                    Arrays.asList(Settings.PRIVATE_RESOURCE_DIR +"/comp220/comp220-courseWork.csv",
//                            Settings.PRIVATE_RESOURCE_DIR +"/comp220/comp220-e1.csv",
//                            Settings.PRIVATE_RESOURCE_DIR +"/comp220/comp220-e2.csv",
//                            Settings.PRIVATE_RESOURCE_DIR +"/comp220/comp220-finalExam.csv") );

//            new ConsoleUI(Arrays.asList(Settings.RESOURCE_DIR + "comp171/comp171Graph.json"),
//                    Arrays.asList(Settings.RESOURCE_DIR + "comp171/comp171Resources-courseWork.json"),
//                    Arrays.asList(Settings.PRIVATE_RESOURCE_DIR +"/comp171/comp171courseWork.csv") );

        } else {
            throw new RuntimeException("Unrecognized starting mode, program cannot execute");
        }
    }
}
