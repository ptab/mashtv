package me.taborda.mashtv.unit ;

import static org.mockito.Mockito.verifyNoMoreInteractions ;
import static org.mockito.Mockito.when ;

import org.junit.Test ;
import org.mockito.InjectMocks ;
import org.mockito.Mock ;

import me.taborda.mashtv.AbstractUnitTest ;
import me.taborda.mashtv.model.Feed ;
import me.taborda.mashtv.repository.FeedRepository ;
import me.taborda.mashtv.service.EpisodeService ;
import me.taborda.mashtv.service.FeedService ;
import me.taborda.mashtv.service.ShowService ;

public class FeedServiceTest extends AbstractUnitTest {

    @InjectMocks
    private FeedService victim ;

    @Mock
    private FeedRepository repository ;

    @Mock
    private ShowService shows ;

    @Mock
    private EpisodeService episodes ;

    @Mock
    private Feed feed ;

    @Test
    public void loadShouldNotFailWhenMalformedURL() {
        String url = "malformed URL" ;
        when(feed.getUrl()).thenReturn(url) ;
        victim.load(feed) ;
        verifyNoMoreInteractions(shows, episodes) ;
    }

}