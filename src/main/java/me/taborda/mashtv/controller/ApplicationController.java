package me.taborda.mashtv.controller;

import me.taborda.mashtv.model.Show;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ApplicationController {

    @RequestMapping("/feeds")
    public String feeds() {
        return "feeds";
    }

    @RequestMapping("/shows")
    public String shows() {
        return "shows";
    }

    @RequestMapping("/shows/{show}")
    public String showinfo(@ModelAttribute final Show show, final Model model) {
        model.addAttribute("show", show);
        return "showinfo";
    }
}
