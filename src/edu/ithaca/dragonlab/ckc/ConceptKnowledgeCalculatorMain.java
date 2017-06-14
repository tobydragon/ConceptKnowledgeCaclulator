package edu.ithaca.dragonlab.ckc;

import edu.ithaca.dragonlab.ckc.ui.ConsoleUI;

import java.util.Scanner;

public class ConceptKnowledgeCalculatorMain {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("View: 1- Structure Graph  2- Cohort Graph");
        Integer num = scanner.nextInt();

        while (num < 1 || num > 2) {
            System.out.println("Out of bounds");
            System.out.println("View: 1- Structure Graph  2- Cohort Graph");
            num = scanner.nextInt();
        }

        if (num == 1) {
            new ConsoleUI("test/testresources/basicRealisticExampleConceptGraphOneStudent.json");

        } else {
            new ConsoleUI("test/testresources/basicRealisticExampleConceptGraphOneStudent.json",
                    "test/testresources/basicRealisticExampleLOLRecordOneStudent.json",
                    "test/testresources/basicRealisticExampleGradeBook2.csv");

        }
    }

}
