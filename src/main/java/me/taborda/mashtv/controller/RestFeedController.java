package me.taborda.mashtv.controller ;

import java.util.List ;

import javax.validation.constraints.NotNull ;

import org.slf4j.Logger ;
import org.slf4j.LoggerFactory ;
import org.springframework.beans.factory.annotation.Autowired ;
import org.springframework.web.bind.annotation.PathVariable ;
import org.springframework.web.bind.annotation.RequestMapping ;
import org.springframework.web.bind.annotation.RequestMethod ;
import org.springframework.web.bind.annotation.RequestParam ;
import org.springframework.web.bind.annotation.RestController ;

import me.taborda.mashtv.model.Feed ;
import me.taborda.mashtv.service.FeedService ;

@RestController
@RequestMapping(value = "/api/feeds", produces = "application/json")
public class RestFeedController extends RestBaseController {

    private static final Logger LOG = LoggerFactory.getLogger(RestFeedController.class) ;

    @Autowired
    private FeedService feeds ;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<Feed> list() {
        return feeds.getAll() ;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Feed add(@NotNull @RequestParam final String url) {
        Feed feed = feeds.add(url.trim()) ;
        LOG.info("Added feed: {}", feed) ;
        feeds.load(feed) ;
        return feed ;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable final long id) {
        Feed f = feeds.get(id) ;
        feeds.delete(f) ;
        LOG.info("Removed feed: {}", f) ;
    }

    @RequestMapping(value = "/{id}/load", method = RequestMethod.POST)
    public void load(@PathVariable final long id) {
        Feed f = feeds.get(id) ;
        feeds.load(f) ;
    }

}
