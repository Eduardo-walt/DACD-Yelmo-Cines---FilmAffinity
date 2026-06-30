package com.cinema.filmaffinity.serializer;

import com.cinema.filmaffinity.model.Film;
import com.cinema.filmaffinity.model.Rating;
import com.cinema.filmaffinity.model.FilmReview;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * Implementación de ReviewSerializer para SQLite.
 * Persiste datos de películas, valoraciones y reseñas en base de datos.
 */
public class DatabaseReviewSerializer implements ReviewSerializer {
    
    private static final String DB_URL = "jdbc:sqlite:filmaffinity.db";
    private Connection connection;
    private boolean isInitialized = false;

    /**
     * Constructor que inicializa la conexión con la BD.
     */
    public DatabaseReviewSerializer() {
        try {
            Class.forName("org.sqlite.JDBC");
            this.connection = DriverManager.getConnection(DB_URL);
            initializeTables();
            this.isInitialized = true;
        } catch (ClassNotFoundException e) {
            System.err.println("Error: Driver SQLite no encontrado - " + e.getMessage());
            this.isInitialized = false;
        } catch (SQLException e) {
            System.err.println("Error al conectar con la base de datos - " + e.getMessage());
            this.isInitialized = false;
        }
    }

    @Override
    public boolean saveFilm(Film film) throws Exception {
        if (!isInitialized) {
            throw new Exception("Base de datos no inicializada correctamente");
        }
        
        String sql = "INSERT OR REPLACE INTO FILMS (id, title, year, genre, director, country, synopsis, captured_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, film.getId());
            pstmt.setString(2, film.getTitle());
            pstmt.setInt(3, film.getYear());
            pstmt.setString(4, film.getGenre());
            pstmt.setString(5, film.getDirector());
            pstmt.setString(6, film.getCountry());
            pstmt.setString(7, film.getSynopsis());
            pstmt.setString(8, film.getCapturedAt().toString());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error guardando película '" + film.getTitle() + "': " + e.getMessage());
            return false;
        }
    }

    @Override
    public int saveFilms(List<Film> films) throws Exception {
        int count = 0;
        for (Film film : films) {
            if (saveFilm(film)) {
                count++;
            }
        }
        System.out.println("[FILMAFFINITY] Guardadas " + count + "/" + films.size() + " películas");
        return count;
    }

    @Override
    public boolean saveRating(Rating rating) throws Exception {
        if (!isInitialized) {
            throw new Exception("Base de datos no inicializada correctamente");
        }
        
        String sql = "INSERT INTO RATINGS (film_id, average_rating, number_of_votes, number_of_critics, captured_at) VALUES (?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, rating.getFilmId());
            pstmt.setBigDecimal(2, rating.getAverageRating());
            pstmt.setInt(3, rating.getNumberOfVotes());
            pstmt.setInt(4, rating.getNumberOfCritics());
            pstmt.setString(5, rating.getCapturedAt().toString());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error guardando valoración para película " + rating.getFilmId() + ": " + e.getMessage());
            return false;
        }
    }

    @Override
    public int saveRatings(List<Rating> ratings) throws Exception {
        int count = 0;
        for (Rating rating : ratings) {
            if (saveRating(rating)) {
                count++;
            }
        }
        System.out.println("[FILMAFFINITY] Guardadas " + count + "/" + ratings.size() + " valoraciones");
        return count;
    }

    @Override
    public boolean saveReview(FilmReview review) throws Exception {
        if (!isInitialized) {
            throw new Exception("Base de datos no inicializada correctamente");
        }
        
        String sql = "INSERT INTO REVIEWS (film_id, reviewer_name, review_content, user_rating, review_date, helpful_count, captured_at) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, review.getFilmId());
            pstmt.setString(2, review.getReviewerName());
            pstmt.setString(3, review.getReviewContent());
            pstmt.setInt(4, review.getUserRating());
            pstmt.setString(5, review.getReviewDate().toString());
            pstmt.setInt(6, review.getHelpfulCount());
            pstmt.setString(7, review.getCapturedAt().toString());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error guardando reseña de '" + review.getReviewerName() + "': " + e.getMessage());
            return false;
        }
    }

    @Override
    public int saveReviews(List<FilmReview> reviews) throws Exception {
        int count = 0;
        for (FilmReview review : reviews) {
            if (saveReview(review)) {
                count++;
            }
        }
        System.out.println("[FILMAFFINITY] Guardadas " + count + "/" + reviews.size() + " reseñas");
        return count;
    }

    @Override
    public boolean isDatabaseAvailable() {
        if (!isInitialized) {
            return false;
        }
        try {
            String sql = "SELECT 1";
            try (Statement stmt = connection.createStatement()) {
                stmt.executeQuery(sql);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Base de datos no disponible: " + e.getMessage());
            return false;
        }
    }

    @Override
    public void initializeTables() {
        String createFilmsTable = "CREATE TABLE IF NOT EXISTS FILMS (" +
                "id INTEGER PRIMARY KEY," +
                "title TEXT NOT NULL," +
                "year INTEGER," +
                "genre TEXT," +
                "director TEXT," +
                "country TEXT," +
                "synopsis TEXT," +
                "captured_at DATETIME DEFAULT CURRENT_TIMESTAMP)";
        
        String createRatingsTable = "CREATE TABLE IF NOT EXISTS RATINGS (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "film_id INTEGER NOT NULL," +
                "average_rating DECIMAL(3,2)," +
                "number_of_votes INTEGER," +
                "number_of_critics INTEGER," +
                "captured_at DATETIME DEFAULT CURRENT_TIMESTAMP," +
                "FOREIGN KEY (film_id) REFERENCES FILMS(id))";
        
        String createReviewsTable = "CREATE TABLE IF NOT EXISTS REVIEWS (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "film_id INTEGER NOT NULL," +
                "reviewer_name TEXT," +
                "review_content TEXT," +
                "user_rating INTEGER," +
                "review_date DATETIME," +
                "helpful_count INTEGER," +
                "captured_at DATETIME DEFAULT CURRENT_TIMESTAMP," +
                "FOREIGN KEY (film_id) REFERENCES FILMS(id))";
        
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createFilmsTable);
            stmt.execute(createRatingsTable);
            stmt.execute(createReviewsTable);
            System.out.println("✓ [FILMAFFINITY] Tablas de base de datos inicializadas correctamente");
        } catch (SQLException e) {
            System.err.println("✗ [FILMAFFINITY] Error creando tablas: " + e.getMessage());
        }
    }

    /**
     * Cierra la conexión con la base de datos.
     */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("✓ [FILMAFFINITY] Conexión cerrada correctamente");
            }
        } catch (SQLException e) {
            System.err.println("✗ [FILMAFFINITY] Error cerrando conexión: " + e.getMessage());
        }
    }
}
