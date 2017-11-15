package edu.ithaca.dragonlab.ckc.io;

import java.io.IOException;
import java.util.List;
import java.util.StringJoiner;

/**
 * Created by Ryan on 10/4/2017.
 */

public class ZybooksReader extends CSVReader{
    public ZybooksReader(String filename)throws IOException{
        super(filename, 5);
    }

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
