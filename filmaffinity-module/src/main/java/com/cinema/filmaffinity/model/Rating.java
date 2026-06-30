package com.cinema.filmaffinity.model;

import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * Representa la valoración agregada de una película en FilmAffinity.
 * Almacena el rating promedio y cantidad de votos.
 */
public class Rating {
    private int id;
    private int filmId;              // Referencia a la película
    private BigDecimal averageRating; // Rating promedio (0.0 - 10.0)
    private int numberOfVotes;       // Cantidad de votos
    private int numberOfCritics;     // Votos de críticos (si está disponible)
    private LocalDateTime lastUpdated; // Cuándo se actualizó
    private LocalDateTime capturedAt;  // Cuándo se capturó el dato

    // Constructor
    public Rating(int id, int filmId, BigDecimal averageRating, int numberOfVotes) {
        this.id = id;
        this.filmId = filmId;
        this.averageRating = averageRating;
        this.numberOfVotes = numberOfVotes;
        this.numberOfCritics = 0;
        this.lastUpdated = LocalDateTime.now();
        this.capturedAt = LocalDateTime.now();
    }

    // Constructor con críticos
    public Rating(int id, int filmId, BigDecimal averageRating, int numberOfVotes, int numberOfCritics) {
        this.id = id;
        this.filmId = filmId;
        this.averageRating = averageRating;
        this.numberOfVotes = numberOfVotes;
        this.numberOfCritics = numberOfCritics;
        this.lastUpdated = LocalDateTime.now();
        this.capturedAt = LocalDateTime.now();
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getFilmId() {
        return filmId;
    }

    public BigDecimal getAverageRating() {
        return averageRating;
    }

    public int getNumberOfVotes() {
        return numberOfVotes;
    }

    public int getNumberOfCritics() {
        return numberOfCritics;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public LocalDateTime getCapturedAt() {
        return capturedAt;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setFilmId(int filmId) {
        this.filmId = filmId;
    }

    public void setAverageRating(BigDecimal averageRating) {
        this.averageRating = averageRating;
    }

    public void setNumberOfVotes(int numberOfVotes) {
        this.numberOfVotes = numberOfVotes;
    }

    public void setNumberOfCritics(int numberOfCritics) {
        this.numberOfCritics = numberOfCritics;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public void setCapturedAt(LocalDateTime capturedAt) {
        this.capturedAt = capturedAt;
    }

    // toString para debugging
    @Override
    public String toString() {
        return "Rating{" +
                "id=" + id +
                ", filmId=" + filmId +
                ", averageRating=" + averageRating +
                ", numberOfVotes=" + numberOfVotes +
                ", numberOfCritics=" + numberOfCritics +
                ", capturedAt=" + capturedAt +
                '}';
    }

    // equals y hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rating rating = (Rating) o;
        return id == rating.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
