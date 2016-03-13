package me.taborda.mashtv.tracker;

import java.util.List;
import java.util.stream.Collectors;

import me.taborda.mashtv.tracker.trakt.TraktClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ShowFinder {
    private static final Logger LOG = LoggerFactory.getLogger(ShowFinder.class);

    @Autowired
    private TraktClient trakt;

    public List<FindShowResult> findShowsMatching(final String query) {
        return trakt.findShowsMatching(query).stream().map(s -> new FindShowResult(s.getTitle(), s.getYear(), s.getTraktId())).collect(Collectors.toList());
    }

}
