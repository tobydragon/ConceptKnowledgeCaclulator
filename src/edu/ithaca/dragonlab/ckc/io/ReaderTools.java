package edu.ithaca.dragonlab.ckc.io;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import edu.ithaca.dragonlab.ckc.learningobject.LearningObject;
import edu.ithaca.dragonlab.ckc.learningobject.LearningObjectResponse;
import edu.ithaca.dragonlab.ckc.learningobject.ManualGradedResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
/**
 * Created by Ryan on 10/25/2017.
 */
public class ReaderTools {

    static Logger logger = LogManager.getLogger(ReaderTools.class);

    String filename;
    BufferedReader csvBuffer = null;
    List<LearningObject> learningObjectList;
    List<LearningObjectResponse> manualGradedResponseList;


    public static ArrayList<ArrayList<String>> staticLineToList(String filename){
        ArrayList<ArrayList<String>> lineList = new ArrayList<ArrayList<String>>();
        try {
            String line;
            BufferedReader csvBuffer = new BufferedReader(new FileReader(filename));
            //Takes the file being read in and calls a function to convert each line into a list split at
            //every comma, then pust all the lists returned into a list of lists lineList[line][item in line]
            while ((line = csvBuffer.readLine()) != null) {
                lineList.add(lineToList(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lineList;
    }

    public static List<LearningObject> learningObjectsFromList(int indexMark, List<String> singleList) {
        List<LearningObject> loList = new ArrayList<LearningObject>();
        while(indexMark<singleList.size()){
            String question = singleList.get(indexMark);
            //used to find the max score of a question (won't be affected if there are other brackets in the question title
            int begin = question.lastIndexOf('('); // important change for the outcome of the exercises
            int end = question.lastIndexOf(')');

            if (indexMark == 2){
                begin = question.lastIndexOf('['); // important change for the outcome of the exercises
                end = question.lastIndexOf(']');
            }


            if (begin >= 0 && end >= 0) {
                String maxScoreStr = question.substring(begin + 1, end);
                double maxScore = Double.parseDouble(maxScoreStr);
                question = question.substring(0, begin - 1);
                LearningObject learningObject = new LearningObject(question);
                learningObject.setMaxPossibleKnowledgeEstimate(maxScore);
                loList.add(learningObject);
            }
            else {
                //logger.error("No max score found for string:"+question+"\t defaulting to 1, which is probably wrong");
                LearningObject learningObject = new LearningObject(question);
                learningObject.setMaxPossibleKnowledgeEstimate(1);
                loList.add(learningObject);
            }
            indexMark++;
        }
        return loList;
    }

    /**
     *
     * @param singleList a list with each line in the csv file holding LORs
     * @param indexMark used to keep track of which index in the list of LORs the function is currently on
     * @throws NullPointerException if the ManualGradedResponse is null
     */

    public void lorLister(ArrayList<String> studentNames, ArrayList<String> singleList, int indexMark, int spaceVariable)throws NullPointerException{
        ///////////////////////////////////////////////////////////////////////////////////////
        boolean inList = false;
        int sameNameCnt = 0;
        String stdID = singleList.get(0) + " " + singleList.get(1); // gets last name and first name to append together
        String nameTest = stdID;
        while(inList == false){
            if (studentNames.indexOf(nameTest) > -1){
                sameNameCnt += 1;
                nameTest = stdID + Integer.toString(sameNameCnt);
            }
            else{
                studentNames.add(nameTest);
                stdID = nameTest;
                inList = true;
            }
        }
        //////////////////////////////////////////////////////////////////////////////////////
        if (learningObjectList.size() + spaceVariable < singleList.size()) {
            logger.warn("More data than learning objects on line for id:" + stdID);
        } else if (learningObjectList.size() + spaceVariable > singleList.size()) {
            logger.warn("More learning objects than data on line for id:" + stdID);
        }
        //need to make sure we don't go out of bounds on either list
        while (indexMark < singleList.size() && indexMark < learningObjectList.size() + spaceVariable) {
            LearningObject currentLearningObject = this.learningObjectList.get(indexMark - spaceVariable);
            String qid = currentLearningObject.getId();
            if (!("".equals(singleList.get(indexMark)))) {
                // made to catch any grade issue with N/A, a temporary fix
                //////////////////////////////////////////////////////////////////////////////
                double studentGrade;
                if (singleList.get(indexMark).equals("N/A")){
                    studentGrade = 0.0;
                }
                else{
                    studentGrade = Double.parseDouble(singleList.get(indexMark));
                }
                ///////////////////////////////////////////////////////////////////////////////
                ManualGradedResponse response = new ManualGradedResponse(qid, currentLearningObject.getMaxPossibleKnowledgeEstimate(), studentGrade, stdID);
                if(response != null) {
                    currentLearningObject.addResponse(response);
                    this.manualGradedResponseList.add(response);
                }else{
                    throw new NullPointerException();
                }
            }
            indexMark++;
        }
    }

    /**
     * takes a list of csv files and creates a single list of LearningObjects from all files
     * @param csvfiles
     * @return a list of all LearningObjects across all files
     */
    //
    // need to know what other classes call this funtion to edit call
    //
    private static List<LearningObject> learningObjectsFromCSVList(int indexMark, List<String> csvfiles){
        List<LearningObject> fullLoList = new ArrayList<LearningObject>();

        //Each csvfile has their LOs searched
        for(String file: csvfiles){
            ArrayList<ArrayList<String>> lineList = ReaderTools.staticLineToList(file);
            List<LearningObject> loList = new ArrayList<LearningObject>();
            loList = ReaderTools.learningObjectsFromList(indexMark,lineList.get(0));

            //adding current csvfile's LOs to the full list of LOs
            for(LearningObject learningObject: loList) {
                fullLoList.add(learningObject);
            }
        }
        return fullLoList;
    }


    public List<LearningObjectResponse> getManualGradedResponses(){return this.manualGradedResponseList;}

    public List<LearningObject> getManualGradedLearningObjects(){return this.learningObjectList;}

    public static String pullNumber(String object) {
        String numbers = "";
        int decimal = 0;
        char character = 'a';
        for (int i = 0; i < object.length(); i++){
            character = object.charAt(i);
            if (Character.isDigit(character) && decimal <= 1){
                numbers += character;
            }
            if (character == '.'){
                decimal += 1;
                numbers += character;
            }
            else if (decimal >= 2){
                return "";
            }
        }
        return numbers;
    }

    public static ArrayList<String> lineToList(String line) {
        ArrayList<String> returnlist = new ArrayList<String>();
        String item = "";
        Boolean betweenQuote = false;
        for (int i = 0; i < line.length(); i++){
            if (line.charAt(i) == '"' && betweenQuote == false){
                betweenQuote = true;
            }
            else if (line.charAt(i) == '"' && betweenQuote == true){
                betweenQuote = false;
            }
            else if (line.charAt(i) == ',' && !betweenQuote){
                returnlist.add(item.trim());
                item = "";
            }
            else{
                item += line.charAt(i);
            }
        }
        // make sure to add a separate function for String stripping
        returnlist.add(item.trim());
//        if (line != null) {
//            String[] splitData = line.split("\\s*,\\s*");
//            for (int i = 0; i < splitData.length; i++) {
//                if (!(splitData[i] == null) || !(splitData[i].length() == 0)) {
//                    returnlist.add(splitData[i].trim());
//                }
//            }
//        }

        return returnlist;
    }


}
