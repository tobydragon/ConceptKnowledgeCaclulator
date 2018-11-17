package edu.ithaca.dragon.tecmap.io.reader;

import edu.ithaca.dragon.tecmap.io.Json;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class SakaiAnonymizer {

    private int numToUseNext;
    private Map<String, String> realId2anonId;
    private Map<String, String> realname2anonName;

    public SakaiAnonymizer(){
        numToUseNext =1;
        realId2anonId = new HashMap<>();
        realname2anonName = new HashMap<>();
    }

    public void anonymize(List<String[]> rows) throws IOException {
        Collections.shuffle(rows.subList(1, rows.size()));
        for(String[] row : rows.subList(1, rows.size())){
            if (row.length >1) {
                row[0] = getAnonStr(row[0], realId2anonId, "s", numToUseNext);
                row[1] = getAnonStr(row[1], realname2anonName, "student", numToUseNext);
                numToUseNext++;
            }
        }
    }

    static String getAnonStr(String str, Map<String, String> real2anon, String prefixForNewStrs, int numToUseNext){
        String anon = real2anon.get(str);
        if (anon == null){
            anon = prefixForNewStrs+numToUseNext;
            real2anon.put(str, anon);
            numToUseNext++;
        }
        return anon;
    }

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
