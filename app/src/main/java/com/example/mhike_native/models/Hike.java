package com.example.mhike_native.models;

import java.time.LocalDate;

public class Hike {
    private int id;
    private String name;
    private String location;
    private LocalDate date;
    private boolean parking_available;
    private double length_km;
    private String difficulty;
    private String description;

    public Hike() {
    }

    public Hike(String name, String location, LocalDate date, boolean parking_available, double length_km, String difficulty, String description) {
        this.name = name;
        this.location = location;
        this.date = date;
        this.parking_available = parking_available;
        this.length_km = length_km;
        this.difficulty = difficulty;
        this.description = description;
    }

    public Hike(int id, String name, String location, LocalDate date, boolean parking_available, double length_km, String difficulty, String description) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.date = date;
        this.parking_available = parking_available;
        this.length_km = length_km;
        this.difficulty = difficulty;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public boolean isParking_available() {
        return parking_available;
    }

    public void setParking_available(boolean parking_available) {
        this.parking_available = parking_available;
    }

    public double getLength_km() {
        return length_km;
    }

    public void setLength_km(double length_km) {
        this.length_km = length_km;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
