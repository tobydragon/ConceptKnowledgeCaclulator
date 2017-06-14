package edu.ithaca.dragonlab.ckc.ui;

import edu.ithaca.dragonlab.ckc.ConceptKnowledgeCalculator;
import edu.ithaca.dragonlab.ckc.ConceptKnowledgeCalculatorAPI;
import edu.ithaca.dragonlab.ckc.conceptgraph.ConceptGraph;
import edu.ithaca.dragonlab.ckc.conceptgraph.ConceptNode;
import edu.ithaca.dragonlab.ckc.suggester.SuggestionResource;

import java.io.IOException;
import java.util.*;

/**
 * Created by tdragon on 6/8/17.
 */
public class ConsoleUI {
    //private ConceptKnowledgeCalculatorAPI ckc;

    public ConceptKnowledgeCalculatorAPI ckc;

    public ConsoleUI(String structureFileName) {
        try {
            ckc = new ConceptKnowledgeCalculator(structureFileName);
        } catch (Exception e) {
            System.out.println("Unable to load default files, please choose files manually.");
            ckc = new ConceptKnowledgeCalculator();
        }
        run();

    }

    public ConsoleUI(String structureFilename, String resourceFilename, String assessmentFilename) {
        try {
            ckc = new ConceptKnowledgeCalculator(structureFilename, resourceFilename, assessmentFilename);
        } catch (Exception e) {
            System.out.println("Unable to load default files, please choose files manually.");
            ckc = new ConceptKnowledgeCalculator();
        }
        run();
    }



    public void run(){
        System.out.println("Current graphs:\t" + ckc.getCohortGraphsUrl());

        Scanner scanner = new Scanner(System.in);

        ConceptKnowledgeCalculator.Mode mode = ckc.getCurrentmode();

        int contQuit = 1;

        while (contQuit == 1) {
            if (mode == ConceptKnowledgeCalculator.Mode.STRUCTUREGRAPH) {
                System.out.println("What do you want to do? \n 1- replace graph \n 2- add Learning Object and Learning Object Resources");
                Integer num = scanner.nextInt();
                while (num < 1 || num > 2) {
                    System.out.println("Out of bounds");
                    System.out.println("What do you want to do? \n 1- replace graph \n 2- add Learning Object and Learning Object Resources");
                    num = scanner.nextInt();
                }

                scanner.nextLine();

                if (num==1){
                    System.out.println("replace graph");
                    System.out.println("What file do you want to replace with?");
                    String file = scanner.nextLine();

                    try {
                        ckc.clearAndCreateStructureData(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }else{
                    System.out.println("add LO and LOR");
                    System.out.println("Learning Object File (starting from root)");
                    String LOfile = scanner.nextLine();

                    System.out.println("Learning Object Resources File (starting from root)");
                    String LORFile = scanner.nextLine();

//                    ckc.clearAndCreateCohortData();


                }


            } else {

                System.out.println("What do you want to do? \n 1 - calculate a list of concept nodes to work on \n 2 - calculate learning object suggestions based on a specific concept \n 3 - automatically calculate suggestions \n 4 - View graph \n 5 - Create new graph ");
                Integer num = scanner.nextInt();

                while (num < 1 || num > 5) {
                    System.out.println("Out of bounds");
                    System.out.println("What do you want to do? \n 1 - calculate a list of concept nodes to work on \n 2 - calculate learning object suggestions based on a specific concept \n 3 - automatically calculate suggestions \n 4 - View graph \n 5 - Create new graph ");
                    num = scanner.nextInt();
                }

                scanner.nextLine();

                if (num == 1) {
                    System.out.println("calculate a list of concept nodes to work ");
                    System.out.println("User ID");
                    String userID = scanner.nextLine();

                    List<ConceptNode> suggestedConceptNodeList = ckc.calcIndividualConceptNodesSuggestions(userID);
                    System.out.println(suggestedConceptNodeList);

                } else if (num == 2) {
                    System.out.println("calculate learning object suggestions based on a specific concept");
                    System.out.println("User ID");
                    String userID = scanner.nextLine();

                    System.out.println("Concept ID");
                    String conceptID = scanner.nextLine();
                    SuggestionResource sugRes = ckc.calcIndividualSpecificConceptSuggestions(userID, conceptID);

                    System.out.println(" 1- Try something new 2- try again");
                    Integer option = scanner.nextInt();
                    while (option > 2 || option < 1) {
                        System.out.println(" 1- Try something new 2- try again");
                        option = scanner.nextInt();
                    }
                    if (option == 1) {
                        System.out.println(sugRes.incompleteList);
                    } else {
                        System.out.println(sugRes.wrongList);
                    }

                } else if (num == 3) {
                    System.out.println("automatically calculate suggestions");
                    System.out.println("User ID");
                    String userID = scanner.nextLine();

                    SuggestionResource sugRes = ckc.calcIndividualGraphSuggestions(userID);

                    System.out.println(" 1- Try something new 2- try again");
                    Integer option = scanner.nextInt();
                    while (option > 2 || option < 1) {
                        System.out.println(" 1- Try something new 2- try again");
                        option = scanner.nextInt();
                    }
                    if (option == 1) {
                        System.out.println(sugRes.incompleteList);
                    } else {
                        System.out.println(sugRes.wrongList);
                    }

                } else if (num == 4) {
                    System.out.println("view graph");

                    ckc.getCohortGraphsUrl();

                } else {
                    System.out.println("create new graph ");

                    System.out.println("Structure");
                    String structure = scanner.nextLine();

                    System.out.println("Resources");
                    String resource = scanner.nextLine();

                    System.out.println("Assessment");
                    String assessment = scanner.nextLine();

                    try {
                        ckc.clearAndCreateCohortData(structure, resource, assessment);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
                System.out.println("1- continue 2- quit");
                contQuit = scanner.nextInt();

                while (contQuit != 1 && contQuit != 2) {
                    System.out.println("Out of bounds");
                    System.out.println("1- continue 2- quit");
                    contQuit = scanner.nextInt();
                }

        }



    }
}
