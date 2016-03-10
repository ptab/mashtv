package me.taborda.mashtv.enricher.trakt;

public class TestTraktEpisode extends TraktEpisode {

    public TestTraktEpisode withTitle(String title) {
        this.title = title ;
        return this;
    }


}
