package edu.ithaca.dragon.tecmap.io.reader;

/**
 * CSVReader is the parent function that is responsible for the reading in CSV files, it is created when
 * a child class calls to it giving file specific parameters
 * Created by willsuchanek on 3/6/17.
 */

import edu.ithaca.dragon.tecmap.learningobject.LearningObject;
import edu.ithaca.dragon.tecmap.learningobject.LearningObjectResponse;
import edu.ithaca.dragon.tecmap.learningobject.ManualGradedResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public abstract class CSVReader {
    static Logger logger = LogManager.getLogger(CSVReader.class);

    String filename;
    BufferedReader csvBuffer = null;
    List<LearningObject> learningObjectList;
    List<LearningObjectResponse> manualGradedResponseList;
    ReaderTools toolBox = new ReaderTools();

    /**
     * This function is passed a filename of a gradebook directly exported from Sakai's built in gradebook.
     * (See DataCSVExample.csv in test/testresources/io for proper file format example)
     * this function also takes an index mark that will determine where grades are first recorded in the
     * CSV file.
     * @param filename
     * @param gradeStartCoulmnIndex
     */
    public CSVReader(String filename, int gradeStartCoulmnIndex)throws IOException{
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
                lineList.add(toolBox.lineToList(line));
            }

            boolean firstIteration = true;
            for(ArrayList<String> singleList: lineList){

                //The first list in the list of lists, is the Learning objects (questions)
                //so we go through the first line and pull out all the learning objects and put them into the
                //learning object list
                if(firstIteration){
                    firstIteration = false;
                    this.learningObjectList = toolBox.learningObjectsFromList(gradeStartCoulmnIndex,singleList);
                } else {
                    try {
                        //goes through and adds all the questions to their proper learning object, as well as adds them to
                        //the general list of manual graded responses
                        lorLister(singleList, gradeStartCoulmnIndex);
                    }catch (NullPointerException e) {
                        System.out.println("No Responses added to one or more LearningObjects");
                    }
                }
            }
        } catch (IOException e) {
//            e.printStackTrace();
        }
    }
    List<String> studentNames = new ArrayList<>();


    public List<LearningObjectResponse> getManualGradedResponses(){return this.manualGradedResponseList;}

    public List<LearningObject> getManualGradedLearningObjects(){return this.learningObjectList;}

    public abstract String makeFullName(List<String> dataLine, List<String> studentNames);

    /**
     * This function takes a list of student data and creates a manual graded response for that user
     * @param singleList - a list with each line in the csv file holding LORs
     * @param gradeMark - used to keep track of which index in the list of LORs the function is currently on
     * @throws NullPointerException if the ManualGradedResponse is null
     */
    public void lorLister(ArrayList<String> singleList,int gradeMark)throws NullPointerException{
        int i = gradeMark;
        String stdID = makeFullName(singleList, studentNames);
        if (learningObjectList.size() + gradeMark < singleList.size()) {
            logger.warn("More data than learning objects on line for id:" + stdID);
        } else if (learningObjectList.size() + gradeMark > singleList.size()) {
            logger.warn("More learning objects than data on line for id:" + stdID);
        }
        //need to make sure we don't go out of bounds on either list
        while (i < singleList.size() && i < learningObjectList.size() + gradeMark) {
            LearningObject currentLearningObject = this.learningObjectList.get(i - gradeMark);
            String qid = currentLearningObject.getId();
            if (!("".equals(singleList.get(i)))) {
                double studentGrade;
                String number = toolBox.pullNumber(singleList.get(i));
                if (number.equals("")){
                    studentGrade = 0.0;
                }
                else{
                    studentGrade = Double.parseDouble(number);
                }
                ManualGradedResponse response = new ManualGradedResponse(qid, currentLearningObject.getMaxPossibleKnowledgeEstimate(), studentGrade, stdID);
                if(response != null) {
                    currentLearningObject.addResponse(response);
                    this.manualGradedResponseList.add(response);
                }else{
                    throw new NullPointerException();
                }
            }
            i++;
        }
    }
}