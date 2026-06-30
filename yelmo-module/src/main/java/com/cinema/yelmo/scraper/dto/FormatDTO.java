package com.cinema.yelmo.scraper.dto;

import java.util.List;

public class FormatDTO {
    public String Name;     // ej. "PREMIUM", "SJ"
    public String Language; // ej. "ESPAÑOL", "INGLÉS SUBTITULADO EN ESPAÑOL (VOSE)"
    public List<ShowtimeDTO> Showtimes;
}
