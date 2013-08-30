package me.taborda.mashtv.controller ;

import java.io.FileNotFoundException ;
import java.io.FileOutputStream ;
import java.io.IOException ;
import java.net.URL ;
import java.nio.channels.Channels ;
import java.nio.channels.ReadableByteChannel ;
import java.util.List ;
import java.util.Set ;

import javax.validation.Valid ;

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
import me.taborda.mashtv.model.Show ;
import me.taborda.mashtv.model.Torrent ;
import me.taborda.mashtv.service.EpisodeService ;
import me.taborda.mashtv.service.ShowService ;

@Controller
@RequestMapping("/shows")
public class ShowController extends AbstractController {

    private static final Logger LOG = LoggerFactory.getLogger(ShowController.class) ;

    @Autowired
    private ShowService shows ;

    @Autowired
    private EpisodeService episodes ;

    @RequestMapping("")
    public String shows() {
        return "shows" ;
    }

    @RequestMapping("/list")
    @ResponseBody
    public List<Show> list() {
        return shows.findAll() ;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public void add(@Valid @ModelAttribute("show") final Show show) {
        shows.save(show) ;
        LOG.info("Added TV Show: " + show.getTitle()) ;
    }

    @RequestMapping(value = "/delete/{show}", method = RequestMethod.POST)
    public void delete(@ModelAttribute final Show show) {
        shows.delete(show) ;
        LOG.info("Removed TV Show: {}", show) ;
    }

    @RequestMapping("/{show}")
    @ResponseBody
    public Show getShow(@ModelAttribute final Show show) {
        return show ;
    }

    @RequestMapping("/{show}/episodes/list")
    @ResponseBody
    public List<Episode> listEpisodes(@ModelAttribute final Show show) {
        return episodes.findAll(show) ;
    }

    @RequestMapping("/{show}/episodes/{season}/{episode}")
    @ResponseBody
    public Episode getEpisode(@ModelAttribute final Show show, @PathVariable final Integer season, @PathVariable final Integer episode) {
        return findEpisode(show, season, episode) ;
    }

    @RequestMapping(value = "/{show}/toggle/{season}/{episode}", method = RequestMethod.POST)
    public void toggleWatched(@ModelAttribute final Show show, @PathVariable final Integer season, @PathVariable final Integer episode) {
        Episode e = findEpisode(show, season, episode) ;
        e.setDownloaded(!e.isDownloaded()) ;
        episodes.save(e) ;
        LOG.info("{} was set as {}", episode, e.isDownloaded() ? "downloaded" : "not downloaded") ;
    }

    @RequestMapping("/{show}/download/{season}/{episode}/{torrent}")
    public void download(@ModelAttribute final Show show, @PathVariable final Integer season, @PathVariable final Integer episode,
                    @PathVariable final Integer torrent) throws FileNotFoundException, IOException {
        Episode e = findEpisode(show, season, episode) ;
        Torrent t = e.getTorrent(torrent) ;

        URL tor = new URL(t.getUrl()) ;
        try (FileOutputStream fos = new FileOutputStream("/home/tab/testes/" + e.toShortString() + ".torrent");
                        ReadableByteChannel rbc = Channels.newChannel(tor.openStream())) {
            fos.getChannel().transferFrom(rbc, 0, 1 << 24) ;
        }

        e.setDownloaded(true) ;
        episodes.save(e) ;
        LOG.info("{} downloaded!", e) ;
    }

    @RequestMapping("/{show}/torrents/{season}/{episode}")
    @ResponseBody
    public Set<Torrent> torrents(@ModelAttribute final Show show, @PathVariable final Integer season, @PathVariable final Integer episode) {
        Episode e = findEpisode(show, season, episode) ;
        return e.getTorrents() ;
    }

    @RequestMapping("/{show}/delete/{season}/{episode}")
    public void delete(@ModelAttribute final Show show, @PathVariable final Integer season, @PathVariable final Integer episode) {
        Episode e = findEpisode(show, season, episode) ;
        episodes.delete(e) ;
        LOG.info("Removed episode: {}", e) ;
    }

    private Episode findEpisode(final Show show, final int season, final int episode) {
        Episode ep = episodes.find(show, season, episode) ;
        if (ep == null) {
            LOG.error("No episode on {} with season {} and episode {}", show, season, episode) ;
            throw new RuntimeException(String.format("No episode on %s with season %d and episode %d", show, season, episode)) ;
        }

        return ep ;
    }

}
