package edu.ithaca.dragon.tecmap.ui.springbootui.controller;

import edu.ithaca.dragon.tecmap.ui.springbootui.service.TecmapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/map")
public class TecmapController {

    private TecmapService tecmapService;

    @Autowired
    public TecmapController(TecmapService tecmapService) {
        super();
        this.tecmapService = tecmapService;
    }
}
