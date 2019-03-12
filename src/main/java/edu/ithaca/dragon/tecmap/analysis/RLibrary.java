package edu.ithaca.dragon.tecmap.analysis;

import com.github.rcaller.rstuff.*;
import com.github.rcaller.util.Globals;
import edu.ithaca.dragon.tecmap.io.record.ContinuousMatrixRecord;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItem;

public class RLibrary {

    /**
     * Must be called at the start of every function that uses RCaller methods in
     * order to properly run RCaller methods. The if statement checks whether the machine
     * running the function is Windows or Mac and sets up RCaller accordingly.
     * @return an RCaller that works on whichever machine is running to use RCaller methods
     */
    public static RCaller RCallerVariable(){
        if(Globals.isWindows() == false) {
            RCallerOptions options = RCallerOptions.create(RSettings.getRScriptPath(), Globals.R_current, FailurePolicy.RETRY_5, Long.MAX_VALUE, 100, RProcessStartUpOptions.create());
            return RCaller.create(options);
        }else {
            return RCaller.create();
        }
    }

    /**
     * Takes a 2D array of doubles and sends it to R in the form of RCode.
     * The commented code is pieces that may be useful in adding column and row
     * headers by name to the R matrix. The headers are currently indices
     * which is provided by the provideDimnames command in the RCode.
     * @param aMatrix the 2D array of doubles from Java
     * @return RCode of a 2D array in the same format as the
     */
    public static RCode convertJavaMatrixToR(double[][] aMatrix, String[] objStr){



        RCode code = RCode.create();
        code.addDoubleMatrix("data", aMatrix);
        for(int i = 0; i <objStr.length; i++){
            objStr[i] = objStr[i].replaceAll(":", "");
            objStr[i] = objStr[i].replaceAll("\\s", "");
        }



        //These lines change the Java structure into the correct format for R
        code.addRCode("matrix <- (t(data))");
        code.addRCode("matrix <- data.frame(matrix)");
        int i = 1;
        for(String columnName: objStr) {
            code.addString("columnName", columnName);
            code.addRCode("colnames(matrix)[" + i + "] <- columnName");
            i++;
        }
        //code.addRCode("provideDimnames(matrix)");
        //code.addRCode("matrix[matrix==-1] <- NA");

        return code;

    }

    public static RCode createRMatrix(ContinuousMatrixRecord assessmentMatrix){


        int objLength = assessmentMatrix.getAssessmentItems().size();

        //object list into string array

        int i = 0;
        String[] objStr = new String[objLength];
        for(AssessmentItem obj: assessmentMatrix.getAssessmentItems()){
            objStr[i] = obj.getId();
            i++;
        }

        RCode rMatrix = convertJavaMatrixToR(assessmentMatrix.getDataMatrix(), objStr);
        return rMatrix;
    }
}
