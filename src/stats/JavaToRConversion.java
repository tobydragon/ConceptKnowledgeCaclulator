package stats;

import com.github.rcaller.rstuff.*;
import com.github.rcaller.util.Globals;
import edu.ithaca.dragonlab.ckc.conceptgraph.Matrix;
import edu.ithaca.dragonlab.ckc.learningobject.LearningObject;

import java.util.ArrayList;

/**
 * Created by bleblanc2 on 6/16/17.
 */
public class JavaToRConversion {

    RCaller rCaller;

    public JavaToRConversion(){
        this.rCaller = Windows();
    }



    public static RCaller Windows(){
        RCaller rCaller;
        if(Globals.isWindows() == false) {
            RCallerOptions options = RCallerOptions.create("/usr/local/Cellar/r/3.4.0_1/bin/Rscript", Globals.R_current, FailurePolicy.RETRY_5, Long.MAX_VALUE, 100, RProcessStartUpOptions.create());
            rCaller = RCaller.create(options);
        }else {
            rCaller  = RCaller.create();
        }
        return rCaller;
    }

    public static RCode JavaToR(Matrix loMatrix){
        double[][] aMatrix = loMatrix.getStructure();
        String[] users = loMatrix.getUserIdList();
        ArrayList<LearningObject> learningObjects = loMatrix.getObjList();
        String[] objStr = new String[learningObjects.size()];

        //object list into single string
        int i = 0;
        for(LearningObject obj: learningObjects){
            objStr[i] = obj.toString();
            i++;
        }
        String objCodeString = "";
        for(int j=0; j<objStr.length; j++){
            objCodeString = objCodeString + "'" + objStr[j] + "'";
            if(j != objStr.length-1){
                objCodeString = objCodeString + ",";
            }
        }

        //user list into single string
        String userCodeString = "";
        for(int j=0; j<users.length; j++){
            userCodeString = userCodeString + "'" + objStr[j] + "'";
            if(j != users.length-1){
                userCodeString = userCodeString + ",";
            }
        }


        RCode code = RCode.create();
        code.addDoubleMatrix("matrix", aMatrix);
        code.addStringArray("headers", objStr);
        code.addStringArray("users", users);
        int userlength = users.length;
        int objlength = objStr.length;
        code.addInt("usercount", userlength);
        code.addInt("objcount", objlength);
        code.addRCode("provideDimnames(matrix)");
        code.addRCode("colnames(matrix) <- letters[1:3]");
        /**
        code.addRCode("for(i in 1:objcount){" +
                "colnames(matrix)[i] <- headers[i]" +
                "}");
        code.addRCode("for(i in 1:usercount){" +
                "rownames(matrix)[i] <- users[i]" +
                "}");

        //code.addRCode("colnames(matrix) <- c(headers)");
        //code.addRCode("rownames(matrix) <- c(users)");
*/
        return code;


    }
}
