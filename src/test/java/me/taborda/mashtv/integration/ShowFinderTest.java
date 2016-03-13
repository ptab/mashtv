package me.taborda.mashtv.integration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.mockito.Mockito.when;

import java.util.Collections;

import me.taborda.mashtv.AbstractIntegrationTest;
import me.taborda.mashtv.tracker.FindShowResult;
import me.taborda.mashtv.tracker.ShowFinder;
import me.taborda.mashtv.tracker.trakt.TestTraktShow;
import me.taborda.mashtv.tracker.trakt.TraktClient;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ShowFinderTest extends AbstractIntegrationTest {

    private static final String QUERY = "game of";
    private static final String TITLE = "Game of Thrones";
    private static final int YEAR = 2015;
    private static final int TRACKER_ID = 1234;

    @Autowired
    private ShowFinder victim;

    @Autowired
    private TraktClient traktClient;

    @Test
    public void findShow() {
        when(traktClient.findShowsMatching(QUERY)).thenReturn(Collections.singletonList(new TestTraktShow().withTitle(TITLE).withYear(YEAR).withTraktId(TRACKER_ID)));
        assertThat(victim.findShowsMatching(QUERY), contains(new FindShowResult(TITLE, YEAR, TRACKER_ID)));
    }

}
