package com.example.tom.myfirstapp;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.GsonBuilder;

import java.util.Collections;
import java.util.Objects;

import provider.MusicController;
import domain.Artist;
import domain.Song;

public class ArtistFragment extends Fragment {

    private static final String PREF_LAST_PLAYED_SONG = "prefLastPlayedSong";
    private MusicController musicController;
    private ArtistAdapter adapter;
    private ListView listView;
    private SharedPreferences sharedPreferences;

    private GsonBuilder gsonBuilder = new GsonBuilder().serializeNulls();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.artisttab, container, false);

        musicController = new MusicController();
        adapter = new ArtistAdapter();
        listView = fragmentView.findViewById(R.id.artistFragmentView);
        addToListView();

        getLastPlayedSong();
        TextView currentPlayingTextView = fragmentView.findViewById(R.id.nowPlayingTextView);
        currentPlayingTextView.setText(musicController.getCurrentSong().getTitle());

        return fragmentView;
    }

    public void addToListView(){
        getMusic();
        listView.setAdapter(adapter);
    }

    public void getMusic(){
        ContentResolver contentResolver = Objects.requireNonNull(getActivity()).getContentResolver();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0 ";
        String order = MediaStore.Audio.Media.TITLE + " ASC";
        Cursor cursor = contentResolver.query(uri, null, selection, null, order);

        if (cursor != null && cursor.moveToFirst()){
            while(cursor.moveToNext()){
                long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
//                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
//                if (artist.equalsIgnoreCase("<unknown>")){
//                    artist = retrieveArtist(title);
//                }
                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)).equals("<unknown>") ?
                        "Unknown artist" : cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                //String albumId = cursor.getString((cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)));
                //String albumCover = cursor.getString((cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART)));
                musicController.getSongs().add(new Song(id, title, new Artist(artist), path));
            }

            cursor.close();
        }
    }

    public void getLastPlayedSong(){
        sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences(PREF_LAST_PLAYED_SONG, Context.MODE_PRIVATE);
        String songJson = sharedPreferences.getString(PREF_LAST_PLAYED_SONG, "");
        if(!songJson.isEmpty()){
            musicController.setCurrentSong(gsonBuilder.create().fromJson(songJson, Song.class));
        }
    }

    class ArtistAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return musicController.getSongs().size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @SuppressLint({"ViewHolder", "InflateParams"})
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.listview_artist, null);

            TextView artistView = convertView.findViewById(R.id.artistTextView);
            artistView.setText(musicController.getSongs().get(position).getArtist().getName());

            return convertView;
        }
    }
}
