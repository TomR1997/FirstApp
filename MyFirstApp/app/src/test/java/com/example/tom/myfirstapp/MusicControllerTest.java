package com.example.tom.myfirstapp;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import domain.Artist;
import domain.Song;
import provider.MusicController;

import org.junit.Test;
import static org.junit.Assert.*;

public class MusicControllerTest {
    private List<Song> songs;
    private MusicController musicController;

    @Before
    public void setUp(){
        musicController = new MusicController();
        songs = new ArrayList<>();
        for (int i = 0; i < 5; i++){
            songs.add(new Song((long)i, "Song" +i, new Artist("Artist"+ i), ""));
        }
        musicController.setSongs(songs);
        musicController.setCurrentSong(songs.get(1));
    }

    @Test
    public void getNextSongTest(){
        Song song = musicController.getNextSong();
        assertEquals(song.getId(), musicController.getCurrentSong().getId() + 1);
    }

    @Test
    public void getNextSongRepeatTest(){
        musicController.setRepeating(true);
        Song song = musicController.getNextSong();
        assertEquals(song.getId(), musicController.getCurrentSong().getId());
    }

    @Test
    public void getNextSongShuffleTest(){
        musicController.setShuffling(true);
        Song song = musicController.getNextSong();
        assertTrue(song.getId() != musicController.getCurrentSong().getId());
    }

    @Test
    public void getNextSongEndLoop(){
        musicController.setLooping(true);
        musicController.setCurrentSong(songs.get(songs.size() - 1));
        Song song = musicController.getNextSong();
        assertEquals(song.getId(), 0);
    }

    @Test
    public void getNextSongRandom(){
        musicController.setCurrentSong(songs.get(songs.size() - 1));
        Song song = musicController.getNextSong();
        assertTrue(song.getId() != musicController.getCurrentSong().getId());
    }

    @Test
    public void getPreviousSong(){
        musicController.setCurrentSong(songs.get(songs.size() - 1));
        Song song = musicController.getPreviousSong();
        assertEquals(song.getId(), musicController.getCurrentSong().getId() - 1);
    }

    @Test
    public void getPreviousSongRepeat(){
        musicController.setRepeating(true);
        Song song = musicController.getPreviousSong();
        assertEquals(song.getId(), musicController.getCurrentSong().getId());
    }

    @Test
    public void getPreviousSongEndLoop(){
        musicController.setLooping(true);
        musicController.setCurrentSong(songs.get(0));
        Song song = musicController.getPreviousSong();
        assertEquals(song.getId(), songs.size() - 1);
    }

    @Test
    public void getPreviousSongRandom(){
        musicController.setCurrentSong(songs.get(0));
        Song song = musicController.getPreviousSong();
        assertTrue(song.getId() != musicController.getCurrentSong().getId());
    }
}
