package me.taborda.mashtv.model ;

import java.io.BufferedReader ;
import java.io.IOException ;
import java.io.InputStreamReader ;
import java.net.MalformedURLException ;
import java.net.URL ;
import java.util.HashSet ;
import java.util.Set ;
import java.util.regex.Matcher ;
import java.util.regex.Pattern ;

import javax.persistence.CascadeType ;
import javax.persistence.Entity ;
import javax.persistence.FetchType ;
import javax.persistence.ManyToOne ;
import javax.persistence.OneToMany ;

import org.slf4j.Logger ;
import org.slf4j.LoggerFactory ;

import me.taborda.mashtv.Util ;

@Entity
public class Episode extends AbstractEntity implements Comparable<Episode> {

    private static final long serialVersionUID = 1L ;

    private static final Logger LOG = LoggerFactory.getLogger(Episode.class) ;

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
        torrents = new HashSet<>() ;
    }

    public Episode(final Show show, final int season, final int episode) {
        setShow(show) ;
        setSeason(season) ;
        setEpisode(episode) ;
        setDownloaded(false) ;
        torrents = new HashSet<>() ;
        fetchTitle() ;
    }

    public void fetchTitle() {
        StringBuilder builder = new StringBuilder() ;

        try {
            URL url = new URL("http://services.tvrage.com/tools/quickinfo.php?show="
                            + Util.fixString(getShow().getTitle()).replaceFirst("The ", "").replace(" ", "") + "&ep=" + getSeason() + "x" + episode) ;
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream())) ;

            LOG.debug("url: " + url.toString()) ;
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
        Pattern pattern = Pattern.compile("Episode Info@" + Util.decimal(getSeason()) + "x" + Util.decimal(episode) + "\\^([^\\^]*)\\^") ;
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

    public int getEpisode() {
        return episode ;
    }

    public void setEpisode(final int episode) {
        this.episode = episode ;
    }

    public Show getShow() {
        return show ;
    }

    public void setShow(final Show show) {
        this.show = show ;
    }

    public int getSeason() {
        return season ;
    }

    public void setSeason(final int season) {
        this.season = season ;
    }

    public String getTitle() {
        return title ;
    }

    public void setTitle(final String title) {
        this.title = title ;
    }

    public Set<Torrent> getTorrents() {
        return torrents ;
    }

    public void setTorrents(final Set<Torrent> torrents) {
        this.torrents = torrents ;
    }

    public void addTorrent(final Torrent torrent) {
        torrents.add(torrent) ;
    }

    public Torrent getTorrent(final int id) {
        for (Torrent t : getTorrents())
            if (t.getId() == id)
                return t ;
        LOG.error("No torrent with id " + id + " for episode " + getId()) ;
        return null ;
    }

    public boolean isDownloaded() {
        return downloaded ;
    }

    public void setDownloaded(final boolean downloaded) {
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
    public int compareTo(final Episode o) {
        int ret ;

        if (getShow().getTitle().equalsIgnoreCase(o.getShow().getTitle()))
            if (getSeason() == o.getSeason())
                ret = new Integer(getEpisode()).compareTo(new Integer(o.getEpisode())) ;
            else
                ret = new Integer(getSeason()).compareTo(new Integer(o.getSeason())) ;
        else
            ret = getShow().getTitle().compareToIgnoreCase(o.getShow().getTitle()) ;

        return ret * -1 ;
    }
}
