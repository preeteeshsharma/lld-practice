package com.example.musicplayer.service;

import com.example.musicplayer.model.Track;

// Observer pattern — MusicPlayer emits events; listeners decide what to do with them.
// Default methods so each listener only overrides the events it cares about.
// Production uses: UI display board, equalizer animation, analytics, scrobbler.
public interface PlaybackListener {
    default void onTrackStarted(Track track) {}
    default void onTrackPaused(Track track) {}
    default void onPlaybackStopped() {}
}
