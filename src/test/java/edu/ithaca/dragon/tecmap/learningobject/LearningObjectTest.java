package edu.ithaca.dragon.tecmap.learningobject;

import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

public class LearningObjectTest {
    @Test
    public void lorEqualsTest(){
        LearningObjectResponse lor0 = new LearningObjectResponse("student1", "Q1", 1);
        LearningObjectResponse lor1 = new LearningObjectResponse("student1", "Q1", 1);
        LearningObjectResponse lor2 = new LearningObjectResponse("student1", "Q2", 1);
        LearningObjectResponse lor3 = new LearningObjectResponse("student2", "Q2", 1);
        LearningObjectResponse lor4 = new LearningObjectResponse("student3", "Q2", 0);

        Assert.assertEquals(true, lor0.equals(lor1));
        Assert.assertEquals(false, lor0.equals(lor2));
        Assert.assertEquals(false, lor0.equals(lor3));
        Assert.assertEquals(false, lor0.equals(lor4));
        Assert.assertEquals(false, lor3.equals(lor4));
    }

    @Test
    public void loEqualsTest(){
        LearningObject lo1 = new LearningObject("Q1");
        lo1.addResponse(new LearningObjectResponse("student1", "Q1", 1));
        lo1.addResponse(new LearningObjectResponse("student1", "Q1", 1));

        LearningObject lo2 = new LearningObject("Q1");
        lo2.addResponse(new LearningObjectResponse("student1", "Q1", 1));
        lo2.addResponse(new LearningObjectResponse("student1", "Q1", 1));

        LearningObject lo3 = new LearningObject("Q2");
        lo3.addResponse(new LearningObjectResponse("student1", "Q2", 1));
        lo3.addResponse(new LearningObjectResponse("student1", "Q2", 1));

        Assert.assertEquals(true, lo1.equals(lo2));
        Assert.assertEquals(false, lo1.equals(lo3));
    }

    @Test
    public void deepCopyLearningObjectMapTest(){
        Map<String, LearningObject> toCopy = ExampleLearningObjectResponseFactory.makeSimpleLearningObjectMap();
        Map<String, LearningObject> newMap = LearningObject.deepCopyLearningObjectMap(toCopy);

        for (LearningObject orig : toCopy.values()){
            LearningObject copy = newMap.get(orig.getId());
            Assert.assertEquals(orig, copy);
            Assert.assertFalse(orig == copy);
        }

        //probably redundant with above, but just being safe
        Assert.assertFalse(toCopy == newMap);
        Assert.assertEquals(toCopy, newMap);
    }

}
