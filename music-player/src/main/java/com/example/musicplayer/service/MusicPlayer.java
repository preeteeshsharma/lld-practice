package com.example.musicplayer.service;

import com.example.musicplayer.model.Playlist;
import com.example.musicplayer.model.Track;
import com.example.musicplayer.state.RepeatMode;

public class MusicPlayer {
    private final Playlist playlist;
    private PlayerState state;
    private int currentIndex;
    private RepeatMode repeatMode;

    public MusicPlayer(Playlist playlist) {
        this.playlist = playlist;
        this.state = StoppedState.INSTANCE;
        this.currentIndex = 0;
        this.repeatMode = RepeatMode.NONE;
    }

    public void setRepeatMode(RepeatMode repeatMode) {
        this.repeatMode = repeatMode;
    }

    public void setState(PlayerState state) {
        this.state = state;
    }

    // Returns true if playback should continue/restart; false if it should stop.
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
                } else yield false;
            }
        };
    }

    // Returns true if playback should restart; false if already at start with NONE (no-op).
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
                } else yield false;
            }
        };
    }

    public void startPlayback() {
        Track track = playlist.trackAt(currentIndex);
        System.out.printf("Now playing: %s%n", track.title());
    }

    public void stopPlayback() {
        System.out.println("Playback stopped");
    }

    // delegate methods — forward user actions to current state
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

}
