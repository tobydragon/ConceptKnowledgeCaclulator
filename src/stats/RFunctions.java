package stats;

import com.github.rcaller.rstuff.*;
import com.github.rcaller.util.Globals;
import edu.ithaca.dragonlab.ckc.conceptgraph.KnowledgeEstimateMatrix;
import edu.ithaca.dragonlab.ckc.learningobject.LearningObject;

import java.util.List;

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

    public static int findFactorCount(KnowledgeEstimateMatrix loMatrix){
        RCaller rCaller = RCallerVariable();

        RCode code = loMatrix.getrMatrix();
        int numOfFactors = 0;

        code.addInt("numOfFactors", numOfFactors);
        int columnCount = getColumnCount(loMatrix);
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
    }

    public static void getFactorMatrix(KnowledgeEstimateMatrix loMatrix){
        int numOfFactors = findFactorCount(loMatrix);
        RCaller rCaller = RCallerVariable();

        rCaller.redirectROutputToStream(System.out);

        RCode code = loMatrix.getrMatrix();
        code.addInt("numOfFactors", numOfFactors);
        code.addRCode("matrixOfLoadings <- factanal(matrix, factors = numOfFactors, method = 'mle')");

        rCaller.getRCallerOptions();
        code.addRCode("factorsMatrix <- matrixOfLoadings$loadings");
        code.addRCode("print(factorsMatrix)");
        //code.addRCode("factorsMatrix <- t(factorsMatrix)");
        rCaller.setRCode(code);
        rCaller.runAndReturnResult("factorsMatrix");
        double[][] result = rCaller.getParser().getAsDoubleMatrix("factorsMatrix");

    }




    /**
     * Must be called at the start of every function that uses RCaller methods in
     * order to properly run RCaller methods. The if statement checks whether the machine
     * running the function is Windows or Mac and sets up RCaller accordingly.
     * @return an RCaller that works on whichever machine is running to use RCaller methods
     */

    public static RCaller RCallerVariable(){
        if(Globals.isWindows() == false) {
            RCallerOptions options = RCallerOptions.create("/usr/local/Cellar/r/3.4.0_1/bin/Rscript", Globals.R_current, FailurePolicy.RETRY_5, Long.MAX_VALUE, 100, RProcessStartUpOptions.create());
            return RCaller.create(options);
        }else {
            return RCaller.create();
        }
    }
}