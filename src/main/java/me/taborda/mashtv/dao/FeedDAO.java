package me.taborda.mashtv.dao ;

import me.taborda.mashtv.model.Feed ;

import org.springframework.stereotype.Repository ;

@Repository
public class FeedDAO extends AbstractDAO<Feed, Integer> {

	public FeedDAO() {
		super(Feed.class) ;
	}
}
