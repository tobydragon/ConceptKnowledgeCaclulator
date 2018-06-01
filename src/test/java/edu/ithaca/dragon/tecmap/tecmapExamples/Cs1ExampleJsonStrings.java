package edu.ithaca.dragon.tecmap.tecmapExamples;

public class Cs1ExampleJsonStrings {

    //Diagram: https://drive.google.com/file/d/1tAXGvSwgYHSx2LS4ZZ57muEyUeVWJa0c/view?usp=sharing

    public static final String structureAsTreeString = "{\n" +
            "  \"name\" : \"Cs1ExampleGraph\",\n" +
            "  \"concepts\" : [ {\n" +
            "    \"id\" : \"Intro CS-1\",\n" +
            "    \"label\" : \"Intro CS\",\n" +
            "    \"knowledgeEstimate\" : 0.0,\n" +
            "    \"knowledgePrediction\" : 0.0,\n" +
            "    \"knowledgeDistFromAvg\" : 0.0,\n" +
            "    \"dataImportance\" : 0.0,\n" +
            "    \"resourceSummaries\" : [ ]\n" +
            "  }, {\n" +
            "    \"id\" : \"Loops-1\",\n" +
            "    \"label\" : \"Loops\",\n" +
            "    \"knowledgeEstimate\" : 0.0,\n" +
            "    \"knowledgePrediction\" : 0.0,\n" +
            "    \"knowledgeDistFromAvg\" : 0.0,\n" +
            "    \"dataImportance\" : 0.0,\n" +
            "    \"resourceSummaries\" : [ ]\n" +
            "  }, {\n" +
            "    \"id\" : \"While Loops-1\",\n" +
            "    \"label\" : \"While Loops\",\n" +
            "    \"knowledgeEstimate\" : 0.0,\n" +
            "    \"knowledgePrediction\" : 0.0,\n" +
            "    \"knowledgeDistFromAvg\" : 0.0,\n" +
            "    \"dataImportance\" : 0.0,\n" +
            "    \"resourceSummaries\" : [ ]\n" +
            "  }, {\n" +
            "    \"id\" : \"Boolean Expressions-1\",\n" +
            "    \"label\" : \"Boolean Expressions\",\n" +
            "    \"knowledgeEstimate\" : 0.0,\n" +
            "    \"knowledgePrediction\" : 0.0,\n" +
            "    \"knowledgeDistFromAvg\" : 0.0,\n" +
            "    \"dataImportance\" : 0.0,\n" +
            "    \"resourceSummaries\" : [ ]\n" +
            "  }, {\n" +
            "    \"id\" : \"For Loops-1\",\n" +
            "    \"label\" : \"For Loops\",\n" +
            "    \"knowledgeEstimate\" : 0.0,\n" +
            "    \"knowledgePrediction\" : 0.0,\n" +
            "    \"knowledgeDistFromAvg\" : 0.0,\n" +
            "    \"dataImportance\" : 0.0,\n" +
            "    \"resourceSummaries\" : [ ]\n" +
            "  }, {\n" +
            "    \"id\" : \"If Statements-1\",\n" +
            "    \"label\" : \"If Statements\",\n" +
            "    \"knowledgeEstimate\" : 0.0,\n" +
            "    \"knowledgePrediction\" : 0.0,\n" +
            "    \"knowledgeDistFromAvg\" : 0.0,\n" +
            "    \"dataImportance\" : 0.0,\n" +
            "    \"resourceSummaries\" : [ ]\n" +
            "  }, {\n" +
            "    \"id\" : \"Boolean Expressions-2\",\n" +
            "    \"label\" : \"Boolean Expressions\",\n" +
            "    \"knowledgeEstimate\" : 0.0,\n" +
            "    \"knowledgePrediction\" : 0.0,\n" +
            "    \"knowledgeDistFromAvg\" : 0.0,\n" +
            "    \"dataImportance\" : 0.0,\n" +
            "    \"resourceSummaries\" : [ ]\n" +
            "  } ],\n" +
            "  \"links\" : [ {\n" +
            "    \"parent\" : \"While Loops-1\",\n" +
            "    \"child\" : \"Boolean Expressions-1\"\n" +
            "  }, {\n" +
            "    \"parent\" : \"Loops-1\",\n" +
            "    \"child\" : \"While Loops-1\"\n" +
            "  }, {\n" +
            "    \"parent\" : \"Loops-1\",\n" +
            "    \"child\" : \"For Loops-1\"\n" +
            "  }, {\n" +
            "    \"parent\" : \"Intro CS-1\",\n" +
            "    \"child\" : \"Loops-1\"\n" +
            "  }, {\n" +
            "    \"parent\" : \"If Statements-1\",\n" +
            "    \"child\" : \"Boolean Expressions-2\"\n" +
            "  }, {\n" +
            "    \"parent\" : \"Intro CS-1\",\n" +
            "    \"child\" : \"If Statements-1\"\n" +
            "  } ]\n" +
            "}"
            ;

    public static final String assessment1Str = "[ {\n" +
            "  \"learningResourceId\" : \"Q1\",\n" +
            "  \"resourceTypes\" : [ \"ASSESSMENT\" ],\n" +
            "  \"conceptIds\" : [ ],\n" +
            "  \"dataImportance\" : 0.0,\n" +
            "  \"maxPossibleKnowledgeEstimate\" : 10.0\n" +
            "}, {\n" +
            "  \"learningResourceId\" : \"Q2\",\n" +
            "  \"resourceTypes\" : [ \"ASSESSMENT\" ],\n" +
            "  \"conceptIds\" : [ ],\n" +
            "  \"dataImportance\" : 0.0,\n" +
            "  \"maxPossibleKnowledgeEstimate\" : 5.0\n" +
            "}, {\n" +
            "  \"learningResourceId\" : \"Q3\",\n" +
            "  \"resourceTypes\" : [ \"ASSESSMENT\" ],\n" +
            "  \"conceptIds\" : [ ],\n" +
            "  \"dataImportance\" : 0.0,\n" +
            "  \"maxPossibleKnowledgeEstimate\" : 10.0\n" +
            "}, {\n" +
            "  \"learningResourceId\" : \"Q4\",\n" +
            "  \"resourceTypes\" : [ \"ASSESSMENT\" ],\n" +
            "  \"conceptIds\" : [ ],\n" +
            "  \"dataImportance\" : 0.0,\n" +
            "  \"maxPossibleKnowledgeEstimate\" : 5.0\n" +
            "}, {\n" +
            "  \"learningResourceId\" : \"Q5\",\n" +
            "  \"resourceTypes\" : [ \"ASSESSMENT\" ],\n" +
            "  \"conceptIds\" : [ ],\n" +
            "  \"dataImportance\" : 0.0,\n" +
            "  \"maxPossibleKnowledgeEstimate\" : 100.0\n" +
            "} ]"
            ;

    public static final String assessment1And2Str = "[ {\n" +
            "  \"learningResourceId\" : \"Q1\",\n" +
            "  \"resourceTypes\" : [ \"ASSESSMENT\" ],\n" +
            "  \"conceptIds\" : [ ],\n" +
            "  \"dataImportance\" : 0.0,\n" +
            "  \"maxPossibleKnowledgeEstimate\" : 10.0\n" +
            "}, {\n" +
            "  \"learningResourceId\" : \"Q2\",\n" +
            "  \"resourceTypes\" : [ \"ASSESSMENT\" ],\n" +
            "  \"conceptIds\" : [ ],\n" +
            "  \"dataImportance\" : 0.0,\n" +
            "  \"maxPossibleKnowledgeEstimate\" : 5.0\n" +
            "}, {\n" +
            "  \"learningResourceId\" : \"Q3\",\n" +
            "  \"resourceTypes\" : [ \"ASSESSMENT\" ],\n" +
            "  \"conceptIds\" : [ ],\n" +
            "  \"dataImportance\" : 0.0,\n" +
            "  \"maxPossibleKnowledgeEstimate\" : 10.0\n" +
            "}, {\n" +
            "  \"learningResourceId\" : \"Q4\",\n" +
            "  \"resourceTypes\" : [ \"ASSESSMENT\" ],\n" +
            "  \"conceptIds\" : [ ],\n" +
            "  \"dataImportance\" : 0.0,\n" +
            "  \"maxPossibleKnowledgeEstimate\" : 5.0\n" +
            "}, {\n" +
            "  \"learningResourceId\" : \"Q5\",\n" +
            "  \"resourceTypes\" : [ \"ASSESSMENT\" ],\n" +
            "  \"conceptIds\" : [ ],\n" +
            "  \"dataImportance\" : 0.0,\n" +
            "  \"maxPossibleKnowledgeEstimate\" : 100.0\n" +
            "}, {\n" +
            "  \"learningResourceId\" : \"HW1\",\n" +
            "  \"resourceTypes\" : [ \"ASSESSMENT\" ],\n" +
            "  \"conceptIds\" : [ ],\n" +
            "  \"dataImportance\" : 0.0,\n" +
            "  \"maxPossibleKnowledgeEstimate\" : 1.0\n" +
            "}, {\n" +
            "  \"learningResourceId\" : \"HW2\",\n" +
            "  \"resourceTypes\" : [ \"ASSESSMENT\" ],\n" +
            "  \"conceptIds\" : [ ],\n" +
            "  \"dataImportance\" : 0.0,\n" +
            "  \"maxPossibleKnowledgeEstimate\" : 5.0\n" +
            "}, {\n" +
            "  \"learningResourceId\" : \"HW3\",\n" +
            "  \"resourceTypes\" : [ \"ASSESSMENT\" ],\n" +
            "  \"conceptIds\" : [ ],\n" +
            "  \"dataImportance\" : 0.0,\n" +
            "  \"maxPossibleKnowledgeEstimate\" : 2.0\n" +
            "}, {\n" +
            "  \"learningResourceId\" : \"HW4\",\n" +
            "  \"resourceTypes\" : [ \"ASSESSMENT\" ],\n" +
            "  \"conceptIds\" : [ ],\n" +
            "  \"dataImportance\" : 0.0,\n" +
            "  \"maxPossibleKnowledgeEstimate\" : 1.0\n" +
            "}, {\n" +
            "  \"learningResourceId\" : \"HW5\",\n" +
            "  \"resourceTypes\" : [ \"ASSESSMENT\" ],\n" +
            "  \"conceptIds\" : [ ],\n" +
            "  \"dataImportance\" : 0.0,\n" +
            "  \"maxPossibleKnowledgeEstimate\" : 1.0\n" +
            "} ]"
            ;
    public static String allConceptsString = "[\"While Loops\", \"For Loops\", \"Boolean Expressions\", \"Intro CS\", \"Loops\", \"If Statements\"]";
    public static String allConceptsStringAsJson = "[\"\\\"While Loops\\\"\",\"\\\"For Loops\\\"\",\"\\\"Boolean Expressions\\\"\",\"\\\"Intro CS\\\"\",\"\\\"Loops\\\"\",\"\\\"If Statements\\\"\"]";

    public static String bartDataTree = "{\n" +
            "  \"name\" : \"s02\",\n" +
            "  \"concepts\" : [ {\n" +
            "    \"id\" : \"Intro CS-1\",\n" +
            "    \"label\" : \"Intro CS\",\n" +
            "    \"knowledgeEstimate\" : 1.0,\n" +
            "    \"knowledgePrediction\" : 0.0,\n" +
            "    \"knowledgeDistFromAvg\" : 0.30694444444444446,\n" +
            "    \"dataImportance\" : 12.0,\n" +
            "    \"resourceSummaries\" : [ \"Q5   Est:1.000  Imp:1.000  ResponseCount:1\" ]\n" +
            "  }, {\n" +
            "    \"id\" : \"Loops-1\",\n" +
            "    \"label\" : \"Loops\",\n" +
            "    \"knowledgeEstimate\" : 1.0,\n" +
            "    \"knowledgePrediction\" : 0.0,\n" +
            "    \"knowledgeDistFromAvg\" : 0.3142857142857144,\n" +
            "    \"dataImportance\" : 7.0,\n" +
            "    \"resourceSummaries\" : [ \"Q4   Est:1.000  Imp:1.000  ResponseCount:1\" ]\n" +
            "  }, {\n" +
            "    \"id\" : \"While Loops-1\",\n" +
            "    \"label\" : \"While Loops\",\n" +
            "    \"knowledgeEstimate\" : 1.0,\n" +
            "    \"knowledgePrediction\" : 0.0,\n" +
            "    \"knowledgeDistFromAvg\" : 0.32500000000000007,\n" +
            "    \"dataImportance\" : 4.0,\n" +
            "    \"resourceSummaries\" : [ \"Q2   Est:1.000  Imp:1.000  ResponseCount:1\", \"HW4   Est:1.000  Imp:1.000  ResponseCount:1\" ]\n" +
            "  }, {\n" +
            "    \"id\" : \"Boolean Expressions-1\",\n" +
            "    \"label\" : \"Boolean Expressions\",\n" +
            "    \"knowledgeEstimate\" : 1.0,\n" +
            "    \"knowledgePrediction\" : 0.0,\n" +
            "    \"knowledgeDistFromAvg\" : 0.3666666666666667,\n" +
            "    \"dataImportance\" : 2.0,\n" +
            "    \"resourceSummaries\" : [ \"HW2   Est:1.000  Imp:1.000  ResponseCount:1\", \"HW1   Est:1.000  Imp:1.000  ResponseCount:1\" ]\n" +
            "  }, {\n" +
            "    \"id\" : \"For Loops-1\",\n" +
            "    \"label\" : \"For Loops\",\n" +
            "    \"knowledgeEstimate\" : 1.0,\n" +
            "    \"knowledgePrediction\" : 0.0,\n" +
            "    \"knowledgeDistFromAvg\" : 0.2833333333333333,\n" +
            "    \"dataImportance\" : 2.0,\n" +
            "    \"resourceSummaries\" : [ \"Q3   Est:1.000  Imp:1.000  ResponseCount:1\", \"HW5   Est:1.000  Imp:1.000  ResponseCount:1\" ]\n" +
            "  }, {\n" +
            "    \"id\" : \"If Statements-1\",\n" +
            "    \"label\" : \"If Statements\",\n" +
            "    \"knowledgeEstimate\" : 1.0,\n" +
            "    \"knowledgePrediction\" : 0.0,\n" +
            "    \"knowledgeDistFromAvg\" : 0.30833333333333324,\n" +
            "    \"dataImportance\" : 4.0,\n" +
            "    \"resourceSummaries\" : [ \"Q1   Est:1.000  Imp:1.000  ResponseCount:1\", \"HW3   Est:1.000  Imp:1.000  ResponseCount:1\" ]\n" +
            "  }, {\n" +
            "    \"id\" : \"Boolean Expressions-2\",\n" +
            "    \"label\" : \"Boolean Expressions\",\n" +
            "    \"knowledgeEstimate\" : 1.0,\n" +
            "    \"knowledgePrediction\" : 0.0,\n" +
            "    \"knowledgeDistFromAvg\" : 0.3666666666666667,\n" +
            "    \"dataImportance\" : 2.0,\n" +
            "    \"resourceSummaries\" : [ \"HW2   Est:1.000  Imp:1.000  ResponseCount:1\", \"HW1   Est:1.000  Imp:1.000  ResponseCount:1\" ]\n" +
            "  } ],\n" +
            "  \"links\" : [ {\n" +
            "    \"parent\" : \"While Loops-1\",\n" +
            "    \"child\" : \"Boolean Expressions-1\"\n" +
            "  }, {\n" +
            "    \"parent\" : \"Loops-1\",\n" +
            "    \"child\" : \"While Loops-1\"\n" +
            "  }, {\n" +
            "    \"parent\" : \"Loops-1\",\n" +
            "    \"child\" : \"For Loops-1\"\n" +
            "  }, {\n" +
            "    \"parent\" : \"Intro CS-1\",\n" +
            "    \"child\" : \"Loops-1\"\n" +
            "  }, {\n" +
            "    \"parent\" : \"If Statements-1\",\n" +
            "    \"child\" : \"Boolean Expressions-2\"\n" +
            "  }, {\n" +
            "    \"parent\" : \"Intro CS-1\",\n" +
            "    \"child\" : \"If Statements-1\"\n" +
            "  } ]\n" +
            "}";

    public static final String structureWithResourceConnectionsAsTree = "{\n" +
            "  \"name\" : \"Cs1ExampleGraph\",\n" +
            "  \"concepts\" : [ {\n" +
            "    \"id\" : \"Intro CS-1\",\n" +
            "    \"label\" : \"Intro CS\",\n" +
            "    \"knowledgeEstimate\" : 0.0,\n" +
            "    \"knowledgePrediction\" : 0.0,\n" +
            "    \"knowledgeDistFromAvg\" : 0.0,\n" +
            "    \"dataImportance\" : 0.0,\n" +
            "    \"resourceSummaries\" : [ \"Q5   Est:0.000  Imp:0.000  ResponseCount:0\" ]\n" +
            "  }, {\n" +
            "    \"id\" : \"Loops-1\",\n" +
            "    \"label\" : \"Loops\",\n" +
            "    \"knowledgeEstimate\" : 0.0,\n" +
            "    \"knowledgePrediction\" : 0.0,\n" +
            "    \"knowledgeDistFromAvg\" : 0.0,\n" +
            "    \"dataImportance\" : 0.0,\n" +
            "    \"resourceSummaries\" : [ \"Q4   Est:0.000  Imp:0.000  ResponseCount:0\" ]\n" +
            "  }, {\n" +
            "    \"id\" : \"While Loops-1\",\n" +
            "    \"label\" : \"While Loops\",\n" +
            "    \"knowledgeEstimate\" : 0.0,\n" +
            "    \"knowledgePrediction\" : 0.0,\n" +
            "    \"knowledgeDistFromAvg\" : 0.0,\n" +
            "    \"dataImportance\" : 0.0,\n" +
            "    \"resourceSummaries\" : [ \"Q2   Est:0.000  Imp:0.000  ResponseCount:0\", \"HW4   Est:0.000  Imp:0.000  ResponseCount:0\" ]\n" +
            "  }, {\n" +
            "    \"id\" : \"Boolean Expressions-1\",\n" +
            "    \"label\" : \"Boolean Expressions\",\n" +
            "    \"knowledgeEstimate\" : 0.0,\n" +
            "    \"knowledgePrediction\" : 0.0,\n" +
            "    \"knowledgeDistFromAvg\" : 0.0,\n" +
            "    \"dataImportance\" : 0.0,\n" +
            "    \"resourceSummaries\" : [ \"HW2   Est:0.000  Imp:0.000  ResponseCount:0\", \"HW1   Est:0.000  Imp:0.000  ResponseCount:0\" ]\n" +
            "  }, {\n" +
            "    \"id\" : \"For Loops-1\",\n" +
            "    \"label\" : \"For Loops\",\n" +
            "    \"knowledgeEstimate\" : 0.0,\n" +
            "    \"knowledgePrediction\" : 0.0,\n" +
            "    \"knowledgeDistFromAvg\" : 0.0,\n" +
            "    \"dataImportance\" : 0.0,\n" +
            "    \"resourceSummaries\" : [ \"Q3   Est:0.000  Imp:0.000  ResponseCount:0\", \"HW5   Est:0.000  Imp:0.000  ResponseCount:0\" ]\n" +
            "  }, {\n" +
            "    \"id\" : \"If Statements-1\",\n" +
            "    \"label\" : \"If Statements\",\n" +
            "    \"knowledgeEstimate\" : 0.0,\n" +
            "    \"knowledgePrediction\" : 0.0,\n" +
            "    \"knowledgeDistFromAvg\" : 0.0,\n" +
            "    \"dataImportance\" : 0.0,\n" +
            "    \"resourceSummaries\" : [ \"Q1   Est:0.000  Imp:0.000  ResponseCount:0\", \"HW3   Est:0.000  Imp:0.000  ResponseCount:0\" ]\n" +
            "  }, {\n" +
            "    \"id\" : \"Boolean Expressions-2\",\n" +
            "    \"label\" : \"Boolean Expressions\",\n" +
            "    \"knowledgeEstimate\" : 0.0,\n" +
            "    \"knowledgePrediction\" : 0.0,\n" +
            "    \"knowledgeDistFromAvg\" : 0.0,\n" +
            "    \"dataImportance\" : 0.0,\n" +
            "    \"resourceSummaries\" : [ \"HW2   Est:0.000  Imp:0.000  ResponseCount:0\", \"HW1   Est:0.000  Imp:0.000  ResponseCount:0\" ]\n" +
            "  } ],\n" +
            "  \"links\" : [ {\n" +
            "    \"parent\" : \"While Loops-1\",\n" +
            "    \"child\" : \"Boolean Expressions-1\"\n" +
            "  }, {\n" +
            "    \"parent\" : \"Loops-1\",\n" +
            "    \"child\" : \"While Loops-1\"\n" +
            "  }, {\n" +
            "    \"parent\" : \"Loops-1\",\n" +
            "    \"child\" : \"For Loops-1\"\n" +
            "  }, {\n" +
            "    \"parent\" : \"Intro CS-1\",\n" +
            "    \"child\" : \"Loops-1\"\n" +
            "  }, {\n" +
            "    \"parent\" : \"If Statements-1\",\n" +
            "    \"child\" : \"Boolean Expressions-2\"\n" +
            "  }, {\n" +
            "    \"parent\" : \"Intro CS-1\",\n" +
            "    \"child\" : \"If Statements-1\"\n" +
            "  } ]\n" +
            "}";
}