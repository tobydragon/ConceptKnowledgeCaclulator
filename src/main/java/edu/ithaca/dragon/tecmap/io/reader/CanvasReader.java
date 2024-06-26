package edu.ithaca.dragon.tecmap.io.reader;

import java.io.IOException;
import java.util.List;

public class CanvasReader extends TecmapCSVReader {
    public CanvasReader(String filename)throws IOException {
        super(filename, 2);
    }

    /**
     * This function takes in a line that holds all the info for a specific student and returns the
     * student name.
     *
     * @param dataLine - the line of the students info and grades
     * @param studentNames - the list of current students already read in
     * @return the name of the student
     */
    @Override
    public String makeFullName(List<String> dataLine, List<String> studentNames) {
        return dataLine.get(0);
    }
}
