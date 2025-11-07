package com.example.mhike_native.models;

import java.time.LocalDateTime;

public class Observation {
    private long id;
    private String title;
    private LocalDateTime timestamp;
    private String comments;
    private int hike_id;

    public Observation() {
    }

    public Observation(String title, LocalDateTime timestamp, String comments, int hike_id) {
        this.title = title;
        this.timestamp = timestamp;
        this.comments = comments;
        this.hike_id = hike_id;
    }

    public Observation(long id, String title, LocalDateTime timestamp, String comments, int hike_id) {
        this.id = id;
        this.title = title;
        this.timestamp = timestamp;
        this.comments = comments;
        this.hike_id = hike_id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
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

    public void setHike_id(int hike_id) {
        this.hike_id = hike_id;
    }
}
