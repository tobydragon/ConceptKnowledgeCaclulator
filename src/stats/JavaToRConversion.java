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

    public static RCode JavaToR(double[][] aMatrix, ArrayList<LearningObject> learningObjects, String[] users){
        /**
        double[][] aMatrix = loMatrix.getStructure();
        String[] users = loMatrix.getUserIdList();
        ArrayList<LearningObject> learningObjects = loMatrix.getObjList();
        int objLength = learningObjects.size();

        //object list into string array

        int i = 0;
        for(LearningObject obj: learningObjects){
            objStr[i] = obj.toString();
            i++;
        }
*/

        RCode code = RCode.create();
        code.addDoubleMatrix("data", aMatrix);

        /**
        code.addStringArray("headers", objStr);
        code.addStringArray("users", users);
        int userlength = users.length;
        int objlength = objLength;
        code.addInt("usercount", userlength);
        code.addInt("objcount", objlength);
        */


        //These lines change the Java structure into the correct format for R
        code.addRCode("matrix <- (t(data))");
        code.addRCode("provideDimnames(matrix)");

        return code;

    }
}
