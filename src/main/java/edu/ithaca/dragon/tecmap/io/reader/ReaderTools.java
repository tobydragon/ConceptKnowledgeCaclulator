package edu.ithaca.dragon.tecmap.io.reader;

import com.opencsv.exceptions.CsvException;
import edu.ithaca.dragon.tecmap.Settings;
import edu.ithaca.dragon.tecmap.io.record.LearningResourceRecord;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItem;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This class is a tool box class for CSV reader, these static functions are called by TecmapCSVReader
 * to data specific and line reading tasks.
 * Created by Ryan on 10/25/2017.
 */
public class ReaderTools {

    /**
     * This function take the list of assessments in a given file with their max grades in the title, This program takes
     * the name of the assessment separate from the max grade and returns a list of the assessmentItems
     * @param rows all rows of the file
     * @return aiList -> a list of each assessment and its maximum grade
     */

    public static List<AssessmentItem> assessmentItemsFromList(List<String[]> rows) {
        List<AssessmentItem> aiList = new ArrayList<>();
        for (int i = 4; i < rows.get(0).length; i++) {
            String text = rows.get(0)[i];
            String maxScoreStr = rows.get(2)[i];
            String id = LearningResourceRecord.generateUniqueID(null);
            if (!maxScoreStr.isEmpty()) {
                double maxScore = Double.parseDouble(maxScoreStr);
                AssessmentItem columnItem = new AssessmentItem(id, text, maxScore);
                aiList.add(columnItem);
            }
            else {
                AssessmentItem columnItem = new AssessmentItem(id, text);
                aiList.add(columnItem);
            }
        }
        return aiList;
    }

    /**
     * takes a list of csv files and creates a single list of AssessmentItem from all files
     *
     * @param csvfiles
     * @return a list of all AssessmentItem across all files
     */
    public static List<AssessmentItem> assessmentItemsFromCSVList(List<String> csvfiles) throws IOException, CsvException {
        List<AssessmentItem> fullAIList = new ArrayList<>();

        // Each csvfile has their AIs searched
        for(String file: csvfiles){
            List<String[]> rows = CsvFileLibrary.parseRowsFromFile(file);
            CanvasConverter.canvasConverter(rows);
            List<AssessmentItem> aiList = ReaderTools.assessmentItemsFromList(rows);

            //adding current csvfile's LOs to the full list of AIs
            for(AssessmentItem columnItem : aiList) {
                fullAIList.add(columnItem);
            }
        }
        return fullAIList;
    }

    /**
     * This function can take a string and pull a number with a decimal and return that number,
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
        ArrayList<String> returnlist = new ArrayList<>();
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
