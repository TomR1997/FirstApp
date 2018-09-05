package provider;

import android.content.Context;
import android.content.SharedPreferences;
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

    private int nextShufflSong = 0;

    private Song currentSong;

    private List<Song> songs;
    private List<Song> shuffledSongs;

    private static final String LAST_PLAYED_SONG = "lastPlayedSong";
    private SharedPreferences sharedPreferences;

    public MusicController(){
        isRepeating = false;
        isLooping = false;
        isShuffling = false;
        songs = new ArrayList<>();
        shuffledSongs = new ArrayList<>();
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
                isRepeating = false;
                break;
            case 2:
                isLooping = false;
                isRepeating = true;
                loopCase = 0;
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
                    handleNextSong();
                }
            });
        }
        if (player != null) {
            player.start();
        }
    }

    public void play(Context context){
        if (currentSong != null) {
            if (player == null) {

                player = MediaPlayer.create(context, Uri.parse(currentSong.getPath()));
                player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        handleNextSong();
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
    }

    public Song handleNextSong(){
        if(isRepeating){
            return currentSong;
        } else if (isLooping && isShuffling){
            return getNextSong(currentSong, true, true);
        } else if (isLooping){
            return getNextSong(currentSong, true, false);
        } else if (isShuffling) {
            return getNextSong(currentSong, false, true);
        } else{
            return getNextSong(currentSong, false, false);
        }
    }

    public Song handlePreviousSong(){
        if(isRepeating){
            return currentSong;
        } else if (isLooping && isShuffling){
            return getPreviousSong(currentSong, true, true);
        } else if (isLooping){
            return getPreviousSong(currentSong, true, false);
        } else if (isShuffling) {
            return getPreviousSong(currentSong, false, true);
        } else {
            return getPreviousSong(currentSong, false, false);
        }
    }



    private Song getPreviousSong(Song currentSong, boolean isLooping, boolean isShuffling){
        int currentSongIndex;
        if (isShuffling){
            currentSongIndex = shuffledSongs.indexOf(currentSong);
        } else {
            currentSongIndex = songs.indexOf(currentSong);
        }

        int nextSong = currentSongIndex - 1;
        if (nextSong <= 0){
            if (isLooping) {
                nextSong = songs.size();
            } else {
                Random random = new Random();
                nextSong = random.nextInt(songs.size() + 1);
            }
        }

        return songs.get(nextSong);
    }

    private Song getNextSong(Song currentSong, boolean isLooping, boolean isShuffling){
        int currentSongIndex;
        if (isShuffling){
            currentSongIndex = shuffledSongs.indexOf(currentSong);
        } else {
            currentSongIndex = songs.indexOf(currentSong);
        }

        int nextSong = currentSongIndex + 1;
        if (nextSong > songs.size()){
            if (isLooping) {
                nextSong = 1;
            } else {
                Random random = new Random();
                nextSong = random.nextInt(songs.size() + 1);
                nextShufflSong=nextSong;
            }
        }

        return songs.get(nextSong);
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

    public void setRepeating(boolean repeating) {
        isRepeating = repeating;
    }

    public boolean isLooping() {
        return isLooping;
    }

    public void setLooping(boolean looping) {
        isLooping = looping;
    }

    public boolean isShuffling() {
        return isShuffling;
    }

    public void setShuffling(boolean shuffling) {
        isShuffling = shuffling;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    public List<Song> getShuffledSongs() {
        return shuffledSongs;
    }

    public void setShuffledSongs(List<Song> shuffledSongs) {
        this.shuffledSongs = shuffledSongs;
    }

    public Song getCurrentSong() {
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

    public int getNextShufflSong() {
        return nextShufflSong;
    }
}
