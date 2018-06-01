package edu.ithaca.dragon.tecmap.tecmapstate;

import edu.ithaca.dragon.tecmap.conceptgraph.CohortConceptGraphs;
import edu.ithaca.dragon.tecmap.io.record.CohortConceptGraphsRecord;
import edu.ithaca.dragon.tecmap.io.record.LearningResourceRecord;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AssessmentConnectedState extends AssessmentAddedState {

    CohortConceptGraphs cohortConceptGraphs;
    List<LearningResourceRecord> links;

    public AssessmentConnectedState(String structureFile, List<String> resourceConnectionFiles, List<String> assessmentFiles) throws IOException {
        super(structureFile, assessmentFiles);
        links = createLinksFromResourceFiles(resourceConnectionFiles);
        graph.addLearningResourcesFromRecords(links);
        cohortConceptGraphs = new CohortConceptGraphs(graph, assessmentItemResponses);
    }

    private static List<LearningResourceRecord> createLinksFromResourceFiles(List<String> resourceConnectionRecords) throws IOException {
        List<LearningResourceRecord> linkRecord = new ArrayList<>();
        for (String rFiles : resourceConnectionRecords){
            List<LearningResourceRecord> temp = LearningResourceRecord.buildListFromJson(rFiles);
            linkRecord.addAll(temp);
        }
        return linkRecord;
    }


    public CohortConceptGraphsRecord createCohortTree(){
        return cohortConceptGraphs.buildCohortConceptTreeRecord();
    }

}
