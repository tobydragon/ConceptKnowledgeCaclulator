package edu.ithaca.dragonlab.ckc.io;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import edu.ithaca.dragonlab.ckc.conceptgraph.CohortConceptGraphs;
import edu.ithaca.dragonlab.ckc.conceptgraph.ConceptGraph;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by home on 5/20/17.
 */
public class CohortConceptGraphsRecord {

    private List<ConceptGraphRecord> graphRecords;

    public void writeToJson(String filename) throws IOException{
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.writeValue(new File(filename), this);
    }


    public static CohortConceptGraphsRecord buildFromJson(String filename) throws IOException{
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        CohortConceptGraphsRecord graphRecords = mapper.readValue(new File(filename), CohortConceptGraphsRecord.class);
        return graphRecords;
    }

    public CohortConceptGraphsRecord(List<ConceptGraphRecord> graphRecords) {
        this.graphRecords = graphRecords;
    }

    //default constructor for jackson
    public CohortConceptGraphsRecord(){
        graphRecords = null;
    }

    public void setGraphRecords(List<ConceptGraphRecord> graphRecords) {
        this.graphRecords = graphRecords;
    }

    public List<ConceptGraphRecord> getGraphRecords() {
        return graphRecords;
    }
}
