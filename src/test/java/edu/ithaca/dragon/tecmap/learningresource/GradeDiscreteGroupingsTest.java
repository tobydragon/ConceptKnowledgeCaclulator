package edu.ithaca.dragon.tecmap.learningresource;

import edu.ithaca.dragon.tecmap.Settings;
import org.junit.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GradeDiscreteGroupingsTest {

    @Test
    public void buildGroupsFromJson() throws IOException {
        GradeDiscreteGroupings discreteGroupings = GradeDiscreteGroupings.buildFromJson(Settings.DEFAULT_TEST_PREDICTION_PATH + "discreteGroupings.json");

        assertNotNull(discreteGroupings);
        assertEquals(5, discreteGroupings.getGroups().size());
        assertEquals(4, discreteGroupings.getPointBreaks().size());
    }

}
