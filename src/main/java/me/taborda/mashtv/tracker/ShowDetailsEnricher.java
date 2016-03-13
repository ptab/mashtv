package me.taborda.mashtv.tracker;

import java.util.List;
import java.util.Optional;

import me.taborda.mashtv.tracker.trakt.TraktClient;
import me.taborda.mashtv.tracker.trakt.TraktEpisode;
import me.taborda.mashtv.tracker.trakt.TraktShow;
import me.taborda.mashtv.model.Episode;
import me.taborda.mashtv.model.Show;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ShowDetailsEnricher {
    private static final Logger LOG = LoggerFactory.getLogger(ShowDetailsEnricher.class);

    @Autowired
    private TraktClient trakt;

    public Show enrich(final Show show) {
        List<TraktShow> shows = trakt.findShowsMatching(show.getTitle());
        if (!shows.isEmpty()) {
            TraktShow s = shows.get(0); // TODO allow user to select, instead of hardcoding
            show.setTraktId(s.getTraktId());
            show.setTitle(s.getTitle());
        }
        return show;
    }

    public Episode enrich(final Episode episode) {
        Optional<TraktEpisode> e = trakt.getEpisode(episode.getShow().getTraktId(), episode.getSeason(), episode.getEpisode());
        if (e.isPresent()) episode.setTitle(e.get().getTitle());
        return episode;
    }

}
