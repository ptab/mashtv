package me.taborda.mashtv.controller ;

import org.springframework.stereotype.Controller ;
import org.springframework.ui.Model ;
import org.springframework.web.bind.annotation.PathVariable ;
import org.springframework.web.bind.annotation.RequestMapping ;

@Controller
public class HtmlController {

    @RequestMapping("/feeds")
    public String feeds() {
        return "feeds" ;
    }

    @RequestMapping("/shows")
    public String shows() {
        return "shows" ;
    }

    @RequestMapping("/shows/{id}")
    public String showinfo(@PathVariable final int id, final Model model) {
        model.addAttribute("showId", id) ;
        return "showinfo" ;
    }
}
