package edu.ithaca.dragon.tecmap.ui.springbootui.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {
    private static final String PATH = "/error";

    public ErrorController() {
        super();
    }

    @RequestMapping(PATH)
    public String redirect404() {
        return "404";
    }

    public String getErrorPath() {
        return PATH;
    }
}