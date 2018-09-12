package dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import database.MusicDbContract;
import database.MusicDbHelper;
import domain.Artist;

public class ArtistDAO {
    private MusicDbHelper musicDbHelper;
    private SQLiteDatabase db;

    public ArtistDAO(Context context){
        musicDbHelper = new MusicDbHelper(context);
        db = musicDbHelper.getWritableDatabase();
    }

    public boolean save(Artist artist){
        ContentValues values = new ContentValues();
        values.put(MusicDbContract.FeedEntry.COLUMN_NAME_NAME, artist.getName());

        return db.insert(MusicDbContract.FeedEntry.TABLE_NAME_ARTIST, null, values) > 0;
    }

    public boolean delete(long id){
        return db.delete(MusicDbContract.FeedEntry.TABLE_NAME_ARTIST, MusicDbContract.FeedEntry.KEY_NAME_ARTIST + " = " + id, null) > 0;
    }

    public List<Artist> getAll(){
        Cursor cursor = db.rawQuery("SELECT * FROM " + MusicDbContract.FeedEntry.TABLE_NAME_ARTIST, null);
        List<Artist> artists = new ArrayList<>();

        if(cursor.moveToFirst()){
            while (cursor.moveToNext()){
                String name = cursor.getString(cursor.getColumnIndex(MusicDbContract.FeedEntry.COLUMN_NAME_NAME));
                artists.add(new Artist(name));
            }
        }
        cursor.close();

        return artists;
    }
}
