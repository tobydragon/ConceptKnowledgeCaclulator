package edu.ithaca.dragon.tecmap.legacy;

import edu.ithaca.dragon.tecmap.suggester.GroupSuggester.*;
import edu.ithaca.dragon.tecmap.suggester.OrganizedLearningResourceSuggestions;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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

    public ConsoleUI(List<String> structureFilenames, List<String> resourceFilenames, List<String> assessmentFilenames) {
        try {
            ckc = new ConceptKnowledgeCalculator(structureFilenames, resourceFilenames, assessmentFilenames);
        } catch (Exception e) {
            System.out.println("Unable to load default files, please choose files manually. Error follows:");
            e.printStackTrace();
            ckc = new ConceptKnowledgeCalculator();
        }
        run();
    }


    public void run(){
        Scanner scanner = new Scanner(System.in);

        boolean studentView = true;

        int contQuit = 1;
        while (contQuit == 1) {
            ConceptKnowledgeCalculator.Mode mode = ckc.getCurrentMode();

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
            }else {


                if (studentView) {
                    System.out.println("What do you want to do? \n 1 - calculate a list of Concept nodes to work on \n 2 - calculate resources suggestions based on a specific Concept \n 3 - automatically calculate suggestions \n 4 - View graph \n 5 - Change Suggestion View \n 6 - More Options \n 7 - Quit");


                    Integer num = scanner.nextInt();

                    while (num < 1 || num > 7) {
                        System.out.println("Out of bounds");
                        System.out.println("What do you want to do? \n 1 - calculate a list of Concept nodes to work on \n 2 - calculate resources suggestions based on a specific Concept \n 3 - automatically calculate suggestions \n 4 - View graph \n 5 - Change Suggestion View \n 6 - More Options \n 7 - Quit");
                        num = scanner.nextInt();
                    }
                    scanner.nextLine();

                    if (num == 1) {
                        createLearningObjectList(scanner);
                    } else if (num == 2) {
                        specificLearningObjectSuggestion(scanner);
                    } else if (num == 3) {
                        graphSuggestions(scanner);
                    } else if (num == 4) {
                        viewgraph();
                    } else if (num ==5) {

                        changeSuggestionView();
                    }else if(num ==6 ){
                        studentView = false;
                    } else{
                      contQuit = 0;
                    }



                } else {

                    System.out.println("What do you want to do? \n 1 - calculate a list of Concept nodes to work on \n 2 - calculate resources suggestions based on a specific Concept \n 3 - automatically calculate suggestions \n 4 - View graph \n 5 - Create new graph \n 6 - Replace graph file \n 7 - Add another assessment file \n 8 - Remove assessment file \n 9 - Replace resource file \n 10 - View list of users \n 11 - Get Learning Object Average \n 12 - Get Student Average \n 13 - Link Learning Objects to similar factors \n 14 -  Graph strength of Concept structure \n 15 - View Structure Graph (switch to structure mode) \n 16 - Create model file \n 17 - Calc Groups \n 18 - Change Suggestion View \n 19 - Quit");


                    Integer num = scanner.nextInt();

                    while (num < 1 || num > 19) {
                        System.out.println("Out of bounds");

                        System.out.println("What do you want to do? \n 1 - calculate a list of Concept nodes to work on \n 2 - calculate resources suggestions based on a specific Concept \n 3 - automatically calculate suggestions \n 4 - View graph \n 5 - Create new graph \n 6 - Replace graph file \n 7 - Add another assessment file \n 8 - Remove assessment file \n 9 - Replace resource file \n 10 - View list of users \n 11 - Get Learning Object Average \n 12 - Get Student Average \n 13 - Link Learning Objects to similar factors \n 14 -  Graph strength of Concept structure \n 15 - View Structure Graph (switch to structure mode) \n 16 - Create model file \n 17 - Calc Groups \n 18 - Change Suggestion View \n 19 - Quit");


                        num = scanner.nextInt();
                    }
                    scanner.nextLine();

                    if (num == 1) {
                        createLearningObjectList(scanner);
                    } else if (num == 2) {
                        specificLearningObjectSuggestion(scanner);
                    } else if (num == 3) {
                        graphSuggestions(scanner);
                    } else if (num == 4) {
                        viewgraph();
                    } else if (num == 5) {
                        createNewCohortGraph(scanner);
                    } else if (num == 6) {
                        replaceCohortGraphFile(scanner);
                    } else if (num == 7) {
                        addAssignment(scanner);
                    } else if (num == 8) {
                        removeAssessment(scanner);

                    } else if (num == 9) {
                        replaceResourceFile(scanner);

                    } else if (num == 10) {
                        getUserList();

//                    } else if (num == 11) {
//                        resourceAverage(scanner);

//                    } else if (num == 12) {
//                        studentAverage(scanner);
//                    } else if (num == 13) {
//                        getFactorMatrix();
//                    } else if (num == 14) {
//                        createConfirmatoryGraph();
//                    } else if (num == 15) {
//                        switchToStructuremode();
//                    } else if (num == 16) {
//
//                        createModelFile();

                    } else if (num == 17) {
                        calculateSmallGroups(scanner);
                    } else if (num == 18) {
                        changeSuggestionView();
                    } else {
                        contQuit = 0;
                    }
                }
            }
            System.out.println("\n");
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
        System.out.println("calculate a list of Concept nodes to work on ");
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
        System.out.println("calculate resource suggestions based on a specific Concept");
        System.out.println("User ID");
        String userID = scanner.nextLine();

        System.out.println("Concept ID");
        String conceptID = scanner.nextLine();
        OrganizedLearningResourceSuggestions sugRes;
        try {
            sugRes = ckc.calcIndividualSpecificConceptSuggestions(userID, conceptID);

            if(ckc.getCurrentSuggestMode() == ConceptKnowledgeCalculator.SuggestMode.LISTEVERYTHING){

                if(sugRes.incompleteList.size()>0){
                    System.out.println("Try something new \n_____________________________________");
                    System.out.println(sugRes.toString(0));
                }
                if(sugRes.wrongList.size()>0){
                    System.out.println("\nTry something again \n_____________________________________");
                    System.out.println(sugRes.toString(1));
                }


            }else{
                System.out.println(" 1- Try something new 2- try something again");
                Integer option = scanner.nextInt();
                while (option > 2 || option < 1) {
                    System.out.println(" 1- Try something new 2- try something again");
                    option = scanner.nextInt();
                }
                if (option == 1) {
                    if (sugRes.incompleteList.size() == 0) {
                        System.out.println("There are no incomplete resources");
                    } else {
                        System.out.println(sugRes.toString(0));
                    }
                } else {
                    if (sugRes.wrongList.size() == 0) {
                        System.out.println("There are no wrong resources");
                    } else {
                        System.out.println(sugRes.toString(1));
                    }
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
            OrganizedLearningResourceSuggestions sugRes = ckc.calcIndividualGraphSuggestions(userID);

            if(ckc.getCurrentSuggestMode() == ConceptKnowledgeCalculator.SuggestMode.LISTEVERYTHING) {
                if(sugRes.incompleteList.size()>0){
                    System.out.println("Try something new \n_____________________________________");
                    System.out.println(sugRes.toString(0));
                }
                if(sugRes.wrongList.size()>0){
                    System.out.println("\n Try something again \n_____________________________________");
                    System.out.println(sugRes.toString(1));
                }



            }else{


                System.out.println(" 1- Try a new resource 2- try a resource again");
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

        System.out.println("Type Concept graph path: ");
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
            System.err.println("Can't load file, error follows:");
            e.printStackTrace();

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
            System.err.println("Can't load file, error follows:");
            e.printStackTrace();
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
                System.err.println("Can't load file, error follows:");
                e.printStackTrace();
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
            System.out.println("An error occured while rading file, details follow:");
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


//    public void resourceAverage(Scanner scanner){
//        System.out.println("Get average learning object Grade");
//
//        System.out.println("What learning object do you want to calculate?");
//        String conceptNode = scanner.nextLine();
//        try {
//            System.out.println("The average is: " + ckc.getLearningObjectAvg(conceptNode));
//        } catch (Exception e) {
//            System.out.println("Can't find " + conceptNode);
//        }
//    }

    public void getUserList(){
        try {
            List<String> userlist = ckc.getUserIdList();
            System.out.println("User List: " + userlist);
        }catch (Exception e){
            System.out.println("No list");
        }
    }

//    public void studentAverage(Scanner scanner){
//        System.out.println("Get user's average grade");
//        System.out.println("Which user would you like to calculate an average for?");
//        String user = scanner.nextLine();
//
//        try{
//            double result = ckc.getStudentAvg(user);
//            System.out.println("The average is: " + result);
//        }catch(NullPointerException e){
//            System.out.println("Cannot find " + user);
//        }
//    }

//    public void getFactorMatrix(){
//        System.out.println("Collecting data and linking strengths of learning objects to different factors...\n");
//        ckc.getFactorMatrix();
//    }
//
//    public void createConfirmatoryGraph(){
//        System.out.println("Creating graphs showing strengths between questions and concepts user has specified\n");
//        ckc.createConfirmatoryGraph();
//    }
//
//    public void createModelFile(){
//        System.out.println("Creating model file for graph making in confirmatoryScript.R");
//        ckc.createModelFile();
//    }


    public void switchToStructuremode() {
        try {
            ckc.switchToStructure();
        } catch (Exception e) {
            System.out.println(e);;
        }

    }

    public void calculateSmallGroups(Scanner scanner){
        System.out.println("Calculate Small Groups");
        System.out.println("What size group: ");
        int size = scanner.nextInt();

        List<Suggester> listSuggester = new ArrayList<>();

        //set up for buckets
        List<List<Integer>> ranges = new ArrayList<>();
        List<Integer> temp = new ArrayList<>();
        temp.add(0);
        temp.add(50);
        List<Integer> temp2 = new ArrayList<>();
        temp2.add(50);
        temp2.add(80);
        List<Integer> temp3 = new ArrayList<>();
        temp3.add(80);
        temp3.add(100);
        ranges.add(temp);
        ranges.add(temp2);
        ranges.add(temp3);
        try {
            listSuggester.add(new BucketSuggester(ranges));
            listSuggester.add(new ConceptSuggester());
            listSuggester.add(new BySizeSuggester(size, true));
//            listSuggester.add(new ComplementaryKnowledgeSuggester());
            List<Group> groupings = ckc.calcSmallGroups(listSuggester, size);

            for(Group gr: groupings){
                System.out.println(gr.toString(0));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void changeSuggestionView(){

        ckc.toggleCurrentSuggestMode();
    }

}
