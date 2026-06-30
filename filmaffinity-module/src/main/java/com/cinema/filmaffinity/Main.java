package com.cinema.filmaffinity;

import com.cinema.filmaffinity.control.FilmAffinityController;
import com.cinema.filmaffinity.feeder.ReviewFeeder;
import com.cinema.filmaffinity.serializer.DatabaseReviewSerializer;
import com.cinema.filmaffinity.serializer.ReviewSerializer;
import com.cinema.filmaffinity.transformer.ReviewTransformer;
import com.cinema.filmaffinity.feeder.FilmAffinityReviewFeeder;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("CINEMA DATA INTEGRATION - FILMAFFINITY MODULE");
        System.out.println("Sprint 1: Recopilación independiente de datos de valoraciones");
        System.out.println("=".repeat(70));

        ReviewFeeder feeder = new FilmAffinityReviewFeeder();
        ReviewTransformer transformer = new ReviewTransformer();
        ReviewSerializer serializer = new DatabaseReviewSerializer();
        FilmAffinityController controller = new FilmAffinityController(feeder, transformer, serializer);

        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(controller::execute, 0, 1, TimeUnit.HOURS);
    }
}
