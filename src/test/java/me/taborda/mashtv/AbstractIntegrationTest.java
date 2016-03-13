package me.taborda.mashtv;

import me.taborda.mashtv.tracker.ShowDetailsEnricher;
import me.taborda.mashtv.tracker.ShowFinder;
import me.taborda.mashtv.tracker.trakt.TraktClient;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@SpringApplicationConfiguration
public abstract class AbstractIntegrationTest extends AbstractTest {

    @Configuration
    @ComponentScan({"me.taborda.mashtv.repository", "me.taborda.mashtv.service"})
    @EnableAutoConfiguration
    static class TestConfig {

        @Bean
        public TraktClient traktClient() {
            return Mockito.mock(TraktClient.class);
        }

        @Bean
        public ShowDetailsEnricher detailsEnricher() {
            return new ShowDetailsEnricher();
        }

        @Bean
        public ShowFinder showFinder() {
            return new ShowFinder();
        }

    }

}
