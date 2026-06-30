package com.cinema.yelmo.serializer;

import com.cinema.yelmo.model.Cinema;
import com.cinema.yelmo.model.Movie;
import com.cinema.yelmo.model.Showtime;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * Implementación de ShowtimeSerializer para SQLite.
 * Persiste datos de cines, películas y funciones en base de datos.
 */
public class DatabaseShowtimeSerializer implements ShowtimeSerializer {
    
    private static final String DB_URL = "jdbc:sqlite:yelmo.db";
    private Connection connection;
    private boolean isInitialized = false;

    /**
     * Constructor que inicializa la conexión con la BD.
     */
    public DatabaseShowtimeSerializer() {
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
    public boolean saveCinema(Cinema cinema) throws Exception {
        if (!isInitialized) {
            throw new Exception("Base de datos no inicializada correctamente");
        }
        
        String sql = "INSERT OR REPLACE INTO CINEMAS (id, name, location, captured_at) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, cinema.getId());
            pstmt.setString(2, cinema.getName());
            pstmt.setString(3, cinema.getLocation());
            pstmt.setString(4, cinema.getCapturedAt().toString());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error guardando cine '" + cinema.getName() + "': " + e.getMessage());
            return false;
        }
    }

    @Override
    public int saveCinemas(List<Cinema> cinemas) throws Exception {
        int count = 0;
        for (Cinema cinema : cinemas) {
            if (saveCinema(cinema)) {
                count++;
            }
        }
        System.out.println("[YELMO] Guardados " + count + "/" + cinemas.size() + " cines");
        return count;
    }

    @Override
    public boolean saveMovie(Movie movie) throws Exception {
        if (!isInitialized) {
            throw new Exception("Base de datos no inicializada correctamente");
        }
        
        String sql = "INSERT OR REPLACE INTO MOVIES (id, title, genre, duration, director, synopsis, captured_at) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, movie.getId());
            pstmt.setString(2, movie.getTitle());
            pstmt.setString(3, movie.getGenre());
            pstmt.setInt(4, movie.getDuration());
            pstmt.setString(5, movie.getDirector());
            pstmt.setString(6, movie.getSynopsis());
            pstmt.setString(7, movie.getCapturedAt().toString());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error guardando película '" + movie.getTitle() + "': " + e.getMessage());
            return false;
        }
    }

    @Override
    public int saveMovies(List<Movie> movies) throws Exception {
        int count = 0;
        for (Movie movie : movies) {
            if (saveMovie(movie)) {
                count++;
            }
        }
        System.out.println("[YELMO] Guardadas " + count + "/" + movies.size() + " películas");
        return count;
    }

    @Override
    public boolean saveShowtime(Showtime showtime) throws Exception {
        if (!isInitialized) {
            throw new Exception("Base de datos no inicializada correctamente");
        }
        
        String sql = "INSERT INTO SHOWTIMES (cinema_id, movie_id, start_time, language, format, price, available_seats, captured_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, showtime.getCinemaId());
            pstmt.setInt(2, showtime.getMovieId());
            pstmt.setString(3, showtime.getStartTime().toString());
            pstmt.setString(4, showtime.getLanguage());
            pstmt.setString(5, showtime.getFormat());
            pstmt.setBigDecimal(6, showtime.getPrice());
            pstmt.setInt(7, showtime.getAvailableSeats());
            pstmt.setString(8, showtime.getCapturedAt().toString());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error guardando función: " + e.getMessage());
            return false;
        }
    }

    @Override
    public int saveShowtimes(List<Showtime> showtimes) throws Exception {
        int count = 0;
        for (Showtime showtime : showtimes) {
            if (saveShowtime(showtime)) {
                count++;
            }
        }
        System.out.println("[YELMO] Guardadas " + count + "/" + showtimes.size() + " funciones");
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
        String createCinemasTable = "CREATE TABLE IF NOT EXISTS CINEMAS (" +
                "id INTEGER PRIMARY KEY," +
                "name TEXT NOT NULL," +
                "location TEXT NOT NULL," +
                "captured_at DATETIME DEFAULT CURRENT_TIMESTAMP)";
        
        String createMoviesTable = "CREATE TABLE IF NOT EXISTS MOVIES (" +
                "id INTEGER PRIMARY KEY," +
                "title TEXT NOT NULL," +
                "genre TEXT," +
                "duration INTEGER," +
                "director TEXT," +
                "synopsis TEXT," +
                "captured_at DATETIME DEFAULT CURRENT_TIMESTAMP)";
        
        String createShowtimesTable = "CREATE TABLE IF NOT EXISTS SHOWTIMES (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "cinema_id INTEGER NOT NULL," +
                "movie_id INTEGER NOT NULL," +
                "start_time DATETIME NOT NULL," +
                "language TEXT," +
                "format TEXT," +
                "price DECIMAL(5,2)," +
                "available_seats INTEGER," +
                "captured_at DATETIME DEFAULT CURRENT_TIMESTAMP," +
                "FOREIGN KEY (cinema_id) REFERENCES CINEMAS(id)," +
                "FOREIGN KEY (movie_id) REFERENCES MOVIES(id))";
        
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createCinemasTable);
            stmt.execute(createMoviesTable);
            stmt.execute(createShowtimesTable);
            System.out.println("✓ [YELMO] Tablas de base de datos inicializadas correctamente");
        } catch (SQLException e) {
            System.err.println("✗ [YELMO] Error creando tablas: " + e.getMessage());
        }
    }

    /**
     * Cierra la conexión con la base de datos.
     */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("✓ [YELMO] Conexión cerrada correctamente");
            }
        } catch (SQLException e) {
            System.err.println("✗ [YELMO] Error cerrando conexión: " + e.getMessage());
        }
    }
}
