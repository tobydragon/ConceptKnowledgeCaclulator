package edu.ithaca.dragonlab.ckc;

import edu.ithaca.dragonlab.ckc.conceptgraph.*;
import edu.ithaca.dragonlab.ckc.io.CSVReader;
import edu.ithaca.dragonlab.ckc.io.CohortConceptGraphsRecord;
import edu.ithaca.dragonlab.ckc.io.ConceptGraphRecord;
import edu.ithaca.dragonlab.ckc.io.LearningObjectLinkRecord;
import edu.ithaca.dragonlab.ckc.learningobject.LearningObject;
import edu.ithaca.dragonlab.ckc.learningobject.LearningObjectResponse;
import edu.ithaca.dragonlab.ckc.suggester.LearningObjectSuggester;
import edu.ithaca.dragonlab.ckc.suggester.SuggestionResource;
import stats.BasicRFunctions;

import java.io.IOException;
import java.util.*;

/**
 * Created by tdragon on 6/8/17.
 */
public class ConceptKnowledgeCalculator implements ConceptKnowledgeCalculatorAPI{
    private static final String OUTPUT_PATH = "out/";

    //graphs
    private CohortConceptGraphs cohortConceptGraphs;
    private ConceptGraph structureGraph;

    public enum Mode{
        STRUCTUREGRAPH, COHORTGRAPH, STRUCTUREGRAPHWITHRESOURCE, STRUCTUREGRAPHWITHASSESSMENT, NODATA
    }

    private Mode currentMode;

    //is only one file for structure and one for resource files.
    private List<String> structureFiles;
    private List<String> resourceFiles;

    //there is allowed multiple files for the assessment files
    private List<String> assessmentFiles;

    public ConceptKnowledgeCalculator() {
        cohortConceptGraphs = null;
        structureGraph = null;
        currentMode= null;
        structureFiles = new ArrayList<>();
        resourceFiles = new ArrayList<>();
        assessmentFiles = new ArrayList<>();
        currentMode = Mode.NODATA;
    }

    public ConceptKnowledgeCalculator(String structureFileName) throws IOException{
        this();
        structureFiles.add(structureFileName);
        currentMode= Mode.STRUCTUREGRAPH;
        clearAndCreateStructureData(new ArrayList<>());
    }

    public ConceptKnowledgeCalculator(String structureFilename, String resourceFilename, String assessmentFilename) throws IOException{
        this();
        structureFiles.add(structureFilename);
        resourceFiles.add(resourceFilename);
        assessmentFiles.add(assessmentFilename);
        currentMode= Mode.COHORTGRAPH;
        clearAndCreateCohortData(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }



    @Override
    public void clearAndCreateStructureData(List<String> structureFilename) throws IOException{
        structureGraph = null;
        cohortConceptGraphs=null;

        if (structureFilename.size()!=0 &&!structureFiles.contains(structureFilename.get(0))) {
            structureFiles.clear();
            structureFiles.add(structureFilename.get(0));
        }

        ConceptGraphRecord structureRecord = ConceptGraphRecord.buildFromJson(structureFiles.get(0));
        structureGraph = new ConceptGraph(structureRecord);

    }

    @Override
    public void setupStructureData(String struct) throws Exception {
        ConceptGraphRecord conceptGraph = ConceptGraphRecord.buildFromJson(struct);
        if(conceptGraph.getConcepts().size()>0){
            structureFiles.clear();
            structureFiles.add(struct);

            clearAndCreateStructureData(structureFiles);
            currentMode=Mode.STRUCTUREGRAPH;
        }else{
            throw new Exception("Structure file invalid");
        }

    }

    @Override
    public void clearAndCreateCohortData(List<String> structureFilename, List<String> resourceFilename, List<String> assessmentFilename) throws IOException {
        cohortConceptGraphs = null;
        structureGraph = null;

        //to change the structure file, clear the old list and add the new one.
        if (structureFilename.size()!=0 &&!structureFiles.contains(structureFilename.get(0))) {
            structureFiles.clear();
            structureFiles.add(structureFilename.get(0));
        }


        if(resourceFilename.size()!=0 && !resourceFiles.contains(resourceFilename.get(0))){
            resourceFiles.add(resourceFilename.get(0));
        }
        //create the graph structure to be copied for each user
        ConceptGraphRecord structureRecord = ConceptGraphRecord.buildFromJson(structureFiles.get(0));
        List<LearningObjectLinkRecord> linkRecord = new ArrayList<>();
        for (String rFiles : resourceFiles){
            List<LearningObjectLinkRecord> temp = LearningObjectLinkRecord.buildListFromJson(rFiles);
            linkRecord.addAll(temp);
        }

        if(assessmentFilename.size()!=0 && !assessmentFiles.contains(assessmentFilename.get(0))){
            assessmentFiles.add(assessmentFilename.get(0));
        }

        ConceptGraph graph = new ConceptGraph(structureRecord, linkRecord);

        //create the data to be used to create and populate the graph copies
        List<LearningObjectResponse> assessments = new ArrayList<>();

        for (String aname: assessmentFiles){
            CSVReader csvReader = new CSVReader(aname);
            List<LearningObjectResponse> temp = csvReader.getManualGradedResponses();
            assessments.addAll(temp);
        }

        //create the average and individual graphs
        cohortConceptGraphs = new CohortConceptGraphs(graph, assessments);
        currentMode=Mode.COHORTGRAPH;
    }

    @Override
    public void switchToStructure() throws Exception {
        if (currentMode == Mode.COHORTGRAPH){
            clearAndCreateStructureData(structureFiles);
            resourceFiles.clear();
            assessmentFiles.clear();
            currentMode = Mode.STRUCTUREGRAPH;
            cohortConceptGraphs=null;
        }else{
            throw new Exception("Wrong mode");
        }
    }

    @Override
    public boolean assessmentIsValid(String name){
        CSVReader csvReader = new CSVReader(name);
        if (csvReader.getManualGradedResponses().size()>0){
            return true;
        }else{
            return false;
        }

    }

    @Override
    public void setupClearandCreateCohort(String struct, String res, String assess) throws Exception {
        List<String> sList = new ArrayList<>();
        List<String> rList = new ArrayList<>();
        List<String> aList = new ArrayList<>();

        boolean sbool;
        boolean rbool;
        boolean abool;

        ConceptGraphRecord conceptGraph = ConceptGraphRecord.buildFromJson(struct);
        if(conceptGraph.getConcepts().size()>0){
            sbool=true;
        }else{
            throw new Exception("Structure file invalid");
        }


        List<LearningObjectLinkRecord> temp = LearningObjectLinkRecord.buildListFromJson(res);
        if(temp.size()>0){
            rbool=true;
        }else{
            throw new Exception("Resource file invalid");
        }

        if(assessmentIsValid(assess)){
            abool=true;
        }else{
            throw new Exception("Assessment file invalid");
        }

        if(sbool && rbool && abool){
            structureFiles.clear();
            sList.add(struct);
            resourceFiles.clear();
            rList.add(res);
            assessmentFiles.clear();
            aList.add(assess);
            clearAndCreateCohortData(sList, rList, aList);

            currentMode= Mode.COHORTGRAPH;

        }
    }

    @Override
    public void replaceCohortGraph(String graph) throws Exception{
        ConceptGraphRecord conceptGraph = ConceptGraphRecord.buildFromJson(graph);
        if(conceptGraph.getConcepts().size()>0){
            List<String>  structure = new ArrayList<>();
            structure.add(graph);

            clearAndCreateCohortData(structure,resourceFiles,assessmentFiles);
        }else{
            throw new Exception("Structure file invalid");
        }
    }

    @Override
    public     void addResourceAndAssessment(String resource, String assignment) throws  Exception{
        if(currentMode == Mode.STRUCTUREGRAPH) {
            List<LearningObjectLinkRecord> temp = LearningObjectLinkRecord.buildListFromJson(resource);
            if(temp.size()>0){
                if(assessmentIsValid(assignment)){
                    resourceFiles.add(resource);
                    assessmentFiles.add(assignment);
                }else{
                    throw new Exception("Assessment file invalid");
                }
            }else{
                throw new Exception("Resource file invalid");
            }


            try{
                clearAndCreateCohortData(structureFiles, resourceFiles, assessmentFiles);
                currentMode = Mode.COHORTGRAPH;

            }catch (Exception e){
                e.printStackTrace();
            }
        }else{
            throw new Exception("Wrong mode");
        }
    }

    @Override
    public void addResource(String secondResourceFile) throws Exception {

        if(currentMode== Mode.STRUCTUREGRAPH || currentMode== Mode.STRUCTUREGRAPHWITHRESOURCE){
            List<LearningObjectLinkRecord> temp = LearningObjectLinkRecord.buildListFromJson(secondResourceFile);
            if(temp.size()>0){
                resourceFiles.add(secondResourceFile);
                currentMode=Mode.STRUCTUREGRAPHWITHRESOURCE;
            }else{
                throw new Exception("Resource file invalid");
            }


        }else if(currentMode== Mode.COHORTGRAPH || currentMode == Mode.STRUCTUREGRAPHWITHASSESSMENT){
            try {
                List<LearningObjectLinkRecord> temp = LearningObjectLinkRecord.buildListFromJson(secondResourceFile);
                if(temp.size()>0){
                    resourceFiles.add(secondResourceFile);
                    clearAndCreateCohortData(structureFiles, resourceFiles, assessmentFiles);
                    currentMode= Mode.COHORTGRAPH;
                }else{
                    throw new Exception("Resource file invalid");
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }else{
            throw new Exception("Wrong mode");
        }

    }

    @Override
    public void addAssignment(String secondAssessmentFilename) throws Exception {
        if(currentMode==Mode.COHORTGRAPH) {
            if(assessmentIsValid(secondAssessmentFilename)){
                assessmentFiles.add(secondAssessmentFilename);
                clearAndCreateCohortData(structureFiles, resourceFiles, assessmentFiles);
            }else{
                throw new Exception();
            }

        } else if(currentMode==Mode.STRUCTUREGRAPHWITHRESOURCE ){
            if(assessmentIsValid(secondAssessmentFilename)){
                assessmentFiles.add(secondAssessmentFilename);
                clearAndCreateCohortData(structureFiles, resourceFiles, assessmentFiles);
                currentMode = Mode.COHORTGRAPH;

            }else{
                throw new Exception();
            }

        } else if(currentMode == Mode.STRUCTUREGRAPHWITHASSESSMENT ){
            if(assessmentIsValid(secondAssessmentFilename)){
                assessmentFiles.add(secondAssessmentFilename);
            }else{
                throw new Exception();
            }
        }else if(currentMode== Mode.STRUCTUREGRAPH){
                if(assessmentIsValid(secondAssessmentFilename)){
                    assessmentFiles.add(secondAssessmentFilename);
                    currentMode = Mode.STRUCTUREGRAPHWITHASSESSMENT;
                }else{
                    throw new Exception();
                }

        }else{
            throw new Exception("Wrong mode");

        }
    }



    @Override
    public void removeAssessmentFile(String assessmentFile) throws Exception {
        if(assessmentFiles.size()<1){
            throw new Exception("You don't have any files");

        }else {
            if(!assessmentFiles.contains(assessmentFile)){
                throw new Exception("Can't find file");

            }else {

                if (currentMode == Mode.COHORTGRAPH) {
                    assessmentFiles.remove(assessmentFile);
                    clearAndCreateCohortData(structureFiles, resourceFiles, assessmentFiles);

                } else if (currentMode == Mode.STRUCTUREGRAPHWITHASSESSMENT) {
                    assessmentFiles.remove(assessmentFile);

                }
            }
        }
    }

    @Override
    public void replaceResourceFile(String resourceFile) throws Exception {
        if(currentMode==Mode.STRUCTUREGRAPHWITHRESOURCE || currentMode==Mode.COHORTGRAPH){

            try {
                List<LearningObjectLinkRecord> temp = LearningObjectLinkRecord.buildListFromJson(resourceFile);

                resourceFiles.clear();
                resourceFiles.add(resourceFile);
                clearAndCreateCohortData(structureFiles, resourceFiles, assessmentFiles);
            }catch (Exception e){
                throw new Exception("Can't find file");
            }
        }else if(currentMode==Mode.STRUCTUREGRAPH){
            try {
                List<LearningObjectLinkRecord> temp = LearningObjectLinkRecord.buildListFromJson(resourceFile);

                resourceFiles.clear();
                resourceFiles.add(resourceFile);
                clearAndCreateStructureData(structureFiles);
                currentMode = Mode.STRUCTUREGRAPHWITHRESOURCE;
            }catch (Exception e){
                throw new Exception("Can't find file");

            }

        } else if(currentMode==Mode.STRUCTUREGRAPHWITHASSESSMENT){
            try {
                List<LearningObjectLinkRecord> temp = LearningObjectLinkRecord.buildListFromJson(resourceFile);

                resourceFiles.clear();
                resourceFiles.add(resourceFile);
                clearAndCreateCohortData(structureFiles, resourceFiles, assessmentFiles);
                currentMode = Mode.COHORTGRAPH;
            }catch (Exception e){
                throw new Exception("Can't find file");

            }

        }else{
            throw new Exception("Wrong mode");
        }
    }


    @Override
    public List<String> currentAssignments(){
        List<String> temp = getAssessmentFiles();
        return temp;
    }

    @Override
    public List<String> currentResource(){
        List<String> temp = getResourceFiles();
        return temp;
    }


    @Override
    public String getCohortGraphsUrl() throws Exception {
        if(currentMode==Mode.COHORTGRAPH){
            CohortConceptGraphsRecord toFile = cohortConceptGraphs.buildCohortConceptTreeRecord();
            String file = OUTPUT_PATH + "cohortTreesCurrent.json";
            toFile.writeToJson(file);
            return "To view graph, right-click CohortGraph.html and choose \"open in Browser\" in ConceptKnowledgeCalculator/ckcvisualizer";

        }else if(currentMode==Mode.STRUCTUREGRAPH || currentMode==Mode.STRUCTUREGRAPHWITHASSESSMENT || currentMode==Mode.STRUCTUREGRAPHWITHRESOURCE){
            ConceptGraphRecord toFile = TreeConverter.makeTreeCopy(structureGraph).buildConceptGraphRecord();
            String file = OUTPUT_PATH + "structureTreeCurrent.json";
            toFile.writeToJson(file);
            return "To view graph, right-click StructureGraph.html and choose \"open in Browser\" in ConceptKnowledgeCalculator/ckcvisualizer";
        }else{
            throw new Exception("Wrong mode");
        }
    }

    @Override
    public SuggestionResource calcIndividualGraphSuggestions(String userId) throws Exception {
        if(currentMode==Mode.COHORTGRAPH) {
            if (cohortConceptGraphs != null) {
                ConceptGraph userGraph;
                userGraph = cohortConceptGraphs.getUserGraph(userId);
                if (userGraph != null) {
                    List<ConceptNode> concepts;

                    concepts = LearningObjectSuggester.conceptsToWorkOn(userGraph);

                    return new SuggestionResource(userGraph, concepts);

                } else {
                    throw new Exception("Invalid User ID");
                }

            } else {
                return new SuggestionResource();
            }
        }else{
            throw new Exception("Wrong mode");
        }
    }

    @Override
    public SuggestionResource calcIndividualSpecificConceptSuggestions(String userId, String conceptId) throws Exception {
        if(currentMode==Mode.COHORTGRAPH) {
            if (cohortConceptGraphs != null) {
                ConceptGraph userGraph;
                userGraph = cohortConceptGraphs.getUserGraph(userId);
                if (userGraph != null) {
                    ConceptNode node;
                    node = userGraph.findNodeById(conceptId);
                    if (node != null) {
                        List<ConceptNode> concepts = new ArrayList<ConceptNode>();
                        concepts.add(node);

                        return new SuggestionResource(userGraph, concepts);

                    } else {
                        throw new Exception("Invalid Concept");
                    }
                } else {
                    throw new Exception("Invalid User ID");
                }
            } else {
                return new SuggestionResource();
            }
        }else{
            throw new Exception("Wrong mode");
        }
    }


    @Override
    public List<String> calcIndividualConceptNodesSuggestions(String userID) throws Exception{
        if(currentMode==Mode.COHORTGRAPH) {
            if (cohortConceptGraphs != null) {
                ConceptGraph userGraph;
                userGraph = cohortConceptGraphs.getUserGraph(userID);

                if (userGraph != null) {
                    List<ConceptNode> nodeList = LearningObjectSuggester.conceptsToWorkOn(userGraph);

                    List<String> suggestedConceptIDList = new ArrayList<>();
                    for (ConceptNode node : nodeList) {
                        suggestedConceptIDList.add(node.getID());
                    }

                    return suggestedConceptIDList;
                } else {
                    throw new Exception("Invalid User ID");
                }
            } else {
                return new ArrayList<>();
            }
        }else{
            throw new Exception("Wrong Mode");
        }
    }

    public String csvToResource() throws Exception {
        if(currentMode==Mode.STRUCTUREGRAPHWITHASSESSMENT) {
            return csvToResource(assessmentFiles, OUTPUT_PATH + "resourcesWithoutConceptConnections.json");
        }
        else {
            throw new Exception("Wrong Mode");
        }
    }

    public static String csvToResource(List<String> assessmentFiles, String destinationFilepath) throws Exception{
            List<LearningObject> fullLoList = new ArrayList<LearningObject>();
            fullLoList = CSVReader.learningObjectsFromCSVList(assessmentFiles);
            List<LearningObjectLinkRecord> lolrList = LearningObjectLinkRecord.createLearningObjectLinkRecords(fullLoList, 10);
            LearningObjectLinkRecord.lolrToJSON(lolrList, destinationFilepath);
            return "Your file has been written to :"+ destinationFilepath;
    }

    public List<String> getUserIdList()throws Exception{
        if(currentMode == Mode.COHORTGRAPH) {
            Map<String, ConceptGraph> userMap = cohortConceptGraphs.getUserToGraph();
            List<String> userList = new ArrayList<String>(userMap.keySet());
            java.util.Collections.sort(userList);

            return userList;
        }else{
            throw new Exception("Wrong Mode");
        }
    }

    public double getLearningObjectAvg(String learningObject) throws Exception {
        if(currentMode==Mode.COHORTGRAPH) {
            ConceptGraph graph = cohortConceptGraphs.getAvgGraph();
            Map<String, LearningObject> loMap = graph.getLearningObjectMap();
            Collection<LearningObject> objList = loMap.values();
            ArrayList<LearningObject> list;
            list = new ArrayList<LearningObject>(objList);
            KnowledgeEstimateMatrix myMatrix = new KnowledgeEstimateMatrix(list);
            LearningObject concept = loMap.get(learningObject);

            if (concept != null) {
                double result = BasicRFunctions.LearningObjectAvg(myMatrix, concept);
                return result;
            } else {
                throw new NullPointerException();
            }
        }else {
            throw new Exception("Wrong Mode");
        }

    }

    public double getStudentAvg(String user)throws NullPointerException{
        ConceptGraph graph = cohortConceptGraphs.getAvgGraph();
        Map<String, LearningObject> loMap = graph.getLearningObjectMap();
        List<LearningObject> objList = new ArrayList<LearningObject>(loMap.values());
        KnowledgeEstimateMatrix myMatrix = new KnowledgeEstimateMatrix(objList);
        List<String> userIdList = myMatrix.getUserIdList();

        if(userIdList.contains(user)) {
            return BasicRFunctions.StudentKnowledgeEstAvg(myMatrix, user);
        }else{
            throw new NullPointerException();
        }
    }


    public List<String> getAssessmentFiles(){
        return assessmentFiles;
    }

    public Mode getCurrentMode(){
        return currentMode;
    }

    //testing purposes
    public List<String> getStructureFiles(){
        return structureFiles;
    }

    public List<String> getResourceFiles(){
        return resourceFiles;
    }

    public CohortConceptGraphs getCohortConceptGraphs(){
        return cohortConceptGraphs;
    }

    public ConceptGraph getStructureGraph(){
        return structureGraph;
    }

}
