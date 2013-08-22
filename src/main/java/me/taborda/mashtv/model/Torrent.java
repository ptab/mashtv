package me.taborda.mashtv.model ;

import javax.persistence.Entity ;
import javax.persistence.ManyToOne ;

@Entity
public class Torrent extends AbstractEntity implements Comparable<Torrent> {

    private static final long serialVersionUID = 1L ;

    @ManyToOne
    private Episode episode ;

    private boolean hd ;

    private String url ;

    private String filename ;

    public Torrent() {
    }

    public Torrent(final Episode episode, final String filename, final String torrent, final boolean hd) {
        setEpisode(episode) ;
        setFilename(filename) ;
        setHd(hd) ;
        if (torrent.contains("btjunkie"))
            setUrl("http://dl." + torrent.substring(7) + "/download.torrent") ;
        else
            setUrl(torrent) ;
    }

    public boolean isHd() {
        return hd ;
    }

    public void setHd(final boolean hd) {
        this.hd = hd ;
    }

    public String getUrl() {
        return url ;
    }

    public void setUrl(final String url) {
        this.url = url ;
    }

    public Episode getEpisode() {
        return episode ;
    }

    public void setEpisode(final Episode episode) {
        this.episode = episode ;
    }

    public String getFilename() {
        return filename ;
    }

    public void setFilename(final String filename) {
        this.filename = filename ;
    }

    @Override
    public int compareTo(final Torrent o) {
        if (isHd() && !o.isHd())
            return 1 ;
        if (!isHd() && o.isHd())
            return -1 ;
        return url.compareTo(o.getUrl()) ;
    }

}
