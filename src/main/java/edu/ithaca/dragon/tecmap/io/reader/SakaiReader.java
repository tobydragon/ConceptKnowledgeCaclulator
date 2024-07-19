package edu.ithaca.dragon.tecmap.io.reader;

import com.opencsv.exceptions.CsvException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * *This is tha child class for sakai files, with its specific function for reading in the student names
 * it will call super to the parent class, TecmapCSVReader, and pass the filename and the specific gradeColumnIndexMark.
 * Created by Ryan on 11/9/2017.
 */
public class SakaiReader extends TecmapCSVReader {
    public SakaiReader(List<String[]> rows, List<CsvProcessor> processors) throws IOException, CsvException {
        super(rows, processors);
    }

    /**
     * This function takes in a line that holds all the info for a specific student and returns the student ID.
     * @param dataLine - the line of the students info and grades
     * @return the ID of the student
     */
    @Override
    public String getIDForAStudent(String[] dataLine) {
        return dataLine[0];
    }

    /**
     * This function takes in a line that holds all the info for a specific student and returns the student name.
     * @param dataLine - the line of the students info and grades
     * @return the name of the student
     */
    @Override
    public String getNameForAStudent(String[] dataLine) {
        return dataLine[1]; // 1
    }
}
