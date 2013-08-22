package me.taborda.mashtv.controller ;

import java.util.ArrayList ;
import java.util.HashMap ;
import java.util.List ;
import java.util.Map ;

import org.slf4j.Logger ;
import org.slf4j.LoggerFactory ;
import org.springframework.beans.factory.annotation.Autowired ;
import org.springframework.stereotype.Controller ;
import org.springframework.web.bind.annotation.RequestMapping ;

import me.taborda.mashtv.model.Episode ;
import me.taborda.mashtv.model.Show ;
import me.taborda.mashtv.service.ShowService ;

@Controller
@RequestMapping({ "/latest", "/" })
public class LatestController {

    @SuppressWarnings("unused")
    private static final Logger LOG = LoggerFactory.getLogger(LatestController.class) ;

    @Autowired(required = true)
    public ShowService shows ;

    @RequestMapping(value = "")
    public String list(final Map<String, Object> map) {
        Map<Integer, List<Episode>> latest = new HashMap<>() ;
        List<Show> ss = shows.getShows() ;

        for (Show s : ss) {
            List<Episode> episodes = new ArrayList<>() ;
            episodes.addAll(shows.getLatestEpisodes(s, 3)) ;
            latest.put(s.getId(), episodes) ;
        }

        map.put("shows", ss) ;
        map.put("latest", latest) ;
        return "latest" ;
    }
}
