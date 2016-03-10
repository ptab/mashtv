package me.taborda.mashtv.service ;

import java.util.List;
import java.util.Optional;
import javax.annotation.Resource;

import me.taborda.mashtv.exception.NonUniqueException;
import me.taborda.mashtv.model.Show;
import me.taborda.mashtv.repository.ShowRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ShowService {

    @Resource
    private ShowRepository repository ;

    @Transactional(readOnly = true)
    public Show find(final long id) {
        return repository.findOne(id) ;
    }

    @Transactional(readOnly = true)
    public Optional<Show> find(final String title) {
        return repository.findByTitleIgnoreCase(title) ;
    }

    @Transactional(readOnly = true)
    public List<Show> findAll() {
        return repository.findAll() ;
    }

    @Transactional
    public Show add(final String title) {
        if (find(title).isPresent()) {
            throw new NonUniqueException(title) ;
        }

        return repository.save(new Show(title)) ;
    }

    @Transactional
    public void delete(final Show show) {
        repository.delete(show) ;
    }

}