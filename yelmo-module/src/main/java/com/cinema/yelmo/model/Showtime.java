package com.cinema.yelmo.model;

import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * Representa una función (showtime) en un cine.
 * Conecta un cine con una película en un horario específico y precio.
 */
public class Showtime {
    private int id;
    private int cinemaId;        // Referencia al cine
    private int movieId;         // Referencia a la película
    private LocalDateTime startTime; // Hora de inicio
    private String language;     // Idioma (original, doblado)
    private String format;       // Formato (2D, 3D, IMAX, etc.)
    private BigDecimal price;    // Precio de la entrada
    private int availableSeats;  // Asientos disponibles
    private LocalDateTime capturedAt;

    // Constructor
    public Showtime(int id, int cinemaId, int movieId, LocalDateTime startTime, 
                   String language, String format, BigDecimal price, int availableSeats) {
        this.id = id;
        this.cinemaId = cinemaId;
        this.movieId = movieId;
        this.startTime = startTime;
        this.language = language;
        this.format = format;
        this.price = price;
        this.availableSeats = availableSeats;
        this.capturedAt = LocalDateTime.now();
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getCinemaId() {
        return cinemaId;
    }

    public int getMovieId() {
        return movieId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public String getLanguage() {
        return language;
    }

    public String getFormat() {
        return format;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public LocalDateTime getCapturedAt() {
        return capturedAt;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setCinemaId(int cinemaId) {
        this.cinemaId = cinemaId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    public void setCapturedAt(LocalDateTime capturedAt) {
        this.capturedAt = capturedAt;
    }

    // toString para debugging
    @Override
    public String toString() {
        return "Showtime{" +
                "id=" + id +
                ", cinemaId=" + cinemaId +
                ", movieId=" + movieId +
                ", startTime=" + startTime +
                ", language='" + language + '\'' +
                ", format='" + format + '\'' +
                ", price=" + price +
                ", availableSeats=" + availableSeats +
                ", capturedAt=" + capturedAt +
                '}';
    }

    // equals y hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Showtime showtime = (Showtime) o;
        return id == showtime.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
