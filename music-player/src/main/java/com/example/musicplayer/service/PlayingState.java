package com.example.musicplayer.service;

public class PlayingState implements PlayerState {
    public static final PlayingState INSTANCE = new PlayingState();

    private PlayingState() {
    }

    @Override
    public void play(MusicPlayer player) {
        // no-op
    }

    @Override
    public void pause(MusicPlayer player) {
        player.stopPlayback();
        player.setState(PausedState.INSTANCE);
    }

    @Override
    public void stop(MusicPlayer player) {
        player.stopPlayback();
        player.setState(StoppedState.INSTANCE);
    }

    @Override
    public void next(MusicPlayer player) {
        if (player.advanceIndex()) {
            player.startPlayback();
        } else {
            player.setState(StoppedState.INSTANCE);
        }
    }

    @Override
    public void prev(MusicPlayer player) {
        if (player.rewindIndex()) {
            player.startPlayback();
        }
        // NONE at first track: no-op — don't restart, don't change state
    }
}
