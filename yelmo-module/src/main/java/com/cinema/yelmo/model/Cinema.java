package com.cinema.yelmo.model;

import java.time.LocalDateTime;

public class Cinema {
    private int id;
    private String name;
    private String location;
    private LocalDateTime capturedAt;

    // Constructor
    public Cinema(int id, String name, String location) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.capturedAt = LocalDateTime.now();
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getLocation() { return location; }
    public LocalDateTime getCapturedAt() { return capturedAt; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setLocation(String location) { this.location = location; }

    // toString
    @Override
    public String toString() {
        return "Cinema{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", capturedAt=" + capturedAt +
                '}';
    }
}