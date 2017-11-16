package edu.ithaca.dragonlab.ckc;

import edu.ithaca.dragonlab.ckc.conceptgraph.*;
import edu.ithaca.dragonlab.ckc.io.CSVReader;
import edu.ithaca.dragonlab.ckc.io.CohortConceptGraphsRecord;
import edu.ithaca.dragonlab.ckc.io.ConceptGraphRecord;
import edu.ithaca.dragonlab.ckc.io.LearningObjectLinkRecord;
import edu.ithaca.dragonlab.ckc.learningobject.LearningObject;
import edu.ithaca.dragonlab.ckc.learningobject.LearningObjectResponse;
import edu.ithaca.dragonlab.ckc.suggester.GroupSuggester.*;
import edu.ithaca.dragonlab.ckc.suggester.LearningObjectSuggester;
import edu.ithaca.dragonlab.ckc.suggester.SuggestionResource;
import stats.RFunctions;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by tdragon on 6/8/17.
 */
public class ConceptKnowledgeCalculator implements ConceptKnowledgeCalculatorAPI{
    private static final String OUTPUT_PATH = "ckcvisualizer/json/";

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
        List<String> struct = new ArrayList<String>();
        struct.add(structureFileName);
        clearAndCreateStructureData(struct);
    }

    public ConceptKnowledgeCalculator(String structureFilename, String resourceFilename, String assessmentFilename) throws IOException{
        this();

        List<String>  structure = new ArrayList<>();
        structure.add(structureFilename);

        List<String>  resource = new ArrayList<>();
        resource.add(resourceFilename);

        List<String>  assessment = new ArrayList<>();
        assessment.add(assessmentFilename);

        clearAndCreateCohortData(structure, resource, assessment);
    }

    public ConceptKnowledgeCalculator(List<String> structureFilenames, List<String> resourceFilenames, List<String> assessmentFilenames) throws IOException{
        this();
        clearAndCreateCohortData(structureFilenames, resourceFilenames, assessmentFilenames);
    }


    @Override
    public void clearAndCreateStructureData(List<String> structureFilename) throws IOException{

        structureGraph = null;
        cohortConceptGraphs=null;
        structureFiles.clear();
        resourceFiles.clear();
        assessmentFiles.clear();

        structureFiles.add(structureFilename.get(0));

        ConceptGraphRecord structureRecord = ConceptGraphRecord.buildFromJson(structureFiles.get(0));
        structureGraph = new ConceptGraph(structureRecord);
        currentMode=Mode.STRUCTUREGRAPH;

    }

    @Override
    public void updateStructureFile(String file) throws IOException {
        Mode tempMode = currentMode;

        List<String> strucTemp = new ArrayList<>();
        strucTemp.add(file);


        List<String> resourceTemp = new ArrayList<>();
        resourceTemp.addAll(resourceFiles);

        List<String> assessTemp = new ArrayList<>();
        assessTemp.addAll(assessmentFiles);

        clearAndCreateStructureData(strucTemp);

        resourceFiles.addAll(resourceTemp);
        assessmentFiles.addAll(assessTemp);

        currentMode=tempMode;

    }


    @Override
    public void clearAndCreateCohortData(List<String> structureFilename, List<String> resourceFilename, List<String> assessmentFilename) throws IOException {
        structureGraph = null;
        cohortConceptGraphs=null;
        structureFiles.clear();
        resourceFiles.clear();
        assessmentFiles.clear();

        structureFiles.add(structureFilename.get(0));

        //resourceFiles.add(resourceFilename.get(0));


        //create the graph structure to be copied for each user
        ConceptGraphRecord structureRecord = ConceptGraphRecord.buildFromJson(structureFiles.get(0));

        List<LearningObjectLinkRecord> linkRecord = new ArrayList<>();
        for (String rFiles : resourceFilename){
            List<LearningObjectLinkRecord> temp = LearningObjectLinkRecord.buildListFromJson(rFiles);
            linkRecord.addAll(temp);
            resourceFiles.add(rFiles);
        }

        assessmentFiles.addAll(assessmentFilename);

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
        if (currentMode == Mode.COHORTGRAPH || currentMode== Mode.STRUCTUREGRAPHWITHASSESSMENT || currentMode== Mode.STRUCTUREGRAPHWITHRESOURCE){
            List<String> structList = new ArrayList<>();
            structList.addAll(currentStructure());

            clearAndCreateStructureData(structList);
        }else{
            throw new Exception("Wrong mode");
        }
    }

    @Override
    public void replaceCohortGraph(String graph) throws Exception{
        if(structureIsValid(graph)){

            List<String>  structure = new ArrayList<>();
            structure.add(graph);

            List<String>  resource = new ArrayList<>();
            resource.addAll(resourceFiles);

            List<String>  assessment = new ArrayList<>();
            assessment.addAll(assessmentFiles);


            clearAndCreateCohortData(structure,resource,assessment);

        }else{
            throw new Exception("Structure file invalid");
        }
    }

    @Override
    public void addResourceAndAssessment(String resource, String assignment) throws  Exception{
        if(currentMode == Mode.STRUCTUREGRAPH) {
            if(resourceIsValid(resource)){
                if(assessmentIsValid(assignment)){
                    List<String>  s = new ArrayList<>();
                    s.addAll(structureFiles);

                    List<String>  r = new ArrayList<>();
                    r.add(resource);

                    List<String>  a = new ArrayList<>();
                    a.add(assignment);

                    clearAndCreateCohortData(s,r,a);

                }else{
                    throw new Exception("Assessment file is invalid");
                }
            }else{
                throw new Exception("Resource file is invalid");
            }
        }else{
            throw new Exception("Wrong mode");
        }
    }

    @Override
    public void addResource(String secondResourceFile) throws Exception {

        if(currentMode== Mode.STRUCTUREGRAPH || currentMode== Mode.STRUCTUREGRAPHWITHRESOURCE){
            if(resourceIsValid(secondResourceFile)){
                resourceFiles.clear();
                resourceFiles.add(secondResourceFile);
                currentMode=Mode.STRUCTUREGRAPHWITHRESOURCE;
            }else{
                throw new Exception("Resource file invalid");
            }


        }else if(currentMode== Mode.COHORTGRAPH || currentMode == Mode.STRUCTUREGRAPHWITHASSESSMENT){
            if(resourceIsValid(secondResourceFile)){
                    List<String>  structure = new ArrayList<>();
                    structure.addAll(structureFiles);

                    List<String>  resource = new ArrayList<>();
                    resource.add(secondResourceFile);

                    List<String>  assessment = new ArrayList<>();
                    assessment.addAll(assessmentFiles);

                    clearAndCreateCohortData(structure, resource, assessment);
            }else{
                throw  new Exception("Resource file is invalid");
            }
        }else{
            throw new Exception("Wrong mode");
        }

    }

    @Override
    public void addAssessement(String secondAssessmentFilename) throws Exception {
        if(currentMode==Mode.COHORTGRAPH || currentMode==Mode.STRUCTUREGRAPHWITHRESOURCE) {
            if(assessmentIsValid(secondAssessmentFilename)){
                List<String>  structure = new ArrayList<>();
                structure.addAll(structureFiles);

                List<String>  resource = new ArrayList<>();
                resource.addAll(resourceFiles);

                List<String>  assessment = new ArrayList<>();
                assessment.addAll(assessmentFiles);
                assessment.add(secondAssessmentFilename);

                clearAndCreateCohortData(structure, resource, assessment);
            }else{
                throw new Exception("Assessment file is invalid");
            }

        } else if(currentMode == Mode.STRUCTUREGRAPHWITHASSESSMENT || currentMode== Mode.STRUCTUREGRAPH){
            if(assessmentIsValid(secondAssessmentFilename)){
                assessmentFiles.add(secondAssessmentFilename);

                if(currentMode==Mode.STRUCTUREGRAPH){
                    currentMode= Mode.STRUCTUREGRAPHWITHASSESSMENT;
                }

            }else{
                throw new Exception("Assessment file is invalid");
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
                    List<String>  structure = new ArrayList<>();
                    structure.addAll(structureFiles);

                    List<String>  resource = new ArrayList<>();
                    resource.addAll(resourceFiles);

                    List<String>  assessment = new ArrayList<>();
                    assessment.addAll(assessmentFiles);
                    assessment.remove(assessmentFile);

                    clearAndCreateCohortData(structure, resource, assessment);
                } else if (currentMode == Mode.STRUCTUREGRAPHWITHASSESSMENT) {
                    assessmentFiles.remove(assessmentFile);

                }
            }
        }
    }

    @Override
    public void replaceResourceFile(String resourceFile) throws Exception {
        if( currentMode==Mode.COHORTGRAPH || currentMode==Mode.STRUCTUREGRAPHWITHASSESSMENT){
            if(resourceIsValid(resourceFile)){
                resourceFiles.clear();
                resourceFiles.add(resourceFile);

                List<String>  structure = new ArrayList<>();
                structure.addAll(structureFiles);

                List<String>  resource = new ArrayList<>();
                resource.add(resourceFile);

                List<String>  assessment = new ArrayList<>();
                assessment.addAll(assessmentFiles);

                clearAndCreateCohortData(structure, resource, assessment);

            }else{
                throw new Exception("Can't find file");
            }
        }else if(currentMode==Mode.STRUCTUREGRAPH || currentMode== Mode.STRUCTUREGRAPHWITHRESOURCE){
            if(resourceIsValid(resourceFile)){
                List<String> resourceTemp = new ArrayList<>();
                resourceTemp.addAll(resourceFiles);
                resourceTemp.add(resourceFile);

                List<String> strucTemp = new ArrayList<>();
                strucTemp.addAll(structureFiles);
                clearAndCreateStructureData(strucTemp);
                resourceFiles.addAll(resourceTemp);

                currentMode = Mode.STRUCTUREGRAPHWITHRESOURCE;

            }else{
                throw new Exception("Can't find file");
            }
        }else{
            throw new Exception("Wrong mode");
        }
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

    public List<Group> calcSmallGroups(List<Suggester> groupTypeList, int groupSize) throws Exception {
        if(currentMode==Mode.COHORTGRAPH) {
            GroupSuggester sug = new GroupSuggester();
            List<Group> initialGroup = sug.getGroupList(this.cohortConceptGraphs);
            return sug.grouping(initialGroup, groupSize, groupTypeList);
        }else{
            throw new Exception("Wrong Mode");
        }

    }



    public String csvToResource() throws Exception {
        if(currentMode==Mode.STRUCTUREGRAPHWITHASSESSMENT) {
            String resourceFilename = "resourcesWithoutConceptConnections.json";
            String conceptIdsFilename = "conceptIdsForConnections.txt";
            csvToResource(assessmentFiles, OUTPUT_PATH + resourceFilename);
            conceptIdsToTextFile(structureGraph.getAllNodeIds(), OUTPUT_PATH+conceptIdsFilename);
            return "Resource files:  written:\n\t" + resourceFilename + "\n\t" + conceptIdsFilename + "\n in directory: " + OUTPUT_PATH;
        }
        else {
            throw new Exception("Wrong Mode");
        }
    }

    public static void csvToResource(List<String> assessmentFiles, String destinationFilepath) throws Exception{
            List<LearningObject> fullLoList = CSVReader.learningObjectsFromCSVList(assessmentFiles);
            List<LearningObjectLinkRecord> lolrList = LearningObjectLinkRecord.createLearningObjectLinkRecords(fullLoList, 10);
            LearningObjectLinkRecord.lolrToJSON(lolrList, destinationFilepath);
    }

    public static void conceptIdsToTextFile(Collection<String> conceptIds, String destinationFilepath) throws Exception{

        List<String> conceptsOut = new ArrayList<>();
        for (String concept : conceptIds){
            conceptsOut.add("\"" + concept + "\"");
        }

        Path path = Paths.get(destinationFilepath);
        Files.write(path, conceptsOut, StandardCharsets.UTF_8);
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
                double result = RFunctions.LearningObjectAvg(myMatrix, concept);
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
            return RFunctions.StudentKnowledgeEstAvg(myMatrix, user);
        }else{
            throw new NullPointerException();
        }
    }

    public void getFactorMatrix(){
        if(currentMode==Mode.COHORTGRAPH){
            ConceptGraph graph = cohortConceptGraphs.getAvgGraph();
            Map<String, LearningObject> loMap = graph.getLearningObjectMap();
            List<LearningObject> objList = new ArrayList<LearningObject>(loMap.values());
            KnowledgeEstimateMatrix myMatrix = new KnowledgeEstimateMatrix(objList);

            try {
                RFunctions.getFactorMatrix(myMatrix);
            }catch (Exception e){
                System.out.println("Insufficient data to perform factor analysis. Please refer to the guidelines of the data below:\n" +
                        "- Learning objects without any variance in scores between students will be ignored \n" +
                        "- There must be at least 3 valid learning objects present\n" +
                        "- There must be more students than learning objects\n");

            }
        }else{
            throw new NullPointerException();
        }

    }

    public void createConfirmatoryGraph(){
        if(currentMode==Mode.COHORTGRAPH){
            ConceptGraph graph = cohortConceptGraphs.getAvgGraph();
            Map<String, LearningObject> loMap = graph.getLearningObjectMap();
            List<LearningObject> objList = new ArrayList<LearningObject>(loMap.values());
//            KnowledgeEstimateMatrix myMatrix = new KnowledgeEstimateMatrix(objList);
            try {
//                RFunctions.confirmatoryGraph(myMatrix, cohortConceptGraphs);
            }catch (IndexOutOfBoundsException e){
                System.out.println("Insufficient data to perform task. Please refer to guidelines of the data below:\n" +
                        "- There must be more than 1 student\n" +
                        "- For each student there must be the same amount of responses as there are learning objects");
            }
        }else{
            throw new NullPointerException();
        }
    }


    @Override
    public List<String> currentAssessment(){
        List<String> temp = assessmentFiles;
        return temp;
    }

    @Override
    public List<String> currentResource(){
        List<String> temp = resourceFiles;
        return temp;
    }

    @Override
    public List<String> currentStructure(){
        List<String> temp = structureFiles;
        return temp;
    }



    @Override
    public boolean structureIsValid(String name) throws IOException {
        ConceptGraphRecord conceptGraph = ConceptGraphRecord.buildFromJson(name);
        if(conceptGraph.getConcepts().size()>0){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public boolean resourceIsValid(String name) throws IOException {
        List<LearningObjectLinkRecord> temp = LearningObjectLinkRecord.buildListFromJson(name);
        if(temp.size()>0){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public boolean assessmentIsValid(String name) throws IOException {
        CSVReader csvReader = new CSVReader(name);
        if (csvReader.getManualGradedResponses().size()>0){
            return true;
        }else{
            return false;
        }
    }

    public Mode getCurrentMode(){
        return currentMode;
    }

    public CohortConceptGraphs getCohortConceptGraphs(){
        return cohortConceptGraphs;
    }

    public ConceptGraph getStructureGraph(){
        return structureGraph;
    }

}
