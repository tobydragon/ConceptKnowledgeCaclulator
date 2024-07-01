package edu.ithaca.dragon.tecmap.io.reader;

import java.io.IOException;
import java.util.List;

/**
 *This is tha child class for zybooks files, with its specific function for reading in the student names
 * it will call super to the parent class, TecmapCSVReader, and pass the filename and the specific gradeColumnIndexMark.
 *
 * Created by Ryan on 10/4/2017.
 */

public class ZybooksReader extends TecmapCSVReader {
    public ZybooksReader(String filename)throws IOException{
        super(filename, 5, 1);
    }

    /**
     * This function takes in a line that holds all the info for a specific student and returns the full student name.
     * @param dataLine - the line of the students info and grades
     * @return the full name of the student
     */
    @Override
    public String getIDForAStudent(List<String> dataLine){
        String stdID = dataLine.get(0) + " " + dataLine.get(1);
        return stdID;
    }

    /**
     * This function takes in a line that holds all the info for a specific student and returns the
     * student name.
     * @param dataLine - the line of the students info and grades
     * @return the name of the student
     */
    @Override
    public String getNameForAStudent(List<String> dataLine){
        return dataLine.get(1);
    }
}
