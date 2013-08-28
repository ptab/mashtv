package me.taborda.mashtv.service ;

import java.util.List ;

import javax.annotation.Resource ;

import org.springframework.stereotype.Service ;
import org.springframework.transaction.annotation.Transactional ;

import me.taborda.mashtv.model.Feed ;
import me.taborda.mashtv.repository.FeedRepository ;

@Service
public class FeedService {

    @Resource
    private FeedRepository repository ;

    @Transactional(readOnly = true)
    public List<Feed> getAll() {
        return repository.findAll() ;
    }

    @Transactional(readOnly = true)
    public Feed get(final long id) {
        return repository.findOne(id) ;
    }

    @Transactional
    public void save(final Feed feed) {
        repository.save(feed) ;
    }

    @Transactional
    public void delete(final Feed feed) {
        repository.delete(feed) ;
    }
}