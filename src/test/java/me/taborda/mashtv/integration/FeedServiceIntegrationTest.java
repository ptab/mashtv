package me.taborda.mashtv.integration ;

import static org.junit.Assert.assertEquals ;

import java.util.ArrayList ;
import java.util.Collections ;
import java.util.List ;

import org.junit.After ;
import org.junit.Test ;
import org.springframework.beans.factory.annotation.Autowired ;
import org.springframework.data.domain.Sort ;
import org.springframework.data.domain.Sort.Direction ;

import me.taborda.mashtv.AbstractIntegrationTest ;
import me.taborda.mashtv.exception.NonUniqueException;
import me.taborda.mashtv.model.Episode ;
import me.taborda.mashtv.model.Feed ;
import me.taborda.mashtv.model.Show ;
import me.taborda.mashtv.repository.EpisodeRepository ;
import me.taborda.mashtv.repository.FeedRepository ;
import me.taborda.mashtv.repository.ShowRepository ;
import me.taborda.mashtv.service.FeedService ;

public class FeedServiceIntegrationTest extends AbstractIntegrationTest {

    private static final Sort SORT_ORDER = new Sort(Direction.DESC, "season", "episode") ;

    private static final int[][] EPISODES_GAME_OF_THRONES = { { 4, 4 }, { 4, 3 }, { 2, 9 }, { 2, 8 }, { 2, 7 }, { 2, 6 }, { 2, 5 }, { 2, 4 }, { 2, 3 }, { 4, 2 }, { 4, 1 }, { 1, 4 }, { 1, 3 },
        { 1, 2 }, { 1, 1 } } ;

    private static final int[][] EPISODES_MODERN_FAMILY = { { 6, 15 }, { 6, 14 }, { 6, 13 } } ;

    @Autowired
    private FeedService victim ;

    @Autowired
    private FeedRepository repository ;

    @Autowired
    private ShowRepository shows ;

    @Autowired
    private EpisodeRepository episodes ;

    @After
    public void cleanup() {
        repository.deleteAll() ;
        shows.deleteAll() ;
        episodes.deleteAll() ;
    }

    @Test
    public void add() {
        String url = "a url" ;
        assertEquals(url, victim.add(url).getUrl()) ;
        assertEquals(url, repository.findByUrlIgnoreCase(url).get().getUrl());
    }

    @Test(expected = NonUniqueException.class)
    public void addShouldFailWhenUrlExists() {
        victim.add("a url") ;
        victim.add("a URL") ;
    }

    @Test
    public void load() throws Exception {
        test("game-of-thrones", "Game of Thrones", EPISODES_GAME_OF_THRONES) ;
        test("modern-family", "Modern Family", EPISODES_MODERN_FAMILY) ;
    }

    private void test(final String resource, final String showTitle, final int[][] pairs) throws Exception {
        Feed feed = new Feed(getResource(String.format("feeds/%s.xml", resource)).toString()) ;
        Show show = shows.save(new Show(showTitle)) ;

        victim.load(feed) ;

        List<Episode> expectedEpisodes = buildExpectedEpisodes(show, pairs) ;
        Collections.sort(expectedEpisodes) ;
        assertEquals(expectedEpisodes, episodes.findByShow(show, SORT_ORDER)) ;

    }

    private List<Episode> buildExpectedEpisodes(final Show show, final int[][] pairs) {
        List<Episode> episodes = new ArrayList<>() ;
        for (int[] p : pairs) {
            episodes.add(new Episode(show, p[0], p[1])) ;
        }
        return episodes ;
    }
}