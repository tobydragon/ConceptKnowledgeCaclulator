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
            ckc = new ConceptKnowledgeCalculator();
        }
        run();
    }

    public ConsoleUI(String structureFilename, String resourceFilename, String assessmentFilename) {
        try {
            ckc = new ConceptKnowledgeCalculator(structureFilename, resourceFilename, assessmentFilename);
        } catch (Exception e) {
            System.out.println("Unable to load default files, please choose files manually. Error follows:");
            ckc = new ConceptKnowledgeCalculator();
        }
        run();
    }


    public void run(){
        Scanner scanner = new Scanner(System.in);

        int contQuit = 1;
        while (contQuit == 1) {
            ConceptKnowledgeCalculator.Mode mode = ckc.getCurrentMode();
            System.out.println("Current Mode: "+ ckc.getCurrentMode());

            if(mode == ConceptKnowledgeCalculator.Mode.NODATA){
                System.out.println("What do you want to do? \n 1 - Switch to Cohort Graph Mode \n 2 - Switch to Structure Graph Mode \n 3 - quit");
                Integer num = scanner.nextInt();

                while (num < 1 || num > 3) {
                    System.out.println("Out of bounds");
                    System.out.println("What do you want to do? \n 1 - Switch to Cohort Graph Mode \n 2 - Switch to Structure Graph Mode \n 3 - quit");

                    num = scanner.nextInt();
                }
                scanner.nextLine();


                if(num==1){
                    createNewCohortGraph(scanner );

                }else if(num ==2){
                    editStructureGraph(scanner);

                }else{
                    contQuit=0;

                }



            } else if (mode == ConceptKnowledgeCalculator.Mode.STRUCTUREGRAPH) {

                System.out.println("What do you want to do? \n 1 - edit graph  \n 2 - View Graph \n 3 - add resource file (switch to Structure Graph with Resource mode) \n 4 - add assessment file (switch to Structure Graph with Assessment mode) \n 5 - quit");
                Integer num = scanner.nextInt();

                while (num < 1 || num > 5) {
                    System.out.println("Out of bounds");
                    System.out.println("What do you want to do? \n 1 - edit graph  \n 2 - View Graph \n 3 - add resource file (switch to Structure Graph with Resource mode) \n 4 - add assessment file (switch to Structure Graph with Assessment mode) \n 5 - quit");
                    num = scanner.nextInt();
                }
                scanner.nextLine();

                if (num==1){
                    editStructureGraph(scanner);

                }else if(num ==2){
                    viewgraph();

                }else if(num ==3){
                    replaceResourceFile(scanner);

                }else if(num==4) {
                    addAssignment(scanner);

                }else{
                    contQuit = 0;
                }

            } else if(mode == ConceptKnowledgeCalculator.Mode.STRUCTUREGRAPHWITHRESOURCE) {

                System.out.println("What do you want to do? \n 1 - edit graph \n 2 - replace resource file \n 3 - add assessment file (switch to cohort mode) \n 4 - view graph \n 5 - View Structure Graph (switch to structure mode) \n 6 - quit ");
                Integer num = scanner.nextInt();

                while (num < 1 || num > 6) {
                    System.out.println("Out of bounds");
                    System.out.println("What do you want to do? \n 1 - edit graph \n 2 - replace resource file \n 3 - add assessment file (switch to cohort mode) \n 4 - view graph \n 5 - View Structure Graph (switch to structure mode) \n 6 - quit ");
                    num = scanner.nextInt();
                }
                scanner.nextLine();

                if (num == 1) {
                    editStructureGraph(scanner);

                } else if (num == 2) {
                    replaceResourceFile(scanner);

                } else if (num == 3) {
                    addAssignment(scanner);

                } else if (num == 4) {
                    viewgraph();

                }else if(num ==5){
                    switchToStructuremode();
                }else{
                    contQuit=0;
                }

            } else if(mode == ConceptKnowledgeCalculator.Mode.STRUCTUREGRAPHWITHASSESSMENT) {

                System.out.println("What do you want to do? \n 1 - edit graph \n 2 - add assessment file  \n 3 - remove assessment file \n 4 - create resource file  \n 5 - add resource file (switch to cohort mode) \n 6 - view graph \n 7 - View Structure Graph (switch to structure mode) \n 8 - quit");
                Integer num = scanner.nextInt();

                while (num < 1 || num > 8) {
                    System.out.println("Out of bounds");
                    System.out.println("What do you want to do? \n 1 - edit graph \n 2 - add assessment file  \n 3 - remove assessment file \n 4 - create resource file  \n 5 - add resource file (switch to cohort mode) \n 6 - view graph \n 7 - View Structure Graph (switch to structure mode) \n 8 - quit");
                    num = scanner.nextInt();
                }
                scanner.nextLine();

                if (num == 1) {
                    editStructureGraph(scanner);

                } else if (num == 2) {
                    addAssignment(scanner);

                } else if (num == 3) {
                    removeAssessment(scanner);

                } else if (num == 4) {
                    createResourceFile();

                } else if (num == 5) {
                    replaceResourceFile(scanner);

                } else if (num == 6) {
                    viewgraph();
                } else  if(num ==7) {
                    switchToStructuremode();
                }else{
                    contQuit=0;
                }

            //COHORTGRAPH MODE
            }else{

                System.out.println("What do you want to do? \n 1 - calculate a list of concept nodes to work on \n 2 - calculate resources suggestions based on a specific concept \n 3 - automatically calculate suggestions \n 4 - View graph \n 5 - Create new graph \n 6 - Replace graph file \n 7 - Add another assessment file \n 8 - Remove assessment file \n 9 - Replace resource file \n 10 - View list of users \n 11 - Get Learning Object Average \n 12 - Get Student Average \n 13 - Link Learning Objects to similar factors \n 14 -  Graph strength of concept structure \n 15 - Create Groups of Random Students \n 16 - Create Groups of Students Based on Their Suggestions \n 17 - Create Group Of Students Based on Their Graph Sums \n 18 - Concept Difference Sum Group Suggestions \n 19 - View Structure Graph (switch to structure mode) \n 20 - Quit");
                Integer num = scanner.nextInt();

                while (num < 1 || num > 20) {
                    System.out.println("Out of bounds");
                    System.out.println("What do you want to do? \n 1 - calculate a list of concept nodes to work on \n 2 - calculate resources suggestions based on a specific concept \n 3 - automatically calculate suggestions \n 4 - View graph \n 5 - Create new graph \n 6 - Replace graph file \n 7 - Add another assessment file \n 8 - Remove assessment file \n 9 - Replace resource file \n 10 - View list of users \n 11 - Get Learning Object Average \n 12 - Get Student Average \n 13 - Link Learning Objects to similar factors \n 14 -  Graph strength of concept structure \n 15 - Create Groups of Random Students \n 16 - Create Groups of Students Based on Their Suggestions \n 17 - Create Group Of Students Based on Their Graph Sums \n 18 - Concept Difference Sum Group Suggestions \n 19 - View Structure Graph (switch to structure mode) \n 20 - Quit");
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
                    replaceCohortGraphFile(scanner);
                }
                else if(num ==7){
                    addAssignment(scanner);
                }
                else if (num ==8) {
                    removeAssessment(scanner);

                }else if(num ==9) {
                    replaceResourceFile(scanner);

                }else if(num ==10){
                    getUserList();

                }else if(num==11){
                    resourceAverage(scanner);

                }else if(num ==12) {
                    studentAverage(scanner);
                }else if(num == 13) {
                    getFactorMatrix();
                }else if(num == 14) {
                    createConfirmatoryGraph();
                }else if (num == 15) {
                    createRandomGroupSuggestions(scanner);
                }else if(num ==16){
                    createResourceGroupSuggestions(scanner);
                }else if(num ==17){
                    createGraphSumGroupSuggestions(scanner);
                } else if(num ==18) {
                    createConceptDiffGroupSuggestions(scanner);
                }else if( num ==19) { 
                    switchToStructuremode();
                }else{
                    contQuit=0;
                }
            }
            System.out.println("\n");
        }
    }


    public void createConceptDiffGroupSuggestions(Scanner scanner){
        System.out.println("Get group suggestions based on the difference in concept node's");

        System.out.println("Do you want groups of 2 or 3? ");
        Integer choice = scanner.nextInt();

        while(choice > 3 || choice <2){
            System.out.println("Do you want groups of 2 or 3? ");
            choice = scanner.nextInt();
        }

        scanner.nextLine();

        System.out.println("If you want to create groups on the entire graph type 'all' or type in the concept ID");
        String concept = scanner.nextLine();


        try {
            List<List<String>> groupings = ckc.conceptDiffGroupSuggestions(choice, concept);

            for(List<String> group: groupings) {
                System.out.println(group);
            }


        } catch (Exception e) {
            System.out.println("Wrong mode");
        }
    }


    public void createGraphSumGroupSuggestions(Scanner scanner){
        System.out.println("Get group suggestions based on the sum of students' graphs");

        System.out.println("Do you want groups of 2 or 3? ");
        Integer choice = scanner.nextInt();

        while(choice > 3 || choice <2){
            System.out.println("Do you want groups of 2 or 3? ");
            choice = scanner.nextInt();
        }

        scanner.nextLine();

        System.out.println("If you want to create groups on the entire graph type 'all' or type in the concept ID");
        String concept = scanner.nextLine();


        try {
            List<List<String>> groupings = ckc.graphSumGroupSuggestions(choice, concept);

            for(List<String> group: groupings){
                System.out.println(group);
            }
        } catch (Exception e) {
            System.out.println("Wrong mode");
        }
    }

    public void createResourceGroupSuggestions(Scanner scanner){
        System.out.println("Get group suggestions based on students resource suggestions");

        System.out.println("Do you want groups of 2 or 3? ");
        Integer choice = scanner.nextInt();

        while(choice > 3 || choice <2){
            System.out.println("Do you want groups of 2 or 3? ");
            choice = scanner.nextInt();
        }

        try {
            List<List<String>> groupings = ckc.resourceGroupSuggestions(choice);

            for(List<String> group: groupings){
                System.out.println(group);
            }
        } catch (Exception e) {
            System.out.println("Wrong mode");
        }
    }



    public void createRandomGroupSuggestions(Scanner scanner){
        System.out.println("Get random group suggestions");

        System.out.println("How many people do you want per group?");
        Integer choice = scanner.nextInt();

        try {
            List<List<String>> groupings = ckc.randomGroupSuggestions(choice);


            for(List<String> group: groupings){
                System.out.println(group);
            }

        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public void editStructureGraph(Scanner scanner){
        System.out.println("Edit structure graph");
        System.out.println("Current Structure File: " + ckc.currentStructure().toString().replace("[", "").replace("]", ""));


        System.out.println("What file do you want to replace with?");
        String file = scanner.nextLine();

        try {
            ckc.updateStructureFile(file);
            System.out.println("Process Completed");

        } catch (Exception e) {
            System.out.println("Cannot find the file");
        }
    }

    public void addResourceAndAssignment(Scanner scanner){
        System.out.println("add Resource and Assignment files");

        System.out.println("Type Resource File (starting from root)");
        String resource = scanner.nextLine();

        System.out.println("Type Assessment File (starting from root)");
        String assignment = scanner.nextLine();

        try {
            ckc.addResourceAndAssessment(resource, assignment);
        } catch (Exception e) {
            System.out.println(e);
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
            System.out.println(e);;
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


        List<String>  s = new ArrayList<>();
        s.add(structure);

        List<String>  r = new ArrayList<>();
        r.add(resource);

        List<String>  a = new ArrayList<>();
        a.add(assessment);

        try {
            ckc.clearAndCreateCohortData(s,r,a);
            System.out.println("Process Completed");
        } catch (Exception e) {
            System.out.println("Can't find files");
        }

    }


    public void replaceCohortGraphFile(Scanner scanner){
        System.out.println("Replace Graph File");
        System.out.println("Current Graph File: " + ckc.currentStructure().toString().replace("[", "").replace("]", ""));

        System.out.println("Type Graph Path ");
        String graph = scanner.nextLine();
        try {
            ckc.replaceCohortGraph(graph);
            System.out.println("Process Completed");
        } catch (Exception e) {
            System.out.println("Can't find file");

        }
    }


    public void addAssignment(Scanner scanner){
        if(ckc.getCurrentMode()== ConceptKnowledgeCalculator.Mode.COHORTGRAPH|| ckc.getCurrentMode()== ConceptKnowledgeCalculator.Mode.STRUCTUREGRAPHWITHASSESSMENT) {
            System.out.println("Add another assesment file to existing graph ");
            System.out.println("Current Assignment Files: " + ckc.currentAssessment().toString().replace("[", "").replace("]", ""));
        }else if(ckc.getCurrentMode()== ConceptKnowledgeCalculator.Mode.STRUCTUREGRAPH|| ckc.getCurrentMode()== ConceptKnowledgeCalculator.Mode.STRUCTUREGRAPHWITHRESOURCE){
            System.out.println("Add a assessment file");
        }

        System.out.println("Type assessment path (from root)");
        String assessment = scanner.nextLine();

        try {
            ckc.addAssessement(assessment);
            System.out.println("Process Completed");
        } catch (Exception e) {
            System.out.println("Can't find file");
        }
    }

    public void removeAssessment(Scanner scanner){
        System.out.println("Remove a assignment file");
        if(ckc.currentAssessment().size()<1){
            System.out.println("You Don't have any files");
        }else{
            System.out.println("Current Assignment Files: " + ckc.currentAssessment().toString().replace("[", "").replace("]", ""));

            System.out.println("Type path to file you want to remove: ");
            String assignment = scanner.nextLine();
            try {
                if (ckc.currentAssessment().size()<1) {
                    System.out.println("You don't have any assessment files. Make sure to add one");
                } else {
                    ckc.removeAssessmentFile(assignment);
                    System.out.println("Process Completed");
                    if (ckc.currentAssessment().size()<1) {
                        System.out.println("You now don't have any assessment files. Make sure to add one");
                    }
                }
            } catch (Exception e) {
                System.out.println("File can't be found");
            }
        }
    }


    public void replaceResourceFile(Scanner scanner){
        if(ckc.getCurrentMode()== ConceptKnowledgeCalculator.Mode.COHORTGRAPH || ckc.getCurrentMode()== ConceptKnowledgeCalculator.Mode.STRUCTUREGRAPHWITHRESOURCE) {
            System.out.println("Replace resource file");
            System.out.println("Current Resource File: " +ckc.currentResource().toString().replace("[","").replace("]", ""));

        }else if (ckc.getCurrentMode()== ConceptKnowledgeCalculator.Mode.STRUCTUREGRAPH || ckc.getCurrentMode()== ConceptKnowledgeCalculator.Mode.STRUCTUREGRAPHWITHASSESSMENT){
            System.out.println("Add resource file");
        }

        System.out.println("Type resource file you want to use: ");
        String resource = scanner.nextLine();

        try {
            ckc.replaceResourceFile(resource);
            System.out.println("Process Completed");

        } catch (Exception e) {
            System.out.println("File can't be found");
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
            System.out.println("Can't find " + conceptNode);
        }
    }

    public void getUserList(){
        try {
            List<String> userlist = ckc.getUserIdList();
            System.out.println("User List: " + userlist);
        }catch (Exception e){
            System.out.println("No list");
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
            System.out.println("Cannot find " + user);
        }
    }

    public void getFactorMatrix(){
        System.out.println("Collecting data and linking strengths of learning objects to different factors...\n");
        ckc.getFactorMatrix();
    }

    public void createConfirmatoryGraph(){
        System.out.println("Creating graphs showing strengths between questions and concepts user has specified\n");
        ckc.createConfirmatoryGraph();
    }


    public void switchToStructuremode() {
        try {
            ckc.switchToStructure();
        } catch (Exception e) {
            System.out.println(e);;
        }

    }


}
