package me.taborda.mashtv.dao ;

import java.util.List ;

import javax.persistence.Query ;

import org.slf4j.Logger ;
import org.slf4j.LoggerFactory ;
import org.springframework.stereotype.Repository ;

import me.taborda.mashtv.model.Show ;

@Repository
public class ShowDAO extends AbstractDAO<Show, Integer> {

    @SuppressWarnings("unused")
    private static final Logger LOG = LoggerFactory.getLogger(ShowDAO.class) ;

    public ShowDAO() {
        super(Show.class) ;
    }

    public Show get(final String show_title) {
        Query query = getEntityManager().createQuery("FROM " + Show.class.getName() + " s WHERE upper(s.title) = upper(:title)") ;
        query.setParameter("title", show_title) ;
        return (Show) query.getSingleResult() ;
    }

    public Show getEager(final String show_title) {
        Query query = getEntityManager().createQuery("FROM " + Show.class.getName() + " s WHERE upper(s.title) = upper(:title)") ;
        query.setParameter("title", show_title) ;
        return (Show) query.getSingleResult() ;
    }

    @Override
    public List<Show> getAll() {
        return getEntityManager().createQuery("FROM " + Show.class.getName() + " ORDER BY title ASC").getResultList() ;
    }
}
