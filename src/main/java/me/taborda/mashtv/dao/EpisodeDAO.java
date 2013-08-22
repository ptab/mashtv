package me.taborda.mashtv.dao ;

import java.util.List ;

import org.hibernate.Query ;
import org.slf4j.Logger ;
import org.slf4j.LoggerFactory ;
import org.springframework.stereotype.Repository ;

import me.taborda.mashtv.Util ;
import me.taborda.mashtv.model.Episode ;
import me.taborda.mashtv.model.Show ;

@Repository
public class EpisodeDAO extends AbstractDAO<Episode, Integer> {

    private static final Logger LOG = LoggerFactory.getLogger(EpisodeDAO.class) ;

    public EpisodeDAO() {
        super(Episode.class) ;
    }

    public Episode get(final Show show, final int season_number, final int episode_number) {
        Query query = getSession()
                        .createQuery("FROM " + Episode.class.getName()
                                        + " e WHERE e.show = :show AND e.season = :season AND e.episode = :episode ORDER BY e.season, e.episode DESC") ;
        query.setParameter("show", show) ;
        query.setParameter("season", season_number) ;
        query.setParameter("episode", episode_number) ;
        List<Episode> episodes = query.list() ;
        if (episodes == null || episodes.size() == 0)
            return null ;
        if (episodes.size() > 1) {
            LOG.info("Found " + episodes.size() + " episodes with id: " + show.getTitle() + " S" + Util.decimal(season_number) + "E"
                            + Util.decimal(episode_number)) ;
            // for (Object e : episodes)
            // LOG.info(((Episode) e).print()) ;
            LOG.info("----") ;
        }
        return episodes.get(0) ;
    }

    public List<Episode> get(final Show show) {
        Query query = getSession().createQuery("FROM " + Episode.class.getName() + " e WHERE e.show = :show ORDER BY e.season DESC, e.episode DESC") ;
        query.setParameter("show", show) ;
        return query.list() ;
    }

    public List<Episode> getLatestEpisodes(final Show show, final int quantity) {
        Query query = getSession().createQuery("FROM " + Episode.class.getName() + " e WHERE e.show = :show ORDER BY e.season DESC, e.episode DESC") ;
        query.setParameter("show", show) ;
        query.setMaxResults(quantity) ;
        return query.list() ;
    }

}
