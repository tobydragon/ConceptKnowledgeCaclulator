package edu.ithaca.dragon.tecmap.learningresource;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

class LearningMaterialTest {

    @Test
    void addTagsMapTest() {

        List<String> tags = new ArrayList<>();
        tags.add("Memory");
        tags.add("Memory");
        tags.add("Pointers");
        List<LearningResourceType> type = new ArrayList<>();

        type.add(LearningResourceType.INFORMATION);

        LearningMaterial lm = new LearningMaterial("INFORMATION 1", type, "Test content one" , Collections.singleton("Scales"), "");

        lm.addTagsMap(tags);

        assertEquals((Integer)1, lm.getTagsMap().get("Pointers"));
        assertEquals((Integer)2, lm.getTagsMap().get("Memory"));
        assertEquals((Integer)1, lm.getTagsMap().get("Scales"));
    }
}