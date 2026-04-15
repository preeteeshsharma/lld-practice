package com.example.musicplayer.service;

public class StoppedState implements PlayerState {
    public static final StoppedState INSTANCE = new StoppedState();

    private StoppedState() {
    }

    @Override
    public void play(MusicPlayer player) {
        player.setState(PlayingState.INSTANCE);
        player.notifyTrackStarted();
    }

    @Override
    public void pause(MusicPlayer player) {
        throw new IllegalStateException("Cannot pause: player is stopped");
    }

    @Override
    public void stop(MusicPlayer player) {
        // no-op — already stopped
    }

    @Override
    public void next(MusicPlayer player) {
        throw new IllegalStateException("Cannot navigate: player is stopped");
    }

    @Override
    public void prev(MusicPlayer player) {
        throw new IllegalStateException("Cannot navigate: player is stopped");
    }
}
