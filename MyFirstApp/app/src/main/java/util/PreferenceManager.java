package util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.GsonBuilder;

import java.util.Objects;

import dao.SongDAO;
import domain.Song;

public class PreferenceManager {
    private static final String PREF_LAST_PLAYED_SONG = "prefLastPlayedSong";
    private static final String PREF_SHUFFLING = "prefShuffling";
    private static final String PREF_LOOPING = "prefLooping";
    private static final String PREF_REPEATING = "prefRepeating";

    private SharedPreferences sharedPreferences;
    private Context context;
    private SongDAO songDao;
    private GsonBuilder gsonBuilder;

    public PreferenceManager(Context context) {
        this.context = context;
        gsonBuilder = new GsonBuilder().serializeNulls();
        songDao = new SongDAO(context);
    }

    public void saveLastPlayedSong(Song song){
        sharedPreferences = context.getSharedPreferences(PREF_LAST_PLAYED_SONG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(PREF_LAST_PLAYED_SONG, gsonBuilder.create().toJson(song));
        editor.apply();
    }

    public void saveIsShuffling(boolean isShuffling){
        sharedPreferences = context.getSharedPreferences(PREF_SHUFFLING, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(PREF_SHUFFLING, isShuffling);
        editor.apply();
    }

    public void saveIsLooping(boolean isLooping){
        sharedPreferences = context.getSharedPreferences(PREF_LOOPING, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(PREF_LOOPING, isLooping);
        editor.apply();
    }

    public void saveIsRepeating(boolean isRepeating){
        sharedPreferences = context.getSharedPreferences(PREF_REPEATING, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(PREF_REPEATING, isRepeating);
        editor.apply();
    }

    public Song getLastPlayedSong(){
        sharedPreferences = context.getSharedPreferences(PREF_LAST_PLAYED_SONG, Context.MODE_PRIVATE);
        String songJson = sharedPreferences.getString(PREF_LAST_PLAYED_SONG, null);

        if (songJson == null){
            return songDao.getFirst();
        }
        return gsonBuilder.create().fromJson(songJson, Song.class);
    }

    public boolean getIsShuffling(){
        sharedPreferences = context.getSharedPreferences(PREF_SHUFFLING, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(PREF_SHUFFLING, false);
    }

    public boolean getIsLooping(){
        sharedPreferences = context.getSharedPreferences(PREF_LOOPING, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(PREF_LOOPING, false);
    }

    public boolean getIsRepeating(){
        sharedPreferences = context.getSharedPreferences(PREF_REPEATING, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(PREF_REPEATING, false);
    }
}
