package edu.ithaca.dragonlab.ckc.io;

/**
 * Created by willsuchanek on 3/6/17.
 */
import java.io.*;
import java.util.ArrayList;

import edu.ithaca.dragonlab.ckc.learningobject.LearningObject;
import edu.ithaca.dragonlab.ckc.learningobject.LearningObjectResponse;
import edu.ithaca.dragonlab.ckc.learningobject.ManualGradedResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class CSVReader {
    Logger logger = LogManager.getLogger(this.getClass());

    String filename;
    BufferedReader csvBuffer = null;
    ArrayList<LearningObject> learningObjectList;
    ArrayList<LearningObjectResponse> manualGradedResponseList;

    /**
     * This function is passed a filename of a gradebook directly exported from Sakai's built in gradebook.
     * (See DataCSVExample.csv in test/testresources/io for proper file format example)
     * @param filename
     */
    public CSVReader(String filename){
        this.filename = filename;
        manualGradedResponseList = new ArrayList<>();
        learningObjectList = new ArrayList<>();
        try {
            String line;
            this.csvBuffer = new BufferedReader(new FileReader(filename));
            ArrayList<ArrayList<String>> lineList = new ArrayList<ArrayList<String>>();
            //Takes the file being read in and calls a function to convert each line into a list split at
            //every comma, then pust all the lists returned into a list of lists lineList[line][item in line]
            while((line = this.csvBuffer.readLine())!= null){
                lineList.add(lineToList(line));
            }

            boolean firstIteration = true;
            for(ArrayList<String> singleList: lineList){

                //The first list in the list of lists, is the Learning objects (questions)
                //so we go through the first line and pull out all the learning objects and put them into the
                //learning object list
                int i = 2; //this is 2 because the first two columns are not assignments, so the first assingment is index 2
                if(firstIteration){
                    firstIteration = false;
                    while(i<singleList.size()){
                        String question = singleList.get(i);
                        //used to find the max score of a question (won't be affected if there are other brackets in the question title
                        int begin = question.lastIndexOf('[');
                        int end = question.lastIndexOf(']');

                        //TODO: temp check to see what problem is... not a proper solution to bug #76
                        if (begin >= 0 && end >= 0) {
                            String maxScoreStr = question.substring(begin + 1, end);
                            double maxScore = Double.parseDouble(maxScoreStr);
                            question = question.substring(0, begin - 1);
                            LearningObject learningObject = new LearningObject(question);
                            learningObject.setMaxPossibleKnowledgeEstimate(maxScore);
                            this.learningObjectList.add(learningObject);
                        }
                        else {
                            logger.error("No max score found for string:"+question+"\t defaulting to 1, which is probably wrong");
                            LearningObject learningObject = new LearningObject(question);
                            learningObject.setMaxPossibleKnowledgeEstimate(1);
                            this.learningObjectList.add(learningObject);
                        }
                        i++;
                    }
                } else {
                    //goes through and adds all the questions to their proper learning object, as well as adds them to
                    //the general list of manual graded responses
                    String stdID = singleList.get(0);
                    while(i<singleList.size()){
                        LearningObject currentLearningObject = this.learningObjectList.get(i - 2);
                        String qid = currentLearningObject.getId();
                        if(!("".equals(singleList.get(i)))) {
                            ManualGradedResponse response = new ManualGradedResponse(qid, currentLearningObject.getMaxPossibleKnowledgeEstimate(), Double.parseDouble(singleList.get(i)), stdID);
                            currentLearningObject.addResponse(response);
                            this.manualGradedResponseList.add(response);
                        }

                        i++;
                    }

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public ArrayList<LearningObjectResponse> getManualGradedResponses(){
        return this.manualGradedResponseList;
    }

    public ArrayList<LearningObject> getManualGradedLearningObjects(){
        return this.learningObjectList;
    }

    private static ArrayList<String> lineToList(String line) {
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