package me.taborda.mashtv.model ;

import java.util.Collections ;
import java.util.HashSet ;
import java.util.Set ;

import javax.persistence.CascadeType ;
import javax.persistence.Entity ;
import javax.persistence.ManyToOne ;
import javax.persistence.OneToMany ;
import javax.validation.constraints.Min ;
import javax.validation.constraints.NotNull ;
import javax.validation.constraints.Size ;

import org.slf4j.Logger ;
import org.slf4j.LoggerFactory ;

import com.fasterxml.jackson.annotation.JsonIgnore ;

@Entity
public class Episode extends AbstractEntity implements Comparable<Episode> {

    private static final long serialVersionUID = 1L ;

    private static final Logger LOG = LoggerFactory.getLogger(Episode.class) ;

    private static final String UNKNOWN_TITLE = "Unknown title" ;

    @JsonIgnore
    @ManyToOne(optional = false)
    private Show show ;

    @Min(1)
    private int season ;

    @Min(1)
    private int episode ;

    @NotNull
    @Size(min = 1)
    private String title ;

    private boolean downloaded = false ;

    @OneToMany(cascade = CascadeType.ALL)
    private final Set<MagnetLink> magnetLinks = new HashSet<>() ;

    protected Episode() {
        // hibernate && JPA
    }

    public Episode(final Show show, final int season, final int episode) {
        this(show, season, episode, UNKNOWN_TITLE) ;
    }

    public Episode(final Show show, final int season, final int episode, final String title) {
        this.show = show ;
        this.season = season ;
        this.episode = episode ;
        this.title = title ;
    }

    /*
     * Getters and setters
     */

    public int getEpisode() {
        return episode ;
    }

    public Show getShow() {
        return show ;
    }

    public int getSeason() {
        return season ;
    }

    public String getTitle() {
        return title ;
    }

    public void setTitle(final String title) {
        this.title = title ;
    }

    public Set<MagnetLink> getMagnetLinks() {
        return Collections.unmodifiableSet(magnetLinks) ;
    }

    public void addMagnetLink(final String url, final String filename, final boolean isHd) {
        magnetLinks.add(new MagnetLink(url, filename, isHd)) ;
    }

    public MagnetLink getMagnetLink(final int id) {
        for (MagnetLink t : getMagnetLinks()) {
            if (t.getId() == id) {
                return t ;
            }
        }
        LOG.error("No torrent with id " + id + " for episode " + getId()) ;
        return null ;
    }

    public boolean isDownloaded() {
        return downloaded ;
    }

    public void setDownloaded(final boolean downloaded) {
        this.downloaded = downloaded ;
    }

    @JsonIgnore
    public boolean isTitleUnknown() {
        return UNKNOWN_TITLE.equals(title) ;
    }

    @Override
    public String toString() {
        return getShow().getTitle() + ": Season " + getSeason() + ", Episode " + episode + " - " + title ;
    }

    public String toShortString() {
        return getShow().getTitle().replace(" ", ".") + ".S" + getSeason() + "E" + getEpisode() ;
    }

    @Override
    public int compareTo(final Episode o) {
        int ret ;

        if (getShow().getTitle().equalsIgnoreCase(o.getShow().getTitle())) {
            if (getSeason() == o.getSeason()) {
                ret = new Integer(getEpisode()).compareTo(new Integer(o.getEpisode())) ;
            } else {
                ret = new Integer(getSeason()).compareTo(new Integer(o.getSeason())) ;
            }
        } else {
            ret = getShow().getTitle().compareToIgnoreCase(o.getShow().getTitle()) ;
        }

        return ret * -1 ;
    }
}
