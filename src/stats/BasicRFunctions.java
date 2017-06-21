package stats;

import com.github.rcaller.rstuff.*;
import com.github.rcaller.util.Globals;
import edu.ithaca.dragonlab.ckc.conceptgraph.KnowledgeEstimateMatrix;
import edu.ithaca.dragonlab.ckc.learningobject.LearningObject;

/**
 * Created by bleblanc2 on 6/14/17.
 */

public class BasicRFunctions {


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

    public static double StudentKnowledgeEstAvg(KnowledgeEstimateMatrix loMatrix, LearningObject lo){
        RCaller rCaller = RCallerVariable();

        int stuIndex = loMatrix.getloIndex(lo);
        RCode code = loMatrix.getrMatrix();
        stuIndex++;
        code.addInt("stuIndex", stuIndex);
        code.addRCode("stuAvg <- mean(matrix[stuIndex, ])");
        rCaller.setRCode(code);
        rCaller.runAndReturnResult("stuAvg");
        double[] results = rCaller.getParser().getAsDoubleArray("stuAvg");
        double actual = results[0];
        return actual;
    }

    //Must be called at the start of every function that uses RCaller methods
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
