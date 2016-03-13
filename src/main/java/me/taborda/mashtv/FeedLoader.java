package me.taborda.mashtv;

import me.taborda.mashtv.service.FeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class FeedLoader {

    @Autowired
    private FeedService feeds;

    @Scheduled(fixedDelay = 60 * 60 * 1000)
    public void loadFeeds() {
        feeds.findAll().stream().forEach(f -> feeds.load(f)) ;
    }
}
