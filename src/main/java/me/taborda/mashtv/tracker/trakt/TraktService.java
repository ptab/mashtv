package me.taborda.mashtv.tracker.trakt;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TraktService {

    String BASE_URL = "https://api-v2launch.trakt.tv";
    String CLIENT_ID = "a086b44ab76f122eba58570b44321b40a479faab2c0b22dcb315186e982ca4ec";

    @Headers({
            "Content-Type: application/json",
            "trakt-api-version: 2",
            "trakt-api-key: " + CLIENT_ID
    })
    @GET("/search?type=show")
    Call<List<TraktSearchResult>> findShow(@Query("query") String query);

    @Headers({
            "Content-Type: application/json",
            "trakt-api-version: 2",
            "trakt-api-key: " + CLIENT_ID
    })
    @GET("/shows/{show}/seasons/{season}/episodes/{episode}")
    Call<TraktEpisode> getEpisode(@Path("show") int show, @Path("season") int season, @Path("episode") int episode);
}

