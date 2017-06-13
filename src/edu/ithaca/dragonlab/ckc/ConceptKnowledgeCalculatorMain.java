package edu.ithaca.dragonlab.ckc;

import edu.ithaca.dragonlab.ckc.ui.ConsoleUI;

public class ConceptKnowledgeCalculatorMain {

    public static void main(String[] args) {
        new ConsoleUI("test/testresources/basicRealisticExampleConceptGraphOneStudent.json",
                "test/testresources/basicRealisticExampleLOLRecordOneStudent.json" ,
                "test/testresources/basicRealisticExampleGradeBook2.csv");

    }

}
