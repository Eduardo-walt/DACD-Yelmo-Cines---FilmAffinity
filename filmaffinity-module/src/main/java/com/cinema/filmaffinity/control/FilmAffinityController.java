package com.cinema.filmaffinity.control;

import com.cinema.filmaffinity.feeder.ReviewFeeder;
import com.cinema.filmaffinity.transformer.ReviewTransformer;
import com.cinema.filmaffinity.serializer.ReviewSerializer;
import com.cinema.filmaffinity.model.Film;
import com.cinema.filmaffinity.model.Rating;
import com.cinema.filmaffinity.model.FilmReview;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador de FilmAffinity.
 * Orquesta el flujo: Feeder → Transformer → Serializer → Base de Datos.
 * 
 * Responsabilidades:
 * - Instanciar componentes (Feeder, Transformer, Serializer)
 * - Ejecutar el flujo de datos
 * - Manejar errores integrales
 * - Reportar resultados
 */
public class FilmAffinityController {
    
    private ReviewFeeder feeder;
    private ReviewTransformer transformer;
    private ReviewSerializer serializer;
    
    private int ratingsObtained = 0;
    private int ratingsTransformed = 0;
    private int ratingsSaved = 0;
    private int reviewsObtained = 0;
    private int reviewsTransformed = 0;
    private int reviewsSaved = 0;
    private long executionTimeMs = 0;

    /**
     * Constructor del controlador.
     * Inicializa todos los componentes necesarios.
     */
    public FilmAffinityController(ReviewFeeder feeder, ReviewTransformer transformer, ReviewSerializer serializer) {
        this.feeder = feeder;
        this.transformer = transformer;
        this.serializer = serializer;
    }

    /**
     * Ejecuta el flujo completo: Feeder → Transformer → Serializer.
     * 
     * Flujo:
     * 1. Obtener datos crudos del Feeder (Ratings y Reviews)
     * 2. Transformar/validar con Transformer
     * 3. Persistir con Serializer
     * 4. Reportar resultados
     */
    public void execute() {
        long startTime = System.currentTimeMillis();
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("FILMAFFINITY CONTROLLER - Iniciando orquestación");
        System.out.println("=".repeat(60));
        
        try {
            // PASO 1: Obtener datos del Feeder (Ratings)
            System.out.println("\n[FILMAFFINITY-CONTROLLER] PASO 1: Obteniendo valoraciones de FilmAffinity...");
            List<Rating> rawRatings = obtenerRatings();
            
            if (rawRatings == null || rawRatings.isEmpty()) {
                System.err.println("[FILMAFFINITY-CONTROLLER] ✗ Error: No se obtuvieron valoraciones");
                return;
            }
            
            this.ratingsObtained = rawRatings.size();
            System.out.println("[FILMAFFINITY-CONTROLLER] ✓ Obtenidas " + ratingsObtained + " valoraciones");
            
            // PASO 1B: Obtener datos del Feeder (Reviews)
            System.out.println("\n[FILMAFFINITY-CONTROLLER] PASO 1B: Obteniendo reseñas de FilmAffinity...");
            List<FilmReview> rawReviews = obtenerReviews();
            
            if (rawReviews == null || rawReviews.isEmpty()) {
                System.err.println("[FILMAFFINITY-CONTROLLER] ✗ Error: No se obtuvieron reseñas");
                return;
            }
            
            this.reviewsObtained = rawReviews.size();
            System.out.println("[FILMAFFINITY-CONTROLLER] ✓ Obtenidas " + reviewsObtained + " reseñas");
            
            // PASO 2: Transformar datos
            System.out.println("\n[FILMAFFINITY-CONTROLLER] PASO 2: Transformando y validando datos...");
            List<Rating> transformedRatings = transformarRatings(rawRatings);
            List<FilmReview> transformedReviews = transformarReviews(rawReviews);
            
            if ((transformedRatings == null || transformedRatings.isEmpty()) && 
                (transformedReviews == null || transformedReviews.isEmpty())) {
                System.err.println("[FILMAFFINITY-CONTROLLER] ✗ Error: No se transformaron datos");
                return;
            }
            
            if (transformedRatings != null) {
                this.ratingsTransformed = transformedRatings.size();
                System.out.println("[FILMAFFINITY-CONTROLLER] ✓ Transformadas " + ratingsTransformed + " valoraciones");
            }
            
            if (transformedReviews != null) {
                this.reviewsTransformed = transformedReviews.size();
                System.out.println("[FILMAFFINITY-CONTROLLER] ✓ Transformadas " + reviewsTransformed + " reseñas");
            }
            
            // PASO 3: Persistir datos
            System.out.println("\n[FILMAFFINITY-CONTROLLER] PASO 3: Persistiendo datos en BD...");
            
            if (transformedRatings != null && !transformedRatings.isEmpty()) {
                int savedRatings = persistirRatings(transformedRatings);
                this.ratingsSaved = savedRatings;
                System.out.println("[FILMAFFINITY-CONTROLLER] ✓ Guardadas " + ratingsSaved + " valoraciones en BD");
            }
            
            if (transformedReviews != null && !transformedReviews.isEmpty()) {
                int savedReviews = persistirReviews(transformedReviews);
                this.reviewsSaved = savedReviews;
                System.out.println("[FILMAFFINITY-CONTROLLER] ✓ Guardadas " + reviewsSaved + " reseñas en BD");
            }
            
            // PASO 4: Reportar resultados
            long endTime = System.currentTimeMillis();
            this.executionTimeMs = endTime - startTime;
            
            System.out.println("\n" + "=".repeat(60));
            reportarResultados();
            System.out.println("=".repeat(60) + "\n");
            
        } catch (Exception e) {
            System.err.println("[FILMAFFINITY-CONTROLLER] ✗ Error fatal en ejecución: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Obtiene valoraciones del Feeder.
     */
    private List<Rating> obtenerRatings() {
        try {
            if (!feeder.isConnected()) {
                System.out.println("[FILMAFFINITY-CONTROLLER] Conectando al servidor de FilmAffinity...");
            }
            
            List<Rating> ratings = feeder.fetchRatings();
            
            if (ratings == null) {
                System.err.println("[FILMAFFINITY-CONTROLLER] ✗ Feeder retornó null");
                return null;
            }
            
            return ratings;
            
        } catch (Exception e) {
            System.err.println("[FILMAFFINITY-CONTROLLER] ✗ Error obteniendo valoraciones: " + e.getMessage());
            return null;
        }
    }

    /**
     * Obtiene reseñas del Feeder.
     */
    private List<FilmReview> obtenerReviews() {
        try {
            List<FilmReview> reviews = feeder.fetchAllReviews();
            
            if (reviews == null) {
                System.err.println("[FILMAFFINITY-CONTROLLER] ✗ Feeder retornó null");
                return null;
            }
            
            return reviews;
            
        } catch (Exception e) {
            System.err.println("[FILMAFFINITY-CONTROLLER] ✗ Error obteniendo reseñas: " + e.getMessage());
            return null;
        }
    }

    /**
     * Transforma y valida valoraciones.
     */
    private List<Rating> transformarRatings(List<Rating> rawRatings) {
        try {
            List<Rating> transformed = new ArrayList<>();
            
            for (Rating rating : rawRatings) {
                if (isValidRating(rating)) {
                    transformed.add(rating);
                }
            }
            
            if (transformed.isEmpty()) {
                System.err.println("[FILMAFFINITY-CONTROLLER] ⚠ No hay valoraciones válidas");
                return null;
            }
            
            double lossPercentage = ((double)(rawRatings.size() - transformed.size()) / rawRatings.size()) * 100;
            
            if (lossPercentage > 50) {
                System.out.println("[FILMAFFINITY-CONTROLLER] ⚠ Advertencia: " + String.format("%.1f", lossPercentage) + "% de valoraciones fueron filtradas");
            }
            
            return transformed;
            
        } catch (Exception e) {
            System.err.println("[FILMAFFINITY-CONTROLLER] ✗ Error transformando valoraciones: " + e.getMessage());
            return null;
        }
    }

    /**
     * Transforma y valida reseñas.
     */
    private List<FilmReview> transformarReviews(List<FilmReview> rawReviews) {
        try {
            List<FilmReview> transformed = new ArrayList<>();
            
            for (FilmReview review : rawReviews) {
                if (isValidReview(review)) {
                    transformed.add(review);
                }
            }
            
            if (transformed.isEmpty()) {
                System.err.println("[FILMAFFINITY-CONTROLLER] ⚠ No hay reseñas válidas");
                return null;
            }
            
            double lossPercentage = ((double)(rawReviews.size() - transformed.size()) / rawReviews.size()) * 100;
            
            if (lossPercentage > 50) {
                System.out.println("[FILMAFFINITY-CONTROLLER] ⚠ Advertencia: " + String.format("%.1f", lossPercentage) + "% de reseñas fueron filtradas");
            }
            
            return transformed;
            
        } catch (Exception e) {
            System.err.println("[FILMAFFINITY-CONTROLLER] ✗ Error transformando reseñas: " + e.getMessage());
            return null;
        }
    }

    /**
     * Valida una valoración.
     */
    private boolean isValidRating(Rating rating) {
        return rating != null && rating.getFilmId() > 0 && rating.getAverageRating() != null;
    }

    /**
     * Valida una reseña.
     */
    private boolean isValidReview(FilmReview review) {
        return review != null && review.getFilmId() > 0 && review.getReviewContent() != null && !review.getReviewContent().isEmpty();
    }

    /**
     * Persiste valoraciones en BD.
     */
    private int persistirRatings(List<Rating> ratings) {
        try {
            if (!serializer.isDatabaseAvailable()) {
                System.err.println("[FILMAFFINITY-CONTROLLER] ✗ Base de datos no disponible");
                return 0;
            }
            
            int saved = serializer.saveRatings(ratings);
            
            if (saved < ratings.size()) {
                System.out.println("[FILMAFFINITY-CONTROLLER] ⚠ Solo se guardaron " + saved + " de " + ratings.size() + " valoraciones");
            }
            
            return saved;
            
        } catch (Exception e) {
            System.err.println("[FILMAFFINITY-CONTROLLER] ✗ Error persistiendo valoraciones: " + e.getMessage());
            return 0;
        }
    }

    /**
     * Persiste reseñas en BD.
     */
    private int persistirReviews(List<FilmReview> reviews) {
        try {
            if (!serializer.isDatabaseAvailable()) {
                System.err.println("[FILMAFFINITY-CONTROLLER] ✗ Base de datos no disponible");
                return 0;
            }
            
            int saved = serializer.saveReviews(reviews);
            
            if (saved < reviews.size()) {
                System.out.println("[FILMAFFINITY-CONTROLLER] ⚠ Solo se guardaron " + saved + " de " + reviews.size() + " reseñas");
            }
            
            return saved;
            
        } catch (Exception e) {
            System.err.println("[FILMAFFINITY-CONTROLLER] ✗ Error persistiendo reseñas: " + e.getMessage());
            return 0;
        }
    }

    /**
     * Reporta los resultados de la ejecución.
     */
    private void reportarResultados() {
        System.out.println("\n📊 REPORTE DE EJECUCIÓN - FILMAFFINITY");
        System.out.println("─".repeat(60));
        System.out.println(String.format("  Valoraciones obtenidas:    %d", ratingsObtained));
        System.out.println(String.format("  Valoraciones transformadas: %d", ratingsTransformed));
        System.out.println(String.format("  Valoraciones guardadas:     %d", ratingsSaved));
        System.out.println(String.format("  Reseñas obtenidas:         %d", reviewsObtained));
        System.out.println(String.format("  Reseñas transformadas:      %d", reviewsTransformed));
        System.out.println(String.format("  Reseñas guardadas:          %d", reviewsSaved));
        System.out.println(String.format("  Tiempo ejecución:          %d ms", executionTimeMs));
        
        int totalObtained = ratingsObtained + reviewsObtained;
        int totalSaved = ratingsSaved + reviewsSaved;
        
        double successRate = (totalObtained > 0) ? ((double)totalSaved / totalObtained) * 100 : 0;
        
        System.out.println(String.format("  Tasa de éxito:             %.1f%%", successRate));
        
        if (successRate >= 80) {
            System.out.println("  Estado: ✓ ÉXITO");
        } else if (successRate >= 50) {
            System.out.println("  Estado: ⚠ PARCIAL");
        } else {
            System.out.println("  Estado: ✗ FALLÓ");
        }
        
        System.out.println("─".repeat(60));
    }

    /**
     * Cierra recursos y conexiones.
     */
    public void shutdown() {
        System.out.println("[FILMAFFINITY-CONTROLLER] Cerrando recursos...");
        try {
            if (serializer instanceof com.cinema.filmaffinity.serializer.DatabaseReviewSerializer) {
                ((com.cinema.filmaffinity.serializer.DatabaseReviewSerializer) serializer).closeConnection();
            }
            System.out.println("[FILMAFFINITY-CONTROLLER] ✓ Recursos cerrados");
        } catch (Exception e) {
            System.err.println("[FILMAFFINITY-CONTROLLER] ✗ Error cerrando recursos: " + e.getMessage());
        }
    }

    // Getters para estadísticas
    public int getRatingsObtained() { return ratingsObtained; }
    public int getRatingsTransformed() { return ratingsTransformed; }
    public int getRatingsSaved() { return ratingsSaved; }
    public int getReviewsObtained() { return reviewsObtained; }
    public int getReviewsTransformed() { return reviewsTransformed; }
    public int getReviewsSaved() { return reviewsSaved; }
    public long getExecutionTimeMs() { return executionTimeMs; }
}
