package edu.ithaca.dragon.tecmap.ui.springbootui.controller;

import edu.ithaca.dragon.tecmap.TecmapAPI;
import edu.ithaca.dragon.tecmap.io.record.CohortConceptGraphsRecord;
import edu.ithaca.dragon.tecmap.io.record.ConceptGraphRecord;
import edu.ithaca.dragon.tecmap.io.record.LearningResourceRecord;
import edu.ithaca.dragon.tecmap.ui.springbootui.service.TecmapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TecmapController {

    private TecmapService tecmapService;

    @Autowired
    public TecmapController(TecmapService tecmapService) {
        super();
        this.tecmapService = tecmapService;
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<Void> selectTecmap(@PathVariable("courseId") String courseId) {
        TecmapAPI tecmap = tecmapService.chooseTecmap(courseId);
        if (tecmap == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok().build();
        }
    }

    @GetMapping("/structureTree")
    public ConceptGraphRecord getStructureTree() {
        return tecmapService.retrieveStructureTree();
    }

    @GetMapping("/conceptList")
    public List<String> getConceptIdList() {
        return tecmapService.retrieveConceptIdList();
    }

    @GetMapping("/blankLRRecords")
    public List<LearningResourceRecord> getBlankLearningResourceRecordsFromAssessment() {
        return tecmapService.retrieveBlankLearningResourceRecordsFromAssessment();
    }

    @GetMapping("/cohortTree")
    public CohortConceptGraphsRecord getcohortTree() {
        return tecmapService.retrieveCohortTree();
    }
}
