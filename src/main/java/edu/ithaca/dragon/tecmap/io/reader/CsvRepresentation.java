package edu.ithaca.dragon.tecmap.io.reader;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.*;
import java.util.List;

public class CsvRepresentation {

    public static List<String[]> parseRowsFromFile(String filename) throws IOException {
        Reader reader = new BufferedReader(new FileReader(filename));
        CSVReader csvReader = new CSVReader(reader);
        List<String[]> list = csvReader.readAll();
        reader.close();
        csvReader.close();
        return list;
    }

    public static void writeRowsToFile(List<String[]> rows, String fileToWrite)throws IOException{
        CSVWriter writer = new CSVWriter(new FileWriter(fileToWrite));
        writer.writeAll(rows,false);
        writer.close();
    }

}
