package edu.ithaca.dragon.tecmap.io.record;

/**
 * Created by Benjamin on 10/22/2018.
 */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public enum ContinuousMatrixRecordType {

    GRADES, FACTORS;

    public static Collection<ContinuousMatrixRecordType> getDefaultResourceTypes(){
        return new ArrayList<>(Arrays.asList(GRADES, FACTORS));
    }
}
