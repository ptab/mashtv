package me.taborda.mashtv.enricher.trakt;

public class TraktShow {

    protected String title;
    protected String overview;
    protected int year;
    protected Images poster = new Images();
    protected Ids ids = new Ids() ;

    public int getTraktId() {
        return ids.getTrakt();
    }

    public String getTitle() {
        return title;
    }

    class Images {
        protected String full;
        protected String medium;
        protected String thumb;
    }

    class Ids {
        protected int trakt;
        protected String imdb;

        public int getTrakt() {
            return trakt;
        }

        public String getImdb() {
            return imdb;
        }
    }

}
