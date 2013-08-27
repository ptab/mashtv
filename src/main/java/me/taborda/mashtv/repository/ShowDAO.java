package me.taborda.mashtv.repository ;

import java.util.List ;

import org.hibernate.Query ;
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
        Query query = getSession().createQuery("FROM " + Show.class.getName() + " s WHERE upper(s.title) = upper(:title)") ;
        query.setParameter("title", show_title) ;
        return (Show) query.uniqueResult() ;
    }

    public Show getEager(final String show_title) {
        Query query = getSession().createQuery("FROM " + Show.class.getName() + " s WHERE upper(s.title) = upper(:title)") ;
        query.setParameter("title", show_title) ;
        return (Show) query.uniqueResult() ;
    }

    @Override
    public List<Show> getAll() {
        Query query = getSession().createQuery("FROM " + Show.class.getName() + " ORDER BY title ASC") ;
        return query.list() ;
    }
}
