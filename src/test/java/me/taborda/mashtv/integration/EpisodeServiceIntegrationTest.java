package me.taborda.mashtv.integration ;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.taborda.mashtv.AbstractIntegrationTest;
import me.taborda.mashtv.model.Episode;
import me.taborda.mashtv.model.Show;
import me.taborda.mashtv.repository.EpisodeRepository;
import me.taborda.mashtv.repository.ShowRepository;
import me.taborda.mashtv.service.EpisodeService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class EpisodeServiceIntegrationTest extends AbstractIntegrationTest {

    private static final int EXPECTED_EPISODES = 5 ;

    @Autowired
    private EpisodeService victim ;

    @Autowired
    private EpisodeRepository repository ;

    @Autowired
    private ShowRepository shows ;

    private Show show ;

    @Before
    public void setup() {
        show = shows.save(new Show("a show")) ;
    }

    @After
    public void cleanup() {
        repository.deleteAll() ;
        shows.deleteAll() ;
    }

    @Test
    public void findLatest() {
        // given a repository populated with episodes from different shows
        List<Episode> allEpisodesFromShow = insertEpisodes(show, 1, 5) ;
        insertEpisodes(shows.save(new Show("another show")), 1, 10) ;
        allEpisodesFromShow.addAll(insertEpisodes(show, 2, 3)) ;

        // when we find the latest 5 episodes
        List<Episode> latestEpisodes = victim.findLatest(show) ;

        // then the returned list should contain only the latest 5 episodes of that show
        assertEquals(EXPECTED_EPISODES, latestEpisodes.size()) ;
        Collections.reverse(allEpisodesFromShow) ;
        for (int i = 0 ; i < EXPECTED_EPISODES ; i++) {
            assertEquals(allEpisodesFromShow.get(i), latestEpisodes.get(i)) ;
        }
    }

    private List<Episode> insertEpisodes(final Show show, final int season, final int episodeCount) {
        List<Episode> episodes = new ArrayList<>() ;
        for (int e = 1 ; e <= episodeCount ; e++) {
            episodes.add(repository.save(new Episode(show, season, e))) ;
        }
        return episodes ;
    }

}