package com.example.musicplayer.service;

public class PausedState implements PlayerState {
    public static final PausedState INSTANCE = new PausedState();

    private PausedState() {
    }

    @Override
    public void play(MusicPlayer player) {
        player.setState(PlayingState.INSTANCE);
        player.notifyTrackStarted();
    }

    @Override
    public void pause(MusicPlayer player) {
        // no-op — already paused
    }

    @Override
    public void stop(MusicPlayer player) {
        player.notifyPlaybackStopped();
        player.setState(StoppedState.INSTANCE);
    }

    @Override
    public void next(MusicPlayer player) {
        player.advanceIndex(); // move index only — no playback restart while paused
    }

    @Override
    public void prev(MusicPlayer player) {
        player.rewindIndex(); // move index only — no playback restart while paused
    }
}
