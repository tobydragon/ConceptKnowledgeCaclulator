package edu.ithaca.dragonlab.ckc.io;

import edu.ithaca.dragonlab.ckc.conceptgraph.ConceptNode;
import edu.ithaca.dragonlab.ckc.util.DataUtil;

/**
 * @author tdragon
 *         2/20/17
 *         This class represents all the information that should be recorded about a concept when serializing
 */
public class ConceptRecord {
    String id;
    String label;

    double knowledgeEstimate;
    double knowledgePrediction;
    double knowledgeDistFromAvg;

    public ConceptRecord() {
        this.id = null;
        this.label = null;
        this.knowledgeEstimate = 0;
        this.knowledgePrediction = 0;
        this.knowledgeDistFromAvg = 0;

    }

    public ConceptRecord(ConceptNode conceptToRecord) {
        this.id = conceptToRecord.getID();
        this.label = conceptToRecord.getLabel();
        this.knowledgeEstimate = conceptToRecord.getKnowledgeEstimate();
        this.knowledgePrediction = 0;
        this.knowledgeDistFromAvg = conceptToRecord.getKnowledgeDistanceFromAvg();
    }

    public ConceptRecord(String id) {
        this.id = id;
        this.label = id;
        this.knowledgeEstimate = 0;
        this.knowledgePrediction = 0;
        this.knowledgeDistFromAvg = 0;
    }

    public String getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public double getKnowledgeEstimate() {
        return knowledgeEstimate;
    }

    public double getKnowledgePrediction() {
        return knowledgePrediction;
    }

    public double getKnowledgeDistFromAvg() {
        return knowledgeDistFromAvg;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setKnowledgeEstimate(double knowledgeEstimate) {
        this.knowledgeEstimate = knowledgeEstimate;
    }

    public void setKnowledgePrediction(double knowledgePrediction) {
        this.knowledgePrediction = knowledgePrediction;
    }

    public void setKnowledgeDistFromAvg(double knowledgeDistFromAvg) {
        this.knowledgeDistFromAvg = knowledgeDistFromAvg;
    }

    public boolean equals(Object other){
        if(other == null){
            return false;
        }
        if(!ConceptRecord.class.isAssignableFrom(other.getClass())){
            return false;
        }
        ConceptRecord otherNode = (ConceptRecord) other;
        if(this.id.equals(otherNode.id) && DataUtil.equalsDoubles(this.knowledgeEstimate, otherNode.knowledgeEstimate)
                && DataUtil.equalsDoubles(this.knowledgeEstimate, otherNode.knowledgeEstimate)
                && DataUtil.equalsDoubles(this.knowledgeEstimate, otherNode.knowledgeEstimate)){
            return true;
        } else {
            return false;
        }
    }
}
