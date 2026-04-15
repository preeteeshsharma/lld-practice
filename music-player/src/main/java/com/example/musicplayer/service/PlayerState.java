package com.example.musicplayer.service;

public interface PlayerState {
    void play(MusicPlayer player);
    void pause(MusicPlayer player);
    void stop(MusicPlayer player);
    void next(MusicPlayer player);
    void prev(MusicPlayer player);
}
