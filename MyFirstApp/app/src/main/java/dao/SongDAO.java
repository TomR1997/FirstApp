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
import domain.Playlist;
import domain.Song;

public class SongDAO {
    private MusicDbHelper musicDbHelper;
    private SQLiteDatabase db;

    public SongDAO(Context context) {
        musicDbHelper = new MusicDbHelper(context);
        db = musicDbHelper.getWritableDatabase();
    }

    public boolean save(Song song){
        ContentValues values = new ContentValues();
        values.put(MusicDbContract.FeedEntry.COLUMN_NAME_TITLE, song.getTitle());
        values.put(MusicDbContract.FeedEntry.COLUMN_NAME_SONG_PATH, song.getPath());

        return db.insert(MusicDbContract.FeedEntry.TABLE_NAME_SONG, null, values) > 0;
    }

    public boolean delete(long id){
        return db.delete(MusicDbContract.FeedEntry.TABLE_NAME_SONG, MusicDbContract.FeedEntry.KEY_NAME_SONG + " = " + id, null) > 0;
    }

    public List<Song> getAll(){
        Cursor cursor = db.rawQuery("SELECT * FROM " + MusicDbContract.FeedEntry.TABLE_NAME_SONG, null);
        List<Song> songs = new ArrayList<>();

        if(cursor.moveToFirst()){
            while (cursor.moveToNext()){
                String title = cursor.getString(cursor.getColumnIndex(MusicDbContract.FeedEntry.COLUMN_NAME_TITLE));
                String path = cursor.getString(cursor.getColumnIndex(MusicDbContract.FeedEntry.COLUMN_NAME_SONG_PATH));
                //TODO GET ARTIST
                songs.add(new Song(title, new Artist("test"), path));
            }
        }
        cursor.close();

        return songs;
    }

    public Song getFirst(){
        Cursor cursor = db.rawQuery("SELECT * FROM " + MusicDbContract.FeedEntry.TABLE_NAME_SONG, null);
        Song song = null;

        if(cursor != null){
            if(cursor.moveToFirst()){
                String title = cursor.getString(cursor.getColumnIndex(MusicDbContract.FeedEntry.COLUMN_NAME_TITLE));
                String path = cursor.getString(cursor.getColumnIndex(MusicDbContract.FeedEntry.COLUMN_NAME_SONG_PATH));
                //TODO GET ARTIST
                song = new Song(title, new Artist("Test"), path);
            }
            cursor.close();
        }

        return song;
    }
}
