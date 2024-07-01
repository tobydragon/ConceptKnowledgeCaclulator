package edu.ithaca.dragon.tecmap.io.reader;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CsvRepresentation {

    public static List<String[]> parseRowsFromFile(String filename) throws IOException, CsvException {
        List<String[]> list = new ArrayList<>();
        try {
            Reader reader = new BufferedReader(new FileReader(filename));
            CSVReader csvReader = new CSVReader(reader);
            String[] nextLine;
            while ((nextLine = csvReader.readNext()) != null) {
                for (int i = 0; i < nextLine.length; i++) {
                    nextLine[i] = nextLine[i].replace("\uFEFF", ""); // Remove ZWNBSP
                }
                list.add(nextLine);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void writeRowsToFile(List<String[]> rows, String fileToWrite)throws IOException{
        CSVWriter writer = new CSVWriter(new FileWriter(fileToWrite));
        writer.writeAll(rows,false);
        writer.close();
    }

}
