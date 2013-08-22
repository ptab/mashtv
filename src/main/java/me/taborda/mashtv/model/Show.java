package me.taborda.mashtv.model ;

import java.util.HashSet ;
import java.util.Set ;

import javax.persistence.CascadeType ;
import javax.persistence.Column ;
import javax.persistence.Entity ;
import javax.persistence.FetchType ;
import javax.persistence.OneToMany ;

@Entity
public class Show extends AbstractEntity implements Comparable<Show> {

    private static final long serialVersionUID = 1L ;

    @Column(unique = true)
    private String title ;

    @OneToMany(mappedBy = "show", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Episode> episodes ;

    public Show() {
    }

    public Show(final String title) {
        setTitle(title) ;
        episodes = new HashSet<>() ;
    }

    public String getTitle() {
        return title ;
    }

    public void setTitle(final String title) {
        this.title = title ;
    }

    public Set<Episode> getEpisodes() {
        return episodes ;
    }

    public void setEpisodes(final Set<Episode> episodes) {
        this.episodes = episodes ;
    }

    @Override
    public int compareTo(final Show o) {
        return getTitle().compareTo(o.getTitle()) ;
    }
}
