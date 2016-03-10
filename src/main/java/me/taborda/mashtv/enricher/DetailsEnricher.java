package me.taborda.mashtv.enricher;

import java.util.List;
import java.util.Optional;

import me.taborda.mashtv.enricher.trakt.TraktClient;
import me.taborda.mashtv.enricher.trakt.TraktEpisode;
import me.taborda.mashtv.enricher.trakt.TraktShow;
import me.taborda.mashtv.model.Episode;
import me.taborda.mashtv.model.Show;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DetailsEnricher {
    private static final Logger LOG = LoggerFactory.getLogger(DetailsEnricher.class);

    @Autowired
    private TraktClient trakt;

    public List<TraktShow> findShow(String query) {
        return trakt.findShow(query);
    }

    public Show enrich(final Show show) {
        List<TraktShow> shows = findShow(show.getTitle());
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
