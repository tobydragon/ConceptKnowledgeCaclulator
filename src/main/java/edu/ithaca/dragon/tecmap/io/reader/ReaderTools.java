package edu.ithaca.dragon.tecmap.io.reader;

import edu.ithaca.dragon.tecmap.learningobject.AssessmentItem;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is a tool box class for CSV reader, these static functions are called by CSVReader
 * to to data specific and line reading tasks.
 * Created by Ryan on 10/25/2017.
 */
public class ReaderTools {


    /**
     * staticLineToList takes a given CSV file and reads each line in the file and adds it to a list
     * to be returned.
     *
     * @param filename the name of the file being read
     * @return lineList, contains a list of each line in the file
     */
    public static ArrayList<ArrayList<String>> staticLineToList(String filename){
        ArrayList<ArrayList<String>> lineList = new ArrayList<ArrayList<String>>();
        try {
            String line;
            BufferedReader csvBuffer = new BufferedReader(new FileReader(filename));
            //Takes the file being read in and calls a function to convert each line into a list split at
            //every comma, then push all the lists returned into a list of lists lineList[line][item in line]
            while ((line = csvBuffer.readLine()) != null) {
                lineList.add(lineToList(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lineList;
    }

    /**
     * This function take the list of assignments in a given file with thier max grades in the title, This program takes
     * the name of the assignment separate from the max grade and returns a list of the learning objects
     *
     * @param indexMark the column index point where the recording of assignments and  grades start for the rest of the file
     * @param singleList the line of the file that contains the list of assignments with the maximum grade
     * @return loList -> a list of each assignment and its maximum grade
     */

    public static List<AssessmentItem> learningObjectsFromList(int indexMark, List<String> singleList) {
        int i = indexMark;
        List<AssessmentItem> loList = new ArrayList<AssessmentItem>();
        while(i<singleList.size()){
            String question = singleList.get(i);
            //used to find the max score of a question (won't be affected if there are other brackets in the question title
            int begin = question.lastIndexOf('(');
            int end = question.lastIndexOf(')');

            if (indexMark == 5){
                begin = question.lastIndexOf('(');
                end = question.lastIndexOf(')');
            }
            if (indexMark == 2){
                begin = question.lastIndexOf('['); // important change for the outcome of the exercises
                end = question.lastIndexOf(']');
            }

            if (begin >= 0 && end >= 0) {
                String maxScoreStr = question.substring(begin + 1, end);
                double maxScore = Double.parseDouble(maxScoreStr);
                question = question.substring(0, begin - 1);
                AssessmentItem assessmentItem = new AssessmentItem(question);
                assessmentItem.setMaxPossibleKnowledgeEstimate(maxScore);
                loList.add(assessmentItem);
            }
            else {
                //logger.error("No max score found for string:"+question+"\t defaulting to 1, which is probably wrong");
                AssessmentItem assessmentItem = new AssessmentItem(question);
                assessmentItem.setMaxPossibleKnowledgeEstimate(1);
                loList.add(assessmentItem);
            }
            i++;
        }
        return loList;
    }

    /**
     * takes a list of csv files and creates a single list of LearningObjects from all files
     *
     * @param csvfiles
     * @return a list of all LearningObjects across all files
     */
    public static List<AssessmentItem> learningObjectsFromCSVList(int indexMark, List<String> csvfiles){
        List<AssessmentItem> fullLoList = new ArrayList<AssessmentItem>();

        //Each csvfile has their LOs searched
        for(String file: csvfiles){
            ArrayList<ArrayList<String>> lineList = ReaderTools.staticLineToList(file);
            List<AssessmentItem> loList = new ArrayList<AssessmentItem>();
            loList = ReaderTools.learningObjectsFromList(indexMark,lineList.get(0));

            //adding current csvfile's LOs to the full list of LOs
            for(AssessmentItem assessmentItem : loList) {
                fullLoList.add(assessmentItem);
            }
        }
        return fullLoList;
    }

    /**
     * This function can take a string and pull a number with a decimal and retrun that number,
     * if no valid number is found then the function will return an empty string
     *
     * @param object a passed in string that we want to find a number from (one decimal is valid)
     * @return the number found in the string, or an empty string when a number isn't found or valid
     */
    public static String pullNumber(String object) {
        String numbers = "";
        int decimal = 0;
        char character = 'a';
        for (int i = 0; i < object.length(); i++){
            character = object.charAt(i);
            if (Character.isDigit(character) && decimal <= 1){
                numbers += character;
            }
            else if (character == '-' && Character.isDigit(object.charAt(i+1)))
                numbers += character;
            else if (character == '.'){
                decimal += 1;
                numbers += character;
            }
            else if (decimal >= 2){
                return "";
            }
            // else that means there is a second decimal and should return an empty string
        }
        return numbers;
    }

    /**
     * This function takes a string and separates them by commas and quotations and
     * returns the list of the strings.
     *
     * @param line a line of strings that need separation by commas and quotations
     * @return a properly separated list of strings
     */
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
        returnlist.add(item.trim());
        return returnlist;
    }
}
