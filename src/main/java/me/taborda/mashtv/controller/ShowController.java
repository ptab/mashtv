package me.taborda.mashtv.controller ;

import java.io.FileNotFoundException ;
import java.io.FileOutputStream ;
import java.io.IOException ;
import java.net.URL ;
import java.nio.channels.Channels ;
import java.nio.channels.ReadableByteChannel ;
import java.util.List ;
import java.util.Set ;

import javax.validation.constraints.NotNull ;

import org.slf4j.Logger ;
import org.slf4j.LoggerFactory ;
import org.springframework.beans.factory.annotation.Autowired ;
import org.springframework.stereotype.Controller ;
import org.springframework.web.bind.annotation.ModelAttribute ;
import org.springframework.web.bind.annotation.PathVariable ;
import org.springframework.web.bind.annotation.RequestMapping ;
import org.springframework.web.bind.annotation.RequestMethod ;
import org.springframework.web.bind.annotation.ResponseBody ;

import me.taborda.mashtv.model.Episode ;
import me.taborda.mashtv.model.MagnetLink ;
import me.taborda.mashtv.model.Show ;
import me.taborda.mashtv.service.EpisodeService ;
import me.taborda.mashtv.service.ShowService ;

@Controller
@RequestMapping("/shows")
public class ShowController extends BaseController {

    private static final Logger LOG = LoggerFactory.getLogger(ShowController.class) ;

    @Autowired
    private ShowService shows ;

    @Autowired
    private EpisodeService episodes ;

    @RequestMapping("")
    @ResponseBody
    public List<Show> list() {
        return shows.findAll() ;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public void add(@NotNull final String title) {
        Show show = new Show(title) ;
        shows.save(show) ;
        LOG.info("Added TV Show: " + show.getTitle()) ;
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public void delete(@PathVariable final long id) {
        Show show = shows.find(id) ;
        shows.delete(show) ;
        LOG.info("Removed TV Show: {}", show) ;
    }

    @RequestMapping("/{id}")
    @ResponseBody
    public Show getShow(@PathVariable final long id) {
        return shows.find(id) ;
    }

    @RequestMapping("/{id}/episodes")
    @ResponseBody
    public List<Episode> listEpisodes(@PathVariable final long id) {
        Show show = shows.find(id) ;
        return show.getEpisodes() ;
    }

    @RequestMapping("/{id}/episodes/{season}/{episode}")
    @ResponseBody
    public Episode getEpisode(@PathVariable final long id, @PathVariable final Integer season, @PathVariable final Integer episode) {
        Show show = shows.find(id) ;
        return findEpisode(show, season, episode) ;
    }

    @RequestMapping(value = "/{id}/toggle/{season}/{episode}", method = RequestMethod.POST)
    @ResponseBody
    public void toggleWatched(@PathVariable final long id, @PathVariable final Integer season, @PathVariable final Integer episode) {
        Show show = shows.find(id) ;

        Episode e = findEpisode(show, season, episode) ;
        e.setDownloaded(!e.isDownloaded()) ;
        episodes.save(e) ;
        LOG.info("{} was set as {}", episode, e.isDownloaded() ? "downloaded" : "not downloaded") ;
    }

    @RequestMapping("/{id}/download/{season}/{episode}/{torrent}")
    @ResponseBody
    public void download(@PathVariable final long id, @PathVariable final Integer season, @PathVariable final Integer episode, @PathVariable final Integer torrent) throws FileNotFoundException,
    IOException {
        Show show = shows.find(id) ;
        Episode e = findEpisode(show, season, episode) ;
        MagnetLink t = e.getMagnetLink(torrent) ;

        URL tor = new URL(t.getUrl()) ;
        try (FileOutputStream fos = new FileOutputStream("/home/tab/testes/" + e.toShortString() + ".torrent"); ReadableByteChannel rbc = Channels.newChannel(tor.openStream())) {
            fos.getChannel().transferFrom(rbc, 0, 1 << 24) ;
        }

        e.setDownloaded(true) ;
        episodes.save(e) ;
        LOG.info("{} downloaded!", e) ;
    }

    @RequestMapping("/{id}/torrents/{season}/{episode}")
    @ResponseBody
    public Set<MagnetLink> torrents(@ModelAttribute final Show show, @PathVariable final Integer season, @PathVariable final Integer episode) {
        Episode e = findEpisode(show, season, episode) ;
        return e.getMagnetLinks() ;
    }

    @RequestMapping(value = "/{id}/delete/{season}/{episode}", method = RequestMethod.DELETE)
    @ResponseBody
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
