package com.cinema.filmaffinity.model;

import java.time.LocalDateTime;

/**
 * Representa una película con información de FilmAffinity.
 * Almacena datos sobre películas y sus valoraciones.
 */
public class Film {
    private int id;
    private String title;
    private int year;
    private String genre;
    private String director;
    private String country;
    private String synopsis;
    private LocalDateTime capturedAt;

    // Constructor
    public Film(int id, String title, int year, String genre, String director, 
                String country, String synopsis) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.genre = genre;
        this.director = director;
        this.country = country;
        this.synopsis = synopsis;
        this.capturedAt = LocalDateTime.now();
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getYear() {
        return year;
    }

    public String getGenre() {
        return genre;
    }

    public String getDirector() {
        return director;
    }

    public String getCountry() {
        return country;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public LocalDateTime getCapturedAt() {
        return capturedAt;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public void setCapturedAt(LocalDateTime capturedAt) {
        this.capturedAt = capturedAt;
    }

    // toString para debugging
    @Override
    public String toString() {
        return "Film{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", year=" + year +
                ", genre='" + genre + '\'' +
                ", director='" + director + '\'' +
                ", capturedAt=" + capturedAt +
                '}';
    }

    // equals y hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Film film = (Film) o;
        return id == film.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
