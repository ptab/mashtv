package me.taborda.mashtv.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import me.taborda.mashtv.enricher.DetailsEnricher;
import me.taborda.mashtv.exception.NonUniqueException;
import me.taborda.mashtv.model.Episode;
import me.taborda.mashtv.model.Feed;
import me.taborda.mashtv.model.Show;
import me.taborda.mashtv.repository.FeedRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FeedService {

    private static final Logger LOG = LoggerFactory.getLogger(FeedService.class);

    private static final String WHITESPACE = "[\\s\\.\\_]";

    private static final String TITLE_CHARS = String.format("[\\w%s\\'\\:\\!\\-\\&\\(\\)]", WHITESPACE);

    private static final String REGULAR_SERIES = String.format("[s\\[]?(\\d?\\d)%s?[ex]%s?(\\d?\\d)\\]", WHITESPACE, WHITESPACE);

    private static final String DAILY_SHOWS = "(\\d\\d\\d\\d)-(\\d??\\d-\\d?\\d)";

    private static final String TITLE_FORMAT = ".*?(%s+)%s%s?.*";

    private static final Pattern PATTERN_REGULAR_SERIES = Pattern.compile(String.format(TITLE_FORMAT, TITLE_CHARS, WHITESPACE, REGULAR_SERIES), Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

    private static final Pattern PATTERN_DAILY_SHOWS = Pattern.compile(String.format(TITLE_FORMAT, TITLE_CHARS, WHITESPACE, DAILY_SHOWS), Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

    private static final Pattern HD_PATTERN = Pattern.compile("(1080|720)p", Pattern.CASE_INSENSITIVE);

    @Autowired
    private FeedRepository repository;

    @Autowired
    private ShowService shows;

    @Autowired
    private EpisodeService episodes;

    @Autowired
    private DetailsEnricher enricher;

    @Transactional(readOnly = true)
    public List<Feed> findAll() {
        return repository.findAll();
    }

    @Transactional
    public Feed add(final String url) {
        if (repository.findByUrlIgnoreCase(url).isPresent()) {
            throw new NonUniqueException(url);
        }
        return repository.save(new Feed(url));
    }

    @Transactional
    public void delete(final Feed feed) {
        repository.delete(feed);
    }

    @Transactional
    public void load(final Feed feed) {
        try (XmlReader reader = new XmlReader(new URL(feed.getUrl()))) {
            SyndFeed sf = new SyndFeedInput().build(reader);
            sf.getEntries().stream().forEach(entry -> loadEntry((SyndEntry) entry));
        } catch (MalformedURLException e) {
            LOG.warn("Invalid URL: {}", e.getMessage());
        } catch (IOException | FeedException e) {
            LOG.warn("Can't read feed content: {}", e.getMessage());
        }
    }

    private void loadEntry(final SyndEntry entry) {
        LOG.trace("Matching {}", entry.getTitle());

        // FIXME there is a better way to do this..
        Optional<MatchedEntry> match = matchRegularShow(entry);
        if (!match.isPresent()) match = matchDailyShow(entry);

        if (match.isPresent()) processEntry(entry, match.get());
        else LOG.debug("No match: {}", entry.getTitle());
    }

    private Optional<MatchedEntry> matchRegularShow(final SyndEntry entry) {
        Matcher matcher = PATTERN_REGULAR_SERIES.matcher(entry.getTitle());
        if (!matcher.matches()) {
            return Optional.empty();
        }

        String showTitle = matcher.group(1).trim();
        int seasonNumber = Integer.parseInt(matcher.group(2));
        int episodeNumber = Integer.parseInt(matcher.group(3));
        return Optional.of(new MatchedEntry(showTitle, seasonNumber, episodeNumber));
    }

    private Optional<MatchedEntry> matchDailyShow(final SyndEntry entry) {
        Matcher matcher = PATTERN_DAILY_SHOWS.matcher(entry.getTitle());
        if (!matcher.matches()) {
            LOG.debug("No match: {}", entry.getTitle());
            return Optional.empty();
        }

        String showTitle = matcher.group(1).trim();
        int year = Integer.parseInt(matcher.group(2));
        String[] monthAndDay = matcher.group(3).split("-");
        int episode = Integer.valueOf(monthAndDay[0] + monthAndDay[1]);
        return Optional.of(new MatchedEntry(showTitle, year, episode));
    }

    private void processEntry(final SyndEntry entry, final MatchedEntry details) {
        Optional<Show> show = shows.find(details.show);
        if (!show.isPresent()) {
            LOG.debug("Not on the list: {} ({})", details.show, entry.getTitle());
            return;
        }

        Episode episode = show.get().findEpisode(details.season, details.episode).orElseGet(() -> show.get().addEpisode(details.season, details.episode));

        boolean hd = HD_PATTERN.matcher(entry.getTitle()).matches();
        episode.addMagnetLink(entry.getLink(), entry.getTitle(), hd);

        enricher.enrich(episode);
        episodes.save(episode);
    }

    private class MatchedEntry {
        private String show;
        private int season;
        private int episode;

        private MatchedEntry(String show, int season, int episode) {
            this.show = show;
            this.season = season;
            this.episode = episode;
        }
    }
}