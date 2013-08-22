package me.taborda.mashtv.dao ;

import java.io.Serializable ;
import java.util.List ;

import org.hibernate.Session ;
import org.hibernate.SessionFactory ;
import org.springframework.beans.factory.annotation.Autowired ;

public abstract class AbstractDAO<T, PK extends Serializable> {

	private Class<T> type ;

	@Autowired
	private SessionFactory sessionFactory ;

	public AbstractDAO(Class<T> type) {
		this.type = type ;
	}

	public T get(PK id) {
		return (T) getSession().get(type, id) ;
	}

	public List<T> getAll() {
		return getSession().createQuery("FROM " + type.getName()).list() ;
	}

	public T save(T o) {
		return (T) getSession().merge(o) ;
	}

	public void delete(T o) {
		getSession().delete(o) ;
	}

	protected Session getSession() {
		return sessionFactory.getCurrentSession() ;
	}
}
