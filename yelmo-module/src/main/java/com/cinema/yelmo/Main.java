package com.cinema.yelmo;

import com.cinema.yelmo.controller.YelmoController;
import com.cinema.yelmo.feeder.ShowtimeFeeder;
import com.cinema.yelmo.feeder.YelmoShowtimeFeeder;
import com.cinema.yelmo.serializer.DatabaseShowtimeSerializer;
import com.cinema.yelmo.serializer.ShowtimeSerializer;
import com.cinema.yelmo.transformer.ShowtimeTransformer;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("CINEMA DATA INTEGRATION - YELMO MODULE");
        System.out.println("Sprint 1: Recopilación independiente de datos de cartelera");
        System.out.println("=".repeat(70));

        ShowtimeFeeder feeder = new YelmoShowtimeFeeder();
        ShowtimeTransformer transformer = new ShowtimeTransformer();
        ShowtimeSerializer serializer = new DatabaseShowtimeSerializer();
        YelmoController controller = new YelmoController(feeder, transformer, serializer);

        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(controller::execute, 0, 30, TimeUnit.MINUTES);
    }
}