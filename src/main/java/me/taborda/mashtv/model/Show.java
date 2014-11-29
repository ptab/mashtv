package me.taborda.mashtv.model ;

import java.util.ArrayList ;
import java.util.Collections ;
import java.util.List ;

import javax.persistence.CascadeType ;
import javax.persistence.Column ;
import javax.persistence.Entity ;
import javax.persistence.OneToMany ;
import javax.validation.constraints.NotNull ;
import javax.validation.constraints.Size ;

import me.taborda.mashtv.util.Util ;

@Entity
public class Show extends AbstractEntity implements Comparable<Show> {

    private static final long serialVersionUID = 1L ;

    @Column(unique = true)
    @NotNull
    @Size(min = 1)
    private String title ;

    @OneToMany(cascade = CascadeType.ALL)
    private final List<Episode> episodes = new ArrayList<>() ;

    protected Show() {
        // hibernate && JPA
    }

    public Show(final String title) {
        this.title = Util.fixTitle(title) ;
    }

    public String getTitle() {
        return title ;
    }

    public List<Episode> getEpisodes() {
        return Collections.unmodifiableList(episodes) ;
    }

    public Episode getEpisode(final int season, final int episode) {
        for (Episode e : episodes) {
            if (e.getSeason() == season && e.getEpisode() == episode) {
                return e ;
            }
        }
        return null ;
    }

    public Episode addEpisode(final int season, final int episode) {
        Episode e = new Episode(this, season, episode) ;
        episodes.add(e) ;
        return e ;
    }

    @Override
    public int compareTo(final Show o) {
        return getTitle().compareTo(o.getTitle()) ;
    }

    @Override
    public String toString() {
        return getTitle() ;
    }
}
