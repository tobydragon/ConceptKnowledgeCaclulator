package edu.ithaca.dragonlab.ckc.ui;

import edu.ithaca.dragonlab.ckc.ConceptKnowledgeCalculator;
import edu.ithaca.dragonlab.ckc.ConceptKnowledgeCalculatorAPI;
import edu.ithaca.dragonlab.ckc.suggester.SuggestionResource;

import java.io.IOException;
import java.util.*;

/**
 * Created by tdragon on 6/8/17.
 */
public class ConsoleUI {
    private ConceptKnowledgeCalculatorAPI ckc;

    public ConsoleUI(String structureFileName) {
        try {
            ckc = new ConceptKnowledgeCalculator(structureFileName);
        } catch (Exception e) {
            System.out.println("Unable to load default files, please choose files manually. Error follows:");
            e.printStackTrace();
            ckc = new ConceptKnowledgeCalculator();
        }
        run();
    }

    public ConsoleUI(String structureFilename, String resourceFilename, String assessmentFilename) {
        try {
            ckc = new ConceptKnowledgeCalculator(structureFilename, resourceFilename, assessmentFilename);
        } catch (Exception e) {
            System.out.println("Unable to load default files, please choose files manually. Error follows:");
            e.printStackTrace();
            ckc = new ConceptKnowledgeCalculator();
        }
        run();
    }


    public void run(){
        Scanner scanner = new Scanner(System.in);

        if(ckc.getCohortConceptGraphs()==null && ckc.getStructureGraph()==null){
            createNewCohortGraph(scanner);
        }

        int contQuit = 1;
        while (contQuit == 1) {
            ConceptKnowledgeCalculator.Mode mode = ckc.getCurrentMode();
            System.out.println("Current Mode: "+ ckc.getCurrentMode());

            if (mode == ConceptKnowledgeCalculator.Mode.STRUCTUREGRAPH) {

                System.out.println("What do you want to do? \n 1 - replace graph  \n 2 - View Graph \n 3 - add resource file (switch to Structure Graph with Resource mode) \n 4 - add assessment file (switch to Structure Graph with Assessment mode) \n 5 - quit");
                Integer num = scanner.nextInt();

                while (num < 1 || num > 5) {
                    System.out.println("Out of bounds");
                    System.out.println("What do you want to do? \n 1 - replace graph  \n 2 - View Graph \n 3 - add resource file (switch to Structure Graph with Resource mode) \n 4 - add assessment file (switch to Structure Graph with Assessment mode) \n 5 - quit");
                    num = scanner.nextInt();
                }
                scanner.nextLine();

                if (num==1){
                    replaceGraph(scanner);

                }else if(num ==2){
                    viewgraph();

                }else if(num ==3){
                    replaceResourceFile(scanner);

                }else if(num==4) {
                    additionalLOR(scanner);

                }else{
                    contQuit = 0;
                }

            } else if(mode == ConceptKnowledgeCalculator.Mode.STRUCTUREGRAPHWITHRESOURCE){

                System.out.println("What do you want to do? \n 1 - replace graph \n 2 - replace resource file \n 3 - add assessment file (switch to cohort mode) \n 4 - view graph \n 5 - quit ");
                Integer num = scanner.nextInt();

                while (num < 1 || num > 5) {
                    System.out.println("Out of bounds");
                    System.out.println("What do you want to do? \n 1 - replace graph \n 2 - replace resource file \n 3 - add assessment file (switch to cohort mode) \n 4 - view graph \n 5 - quit ");
                    num = scanner.nextInt();
                }
                scanner.nextLine();

                if (num==1){
                    replaceGraph(scanner);

                } else if(num==2){
                    replaceResourceFile(scanner);

                }else if(num==3){
                    additionalLOR(scanner);

                }else if(num ==4){
                    viewgraph();

                } else{
                    contQuit=0;
                }

            } else if(mode == ConceptKnowledgeCalculator.Mode.STRUCTUREGRAPHWITHASSESSMENT) {

                System.out.println("What do you want to do? \n 1 - replace graph \n 2 - add assessment file  \n 3 - remove assessment file \n 4 - create resource file  \n 5 - add resource file (switch to cohort mode) \n 6 - view graph \n 7 - quit");
                Integer num = scanner.nextInt();

                while (num < 1 || num > 7) {
                    System.out.println("Out of bounds");
                    System.out.println("What do you want to do? \n 1 - replace graph \n 2 - add assessment file  \n 3 - remove assessment file \n 4 - create resource file  \n 5 - add resource file (switch to cohort mode) \n 6 - view graph \n 7 - quit");
                    num = scanner.nextInt();
                }
                scanner.nextLine();

                if (num == 1) {
                    replaceGraph(scanner);

                } else if (num == 2) {
                    additionalLOR(scanner);

                } else if (num == 3) {
                    removeLORFile(scanner);

                } else if (num == 4) {
                    createResourceFile();

                } else if (num == 5) {
                    replaceResourceFile(scanner);

                } else if (num == 6) {
                    viewgraph();
                } else {
                    contQuit=0;
                }

            }else{
                System.out.println("What do you want to do? \n 1 - calculate a list of concept nodes to work on \n 2 - calculate resources suggestions based on a specific concept \n 3 - automatically calculate suggestions \n 4 - View graph \n 5 - Create new graph \n 6 - Replace graph file \n 7 - Add another assessment file \n 8 - Remove assessment file \n 9 - Replace resource file \n 10 - Get Learning Object Average \n 11 Get Student Average \n 12 - View Structure Graph (switch to structure mode) \n 13 - quit");
                Integer num = scanner.nextInt();

                while (num < 1 || num > 13) {
                    System.out.println("Out of bounds");
                    System.out.println("What do you want to do? \n 1 - calculate a list of concept nodes to work on \n 2 - calculate resource suggestions based on a specific concept \n 3 - automatically calculate suggestions \n 4 - View graph \n 5 - Create new graph \n 6 - Replace graph file \n 7 - Add another assessment file \n 8 - Remove assessment file \n 9 - Replace resource file \n 10 - Get Learning Object Average \n 11 Get Student Average \n 12 - View Structure Graph (switch to structure mode) \n 13 - quit");
                    num = scanner.nextInt();
                }
                scanner.nextLine();

                if (num == 1) {
                    createLearningObjectList(scanner);
                }
                else if (num == 2) {
                    specificLearningObjectSuggestion( scanner);
                }
                else if (num == 3) {
                    graphSuggestions(scanner);
                }
                else if (num == 4) {
                    viewgraph();
                }
                else  if(num ==5){
                    createNewCohortGraph(scanner );
                }
                else if(num ==6){
                    replaceGraphFile(scanner);
                }
                else if(num ==7){
                    additionalLOR(scanner);
                }
                else if (num ==8) {
                    removeLORFile(scanner);

                }else if(num ==9){
                    replaceResourceFile(scanner);

                }else if(num==10){
                    resourceAverage(scanner);

                }else if(num ==11){
                    studentAverage(scanner);

                }else if (num == 12){
                    switchToStructuremode();
                }else{
                    contQuit=0;
                }
            }
            System.out.println("\n");
        }
    }


    public void replaceGraph(Scanner scanner){
        System.out.println("replace graph");

        System.out.println("What file do you want to replace with?");
        String file = scanner.nextLine();

        try {
            List<String> graphFile = new ArrayList<>();
            graphFile.add(file);
            ckc.clearAndCreateStructureData(graphFile);
            System.out.println("Process Completed");

        } catch (Exception e) {
            System.out.println("Cannot find the file");
        }
    }

    public void addLOAndLOR(Scanner scanner){
        System.out.println("add LO and LOR");

        System.out.println("Type Resource File (starting from root)");
        String LOfile = scanner.nextLine();

        System.out.println("Type Assessment File (starting from root)");
        String LORFile = scanner.nextLine();

        try {
            ckc.addLORAndLO(LOfile, LORFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    public void createLearningObjectList(Scanner scanner){
        System.out.println("calculate a list of concept nodes to work on ");
        System.out.println("User ID");
        String userID = scanner.nextLine();

        try {
            List<String> suggestedConceptNodeList = ckc.calcIndividualConceptNodesSuggestions(userID);

            String st = "";
            for(String node: suggestedConceptNodeList){
                st+= node+ "\n";
            }
            System.out.println(st);
        } catch (Exception e) {
            System.out.println(e);
        }
    }


    public void specificLearningObjectSuggestion(Scanner scanner){
        System.out.println("calculate resource suggestions based on a specific concept");
        System.out.println("User ID");
        String userID = scanner.nextLine();

        System.out.println("Concept ID");
        String conceptID = scanner.nextLine();
        SuggestionResource sugRes;
        try {
            sugRes = ckc.calcIndividualSpecificConceptSuggestions(userID, conceptID);

            System.out.println(" 1- Try something new 2- try something again");
            Integer option = scanner.nextInt();
            while (option > 2 || option < 1) {
                System.out.println(" 1- Try something new 2- try something again");
                option = scanner.nextInt();
            }
            if (option == 1) {
                if(sugRes.incompleteList.size()==0){
                    System.out.println("There are no incomplete resources");
                }else {
                    System.out.println(sugRes.toString(0));
                }
            } else {
                if(sugRes.wrongList.size()==0){
                    System.out.println("There are no wrong resources");
                }else{
                    System.out.println(sugRes.toString(1));
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }


    public void graphSuggestions(Scanner scanner){
        System.out.println("automatically calculate suggestions");
        System.out.println("User ID");
        String userID = scanner.nextLine();
        try {
            SuggestionResource sugRes = ckc.calcIndividualGraphSuggestions(userID);
            System.out.println(" 1- Try something new 2- try something again");
            Integer option = scanner.nextInt();
            while (option > 2 || option < 1) {
                System.out.println(" 1- Try something new 2- try something again");
                option = scanner.nextInt();
            }
            if (option == 1) {
                if(sugRes.incompleteList.size()==0){
                    System.out.println("There are no incomplete resources to work on");
                }else {
                    System.out.println(sugRes.toString(0));
                }
            } else {
                if(sugRes.wrongList.size()==0){
                    System.out.println("There are no wrong resources to work on");
                }else{
                    System.out.println(sugRes.toString(1));
                }
            }
        }catch (Exception e){
            System.out.println(e);
        }
    }


    public void viewgraph(){
        System.out.println("view graph");

        try {
            System.out.println(ckc.getCohortGraphsUrl());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void createNewCohortGraph(Scanner scanner){
        System.out.println("create new cohort graphs");

        System.out.println("Type concept graph path: ");
        String structure = scanner.nextLine();

        System.out.println("Type learning object record path: ");
        String resource = scanner.nextLine();

        System.out.println("Type grade book path: ");
        String assessment = scanner.nextLine();

        try {
            List<String> struc = new ArrayList<>();
            List<String> res = new ArrayList<>();
            List<String> assess = new ArrayList<>();
            struc.add(structure);
            res.add(resource);
            assess.add(assessment);

            System.out.println("struct " + struc);
            System.out.println("res " + res);
            System.out.println("acess " + assess);


            ckc.clearAndCreateCohortData(struc, res, assess);
            System.out.println("Process Completed");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Can't find files");
        }
    }


    public void replaceGraphFile(Scanner scanner){
        System.out.println("Replace Graph File");

        System.out.println("Type Graph Path ");
        String graph = scanner.nextLine();
        try {
            ckc.replaceGraph(graph);
            System.out.println("Process Completed");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Can't find files");

        }
    }


    public void additionalLOR(Scanner scanner){
        System.out.println("Add another assesment file to existing graph ");

        System.out.println("Type assessment path (from root)");
        String LOR = scanner.nextLine();

        try {
            ckc.additionalLOR(LOR);
            System.out.println("Process Completed");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Can't find file");
        }
    }

    public void removeLORFile(Scanner scanner){
        System.out.println("Remove a assignment file");

        System.out.println("Type path to file you want to remove: ");
        String assignment = scanner.nextLine();
        try {
            if(ckc.getAssessmentFiles().size()==0){
                System.out.println("You don't have any assessment files. Make sure to add one");
            }else {
                ckc.removeLORFile(assignment);
                System.out.println("Process Completed");
                if(ckc.getAssessmentFiles().size()==0){
                    System.out.println("You now don't have any assessment files. Make sure to add one");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void replaceResourceFile(Scanner scanner){
        System.out.println("Replace resource file");

        System.out.println("Type resource file you want to use: ");
        String resource = scanner.nextLine();

        try {
            ckc.replaceLOFile(resource);
            System.out.println("Process Completed");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void createResourceFile(){
        System.out.println("Creating resources file...");
        try {
            String confirmation = ckc.csvToResource();
            System.out.println(confirmation);
        }catch (Exception e){
            System.out.println("Error occurred - could not create file");
        }
    }


    public void resourceAverage(Scanner scanner){
        System.out.println("Get average learning object Grade");

        System.out.println("What learning object do you want to calculate?");
        String conceptNode = scanner.nextLine();
        try {
            System.out.println("The average is: " + ckc.getLearningObjectAvg(conceptNode));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void studentAverage(Scanner scanner){
        System.out.println("Get user's average grade");
        System.out.println("Which user would you like to calculate an average for?");
        String user = scanner.nextLine();

        try{
            double result = ckc.getStudentAvg(user);
            System.out.println("The average is: " + result);
        }catch(NullPointerException e){
            System.out.println("Error: User not found");
        }
    }


    public void switchToStructuremode() {
        try {
            ckc.switchToStructure();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
