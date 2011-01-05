package org.tbrd.mashtv.dao ;

import java.util.List ;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;
import org.hibernate.Query ;
import org.springframework.stereotype.Repository ;
import org.tbrd.mashtv.model.Show ;

@Repository
public class ShowDAO extends GenericDAO<Show, Integer> {

	@SuppressWarnings("unused")
	private Log log = LogFactory.getLog(this.getClass()) ;

	public ShowDAO() {
		super(Show.class) ;
	}

	public Show get(String show_title) {
		Query query = getSession().createQuery(
				"FROM " + Show.class.getName() + " s WHERE upper(s.title) = upper(:title)") ;
		query.setParameter("title", show_title) ;
		return (Show) query.uniqueResult() ;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Show> getAll() {
		Query query = getSession().createQuery("FROM " + Show.class.getName() + " ORDER BY title ASC") ;
		return query.list() ;
	}
}
