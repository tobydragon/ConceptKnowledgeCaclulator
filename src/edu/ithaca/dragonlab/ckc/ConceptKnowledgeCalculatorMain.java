package edu.ithaca.dragonlab.ckc;

import edu.ithaca.dragonlab.ckc.ui.ConsoleUI;

import java.util.*;

public class ConceptKnowledgeCalculatorMain {

    public static void main(String[] args) {
        int num =2;


//        Scanner scanner = new Scanner(System.in);
//        int num = -1;
//
//        System.out.println("View: 1- Structure Graph  2- Cohort Graph");
//        try{
//            num = scanner.nextInt();
//        }catch (InputMismatchException a){
//            System.out.println("Wrong input");
//        }
//
//        while (num < 1 || num > 2) {
//            System.out.println(num);
//            System.out.println("Out of bounds");
//            System.out.println("View: 1- Structure Graph  2- Cohort Graph");
//            try{
//                num = scanner.nextInt();
//            }catch (InputMismatchException a){
//                System.out.println("Wrong input");
//                scanner.next();
//
//            }
//
//        }


        if (num == 1) {
        new ConsoleUI("test/testresources/basicRealisticExampleConceptGraphOneStudent.json");
    }

//        if (num ==2){
//            new ConsoleUI("test/testresources/basicRealisticExampleConceptGraphOneStudent.json",
//                    "test/testresources/basicRealisticExampleLOLRecordOneStudent.json",
//                    "test/testresources/basicRealisticExampleGradeBook2.csv");
//
//        }
        if (num ==2){
            new ConsoleUI("resources/comp220/comp220Graph.json",
                    "resources/comp220/comp220Resources.json",
                    "localresources/comp220ExampleDataPortion.csv");

        }
}

}
