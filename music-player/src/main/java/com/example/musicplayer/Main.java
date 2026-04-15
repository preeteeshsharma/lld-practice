package com.example.musicplayer;

import com.example.musicplayer.model.Playlist;
import com.example.musicplayer.model.RepeatMode;
import com.example.musicplayer.model.Track;
import com.example.musicplayer.service.ConsoleDisplayBoard;
import com.example.musicplayer.service.MusicPlayer;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Track> tracks = List.of(
            new Track("1", "Blinding Lights", "The Weeknd", 200000),
            new Track("2", "Levitating", "Dua Lipa", 203000),
            new Track("3", "Stay", "Kid Laroi", 141000)
        );
        Playlist playlist = new Playlist("p1", "My Playlist", tracks);

        // Scenario 1: NONE — play through to end, stops on last track
        System.out.println("=== Scenario 1: NONE repeat, play to end ===");
        MusicPlayer player = new MusicPlayer(playlist);
        player.addListener(new ConsoleDisplayBoard());
        player.play();
        player.next();
        player.next(); // last track -> stops

        // Scenario 2: Pause -> next -> resume on correct track
        System.out.println("\n=== Scenario 2: Pause -> next -> resume ===");
        player = new MusicPlayer(playlist);
        player.addListener(new ConsoleDisplayBoard());
        player.play();
        player.pause();
        player.next(); // move index while paused (no notify)
        player.play(); // resume on track 2

        // Scenario 3: REPEAT_ALL — wraps around on last track
        System.out.println("\n=== Scenario 3: REPEAT_ALL wraps to first ===");
        player = new MusicPlayer(playlist);
        player.addListener(new ConsoleDisplayBoard());
        player.setRepeatMode(RepeatMode.REPEAT_ALL);
        player.play();
        player.next();
        player.next(); // last track
        player.next(); // wraps to track 1
    }
}
