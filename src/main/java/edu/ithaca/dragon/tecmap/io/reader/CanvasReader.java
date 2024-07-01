package edu.ithaca.dragon.tecmap.io.reader;

import java.io.IOException;
import java.util.List;

public class CanvasReader extends TecmapCSVReader {
    public CanvasReader(String filename)throws IOException {
        super(filename, 4, 3);
    }

    /**
     * This function takes in a line that holds all the info for a specific student and returns the student ID.
     * @param dataLine - the line of the students info and grades
     * @return the ID of the student
     */
    @Override
    public String getIDForAStudent(List<String> dataLine) {
        return dataLine.get(1);
    }

    /**
     * This function takes in a line that holds all the info for a specific student and returns the student name.
     * @param dataLine - the line of the students info and grades
     * @return the name of the student
     */
    @Override
    public String getNameForAStudent(List<String> dataLine) {
        return dataLine.get(0);
    }
}
