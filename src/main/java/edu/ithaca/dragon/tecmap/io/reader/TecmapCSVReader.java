package edu.ithaca.dragon.tecmap.io.reader;

/**
 * TecmapCSVReader is the parent function that is responsible for the reading in CSV files, it is created when
 * a child class calls to it giving file specific parameters
 * Created by willsuchanek on 3/6/17.
 */

import edu.ithaca.dragon.tecmap.learningresource.AssessmentItem;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItemResponse;
import edu.ithaca.dragon.tecmap.learningresource.ManualGradedResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public abstract class TecmapCSVReader {
    static Logger logger = LogManager.getLogger(TecmapCSVReader.class);

    String filename;
    BufferedReader csvBuffer = null;
    List<AssessmentItem> columnItemList;
    List<AssessmentItemResponse> manualGradedResponseList;
    ReaderTools toolBox = new ReaderTools();
    int gradeStartColumnIndex;
    int nameStartRowIndex;
    List<String> studentNames = new ArrayList<>();

    /**
     * This function is passed a filename of a gradebook directly exported from Sakai's built in gradebook.
     * (See DataCSVExample.csv in test/testresources/io for proper file format example)
     * this function also takes an index mark that will determine where grades are first recorded in the
     * CSV file.
     * @param filename
     * @param gradeStartColumnIndex
     */
    public TecmapCSVReader(String filename, int gradeStartColumnIndex, int nameStartRowIndex)throws IOException{
        this.filename = filename;
        manualGradedResponseList = new ArrayList<>();
        columnItemList = new ArrayList<>();
        this.gradeStartColumnIndex = gradeStartColumnIndex;
        this.nameStartRowIndex = nameStartRowIndex;
        try {
            String line;
            this.csvBuffer = new BufferedReader(new FileReader(filename));
            ArrayList<ArrayList<String>> lineList = new ArrayList<>();
            //Takes the file being read in and calls a function to convert each line into a list split at
            //every comma, then pust all the lists returned into a list of lists lineList[line][item in line]
            while((line = this.csvBuffer.readLine())!= null){
                ArrayList<String> potentialLineList = toolBox.lineToList(line);
                if (potentialLineList.size() > 1){
                    lineList.add(potentialLineList);
                }
            }

//           The first list in the list of lists is the assessments (questions) so we go through the first line and
//           pull out all the learning objects and put them into the columnItemList
            this.columnItemList = toolBox.assessmentItemsFromList(gradeStartColumnIndex, lineList.get(0));
            for (int i = nameStartRowIndex; i < lineList.size(); i++) {
                studentNames.add(getNameForAStudent(lineList.get(i)));
                try {
                    //goes through and adds all the questions to their proper learning object, as well as adds them to
                    //the general list of manual graded responses
                    createManualGradedResponseList(lineList.get(i));
                } catch (NullPointerException e) {
                    System.out.println("No Responses added to one or more LearningObjects");
                }
            }
        } catch (IOException e) {
//            e.printStackTrace();
        }
    }

    public List<AssessmentItemResponse> getManualGradedResponses(){return this.manualGradedResponseList;}

    public List<AssessmentItem> getManualGradedLearningObjects(){return this.columnItemList;}

    public abstract String getIDForAStudent(List<String> dataLine);

    public abstract String getNameForAStudent(List<String> dataLine);

    /**
     * This function takes a list of student data and creates a manual graded response for that user
     * @param singleList - a list with each line in the csv file holding LORs
     * @throws NullPointerException if the ManualGradedResponse is null
     */
    public void createManualGradedResponseList(ArrayList<String> singleList)throws NullPointerException{
        int i = gradeStartColumnIndex;
        String stdID = getIDForAStudent(singleList);
        if (columnItemList.size() + gradeStartColumnIndex < singleList.size()) {
            logger.error("More data than learning objects on line for id:" + stdID);
        } else if (columnItemList.size() + gradeStartColumnIndex > singleList.size()) {
            logger.warn("More learning objects than data on line for id:" + stdID);
        }
        //need to make sure we don't go out of bounds on either list
        while (i < singleList.size() && i < columnItemList.size() + gradeStartColumnIndex) {
            AssessmentItem currentColumnItem = this.columnItemList.get(i - gradeStartColumnIndex);
            String qid = currentColumnItem.getId();
            if (!("".equals(singleList.get(i)))) {
                double studentGrade;
                String number = toolBox.pullNumber(singleList.get(i));
                if (number.equals("")){
                    studentGrade = 0.0;
                }
                else{
                    studentGrade = Double.parseDouble(number);
                }
                ManualGradedResponse response = new ManualGradedResponse(qid, currentColumnItem.getMaxPossibleKnowledgeEstimate(), studentGrade, stdID);
                if(response != null) {
                    currentColumnItem.addResponse(response);
                    this.manualGradedResponseList.add(response);
                }else{
                    throw new NullPointerException();
                }
            }
            i++;
        }
    }
}