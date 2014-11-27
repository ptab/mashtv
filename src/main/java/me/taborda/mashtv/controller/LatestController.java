package me.taborda.mashtv.controller ;

import java.util.HashMap ;
import java.util.List ;
import java.util.Map ;

import org.slf4j.Logger ;
import org.slf4j.LoggerFactory ;
import org.springframework.beans.factory.annotation.Autowired ;
import org.springframework.stereotype.Controller ;
import org.springframework.web.bind.annotation.RequestMapping ;
import org.springframework.web.bind.annotation.ResponseBody ;

import me.taborda.mashtv.model.Episode ;
import me.taborda.mashtv.model.Show ;
import me.taborda.mashtv.service.EpisodeService ;
import me.taborda.mashtv.service.ShowService ;

@Controller
@RequestMapping("/latest")
public class LatestController {

    @SuppressWarnings("unused")
    private static final Logger LOG = LoggerFactory.getLogger(LatestController.class) ;

    @Autowired(required = true)
    public ShowService shows ;

    @Autowired(required = true)
    public EpisodeService episodes ;

    @RequestMapping("")
    @ResponseBody
    public Map<Show, List<Episode>> list() {
        Map<Show, List<Episode>> latest = new HashMap<>() ;
        // FIXME query in loop is a no-no
        for (Show s : shows.findAll()) {
            latest.put(s, episodes.findLatest(s)) ;
        }
        return latest ;
    }
}
