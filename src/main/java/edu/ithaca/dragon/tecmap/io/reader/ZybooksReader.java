package edu.ithaca.dragon.tecmap.io.reader;

import edu.ithaca.dragon.tecmap.io.reader.CSVReader;

import java.io.IOException;
import java.util.List;

/**
 *This is tha child class for zybooks files, with its specific function for reading in the student names
 * it will call super to the parent class, CSVReader, and pass the filename and the specific gradeColumnIndexMark.
 *
 * Created by Ryan on 10/4/2017.
 */

public class ZybooksReader extends CSVReader {
    public ZybooksReader(String filename)throws IOException{
        super(filename, 5);
    }

    /**
     * This function will take a line of data from the CSV file that will contain all of the students data
     * the last and first name are added together and then compared to the list of current students, if
     * there is a repeat then a number is added to the end of the name
     *
     * @param dataLine - a row in the CSV file that has a students information and grades
     * @param studentNames - a list that holds all the currently read in list of students
     */
    @Override
    public String makeFullName(List<String> dataLine, List<String> studentNames){
        String stdID = dataLine.get(0) + " " + dataLine.get(1);
        boolean inList = false;
        int sameNameCnt = 0;
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
        return stdID;
    }
}
