package com.cinema.filmaffinity.serializer;

import com.cinema.filmaffinity.model.Film;
import com.cinema.filmaffinity.model.Rating;
import com.cinema.filmaffinity.model.FilmReview;
import java.util.List;

/**
 * Interfaz que define el contrato para persistir datos en base de datos.
 * La implementación concreta guardará en SQLite.
 */
public interface ReviewSerializer {
    
    /**
     * Persiste una película en la base de datos.
     * 
     * @param film la película a guardar
     * @return true si se guardó exitosamente, false en caso contrario
     * @throws Exception si ocurre un error al guardar
     */
    boolean saveFilm(Film film) throws Exception;
    
    /**
     * Persiste una lista de películas.
     * 
     * @param films lista de películas a guardar
     * @return cantidad de películas guardadas
     * @throws Exception si ocurre un error
     */
    int saveFilms(List<Film> films) throws Exception;
    
    /**
     * Persiste una valoración en la base de datos.
     * 
     * @param rating la valoración a guardar
     * @return true si se guardó exitosamente, false en caso contrario
     * @throws Exception si ocurre un error al guardar
     */
    boolean saveRating(Rating rating) throws Exception;
    
    /**
     * Persiste una lista de valoraciones.
     * 
     * @param ratings lista de valoraciones a guardar
     * @return cantidad de valoraciones guardadas
     * @throws Exception si ocurre un error
     */
    int saveRatings(List<Rating> ratings) throws Exception;
    
    /**
     * Persiste una reseña en la base de datos.
     * 
     * @param review la reseña a guardar
     * @return true si se guardó exitosamente, false en caso contrario
     * @throws Exception si ocurre un error al guardar
     */
    boolean saveReview(FilmReview review) throws Exception;
    
    /**
     * Persiste una lista de reseñas.
     * 
     * @param reviews lista de reseñas a guardar
     * @return cantidad de reseñas guardadas
     * @throws Exception si ocurre un error
     */
    int saveReviews(List<FilmReview> reviews) throws Exception;
    
    /**
     * Verifica si la base de datos está disponible.
     * 
     * @return true si la BD está disponible, false en caso contrario
     */
    boolean isDatabaseAvailable();
    
    /**
     * Inicializa las tablas necesarias en la base de datos.
     * 
     * @throws Exception si ocurre un error al crear las tablas
     */
    void initializeTables() throws Exception;
}
