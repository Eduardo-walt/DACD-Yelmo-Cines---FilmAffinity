package com.cinema.yelmo.controller;

import com.cinema.yelmo.feeder.ShowtimeFeeder;
import com.cinema.yelmo.model.Showtime;
import com.cinema.yelmo.model.ShowtimeData;
import com.cinema.yelmo.serializer.DatabaseShowtimeSerializer;
import com.cinema.yelmo.serializer.ShowtimeSerializer;
import com.cinema.yelmo.transformer.ShowtimeTransformer;

import java.util.List;

public class YelmoController {

    private final ShowtimeFeeder feeder;
    private final ShowtimeTransformer transformer;
    private final ShowtimeSerializer serializer;

    public YelmoController(ShowtimeFeeder feeder, ShowtimeTransformer transformer, ShowtimeSerializer serializer) {
        this.feeder = feeder;
        this.transformer = transformer;
        this.serializer = serializer;
    }

    public void execute() {

        try {
            System.out.println("============================================================");
            System.out.println("YELMO CONTROLLER - Iniciando orquestación");
            System.out.println("============================================================");

            List<ShowtimeData> rawData = feeder.fetchShowtimes();
            System.out.println("[YELMO] ✓ Obtenidos " + rawData.size() + " horarios en bruto");

            List<Showtime> showtimes = transformer.transform(rawData);
            System.out.println("[YELMO] ✓ Transformados " + showtimes.size() + " horarios");

            for (Showtime s : showtimes) {
                serializer.saveShowtime(s);
            }

            System.out.println("[YELMO] ✓ Datos guardados correctamente");

            System.out.println("============================================================");
            System.out.println("📊 REPORTE DE EJECUCIÓN - YELMO");
            System.out.println("────────────────────────────────────────────────────────────");
            System.out.println("  Horarios obtenidos:     " + showtimes.size());
            System.out.println("  Estado: ✓ ÉXITO");
            System.out.println("────────────────────────────────────────────────────────────");
            System.out.println("============================================================");

        } catch (Exception e) {
            System.out.println("[YELMO] ✗ Error: " + e.getMessage());
        }
    }
}