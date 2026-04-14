package com.example.musicplayer.model;

import java.util.List;

public record Playlist(String id, String name, List<Track> tracks) {
    public Playlist {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("Playlist id required");
        if (tracks == null) throw new IllegalArgumentException("Tracks cannot be null");
        tracks = List.copyOf(tracks); // defensive immutable copy
    }

    public int size() {
        return tracks.size();
    }

    public Track trackAt(int index) {
        return tracks.get(index);
    }
}
