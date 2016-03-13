package me.taborda.mashtv.tracker.trakt;

public class TestTraktShow extends TraktShow {

    public TestTraktShow withTitle(String title) {
        this.title = title;
        return this;
    }

    public TestTraktShow withYear(int year) {
        this.year = year ;
        return this ;
    }

    public TestTraktShow withTraktId(int traktId) {
        this.ids = new TestIds(ids).withTrakt(traktId) ;
        return this;
    }

    class TestIds extends TraktShow.Ids {

        public TestIds(TraktShow.Ids ids) {
            this.trakt = ids.trakt;
            this.imdb = ids.imdb;
        }

        public TestIds withTrakt(int trakt) {
            this.trakt = trakt;
            return this;
        }
    }
}
