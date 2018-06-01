package edu.ithaca.dragon.tecmap.ui.springbootui.controller;

import edu.ithaca.dragon.tecmap.io.record.CohortConceptGraphsRecord;
import edu.ithaca.dragon.tecmap.io.record.ConceptGraphRecord;
import edu.ithaca.dragon.tecmap.io.record.LearningResourceRecord;
import edu.ithaca.dragon.tecmap.ui.springbootui.service.TecmapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/map")
public class TecmapController {

    private TecmapService tecmapService;

    @Autowired
    public TecmapController(TecmapService tecmapService) {
        super();
        this.tecmapService = tecmapService;
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
