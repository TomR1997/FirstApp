package com.example.tom.myfirstapp;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.GsonBuilder;

import java.util.Collections;
import java.util.Objects;

import adapter.SongAdapter;
import dao.SongDAO;
import domain.Artist;
import domain.Song;
import provider.MusicController;
import util.PermissionManager;

public class SongFragment extends Fragment {
    public static final String EXTRA_MESSAGE = "com.example.tom.myfirstapp.MESSAGE";
    public static final String SONG_DETAIL = "com.example.tom.myfirstapp.SONG_DETAIL";
    private static final int MY_PERMISSION_REQUEST = 1;
    private static final String PREF_LAST_PLAYED_SONG = "prefLastPlayedSong";
    private static final String PREF_SHUFFLING = "prefShuffling";
    private static final String PREF_LOOPING = "prefLooping";
    private static final String PREF_REPEATING = "prefRepeating";

    private RecyclerView recyclerView;
    private RecyclerView.Adapter rAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private SongBaseAdapter adapter;
    private ListView listView;
    private MusicController musicController;
    private PermissionManager permissionManager;
    private SharedPreferences sharedPreferences;

    private SongDAO songDao;

    private GsonBuilder gsonBuilder = new GsonBuilder().serializeNulls();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragmentView = inflater.inflate(R.layout.songtab, container, false);

        recyclerView = fragmentView.findViewById(R.id.songRecyclerView);
        layoutManager = new LinearLayoutManager(fragmentView.getContext());
        recyclerView.setLayoutManager(layoutManager);
        songDao = new SongDAO(fragmentView.getContext());

        permissionManager = new PermissionManager();
        musicController = new MusicController();
        if(permissionManager.requestPermissionFragment(getActivity())){
            getMusic();
        }

        rAdapter = new SongAdapter(musicController.getSongs());
        //rAdapter = new SongAdapter(songDao.getAll());
        recyclerView.setAdapter(rAdapter);

        /*adapter = new SongBaseAdapter();
        listView = fragmentView.findViewById(R.id.songTextView);
        if(permissionManager.requestPermissionFragment(getActivity())){
            addToListView();
        }*/

        getLastPlayedSong();
        TextView currentPlayingTextView = fragmentView.findViewById(R.id.nowPlayingTextView);
        currentPlayingTextView.setText(musicController.getCurrentSong().getTitle());
        currentPlayingTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                routeSongDetail(view);
            }
        });

        ImageButton playButton = fragmentView.findViewById(R.id.button_play);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                play(view);
            }
        });

        return fragmentView;
    }

    public void addToListView(){
        getMusic();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                play(musicController.getSongs().get(i));
                listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
                listView.setItemChecked(adapterView.getPositionForView(view), true);
                //routeSongDetail(view);
            }
        });

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

                Song newSong = new Song(id, title, new Artist(artist), path);
                musicController.getSongs().add(newSong);
                songDao.insert(newSong);
            }
            musicController.getShuffledSongs().addAll(musicController.getSongs());
            Collections.shuffle(musicController.getShuffledSongs());

            cursor.close();
        }
    }

    public void play(Song song){
        musicController.play(getActivity(), song);
        switchPlayButtonBackground();
        saveLastPlayedSong();
//
//        progressBar = findViewById(R.id.progressBar);
//        progressBar.setProgress(0);
//        progressBar.setMax(musicController.getSongDuration());
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while(musicController.getSongProgress() < musicController.getSongDuration()){
//                    progressHandler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            progressBar.setProgress(musicController.getSongProgress());
//                        }
//                    });
//                }
//            }
//        }).start();
    }

    public void play(View view){
        musicController.play(getActivity());
        switchPlayButtonBackground();
        saveLastPlayedSong();
//
//        progressBar = findViewById(R.id.progressBar);
//        progressBar.setProgress(0);
//        progressBar.setMax(musicController.getSongDuration());
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while(musicController.getSongProgress() < musicController.getSongDuration()){
//                    progressHandler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            progressBar.setProgress(musicController.getSongProgress());
//                        }
//                    });
//                }
//            }
//        }).start();
    }

    public void next(View view){
        handleNextSong();
        //listView.setItemChecked(musicController.getNextShuffleSong(), true);
        //View songView = adapter.getView(musicController.getSongs().get(1).toString());
        //check & mark next song
    }

    public void handleNextSong(){
        musicController.play(getActivity(), musicController.handleNextSong());
    }

    public void routeSongDetail(View view){
        Intent intent = new Intent(getActivity(), SongDetailActivity.class);
        intent.putExtra(SONG_DETAIL, gsonBuilder.create().toJson(musicController.getCurrentSong()));
        startActivity(intent);
        Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top);
    }

    public void saveLastPlayedSong(){
        sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences(PREF_LAST_PLAYED_SONG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(PREF_LAST_PLAYED_SONG, gsonBuilder.create().toJson(musicController.getCurrentSong()));
        editor.apply();
    }

    public void getLastPlayedSong(){
        sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences(PREF_LAST_PLAYED_SONG, Context.MODE_PRIVATE);
        String songJson = sharedPreferences.getString(PREF_LAST_PLAYED_SONG, "");
        if(!songJson.isEmpty()){
            musicController.setCurrentSong(gsonBuilder.create().fromJson(songJson, Song.class));
        }
        else {
            Song song = musicController.getSongs().get(0);
            if (song != null){
                musicController.setCurrentSong(song);
            }
        }
    }

    public void switchPlayButtonBackground(){
        ImageButton playButton = Objects.requireNonNull(getActivity()).findViewById(R.id.button_play);
        if(musicController.isPlaying()){
            playButton.setImageResource(R.drawable.baseline_pause_circle_outline_white_48dp);
        } else {
            playButton.setImageResource(R.drawable.baseline_play_circle_outline_white_48dp);
        }
    }

    public void routeTab(MenuItem menuItem){
        Intent intent = new Intent(getActivity(), TabbedActivity.class);
        startActivity(intent);
        Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top);
    }

    class SongBaseAdapter extends BaseAdapter {

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
            convertView = getLayoutInflater().inflate(R.layout.listview_song, null);

            TextView songView = convertView.findViewById(R.id.songTextView);
            TextView artistView = convertView.findViewById(R.id.artistTextView);

            songView.setText(musicController.getSongs().get(position).getTitle());
            artistView.setText(musicController.getSongs().get(position).getArtist().getName());

            //songView.setTag(musicController.getSongs().get(position).getTitle());

            return convertView;
        }
    }
}
