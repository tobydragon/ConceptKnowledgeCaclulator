package edu.ithaca.dragonlab.ckc.io;

import edu.ithaca.dragonlab.ckc.learningobject.LearningMaterial;
import edu.ithaca.dragonlab.ckc.learningobject.LearningObject;
import edu.ithaca.dragonlab.ckc.learningobject.LearningResource;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class LearningResourceRecordTest {


    @Test
    public void testBuildingResourcesFromRecords(){
        Collection<LearningObject> assessments = new ArrayList<>();
        Collection<LearningMaterial> materials = new ArrayList<>();
        for (LearningResourceRecord record : buildTestInstances()){
            //may create duplicate records if one resource is both an assessment and a material
            if (record.isType(LearningResource.Type.ASSESSMENT)){
                assessments.add(new LearningObject(record));
            }
            if (record.isType(LearningResource.Type.INFORMATION) || record.isType(LearningResource.Type.PRACTICE)){
                //since we've already added an assessment for this record, remove it so the list can be used to create the material directly from the list
                record.getResourceTypes().remove(LearningResource.Type.ASSESSMENT);
                materials.add(new LearningMaterial(record));
            }
        }
        Assert.assertEquals(2, assessments.size());
        Assert.assertEquals(3, materials.size());
    }

    public static Collection<LearningResourceRecord> buildTestInstances(){
        Collection<LearningResourceRecord> list = new ArrayList<>();

        list.add( new LearningResourceRecord(
                "variableHiddenQuizQuestion",
                Arrays.asList(LearningResource.Type.ASSESSMENT),
                Arrays.asList("Variables", "Assignments"),
                1,
                1
        ));

        list.add( new LearningResourceRecord(
                "reassignReturnedQuizQuestion",
                Arrays.asList(LearningResource.Type.ASSESSMENT, LearningResource.Type.PRACTICE),
                Arrays.asList("Variables", "Assignments"),
                1,
                1
        ));

        list.add( new LearningResourceRecord(
                "VariablesChapter",
                Arrays.asList(LearningResource.Type.INFORMATION),
                Arrays.asList("Variables", "Assignments"),
                1,
                1
        ));

        list.add( new LearningResourceRecord(
                "reassignQuizQuestion",
                Arrays.asList(LearningResource.Type.INFORMATION, LearningResource.Type.PRACTICE),
                Arrays.asList("Variables", "Assignments"),
                1,
                1
        ));

        return list;
    }

    public static void main(String[] args){
        Collection<LearningResourceRecord> toWrite = buildTestInstances();
        try {
            LearningResourceRecord.resourceRecordsToJSON(toWrite, "test/testresources/AutoCreated/LearningRecordResourceTest.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
