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
        public static final String COLUMN_NAME_NAME = "Name";
        public static final String COLUMN_NAME_SONG_PATH = "Path";

        public static final String KEY_NAME_PLAYLIST = "Playlist_Id";
        public static final String KEY_NAME_SONG = "Song_Id";
        public static final String KEY_NAME_ARTIST = "Artist_Id";
    }

    private static final String CREATE_TABLE_PLAYLIST =
            "CREATE TABLE " + FeedEntry.TABLE_NAME_PLAYLIST + " (" +
                    FeedEntry.KEY_NAME_PLAYLIST + " INTEGER PRIMARY KEY, " +
                    FeedEntry.COLUMN_NAME_TITLE + " TEXT)";

    private static final String CREATE_TABLE_SONG =
            "CREATE TABLE " + FeedEntry.TABLE_NAME_SONG + "(" +
                    FeedEntry.KEY_NAME_SONG + " INTEGER PRIMARY KEY, " +
                    FeedEntry.COLUMN_NAME_TITLE + " TEXT)";

    private static final String CREATE_TABLE_ARTIST =
            "CREATE TABLE " + FeedEntry.TABLE_NAME_ARTIST + " (" +
                    FeedEntry.KEY_NAME_ARTIST + " INTEGER PRIMARY KEY, " +
                    FeedEntry.COLUMN_NAME_NAME + " TEXT, " +
                    FeedEntry.COLUMN_NAME_SONG_PATH + " TEXT)";

    public static final String CREATE_TABLE_PLAYLIST_SONG =
            "CREATE TABLE " + FeedEntry.TABLE_NAME_PLAYLIST_SONG + " (" +
                    FeedEntry._ID + " INTEGER PRIMARY KEY, " +
                    FeedEntry.KEY_NAME_PLAYLIST + " INTEGER, " +
                    FeedEntry.KEY_NAME_SONG + " INTEGER)";

    public static final String CREATE_TABLE_ARTIST_SONG =
            "CREATE TABLE " + FeedEntry.TABLE_NAME_ARTIST_SONG + " (" +
                    FeedEntry._ID + " INTEGER PRIMARY KEY, " +
                    FeedEntry.KEY_NAME_ARTIST + " INTEGER, " +
                    FeedEntry.KEY_NAME_SONG + " INTEGER)";


    private static final String DROP_ALL =
             DROP_TABLE + FeedEntry.TABLE_NAME_PLAYLIST +
             DROP_TABLE + FeedEntry.TABLE_NAME_ARTIST +
             DROP_TABLE + FeedEntry.TABLE_NAME_SONG +
             DROP_TABLE + FeedEntry.TABLE_NAME_ARTIST_SONG +
             DROP_TABLE + FeedEntry.TABLE_NAME_PLAYLIST_SONG;
}
