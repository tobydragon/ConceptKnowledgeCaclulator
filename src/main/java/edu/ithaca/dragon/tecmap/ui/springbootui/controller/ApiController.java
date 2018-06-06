package edu.ithaca.dragon.tecmap.ui.springbootui.controller;

import edu.ithaca.dragon.tecmap.TecmapAction;
import edu.ithaca.dragon.tecmap.io.record.CohortConceptGraphsRecord;
import edu.ithaca.dragon.tecmap.io.record.ConceptGraphRecord;
import edu.ithaca.dragon.tecmap.io.record.LearningResourceRecord;
import edu.ithaca.dragon.tecmap.ui.springbootui.service.TecmapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiController {

    private TecmapService tecmapService;

    @Autowired
    public ApiController(TecmapService tecmapService) {
        super();
        this.tecmapService = tecmapService;
    }

    @GetMapping("/structureTree/{courseId}")
    public ConceptGraphRecord getStructureTree(@PathVariable("courseId") String courseId) {
        return tecmapService.retrieveStructureTree(courseId);
    }

    @GetMapping("/conceptList/{courseId}")
    public List<String> getConceptIdList(@PathVariable("courseId") String courseId) {
        return tecmapService.retrieveConceptIdList(courseId);
    }

    @GetMapping("/blankLRRecords/{courseId}")
    public List<LearningResourceRecord> getBlankLearningResourceRecordsFromAssessment(@PathVariable("courseId") String courseId) {
        return tecmapService.retrieveBlankLearningResourceRecordsFromAssessment(courseId);
    }

    @GetMapping("/cohortTree/{courseId}")
    public CohortConceptGraphsRecord getcohortTree(@PathVariable("courseId") String courseId) {
        return tecmapService.retrieveCohortTree(courseId);
    }

    @GetMapping("/actions")
    public Map<String, List<TecmapAction>> getValidIdsAndActions() {
        return tecmapService.retrieveValidIdsAndActions();
    }
}
