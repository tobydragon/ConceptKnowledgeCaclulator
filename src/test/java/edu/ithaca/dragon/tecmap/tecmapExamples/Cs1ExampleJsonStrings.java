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
}