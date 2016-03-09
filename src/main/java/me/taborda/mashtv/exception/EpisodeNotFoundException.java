package me.taborda.mashtv.exception;

import me.taborda.mashtv.model.Show;

public class EpisodeNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -60441491295589110L ;

    public EpisodeNotFoundException(final Show show, final int season, final int episode) {
        super(String.format("No episode on %s with season %d and episode %d", show.getTitle(), season, episode)) ;
    }

}
