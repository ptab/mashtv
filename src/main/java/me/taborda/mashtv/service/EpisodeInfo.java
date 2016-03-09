package me.taborda.mashtv.service ;

import java.net.MalformedURLException ;
import java.net.URL ;

import org.slf4j.Logger ;
import org.slf4j.LoggerFactory ;
import org.springframework.stereotype.Component ;

import me.taborda.mashtv.model.Episode ;

@Component
public class EpisodeInfo {
    private static final Logger LOG = LoggerFactory.getLogger(EpisodeInfo.class) ;

    private static final String EPISODE_INFO_URL = "http://services.tvrage.com/tools/quickinfo.php?show=%s&ep=%sx%s" ;

    public URL getEpisodeInfoURL(final Episode episode) {
        try {
            return new URL(String.format(EPISODE_INFO_URL, episode.getShow().getTitle().replaceFirst("The ", ""), episode.getSeason(), episode)) ;
        } catch (MalformedURLException e) {
            LOG.warn("Invalid URL", e.getMessage()) ;
            return null ;
        }
    }
}
