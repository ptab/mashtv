package me.taborda.mashtv.controller.api;

import java.util.List;
import javax.validation.constraints.NotNull;

import me.taborda.mashtv.model.Feed;
import me.taborda.mashtv.service.FeedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/feeds", produces = "application/json")
public class RestFeedController extends RestBaseController {

    private static final Logger LOG = LoggerFactory.getLogger(RestFeedController.class) ;

    @Autowired
    private FeedService feeds ;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<Feed> list() {
        return feeds.findAll() ;
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public Feed add(@NotNull @RequestParam final String url) {
        Feed feed = feeds.add(url.trim()) ;
        LOG.info("Added feed: {}", feed) ;
        feeds.load(feed) ;
        return feed ;
    }

    @RequestMapping(value = "/{feed}", method = RequestMethod.DELETE)
    public void delete(@ModelAttribute final Feed feed) {
        feeds.delete(feed) ;
        LOG.info("Removed feed: {}", feed) ;
    }

    @RequestMapping(value = "/{feed}/load", method = RequestMethod.POST)
    public void load(@ModelAttribute final Feed feed) {
        feeds.load(feed) ;
    }

}
