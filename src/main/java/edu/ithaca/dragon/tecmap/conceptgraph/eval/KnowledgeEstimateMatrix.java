package edu.ithaca.dragon.tecmap.conceptgraph.eval;

import com.github.rcaller.rstuff.RCode;
import edu.ithaca.dragon.tecmap.learningresource.ColumnItem;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItemResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * Created by bleblanc2 on 6/13/17.
 * Creates an object that holds 2D array of doubles that store student knowledge estimates,
 * a list of learning objects and users that make up these estimate values, and a matrix
 * of this 2D array that is usable in R
 */
public class KnowledgeEstimateMatrix {

    String id;
    double[][] studentKnowledgeEstimates;
    public List<ColumnItem> objList;
    List<String> userIdList;
    RCode rMatrix;


    public KnowledgeEstimateMatrix(List<ColumnItem> lo) throws IOException {
        this.id = id;
        this.objList = lo;
        this.userIdList = new ArrayList<String>();
        this.studentKnowledgeEstimates = createMatrix(lo);
        this.rMatrix = createRMatrix(studentKnowledgeEstimates);
    }


    /**
     * Helper function for constructor
     * Creates a 2D array from a list of LearningObjects and their responses held within the objects.
     * The columns are sorted by LearningObjects. The rows are sorted by the userId held within the responses.
     * A userIdList is also created to track the userIds to the correct row.
     * @param columnItems collection of columnItems
     * @return a 2D Array of learningObjectResponses based on the list of columnItems
     * @post userListId is changed to add each new user found within the LORs
     */

    public double[][] createMatrix(Collection<ColumnItem> columnItems) throws IOException {
        //number of rows and columns needed check
        int columns = columnItems.size();
        int rows = 0;
        for(ColumnItem obj: columnItems){
            List<AssessmentItemResponse> responses = obj.getResponses();
            if(responses.size() > rows){
                rows = responses.size();
            }
        }
        double[][] newMatrix = new double[columns][rows];

        int numOfIds = 0;
        int currentColumn = 0;
        int currentRow = 0;

        List<String> assessmentItemIDs = new ArrayList<>();

        for(ColumnItem obj: columnItems) {
            if (assessmentItemIDs.contains(obj.getId())) {
                throw new IOException("Cannot Add Multiple Assessments With Same ID");
            }
            assessmentItemIDs.add(obj.getId());
            List<AssessmentItemResponse> responses = obj.getResponses();
            //first column of LearningObjectResponses cannot compare to anything to keep userid in same row as any previous columns of LearningObjectResponses
            if (currentColumn == 0) {
                for (AssessmentItemResponse ans : responses) {
                        newMatrix[currentColumn][currentRow] = ans.calcKnowledgeEstimate();
                        userIdList.add(ans.getUserId());
                        numOfIds++;
                        currentRow++;
                }
            } else {

                //these columns must have response's userid matching across all rows
                //and make a new row if it does not match with anything

                //for each response in a ColumnItem
                for (AssessmentItemResponse ans : responses) {
                    boolean isPlaced = false;
                    //cycle through each index of the userIdList
                    for (String user: userIdList){
                        //if the current userId matches a userId in the list, place it at the same row as the userIdList index
                        if (ans.getUserId().equals(user)) {
                            newMatrix[currentColumn][userIdList.indexOf(user)] = ans.calcKnowledgeEstimate();
                            isPlaced = true;
                        }

                    }
                    //if all userIds in the list have been looked through and the response is still not placed, place the id in the list and the value into new row

                    if ((isPlaced == false)) {
                        userIdList.add(ans.getUserId());
                        newMatrix[currentColumn][numOfIds] = ans.calcKnowledgeEstimate();
                        numOfIds++;

                    }
                }
            }
            currentColumn++;
        }
        return newMatrix;
    }


    /**
     * Calls a function that uses the 2D Array to make base RCode for R functions
     * @pre called from constructor
     * @return RCode holding a R-readable version of the studentKnowledgeEstimates matrix
     */

    //TODO: look into putting into constructor
    public RCode createRMatrix(double[][] studentKnowledgeEstimates){

        int objLength = objList.size();

        //object list into string array

        int i = 0;
        String[] objStr = new String[objLength];
        for(ColumnItem obj: objList){
            objStr[i] = obj.getId();
            i++;
        }


        RCode rMatrix = RFunctions.JavaToR(studentKnowledgeEstimates, objStr);
        return rMatrix;
    }

    /**
     * Uses a learningObject to find which column in the array to search
     * @param lo learningObject
     * @return the index of the learningObject
     */
    public int getloIndex(ColumnItem lo){
        int loIndex = -1;
        loIndex = objList.indexOf(lo);
        return loIndex;
    }


    public double[][] getStudentKnowledgeEstimates(){return this.studentKnowledgeEstimates;}

    public List<String> getUserIdList(){return this.userIdList;}

    public List<ColumnItem> getObjList(){return this.objList;}

    public List<String> getAssessmentIdList() {
        List<ColumnItem> columnItems = getObjList();

        List<String> assessmentIds = new ArrayList<>();

        for (ColumnItem columnItem : columnItems) {
            assessmentIds.add(columnItem.getId());
        }

        return assessmentIds;
    }

    public RCode getrMatrix() {return rMatrix;}
}