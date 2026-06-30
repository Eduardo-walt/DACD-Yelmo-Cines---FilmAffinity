package com.cinema.filmaffinity.transformer;

import com.cinema.filmaffinity.model.FilmData;
import com.cinema.filmaffinity.model.FilmReview;
import com.cinema.filmaffinity.model.Rating;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReviewTransformer {

    // Convierte FilmData → Rating
    public Rating transformToRating(FilmData data) {

        BigDecimal ratingValue;

        try {
            ratingValue = new BigDecimal(data.rating().replace(",", "."));
        } catch (Exception e) {
            ratingValue = BigDecimal.ZERO;
        }

        int votes = 0;
        try {
            votes = Integer.parseInt(data.votes().replaceAll("[^0-9]", ""));
        } catch (Exception ignored) {}

        return new Rating(
                generateFilmId(data.title()),
                generateFilmId(data.title()),
                ratingValue,
                votes,
                0 // número de reseñas (lo rellenaremos en Sprint 2)
        );
    }

    // Convierte FilmData → lista de FilmReview
    public List<FilmReview> transformToReviews(FilmData data) {
        List<FilmReview> reviews = new ArrayList<>();

        // FilmAffinity no da reseñas reales en HTML → generamos una reseña simple
        reviews.add(new FilmReview(
                generateReviewId(data.title()),
                generateFilmId(data.title()),
                "FilmAffinity",
                "Puntuación media: " + data.rating() + " (" + data.votes() + " votos)",
                safeParseRating(data.rating()),
                LocalDateTime.now(),
                0
        ));

        return reviews;
    }

    // Helpers para generar IDs consistentes
    private int generateFilmId(String title) {
        return Math.abs(title.hashCode());
    }

    private int generateReviewId(String title) {
        return Math.abs((title + "_review").hashCode());
    }

    private int safeParseRating(String rating) {
        try {
            return (int) Math.round(Double.parseDouble(rating.replace(",", ".")));
        } catch (Exception e) {
            return 0;
        }
    }
}
