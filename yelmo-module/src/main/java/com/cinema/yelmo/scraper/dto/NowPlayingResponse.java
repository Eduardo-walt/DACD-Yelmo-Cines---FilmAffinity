package com.cinema.yelmo.scraper.dto;

/**
 * Respuesta raíz de https://www.yelmocines.es/now-playing.aspx/GetNowPlaying
 * El servicio ASP.NET envuelve todo el payload real en un campo "d".
 */
public class NowPlayingResponse {
    public NowPlayingData d;
}
