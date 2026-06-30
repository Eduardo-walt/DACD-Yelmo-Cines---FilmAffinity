package com.cinema.filmaffinity.model;

public record FilmData(
        String title,
        String year,
        String director,
        String genre,
        String rating,
        String votes,
        String synopsis,
        String url,
        String capturedAt
) {}
