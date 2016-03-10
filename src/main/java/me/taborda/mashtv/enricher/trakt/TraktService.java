package me.taborda.mashtv.enricher.trakt;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TraktService {

    String BASE_URL = "https://api-v2launch.trakt.tv";
    String CLIENT_ID = "<trakt-client-id>";

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

