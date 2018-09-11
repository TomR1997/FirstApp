package provider;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import dao.ArtistDAO;
import dao.SongDAO;
import domain.Artist;
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

    public void getMusic(Context context){
        SongDAO songDao = new SongDAO(context);
        ArtistDAO artistDao = new ArtistDAO(context);

        ContentResolver contentResolver = context.getContentResolver();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0 ";
        String order = MediaStore.Audio.Media.TITLE + " ASC";
        Cursor cursor = contentResolver.query(uri, null, selection, null, order);

        if (cursor != null && cursor.moveToFirst()){
            while(cursor.moveToNext()){
                long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)).equals("<unknown>") ?
                        "Unknown artist" : cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                //String albumId = cursor.getString((cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)));
                //String albumCover = cursor.getString((cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART)));

                Artist newArtist = new Artist(artist);
                Song newSong = new Song(id, title, newArtist, path);

                songs.add(newSong);
                songDao.save(newSong);
                if(!newArtist.getName().equals("Unknown artist")){
                    artistDao.save(newArtist);
                }
            }

            cursor.close();
        }
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
            nextSong = currentSongIndex;
            while(nextSong == currentSongIndex){
                nextSong = random.nextInt(songs.size());
            }
        } else {
            nextSong = currentSongIndex + 1;
            if (nextSong >= songs.size()){
                if (isLooping){
                    nextSong = 0;
                }
                else {
                    nextSong = currentSongIndex;
                    while(nextSong == currentSongIndex){
                        nextSong = random.nextInt(songs.size());
                    }
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
                    previousSong = currentSongIndex;
                    while(previousSong == currentSongIndex){
                        previousSong = random.nextInt(songs.size());
                    }
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

    public void setRepeating(boolean repeating) {
        isRepeating = repeating;
    }

    public void setLooping(boolean looping) {
        isLooping = looping;
    }

    public void setShuffling(boolean shuffling) {
        isShuffling = shuffling;
    }
}
