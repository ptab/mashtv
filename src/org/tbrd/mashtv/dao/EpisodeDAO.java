package org.tbrd.mashtv.dao ;

import java.util.List ;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;
import org.hibernate.Query ;
import org.springframework.stereotype.Repository ;
import org.tbrd.mashtv.Util ;
import org.tbrd.mashtv.model.Episode ;
import org.tbrd.mashtv.model.Show ;

@Repository
public class EpisodeDAO extends GenericDAO<Episode, Integer> {

	private Log log = LogFactory.getLog(this.getClass()) ;

	public EpisodeDAO() {
		super(Episode.class) ;
	}

	@SuppressWarnings("unchecked")
	public Episode get(Show show, int season_number, int episode_number) {
		Query query = getSession()
				.createQuery(
						"FROM "
								+ Episode.class.getName()
								+ " e WHERE e.show = :show AND e.season = :season AND e.episode = :episode ORDER BY e.season, e.episode DESC") ;
		query.setParameter("show", show) ;
		query.setParameter("season", season_number) ;
		query.setParameter("episode", episode_number) ;
		List<Episode> episodes = query.list() ;
		if ((episodes == null) || (episodes.size() == 0))
			return null ;
		if (episodes.size() > 1) {
			log.info("Found " + episodes.size() + " episodes with id: " + show.getTitle() + " S"
					+ Util.decimal(season_number) + "E" + Util.decimal(episode_number)) ;
			// for (Object e : episodes)
			// log.info(((Episode) e).print()) ;
			log.info("----") ;
		}
		return episodes.get(0) ;
	}

	@SuppressWarnings("unchecked")
	public List<Episode> get(Show show) {
		Query query = getSession().createQuery(
				"FROM " + Episode.class.getName() + " e WHERE e.show = :show ORDER BY e.season DESC, e.episode DESC") ;
		query.setParameter("show", show) ;
		return query.list() ;
	}

	@SuppressWarnings("unchecked")
	public List<Episode> getLatestEpisodes(Show show, int quantity) {
		Query query = getSession().createQuery(
				"FROM " + Episode.class.getName() + " e WHERE e.show = :show ORDER BY e.season DESC, e.episode DESC") ;
		query.setParameter("show", show) ;
		query.setMaxResults(quantity) ;
		return query.list() ;
	}

}
