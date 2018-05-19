package edu.ithaca.dragon.tecmap.learningobject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class LearningResource {
    public enum Type {ASSESSMENT, INFORMATION, PRACTICE}

    public static Collection<Type> DEFAULT_RESOURCE_TYPES = new ArrayList<>(Arrays.asList(Type.ASSESSMENT, Type.PRACTICE));

    protected Collection<Type> types;
    private String id;

    LearningResource(String id, Collection<Type> types){
        this.id = id;
        this.types = types;
    }

    LearningResource(String id, Type type){
        this(id,  new ArrayList<>(Arrays.asList(type)));
    }

    public String getId() {
        return id;
    }
}
