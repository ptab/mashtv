package me.taborda.mashtv.unit;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Optional;

import me.taborda.mashtv.AbstractUnitTest;
import me.taborda.mashtv.tracker.ShowDetailsEnricher;
import me.taborda.mashtv.tracker.trakt.TestTraktEpisode;
import me.taborda.mashtv.tracker.trakt.TestTraktShow;
import me.taborda.mashtv.tracker.trakt.TraktClient;
import me.taborda.mashtv.model.Episode;
import me.taborda.mashtv.model.Show;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class DetailsEnricherTest extends AbstractUnitTest {

    private static final String SHOW_TITLE = "game of thrones";
    private static final String EXPECTED_SHOW_TITLE = "Game of Thrones";
    private static final int SHOW_TRAKTID = 1234;
    private static final String EXPECTED_EPISODE_TITLE = "Winter is Coming";

    @InjectMocks
    private ShowDetailsEnricher victim;

    @Mock
    private TraktClient traktClient;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void shouldEnrichShowWhenTitleIsFound() {
        Show s = new Show(SHOW_TITLE);
        when(traktClient.findShowsMatching(SHOW_TITLE)).thenReturn(Collections.singletonList(new TestTraktShow().withTitle(EXPECTED_SHOW_TITLE).withTraktId(SHOW_TRAKTID)));
        victim.enrich(s);
        assertEquals(EXPECTED_SHOW_TITLE, s.getTitle()) ;
        assertEquals(SHOW_TRAKTID, s.getTraktId()) ;
    }

    @Test
    public void shouldNotEnrichShowWhenTitleIsNotFound() {
        Show s = new Show(SHOW_TITLE);
        when(traktClient.findShowsMatching(anyString())).thenReturn(Collections.emptyList());
        victim.enrich(s);
        assertEquals(SHOW_TITLE, s.getTitle()) ;
        assertEquals(Show.NO_ID, s.getTraktId());
    }

    @Test
    public void shouldEnrichEpisodeWhenItIsFound() {
        Show s = new Show(SHOW_TITLE);
        s.setTraktId(SHOW_TRAKTID);
        Episode e = new Episode(s, 1, 2);
        when(traktClient.getEpisode(SHOW_TRAKTID, 1, 2)).thenReturn(Optional.of(new TestTraktEpisode().withTitle(EXPECTED_EPISODE_TITLE)));
        victim.enrich(e);
        assertEquals(EXPECTED_EPISODE_TITLE, e.getTitle()) ;
    }

    @Test
    public void shouldNotEnrichEpisodeWhenEpisodeNotFound() {
        Show s = new Show(SHOW_TITLE);
        s.setTraktId(SHOW_TRAKTID);
        Episode e = new Episode(s, 1, 2);
        when(traktClient.getEpisode(SHOW_TRAKTID, 1, 2)).thenReturn(Optional.empty());
        victim.enrich(e);
        assertEquals(Episode.UNKNOWN_TITLE, e.getTitle()) ;
    }

}