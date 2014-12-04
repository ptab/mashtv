package me.taborda.mashtv ;

import org.springframework.beans.factory.annotation.Autowired ;
import org.springframework.scheduling.annotation.Scheduled ;
import org.springframework.stereotype.Component ;

import me.taborda.mashtv.model.Feed ;
import me.taborda.mashtv.service.FeedService ;

@Component
public class FeedLoader {

    @Autowired
    private FeedService feeds ;

    @Scheduled(fixedDelay = 60 * 60 * 1000)
    public void loadFeeds() {
        for (Feed f : feeds.getAll()) {
            feeds.load(f) ;
        }
    }
}
