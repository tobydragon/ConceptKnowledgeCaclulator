package edu.ithaca.dragonlab.ckc.conceptgraph;

import com.github.rcaller.rstuff.RCode;
import edu.ithaca.dragonlab.ckc.learningobject.LearningObject;
import edu.ithaca.dragonlab.ckc.learningobject.LearningObjectResponse;
import stats.JavaToRConversion;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by bleblanc2 on 6/13/17.
 */
public class KnowledgeEstimateMatrix {

    String id;
    double[][] studentKnowledgeEstimates;
    public List<LearningObject> objList;
    String[] userIdList;
    RCode rMatrix;


    public KnowledgeEstimateMatrix(ArrayList<LearningObject> lo){
        this.id = id;
        this.studentKnowledgeEstimates = createMatrix(lo);
        this.objList = lo;
        this.userIdList = userIdList;
        this.rMatrix = createRMatrix();

    }


    public double[][] createMatrix(Collection<LearningObject> learningObjects){
        //number of rows and columns needed check
        int columns = learningObjects.size();
        int rows = 0;
        for(LearningObject obj: learningObjects){
            List<LearningObjectResponse> responses = obj.getResponses();
            if(responses.size() > rows){
                rows = responses.size();
            }
        }
        double[][] newMatrix = new double[columns][rows];

        this.userIdList = new String[rows];
        int numOfIds = 0;
        int currentColumn = 0;
        int currentRow = 0;

        for(LearningObject obj: learningObjects) {
            List<LearningObjectResponse> responses = obj.getResponses();
            //first column of LearningObjectResponses cannot compare to anything to keep userid in same row as any previous columns of LearningObjectResponses
            if (currentColumn == 0) {
                for (LearningObjectResponse ans : responses) {
                        newMatrix[currentColumn][currentRow] = ans.calcKnowledgeEstimate();
                        userIdList[currentRow] = ans.getUserId();
                        numOfIds++;
                        currentRow++;
                }
            } else {

                //these columns must have response's userid matching across all rows
                //and make a new row if it does not match with anything

                //for each response in a LearningObject
                for (LearningObjectResponse ans : responses) {
                    boolean isPlaced = false;
                    //cycle through each index of the userIdList
                    for (int i = 0; i < numOfIds; i++) {
                        //if the current userId matches a userId in the list, place it at the same row as the userIdList index
                        if (ans.getUserId() == userIdList[i]) {
                            newMatrix[currentColumn][i] = ans.calcKnowledgeEstimate();
                            isPlaced = true;
                        } else {
                            //if all userIds in the list have been looked through and the response is still not placed, place the id in the list and the value into new row
                            if ((i == numOfIds - 1) && (isPlaced == false)) {
                                userIdList[numOfIds] = ans.getUserId();
                                newMatrix[currentColumn][numOfIds] = ans.calcKnowledgeEstimate();
                                numOfIds++;

                            }
                        }
                    }
                }
            }
            currentColumn++;
        }
        return newMatrix;
    }

    public RCode createRMatrix(){
        RCode rMatrix = JavaToRConversion.JavaToR(studentKnowledgeEstimates, objList, userIdList);
        return rMatrix;
    }

    public int getloIndex(LearningObject lo){
        int loIndex = -1;
        loIndex = objList.indexOf(lo);
        return loIndex;
    }


    public double[][] getStudentKnowledgeEstimates(){return this.studentKnowledgeEstimates;}

    public String[] getUserIdList(){return this.userIdList;}

    public List<LearningObject> getObjList(){return this.objList;}

    public RCode getrMatrix() {return rMatrix;}
}