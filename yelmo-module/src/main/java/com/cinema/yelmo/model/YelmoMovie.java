package com.cinema.yelmo.model;

import java.util.List;

public class YelmoMovie {

    private final String title;
    private final String duration;
    private final String rating;
    private final List<String> showtimes;

    public YelmoMovie(String title, String duration, String rating, List<String> showtimes) {
        this.title = title;
        this.duration = duration;
        this.rating = rating;
        this.showtimes = showtimes;
    }

    public String getTitle() { return title; }
    public String getDuration() { return duration; }
    public String getRating() { return rating; }
    public List<String> getShowtimes() { return showtimes; }
}
