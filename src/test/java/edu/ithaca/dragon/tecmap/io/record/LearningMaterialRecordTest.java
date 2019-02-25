package edu.ithaca.dragon.tecmap.io.record;

import edu.ithaca.dragon.tecmap.learningresource.LearningMaterial;
import edu.ithaca.dragon.tecmap.learningresource.LearningResourceType;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static edu.ithaca.dragon.tecmap.io.record.LearningMaterialRecord.jsonToLearningMaterialRecords;
import static edu.ithaca.dragon.tecmap.io.record.LearningMaterialRecord.learningMaterialRecordsToJson;
import static org.junit.jupiter.api.Assertions.assertEquals;

class LearningMaterialRecordTest {

    private final String jsonFile = "src/test/resources/datastore/learningMaterialRecords.json";

    @Test
    void learningMaterialsToJsonTestAndReverse() throws IOException {

        List<LearningMaterialRecord> records = new ArrayList<>();
        List<LearningResourceType> type = new ArrayList<>();
        List<LearningResourceType> types = new ArrayList<>();

        type.add(LearningResourceType.INFORMATION);

        types.add(LearningResourceType.ASSESSMENT);
        types.add(LearningResourceType.INFORMATION);

        LearningMaterialRecord lmr = new LearningMaterialRecord("INFORMATION 1", type, "Test content one", Collections.singleton("Scales"), "");
        LearningMaterialRecord lmr2 = new LearningMaterialRecord("INFORMATION 1", type, "Test content one", Collections.singleton("Scales"), "");
        LearningMaterialRecord lmr3 = new LearningMaterialRecord("INFORMATION 2", types, "Test content one", Collections.singleton("Scales"), "www.google.com");

        records.add(lmr);
        records.add(lmr2);
        records.add(lmr3);

        learningMaterialRecordsToJson(records, jsonFile);

        assertEquals(lmr, jsonToLearningMaterialRecords(jsonFile).get(0));
        assertEquals(lmr, jsonToLearningMaterialRecords(jsonFile).get(1));
        assertEquals(false, lmr.equals(lmr3));

    }

    @Test
    void compareLearningMaterialToLearningMaterialRecord() {
        List<LearningResourceType> type = new ArrayList<>();
        type.add(LearningResourceType.INFORMATION);

        LearningMaterialRecord lmr = new LearningMaterialRecord("INFORMATION 1", type, "Test content one", Collections.singleton("Scales"), "");
        LearningMaterial lm = new LearningMaterial(lmr);

        assertEquals(lm.getLearningResourceType(), lmr.getTypes());
        assertEquals(lm.getContent(), lmr.getContent());
        assertEquals(lm.getId(), lmr.getId());
        assertEquals(lm.getTagsMap(), lmr.getTagsMap());
        assertEquals(lm.getUrl(), lmr.getUrl());

    }

    @Test
    void jsonToLearningMaterialRecordCs1Example() throws IOException {

        final String lmrjson = "src/test/resources/datastore/Cs1Example/Cs1ExampleLearningMaterial.json";

        String content0 = "What's are advantages of using while loops instead of for loops?";
        String content1 = "How many choices are possible when executing an if-statement?";
        String content2 = "When does an 'else' conditional get executed in a function?";
        String content3 = "https://www.learncpp.com/cpp-tutorial/52-if-statements/";
        String content4 = "https://www.programiz.com/c-programming/c-if-else-statement";
        String content5 = "https://programming.guide/go/for-loop.html";

        List<LearningMaterialRecord> records = jsonToLearningMaterialRecords(lmrjson);

        assertEquals(6, records.size(), "Could not find 6 LearningMaterialRecords");

        assertEquals(content0, records.get(0).getContent(), "Content of LearningMaterialRecord is incorrect");
        assertEquals(content1, records.get(1).getContent(), "Content of LearningMaterialRecord is incorrect");
        assertEquals(content2, records.get(2).getContent(), "Content of LearningMaterialRecord is incorrect");
        assertEquals(content3, records.get(3).getContent(), "Content of LearningMaterialRecord is incorrect");
        assertEquals(content4, records.get(4).getContent(), "Content of LearningMaterialRecord is incorrect");
        assertEquals(content5, records.get(5).getContent(), "Content of LearningMaterialRecord is incorrect");
    }

}
