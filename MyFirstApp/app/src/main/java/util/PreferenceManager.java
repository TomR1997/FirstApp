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

    public Song getLastPlayedSong(){
        sharedPreferences = context.getSharedPreferences(PREF_LAST_PLAYED_SONG, Context.MODE_PRIVATE);
        String songJson = sharedPreferences.getString(PREF_LAST_PLAYED_SONG, null);

        if (songJson == null){
            //TODO INIT DEFAULT SONG
            return songDao.getFirst();
        }
        return gsonBuilder.create().fromJson(songJson, Song.class);
    }
}
