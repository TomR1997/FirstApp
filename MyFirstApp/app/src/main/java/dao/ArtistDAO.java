package dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import database.MusicDbContract;
import database.MusicDbHelper;
import domain.Artist;;

public class ArtistDAO {
    private MusicDbHelper musicDbHelper;
    private SQLiteDatabase db;

    public ArtistDAO(Context context){
        musicDbHelper = new MusicDbHelper(context);
        db = musicDbHelper.getWritableDatabase();
    }

    public boolean insert(Artist artist){
        ContentValues values = new ContentValues();
        values.put(MusicDbContract.FeedEntry.TABLE_NAME_ARTIST, artist.getName());

        return db.insert(MusicDbContract.FeedEntry.TABLE_NAME_ARTIST, null, values) > 0;
    }

    public boolean delete(long id){
        return db.delete(MusicDbContract.FeedEntry.TABLE_NAME_ARTIST, MusicDbContract.FeedEntry.KEY_NAME_ARTIST + " = " + id, null) > 0;
    }
}
