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
        boolean structFileNameValid = true;
        ckc.setLastWorkingStructureName(ckc.getStructureFileName());


        int contQuit = 1;
        while (contQuit == 1) {
            ConceptKnowledgeCalculator.Mode mode = ckc.getCurrentmode();

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
                        System.out.println("Process Completed");


                    } catch (Exception e) {
                        System.out.println("Cannot find the file");
                        structFileNameValid=false;
                        try {
                            ckc.clearAndCreateStructureData(ckc.getLastWorkingStructureName());
                            ckc.setStructureFileName(ckc.getLastWorkingStructureName());
                            System.out.println("Last working files used");
                        } catch (IOException e1) {
                            e1.printStackTrace();
                            System.out.println("Error");
                        }
                    }

                    if(structFileNameValid){
                        ckc.setLastWorkingStructureName(ckc.getStructureFileName());
                    }

                    structFileNameValid=true;

                }else{
                    System.out.println("add LO and LOR");

                    System.out.println("Type learning Object File (starting from root)");
                    String LOfile = scanner.nextLine();

                    System.out.println("Type learning Object Resources File (starting from root)");
                    String LORFile = scanner.nextLine();

                    String strucFile = ckc.getStructureFileName();

                    try {
                        ckc.clearAndCreateCohortData(strucFile, LOfile, LORFile);
                        ckc.setCurrentMode(ConceptKnowledgeCalculator.Mode.COHORTGRAPH);
                        System.out.println("Process Completed");


                    } catch (Exception e) {
                        System.out.println("Cannot find files");

                    }

                }


            } else {

                System.out.println("What do you want to do? \n 1 - calculate a list of concept nodes to work on \n 2 - calculate learning object suggestions based on a specific concept \n 3 - automatically calculate suggestions \n 4 - View graph \n 5 - Create new graph \n 6 - Replace graph file \n 7 - View Structure");
                Integer num = scanner.nextInt();

                while (num < 1 || num > 6) {
                    System.out.println("Out of bounds");
                    System.out.println("What do you want to do? \n 1 - calculate a list of concept nodes to work on \n 2 - calculate learning object suggestions based on a specific concept \n 3 - automatically calculate suggestions \n 4 - View graph \n 5 - Create new graph \n 6 - Replace graph file \n 7 - View Structure");
                    num = scanner.nextInt();
                }

                scanner.nextLine();

                if (num == 1) {
                    System.out.println("calculate a list of concept nodes to work ");
                    System.out.println("User ID");
                    String userID = scanner.nextLine();

                    List<ConceptNode> suggestedConceptNodeList = ckc.calcIndividualConceptNodesSuggestions(userID);
                    //to string
                    System.out.println(suggestedConceptNodeList);

                } else if (num == 2) {
                    System.out.println("calculate learning object suggestions based on a specific concept");
                    System.out.println("User ID");
                    String userID = scanner.nextLine();

                    System.out.println("Concept ID");
                    String conceptID = scanner.nextLine();
                    SuggestionResource sugRes = ckc.calcIndividualSpecificConceptSuggestions(userID, conceptID);

                    System.out.println(" 1- Try something new 2- try something again");
                    Integer option = scanner.nextInt();
                    while (option > 2 || option < 1) {
                        System.out.println(" 1- Try something new 2- try something again");
                        option = scanner.nextInt();
                    }
                    if (option == 1) {
                        //to string
                        System.out.println(sugRes.incompleteList);
                    } else {
                        //to string
                        System.out.println(sugRes.wrongList);
                    }

                } else if (num == 3) {
                    System.out.println("automatically calculate suggestions");
                    System.out.println("User ID");
                    String userID = scanner.nextLine();

                    SuggestionResource sugRes = ckc.calcIndividualGraphSuggestions(userID);

                    System.out.println(" 1- Try something new 2- try something again");
                    Integer option = scanner.nextInt();
                    while (option > 2 || option < 1) {
                        System.out.println(" 1- Try something new 2- try something again");
                        option = scanner.nextInt();
                    }
                    if (option == 1) {
                        //to string
                        System.out.println(sugRes.incompleteList);
                    } else {
                        //to string
                        System.out.println(sugRes.wrongList);
                    }

                } else if (num == 4) {
                    System.out.println("view graph");

                    ckc.getCohortGraphsUrl();

                } else  if(num ==5){
                    System.out.println("create new cohort graphs");

                    System.out.println("Type concept graph path: ");
                    String structure = scanner.nextLine();

                    System.out.println("Type learning object record path: ");
                    String resource = scanner.nextLine();

                    System.out.println("Type grade book path: ");
                    String assessment = scanner.nextLine();

                    try {
                        ckc.clearAndCreateCohortData(structure, resource, assessment);
                        System.out.println("Process Completed");
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("Can't find files");

                    }

                }else if(num ==6){
                    System.out.println("Replace Graph File");





                }else{
                    System.out.println("View Structure graph");

                    try {
                        ckc.clearAndCreateStructureData(ckc.getStructureFileName());
                        ckc.setCurrentMode(ConceptKnowledgeCalculator.Mode.STRUCTUREGRAPH);
                        System.out.println("Process Completed");

                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("Can't find file");
                    }

                }
            }

            //to repeat or break loop in graph mode or structure mode
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
