package com.cinema.yelmo.transformer;

import com.cinema.yelmo.model.ShowtimeData;
import com.cinema.yelmo.model.Showtime;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShowtimeTransformer {

    // Cachés simples para no repetir ids: nombre de cine/película -> id asignado
    private final Map<String, Integer> cinemaIds = new HashMap<>();
    private final Map<String, Integer> movieIds = new HashMap<>();
    private int nextCinemaId = 1;
    private int nextMovieId = 1;

    public List<Showtime> transform(List<ShowtimeData> raw) {
        List<Showtime> result = new ArrayList<>();

        for (ShowtimeData data : raw) {
            result.add(toShowtime(data));
        }

        return result;
    }

    private Showtime toShowtime(ShowtimeData data) {
        int cinemaId = resolveCinemaId(data.cinema());
        int movieId = resolveMovieId(data.title());

        LocalDateTime startTime = parseStartTime(data.hour());
        LocalDateTime capturedAt = parseCapturedAt(data.capturedAt());

        return new Showtime(
                generateId(data),
                cinemaId,
                movieId,
                startTime,
                "N/A",              // idioma: no disponible todavía en el scraping de Yelmo
                data.format(),
                BigDecimal.ZERO,    // precio: no disponible todavía en el scraping de Yelmo
                0                   // asientos disponibles: no disponible todavía
        );
    }

    private int resolveCinemaId(String cinemaName) {
        return cinemaIds.computeIfAbsent(cinemaName, name -> nextCinemaId++);
    }

    private int resolveMovieId(String movieTitle) {
        return movieIds.computeIfAbsent(movieTitle, title -> nextMovieId++);
    }

    private LocalDateTime parseStartTime(String hour) {
        // El scraper de momento solo trae la hora como texto suelto (ej. "18:30"),
        // no una fecha completa, así que la combinamos con la fecha de hoy.
        try {
            java.time.LocalTime time = java.time.LocalTime.parse(hour.trim());
            return LocalDateTime.of(java.time.LocalDate.now(), time);
        } catch (Exception e) {
            // Si el formato no es el esperado, no abortamos todo el pipeline:
            // devolvemos "ahora" como valor de respaldo y seguimos.
            return LocalDateTime.now();
        }
    }

    private LocalDateTime parseCapturedAt(String capturedAt) {
        try {
            return LocalDateTime.parse(capturedAt);
        } catch (Exception e) {
            return LocalDateTime.now();
        }
    }

    private int generateId(ShowtimeData data) {
        return Math.abs((data.cinema() + data.title() + data.hour()).hashCode());
    }
}