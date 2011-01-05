package org.tbrd.mashtv.controller ;

import java.util.ArrayList ;
import java.util.HashMap ;
import java.util.List ;
import java.util.Map ;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;
import org.springframework.beans.factory.annotation.Autowired ;
import org.springframework.stereotype.Controller ;
import org.springframework.web.bind.annotation.RequestMapping ;
import org.tbrd.mashtv.model.Episode ;
import org.tbrd.mashtv.model.Show ;
import org.tbrd.mashtv.service.ShowService ;

@Controller
@RequestMapping("/latest")
public class LatestController {

	private Log log = LogFactory.getLog(this.getClass()) ;

	@Autowired(required = true)
	public ShowService shows ;

	@RequestMapping(value = "")
	public String list(Map<String, Object> map) {
		Map<Integer, List<Episode>> latest = new HashMap<Integer, List<Episode>>() ;
		List<Show> ss = shows.getShows() ;

		for (Show s : ss) {
			List<Episode> episodes = new ArrayList<Episode>() ;
			episodes.addAll(shows.getLatestEpisodes(s, 3)) ;
			latest.put(s.getId(), episodes) ;
		}

		map.put("shows", ss) ;
		map.put("latest", latest) ;
		return "latest" ;
	}
}
