package edu.ithaca.dragon.tecmap.data;

import edu.ithaca.dragon.tecmap.tecmapstate.TecmapState;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TecmapFileDataTest {

    private TecmapFileData tecmapFileData;

    @Before
    public void setup() throws IOException {

        tecmapFileData = new TecmapFileData("Cs1ExampleAssessmentAdded", "src/test/resources/datastore/Cs1ExampleAssessmentAdded/Cs1ExampleGraph.json", new ArrayList<String>(), Arrays.asList("src/test/resources/datastore/Cs1ExampleAssessmentAdded/Cs1ExampleAssessment1.csv", "src/test/resources/datastore/Cs1ExampleAssessmentAdded/Cs1ExampleAssessment2.csv"));

    }

    @Test
    public void addResourceFiles() {
        assertEquals(TecmapState.assessmentAdded, tecmapFileData.getAvailableState());
        tecmapFileData.addResourceFiles("fakeFile");
        assertEquals(TecmapState.assessmentConnected, tecmapFileData.getAvailableState());
    }

}
