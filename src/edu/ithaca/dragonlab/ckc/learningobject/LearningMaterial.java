package edu.ithaca.dragonlab.ckc.learningobject;

import edu.ithaca.dragonlab.ckc.io.LearningResourceRecord;

import java.util.Arrays;
import java.util.Collection;

public class LearningMaterial extends LearningResource {

    public LearningMaterial(String id, Collection<Type> types){
        super(id, types);
    }

    public LearningMaterial(String id, Type type){
        super(id, Arrays.asList(type));
    }

    public LearningMaterial(LearningResourceRecord record){
        super(record.getResourceId(), record.getResourceTypes());
    }

}
