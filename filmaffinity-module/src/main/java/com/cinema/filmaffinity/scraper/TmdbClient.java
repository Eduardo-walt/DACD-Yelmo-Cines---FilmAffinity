package com.cinema.filmaffinity.scraper;

import com.cinema.filmaffinity.model.FilmData;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TmdbClient {

    private static final String BASE_URL = "https://api.themoviedb.org/3";
    private static final String LANGUAGE = "es-ES";
    private static final String REGION = "ES";

    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson = new Gson();

    private final String bearerToken;

    public TmdbClient() {
        this.bearerToken = System.getenv("TMDB_TOKEN"); // ← variable de entorno
        if (bearerToken == null) {
            throw new RuntimeException("TMDB_TOKEN no está definido en variables de entorno");
        }
    }

    // ============================
    // 1) Llamada genérica a TMDB
    // ============================
    private JsonObject call(String url) throws Exception {

        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "Bearer " + bearerToken)
                .header("Accept", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {

            if (!response.isSuccessful()) {
                throw new RuntimeException("TMDB error HTTP: " + response.code());
            }

            String json = response.body().string();
            return gson.fromJson(json, JsonObject.class);
        }
    }

    // ============================
    // 2) Películas en cartelera
    // ============================
    public List<Integer> getNowPlayingMovieIds() throws Exception {

        String url = BASE_URL + "/movie/now_playing?language=" + LANGUAGE + "&region=" + REGION;

        JsonObject json = call(url);

        JsonArray results = json.getAsJsonArray("results");

        List<Integer> ids = new ArrayList<>();

        for (int i = 0; i < results.size(); i++) {
            JsonObject movie = results.get(i).getAsJsonObject();
            ids.add(movie.get("id").getAsInt());
        }

        return ids;
    }

    // ============================
    // 3) Detalle de película
    // ============================
    private JsonObject getMovieDetails(int movieId) throws Exception {
        String url = BASE_URL + "/movie/" + movieId + "?language=" + LANGUAGE;
        return call(url);
    }

    // ============================
    // 4) Créditos (director)
    // ============================
    private String getDirector(int movieId) throws Exception {
        String url = BASE_URL + "/movie/" + movieId + "/credits?language=" + LANGUAGE;

        JsonObject json = call(url);
        JsonArray crew = json.getAsJsonArray("crew");

        for (int i = 0; i < crew.size(); i++) {
            JsonObject person = crew.get(i).getAsJsonObject();
            if (person.get("job").getAsString().equalsIgnoreCase("Director")) {
                return person.get("name").getAsString();
            }
        }

        return "Desconocido";
    }

    // ============================
    // 5) Convertir TMDB → FilmData
    // ============================
    public FilmData getFilmData(int movieId) throws Exception {

        JsonObject details = getMovieDetails(movieId);

        String title = details.get("title").getAsString();
        String year = details.get("release_date").getAsString().substring(0, 4);

        JsonArray genresArray = details.getAsJsonArray("genres");
        String genre = genresArray.size() > 0 ? genresArray.get(0).getAsJsonObject().get("name").getAsString() : "Desconocido";

        String synopsis = details.get("overview").getAsString();

        double rating = details.get("vote_average").getAsDouble();
        int votes = details.get("vote_count").getAsInt();

        String director = getDirector(movieId);

        return new FilmData(
                title,
                year,
                director,
                genre,
                String.valueOf(rating),
                String.valueOf(votes),
                synopsis,
                "https://www.themoviedb.org/movie/" + movieId,
                LocalDateTime.now().toString()
        );
    }
}
