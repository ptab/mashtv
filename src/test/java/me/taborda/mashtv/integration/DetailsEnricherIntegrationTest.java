package me.taborda.mashtv.integration;

import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import me.taborda.mashtv.AbstractIntegrationTest;
import me.taborda.mashtv.enricher.DetailsEnricher;
import me.taborda.mashtv.enricher.trakt.TestTraktShow;
import me.taborda.mashtv.enricher.trakt.TraktClient;
import me.taborda.mashtv.enricher.trakt.TraktShow;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


// TODO mock TraktService
public class DetailsEnricherIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private DetailsEnricher victim;

    @Autowired
    private TraktClient traktClient;

    @Test
    public void findShow() {
        when(traktClient.findShow("game of thrones")).thenReturn(Collections.singletonList(new TestTraktShow().withTitle("Game of Thrones"))) ;
        List<TraktShow> show = victim.findShow("game of thrones");
        show.stream().forEach(s -> System.out.println(s.getTitle()));
    }

}
