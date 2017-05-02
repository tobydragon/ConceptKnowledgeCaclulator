package edu.ithaca.dragonlab.ckc.io;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ithaca.dragonlab.ckc.util.TestUtil;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by willsuchanek on 4/24/17.
 */
public class JsonImportExportTest {
    @Test
    public void readSimpleFromFile(){
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);


        List<LearningObjectLinkRecord> list =JsonImportExport.LOLRFromRecords("test/testresources/recordsSimple.json");

        Assert.assertEquals(3, list.size());
        Assert.assertEquals("A", list.get(0).getLearningObject());
        Assert.assertEquals(3, list.get(0).getConceptIds().size());
        Assert.assertEquals("Q5", list.get(2).getConceptIds().get(0));
        Assert.assertEquals(2,list.get(0).getDataImportance(), TestUtil.OK_FLOAT_MARGIN);
        Assert.assertEquals(1,list.get(1).getDataImportance(),TestUtil.OK_FLOAT_MARGIN);
        Assert.assertEquals(.2,list.get(2).getDataImportance(),TestUtil.OK_FLOAT_MARGIN);


    }
}
