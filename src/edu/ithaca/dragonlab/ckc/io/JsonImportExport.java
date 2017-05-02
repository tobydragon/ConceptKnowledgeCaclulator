package edu.ithaca.dragonlab.ckc.io;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by willsuchanek on 4/18/17.
 */
public class JsonImportExport {

    public static List<LearningObjectLinkRecord> LOLRFromRecords (String fullFileName) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // Read in JSON and build nodes and edges lists (class)
        File test = new File(fullFileName);
        try {
            List<LearningObjectLinkRecord> LORLList = mapper.readValue(new File(fullFileName), new TypeReference<List<LearningObjectLinkRecord>>(){});
            return LORLList;
        }
        catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
