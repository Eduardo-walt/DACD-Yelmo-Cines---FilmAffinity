package com.cinema.yelmo.serializer;

import com.cinema.yelmo.model.Cinema;
import com.cinema.yelmo.model.Movie;
import com.cinema.yelmo.model.Showtime;
import java.util.List;

/**
 * Interfaz que define el contrato para persistir datos en base de datos.
 * La implementación concreta guardará en SQLite.
 */
public interface ShowtimeSerializer {
    
    /**
     * Persiste un cine en la base de datos.
     * 
     * @param cinema el cine a guardar
     * @return true si se guardó exitosamente, false en caso contrario
     * @throws Exception si ocurre un error al guardar
     */
    boolean saveCinema(Cinema cinema) throws Exception;
    
    /**
     * Persiste una lista de cines.
     * 
     * @param cinemas lista de cines a guardar
     * @return cantidad de cines guardados
     * @throws Exception si ocurre un error
     */
    int saveCinemas(List<Cinema> cinemas) throws Exception;
    
    /**
     * Persiste una película en la base de datos.
     * 
     * @param movie la película a guardar
     * @return true si se guardó exitosamente, false en caso contrario
     * @throws Exception si ocurre un error al guardar
     */
    boolean saveMovie(Movie movie) throws Exception;
    
    /**
     * Persiste una lista de películas.
     * 
     * @param movies lista de películas a guardar
     * @return cantidad de películas guardadas
     * @throws Exception si ocurre un error
     */
    int saveMovies(List<Movie> movies) throws Exception;
    
    /**
     * Persiste una función en la base de datos.
     * 
     * @param showtime la función a guardar
     * @return true si se guardó exitosamente, false en caso contrario
     * @throws Exception si ocurre un error al guardar
     */
    boolean saveShowtime(Showtime showtime) throws Exception;
    
    /**
     * Persiste una lista de funciones.
     * 
     * @param showtimes lista de funciones a guardar
     * @return cantidad de funciones guardadas
     * @throws Exception si ocurre un error
     */
    int saveShowtimes(List<Showtime> showtimes) throws Exception;
    
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
