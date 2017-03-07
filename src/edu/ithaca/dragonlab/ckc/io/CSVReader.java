package edu.ithaca.dragonlab.ckc.io;

/**
 * Created by willsuchanek on 3/6/17.
 */
import java.io.*;
import java.util.ArrayList;

import edu.ithaca.dragonlab.ckc.learningobject.LearningObject;
import edu.ithaca.dragonlab.ckc.learningobject.ManualGradedResponse;


public class CSVReader {
    String filename;
    BufferedReader csvBuffer = null;
    //HandGradedQuestionsList
    ArrayList<LearningObject> learningObjectList;
    ArrayList<ManualGradedResponse> manualGradedResponseList;


    public CSVReader(String filename){
        this.filename = filename;
        manualGradedResponseList = new ArrayList<ManualGradedResponse>();
        learningObjectList = new ArrayList<LearningObject>();
        try {
            String line;
            this.csvBuffer = new BufferedReader(new FileReader(filename));
            ArrayList<ArrayList<String>> lineList = new ArrayList<ArrayList<String>>();
            while((line = this.csvBuffer.readLine())!= null){
                lineList.add(lineToList(line));
            }
            boolean firstIteration = true;
            double maxScore = 0;
            for(ArrayList<String> singleList: lineList){
                int i = 2;
                if(firstIteration){
                    firstIteration = false;
                    while(i<singleList.size()){
                        String question = singleList.get(i);
                        int begin = question.lastIndexOf('[');
                        int end = question.lastIndexOf(']');
                        String maxScoreStr = question.substring(begin + 1, end);
                        maxScore = Double.parseDouble(maxScoreStr);
                        question = question.substring(0, begin-1);
                        LearningObject learningObject = new LearningObject(question);
                        learningObject.setMaxKnowledgeEstimate(maxScore);
                        this.learningObjectList.add(learningObject);
                        i++;
                    }
                } else {
                    String stdID = singleList.get(0);
                    while(i<singleList.size()){
                        LearningObject currentLearningObject = this.learningObjectList.get(i - 2);
                        String qid = currentLearningObject.getId();
                        ManualGradedResponse response = new ManualGradedResponse(qid,currentLearningObject.getMaxKnowledgeEstimate(),Double.parseDouble(singleList.get(i)),stdID);
                        currentLearningObject.addResponse(response);
                        this.manualGradedResponseList.add(response);
                        i++;
                    }

                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public ArrayList<ManualGradedResponse> getManualGradedResponses(){
        return this.manualGradedResponseList;
    }

    public ArrayList<LearningObject> getManualGradedLearningObjects(){
        return this.learningObjectList;
    }

    public static ArrayList<String> lineToList(String line) {
        ArrayList<String> returnlist = new ArrayList<String>();

        if (line != null) {
            String[] splitData = line.split("\\s*,\\s*");
            for (int i = 0; i < splitData.length; i++) {
                if (!(splitData[i] == null) || !(splitData[i].length() == 0)) {
                    returnlist.add(splitData[i].trim());
                }
            }
        }

        return returnlist;
    }


}