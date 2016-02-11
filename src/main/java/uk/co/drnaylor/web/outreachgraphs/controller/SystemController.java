package uk.co.drnaylor.web.outreachgraphs.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SystemController {

    @RequestMapping("/")
    public String getHome(Model model) {
        return "index";
    }

    @RequestMapping("/input")
    public String getInput(Model model) {
        return "control";
    }
}
