package org.tbrd.mashtv.dao ;

import org.springframework.stereotype.Repository ;
import org.tbrd.mashtv.model.Feed ;

@Repository
public class FeedDAO extends GenericDAO<Feed, Integer> {

	public FeedDAO() {
		super(Feed.class) ;
	}
}
