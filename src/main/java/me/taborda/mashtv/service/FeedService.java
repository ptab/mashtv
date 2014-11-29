package me.taborda.mashtv.service ;

import java.io.IOException ;
import java.net.URL ;
import java.util.List ;
import java.util.regex.Matcher ;
import java.util.regex.Pattern ;

import org.slf4j.Logger ;
import org.slf4j.LoggerFactory ;
import org.springframework.beans.factory.annotation.Autowired ;
import org.springframework.stereotype.Service ;
import org.springframework.transaction.annotation.Transactional ;

import com.rometools.rome.feed.synd.SyndEntry ;
import com.rometools.rome.feed.synd.SyndFeed ;
import com.rometools.rome.io.FeedException ;
import com.rometools.rome.io.SyndFeedInput ;
import com.rometools.rome.io.XmlReader ;

import me.taborda.mashtv.model.Episode ;
import me.taborda.mashtv.model.Feed ;
import me.taborda.mashtv.model.Show ;
import me.taborda.mashtv.repository.FeedRepository ;
import me.taborda.mashtv.util.Util ;

@Service
public class FeedService {

    private static final Logger LOG = LoggerFactory.getLogger(FeedService.class) ;

    private static final String WHITESPACE = "[\\s\\.\\_]" ;

    private static final String TITLE_FORMAT = ".*?([\\w+%s]+)%s[s\\[]?(\\d?\\d)%s?[ex](\\d?\\d)\\]?.*" ;

    private static final Pattern TITLE_PATTERN = Pattern.compile(String.format(TITLE_FORMAT, WHITESPACE, WHITESPACE, WHITESPACE), Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE) ;

    private static final Pattern HD_PATTERN = Pattern.compile("(?:1080|720)p", Pattern.CASE_INSENSITIVE) ;

    @Autowired
    private FeedRepository repository ;

    @Autowired
    private ShowService shows ;

    @Autowired
    private EpisodeService episodes ;

    @Transactional(readOnly = true)
    public List<Feed> getAll() {
        return repository.findAll() ;
    }

    @Transactional(readOnly = true)
    public Feed get(final long id) {
        return repository.findOne(id) ;
    }

    @Transactional
    public void save(final Feed feed) {
        repository.save(feed) ;
    }

    @Transactional
    public void delete(final Feed feed) {
        repository.delete(feed) ;
    }

    @Transactional
    public void load(final Feed feed) throws IOException, IllegalArgumentException, FeedException {
        URL feedSource = new URL(feed.getUrl()) ;
        try (XmlReader reader = new XmlReader(feedSource)) {
            SyndFeedInput input = new SyndFeedInput() ;
            SyndFeed sf = input.build(reader) ;
            for (Object o : sf.getEntries()) {
                loadEntry((SyndEntry) o) ;
            }
        }
    }

    private void loadEntry(final SyndEntry entry) {
        LOG.trace("Matching {}", entry.getTitle()) ;
        Matcher matcher = TITLE_PATTERN.matcher(entry.getTitle()) ;
        if (!matcher.matches()) {
            LOG.debug("No match: {}", entry.getTitle()) ;
            return ;
        }

        String showTitle = Util.fixTitle(matcher.group(1)) ;
        int seasonNumber = Integer.parseInt(matcher.group(2)) ;
        int episodeNumber = Integer.parseInt(matcher.group(3)) ;
        boolean hd = HD_PATTERN.matcher(entry.getTitle()).matches() ;

        Show show = shows.find(showTitle) ;
        if (show == null) {
            LOG.debug("Not on the list: {} ({})", showTitle, entry.getTitle()) ;
            return ;
        }

        Episode episode = show.getEpisode(seasonNumber, episodeNumber) ;
        if (episode == null) {
            episode = show.addEpisode(seasonNumber, episodeNumber) ;
            LOG.info("Added episode: {}", episode) ;
        }

        if (episode.isTitleUnknown()) {
            episodes.updateTitle(episode) ;
        }

        episode.addMagnetLink(entry.getLink(), entry.getTitle(), hd) ;
        LOG.info("Added new link: {}", entry.getTitle()) ;

        episodes.save(episode) ;
    }
}