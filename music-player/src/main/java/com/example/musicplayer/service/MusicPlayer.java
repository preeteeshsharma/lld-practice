package com.example.musicplayer.service;

import com.example.musicplayer.model.Playlist;
import com.example.musicplayer.model.RepeatMode;
import com.example.musicplayer.model.Track;

import java.util.ArrayList;
import java.util.List;

public class MusicPlayer {
    private final Playlist playlist;
    private PlayerState state;
    private int currentIndex;
    private RepeatMode repeatMode;
    private final List<PlaybackListener> listeners = new ArrayList<>();

    public MusicPlayer(Playlist playlist) {
        if (playlist == null || playlist.size() == 0) {
            throw new IllegalArgumentException("Playlist must be non-empty");
        }
        this.playlist = playlist;
        this.state = StoppedState.INSTANCE;
        this.currentIndex = 0;
        this.repeatMode = RepeatMode.NONE;
    }

    // public API — user-facing actions
    public void play() {
        state.play(this);
    }

    public void pause() {
        state.pause(this);
    }

    public void stop() {
        state.stop(this);
    }

    public void next() {
        state.next(this);
    }

    public void prev() {
        state.prev(this);
    }

    public void setRepeatMode(RepeatMode repeatMode) {
        this.repeatMode = repeatMode;
    }

    // Observer pattern — register a listener to receive playback events.
    public void addListener(PlaybackListener listener) {
        listeners.add(listener);
    }

    // package-private — only state classes within this package should drive transitions
    void setState(PlayerState state) {
        this.state = state;
    }

    // Returns true if playback should continue/restart; false if it should stop.
    // package-private — called only by state classes
    boolean advanceIndex() {
        return switch (repeatMode) {
            case REPEAT_ONE -> true;
            case REPEAT_ALL -> {
                currentIndex = (currentIndex + 1) % playlist.size();
                yield true;
            }
            case NONE -> {
                if (currentIndex < playlist.size() - 1) {
                    currentIndex++;
                    yield true;
                } else {
                    yield false;
                }
            }
        };
    }

    // Returns true if playback should restart; false if already at start with NONE (no-op).
    // package-private — called only by state classes
    boolean rewindIndex() {
        return switch (repeatMode) {
            case REPEAT_ONE -> true;
            case REPEAT_ALL -> {
                currentIndex = (currentIndex - 1 + playlist.size()) % playlist.size();
                yield true;
            }
            case NONE -> {
                if (currentIndex > 0) {
                    currentIndex--;
                    yield true;
                } else {
                    yield false;
                }
            }
        };
    }

    // Event emission — domain stays pure, I/O is the listener's responsibility.
    // package-private — called only by state classes
    void notifyTrackStarted() {
        Track track = playlist.trackAt(currentIndex);
        for (PlaybackListener l : listeners) l.onTrackStarted(track);
    }

    void notifyTrackPaused() {
        Track track = playlist.trackAt(currentIndex);
        for (PlaybackListener l : listeners) l.onTrackPaused(track);
    }

    void notifyPlaybackStopped() {
        for (PlaybackListener l : listeners) l.onPlaybackStopped();
    }
}
