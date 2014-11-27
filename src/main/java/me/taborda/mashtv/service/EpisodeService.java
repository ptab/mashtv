package me.taborda.mashtv.service ;

import java.util.List ;

import javax.annotation.Resource ;

import org.springframework.data.domain.Sort ;
import org.springframework.data.domain.Sort.Direction ;
import org.springframework.stereotype.Service ;
import org.springframework.transaction.annotation.Transactional ;

import me.taborda.mashtv.model.Episode ;
import me.taborda.mashtv.model.Show ;
import me.taborda.mashtv.repository.EpisodeRepository ;

@Service
public class EpisodeService {

    private final static Sort SORT_ORDER = new Sort(Direction.DESC, "season", "episode") ;

    @Resource
    private EpisodeRepository repository ;

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
}
