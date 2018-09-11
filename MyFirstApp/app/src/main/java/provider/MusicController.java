package provider;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import domain.Song;

public class MusicController {
    private MediaPlayer player;

    private boolean isRepeating;
    private boolean isLooping;
    private boolean isShuffling;

    private int loopCase = 0;

    private Song lastSong;
    private Song currentSong;

    private List<Song> songs;

    public MusicController(){
        isRepeating = false;
        isLooping = false;
        isShuffling = false;
        songs = new ArrayList<>();
    }

    public void shuffle(){
        isShuffling = !isShuffling;
    }

    public void loop(){
        switch (loopCase){
            case 0:
                isLooping = true;
                break;
            case 1:
                isLooping = false;
                isRepeating = true;
                break;
            case 2:
                isLooping = false;
                isRepeating = false;
                loopCase = -1;
                break;
        }
        loopCase++;
    }

    public void play(Context context, Song song){
        currentSong = song;
        releasePlayer();
        if (player == null){
            player = MediaPlayer.create(context, Uri.parse(song.getPath()));
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    getNextSong();
                }
            });
        }
        if (player != null) {
            player.start();
        }
    }

    public void play(Context context){
        if (currentSong == null) {
            getCurrentSong();
        }
        if (player == null) {

            player = MediaPlayer.create(context, Uri.parse(currentSong.getPath()));
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    getNextSong();
                }
            });
        }
        if (player != null){
            if (player.isPlaying()) {
                pause();
            } else {
                player.start();
            }
        }
    }

    public Song getNextSong(){
        int currentSongIndex = songs.indexOf(currentSong);
        int nextSong;
        Random random = new Random();

        if(isRepeating){
            return currentSong;
        } else if (isShuffling){
            nextSong = random.nextInt(songs.size() + 1);
        } else {
            nextSong = currentSongIndex + 1;
            if (nextSong >= songs.size()){
                if (isLooping){
                    nextSong = 0;
                }
                else {
                    nextSong = random.nextInt(songs.size() + 1);
                }
            }
        }

        lastSong = currentSong;
        return songs.get(nextSong);
    }

    public Song getPreviousSong(){
        int currentSongIndex = songs.indexOf(currentSong);
        int previousSong;

        if(isRepeating){
            return currentSong;
        } else if(isShuffling){
            return lastSong;
        } else {
            previousSong = currentSongIndex - 1;
            if (previousSong < 0){
                if(isLooping){
                    previousSong = songs.size() - 1;
                } else {
                    Random random = new Random();
                    previousSong = random.nextInt(songs.size() + 1);
                }
            }
        }

        return songs.get(previousSong);
    }

    public void pause(){
        if (player != null){
            player.pause();
        }
    }

    public void releasePlayer(){
        if (player != null){
            player.release();
            player = null;
        }
    }

    public boolean isRepeating() {
        return isRepeating;
    }

    public boolean isLooping() {
        return isLooping;
    }

    public boolean isShuffling() {
        return isShuffling;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    public Song getCurrentSong() {
        if (currentSong == null && songs.size() > 0){
            currentSong = songs.get(1);
        }
        return currentSong;
    }

    public void setCurrentSong(Song currentSong) {
        this.currentSong = currentSong;
    }

    public boolean isPlaying() {
        return player != null && player.isPlaying();
    }

    public int getSongDuration() {
        return player != null ? player.getDuration() : 0;
    }

    public int getSongProgress(){
        return player != null ? player.getCurrentPosition() : 0;
    }

    public Song getLastSong() {
        if(lastSong == null){
            lastSong = currentSong;
        }
        return lastSong;
    }

    public void setLastSong(Song lastSong) {
        this.lastSong = lastSong;
    }
}
