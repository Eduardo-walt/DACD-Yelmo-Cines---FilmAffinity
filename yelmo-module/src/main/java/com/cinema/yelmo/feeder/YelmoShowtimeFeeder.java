package com.cinema.yelmo.feeder;

import com.cinema.yelmo.model.ShowtimeData;
import com.cinema.yelmo.scraper.YelmoScraper;
import com.google.gson.Gson;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;
import java.util.List;

public class YelmoShowtimeFeeder implements ShowtimeFeeder {

    private final YelmoScraper scraper = new YelmoScraper();
    private final YelmoPublisher publisher;
    private final Gson gson = new Gson();

    // Slugs ("Key") de los cines de Gran Canaria que nos interesan
    private static final List<String> CINEMA_KEYS = Arrays.asList(
            "premium-alisios",
            "las-arenas",
            "vecindario"
    );

    public YelmoShowtimeFeeder() {
        try {
            this.publisher = new YelmoPublisher();
        } catch (Exception e) {
            throw new RuntimeException("No se pudo inicializar YelmoPublisher", e);
        }

        // Cerrar publisher al apagar la JVM
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                this.publisher.close();
            } catch (Exception ignored) {}
        }));
    }

    @Override
    public List<ShowtimeData> fetchShowtimes() throws Exception {

        // Obtener cartelera filtrada por cines
        List<ShowtimeData> data = scraper.getNowPlayingForCinemas(CINEMA_KEYS);
        System.out.println("[YELMO] Obtenidos " + data.size() + " horarios en bruto");

        // Publicar cada showtime como evento en ActiveMQ (CinemaShowtime)
        for (ShowtimeData sd : data) {
            try {
                Map<String, Object> event = new HashMap<>();
                event.put("ts", Instant.now().getEpochSecond()); // long
                event.put("ss", "yelmo-feeder");
                event.put("topic", "Movie"); // campo que faltaba
                event.put("payload", sd);

                publisher.publishObject(event);

            } catch (Exception e) {
                System.err.println("[YELMO] Error publicando evento: " + e.getMessage());
                e.printStackTrace();
            }
        }

        return data;
    }

    @Override
    public List<ShowtimeData> fetchShowtimesByCinema(int cinemaId) {
        return java.util.Collections.emptyList(); // Sprint 1 no lo usa
    }

    @Override
    public boolean isConnected() {
        return true;
    }
}
