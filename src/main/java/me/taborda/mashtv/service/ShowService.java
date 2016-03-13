package me.taborda.mashtv.service;

import java.util.List;
import java.util.Optional;
import javax.annotation.Resource;

import me.taborda.mashtv.tracker.ShowDetailsEnricher;
import me.taborda.mashtv.tracker.trakt.TraktClient;
import me.taborda.mashtv.tracker.trakt.TraktShow;
import me.taborda.mashtv.exception.NonUniqueException;
import me.taborda.mashtv.model.Show;
import me.taborda.mashtv.repository.ShowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ShowService {

    @Autowired
    private ShowDetailsEnricher enricher;

    @Autowired
    private TraktClient traktClient ;

    @Resource
    private ShowRepository repository;

    @Transactional(readOnly = true)
    public Show find(final long id) {
        return repository.findOne(id);
    }

    @Transactional(readOnly = true)
    public Optional<Show> find(final String title) {
        return repository.findByTitleIgnoreCase(title);
    }

    @Transactional(readOnly = true)
    public List<Show> findAll() {
        return repository.findAll();
    }

    @Transactional
    public Show add(final String title) {
        Show s = new Show(title) ;
        assertTitleIsUnique(s);

        enricher.enrich(s) ;
        assertTitleIsUnique(s); // make sure the title is still unique after it's enriched.

        return repository.save(s);
    }

    private void assertTitleIsUnique(Show show) {
        if (find(show.getTitle()).isPresent()) {
            throw new NonUniqueException(show.getTitle());
        }
    }

    @Transactional
    public void delete(final Show show) {
        repository.delete(show);
    }

}