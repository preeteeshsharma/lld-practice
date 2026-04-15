package com.example.musicplayer.service;

import com.example.musicplayer.model.Track;

public class ConsoleDisplayBoard implements PlaybackListener {
    @Override
    public void onTrackStarted(Track track) {
        System.out.printf("[Display] Now playing: %s - %s%n", track.title(), track.artist());
    }

    @Override
    public void onTrackPaused(Track track) {
        System.out.printf("[Display] Paused: %s%n", track.title());
    }

    @Override
    public void onPlaybackStopped() {
        System.out.println("[Display] Playback stopped");
    }
}
