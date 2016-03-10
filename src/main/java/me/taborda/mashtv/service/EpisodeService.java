package me.taborda.mashtv.service;

import java.util.List;
import javax.annotation.Resource;

import me.taborda.mashtv.model.Episode;
import me.taborda.mashtv.model.Show;
import me.taborda.mashtv.repository.EpisodeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EpisodeService {

    private static final Logger LOG = LoggerFactory.getLogger(EpisodeService.class);

    private static final Sort SORT_ORDER = new Sort(Direction.DESC, "season", "episode");

    @Resource
    private EpisodeRepository repository;

    @Transactional(readOnly = true)
    public List<Episode> findLatest(final Show show) {
        return repository.findFirst5ByShow(show, SORT_ORDER);
    }

    @Transactional
    public Episode save(final Episode episode) {
        return repository.save(episode);
    }

    @Transactional
    public void delete(final Episode episode) {
        repository.delete(episode);
    }

}
