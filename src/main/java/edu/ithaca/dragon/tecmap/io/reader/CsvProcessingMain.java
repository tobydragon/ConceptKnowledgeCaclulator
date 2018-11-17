package edu.ithaca.dragon.tecmap.io.reader;

import edu.ithaca.dragon.tecmap.io.Json;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvProcessingMain {

    private static void convertToTotalPointsAndAnonymize(){
        //create anonymizer, either new or from old file
        SakaiAnonymizer anonymizer;
        try {
            anonymizer = Json.fromJsonFile(
                    "src/main/resources/anonHere/anonymizer.json", SakaiAnonymizer.class);
        } catch (IOException e) {
            anonymizer = new SakaiAnonymizer();
        }
        //anonymize all files in folder
        try {
            for (String filename : allCsvFilesInDirectory("src/main/resources/anonHere/")) {
                if (!filename.contains("anon")) {
                    List<String[]> rows = CsvRepresentation.parseRowsFromFile("src/main/resources/anonHere/"+filename);
                    if (filename.contains("pointsOff")){
                        PointsOffConverter.convertFromPointsOffToTotalPoints(rows);
                        filename = filename.replace("pointsOff", "totalPoints");
                    }
                    anonymizer.anonymize(rows);
                    CsvRepresentation.writeRowsToFile(rows, "src/main/resources/anonHere/"+filename.substring(0, filename.length() - 4) + "-anon.csv");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //write new anonymizer file
        try {
            Json.toJsonFile("src/main/resources/anonHere/anonymizer.json", anonymizer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        convertToTotalPointsAndAnonymize();
    }

    private static List<String> allCsvFilesInDirectory(String directory) {
        List<String> textFiles = new ArrayList<>();
        File dir = new File(directory);
        for (File file : dir.listFiles()) {
            if (file.getName().toLowerCase().endsWith((".csv"))) {
                textFiles.add(file.getName());
            }
        }
        return textFiles;
    }
}
