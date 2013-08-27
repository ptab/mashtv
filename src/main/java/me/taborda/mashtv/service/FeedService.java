package me.taborda.mashtv.service ;

import java.util.List ;

import me.taborda.mashtv.model.Feed ;

import org.springframework.beans.factory.annotation.Autowired ;
import org.springframework.stereotype.Service ;
import org.springframework.transaction.annotation.Transactional ;

import me.taborda.mashtv.repository.FeedDAO ;

@Service
public class FeedService {

	@Autowired(required = true)
	private FeedDAO feedDAO ;

	@Transactional(readOnly = true)
	public List<Feed> getAll() {
		return feedDAO.getAll() ;
	}

	@Transactional(readOnly = true)
	public Feed get(int id) {
		return feedDAO.get(id) ;
	}

	@Transactional
	public void save(Feed feed) {
		feedDAO.save(feed) ;
	}

	@Transactional
	public void delete(Feed feed) {
		feedDAO.delete(feed) ;
	}
}