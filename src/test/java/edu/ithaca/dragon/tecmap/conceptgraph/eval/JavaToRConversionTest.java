package edu.ithaca.dragon.tecmap.conceptgraph.eval;

import com.github.rcaller.rstuff.*;
import com.github.rcaller.util.Globals;
import edu.ithaca.dragon.tecmap.Settings;
import edu.ithaca.dragon.tecmap.io.reader.CSVReader;
import edu.ithaca.dragon.tecmap.io.reader.SakaiReader;
import edu.ithaca.dragon.tecmap.learningresource.ColumnItem;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.List;


/**
 * Created by bleblanc2 on 6/16/17.
 */

public class JavaToRConversionTest {

    @Test

    public void matrixTransfer(){



        String file = Settings.TEST_RESOURCE_DIR + "ManuallyCreated/partialComplexRealitsticAssessment.csv";
        try {
            CSVReader data = new SakaiReader(file);
            List<ColumnItem> gotoMatrix = data.getManualGradedLearningObjects();
            KnowledgeEstimateMatrix newMatrix = new KnowledgeEstimateMatrix(gotoMatrix);
            double[][] struct = newMatrix.getStudentKnowledgeEstimates();
            List<ColumnItem> objList = newMatrix.getObjList();
            List<String> user = newMatrix.getUserIdList();

            int objLength = objList.size();

            //object list into string array

            int i = 0;
            String[] objStr = new String[objLength];
            for(ColumnItem obj: objList){
                objStr[i] = obj.getId();
                i++;
            }
            try {
                RCode mycode = RFunctions.JavaToR(struct, objStr);

                mycode.addRCode("classAvg <- mean(matrix[, 3])");


                RCaller rCaller;
                if (Globals.isWindows() == false) {
                    RCallerOptions options = RCallerOptions.create("/usr/local/Cellar/r/3.4.0_1/bin/Rscript", Globals.R_current, FailurePolicy.RETRY_5, Long.MAX_VALUE, 100, RProcessStartUpOptions.create());
                    rCaller = RCaller.create(options);
                } else {
                    rCaller = RCaller.create();
                }

                rCaller.setRCode(mycode);
                rCaller.runAndReturnResult("classAvg");
                double[] results = rCaller.getParser().getAsDoubleArray("classAvg");
                double actual = results[0];
                Assert.assertEquals(0.88166, actual, 0.001);

            }catch (Exception e){
                System.out.println("R not installed");
            }
        }catch (IOException e){
            Assert.fail();
        }
    }


}