package edu.ithaca.dragon.tecmap.io.reader;

import edu.ithaca.dragon.tecmap.io.Json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvProcessingMain {

    public static void main(String[] args){
        List<CsvProcessor> processors = new ArrayList<>();
        //processors.add(new PointsOffConverter());
        processors.add(SakaiAnonymizer.SakaiAnonymizerCreator("src/main/resources/anonHere/anonymizer.json"));

        for (CsvProcessor processor : processors){
            try {
                CsvProcessor.processFilesInDirectory(processor, "src/main/resources/anonHere/");
                processor.writeToFile("src/main/resources/anonHere/");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
