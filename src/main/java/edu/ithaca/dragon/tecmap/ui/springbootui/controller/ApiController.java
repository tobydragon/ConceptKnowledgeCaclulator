package edu.ithaca.dragon.tecmap.ui.springbootui.controller;

import edu.ithaca.dragon.tecmap.io.record.CohortConceptGraphsRecord;
import edu.ithaca.dragon.tecmap.io.record.ConceptGraphRecord;
import edu.ithaca.dragon.tecmap.io.record.LearningResourceRecord;
import edu.ithaca.dragon.tecmap.suggester.OrganizedLearningResourceSuggestions;
import edu.ithaca.dragon.tecmap.ui.TecmapUserAction;
import edu.ithaca.dragon.tecmap.ui.springbootui.service.TecmapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
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

    @PostMapping("/connectResources/{courseId}")
    public void postConnectedResources(@PathVariable("courseId") String courseId, @RequestBody List<LearningResourceRecord> filledLearningResourceRecords, HttpServletResponse response) {
        System.out.println(filledLearningResourceRecords);
        response.setStatus(200);
    }

    @GetMapping("/cohortTree/{courseId}")
    public CohortConceptGraphsRecord getcohortTree(@PathVariable("courseId") String courseId) {
        return tecmapService.retrieveCohortTree(courseId);
    }

    @GetMapping("/suggestConcepts/{courseId}/{userId}")
    public List<String> getConceptSuggestionsForUser(@PathVariable("courseId") String courseId, @PathVariable("userId") String userId) {
        return tecmapService.retrieveConceptSuggestionsForUser(courseId, userId);
    }

    @GetMapping("/suggestResources/{courseId}/{userId}")
    public OrganizedLearningResourceSuggestions getResourceSuggestionsForUser(@PathVariable("courseId") String courseId, @PathVariable("userId") String userId) {
        return tecmapService.retrieveResourceSuggestionsForUser(courseId, userId);
    }

    @GetMapping("/suggestResources/{courseId}/{userId}/{conceptId}")
    public OrganizedLearningResourceSuggestions getResourceSuggestionsForSpecificConceptForUser(@PathVariable("courseId") String courseId, @PathVariable("userId") String userId, @PathVariable("conceptId") String conceptId) {
        return tecmapService.retrieveResourceSuggestionsForSpecificConceptForUser(courseId, userId, conceptId);
    }

    @GetMapping("/actions")
    public Map<String, List<TecmapUserAction>> getValidIdsAndActions() {
        return tecmapService.retrieveValidIdsAndActions();
    }
}
