package me.taborda.mashtv.service ;

import java.util.List ;

import javax.annotation.Resource ;

import org.springframework.beans.factory.annotation.Autowired ;
import org.springframework.stereotype.Service ;
import org.springframework.transaction.annotation.Transactional ;

import me.taborda.mashtv.dao.EpisodeDAO ;
import me.taborda.mashtv.model.Episode ;
import me.taborda.mashtv.model.Show ;
import me.taborda.mashtv.repository.EpisodeRepository ;

@Service
public class EpisodeService {

    @Resource
    private EpisodeRepository repository ;

    @Autowired(required = true)
    private EpisodeDAO episodeDAO ;

    @Transactional(readOnly = true)
    public List<Episode> findAll(final Show show) {
        return episodeDAO.get(show) ;
    }

    @Transactional(readOnly = true)
    public Episode find(final Show show, final int season, final int episode) {
        return episodeDAO.get(show, season, episode) ;
    }

    @Transactional(readOnly = true)
    public List<Episode> findLatest(final Show show, final int quantity) {
        return episodeDAO.getLatestEpisodes(show, quantity) ;
    }

    @Transactional
    public Episode create(final Show show, final int season, final int episode) {
        return save(new Episode(show, season, episode)) ;
    }

    @Transactional
    public Episode save(final Episode episode) {
        return repository.save(episode) ;
    }

    @Transactional
    public void delete(final Episode episode) {
        repository.delete(episode) ;
    }
}
