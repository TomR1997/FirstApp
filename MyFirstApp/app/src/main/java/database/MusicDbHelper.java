package database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MusicDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "MusicDb.db";

    public MusicDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(MusicDbContract.CREATE_TABLE_SONG);
        db.execSQL(MusicDbContract.CREATE_TABLE_PLAYLIST);
        db.execSQL(MusicDbContract.CREATE_TABLE_ARTIST);
        db.execSQL(MusicDbContract.CREATE_TABLE_PLAYLIST_SONG);
        db.execSQL(MusicDbContract.CREATE_TABLE_ARTIST_SONG);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(MusicDbContract.DROP_ALL);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){
        onUpgrade(db, oldVersion, newVersion);
    }
}

