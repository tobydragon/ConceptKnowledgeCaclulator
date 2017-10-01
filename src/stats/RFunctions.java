package stats;

import com.github.rcaller.rstuff.*;
import com.github.rcaller.util.Globals;
import edu.ithaca.dragonlab.ckc.conceptgraph.CohortConceptGraphs;
import edu.ithaca.dragonlab.ckc.conceptgraph.ConceptGraph;
import edu.ithaca.dragonlab.ckc.conceptgraph.ConceptNode;
import edu.ithaca.dragonlab.ckc.conceptgraph.KnowledgeEstimateMatrix;
import edu.ithaca.dragonlab.ckc.learningobject.LearningObject;

import java.io.File;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class holds R Functions that can be used on the RCode's rMatrix 2DArray from the KnowledgeEstimateMatrix class
 * in order to make statistical operations
 * Created by bleblanc2 on 6/14/17.
 */

public class RFunctions {


    /**
     * Using RCode, the average of knowledgeEstimates of a LearningObject across all user's is calculated
     * @param loMatrix the current graph being searched
     * @param lo the LearningObject that is selected to have an average taken from
     * @return an average of all knowledgeEstimates in a single LearningObject
     */
    public static double LearningObjectAvg(KnowledgeEstimateMatrix loMatrix, LearningObject lo){
        RCaller rCaller = RCallerVariable();

        int loIndex = loMatrix.getloIndex(lo);
        RCode code = loMatrix.getrMatrix();
        loIndex++;
        code.addInt("loIndex", loIndex);
        code.addRCode("classAvg <- mean(matrix[, loIndex])");
        rCaller.setRCode(code);
        rCaller.runAndReturnResult("classAvg");
        double[] results = rCaller.getParser().getAsDoubleArray("classAvg");
        double actual = results[0];
        return actual;

    }

    /**
     * Using RCode, a user's average of knowledgeEstimates across all LearningObjects is calculated
     * @param loMatrix the current graph being searched
     * @param user the user that is selected by the user to have an average taken from
     * @return an average of the student's knowledge estimates across all learning objects
     * within the graph in the form of a double
     */
    public static double StudentKnowledgeEstAvg(KnowledgeEstimateMatrix loMatrix, String user){
        RCaller rCaller = RCallerVariable();

        rCaller.redirectROutputToStream(System.out);
        List<String> userIdList = loMatrix.getUserIdList();

        int stuIndex = userIdList.indexOf(user);
        RCode code = loMatrix.getrMatrix();
        stuIndex++;
        code.addInt("stuIndex", stuIndex);
        //code.addRCode("stuIndex <- stuIndex + 1");
        //code.addRCode("print(matrix)");
        code.addRCode("stuAvgList <- rowMeans(matrix, na.rm = TRUE)");
        //code.addRCode("print(stuAvg)");
        code.addRCode("stuAvg <- stuAvgList[stuIndex]");
        rCaller.setRCode(code);
        rCaller.runAndReturnResult("stuAvg");
        double[] results = rCaller.getParser().getAsDoubleArray("stuAvg");
        double actual = results[0];
        return actual;
    }



    /**
     * A helper function for getFactorCount() that deletes columns of the matrix that will crash R/is unnecessary data
     * and finds the amount of columns that will be used in getFactorCount()
     * @param loMatrix
     * @return result the amount of columns valid for use in factor analysis
     */
    public static int getColumnCount(KnowledgeEstimateMatrix loMatrix){
        RCaller rCaller = RCallerVariable();

        RCode code = loMatrix.getrMatrix();
        int numOfFactors = 0;

        code.addInt("numOfFactors", numOfFactors);
        code.addRCode("columnCount <- ncol(matrix)");

        //delete any columns that have no variance in data
        code.addRCode("deleteVector = c()");
        code.addRCode("vectorIndex <- 0");
        code.addRCode("for(i in columnCount:1){" +
                "if(data[1,i] == mean(matrix[,i])){");
        code.addRCode("vectorIndex <- vectorIndex + 1");
        code.addRCode("deleteVector[vectorIndex] <- names(matrix[i])");
        code.addRCode("matrix <- matrix[,-c(i)]" +
                "}" +
                "}");
        code.addRCode("if(vectorIndex > 0){" +
                "print(\"The LearningObjects directly below were removed due to 0 variance throughout all responses:\")");
        code.addRCode("print(deleteVector)");
        code.addRCode("}");

        code.addRCode("columnCount <- ncol(matrix)");
        rCaller.setRCode(code);
        rCaller.runAndReturnResult("columnCount");
        int[] result = rCaller.getParser().getAsIntArray("columnCount");
        return result[0];
    }

    /**
     * A helper function for getFactorMatrix() that finds the best number of factors to use in factor analysis
     * @param loMatrix KnowledgeEstimateMatrix
     * @return result the number of factors to be used in factor analysis on the current matrix
     * @throws Exception
     */
    public static int findFactorCount(KnowledgeEstimateMatrix loMatrix)throws Exception{
        RCaller rCaller = RCallerVariable();

        RCode code = loMatrix.getrMatrix();
        int numOfFactors = 0;

        code.addInt("numOfFactors", numOfFactors);
        int columnCount = getColumnCount(loMatrix);
        if(columnCount > 2) {
            code.addInt("columnCount", columnCount);

            //sets a limit on how high R will go to find the factor amount based on how many columns exist
            code.addRCode("if(columnCount %% 2 == 0){" +
                    "upperlimit <- (columnCount/2) - 1" +
                    "}else{" +
                    "upperlimit <- (columnCount/2) - 0.5" +
                    "}");

            //put all p-values of the factor analysis into a list
            //run through the list and take the factor number with the first p-value above .05
            code.addRCode("vector = c()");
            code.addRCode("for(i in 1:upperlimit){" +
                    "vector[i] <- factanal(matrix, i, method = 'mle')$PVAL" +
                    "}");
            code.addRCode("for(i in 1:upperlimit){" +
                    "if(vector[i] < .05){" +
                    "numOfFactors <- i + 1" +
                    "}" +
                    "}");
            rCaller.setRCode(code);
            rCaller.runAndReturnResult("numOfFactors");
            int[] result = rCaller.getParser().getAsIntArray("numOfFactors");
            return result[0];
        }else{
            throw new Exception();
        }
    }

    /**
     * Prints out a factor matrix connecting learning objects to similar higher up objects
     * @param loMatrix KnowledgeEstimateMatrix object will use the RMatrix data member
     * @pre resource, assessment, structure files are all present and an R Matrix is created
     * @throws Exception
     */
    public static void getFactorMatrix(KnowledgeEstimateMatrix loMatrix)throws Exception {
        try{
        int numOfFactors = findFactorCount(loMatrix);
        RCaller rCaller = RCallerVariable();

        rCaller.redirectROutputToStream(System.out);

        RCode code = loMatrix.getrMatrix();
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
        Logger.getLogger(RFunctions.class.getName()).log(Level.SEVERE, e.getMessage());
    }
    }

    /**
     * creates a matrix of factors in java (factors=rows, LearningObjects=columns)
     * @param loMatrix
     * @return statsMatrix the matrix of factors
     * @pre resource, assessment, structure files are all present and an R Matrix is created
     * @throws Exception
     */
    public static double[][] returnFactorMatrix(KnowledgeEstimateMatrix loMatrix)throws Exception {
        int learningObjectCount = getColumnCount(loMatrix);
        int numOfFactors = findFactorCount(loMatrix);
        RCaller rCaller = RCallerVariable();

        rCaller.redirectROutputToStream(System.out);

        RCode code = loMatrix.getrMatrix();
        code.addInt("numOfFactors", numOfFactors);
        code.addRCode("matrixOfLoadings <- factanal(matrix, factors = numOfFactors, method = 'mle')");

        code.addRCode("factorsMatrix <- matrixOfLoadings$loadings");
        rCaller.setRCode(code);
        rCaller.runAndReturnResult("factorsMatrix");
        double[][] factorMatrix = rCaller.getParser().getAsDoubleMatrix("factorsMatrix");

        double newArray[] = new double[factorMatrix.length*factorMatrix[0].length];
        for(int i = 0; i < factorMatrix.length; i++) {
            double[] row = factorMatrix[i];
            for(int j = 0; j < row.length; j++) {
                double number = factorMatrix[i][j];
                newArray[i*row.length+j] = number;
            }
        }

        int arrayIndex = 0;
        double[][] statsMatrix = new double[learningObjectCount][numOfFactors];
        for(int i = 0; i < numOfFactors; i++){
            for(int j = 0; j <learningObjectCount; j++){
                statsMatrix[j][i] = newArray[arrayIndex];
                arrayIndex++;
            }
        }

        return statsMatrix;
    }


    public static Collection<String> duplicateCheck(ConceptGraph graph, Collection<String> conceptStringList){
        List<String> removalList = new ArrayList<String>();
        Collection<String> newStringList = new ArrayList<String>();
        //copy the collection of concept strings into the new collection
        for(String curString : conceptStringList){
            newStringList.add(curString);
        }
        //Compare each concept string with a concept string
        for(String curString : conceptStringList){
            for(String otherString : newStringList){

                //If the secondary concept list's concept is not already in the removal list and the list of LOs in
                //the concept list are identical but the names of the concepts are different, add concept to removal list
                if(!removalList.contains(curString) && !removalList.contains(otherString) && curString != otherString){
                    ConceptNode curNode = graph.findNodeById(curString);
                    ConceptNode otherNode = graph.findNodeById(otherString);
                    if(curNode.getLearningObjectMap().equals(otherNode.getLearningObjectMap())){
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

    public static String modelMaker(CohortConceptGraphs ccg){
        String modelString = "";
        ConceptGraph graph = ccg.getAvgGraph();
        Collection<String> conceptStringList = graph.getAllNodeIds();
        conceptStringList = duplicateCheck(graph, conceptStringList);

        for(String conceptString : conceptStringList){
            ConceptNode concept = graph.findNodeById(conceptString);
            Map<String, LearningObject> loMap = concept.getLearningObjectMap();
            Collection<LearningObject> loList = loMap.values();
            for(LearningObject lo : loList){



                //modelString += conceptString + " -> " + lo.getId() + ", " + lo.getId() + "To" + conceptString + ", NA \n";
                modelString += conceptString + " -> " + lo.getId().replaceAll("\\s","") + ", " + lo.getId().replaceAll("\\s","") + "To" + conceptString + ", NA \n";

            }
        }

        modelString = modelString.replaceAll(":", "");
        modelString = modelString.replaceAll("\\.", "");
        return modelString;
    }

    public static void confirmatoryGraph(KnowledgeEstimateMatrix loMatrix, CohortConceptGraphs ccg){
        int matrixSize = loMatrix.getStudentKnowledgeEstimates().length;
        //if((matrixSize/loMatrix.getObjList().size()) > 1) {
            try {
                String modelString = modelMaker(ccg);
                RCaller rCaller = RCallerVariable();
                RCode code = loMatrix.getrMatrix();
                code.addRCode("library(sem)");
                code.addRCode("library(semPlot)");
                code.addRCode("data.dhp <- specifyModel(text=\"" + modelString + "\")");
                code.addRCode("dataCorrelation <- cor(matrix)");
                code.addRCode("rowCount <- nrow(matrix)");
                code.addRCode("dataSem.dhp <- sem(data.dhp, dataCorrelation, rowCount)");
                File file = code.startPlot();
                code.addRCode("semPaths(dataSem.dhp, \"est\")");
                code.endPlot();
                rCaller.setRCode(code);
                rCaller.runOnly();
                code.getPlot(file);
                code.showPlot(file);
            } catch (Exception e) {
                Logger.getLogger(RFunctions.class.getName()).log(Level.SEVERE, e.getMessage());
                throw new IndexOutOfBoundsException();
            }
        //}else{
          //  throw new IndexOutOfBoundsException();
        //}
    }



    /**
     * Must be called at the start of every function that uses RCaller methods in
     * order to properly run RCaller methods. The if statement checks whether the machine
     * running the function is Windows or Mac and sets up RCaller accordingly.
     * @return an RCaller that works on whichever machine is running to use RCaller methods
     */
    public static RCaller RCallerVariable(){
        if(Globals.isWindows() == false) {
            RCallerOptions options = RCallerOptions.create("/usr/local/Cellar/r/3.4.2/bin/Rscript", Globals.R_current, FailurePolicy.RETRY_5, Long.MAX_VALUE, 100, RProcessStartUpOptions.create());
            return RCaller.create(options);
        }else {
            return RCaller.create();
        }
    }
}
