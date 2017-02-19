package edu.ithaca.dragonlab.ckc.learningobject;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author tdragon
 *         2/19/17.
 */
public class LearningObjectResponseTest {


    @Test
    public void getUserResponseMap() throws Exception {
        List<LearningObjectResponse> simpleResponses = ExampleLearningObjectResponseFactory.makeSimpleResponses();
        Map<String, List<LearningObjectResponse>> userIdToResponseMap = LearningObjectResponse.getUserResponseMap(simpleResponses);

        Assert.assertEquals(3, userIdToResponseMap.size());
        Assert.assertEquals(6, userIdToResponseMap.get("student1").size());
        Assert.assertEquals(4, userIdToResponseMap.get("student3").size());
    }

}