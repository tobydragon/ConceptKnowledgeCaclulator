package edu.ithaca.dragonlab.ckc.io;

import edu.ithaca.dragonlab.ckc.conceptgraph.ConceptNode;

/**
 * @author tdragon
 *         2/20/17
 *         This class represents all the information that should be recorded about a concept when serializing
 */
public class ConceptRecord {

    //TODO: ConceptGraphRecord should hold these instead of actual nodes, so we know what is being held and what not...
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
        this.knowledgePrediction = conceptToRecord.getKnowledgeEstimate();
        this.knowledgeDistFromAvg = conceptToRecord.getKnowledgeDistanceFromAvg();
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
}
