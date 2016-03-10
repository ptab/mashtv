package me.taborda.mashtv;

import me.taborda.mashtv.enricher.DetailsEnricher;
import me.taborda.mashtv.enricher.trakt.TraktClient;
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
        public DetailsEnricher detailsEnricher() {
            return new DetailsEnricher();
        }
    }

}
