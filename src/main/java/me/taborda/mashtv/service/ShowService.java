package me.taborda.mashtv.service ;

import java.util.List ;

import me.taborda.mashtv.dao.EpisodeDAO ;
import me.taborda.mashtv.dao.ShowDAO ;
import me.taborda.mashtv.model.Episode ;
import me.taborda.mashtv.model.Show ;

import org.springframework.beans.factory.annotation.Autowired ;
import org.springframework.stereotype.Service ;
import org.springframework.transaction.annotation.Transactional ;

@Service
public class ShowService {

	@Autowired(required = true)
	private ShowDAO showDAO ;

	@Autowired(required = true)
	private EpisodeDAO episodeDAO ;

	@Transactional(readOnly = true)
	public boolean contains(String title) {
		if (getShow(title) != null)
			return true ;
		return false ;
	}

	@Transactional(readOnly = true)
	public Show getShow(int id) {
		return showDAO.get(id) ;
	}

	@Transactional(readOnly = true)
	public Show getShow(String show_title) {
		return showDAO.get(show_title) ;
	}

	@Transactional(readOnly = true)
	public Show getShowEager(String show_title) {
		Show show = showDAO.get(show_title) ;
		if (show != null)
			show.getEpisodes().size() ;
		return show ;
	}

	@Transactional(readOnly = true)
	public List<Show> getShows() {
		return showDAO.getAll() ;
	}

	@Transactional(readOnly = true)
	public List<Episode> getEpisodes(Show show) {
		return episodeDAO.get(show) ;
	}

	@Transactional(readOnly = true)
	public Episode getEpisode(Show show, int season_number, int episode_number) {
		return episodeDAO.get(show, season_number, episode_number) ;
	}

	@Transactional(readOnly = true)
	public List<Episode> getLatestEpisodes(Show show, int quantity) {
		return episodeDAO.getLatestEpisodes(show, quantity) ;
	}

	@Transactional
	public void saveShow(Show show) {
		showDAO.save(show) ;
	}

	@Transactional
	public void removeShow(Show show) {
		showDAO.delete(show) ;
	}

	@Transactional
	public void saveEpisode(Episode episode) {
		episodeDAO.save(episode) ;
	}

	@Transactional
	public void removeEpisode(Episode episode) {
		episodeDAO.delete(episode) ;
	}
}