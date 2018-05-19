package edu.ithaca.dragon.tecmap.learningresource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public enum LearningResourceType {

    ASSESSMENT, INFORMATION, PRACTICE;

    public static Collection<LearningResourceType> getDefaultResourceTypes(){
        return new ArrayList<>(Arrays.asList(ASSESSMENT, PRACTICE));
    }
}
