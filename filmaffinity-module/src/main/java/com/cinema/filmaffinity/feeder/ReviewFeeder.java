package com.cinema.filmaffinity.feeder;

import com.cinema.filmaffinity.model.FilmReview;
import com.cinema.filmaffinity.model.Rating;
import java.util.List;

/**
 * Interfaz que define el contrato para obtener datos de películas y valoraciones.
 * Cualquier fuente de datos (API, scraping, etc.) debe implementar esta interfaz.
 */
public interface ReviewFeeder {
    
    /**
     * Obtiene una lista de películas con sus valoraciones.
     * 
     * @return Lista de objetos Rating obtenidos de la fuente
     * @throws Exception si ocurre un error al obtener los datos
     */
    List<Rating> fetchRatings() throws Exception;
    
    /**
     * Obtiene las valoraciones de una película específica.
     * 
     * @param filmId el ID de la película
     * @return Rating de la película especificada
     * @throws Exception si ocurre un error
     */
    Rating fetchRatingByFilm(int filmId) throws Exception;
    
    /**
     * Obtiene una lista de reseñas de una película.
     * 
     * @param filmId el ID de la película
     * @return Lista de reseñas de esa película
     * @throws Exception si ocurre un error
     */
    List<FilmReview> fetchReviewsByFilm(int filmId) throws Exception;
    
    /**
     * Obtiene todas las reseñas disponibles.
     * 
     * @return Lista de todas las reseñas
     * @throws Exception si ocurre un error
     */
    List<FilmReview> fetchAllReviews() throws Exception;
    
    /**
     * Verifica si la conexión con la fuente es válida.
     * 
     * @return true si la conexión es correcta, false en caso contrario
     */
    boolean isConnected();
}
