package me.taborda.mashtv.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import me.taborda.mashtv.exception.EpisodeNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Entity
public class Show extends AbstractEntity implements Comparable<Show> {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LoggerFactory.getLogger(Show.class);
    public static final int NO_ID = -1 ;

    @Column
    private int traktId = NO_ID ;

    @Column(unique = true)
    @NotNull
    @Size(min = 1)
    private String title;

    @OneToMany(cascade = CascadeType.ALL)
    private final List<Episode> episodes = new ArrayList<>();

    protected Show() {
        // hibernate && JPA
    }

    public Show(@NotNull final String title) {
        this.title = title;
    }

    public int getTraktId() {
        return traktId;
    }

    public void setTraktId(int traktId) {
        this.traktId = traktId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(@NotNull String title) {
        this.title = title;
    }

    @JsonIgnore
    public List<Integer> getSeasons() {
        return episodes.stream().map(Episode::getSeason).distinct().sorted().collect(Collectors.toList());
    }

    public List<Episode> getEpisodes(int season) {
        return episodes.stream().filter(e -> e.getSeason() == season).sorted().collect(Collectors.toList());
    }

    public Optional<Episode> findEpisode(final int season, final int episode) {
        return episodes.stream().filter(e -> e.getSeason() == season && e.getEpisode() == episode).findAny();
    }

    public Episode getEpisode(final int season, final int episode) {
        return findEpisode(season, episode).orElseThrow(() -> new EpisodeNotFoundException(this, season, episode));
    }

    public Episode addEpisode(final int season, final int episode) {
        Episode e = new Episode(this, season, episode);
        episodes.add(e);
        LOG.info("Added episode: {}", e);
        return e;
    }

    @Override
    public int hashCode() {
        return title.hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Show)) {
            return false;
        }
        Show other = (Show) obj;
        return title.equals(other.title);
    }

    @Override
    public int compareTo(final Show o) {
        return getTitle().compareTo(o.getTitle());
    }

    @Override
    public String toString() {
        return getTitle();
    }
}
