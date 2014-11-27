package me.taborda.mashtv.controller ;

import java.io.IOException ;
import java.net.URL ;
import java.util.List ;
import java.util.regex.Matcher ;
import java.util.regex.Pattern ;

import javax.validation.constraints.NotNull ;

import org.slf4j.Logger ;
import org.slf4j.LoggerFactory ;
import org.springframework.beans.factory.annotation.Autowired ;
import org.springframework.stereotype.Controller ;
import org.springframework.web.bind.annotation.PathVariable ;
import org.springframework.web.bind.annotation.RequestMapping ;
import org.springframework.web.bind.annotation.RequestMethod ;
import org.springframework.web.bind.annotation.RequestParam ;
import org.springframework.web.bind.annotation.ResponseBody ;

import com.sun.syndication.feed.synd.SyndEntry ;
import com.sun.syndication.feed.synd.SyndFeed ;
import com.sun.syndication.io.FeedException ;
import com.sun.syndication.io.SyndFeedInput ;
import com.sun.syndication.io.XmlReader ;

import me.taborda.mashtv.model.Episode ;
import me.taborda.mashtv.model.Feed ;
import me.taborda.mashtv.model.Show ;
import me.taborda.mashtv.service.EpisodeService ;
import me.taborda.mashtv.service.FeedService ;
import me.taborda.mashtv.service.ShowService ;
import me.taborda.mashtv.util.Util ;

@Controller
@RequestMapping("/feeds")
public class FeedController {

    private static final Logger LOG = LoggerFactory.getLogger(FeedController.class) ;

    private static final Pattern TITLE_PATTERN = Pattern.compile("\\s?([\\w+[\\s\\.\\_]]+)[\\s\\.\\_][sS\\[]?(\\d?\\d)[\\s\\.\\_]?[eEx](\\d?\\d)\\]?.*") ;

    private static final Pattern HD_PATTERN = Pattern.compile("(?:1080|720)[pP]") ;

    @Autowired
    private FeedService feeds ;

    @Autowired
    private ShowService shows ;

    @Autowired
    private EpisodeService episodes ;

    @RequestMapping(value = "")
    @ResponseBody
    public List<Feed> list() {
        return feeds.getAll() ;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public void add(@NotNull @RequestParam final String url) throws IllegalArgumentException, IOException, FeedException {
        Feed f = new Feed(url) ;
        feeds.save(f) ;
        LOG.info("Added feed: {}", f) ;
        loadFeed(f.getUrl()) ;
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable final long id) {
        Feed f = feeds.get(id) ;
        feeds.delete(f) ;
        LOG.info("Removed feed: {}", f) ;
    }

    @RequestMapping(value = "/load/{id}", method = RequestMethod.DELETE)
    public void load(@PathVariable final long id) throws IllegalArgumentException, IOException, FeedException {
        Feed f = feeds.get(id) ;
        loadFeed(f.getUrl()) ;
    }

    private void loadFeed(final String url) throws IOException, IllegalArgumentException, FeedException {
        URL feedSource = new URL(url) ;
        try (XmlReader reader = new XmlReader(feedSource)) {
            SyndFeedInput input = new SyndFeedInput() ;
            SyndFeed sf = input.build(reader) ;
            for (Object o : sf.getEntries()) {
                addItem((SyndEntry) o) ;
            }
        }
    }

    private void addItem(final SyndEntry entry) {
        LOG.debug("Processing item {}", entry.getTitle()) ;
        String showTitle ;
        int seasonNumber ;
        int episodeNumber ;
        boolean hd ;

        Matcher matcher = TITLE_PATTERN.matcher(entry.getTitle()) ;
        if (matcher.find()) {
            showTitle = Util.fixString(matcher.group(1)) ;
            // FIXME: this is a ugly hack for the show The Office
            if (showTitle.equalsIgnoreCase("The Office US")) {
                showTitle = new String("The Office") ;
            }

            seasonNumber = Integer.parseInt(matcher.group(2)) ;
            episodeNumber = Integer.parseInt(matcher.group(3)) ;
        } else {
            return ;
        }

        hd = HD_PATTERN.matcher(entry.getTitle()).matches() ;

        Show show = shows.find(showTitle) ;
        if (show == null) {
            LOG.debug("{} is not on the list - ignoring", showTitle) ;
            return ;
        }

        Episode episode = show.getEpisode(seasonNumber, episodeNumber) ;
        if (episode == null) {
            episode = show.addEpisode(seasonNumber, episodeNumber) ;
        }

        if (episode.isTitleUnknown()) {
            episode.fetchTitle() ;
        }

        episode.addMagnetLink(entry.getLink(), hd) ;

        episodes.save(episode) ;
        LOG.info("Added episode: {}", episode) ;
    }
}
