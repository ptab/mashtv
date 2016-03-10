package me.taborda.mashtv.service ;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

import me.taborda.mashtv.model.Episode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class EpisodeDetailsFetcher {
    private static final Logger LOG = LoggerFactory.getLogger(EpisodeDetailsFetcher.class) ;

    private static final String EPISODE_INFO_URL = "http://services.tvrage.com/tools/quickinfo.php?show=%s&ep=%sx%s" ;

    public Optional<URL> getEpisodeInfoURL(final Episode episode) {
        try {
            return Optional.of(new URL(String.format(EPISODE_INFO_URL, episode.getShow().getTitle().replaceFirst("The ", ""), episode.getSeason(), episode))) ;
        } catch (MalformedURLException e) {
            LOG.warn("Invalid URL", e.getMessage()) ;
            return Optional.empty() ;
        }
    }
}
