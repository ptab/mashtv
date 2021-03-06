package me.taborda.mashtv.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Entity
public class Episode extends AbstractEntity implements Comparable<Episode> {

    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(Episode.class);

    public static final String UNKNOWN_TITLE = "Unknown title";

    @JsonIgnore
    @ManyToOne(optional = false)
    private Show show;

    @Min(1)
    private int season;

    @Min(1)
    private int episode;

    @NotNull
    @Size(min = 1)
    private String title;

    @OneToMany(cascade = CascadeType.ALL)
    private final Set<MagnetLink> magnetLinks = new HashSet<>();

    protected Episode() {
        // hibernate && JPA
    }

    public Episode(final Show show, final int season, final int episode) {
        this(show, season, episode, UNKNOWN_TITLE);
    }

    public Episode(final Show show, final int season, final int episode, final String title) {
        this.show = show;
        this.season = season;
        this.episode = episode;
        setTitle(title);
    }

    /*
     * Getters and setters
     */

    public int getEpisode() {
        return episode;
    }

    public Show getShow() {
        return show;
    }

    public int getSeason() {
        return season;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(@NotNull final String title) {
        if (StringUtils.isEmpty(title)) {
            this.title = UNKNOWN_TITLE;
        } else {
            this.title = title;
        }
    }

    public Set<MagnetLink> getMagnetLinks() {
        return Collections.unmodifiableSet(magnetLinks);
    }

    public void addMagnetLink(final String url, final String filename, final boolean isHd) {
        magnetLinks.add(new MagnetLink(url, filename, isHd));
        LOG.info("Added new link for episode {}", toShortString());
    }

    public Optional<MagnetLink> getMagnetLink(final int id) {
        return magnetLinks.stream().filter(l -> l.getId() == id).findAny();
    }

    @JsonIgnore
    public boolean isTitleUnknown() {
        return UNKNOWN_TITLE.equals(title);
    }

    @Override
    public String toString() {
        return String.format("%s: Season %d, Episode %d - %s", show.getTitle(), season, episode, title);
    }

    public String toShortString() {
        return String.format("%s.S%dE%d", show.getTitle().replace(" ", "."), season, episode);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Episode)) return false;
        Episode e = (Episode) o;
        return season == e.season && episode == e.episode && Objects.equals(show, e.show);
    }

    @Override
    public int hashCode() {
        return Objects.hash(show, season, episode);
    }

    @Override
    public int compareTo(final Episode o) {
        return -1 * new CompareToBuilder().append(show, o.getShow()).append(season, o.getSeason()).append(episode, o.getEpisode()).build();
    }

}
