package edu.ithaca.dragonlab.ckc.io;

import java.io.IOException;
import java.util.List;

/**
 * *This is tha child class for sakai files, with its specific function for reading in the student names
 * it will call super to the parent class, CSVReader, and pass the filename and the specific gradeColumnIndexMark.
 * Created by Ryan on 11/9/2017.
 */
public class SakaiReader extends CSVReader{
    public SakaiReader(String filename)throws IOException{
        super(filename, 2);
    }

    /**
     * This function takes in a line that holds all the info for a specific student and returns the
     * student name.
     *
     * @param dataLine the line of the students info and grades
     * @param studentNames the list of current students already read in
     * @return the name of the student
     */
    @Override
    public String makeFullName(List<String> dataLine, List<String> studentNames) {
        return dataLine.get(0);
    }
}
