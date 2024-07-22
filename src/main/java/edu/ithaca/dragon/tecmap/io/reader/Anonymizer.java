package edu.ithaca.dragon.tecmap.io.reader;

import edu.ithaca.dragon.tecmap.io.Json;

import java.io.IOException;
import java.util.*;

public class Anonymizer implements CsvProcessor{

    private int numToUseNext;
    private Map<String, String> realId2anonId;
    private Map<String, String> realname2anonName;
    private final int nameStartRowIndex;
    private final int nameColumnIndex;
    private final int idColumnIndex;

    // Default values
    private static final int DEFAULT_NAME_COLUMN_INDEX = 0; // canvas format
    private static final int DEFAULT_ID_COLUMN_INDEX = 1; // canvas format

    public Anonymizer(int nameStartRowIndex, int nameColumnIndex, int idColumnIndex){
        numToUseNext = 1;
        realId2anonId = new HashMap<>();
        realname2anonName = new HashMap<>();
        this.nameStartRowIndex = nameStartRowIndex;
        this.nameColumnIndex = nameColumnIndex;
        this.idColumnIndex = idColumnIndex;
    }

    public Anonymizer(int nameStartRowIndex) {
        this(nameStartRowIndex, DEFAULT_NAME_COLUMN_INDEX, DEFAULT_ID_COLUMN_INDEX);
    }

    public static Anonymizer AnonymizerCreator(String filepathAndName, int nameStartRowIndex, int nameColumnIndex, int idColumnIndex) {
        try {
            return Json.fromJsonFile(filepathAndName, Anonymizer.class);
        } catch (IOException e) {
            return new Anonymizer(nameStartRowIndex, nameColumnIndex, idColumnIndex);
        }
    }

    public static Anonymizer AnonymizerCreator(String filepathAndName, int gradeStartIndex) {
        try {
            return Json.fromJsonFile(filepathAndName, Anonymizer.class);
        } catch (IOException e) {
            return new Anonymizer(gradeStartIndex);
        }
    }

    public void anonymize(List<String[]> rows) {
        Collections.shuffle(rows.subList(nameStartRowIndex, rows.size()));
        for(String[] row : rows.subList(nameStartRowIndex, rows.size())){
            if (row.length > 1) {
                row[nameColumnIndex] = getAnonStr(row[0], realname2anonName, "student", numToUseNext);
                row[idColumnIndex] = getAnonStr(row[1], realId2anonId, "s", numToUseNext);
                numToUseNext++;
            }
        }
    }

    public static String getAnonStr(String str, Map<String, String> real2anon, String prefixForNewStrs, int numToUseNext){
        String anon = real2anon.get(str);
        if (anon == null){
            anon = prefixForNewStrs+numToUseNext;
            real2anon.put(str, anon);
            numToUseNext++;
        }
        return anon;
    }

    @Override
    public boolean shouldProcessFile(String filename) {
        return filename.contains("removeNames");
    }

    @Override
    public void processRows(List<String[]> rows) {
        anonymize(rows);
    }

    @Override
    public String createProcessedFilename(String origFilename) {
        return origFilename.replace("removeNames", "anon");
    }

    @Override
    public void writeToFile(String filepath) throws IOException {
        Json.toJsonFile(filepath+this.getClass().getSimpleName()+".json", this);
    }

    public int getNameStartRowIndex() { return nameStartRowIndex; }

    public int getNumToUseNext() {
        return numToUseNext;
    }

    public void setNumToUseNext(int numToUseNext) {
        this.numToUseNext = numToUseNext;
    }

    public Map<String, String> getRealId2anonId() {
        return realId2anonId;
    }

    public void setRealId2anonId(Map<String, String> realId2anonId) {
        this.realId2anonId = realId2anonId;
    }

    public Map<String, String> getRealname2anonName() {
        return realname2anonName;
    }

    public void setRealname2anonName(Map<String, String> realname2anonName) {
        this.realname2anonName = realname2anonName;
    }


}
