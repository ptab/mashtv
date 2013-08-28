package me.taborda.mashtv.controller ;

import java.io.FileOutputStream ;
import java.io.IOException ;
import java.net.MalformedURLException ;
import java.net.URL ;
import java.nio.channels.Channels ;
import java.nio.channels.ReadableByteChannel ;
import java.util.Map ;

import javax.servlet.http.HttpServletRequest ;

import org.slf4j.Logger ;
import org.slf4j.LoggerFactory ;
import org.springframework.beans.factory.annotation.Autowired ;
import org.springframework.stereotype.Controller ;
import org.springframework.web.bind.annotation.ModelAttribute ;
import org.springframework.web.bind.annotation.PathVariable ;
import org.springframework.web.bind.annotation.RequestMapping ;
import org.springframework.web.bind.annotation.RequestMethod ;

import me.taborda.mashtv.model.Episode ;
import me.taborda.mashtv.model.Show ;
import me.taborda.mashtv.model.Torrent ;
import me.taborda.mashtv.service.EpisodeService ;
import me.taborda.mashtv.service.ShowService ;

@Controller
@RequestMapping("/shows")
public class ShowController {

    private static final Logger LOG = LoggerFactory.getLogger(ShowController.class) ;

    @Autowired
    private ShowService shows ;

    @Autowired
    private EpisodeService episodes ;

    @RequestMapping("")
    public String list(final Map<String, Object> map) {
        map.put("show", new Show()) ;
        map.put("showList", shows.findAll()) ;
        return "shows" ;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String add(@ModelAttribute("show") final Show show) {
        shows.save(show) ;
        LOG.info("Added TVShow: " + show.getTitle()) ;
        return "redirect:/shows" ;
    }

    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable("id") final Integer id) {
        Show show = shows.find(id) ;
        if (show == null) {
            LOG.error("No show with id " + id) ;
            return "redirect:/shows" ;
        }

        shows.delete(show) ;
        LOG.info("Removed TVShow: " + show.getTitle()) ;
        return "redirect:/shows" ;
    }

    @RequestMapping("/{id}")
    public String getShow(@PathVariable("id") final Integer id, final Map<String, Object> map) {
        Show show = shows.find(id) ;
        if (show == null) {
            LOG.error("No show with id " + id) ;
            return "redirect:/shows" ;
        }

        map.put("show", show) ;
        map.put("episodes", episodes.findAll(show)) ;
        return "episodes" ;
    }

    @RequestMapping("/{show}/toggle/{season}/{episode}")
    public String toggleWatched(@PathVariable("show") final Integer show, @PathVariable("season") final Integer season,
                    @PathVariable("episode") final Integer episode, final HttpServletRequest request) {
        String redirect = getRedirect(request) ;

        Show s = shows.find(show) ;
        if (s == null) {
            LOG.error("No show with id " + show) ;
            return "redirect:/" + redirect ;
        }

        Episode ep = episodes.find(s, season, episode) ;
        if (ep == null) {
            LOG.error("No episode on " + s.getTitle() + " with season " + season + " and episode " + episode) ;
            return "redirect:/" + redirect ;
        }

        ep.setDownloaded(!ep.isDownloaded()) ;
        episodes.save(ep) ;
        LOG.info(ep.toString() + " was set as " + (ep.isDownloaded() ? "downloaded" : "not downloaded")) ;
        return "redirect:/" + redirect ;
    }

    @RequestMapping("/{show}/download/{season}/{episode}/{torrent}")
    public String download(@PathVariable("show") final Integer show, @PathVariable("season") final Integer season,
                    @PathVariable("episode") final Integer episode, @PathVariable("torrent") final Integer torrent, final HttpServletRequest request) {

        String redirect = getRedirect(request) ;

        Episode ep = getEpisode(show, season, episode) ;
        if (ep == null) {
            LOG.error("No episode with show " + show + ", season " + season + " and episode " + episode) ;
            return "redirect:/" + redirect ;
        }

        Torrent t = ep.getTorrent(torrent) ;

        URL tor ;
        try {
            tor = new URL(t.getUrl()) ;
        } catch (MalformedURLException e) {
            e.printStackTrace() ;
            LOG.error("Error downloading " + ep.toString()) ;
            return "redirect:/" + redirect ;
        }

        try (FileOutputStream fos = new FileOutputStream("/home/tab/testes/" + ep.toShortString() + ".torrent");
                        ReadableByteChannel rbc = Channels.newChannel(tor.openStream())) {
            fos.getChannel().transferFrom(rbc, 0, 1 << 24) ;
        } catch (IOException e) {
            e.printStackTrace() ;
            LOG.error("Error downloading " + ep.toString()) ;
            return "redirect:/" + redirect ;
        }

        ep.setDownloaded(true) ;
        episodes.save(ep) ;
        LOG.info(ep.toString() + " downloaded!") ;

        return "redirect:/" + redirect ;
    }

    @RequestMapping("/{show}/torrents/{season}/{episode}")
    public String torrents(@PathVariable("show") final Integer show, @PathVariable("season") final Integer season,
                    @PathVariable("episode") final Integer episode, final Map<String, Object> map) {
        Episode ep = getEpisode(show, season, episode) ;
        if (ep == null) {
            LOG.error("No episode with show " + show + ", season " + season + " and episode " + episode) ;
            return "redirect:/latest" ;
        }

        map.put("episode", ep) ;
        return "torrents" ;
    }

    @RequestMapping("/{show}/delete/{season}/{episode}")
    public String delete(@PathVariable("show") final Integer show, @PathVariable("season") final Integer season,
                    @PathVariable("episode") final Integer episode, final HttpServletRequest request) {
        String redirect = getRedirect(request) ;

        Episode ep = getEpisode(show, season, episode) ;
        if (ep == null) {
            LOG.error("No episode with show " + show + ", season " + season + " and episode " + episode) ;
            return "redirect:/" + redirect ;
        }

        episodes.delete(ep) ;
        LOG.info("Removed episode: " + ep.toString()) ;
        return "redirect:/" + redirect ;
    }

    private Episode getEpisode(final int show, final int season, final int episode) {
        Show s = shows.find(show) ;
        if (s == null) {
            LOG.error("No show with id " + show) ;
            return null ;
        }

        Episode ep = episodes.find(s, season, episode) ;
        if (ep == null)
            LOG.info("Show " + s.getTitle() + " has no season " + season + " or episode " + episode) ;
        return ep ;
    }

    private String getRedirect(final HttpServletRequest request) {
        LOG.info("referer: " + request.getHeader("Referer")) ;

        String referer = request.getHeader("Referer") ;
        if (referer.contains("/shows/"))
            return "shows" ;

        return "latest" ;
    }
}
