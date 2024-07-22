package edu.ithaca.dragon.tecmap.io.reader;


import com.opencsv.exceptions.CsvException;
import edu.ithaca.dragon.tecmap.Settings;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class AnonymizerTest {

//    @Test
//    public void anonymizerToFileTest() throws IOException {
//        SakaiAnonymizer anonymizer = new SakaiAnonymizer();
//        anonymizer.anonymize(Settings.DEFAULT_TEST_DATASTORE_PATH+"Cs1Example/Cs1ExampleAssessment1.csv",
//                Settings.DEFAULT_TEST_DATASTORE_PATH+"autogenerated/SakaiAnonymizerTest-anonymizerToFileTest.csv");
//        Json.toJsonFile(Settings.DEFAULT_TEST_DATASTORE_PATH+"autogenerated/SakaiAnonymizerTest-anonymizerToFileTest.json", anonymizer);
//        SakaiAnonymizer anonymizerFromFile = Json.fromJsonFile(
//                Settings.DEFAULT_TEST_DATASTORE_PATH+"autogenerated/SakaiAnonymizerTest-anonymizerToFileTest.json", SakaiAnonymizer.class);
//    }

    @Test
    public void anonymizeTest() throws IOException , CsvException {
        List<String[]> newRows = CsvFileLibrary.parseRowsFromFile(Settings.DEFAULT_TEST_DATASTORE_PATH+"Cs1Example/Cs1ExampleAssessment1.csv");
        Anonymizer anonymizer = new Anonymizer(2, 1, 0);
        anonymizer.anonymize(newRows);

        //check labels werent disturbed
        assertEquals("student ID", newRows.get(0)[0]);
        assertEquals("Name", newRows.get(0)[1]);

        List<String[]> origRows = CsvFileLibrary.parseRowsFromFile(
                Settings.DEFAULT_TEST_DATASTORE_PATH + "Cs1Example/Cs1ExampleAssessment1.csv");

        Map<String, String> real2anonId = anonymizer.getRealId2anonId();
        //make sure look up of orig matches new for all numbers
        for(String[] origRow: origRows){
            for (String[] newRow : newRows) {
                if (newRow[0].equals(real2anonId.get(origRow[0]))){
                    for (int i=2; i<newRow.length; i++){ // check if the grades are matched
                        if (!newRow[i].equals(origRow[i])){
                            fail("not matching values for new row:"+ newRow[0] + " and orig row:" + origRow[0]);
                        }
                    }
                }
            }
        }
    }

    @Test
    public void getAnonStrTest(){
        Map<String, String> map = new HashMap<>();
        map.put("aReal", "aAnon");
        map.put("bReal", "bAnon");

        assertEquals("s1", Anonymizer.getAnonStr("cReal", map, "s", 1));
        assertEquals("bAnon", Anonymizer.getAnonStr("bReal", map, "s", 1));
        assertEquals("student2", Anonymizer.getAnonStr("dReal", map, "student", 2));
        assertEquals("aAnon", Anonymizer.getAnonStr("aReal", map, "s", 2));
        assertEquals("student2", map.get("dReal"));
        assertEquals("s1", map.get("cReal"));
    }



}