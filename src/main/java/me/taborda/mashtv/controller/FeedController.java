package me.taborda.mashtv.controller ;

import java.io.IOException ;
import java.net.MalformedURLException ;
import java.net.URL ;
import java.util.Map ;
import java.util.regex.Matcher ;
import java.util.regex.Pattern ;

import org.slf4j.Logger ;
import org.slf4j.LoggerFactory ;
import org.springframework.beans.factory.annotation.Autowired ;
import org.springframework.stereotype.Controller ;
import org.springframework.transaction.annotation.Transactional ;
import org.springframework.validation.BindingResult ;
import org.springframework.web.bind.annotation.ModelAttribute ;
import org.springframework.web.bind.annotation.PathVariable ;
import org.springframework.web.bind.annotation.RequestMapping ;
import org.springframework.web.bind.annotation.RequestMethod ;

import com.sun.syndication.feed.synd.SyndEntry ;
import com.sun.syndication.feed.synd.SyndFeed ;
import com.sun.syndication.io.FeedException ;
import com.sun.syndication.io.SyndFeedInput ;
import com.sun.syndication.io.XmlReader ;

import me.taborda.mashtv.Util ;
import me.taborda.mashtv.model.Episode ;
import me.taborda.mashtv.model.Feed ;
import me.taborda.mashtv.model.Show ;
import me.taborda.mashtv.model.Torrent ;
import me.taborda.mashtv.service.FeedService ;
import me.taborda.mashtv.service.ShowService ;

@Controller
@RequestMapping("/feeds")
public class FeedController {

    private static final Logger LOG = LoggerFactory.getLogger(FeedController.class) ;

    @Autowired
    public FeedService feeds ;

    @Autowired
    public ShowService shows ;

    @RequestMapping(value = "")
    public String list(final Map<String, Object> map) {
        map.put("feed", new Feed()) ;
        map.put("feedList", feeds.getAll()) ;
        return "feeds" ;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String add(@ModelAttribute("feed") final Feed feed, final BindingResult result) {
        feeds.save(feed) ;
        loadFeed(feed.getUrl()) ;
        return "redirect:/feeds" ;
    }

    @RequestMapping("/load/{id}")
    public String load(@PathVariable("id") final Integer id) {
        Feed feed = feeds.get(id) ;
        loadFeed(feed.getUrl()) ;
        return "redirect:/feeds" ;
    }

    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable("id") final Integer id) {
        Feed feed = feeds.get(id) ;
        if (feed != null) {
            feeds.delete(feed) ;
            LOG.info("Removed feed: " + feed.getUrl()) ;
        }
        else
            LOG.info("No feed with id " + id) ;

        return "redirect:/feeds" ;
    }

    private void loadFeed(final String url) {
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
    private void addItem(final SyndEntry entry) {
        LOG.debug("Processing item " + entry.getTitle()) ;
        String show_title ;
        int season_number ;
        int episode_number ;
        boolean hd ;

        Pattern pattern = Pattern.compile("\\s?([\\w+[\\s\\.\\_]]+)[\\s\\.\\_][sS\\[]?(\\d?\\d)[\\s\\.\\_]?[eEx](\\d?\\d)\\]?.*") ;
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

        Show show = shows.getShowEager(show_title) ;
        if (show == null) {
            LOG.debug(show_title + " is not on the list - ignoring.") ;
            return ;
        }

        Episode episode = shows.getEpisode(show, season_number, episode_number) ;
        if (episode == null)
            episode = new Episode(show, season_number, episode_number) ;

        if (episode.getTitle().equals("Unknown title"))
            episode.fetchTitle() ;
        Torrent torrent = new Torrent(episode, entry.getTitle(), entry.getLink(), hd) ;
        if (!episode.getTorrents().contains(torrent))
            episode.addTorrent(torrent) ;

        show.getEpisodes().add(episode) ;
        shows.saveShow(show) ;
        LOG.info("Added episode: " + episode.toString()) ;
    }
}
