package me.taborda.mashtv.api;

import java.util.List ;
import java.util.Set ;

import javax.validation.constraints.NotNull ;

import org.slf4j.Logger ;
import org.slf4j.LoggerFactory ;
import org.springframework.beans.factory.annotation.Autowired ;
import org.springframework.web.bind.annotation.ModelAttribute ;
import org.springframework.web.bind.annotation.PathVariable ;
import org.springframework.web.bind.annotation.RequestMapping ;
import org.springframework.web.bind.annotation.RequestMethod ;
import org.springframework.web.bind.annotation.RestController ;

import me.taborda.mashtv.model.Episode ;
import me.taborda.mashtv.model.MagnetLink ;
import me.taborda.mashtv.model.Show ;
import me.taborda.mashtv.service.EpisodeService ;
import me.taborda.mashtv.service.ShowService ;

@RestController
@RequestMapping(value = "/api/shows", produces = "application/json")
public class RestShowController extends RestBaseController {

    private static final Logger LOG = LoggerFactory.getLogger(RestShowController.class) ;

    @Autowired
    private ShowService shows ;

    @Autowired
    private EpisodeService episodes ;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<Show> list() {
        return shows.findAll() ;
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public Show add(@NotNull final String title) {
        Show show = shows.add(title) ;
        LOG.info("Added TV Show: " + show.getTitle()) ;
        return show ;
    }

    @RequestMapping(value = "/{show}", method = RequestMethod.GET)
    public Show getShow(@ModelAttribute final Show show) {
        return show ;
    }

    @RequestMapping(value = "/{show}", method = RequestMethod.DELETE)
    public void delete(@ModelAttribute final Show show) {
        shows.delete(show) ;
        LOG.info("Removed TV Show: {}", show) ;
    }

    @RequestMapping(value = "/{show}/{season}", method = RequestMethod.GET)
    public List<Episode> listEpisodes(@ModelAttribute final Show show, @PathVariable final Integer season) {
        return show.getEpisodes(season) ;
    }

    @RequestMapping(value = "/{show}/{season}/{episode}", method = RequestMethod.GET)
    public Episode getEpisode(@ModelAttribute final Show show, @PathVariable final Integer season, @PathVariable final Integer episode) {
        return show.getEpisode(season, episode) ;
    }

    @RequestMapping(value = "/{show}/{season}/{episode}", method = RequestMethod.DELETE)
    public void delete(@ModelAttribute final Show show, @PathVariable final Integer season, @PathVariable final Integer episode) {
        Episode e = show.getEpisode(season, episode) ;
        episodes.delete(e) ;
        LOG.info("Removed episode: {}", e) ;
    }

    @RequestMapping(value = "/{show}/{season}/{episode}/links", method = RequestMethod.GET)
    public Set<MagnetLink> torrents(@ModelAttribute final Show show, @PathVariable final Integer season, @PathVariable final Integer episode) {
        return show.getEpisode(season, episode).getMagnetLinks() ;
    }

}
