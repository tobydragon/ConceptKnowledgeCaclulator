package edu.ithaca.dragon.tecmap.tecmapstate;

import edu.ithaca.dragon.tecmap.conceptgraph.CohortConceptGraphs;
import edu.ithaca.dragon.tecmap.conceptgraph.ConceptGraph;
import edu.ithaca.dragon.tecmap.io.record.CohortConceptGraphsRecord;
import edu.ithaca.dragon.tecmap.io.record.LearningResourceRecord;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AssessmentConnectedState extends AssessmentAddedState {

    protected CohortConceptGraphs cohortConceptGraphs;
    private List<LearningResourceRecord> links;

    public AssessmentConnectedState(String structureFile, List<String> resourceConnectionFiles, List<String> assessmentFiles) throws IOException {
        super(structureFile, assessmentFiles);
        links = LearningResourceRecord.createLinksFromResourceFiles(resourceConnectionFiles);
        graph.addLearningResourcesFromRecords(links);
        cohortConceptGraphs = new CohortConceptGraphs(graph, assessmentItemResponses);
    }

    public CohortConceptGraphsRecord createCohortTree(){
        return cohortConceptGraphs.buildCohortConceptTreeRecord();
    }

    public ConceptGraph getGraphForUser(String userId){
        return cohortConceptGraphs.getUserGraph(userId);
    }

}
