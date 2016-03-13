package me.taborda.mashtv.tracker.trakt;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Component
public class TraktClient {

    private static final Logger LOG = LoggerFactory.getLogger(TraktClient.class);

    private final TraktService trakt = buildClient();

    public List<TraktShow> findShowsMatching(String query) {
        try {
            Response<List<TraktSearchResult>> response = trakt.findShow(query).execute();
            if (response.isSuccess()) {
                return response.body().stream().map(TraktSearchResult::getShow).collect(Collectors.toList());
            } else {
                LOG.info("Error matching '{}': {}", query, response.message());
                return Collections.emptyList();
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Optional<TraktEpisode> getEpisode(int showId, int season, int number) {
        try {
            Response<TraktEpisode> response = trakt.getEpisode(showId, season, number).execute();
            if (response.isSuccess()) {
                return Optional.of(response.body());
            } else {
                LOG.info("Unable to find episode: {}", response.message());
                return Optional.empty();
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private static TraktService buildClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TraktService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(TraktService.class);
    }
}
