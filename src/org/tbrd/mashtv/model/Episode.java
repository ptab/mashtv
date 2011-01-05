package org.tbrd.mashtv.model ;

import java.io.BufferedReader ;
import java.io.IOException ;
import java.io.InputStreamReader ;
import java.io.Serializable ;
import java.net.MalformedURLException ;
import java.net.URL ;
import java.util.HashSet ;
import java.util.Set ;
import java.util.regex.Matcher ;
import java.util.regex.Pattern ;

import javax.persistence.CascadeType ;
import javax.persistence.Entity ;
import javax.persistence.FetchType ;
import javax.persistence.GeneratedValue ;
import javax.persistence.Id ;
import javax.persistence.ManyToOne ;
import javax.persistence.OneToMany ;
import javax.persistence.Transient ;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;
import org.tbrd.mashtv.Util ;

@Entity
public class Episode implements Comparable<Episode>, Serializable {

	private static final long serialVersionUID = 1L ;

	@Transient
	private final Log log = LogFactory.getLog(this.getClass()) ;

	@Id
	@GeneratedValue
	private Integer id ;

	@ManyToOne
	private Show show ;

	private int season ;

	private int episode ;

	private String title ;

	private boolean downloaded ;

	@OneToMany(mappedBy = "episode", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Set<Torrent> torrents ;

	public Episode() {
		setDownloaded(false) ;
		torrents = new HashSet<Torrent>() ;
	}

	public Episode(Show show, int season, int episode) {
		setShow(show) ;
		setSeason(season) ;
		setEpisode(episode) ;
		setDownloaded(false) ;
		torrents = new HashSet<Torrent>() ;
		fetchTitle() ;
	}

	public void fetchTitle() {
		StringBuilder builder = new StringBuilder() ;

		try {
			URL url = new URL("http://services.tvrage.com/tools/quickinfo.php?show="
					+ Util.fixString(getShow().getTitle()).replaceFirst("The ", "").replace(" ", "") + "&ep="
					+ getSeason() + "x" + episode) ;
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream())) ;

			log.debug("url: " + url.toString()) ;
			String line ;

			while ((line = in.readLine()) != null)
				builder.append(line) ;

			in.close() ;
		}
		catch (MalformedURLException e) {
			e.printStackTrace() ;
			title = "Unknown title" ;
		}
		catch (IOException e) {
			e.printStackTrace() ;
			title = "Unknown title" ;
		}

		String contents = builder.toString() ;
		Pattern pattern = Pattern.compile("Episode Info@" + Util.decimal(getSeason()) + "x" + Util.decimal(episode)
				+ "\\^([^\\^]*)\\^") ;
		Matcher matcher = pattern.matcher(contents) ;
		if (matcher.find())
			title = matcher.group(1) ;
		else
			title = "Unknown title" ;
	}

	public String getSubtitle() {
		return "http://www.podnapisi.net/ppodnapisi/search?tbsl=3&asdp=0&sK=" + getShow().getTitle().replace(" ", "+")
				+ "&sJ=2&sT=1&sY=&sAKA2=1&sR=&sTS=" + getSeason() + "&sTE=" + getEpisode() ;
	}

	/*
	 * Getters and setters
	 */

	public Integer getId() {
		return id ;
	}

	public void setId(Integer id) {
		this.id = id ;
	}

	public int getEpisode() {
		return episode ;
	}

	public void setEpisode(int episode) {
		this.episode = episode ;
	}

	public Show getShow() {
		return show ;
	}

	public void setShow(Show show) {
		this.show = show ;
	}

	public int getSeason() {
		return season ;
	}

	public void setSeason(int season) {
		this.season = season ;
	}

	public String getTitle() {
		return title ;
	}

	public void setTitle(String title) {
		this.title = title ;
	}

	public Set<Torrent> getTorrents() {
		return torrents ;
	}

	public void setTorrents(Set<Torrent> torrents) {
		this.torrents = torrents ;
	}

	public void addTorrent(String url, boolean hd) {
		if (url.contains("btjunkie"))
			addTorrent(new Torrent(this, "http://dl." + url.substring(7) + "/download.torrent", hd)) ;
		else
			addTorrent(new Torrent(this, url, hd)) ;
	}

	public void addTorrent(Torrent torrent) {
		torrents.add(torrent) ;
	}

	public Torrent getTorrent(int id) {
		for (Torrent t : getTorrents())
			if (t.getId() == id)
				return t ;
		log.error("No torrent with id " + id + " for episode " + getId()) ;
		return null ;
	}

	public boolean isDownloaded() {
		return downloaded ;
	}

	public void setDownloaded(boolean downloaded) {
		this.downloaded = downloaded ;
	}

	@Override
	public String toString() {
		return getShow().getTitle() + ": Season " + getSeason() + ", Episode " + episode + " - " + title ;
	}

	public String toShortString() {
		return getShow().getTitle().replace(" ", ".") + ".S" + getSeason() + "E" + getEpisode() ;
	}

	@Override
	public int compareTo(Episode o) {
		int ret ;

		if (getShow().getTitle().equalsIgnoreCase(o.getShow().getTitle()))
			if (getSeason() == o.getSeason())
				ret = new Integer(getEpisode()).compareTo(new Integer(o.getEpisode())) ;
			else
				ret = new Integer(getSeason()).compareTo(new Integer(o.getSeason())) ;
		else
			ret = getShow().getTitle().compareToIgnoreCase(o.getShow().getTitle()) ;

		return ret * (-1) ;
	}
}
