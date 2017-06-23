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

public class BasicRFunctions {


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


        List<String> userIdList = loMatrix.getUserIdList();

        int stuIndex = userIdList.indexOf(user);
        RCode code = loMatrix.getrMatrix();
        stuIndex++;
        code.addInt("stuIndex", stuIndex);
        //code.addRCode("flippedMatrix <- t(matrix)");
        //code.addRCode("stuAvg <- mean(matrix[stuIndex, 1:14], na.rm=TRUE)");
        //code.addRCode("stuAvg <- mean(newData)");
        code.addRCode("stuAvg <- mean(matrix[stuIndex, ])");
        rCaller.setRCode(code);
        rCaller.runAndReturnResult("stuAvg");
        double[] results = rCaller.getParser().getAsDoubleArray("stuAvg");
        double actual = results[0];
        return actual;
    }

    /**
     * Must be called at the start of every function that uses RCaller methods in
     * order to properly run RCaller methods. The if statement checks whether the machine
     * running the function is Windows or Mac and sets up RCaller accordingly.
     * @pre called at some point before RCode is set and returned
     * @return an RCaller that works on whichever machine is running
     */

    public static RCaller RCallerVariable(){
        RCaller rCaller;
        if(Globals.isWindows() == false) {
            RCallerOptions options = RCallerOptions.create("/usr/local/Cellar/r/3.4.0_1/bin/Rscript", Globals.R_current, FailurePolicy.RETRY_5, Long.MAX_VALUE, 100, RProcessStartUpOptions.create());
            rCaller = RCaller.create(options);
        }else {
            rCaller  = RCaller.create();
        }
        return rCaller;
    }


}
