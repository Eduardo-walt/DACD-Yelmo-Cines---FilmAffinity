package com.cinema.filmaffinity.model;

import java.time.LocalDateTime;

/**
 * Representa una reseña de una película en FilmAffinity.
 * Almacena el contenido de la reseña y metadatos asociados.
 */
public class FilmReview {
    private int id;
    private int filmId;              // Referencia a la película
    private String reviewerName;     // Nombre de quién escribió la reseña
    private String reviewContent;    // Contenido de la reseña
    private int userRating;          // Rating personal del reseñador (1-10)
    private LocalDateTime reviewDate; // Fecha en que se escribió la reseña
    private int helpfulCount;        // Cuántas personas marcaron como útil
    private LocalDateTime capturedAt; // Cuándo se capturó el dato

    // Constructor
    public FilmReview(int id, int filmId, String reviewerName, String reviewContent, 
                     int userRating, LocalDateTime reviewDate) {
        this.id = id;
        this.filmId = filmId;
        this.reviewerName = reviewerName;
        this.reviewContent = reviewContent;
        this.userRating = userRating;
        this.reviewDate = reviewDate;
        this.helpfulCount = 0;
        this.capturedAt = LocalDateTime.now();
    }

    // Constructor con helpfulCount
    public FilmReview(int id, int filmId, String reviewerName, String reviewContent, 
                     int userRating, LocalDateTime reviewDate, int helpfulCount) {
        this.id = id;
        this.filmId = filmId;
        this.reviewerName = reviewerName;
        this.reviewContent = reviewContent;
        this.userRating = userRating;
        this.reviewDate = reviewDate;
        this.helpfulCount = helpfulCount;
        this.capturedAt = LocalDateTime.now();
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getFilmId() {
        return filmId;
    }

    public String getReviewerName() {
        return reviewerName;
    }

    public String getReviewContent() {
        return reviewContent;
    }

    public int getUserRating() {
        return userRating;
    }

    public LocalDateTime getReviewDate() {
        return reviewDate;
    }

    public int getHelpfulCount() {
        return helpfulCount;
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

    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }

    public void setReviewContent(String reviewContent) {
        this.reviewContent = reviewContent;
    }

    public void setUserRating(int userRating) {
        this.userRating = userRating;
    }

    public void setReviewDate(LocalDateTime reviewDate) {
        this.reviewDate = reviewDate;
    }

    public void setHelpfulCount(int helpfulCount) {
        this.helpfulCount = helpfulCount;
    }

    public void setCapturedAt(LocalDateTime capturedAt) {
        this.capturedAt = capturedAt;
    }

    // toString para debugging
    @Override
    public String toString() {
        return "FilmReview{" +
                "id=" + id +
                ", filmId=" + filmId +
                ", reviewerName='" + reviewerName + '\'' +
                ", userRating=" + userRating +
                ", reviewDate=" + reviewDate +
                ", capturedAt=" + capturedAt +
                '}';
    }

    // equals y hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FilmReview review = (FilmReview) o;
        return id == review.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
