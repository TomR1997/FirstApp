package database;

import android.provider.BaseColumns;

public final class FeedReaderContract {
    public static final String DROP_TABLE = "DROP TABLE IF EXISTS ";

    private FeedReaderContract(){}

    public static class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME_PLAYLIST = "Playlist";
        public static final String TABLE_NAME_SONG = "Song";
        public static final String TABLE_NAME_PLAYLIST_SONG = "Playlist_Song";
        public static final String TABLE_NAME_ARTIST = "Artist";
        public static final String TABLE_NAME_ARTIST_SONG = "Artist_Song";

        public static final String COLUMN_NAME_TITLE = "Title";
    }

    private static final String CREATE_TABLE_PLAYLIST =
            "CREATE TABLE " + FeedEntry.TABLE_NAME_PLAYLIST + " (" +
                    FeedEntry._ID + " INTEGER PRIMARY KEY, " +
                    FeedEntry.COLUMN_NAME_TITLE + " TEXT)";

    private static final String CREATE_TABLE_SONG =
            "CREATE TABLE " + FeedEntry._ID + "(" +
                    FeedEntry._ID + " INTEGER PRIMARY KEY, " +
                    FeedEntry.TABLE_NAME_SONG + " TEXT)";


    private static final String DROP_ALL =
             DROP_TABLE + FeedEntry.TABLE_NAME_PLAYLIST +
             DROP_TABLE + FeedEntry.TABLE_NAME_ARTIST +
             DROP_TABLE + FeedEntry.TABLE_NAME_SONG +
             DROP_TABLE + FeedEntry.TABLE_NAME_ARTIST_SONG +
             DROP_TABLE + FeedEntry.TABLE_NAME_PLAYLIST_SONG;
}
