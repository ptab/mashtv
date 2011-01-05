package org.tbrd.mashtv.controller ;

import java.io.IOException ;
import java.net.MalformedURLException ;
import java.net.URL ;
import java.util.Map ;
import java.util.regex.Matcher ;
import java.util.regex.Pattern ;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;
import org.springframework.beans.factory.annotation.Autowired ;
import org.springframework.stereotype.Controller ;
import org.springframework.transaction.annotation.Transactional ;
import org.springframework.validation.BindingResult ;
import org.springframework.web.bind.annotation.ModelAttribute ;
import org.springframework.web.bind.annotation.PathVariable ;
import org.springframework.web.bind.annotation.RequestMapping ;
import org.springframework.web.bind.annotation.RequestMethod ;
import org.tbrd.mashtv.Util ;
import org.tbrd.mashtv.model.Episode ;
import org.tbrd.mashtv.model.Feed ;
import org.tbrd.mashtv.model.Show ;
import org.tbrd.mashtv.model.Torrent ;
import org.tbrd.mashtv.service.FeedService ;
import org.tbrd.mashtv.service.ShowService ;

import com.sun.syndication.feed.synd.SyndEntry ;
import com.sun.syndication.feed.synd.SyndFeed ;
import com.sun.syndication.io.FeedException ;
import com.sun.syndication.io.SyndFeedInput ;
import com.sun.syndication.io.XmlReader ;

@Controller
@RequestMapping("/feeds")
public class FeedController {

	private Log log = LogFactory.getLog(this.getClass()) ;

	@Autowired(required = true)
	public FeedService feeds ;

	@Autowired(required = true)
	public ShowService shows ;

	@RequestMapping(value = "")
	public String list(Map<String, Object> map) {
		map.put("feed", new Feed()) ;
		map.put("feedList", feeds.getAll()) ;
		return "feeds" ;
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(@ModelAttribute("feed") Feed feed, BindingResult result) {
		feeds.save(feed) ;
		loadFeed(feed.getUrl()) ;
		return "redirect:/feeds" ;
	}

	@RequestMapping("/load/{id}")
	public String load(@PathVariable("id") Integer id) {
		Feed feed = feeds.get(id) ;
		loadFeed(feed.getUrl()) ;
		return "redirect:/feeds" ;
	}

	@RequestMapping("/delete/{id}")
	public String delete(@PathVariable("id") Integer id) {
		Feed feed = feeds.get(id) ;
		if (feed != null) {
			feeds.delete(feed) ;
			log.info("Removed feed: " + feed.getUrl()) ;
		}
		else
			log.info("No feed with id " + id) ;

		return "redirect:/feeds" ;
	}

	private void loadFeed(String url) {
		try {
			URL feedSource = new URL(url) ;
			SyndFeedInput input = new SyndFeedInput() ;
			SyndFeed sf = input.build(new XmlReader(feedSource)) ;
			for (Object o : sf.getEntries())
				addItem((SyndEntry) o) ;
		}
		catch (MalformedURLException e) {
			e.printStackTrace() ;
		}
		catch (IllegalArgumentException e) {
			e.printStackTrace() ;
		}
		catch (FeedException e) {
			e.printStackTrace() ;
		}
		catch (IOException e) {
			e.printStackTrace() ;
		}
	}

	@Transactional
	private void addItem(SyndEntry entry) {
		log.debug("Processing item " + entry.getTitle()) ;
		String show_title ;
		int season_number ;
		int episode_number ;
		boolean hd ;

		Pattern pattern = Pattern
				.compile("\\s?([\\w+[\\s\\.\\_]]+)[\\s\\.\\_][sS\\[]?(\\d?\\d)[\\s\\.\\_]?[eEx](\\d?\\d)\\]?.*") ;
		Matcher matcher = pattern.matcher(entry.getTitle()) ;
		if (matcher.find()) {
			show_title = Util.fixString(matcher.group(1)) ;
			// FIXME: this is a ugly hack for the show The Office
			if (show_title.equalsIgnoreCase("The Office US"))
				show_title = new String("The Office") ;

			season_number = new Integer(matcher.group(2)).intValue() ;
			episode_number = new Integer(matcher.group(3)).intValue() ;
		}
		else
			return ;

		pattern = Pattern.compile("720[pP]") ;
		matcher = pattern.matcher(entry.getTitle()) ;
		hd = matcher.find() ;

		Show show = shows.getShow(show_title) ;
		if (show == null) {
			log.debug(show_title + " is not on the list - ignoring.") ;
			return ;
		}

		Episode episode = shows.getEpisode(show, season_number, episode_number) ;
		if (episode == null)
			episode = new Episode(show, season_number, episode_number) ;

		if (episode.getTitle().equals("Unknown title"))
			episode.fetchTitle() ;
		Torrent torrent = new Torrent(episode, entry.getLink(), hd) ;
		if (!episode.getTorrents().contains(torrent))
			episode.addTorrent(torrent) ;

		show.getEpisodes().add(episode) ;
		shows.saveShow(show) ;
		log.info("Added episode: " + episode.toString()) ;
	}
}
