package me.taborda.mashtv.unit ;

import static org.mockito.Matchers.any ;
import static org.mockito.Matchers.anyString ;
import static org.mockito.Mockito.never ;
import static org.mockito.Mockito.verify ;
import static org.mockito.Mockito.when ;
import static org.powermock.api.mockito.PowerMockito.mock ;
import static org.powermock.api.mockito.PowerMockito.whenNew ;

import java.io.BufferedReader ;
import java.io.IOException ;
import java.net.MalformedURLException ;
import java.net.URL ;

import org.junit.Before ;
import org.junit.Test ;
import org.junit.runner.RunWith ;
import org.mockito.InjectMocks ;
import org.mockito.Mock ;
import org.powermock.core.classloader.annotations.PrepareForTest ;
import org.powermock.modules.junit4.PowerMockRunner ;

import me.taborda.mashtv.AbstractUnitTest ;
import me.taborda.mashtv.model.Episode ;
import me.taborda.mashtv.model.Show ;
import me.taborda.mashtv.repository.EpisodeRepository ;
import me.taborda.mashtv.service.EpisodeService ;

@RunWith(PowerMockRunner.class)
@PrepareForTest(EpisodeService.class)
public class EpisodeServiceTest extends AbstractUnitTest {

    private static final String EPISODE_INFO = "episodes/ok.txt" ;

    private static final String EPISODE_INFO_WITHOUT_TITLE = "episodes/fail-no-title.txt" ;

    private static final String EXPECTED_TITLE = "Winter is Coming" ;

    @InjectMocks
    private EpisodeService victim ;

    @Mock
    private EpisodeRepository repository ;

    @Mock
    private Episode episode ;

    @Override
    @Before
    public void setup() {
        Show show = mock(Show.class) ;
        when(episode.getShow()).thenReturn(show) ;
        when(episode.getShow().getTitle()).thenReturn("Game of Thrones") ;
        when(episode.getEpisode()).thenReturn(1) ;
    }

    @Test
    public void updateTitle() throws Exception {
        whenNew(URL.class).withArguments(anyString()).thenReturn(getResource(EPISODE_INFO)) ;
        victim.updateTitle(episode) ;
        verify(episode).setTitle(EXPECTED_TITLE) ;
        verify(repository).save(episode) ;
    }

    @Test
    public void updateTitleShouldNotCompleteWhenNoTitleOnEpisodeInfo() throws Exception {
        whenNew(URL.class).withArguments(anyString()).thenReturn(getResource(EPISODE_INFO_WITHOUT_TITLE)) ;
        victim.updateTitle(episode) ;
        verify(repository, never()).save(any(Episode.class)) ;
    }

    @Test
    public void updateTitleShouldNotCompleteWhenMalformedURL() throws Exception {
        whenNew(URL.class).withArguments(anyString()).thenThrow(new MalformedURLException()) ;
        victim.updateTitle(episode) ;
        verify(repository, never()).save(any(Episode.class)) ;
    }

    @Test
    @SuppressWarnings("resource")
    public void updateTitleShouldNotCompleteWhenIOException() throws Exception {
        // prevent the unit test from creating the real URL
        whenNew(URL.class).withArguments(anyString()).thenReturn(getResource(EPISODE_INFO)) ;
        BufferedReader reader = mock(BufferedReader.class) ;
        when(reader.readLine()).thenThrow(new IOException()) ;
        whenNew(BufferedReader.class).withAnyArguments().thenReturn(reader) ;
        victim.updateTitle(episode) ;
        verify(repository, never()).save(any(Episode.class)) ;
    }
}