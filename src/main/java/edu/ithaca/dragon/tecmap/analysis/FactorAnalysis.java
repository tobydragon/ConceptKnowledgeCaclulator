package edu.ithaca.dragon.tecmap.analysis;

import com.github.rcaller.rstuff.*;
import com.github.rcaller.util.Globals;
import edu.ithaca.dragon.tecmap.Settings;
import edu.ithaca.dragon.tecmap.conceptgraph.CohortConceptGraphs;
import edu.ithaca.dragon.tecmap.conceptgraph.ConceptGraph;
import edu.ithaca.dragon.tecmap.conceptgraph.ConceptNode;
import edu.ithaca.dragon.tecmap.conceptgraph.eval.RFunctions;
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



    /**
     * Prints out a factor matrix connecting learning objects to similar higher up objects
     * @param assessmentMatrix KnowledgeEstimateMatrix object will use the RMatrix data member
     * @pre resource, assessment, structure files are all present and an R Matrix is created
     * @throws Exception
     */

    public void displayExploratoryGraph(ContinuousMatrixRecord assessmentMatrix)throws Exception {
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
    }

    public ContinuousMatrixRecord ExploratoryMatrix(ContinuousMatrixRecord assessmentMatrix)throws Exception{
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

    public static ContinuousMatrixRecord calculateExploratoryMatrix(ContinuousMatrixRecord assessmentMatrix)throws Exception{
        RCaller rCaller = RLibrary.RCallerVariable();
        rCaller.redirectROutputToStream(System.out);

        RCode code = RLibrary.createRMatrix(assessmentMatrix);
        double[][] gradeMatrix = assessmentMatrix.getDataMatrix();
        code.addRCode("source('/Users/bleblanc2/IdeaProjects/tecmap/src/main/r/ExploratoryMatrix.R')");
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

        ContinuousMatrixRecord factorMatrixRecord = new ContinuousMatrixRecord(factorMatrix, factorList, assessmentMatrix.getAssessmentItems());

        return factorMatrixRecord;
    }



    public static Collection<String> duplicateCheck(ConceptGraph graph, Collection<String> conceptStringList){
        List<String> removalList = new ArrayList<String>();
        Collection<String> newStringList = new ArrayList<String>();
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

    public static String modelMaker(ConceptGraph acg){
        String modelString = "";
        Collection<String> conceptStringList = acg.getAllNodeIds();
        conceptStringList = duplicateCheck(acg, conceptStringList);

        for(String conceptString : conceptStringList){
            int conceptIndex = 1;
            ConceptNode concept = acg.findNodeById(conceptString);
            Map<String, AssessmentItem> loMap = concept.getAssessmentItemMap();
            Collection<AssessmentItem> loList = loMap.values();
            int loListSize = loList.size();
            int loListIndex = 1;
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


    public static void modelToFile(ConceptGraph acg){
        String modelString = modelMaker(acg);
        File file = new File("/Users/bleblanc2/IdeaProjects/tecmap/src/test/resources/model/model.txt");
        try{

              // creates the file
              file.createNewFile();

              // creates a FileWriter Object
              FileWriter writer = new FileWriter(file);

              // Writes the content to the file
              writer.write(modelString);
              writer.flush();
              writer.close();
            System.out.println("File successfully created. File: /Users/bleblanc2/IdeaProjects/tecmap/src/test/resources/model/model.txt");
        }catch (IOException e){
            e.printStackTrace();
            System.out.println("Error occurred in exporting data model to file.");
        }
    }

    public void displayConfirmatoryGraph(ContinuousMatrixRecord assessmentMatrix, ConceptGraph acg){
            try {
                modelToFile(acg);

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
            } catch (Exception e) {
                Logger.getLogger(FactorAnalysis.class.getName()).log(Level.SEVERE, e.getMessage());
                throw new IndexOutOfBoundsException();
            }
    }

    //TODO: dataSem.dhp$A returns the values wanted but not in necessarily correct format. Also, many 0s are present.
    public static ContinuousMatrixRecord calculateConfirmatoryMatrix(ConceptGraph acg){
        try {
            Map<String, AssessmentItem> assessmentItemMap = acg.getAssessmentItemMap();
            List<AssessmentItem> assessmentItems = new ArrayList<>(assessmentItemMap.values());
            ContinuousMatrixRecord assessmentMatrix = new ContinuousMatrixRecord(assessmentItems);

            modelToFile(acg);
            String modelFilePath = "/Users/bleblanc2/IdeaProjects/tecmap/src/test/resources/model/model.txt";
            RCaller rCaller = RLibrary.RCallerVariable();
            RCode code = RLibrary.createRMatrix(assessmentMatrix);
            double[][] gradeMatrix = assessmentMatrix.getDataMatrix();
            code.addString("modelFile", modelFilePath);
            code.addRCode("source('/Users/bleblanc2/IdeaProjects/tecmap/src/main/r/ExploratoryMatrix.R')");
            code.addDoubleMatrix("data", gradeMatrix);
            //code.addRCode("data <- as.data.frame(t(data))");
            code.addRCode("factorMatrix <- calculateConfirmatoryMatrix(data, modelFile)");
            rCaller.setRCode(code);
            rCaller.runAndReturnResult("factorMatrix");
            double[][] confirmatoryMatrix = rCaller.getParser().getAsDoubleMatrix("factorMatrix");

            //The matrix has # of columns and rows equal to the sum of number of factors and number of assessments
            //Most of the data is irrelevant and is a 0. This loop makes a smaller loop that is just assessments by factors
            List<Integer> indicesToAdd = new ArrayList<>();
            for(int i=0;i<confirmatoryMatrix.length; i++){
                for(int j=0;j<confirmatoryMatrix[0].length; j++){
                    if(confirmatoryMatrix[i][j] != 0){
                        if(false == indicesToAdd.contains(i)){
                            indicesToAdd.add(i);
                        }

                    }
                }
            }
            double[][] cleanedMatrix = new double[confirmatoryMatrix[0].length][indicesToAdd.size()];
            int newIndex = 0;
            for(int rowIndex :indicesToAdd){
                for (int j = 0; j < confirmatoryMatrix[0].length; j++) {
                    cleanedMatrix[j][newIndex] = confirmatoryMatrix[rowIndex][j];
                }
                newIndex++;
            }
            List<String> factorList = new ArrayList<String>();
            Collection<String> nodeCollection = acg.getAllNodeIds();
            for(String node : nodeCollection){
                factorList.add(node);
            }
            ContinuousMatrixRecord confirmatoryMatrixRecord = new ContinuousMatrixRecord(cleanedMatrix, factorList, assessmentMatrix.getAssessmentItems());
            return confirmatoryMatrixRecord;
        } catch (Exception e) {
            Logger.getLogger(FactorAnalysis.class.getName()).log(Level.SEVERE, e.getMessage());
            throw new IndexOutOfBoundsException();
        }
    }


    public static class NoVarianceException extends Exception {
        public NoVarianceException() { super(); }
        public NoVarianceException(String message) { super(message); }
        public NoVarianceException(String message, Throwable cause) { super(message, cause); }
        public NoVarianceException(Throwable cause) { super(cause); }
    }
}
