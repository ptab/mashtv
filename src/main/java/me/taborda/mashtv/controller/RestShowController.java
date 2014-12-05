package me.taborda.mashtv.controller ;

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
@RequestMapping("/api/shows")
public class RestShowController extends RestBaseController {

    private static final Logger LOG = LoggerFactory.getLogger(RestShowController.class) ;

    @Autowired
    private ShowService shows ;

    @Autowired
    private EpisodeService episodes ;

    @RequestMapping("")
    public List<Show> list() {
        return shows.findAll() ;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Show add(@NotNull final String title) {
        Show show = shows.add(title) ;
        LOG.info("Added TV Show: " + show.getTitle()) ;
        return show ;
    }

    @RequestMapping("/{id}")
    public Show getShow(@PathVariable final long id) {
        return shows.find(id) ;
    }

    @RequestMapping(value = "/{id}/delete", method = RequestMethod.DELETE)
    public void delete(@PathVariable final long id) {
        Show show = shows.find(id) ;
        shows.delete(show) ;
        LOG.info("Removed TV Show: {}", show) ;
    }

    @RequestMapping("/{id}/episodes")
    public List<Episode> listEpisodes(@PathVariable final long id) {
        Show show = shows.find(id) ;
        return show.getEpisodes() ;
    }

    @RequestMapping("/{id}/episodes/{season}/{episode}")
    public Episode getEpisode(@PathVariable final long id, @PathVariable final Integer season, @PathVariable final Integer episode) {
        Show show = shows.find(id) ;
        return findEpisode(show, season, episode) ;
    }

    @RequestMapping("/{id}/episodes/{season}/{episode}/torrents")
    public Set<MagnetLink> torrents(@ModelAttribute final Show show, @PathVariable final Integer season, @PathVariable final Integer episode) {
        Episode e = findEpisode(show, season, episode) ;
        return e.getMagnetLinks() ;
    }

    @RequestMapping(value = "/{id}/episodes/{season}/{episode}/delete", method = RequestMethod.DELETE)
    public void delete(@ModelAttribute final Show show, @PathVariable final Integer season, @PathVariable final Integer episode) {
        Episode e = findEpisode(show, season, episode) ;
        episodes.delete(e) ;
        LOG.info("Removed episode: {}", e) ;
    }

    private Episode findEpisode(final Show show, final int season, final int episode) {
        Episode ep = show.getEpisode(season, episode) ;
        if (ep == null) {
            LOG.error("No episode on {} with season {} and episode {}", show, season, episode) ;
            throw new RuntimeException(String.format("No episode on %s with season %d and episode %d", show, season, episode)) ;
        }

        return ep ;
    }

}
