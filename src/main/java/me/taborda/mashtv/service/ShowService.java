package me.taborda.mashtv.service ;

import java.util.List ;

import javax.annotation.Resource ;

import org.springframework.stereotype.Service ;
import org.springframework.transaction.annotation.Transactional ;

import me.taborda.mashtv.model.Show ;
import me.taborda.mashtv.repository.ShowRepository ;

@Service
public class ShowService {

    @Resource
    private ShowRepository repository ;

    @Transactional(readOnly = true)
    public Show find(final long id) {
        return repository.findOne(id) ;
    }

    @Transactional(readOnly = true)
    public Show find(final String title) {
        return repository.findByTitle(title) ;
    }

    @Transactional(readOnly = true)
    public List<Show> findAll() {
        return repository.findAll() ;
    }

    @Transactional
    public Show save(final Show show) {
        return repository.save(show) ;
    }

    @Transactional
    public void delete(final Show show) {
        repository.delete(show) ;
    }

}