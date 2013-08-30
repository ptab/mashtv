package me.taborda.mashtv.controller ;

import java.io.IOException ;
import java.net.URL ;
import java.util.List ;
import java.util.regex.Matcher ;
import java.util.regex.Pattern ;

import javax.validation.Valid ;

import org.slf4j.Logger ;
import org.slf4j.LoggerFactory ;
import org.springframework.beans.factory.annotation.Autowired ;
import org.springframework.stereotype.Controller ;
import org.springframework.transaction.annotation.Transactional ;
import org.springframework.web.bind.annotation.ModelAttribute ;
import org.springframework.web.bind.annotation.RequestMapping ;
import org.springframework.web.bind.annotation.RequestMethod ;
import org.springframework.web.bind.annotation.ResponseBody ;

import com.sun.syndication.feed.synd.SyndEntry ;
import com.sun.syndication.feed.synd.SyndFeed ;
import com.sun.syndication.io.FeedException ;
import com.sun.syndication.io.SyndFeedInput ;
import com.sun.syndication.io.XmlReader ;

import me.taborda.mashtv.model.Episode ;
import me.taborda.mashtv.model.Feed ;
import me.taborda.mashtv.model.Show ;
import me.taborda.mashtv.model.Torrent ;
import me.taborda.mashtv.service.EpisodeService ;
import me.taborda.mashtv.service.FeedService ;
import me.taborda.mashtv.service.ShowService ;
import me.taborda.mashtv.util.Util ;

@Controller
@RequestMapping("/feeds")
public class FeedController extends AbstractController {

    private static final Logger LOG = LoggerFactory.getLogger(FeedController.class) ;

    @Autowired
    private FeedService feeds ;

    @Autowired
    private ShowService shows ;

    @Autowired
    private EpisodeService episodes ;

    @RequestMapping
    public String feeds() {
        return "feeds" ;
    }

    @RequestMapping(value = "/list")
    @ResponseBody
    public List<Feed> list() {
        return feeds.getAll() ;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public void add(@Valid @ModelAttribute final Feed feed) throws IllegalArgumentException, IOException, FeedException {
        feeds.save(feed) ;
        LOG.info("Added feed: {}", feed) ;
        loadFeed(feed.getUrl()) ;
    }

    @RequestMapping("/delete/{feed}")
    public void delete(@ModelAttribute final Feed feed) {
        feeds.delete(feed) ;
        LOG.info("Removed feed: {}", feed) ;
    }

    @RequestMapping("/load/{feed}")
    public void load(@ModelAttribute final Feed feed) throws IllegalArgumentException, IOException, FeedException {
        loadFeed(feed.getUrl()) ;
    }

    private void loadFeed(final String url) throws IOException, IllegalArgumentException, FeedException {
        URL feedSource = new URL(url) ;
        try (XmlReader reader = new XmlReader(feedSource)) {
            SyndFeedInput input = new SyndFeedInput() ;
            SyndFeed sf = input.build(reader) ;
            for (Object o : sf.getEntries())
                addItem((SyndEntry) o) ;
        }
    }

    @Transactional
    private void addItem(final SyndEntry entry) {
        LOG.debug("Processing item {}", entry.getTitle()) ;
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
        } else
            return ;

        pattern = Pattern.compile("720[pP]") ;
        matcher = pattern.matcher(entry.getTitle()) ;
        hd = matcher.find() ;

        Show show = shows.find(show_title) ;
        if (show == null) {
            LOG.debug("{} is not on the list - ignoring", show_title) ;
            return ;
        }

        Episode episode = episodes.find(show, season_number, episode_number) ;
        if (episode == null)
            episode = episodes.create(show, season_number, episode_number) ;

        if (episode.getTitle().equals("Unknown title"))
            episode.fetchTitle() ;
        Torrent torrent = new Torrent(episode, entry.getTitle(), entry.getLink(), hd) ;
        if (!episode.getTorrents().contains(torrent))
            episode.addTorrent(torrent) ;

        episodes.save(episode) ;
        LOG.info("Added episode: {}", episode) ;
    }
}
