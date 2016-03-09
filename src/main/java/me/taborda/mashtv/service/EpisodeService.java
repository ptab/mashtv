package me.taborda.mashtv.service ;

import java.io.BufferedReader ;
import java.io.IOException ;
import java.io.InputStreamReader ;
import java.net.URL ;
import java.util.List ;
import java.util.regex.Matcher ;
import java.util.regex.Pattern ;

import javax.annotation.Resource ;

import org.slf4j.Logger ;
import org.slf4j.LoggerFactory ;
import org.springframework.beans.factory.annotation.Autowired ;
import org.springframework.data.domain.Sort ;
import org.springframework.data.domain.Sort.Direction ;
import org.springframework.stereotype.Service ;
import org.springframework.transaction.annotation.Transactional ;

import me.taborda.mashtv.model.Episode ;
import me.taborda.mashtv.model.Show ;
import me.taborda.mashtv.repository.EpisodeRepository ;

@Service
public class EpisodeService {

    private static final Logger LOG = LoggerFactory.getLogger(EpisodeService.class) ;

    private static final Sort SORT_ORDER = new Sort(Direction.DESC, "season", "episode") ;

    private static final Pattern EPISODE_TITLE_PATTERN = Pattern.compile("Episode Info@\\d\\dx\\d\\d\\^(.+?)\\^", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE) ;

    @Resource
    private EpisodeRepository repository ;

    @Autowired
    private EpisodeInfo episodeInfo ;

    @Transactional(readOnly = true)
    public List<Episode> findLatest(final Show show) {
        return repository.findFirst5ByShow(show, SORT_ORDER) ;
    }

    @Transactional
    public Episode save(final Episode episode) {
        return repository.save(episode) ;
    }

    @Transactional
    public void delete(final Episode episode) {
        repository.delete(episode) ;
    }

    @Transactional
    public void updateTitle(final Episode episode) {
        URL url = episodeInfo.getEpisodeInfoURL(episode) ;
        if (url == null) {
            return ;
        }

        LOG.debug("url: " + url.toString()) ;
        try (BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()))) {
            String line ;

            while ((line = in.readLine()) != null) {
                Matcher matcher = EPISODE_TITLE_PATTERN.matcher(line) ;
                if (matcher.find()) {
                    episode.setTitle(matcher.group(1)) ;
                    save(episode) ;
                    break ;
                }
            }
        } catch (IOException e) {
            LOG.warn("Unable to update episode info", e) ;
        }
    }

}
