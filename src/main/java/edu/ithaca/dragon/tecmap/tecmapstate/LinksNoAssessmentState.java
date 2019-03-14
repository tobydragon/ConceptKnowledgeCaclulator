package edu.ithaca.dragon.tecmap.tecmapstate;

import edu.ithaca.dragon.tecmap.conceptgraph.ConceptGraph;
import edu.ithaca.dragon.tecmap.io.record.LearningResourceRecord;

import java.util.List;

public class LinksNoAssessmentState extends OnlyGraphStructureState {

    private List<LearningResourceRecord> links;

    public LinksNoAssessmentState(ConceptGraph structureGraph, List<LearningResourceRecord> links) {
        super(structureGraph);
        this.links = links;
        structureGraph.addLearningResourcesFromRecords(links);
    }

    public List<LearningResourceRecord> getResourceRecordLinks() {
        return links;
    }
}
