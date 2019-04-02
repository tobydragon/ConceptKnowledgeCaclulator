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

    public static final String structureWithResourcesAsTreeString = "{\n" +
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

    public static final String assessment1Str = "[ {\n" +
            "  \"learningResourceId\" : \"Q1\",\n" +
            "  \"resourceTypes\" : [ \"ASSESSMENT\", \"PRACTICE\" ],\n" +
            "  \"conceptIds\" : [ ],\n" +
            "  \"dataImportance\" : 0.0,\n" +
            "  \"maxPossibleKnowledgeEstimate\" : 10.0\n" +
            "}, {\n" +
            "  \"learningResourceId\" : \"Q2\",\n" +
            "  \"resourceTypes\" : [ \"ASSESSMENT\", \"PRACTICE\" ],\n" +
            "  \"conceptIds\" : [ ],\n" +
            "  \"dataImportance\" : 0.0,\n" +
            "  \"maxPossibleKnowledgeEstimate\" : 5.0\n" +
            "}, {\n" +
            "  \"learningResourceId\" : \"Q3\",\n" +
            "  \"resourceTypes\" : [ \"ASSESSMENT\", \"PRACTICE\" ],\n" +
            "  \"conceptIds\" : [ ],\n" +
            "  \"dataImportance\" : 0.0,\n" +
            "  \"maxPossibleKnowledgeEstimate\" : 10.0\n" +
            "}, {\n" +
            "  \"learningResourceId\" : \"Q4\",\n" +
            "  \"resourceTypes\" : [ \"ASSESSMENT\", \"PRACTICE\" ],\n" +
            "  \"conceptIds\" : [ ],\n" +
            "  \"dataImportance\" : 0.0,\n" +
            "  \"maxPossibleKnowledgeEstimate\" : 5.0\n" +
            "}, {\n" +
            "  \"learningResourceId\" : \"Q5\",\n" +
            "  \"resourceTypes\" : [ \"ASSESSMENT\", \"PRACTICE\" ],\n" +
            "  \"conceptIds\" : [ ],\n" +
            "  \"dataImportance\" : 0.0,\n" +
            "  \"maxPossibleKnowledgeEstimate\" : 100.0\n" +
            "} ]";

    public static final String assessment1And2Str = "[ {\n" +
            "  \"learningResourceId\" : \"Q1\",\n" +
            "  \"resourceTypes\" : [ \"ASSESSMENT\", \"PRACTICE\" ],\n" +
            "  \"conceptIds\" : [ ],\n" +
            "  \"dataImportance\" : 0.0,\n" +
            "  \"maxPossibleKnowledgeEstimate\" : 10.0\n" +
            "}, {\n" +
            "  \"learningResourceId\" : \"Q2\",\n" +
            "  \"resourceTypes\" : [ \"ASSESSMENT\", \"PRACTICE\" ],\n" +
            "  \"conceptIds\" : [ ],\n" +
            "  \"dataImportance\" : 0.0,\n" +
            "  \"maxPossibleKnowledgeEstimate\" : 5.0\n" +
            "}, {\n" +
            "  \"learningResourceId\" : \"Q3\",\n" +
            "  \"resourceTypes\" : [ \"ASSESSMENT\", \"PRACTICE\" ],\n" +
            "  \"conceptIds\" : [ ],\n" +
            "  \"dataImportance\" : 0.0,\n" +
            "  \"maxPossibleKnowledgeEstimate\" : 10.0\n" +
            "}, {\n" +
            "  \"learningResourceId\" : \"Q4\",\n" +
            "  \"resourceTypes\" : [ \"ASSESSMENT\", \"PRACTICE\" ],\n" +
            "  \"conceptIds\" : [ ],\n" +
            "  \"dataImportance\" : 0.0,\n" +
            "  \"maxPossibleKnowledgeEstimate\" : 5.0\n" +
            "}, {\n" +
            "  \"learningResourceId\" : \"Q5\",\n" +
            "  \"resourceTypes\" : [ \"ASSESSMENT\", \"PRACTICE\" ],\n" +
            "  \"conceptIds\" : [ ],\n" +
            "  \"dataImportance\" : 0.0,\n" +
            "  \"maxPossibleKnowledgeEstimate\" : 100.0\n" +
            "}, {\n" +
            "  \"learningResourceId\" : \"HW1\",\n" +
            "  \"resourceTypes\" : [ \"ASSESSMENT\", \"PRACTICE\" ],\n" +
            "  \"conceptIds\" : [ ],\n" +
            "  \"dataImportance\" : 0.0,\n" +
            "  \"maxPossibleKnowledgeEstimate\" : 1.0\n" +
            "}, {\n" +
            "  \"learningResourceId\" : \"HW2\",\n" +
            "  \"resourceTypes\" : [ \"ASSESSMENT\", \"PRACTICE\" ],\n" +
            "  \"conceptIds\" : [ ],\n" +
            "  \"dataImportance\" : 0.0,\n" +
            "  \"maxPossibleKnowledgeEstimate\" : 5.0\n" +
            "}, {\n" +
            "  \"learningResourceId\" : \"HW3\",\n" +
            "  \"resourceTypes\" : [ \"ASSESSMENT\", \"PRACTICE\" ],\n" +
            "  \"conceptIds\" : [ ],\n" +
            "  \"dataImportance\" : 0.0,\n" +
            "  \"maxPossibleKnowledgeEstimate\" : 2.0\n" +
            "}, {\n" +
            "  \"learningResourceId\" : \"HW4\",\n" +
            "  \"resourceTypes\" : [ \"ASSESSMENT\", \"PRACTICE\" ],\n" +
            "  \"conceptIds\" : [ ],\n" +
            "  \"dataImportance\" : 0.0,\n" +
            "  \"maxPossibleKnowledgeEstimate\" : 1.0\n" +
            "}, {\n" +
            "  \"learningResourceId\" : \"HW5\",\n" +
            "  \"resourceTypes\" : [ \"ASSESSMENT\", \"PRACTICE\" ],\n" +
            "  \"conceptIds\" : [ ],\n" +
            "  \"dataImportance\" : 0.0,\n" +
            "  \"maxPossibleKnowledgeEstimate\" : 1.0\n" +
            "} ]";

    public static String allConceptsString = "[While Loops, For Loops, Boolean Expressions, Intro CS, Loops, If Statements]";
    public static String allConceptsStringAsJson = "[\"While Loops\",\"For Loops\",\"Boolean Expressions\",\"Intro CS\",\"Loops\",\"If Statements\"]";

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

    public static final String resourcesConnectedString = "[ {\n" +
            "  \"learningResourceId\" : \"Q1\",\n" +
            "  \"resourceTypes\" : [ \"PRACTICE\" ],\n" +
            "  \"conceptIds\" : [ \"If Statements\" ],\n" +
            "  \"dataImportance\" : 0.0,\n" +
            "  \"maxPossibleKnowledgeEstimate\" : 10.0\n" +
            "}, {\n" +
            "  \"learningResourceId\" : \"Q2\",\n" +
            "  \"resourceTypes\" : [ \"PRACTICE\" ],\n" +
            "  \"conceptIds\" : [ \"While Loops\" ],\n" +
            "  \"dataImportance\" : 0.0,\n" +
            "  \"maxPossibleKnowledgeEstimate\" : 5.0\n" +
            "}, {\n" +
            "  \"learningResourceId\" : \"Q3\",\n" +
            "  \"resourceTypes\" : [ \"PRACTICE\" ],\n" +
            "  \"conceptIds\" : [ \"For Loops\" ],\n" +
            "  \"dataImportance\" : 0.0,\n" +
            "  \"maxPossibleKnowledgeEstimate\" : 10.0\n" +
            "}, {\n" +
            "  \"learningResourceId\" : \"Q4\",\n" +
            "  \"resourceTypes\" : [ \"PRACTICE\" ],\n" +
            "  \"conceptIds\" : [ \"Loops\" ],\n" +
            "  \"dataImportance\" : 0.0,\n" +
            "  \"maxPossibleKnowledgeEstimate\" : 5.0\n" +
            "}, {\n" +
            "  \"learningResourceId\" : \"Q5\",\n" +
            "  \"resourceTypes\" : [ \"PRACTICE\" ],\n" +
            "  \"conceptIds\" : [ \"Intro CS\" ],\n" +
            "  \"dataImportance\" : 0.0,\n" +
            "  \"maxPossibleKnowledgeEstimate\" : 100.0\n" +
            "}, {\n" +
            "  \"learningResourceId\" : \"HW1\",\n" +
            "  \"resourceTypes\" : [ \"PRACTICE\" ],\n" +
            "  \"conceptIds\" : [ \"Boolean Expressions\" ],\n" +
            "  \"dataImportance\" : 0.0,\n" +
            "  \"maxPossibleKnowledgeEstimate\" : 1.0\n" +
            "}, {\n" +
            "  \"learningResourceId\" : \"HW2\",\n" +
            "  \"resourceTypes\" : [ \"PRACTICE\" ],\n" +
            "  \"conceptIds\" : [ \"Boolean Expressions\" ],\n" +
            "  \"dataImportance\" : 0.0,\n" +
            "  \"maxPossibleKnowledgeEstimate\" : 5.0\n" +
            "}, {\n" +
            "  \"learningResourceId\" : \"HW3\",\n" +
            "  \"resourceTypes\" : [ \"PRACTICE\" ],\n" +
            "  \"conceptIds\" : [ \"If Statements\" ],\n" +
            "  \"dataImportance\" : 0.0,\n" +
            "  \"maxPossibleKnowledgeEstimate\" : 2.0\n" +
            "}, {\n" +
            "  \"learningResourceId\" : \"HW4\",\n" +
            "  \"resourceTypes\" : [ \"PRACTICE\" ],\n" +
            "  \"conceptIds\" : [ \"While Loops\" ],\n" +
            "  \"dataImportance\" : 0.0,\n" +
            "  \"maxPossibleKnowledgeEstimate\" : 1.0\n" +
            "}, {\n" +
            "  \"learningResourceId\" : \"HW5\",\n" +
            "  \"resourceTypes\" : [ \"PRACTICE\" ],\n" +
            "  \"conceptIds\" : [ \"For Loops\" ],\n" +
            "  \"dataImportance\" : 0.0,\n" +
            "  \"maxPossibleKnowledgeEstimate\" : 1.0\n" +
            "} ]";

    public static final String learningMaterialsLinks = "[ {\n" +
            "  \"learningResourceId\" : \"[INFORMATION] 4 [Cs1]\",\n" +
            "  \"resourceTypes\" : [ \"PRACTICE\" ],\n" +
            "  \"conceptIds\" : [ \"If Statements\" ],\n" +
            "  \"dataImportance\" : 0.0,\n" +
            "  \"maxPossibleKnowledgeEstimate\" : 10.0\n" +
            "} ]";

    public static final String learningMaterials = "[{\n" +
            "  \"types\" : [ \"ASSESSMENT\" ],\n" +
            "  \"id\" : \"[ASSESSMENT] 1 [Cs1]\",\n" +
            "  \"content\" : \"What's are advantages of using while loops instead of for loops?\",\n" +
            "  \"url\" : \"\",\n" +
            "  \"tagsMap\" : {\n" +
            "    \"Cs1\" : 1,\n" +
            "    \"using\" : 1,\n" +
            "    \"advantages\" : 1\n" +
            "  }\n" +
            "},{\n" +
            "  \"types\" : [ \"ASSESSMENT\" ],\n" +
            "  \"id\" : \"[ASSESSMENT] 2 [Cs1]\",\n" +
            "  \"content\" : \"How many choices are possible when executing an if-statement?\",\n" +
            "  \"url\" : \"\",\n" +
            "  \"tagsMap\" : {\n" +
            "    \"Cs1\" : 1,\n" +
            "    \"if-statement\" : 1,\n" +
            "    \"executing\" : 1\n" +
            "  }\n" +
            "},{\n" +
            "  \"types\" : [ \"ASSESSMENT\" ],\n" +
            "  \"id\" : \"[ASSESSMENT] 3 [Cs1]\",\n" +
            "  \"content\" : \"When does an 'else' conditional get executed in a function?\",\n" +
            "  \"url\" : \"\",\n" +
            "  \"tagsMap\" : {\n" +
            "    \"Cs1\" : 1,\n" +
            "    \"conditional\" : 1,\n" +
            "    \"does\" : 1\n" +
            "  }\n" +
            "},{\n" +
            "  \"types\" : [ \"INFORMATION\" ],\n" +
            "  \"id\" : \"[INFORMATION] 4 [Cs1]\",\n" +
            "  \"content\" : \"https://www.learncpp.com/cpp-tutorial/52-if-statements/\",\n" +
            "  \"url\" : \"https://www.learncpp.com/cpp-tutorial/52-if-statements/\",\n" +
            "  \"tagsMap\" : {\n" +
            "    \"Cs1\" : 1,\n" +
            "    \"implicit\" : 1,\n" +
            "    \"using\" : 2,\n" +
            "    \"operator=\" : 1,\n" +
            "    \"conditional\" : 2,\n" +
            "    \"blocks\" : 1,\n" +
            "    \"operator==\" : 1,\n" +
            "    \"dangling\" : 1,\n" +
            "    \"statements\" : 6,\n" +
            "    \"logical\" : 1,\n" +
            "    \"null\" : 2,\n" +
            "    \"operators\" : 1,\n" +
            "    \"uses\" : 1,\n" +
            "    \"returns\" : 1,\n" +
            "    \"nesting\" : 1,\n" +
            "    \"chaining\" : 1\n" +
            "  }\n" +
            "},{\n" +
            "  \"types\" : [ \"INFORMATION\" ],\n" +
            "  \"id\" : \"[INFORMATION] 5 [Cs1]\",\n" +
            "  \"content\" : \"https://www.programiz.com/c-programming/c-if-else-statement\",\n" +
            "  \"url\" : \"https://www.programiz.com/c-programming/c-if-else-statement\",\n" +
            "  \"tagsMap\" : {\n" +
            "    \"Cs1\" : 1\n" +
            "  }\n" +
            "},{\n" +
            "  \"types\" : [ \"INFORMATION\" ],\n" +
            "  \"id\" : \"[INFORMATION] 6 [Cs1]\",\n" +
            "  \"content\" : \"https://programming.guide/go/for-loop.html\",\n" +
            "  \"url\" : \"https://programming.guide/go/for-loop.html\",\n" +
            "  \"tagsMap\" : {\n" +
            "    \"Cs1\" : 1\n" +
            "  }\n" +
            "}]";
}