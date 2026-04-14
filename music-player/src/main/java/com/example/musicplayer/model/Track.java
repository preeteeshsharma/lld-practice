package com.example.musicplayer.model;

public record Track(String id, String title, String artist, long durationMs) {
    public Track {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("Track id required");
        if (title == null || title.isBlank()) throw new IllegalArgumentException("Track title required");
    }
}
