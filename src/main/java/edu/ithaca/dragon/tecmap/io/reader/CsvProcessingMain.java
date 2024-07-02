package edu.ithaca.dragon.tecmap.io.reader;

import java.util.ArrayList;
import java.util.List;

public class CsvProcessingMain {

    public static void main(String[] args){
        List<CsvProcessor> processors = new ArrayList<>();
        //processors.add(new PointsOffConverter());
        processors.add(Anonymizer.AnonymizerCreator("src/main/resources/anonHere/anonymizer.json", 2));

        for (CsvProcessor processor : processors){
            try {
                CsvProcessor.processFilesInDirectory(processor, "src/main/resources/anonHere/");
                processor.writeToFile("src/main/resources/anonHere/");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
