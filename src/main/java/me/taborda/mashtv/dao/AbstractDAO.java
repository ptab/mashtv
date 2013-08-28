package me.taborda.mashtv.dao ;

import java.io.Serializable ;
import java.util.List ;

import javax.persistence.EntityManager ;
import javax.persistence.PersistenceContext ;

public abstract class AbstractDAO<T, PK extends Serializable> {

    private final Class<T> type ;

    @PersistenceContext
    private EntityManager entityManager ;

    public AbstractDAO(final Class<T> type) {
        this.type = type ;
    }

    public T get(final PK id) {
        return getEntityManager().find(type, id) ;
    }

    @SuppressWarnings("unchecked")
    public List<T> getAll() {
        return getEntityManager().createQuery("FROM " + type.getName()).getResultList() ;
    }

    public void save(final T o) {
        getEntityManager().persist(o) ;
    }

    public T update(final T o) {
        return getEntityManager().merge(o) ;
    }

    public void delete(final T o) {
        getEntityManager().remove(o) ;
    }

    protected EntityManager getEntityManager() {
        return entityManager ;
    }
}
