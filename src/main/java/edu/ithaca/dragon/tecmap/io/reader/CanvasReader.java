package edu.ithaca.dragon.tecmap.io.reader;

import java.io.IOException;
import java.util.List;


public class CanvasReader extends TecmapCSVReader {

    public CanvasReader(List<String[]> rows, List<CsvProcessor> processors) throws IOException {
        super(rows, processors);
    }

    /**
     * This function takes in a line that holds all the info for a specific student and returns the student ID.
     * @param dataLine - the line of the students info and grades
     * @return the ID of the student
     */
    @Override
    public String getIDForAStudent(String[] dataLine) {
        return dataLine[1];
    }

    /**
     * This function takes in a line that holds all the info for a specific student and returns the student name.
     * @param dataLine - the line of the students info and grades
     * @return the name of the student
     */
    @Override
    public String getNameForAStudent(String[] dataLine) {
        return dataLine[0];
    }
}
