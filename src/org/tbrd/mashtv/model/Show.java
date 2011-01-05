package org.tbrd.mashtv.model ;

import java.io.Serializable ;
import java.util.HashSet ;
import java.util.Set ;

import javax.persistence.CascadeType ;
import javax.persistence.Column ;
import javax.persistence.Entity ;
import javax.persistence.FetchType ;
import javax.persistence.GeneratedValue ;
import javax.persistence.Id ;
import javax.persistence.OneToMany ;

@Entity
public class Show implements Comparable<Show>, Serializable {

	private static final long serialVersionUID = 1L ;

	@Id
	@GeneratedValue
	private Integer id ;

	@Column(unique = true)
	private String title ;

	@OneToMany(mappedBy = "show", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<Episode> episodes ;

	public Show() {
	}

	public Show(String title) {
		setTitle(title) ;
		episodes = new HashSet<Episode>() ;
	}

	public String getTitle() {
		return title ;
	}

	public void setTitle(String title) {
		this.title = title ;
	}

	public Set<Episode> getEpisodes() {
		return episodes ;
	}

	public void setEpisodes(Set<Episode> episodes) {
		this.episodes = episodes ;
	}

	public void setId(Integer id) {
		this.id = id ;
	}

	public Integer getId() {
		return id ;
	}

	@Override
	public int compareTo(Show o) {
		return getTitle().compareTo(o.getTitle()) ;
	}
}
