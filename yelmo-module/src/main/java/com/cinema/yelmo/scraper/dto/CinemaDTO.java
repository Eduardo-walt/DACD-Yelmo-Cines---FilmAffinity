package com.cinema.yelmo.scraper.dto;

import java.util.List;

public class CinemaDTO {
    public int Id;
    public String CityKey;
    public String CityName;
    public String Key;   // ej. "premium-alisios"
    public String Name;  // ej. "Premium Alisios"
    public List<DateDTO> Dates;
}
