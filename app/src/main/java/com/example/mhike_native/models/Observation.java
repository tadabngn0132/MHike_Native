package com.example.mhike_native.models;

import java.time.LocalTime;

public class Observation {
    private int id;
    private String title;
    private LocalTime timestamp;
    private String comments;
    private int hike_id;

    public Observation() {
    }

    public Observation(String title, LocalTime timestamp, String comments, int hike_id) {
        this.title = title;
        this.timestamp = timestamp;
        this.comments = comments;
        this.hike_id = hike_id;
    }

    public Observation(int id, String title, LocalTime timestamp, String comments, int hike_id) {
        this.id = id;
        this.title = title;
        this.timestamp = timestamp;
        this.comments = comments;
        this.hike_id = hike_id;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public int getHike_id() {
        return hike_id;
    }
}
