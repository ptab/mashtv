package org.tbrd.mashtv.model ;

import javax.persistence.Entity ;
import javax.persistence.GeneratedValue ;
import javax.persistence.Id ;
import javax.persistence.ManyToOne ;

@Entity
public class Torrent implements Comparable<Torrent> {

	@Id
	@GeneratedValue
	private int id ;

	@ManyToOne
	private Episode episode ;

	private boolean hd ;

	private String url ;

	private String filename ;

	public Torrent() {
	}

	public Torrent(Episode episode, String filename, String torrent, boolean hd) {
		setEpisode(episode) ;
		setFilename(filename) ;
		setHd(hd) ;
		if (torrent.contains("btjunkie"))
			setUrl("http://dl." + torrent.substring(7) + "/download.torrent") ;
		else
			setUrl(torrent) ;
	}

	public int getId() {
		return id ;
	}

	public void setId(int id) {
		this.id = id ;
	}

	public boolean isHd() {
		return hd ;
	}

	public void setHd(boolean hd) {
		this.hd = hd ;
	}

	public String getUrl() {
		return url ;
	}

	public void setUrl(String url) {
		this.url = url ;
	}

	public Episode getEpisode() {
		return episode ;
	}

	public void setEpisode(Episode episode) {
		this.episode = episode ;
	}

	public String getFilename() {
		return filename ;
	}

	public void setFilename(String filename) {
		this.filename = filename ;
	}

	@Override
	public int compareTo(Torrent o) {
		if (isHd() && !o.isHd())
			return 1 ;
		if (!isHd() && o.isHd())
			return -1 ;
		return url.compareTo(o.getUrl()) ;
	}

}
