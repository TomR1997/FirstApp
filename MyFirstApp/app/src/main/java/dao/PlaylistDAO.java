package dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import database.MusicDbContract;
import database.MusicDbHelper;
import domain.Playlist;
import domain.Song;

public class PlaylistDAO {
    private MusicDbHelper musicDbHelper;
    private SQLiteDatabase db;

    public PlaylistDAO(Context context){
        musicDbHelper = new MusicDbHelper(context);
        db = musicDbHelper.getWritableDatabase();
    }

    public boolean save(Playlist playlist){
        ContentValues values = new ContentValues();
        values.put(MusicDbContract.FeedEntry.COLUMN_NAME_TITLE, playlist.getTitle());

        return db.insert(MusicDbContract.FeedEntry.TABLE_NAME_PLAYLIST, null, values) > 0;
    }

    public boolean delete(long id){
        return db.delete(MusicDbContract.FeedEntry.TABLE_NAME_PLAYLIST,
                MusicDbContract.FeedEntry.KEY_NAME_PLAYLIST + " = " + id, null) > 0;
    }

    public List<Playlist> getAll(){
       Cursor cursor = db.rawQuery("SELECT * FROM " + MusicDbContract.FeedEntry.TABLE_NAME_PLAYLIST, null);
       List<Playlist> playlists = new ArrayList<>();

       if(cursor.moveToFirst()){
           while (cursor.moveToNext()){
               String title = cursor.getString(cursor.getColumnIndex(MusicDbContract.FeedEntry.COLUMN_NAME_TITLE));
               playlists.add(new Playlist(title));
           }
       }
       cursor.close();

       return playlists;
    }

    public boolean addSong(Playlist playlist, Song song){
        ContentValues values = new ContentValues();

        values.put(MusicDbContract.FeedEntry.KEY_NAME_PLAYLIST, playlist.getId());
        values.put(MusicDbContract.FeedEntry.KEY_NAME_SONG, playlist.getId());

        return db.insert(MusicDbContract.FeedEntry.TABLE_NAME_PLAYLIST_SONG, null, values) > 0;
    }

    public boolean deleteSong(long playlistId, long songId){
        String selection = MusicDbContract.FeedEntry.KEY_NAME_PLAYLIST + " = " + playlistId +
                " AND " + MusicDbContract.FeedEntry.KEY_NAME_SONG + " = " + songId;
        return db.delete(MusicDbContract.FeedEntry.TABLE_NAME_PLAYLIST_SONG, selection, null) > 0;
    }
}
