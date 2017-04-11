package edu.ithaca.dragonlab.ckc.io;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Created by willsuchanek on 4/10/17.
 */
public class LearningObjectLinkRecordTest {
    @Test
    public void addConceptIDTest(){
        ArrayList<String> concepts = new ArrayList<>();
        concepts.add("concept 1");
        String id = "id 1";
        LearningObjectLinkRecord loObject = new LearningObjectLinkRecord(id,concepts);
        Assert.assertEquals("concept 1",loObject.getConceptIds().get(0));
        Assert.assertEquals(1,loObject.getConceptIds().size());
        Assert.assertEquals("id 1", loObject.getLearningObject());
        loObject.addConceptId("concept 2");
        Assert.assertEquals("concept 2", loObject.getConceptIds().get(1));
        Assert.assertEquals(2,loObject.getConceptIds().size());
    }
    public void toStringTest(){
        ArrayList<String> concepts = new ArrayList<>();
        concepts.add("concept 1");
        concepts.add("concept 2");
        String id = "id 1";
        LearningObjectLinkRecord loObject = new LearningObjectLinkRecord(id,concepts);
        Assert.assertEquals("(Learning Object ID: id 1 Concept IDs: concept 1, concept 2)\n",loObject.toString());
    }
}
