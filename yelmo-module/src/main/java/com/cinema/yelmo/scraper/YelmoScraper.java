package com.cinema.yelmo.scraper;

import com.cinema.yelmo.model.ShowtimeData;
import com.cinema.yelmo.scraper.dto.CinemaDTO;
import com.cinema.yelmo.scraper.dto.DateDTO;
import com.cinema.yelmo.scraper.dto.FormatDTO;
import com.cinema.yelmo.scraper.dto.MovieDTO;
import com.cinema.yelmo.scraper.dto.NowPlayingResponse;
import com.cinema.yelmo.scraper.dto.ShowtimeDTO;
import com.google.gson.Gson;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Cliente del endpoint real de Yelmo Cines (servicio ASP.NET ScriptService),
 * descubierto inspeccionando la pestaña Network del navegador:
 *
 *   POST https://www.yelmocines.es/now-playing.aspx/GetNowPlaying
 *
 * Este endpoint devuelve, en una sola llamada, la cartelera completa de
 * TODOS los cines de una ciudad (en nuestro caso, Las Palmas), por lo que
 * ya no es necesario hacer una petición por cada cine ni parsear HTML.
 */
public class YelmoScraper {

    private static final String ENDPOINT = "https://www.yelmocines.es/now-playing.aspx/GetNowPlaying";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson = new Gson();

    /**
     * Llama al endpoint y devuelve la cartelera completa, ya aplanada en una
     * lista de ShowtimeData (un elemento por cada cine+película+formato+hora).
     *
     * No se filtra por cine aquí: filtrar por cines concretos (p.ej. solo
     * Premium Alisios y Las Arenas) se hace después, sobre esta lista, para
     * no acoplar el scraper a la lista de cines que nos interesan.
     */
    public List<ShowtimeData> getNowPlaying() throws Exception {

        // El endpoint ASP.NET ScriptService requiere el parámetro cityKey
        // en el body (confirmado inspeccionando la pestaña Network/Payload
        // del navegador). Sin él, responde con un error 500.
        RequestBody body = RequestBody.create("{\"cityKey\":\"las-palmas\"}", JSON);

        Request request = new Request.Builder()
                .url(ENDPOINT)
                .post(body)
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 "
                        + "(KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36")
                .header("Content-Type", "application/json; charset=utf-8")
                .header("Accept", "application/json, text/javascript, */*; q=0.01")
                .header("Referer", "https://www.yelmocines.es/cartelera/las-palmas/las-arenas")
                .header("X-Requested-With", "XMLHttpRequest")
                .build();

        String json;
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RuntimeException("HTTP error llamando a GetNowPlaying: " + response.code());
            }
            json = response.body().string();
        }

        NowPlayingResponse parsed = gson.fromJson(json, NowPlayingResponse.class);

        if (parsed == null || parsed.d == null || parsed.d.Cinemas == null) {
            throw new RuntimeException("Respuesta de GetNowPlaying vacía o con formato inesperado");
        }

        return flatten(parsed.d.Cinemas);
    }

    /**
     * Igual que getNowPlaying(), pero filtrando solo los cines cuyo "Key"
     * (slug) esté en cinemaKeys (ej. "premium-alisios", "las-arenas").
     */
    public List<ShowtimeData> getNowPlayingForCinemas(List<String> cinemaKeys) throws Exception {
        List<ShowtimeData> all = getNowPlaying();
        List<ShowtimeData> filtered = new ArrayList<>();

        for (ShowtimeData data : all) {
            if (cinemaKeys.contains(data.cinema())) {
                filtered.add(data);
            }
        }

        return filtered;
    }

    // ============================
    // Aplanado del árbol JSON: Cinema -> Date -> Movie -> Format -> Showtime
    // ============================
    private List<ShowtimeData> flatten(List<CinemaDTO> cinemas) {
        List<ShowtimeData> result = new ArrayList<>();
        String capturedAt = LocalDateTime.now().toString();

        for (CinemaDTO cinema : cinemas) {
            if (cinema.Dates == null) continue;

            for (DateDTO date : cinema.Dates) {
                if (date.Movies == null) continue;

                for (MovieDTO movie : date.Movies) {
                    if (movie.Formats == null) continue;

                    for (FormatDTO format : movie.Formats) {
                        if (format.Showtimes == null) continue;

                        for (ShowtimeDTO showtime : format.Showtimes) {

                            ShowtimeData data = new ShowtimeData(
                                    cinema.Key,           // identificador estable del cine (slug)
                                    movie.Title,
                                    showtime.Time,        // ej. "18:10"
                                    format.Name + " " + format.Language, // ej. "PREMIUM ESPAÑOL"
                                    movie.RunTime,
                                    movie.Rating,
                                    capturedAt
                            );

                            result.add(data);
                        }
                    }
                }
            }
        }

        return result;
    }
}