package edu.ithaca.dragon.tecmap.ui.springbootui.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/view")
public class WebController {

    public WebController() {
        super();
    }

    @GetMapping("/structureTree")
    public String viewStructureTree() {
        return "StructureGraph";
    }

    @GetMapping("/cohortTree")
    public String viewCohortTree() {
        return "CohortGraph";
    }
}
