package edu.ithaca.dragon.tecmap.learningresource;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class GradeDiscreteGroupings {

    private List<String> groups;
    private List<Integer> pointBreaks;

    public GradeDiscreteGroupings() {
        groups = null;
        pointBreaks = null;
    }

    public static GradeDiscreteGroupings buildFromJson(String filename) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        GradeDiscreteGroupings discreteGroupings = mapper.readValue(new File(filename), GradeDiscreteGroupings.class);
        return discreteGroupings;
    }

    public List<String> getGroups() {
        return groups;
    }

    public List<Integer> getPointBreaks() {
        return pointBreaks;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }

    public void setPointBreaks(List<Integer> pointBreaks) {
        this.pointBreaks = pointBreaks;
    }
}
