package com.cinema.filmaffinity.feeder;

import com.cinema.filmaffinity.model.FilmData;
import com.cinema.filmaffinity.model.FilmReview;
import com.cinema.filmaffinity.model.Rating;
import com.cinema.filmaffinity.scraper.TmdbClient;
import com.cinema.filmaffinity.transformer.ReviewTransformer;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilmAffinityReviewFeeder implements ReviewFeeder {

    private final TmdbClient tmdb = new TmdbClient();
    private final ReviewTransformer transformer = new ReviewTransformer();
    private final FilmAffinityPublisher publisher;

    private boolean connected = true;

    public FilmAffinityReviewFeeder() {
        try {
            this.publisher = new FilmAffinityPublisher();
        } catch (Exception e) {
            throw new RuntimeException("No se pudo inicializar FilmAffinityPublisher", e);
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                this.publisher.close();
            } catch (Exception ignored) {}
        }));
    }

    @Override
    public List<Rating> fetchRatings() throws Exception {
        List<Integer> ids = tmdb.getNowPlayingMovieIds();
        List<Rating> ratings = new ArrayList<>();
        for (int id : ids) {
            FilmData data = tmdb.getFilmData(id);
            ratings.add(transformer.transformToRating(data));
        }
        return ratings;
    }

    @Override
    public Rating fetchRatingByFilm(int filmId) throws Exception {
        FilmData data = tmdb.getFilmData(filmId);
        return transformer.transformToRating(data);
    }

    @Override
    public List<FilmReview> fetchReviewsByFilm(int filmId) throws Exception {
        FilmData data = tmdb.getFilmData(filmId);
        return transformer.transformToReviews(data);
    }

    @Override
    public List<FilmReview> fetchAllReviews() throws Exception {
        List<Integer> ids = tmdb.getNowPlayingMovieIds();
        List<FilmReview> reviews = new ArrayList<>();

        for (int id : ids) {
            FilmData data = tmdb.getFilmData(id);
            List<FilmReview> filmReviews = transformer.transformToReviews(data);
            reviews.addAll(filmReviews);

            for (FilmReview review : filmReviews) {
                try {
                    Map<String, Object> event = new HashMap<>();
                    event.put("ts", Instant.now().getEpochSecond());
                    event.put("ss", "filmaffinity-feeder");
                    event.put("topic", "Review");
                    event.put("payload", review);
                    publisher.publishObject(event);
                } catch (Exception e) {
                    System.err.println("[FILMAFFINITY] Error publicando evento: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }

        return reviews;
    }

    @Override
    public boolean isConnected() {
        return connected;
    }
}