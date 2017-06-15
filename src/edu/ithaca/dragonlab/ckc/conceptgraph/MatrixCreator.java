package edu.ithaca.dragonlab.ckc.conceptgraph;

import edu.ithaca.dragonlab.ckc.learningobject.LearningObject;
import edu.ithaca.dragonlab.ckc.learningobject.LearningObjectResponse;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static jdk.nashorn.internal.objects.ArrayBufferView.length;

/**
 * Created by bleblanc2 on 6/13/17.
 */
public class MatrixCreator {

    String id;
    LearningObjectResponse[][] structure;
    List<LearningObject> objList;


    public MatrixCreator(String id){
        this.id = id;
        this.structure = structure;
        this.objList = new ArrayList<>();


    }

    //takes an ArrayList of LearningObjects and puts them into columns.
    //Within each column, the responses in that LearningObject are placed in each row.
    /**
    public void collectingLearningObjects(ConceptGraph objectMap){

        Collection<String> objIDs = objectMap.getAllNodeIds();
        /*
        int idListLength = length(objIDs);
        String[] newObjIDs = objIDs.toArray();
        for(int i = 0; i < idListLength; i++){
            objectMap.findNodeById(newObjIDs[i]);
         }
         */
        //createMatrix(objIDs);
         //}




    public LearningObjectResponse[][] createMatrix(ConceptGraph graph){
        /** param: Collection<String> loidList
        int loidLength = length(loidList);
        LearningObject[] topRow = new LearningObject[loidLength];
        Object[] topRow = loidList.toArray();
        int maxResponses = 0;
        for(int i = 0; i < loidLength; i++) {
         */

        //list of learning objects for top row
        Map<String, LearningObject> loMap= graph.getLearningObjectMap();
        for(Map.Entry<String, LearningObject> entry : loMap.entrySet()){
            String key = entry.getKey();
            LearningObject value = entry.getValue();
            objList.add(value);
        }
        //number of rows and columns needed check
        int columns = length(objList);
        int rows = 0;
        for(LearningObject obj: objList){
            List<LearningObjectResponse> responses = obj.getResponses();
            if(length(responses) > rows){
                rows = length(responses);
            }
        }
        structure = new LearningObjectResponse[columns][rows];



        String[] userIdList = new String[rows];
        int numOfIds = 0;
        int currentColumn = 0;

        for(LearningObject obj: objList){
            List<LearningObjectResponse> responses = obj.getResponses();
            //first column of LearningObjectResponses cannot compare to anything to keep userid in same row as any previous columns of LearningObjectResponses
            if(currentColumn == 0){
                int currentRow = 0;
                for(LearningObjectResponse ans: responses) {
                    if(ans != null) {
                        structure[currentColumn][currentRow] = ans;
                        userIdList[currentRow] = ans.getUserId();
                        numOfIds++;
                        currentRow++;
                    }
                }
                currentColumn++;
            }else{

                //these columns must have response's userid matching across all rows
                //and make a new row if it does not match with anything
                for(LearningObjectResponse ans: responses){
                    for(int i = 0; i < numOfIds; i++){
                        //existing userid
                        if(ans.getUserId() == userIdList[i]){
                            structure[currentColumn][i] = ans;
                            break;
                        }else{
                            //new userid -> new row
                            if(i == (numOfIds-1)){
                                userIdList[numOfIds] = ans.getUserId();
                                structure[currentColumn][numOfIds] = ans;
                                numOfIds++;
                                break;
                            }
                        }
                    }
                }
            }
        }

        return structure;

    }



}