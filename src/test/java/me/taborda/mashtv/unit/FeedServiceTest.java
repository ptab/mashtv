package me.taborda.mashtv.unit ;

import static org.mockito.Matchers.any ;
import static org.mockito.Mockito.mock ;
import static org.mockito.Mockito.when ;
import static org.powermock.api.mockito.PowerMockito.whenNew ;

import java.io.IOException ;
import java.io.Reader ;
import java.net.MalformedURLException ;
import java.net.URL ;

import org.junit.Test ;
import org.junit.runner.RunWith ;
import org.mockito.InjectMocks ;
import org.mockito.Mock ;
import org.powermock.core.classloader.annotations.PrepareForTest ;
import org.powermock.modules.junit4.PowerMockRunner ;

import com.rometools.rome.io.FeedException ;
import com.rometools.rome.io.SyndFeedInput ;
import com.rometools.rome.io.XmlReader ;

import me.taborda.mashtv.AbstractUnitTest ;
import me.taborda.mashtv.model.Feed ;
import me.taborda.mashtv.repository.FeedRepository ;
import me.taborda.mashtv.service.EpisodeService ;
import me.taborda.mashtv.service.FeedService ;
import me.taborda.mashtv.service.ShowService ;

@RunWith(PowerMockRunner.class)
@PrepareForTest(FeedService.class)
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

    @Test(expected = RuntimeException.class)
    public void loadShouldFailWhenMalformedURL() throws Exception {
        whenNew(URL.class).withArguments(feed.getUrl()).thenThrow(new MalformedURLException()) ;
        victim.load(feed) ;
    }

    @Test(expected = RuntimeException.class)
    public void loadShouldFailWhenIOException() throws Exception {
        whenNew(XmlReader.class).withAnyArguments().thenThrow(new IOException()) ;
        victim.load(feed) ;
    }

    @Test(expected = RuntimeException.class)
    public void loadShouldFailWhenFeedException() throws Exception {
        SyndFeedInput input = mock(SyndFeedInput.class) ;
        whenNew(SyndFeedInput.class).withNoArguments().thenReturn(input) ;
        when(input.build(any(Reader.class))).thenThrow(new FeedException("exception")) ;
        victim.load(feed) ;
    }

}