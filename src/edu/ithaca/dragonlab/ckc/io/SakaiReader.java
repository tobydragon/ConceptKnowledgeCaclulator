package edu.ithaca.dragonlab.ckc.io;

import java.io.IOException;
import java.util.List;

/**
 * Created by Ryan on 11/9/2017.
 */
public class SakaiReader extends CSVReader{
    public SakaiReader(String filename)throws IOException{
        super(filename, 2);
    }

    @Override
    public String makeFullName(List<String> dataLine, List<String> studentNames) {
        return dataLine.get(0);
    }
}
