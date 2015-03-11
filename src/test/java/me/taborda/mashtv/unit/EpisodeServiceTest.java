package me.taborda.mashtv.unit ;

import static org.mockito.Matchers.any ;
import static org.mockito.Mockito.mock ;
import static org.mockito.Mockito.never ;
import static org.mockito.Mockito.verify ;
import static org.mockito.Mockito.when ;

import java.io.IOException ;
import java.net.URL ;

import org.junit.Before ;
import org.junit.Ignore ;
import org.junit.Rule ;
import org.junit.Test ;
import org.junit.rules.ExpectedException ;
import org.mockito.InjectMocks ;
import org.mockito.Mock ;

import me.taborda.mashtv.AbstractUnitTest ;
import me.taborda.mashtv.model.Episode ;
import me.taborda.mashtv.model.Show ;
import me.taborda.mashtv.repository.EpisodeRepository ;
import me.taborda.mashtv.service.EpisodeInfo ;
import me.taborda.mashtv.service.EpisodeService ;

public class EpisodeServiceTest extends AbstractUnitTest {

    private static final String EPISODE_INFO = "episodes/ok.txt" ;

    private static final String EPISODE_INFO_WITHOUT_TITLE = "episodes/fail-no-title.txt" ;

    private static final String EXPECTED_TITLE = "Winter is Coming" ;

    @InjectMocks
    private EpisodeService victim ;

    @Mock
    private EpisodeRepository repository ;

    @Mock
    private EpisodeInfo episodeInfo ;

    @Mock
    private Episode episode ;

    @Rule
    public ExpectedException exception = ExpectedException.none() ;

    @Override
    @Before
    public void setup() {
        Show show = mock(Show.class) ;
        when(episode.getShow()).thenReturn(show) ;
        when(episode.getShow().getTitle()).thenReturn("Game of Thrones") ;
        when(episode.getEpisode()).thenReturn(1) ;
    }

    @Test
    public void updateTitle() {
        when(episodeInfo.getEpisodeInfoURL(episode)).thenReturn(getResource(EPISODE_INFO)) ;
        victim.updateTitle(episode) ;
        verify(episode).setTitle(EXPECTED_TITLE) ;
        verify(repository).save(episode) ;
    }

    @Test
    public void updateTitleShouldNotCompleteWhenNoTitleOnEpisodeInfo() {
        when(episodeInfo.getEpisodeInfoURL(episode)).thenReturn(getResource(EPISODE_INFO_WITHOUT_TITLE)) ;
        victim.updateTitle(episode) ;
        verify(repository, never()).save(any(Episode.class)) ;
    }

    @Test
    public void updateTitleShouldNotCompleteWhenNoURL() {
        when(episodeInfo.getEpisodeInfoURL(episode)).thenReturn(null) ;
        victim.updateTitle(episode) ;
        verify(repository, never()).save(any(Episode.class)) ;
    }

    @Test
    @Ignore("can't mock URL, don't want to pollute the sources just for this..")
    public void updateTitleShouldNotCompleteWhenIOException() throws Exception {
        URL url = mock(URL.class) ;
        when(url.openStream()).thenThrow(new IOException()) ;
        when(episodeInfo.getEpisodeInfoURL(episode)).thenReturn(url) ;
        exception.expect(IOException.class) ;

        victim.updateTitle(episode) ;
        verify(repository, never()).save(any(Episode.class)) ;
    }
}