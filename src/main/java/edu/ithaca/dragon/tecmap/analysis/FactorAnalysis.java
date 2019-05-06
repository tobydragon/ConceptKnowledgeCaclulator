package edu.ithaca.dragon.tecmap.analysis;

import com.github.rcaller.rstuff.RCaller;
import com.github.rcaller.rstuff.RCode;
import edu.ithaca.dragon.tecmap.conceptgraph.ConceptGraph;
import edu.ithaca.dragon.tecmap.conceptgraph.ConceptNode;
import edu.ithaca.dragon.tecmap.io.record.ContinuousMatrixRecord;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItem;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class holds R Functions that can be used on the RCode's rMatrix 2DArray from the KnowledgeEstimateMatrix class
 * in order to make statistical operations
 * Created by bleblanc2 on 6/14/17.
 */

public class FactorAnalysis implements FactorAnalysisAPI{


    public static String modelFilePath = "src/main/resources/confirmatoryModelFiles/autoCreated/model.txt";

    //TODO: make absolute paths variables set up as class variables


    /**
     * Prints out a factor matrix connecting learning objects to similar higher up objects
     * @param assessmentMatrix KnowledgeEstimateMatrix object will use the RMatrix data member
     * @pre resource, assessment, structure files are all present and an R Matrix is created
     * @throws Exception
     */

    public void exploratoryGraph(ContinuousMatrixRecord assessmentMatrix)throws Exception{
        displayExploratoryGraph(assessmentMatrix);
    }

    //TODO: Get working in ExploratoryMatrix.R
    public static void displayExploratoryGraph(ContinuousMatrixRecord assessmentMatrix)throws Exception {
        /*
        try{


        int numOfFactors = findFactorCount(assessmentMatrix);
        RCaller rCaller = RLibrary.RCallerVariable();

        rCaller.redirectROutputToStream(System.out);

        RCode code = RLibrary.createRMatrix(assessmentMatrix);
        code.addInt("numOfFactors", numOfFactors);
        code.addRCode("matrixOfLoadings <- factanal(matrix,numOfFactors, method=\"MLE\")");
        //rCaller.getRCallerOptions();
        code.addRCode("factorsMatrix <- matrixOfLoadings$loadings");
        code.addRCode("print(factorsMatrix)");
        //code.addRCode("require(semPlot)");
        code.addRCode("library(semPlot)");
        File file = code.startPlot();
        System.out.println("Ignore warning messages below");
        code.addRCode("semPaths(matrixOfLoadings, \"est\")");
        code.endPlot();
        rCaller.setRCode(code);
        rCaller.runOnly();
        //double[][] result = rCaller.getParser().getAsDoubleMatrix("factorsMatrix");
        code.getPlot(file);
        code.showPlot(file);
    }catch(Exception e){
        Logger.getLogger(FactorAnalysis.class.getName()).log(Level.SEVERE, e.getMessage());
    }
    */

        try {
            List<AssessmentItem> assessments = assessmentMatrix.getAssessmentItems();
            int assessmentIterator = 0;
            String[] assessmentArr = new String[assessments.size()];
            for(AssessmentItem assessment : assessments){
                assessmentArr[assessmentIterator] = assessment.getId();
                assessmentIterator++;
            }

            List<String> students = assessmentMatrix.getRowIds();
            String[] studentArr= new String[students.size()];
            int studentIterator = 0;
            for(String student : students){
                studentArr[studentIterator] = student;
                studentIterator++;
            }

            RCaller rCaller = RLibrary.RCallerVariable();
            rCaller.redirectROutputToStream(System.out);
            RCode code = RLibrary.createRMatrix(assessmentMatrix);
            double[][] gradeMatrix = assessmentMatrix.getDataMatrix();
            //Call file
            File file = code.startPlot();
            code.addRCode("source('" + RSettings.getRCodeRoot() + "ExploratoryMatrix.R')");
            code.addStringArray("students", studentArr);
            code.addDoubleMatrix("data", gradeMatrix);
            //code.addRCode("data <- as.data.frame(t(data))");
            code.addRCode("factorMatrix <- calculateExploratoryMatrix(data)");
            rCaller.setRCode(code);
            rCaller.runOnly();
            //rCaller.runAndReturnResult("factorMatrix");
            //double[][] factorMatrix = rCaller.getParser().getAsDoubleMatrix("factorMatrix");
            code.getPlot(file);
            code.showPlot(file);
        }catch(Exception e){
            Logger.getLogger(FactorAnalysis.class.getName()).log(Level.SEVERE, e.getMessage());
        }


    }

    public ContinuousMatrixRecord exploratoryMatrix(ContinuousMatrixRecord assessmentMatrix)throws Exception{
        ContinuousMatrixRecord factorMatrix = calculateExploratoryMatrix(assessmentMatrix);
        return factorMatrix;
    }

    /**
     * creates a matrix of factors in java (factors=rows, LearningObjects=columns)
     * @param assessmentMatrix
     * @return statsMatrix the matrix of strengths between a factor and AssessmentItem
     * @pre resource, assessment, structure files are all present and an R Matrix is created
     * @throws Exception
     */

    public static ContinuousMatrixRecord calculateExploratoryMatrix(ContinuousMatrixRecord assessmentMatrix){
        RCaller rCaller = RLibrary.RCallerVariable();
        rCaller.redirectROutputToStream(System.out);
        RCode code = RLibrary.createRMatrix(assessmentMatrix);
        double[][] gradeMatrix = assessmentMatrix.getDataMatrix();
        //Call file
        code.addRCode("source('"+ RSettings.getRCodeRoot() + "ExploratoryMatrix.R')");
        code.addDoubleMatrix("data", gradeMatrix);
        //code.addRCode("data <- as.data.frame(t(data))");
        code.addRCode("factorMatrix <- calculateExploratoryMatrix(data)");
        rCaller.setRCode(code);
        rCaller.runAndReturnResult("factorMatrix");
        double[][] factorMatrix = rCaller.getParser().getAsDoubleMatrix("factorMatrix");

        //Create List of factors for being placed into the columns
        int numOfFactors = factorMatrix[0].length;
        List<String> factorList = new ArrayList<>();
        for(int i=0; i<numOfFactors; i++){
            factorList.add("Factor "+(i+1));
        }
        double[][] transposedFactorMatrix = MatrixTransforms.transposeMatrix(factorMatrix);
        ContinuousMatrixRecord factorMatrixRecord = new ContinuousMatrixRecord(transposedFactorMatrix, factorList, assessmentMatrix.getAssessmentItems());
        return factorMatrixRecord;
    }



    public static List<String> duplicateCheck(ConceptGraph graph, List<String> conceptStringList){
        List<String> removalList = new ArrayList<String>();
        List<String> newStringList = new ArrayList<String>();
        //copy the collection of Concept strings into the new collection
        for(String curString : conceptStringList){
            newStringList.add(curString);
        }
        //Compare each Concept string with a Concept string
        for(String curString : conceptStringList){
            for(String otherString : newStringList){

                //If the secondary Concept list's Concept is not already in the removal list and the list of LOs in
                //the Concept list are identical but the names of the concepts are different, add Concept to removal list
                if(!removalList.contains(curString) && !removalList.contains(otherString) && curString != otherString){
                    ConceptNode curNode = graph.findNodeById(curString);
                    ConceptNode otherNode = graph.findNodeById(otherString);
                    if(curNode.getAssessmentItemMap().equals(otherNode.getAssessmentItemMap())){
                        removalList.add(curString);
                    }
                }
            }
        }
        for(String string : removalList){
            newStringList.remove(string);

        }
        return newStringList;
    }

    public static String modelMaker(ConceptGraph acg, List<String> nodeList){
        String modelString = "";
        List<String> conceptStringList = duplicateCheck(acg, nodeList);

        for(String conceptString : conceptStringList){
            int conceptIndex = 1;
            ConceptNode concept = acg.findNodeById(conceptString);
            Map<String, AssessmentItem> loMap = concept.getAssessmentItemMap();
            Collection<AssessmentItem> loList = loMap.values();

            for(AssessmentItem lo : loList){
                //modelString += conceptString + " -> " + lo.getLearningResourceId() + ", " + lo.getLearningResourceId() + "To" + conceptString + ", NA \n";
                modelString += conceptString + " -> " + lo.getId().replaceAll("\\s", "") + ", " + lo.getId().replaceAll("\\s", "") + "To" + conceptString + ", NA \n";

            }
        }
        //remove the last new line (\n) from the modelString
        for(int i=0;i<2;i++) {
            modelString = modelString.substring(0, modelString.length() - 1);
        }

        modelString = modelString.replaceAll(":", "");
        modelString = modelString.replaceAll("\\.", "");
        return modelString;
    }


    public static void modelToFile(String modelString) throws IOException {
        File file = new File(modelFilePath);
        // creates a FileWriter Object
        FileWriter writer = new FileWriter(file);

        // Writes the content to the file
        writer.write(modelString);
        writer.flush();
        writer.close();
    }

//TODO: Get working in ExploratoryMatrix.R
    public void displayConfirmatoryGraph(ContinuousMatrixRecord assessmentMatrix, ConceptGraph acg) throws IOException {
            List<String> factorList = new ArrayList<String>();
            Collection<String> nodeCollection = acg.getAllNodeIds();
            for(String node : nodeCollection){
                factorList.add(node);
            }

            String modelString = modelMaker(acg, factorList);
            modelToFile(modelString);

            RCaller rCaller = RLibrary.RCallerVariable();
            RCode code = RLibrary.createRMatrix(assessmentMatrix);
            code.addRCode("library(sem)");
            code.addRCode("library(semPlot)");
            code.addRCode("library(stringr)");
            code.addRCode("library(readr)");

            code.addRCode("model.txt <- readLines('resources/stats/model.txt')");
            code.addRCode("data.dhp <- specifyModel(text=model.txt)");
            code.addRCode("dataCorrelation <- cor(matrix)");
            code.addRCode("rowCount <- nrow(matrix)");
            code.addRCode("dataSem.dhp <- sem(data.dhp, dataCorrelation, rowCount)");
            rCaller.redirectROutputToStream(System.out);
            code.addRCode("dataSem.dhp");
            File file = code.startPlot();
            code.addRCode("semPaths(dataSem.dhp, \"est\")");
            code.endPlot();
            rCaller.setRCode(code);
            rCaller.runOnly();
            code.getPlot(file);
            code.showPlot(file);
    }

    public ContinuousMatrixRecord confirmatoryMatrix(ConceptGraph acg)throws Exception{
        ContinuousMatrixRecord factorMatrix = calculateConfirmatoryMatrix(acg);
        return factorMatrix;
    }

    public static ContinuousMatrixRecord calculateConfirmatoryMatrix(ConceptGraph acg) throws IOException{
        Map<String, AssessmentItem> assessmentItemMap = acg.getAssessmentItemMap();
        List<AssessmentItem> assessmentItems = new ArrayList<>(assessmentItemMap.values());
        ContinuousMatrixRecord assessmentMatrix = new ContinuousMatrixRecord(assessmentItems);

        List<String> factorList = new ArrayList<String>();
        Collection<String> nodeCollection = acg.getAllNodeIds();
        for(String node : nodeCollection){
            factorList.add(node);
        }

        String modelString = modelMaker(acg, factorList);
        modelToFile(modelString);

        RCaller rCaller = RLibrary.RCallerVariable();
        RCode code = RLibrary.createRMatrix(assessmentMatrix);
        double[][] gradeMatrix = assessmentMatrix.getDataMatrix();
        code.addString("modelFile", modelFilePath);
        code.addRCode("source('" + RSettings.getRCodeRoot() + "ExploratoryMatrix.R')");
        code.addDoubleMatrix("data", gradeMatrix);
        //code.addRCode("data <- as.data.frame(t(data))");
        code.addRCode("factorMatrix <- calculateConfirmatoryMatrix(data, modelFile)");
        rCaller.setRCode(code);
        rCaller.runAndReturnResult("factorMatrix");
        double[][] confirmatoryMatrix = rCaller.getParser().getAsDoubleMatrix("factorMatrix");

        //The matrix has # of columns and rows equal to the sum of number of factors and number of assessments
        //Most of the data is irrelevant and is a 0. This loop makes a smaller loop that is just assessments by factors
        double[][] removedMatrix = MatrixTransforms.removeEmptyRowsFromMatrix(confirmatoryMatrix);
        //double[][] cleanedMatrix = MatrixTransforms.transposeMatrix(removedMatrix);

        ContinuousMatrixRecord confirmatoryMatrixRecord = new ContinuousMatrixRecord(removedMatrix, factorList, assessmentMatrix.getAssessmentItems());
        return confirmatoryMatrixRecord;

    }


}

