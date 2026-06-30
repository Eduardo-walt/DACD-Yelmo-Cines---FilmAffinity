package com.cinema.yelmo.model;

public record ShowtimeData(
        String cinema,
        String title,
        String hour,
        String format,
        String duration,
        String rating,
        String capturedAt
) {}
