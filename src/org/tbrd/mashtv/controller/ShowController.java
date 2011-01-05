package org.tbrd.mashtv.controller ;

import java.io.FileOutputStream ;
import java.io.IOException ;
import java.net.MalformedURLException ;
import java.net.URL ;
import java.nio.channels.Channels ;
import java.nio.channels.ReadableByteChannel ;
import java.util.Map ;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;
import org.springframework.beans.factory.annotation.Autowired ;
import org.springframework.stereotype.Controller ;
import org.springframework.validation.BindingResult ;
import org.springframework.web.bind.annotation.ModelAttribute ;
import org.springframework.web.bind.annotation.PathVariable ;
import org.springframework.web.bind.annotation.RequestMapping ;
import org.springframework.web.bind.annotation.RequestMethod ;
import org.tbrd.mashtv.model.Episode ;
import org.tbrd.mashtv.model.Show ;
import org.tbrd.mashtv.model.Torrent ;
import org.tbrd.mashtv.service.ShowService ;

@Controller
@RequestMapping("/shows")
public class ShowController {

	private Log log = LogFactory.getLog(this.getClass()) ;

	@Autowired(required = true)
	private ShowService shows ;

	@RequestMapping("")
	public String list(Map<String, Object> map) {
		map.put("show", new Show()) ;
		map.put("showList", shows.getShows()) ;
		return "shows" ;
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(@ModelAttribute("show") Show show, BindingResult result) {
		shows.saveShow(show) ;
		log.info("Added TVShow: " + show.getTitle()) ;
		return "redirect:/shows" ;
	}

	@RequestMapping("/delete/{id}")
	public String delete(@PathVariable("id") Integer id) {
		Show show = shows.getShow(id) ;
		if (show == null) {
			log.error("No show with id " + id) ;
			return "redirect:/shows" ;
		}

		shows.removeShow(show) ;
		log.info("Removed TVShow: " + show.getTitle()) ;
		return "redirect:/shows" ;
	}

	@RequestMapping("/{id}")
	public String getShow(@PathVariable("id") Integer id, Map<String, Object> map) {
		Show show = shows.getShow(id) ;
		if (show == null) {
			log.error("No show with id " + id) ;
			return "redirect:/shows" ;
		}

		map.put("show", show) ;
		map.put("episodes", shows.getEpisodes(show)) ;
		return "episodes" ;
	}

	@RequestMapping("/{show}/toggle/{season}/{episode}")
	public String toggleWatched(@PathVariable("show") Integer show, @PathVariable("season") Integer season,
			@PathVariable("episode") Integer episode) {
		Show s = shows.getShow(show) ;
		if (s == null) {
			log.error("No show with id " + show) ;
			return "redirect:/shows" ;
		}

		Episode ep = shows.getEpisode(s, season, episode) ;
		if (ep == null) {
			log.error("No episode on " + s.getTitle() + " with season " + season + " and episode " + episode) ;
			return "redirect:/shows" ;
		}

		ep.setDownloaded(!ep.isDownloaded()) ;
		shows.saveEpisode(ep) ;
		log.info(ep.toString() + " was set as " + (ep.isDownloaded() ? "downloaded" : "not downloaded")) ;
		return "redirect:/shows" ;
	}

	@RequestMapping("/{show}/download/{season}/{episode}/{torrent}")
	public String download(@PathVariable("show") Integer show, @PathVariable("season") Integer season,
			@PathVariable("episode") Integer episode, @PathVariable("torrent") Integer torrent) {
		Episode ep = getEpisode(show, season, episode) ;
		if (ep == null) {
			log.error("No episode with show " + show + ", season " + season + " and episode " + episode) ;
			return "redirect:/shows" ;
		}

		Torrent t = ep.getTorrent(torrent) ;
		try {
			URL tor = new URL(t.getUrl()) ;
			ReadableByteChannel rbc = Channels.newChannel(tor.openStream()) ;
			FileOutputStream fos = new FileOutputStream("/home/tab/testes/" + ep.toShortString() + ".torrent") ;
			fos.getChannel().transferFrom(rbc, 0, 1 << 24) ;
			fos.close() ;
			rbc.close() ;
		}
		catch (MalformedURLException e) {
			e.printStackTrace() ;
			log.error("Error downloading " + ep.toString()) ;
		}
		catch (IOException e) {
			e.printStackTrace() ;
			log.error("Error downloading " + ep.toString()) ;
		}

		ep.setDownloaded(true) ;
		shows.saveEpisode(ep) ;
		log.info(ep.toString() + " downloaded!") ;

		return "redirect:/latest" ;
	}

	@RequestMapping("/{show}/torrents/{season}/{episode}")
	public String torrents(@PathVariable("show") Integer show, @PathVariable("season") Integer season,
			@PathVariable("episode") Integer episode, Map<String, Object> map) {
		Episode ep = getEpisode(show, season, episode) ;
		if (ep == null) {
			log.error("No episode with show " + show + ", season " + season + " and episode " + episode) ;
			return "redirect:/latest" ;
		}

		map.put("episode", ep) ;
		return "torrents" ;
	}

	private Episode getEpisode(int show, int season, int episode) {
		Show s = shows.getShow(show) ;
		if (s == null) {
			log.error("No show with id " + show) ;
			return null ;
		}

		Episode ep = shows.getEpisode(s, season, episode) ;
		if (ep == null)
			log.info("Show " + s.getTitle() + " has no season " + season + " or episode " + episode) ;
		return ep ;
	}
}
