package dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import database.MusicDbContract;
import database.MusicDbHelper;
import domain.Song;

public class SongDAO {
    private MusicDbHelper musicDbHelper;
    private SQLiteDatabase db;

    public SongDAO(Context context) {
        musicDbHelper = new MusicDbHelper(context);
        db = musicDbHelper.getWritableDatabase();
    }

    public boolean insert(Song song){
        ContentValues values = new ContentValues();
        values.put(MusicDbContract.FeedEntry.COLUMN_NAME_TITLE, song.getTitle());
        values.put(MusicDbContract.FeedEntry.COLUMN_NAME_SONG_PATH, song.getPath());

        return db.insert(MusicDbContract.FeedEntry.TABLE_NAME_SONG, null, values) > 0;
    }

    public boolean delete(long id){
        return db.delete(MusicDbContract.FeedEntry.TABLE_NAME_SONG, MusicDbContract.FeedEntry.KEY_NAME_SONG + " = " + id, null) > 0;
    }
}
