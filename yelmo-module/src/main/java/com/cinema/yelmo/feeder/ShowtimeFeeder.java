package com.cinema.yelmo.feeder;

import com.cinema.yelmo.model.ShowtimeData;
import java.util.List;

/**
 * Interfaz que define el contrato para obtener datos de cines y funciones.
 * Cualquier fuente de datos (API, scraping, etc.) debe implementar esta interfaz.
 *
 * Devuelve ShowtimeData (datos crudos, sin tipar) porque la fuente externa
 * (scraping, API, etc.) entrega texto. La conversión a Showtime (tipado,
 * con ids resueltos) la hace ShowtimeTransformer.
 */
public interface ShowtimeFeeder {

    /**
     * Obtiene una lista de funciones en bruto desde la fuente externa.
     *
     * @return Lista de ShowtimeData obtenidos de la fuente
     * @throws Exception si ocurre un error al obtener los datos
     */
    List<ShowtimeData> fetchShowtimes() throws Exception;

    /**
     * Obtiene funciones en bruto para un cine específico.
     *
     * @param cinemaId el ID del cine
     * @return Lista de funciones del cine especificado
     * @throws Exception si ocurre un error
     */
    List<ShowtimeData> fetchShowtimesByCinema(int cinemaId) throws Exception;

    /**
     * Verifica si la conexión con la fuente es válida.
     *
     * @return true si la conexión es correcta, false en caso contrario
     */
    boolean isConnected();
}