package edu.ithaca.dragonlab.ckc.learningobject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class LearningResource {
    public enum Type {ASSESSMENT, INFORMATION, PRACTICE}

    public static Collection<Type> DEFAULT_RESOURCE_TYPES = Arrays.asList(Type.ASSESSMENT, Type.PRACTICE);

    private Collection<Type> types;
    String id;

    LearningResource(String id, Collection<Type> types){
        this.id = id;
        this.types = new ArrayList<>(types);
    }

    LearningResource(String id, Type type){
        this(id,  Arrays.asList(type));
    }

}
