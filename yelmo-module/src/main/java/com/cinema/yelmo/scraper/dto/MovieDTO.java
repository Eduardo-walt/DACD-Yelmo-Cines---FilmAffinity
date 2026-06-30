package com.cinema.yelmo.scraper.dto;

import java.util.List;

public class MovieDTO {
    public int Id;
    public String Title;
    public String Rating;
    public String RunTime; // viene como String en el JSON, ej. "108"
    public String Director;
    public String Synopsis;
    public List<FormatDTO> Formats;
}
