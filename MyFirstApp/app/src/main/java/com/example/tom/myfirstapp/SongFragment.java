package com.example.tom.myfirstapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
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
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.GsonBuilder;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import dao.ArtistDAO;
import dao.PlaylistDAO;
import dao.SongDAO;
import domain.Artist;
import domain.Playlist;
import domain.Song;
import provider.MusicController;
import util.PermissionManager;
import util.PreferenceManager;

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
    private PreferenceManager preferenceManager;

    private PermissionManager permissionManager;
    private SharedPreferences sharedPreferences;

    private SongDAO songDao;
    private PlaylistDAO playlistDao;
    private ArtistDAO artistDao;

    private GsonBuilder gsonBuilder = new GsonBuilder().serializeNulls();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragmentView = inflater.inflate(R.layout.songtab, container, false);

        //recyclerView = fragmentView.findViewById(R.id.songRecyclerView);
        //layoutManager = new LinearLayoutManager(fragmentView.getContext());
        //recyclerView.setLayoutManager(layoutManager);
        songDao = new SongDAO(fragmentView.getContext());
        playlistDao = new PlaylistDAO(fragmentView.getContext());
        artistDao = new ArtistDAO(fragmentView.getContext());

        preferenceManager = new PreferenceManager(fragmentView.getContext());

        permissionManager = new PermissionManager();
        musicController = new MusicController(fragmentView.getContext());

        /*if(permissionManager.requestPermissionFragment(getActivity())){
            getMusic();
        }*/

        //rAdapter = new SongAdapter(musicController.getSongs(), fragmentView.getContext());
        //rAdapter = new SongAdapter(songDao.getAll(), fragmentView.getContext());
        //recyclerView.setAdapter(rAdapter);

        adapter = new SongBaseAdapter();
        listView = fragmentView.findViewById(R.id.songRecyclerView);
        if(permissionManager.requestPermissionFragment(getActivity())){
            addToListView();
        }

        Song lastPlayedSong = preferenceManager.getLastPlayedSong();
        musicController.setCurrentSong(lastPlayedSong);
        TextView currentPlayingTextView = fragmentView.findViewById(R.id.nowPlayingTextView);
        if(lastPlayedSong != null){
            currentPlayingTextView.setText(lastPlayedSong.getTitle());
        }
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
        musicController.getMusic();
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

    public void play(Song song){
        musicController.play(song);
        switchPlayButtonBackground();
        updateLastPlayedSong();
    }

    public void play(View view){
        musicController.play();
        switchPlayButtonBackground();
        updateLastPlayedSong();
    }

    public void next(View view){
        Song nextSong = musicController.getNextSong();
        musicController.play(nextSong);
        int pos = adapter.getItemByName(nextSong.getTitle());
        listView.setItemChecked(pos, true);
    }

    public void updateLastPlayedSong(){
        TextView currentPlayingTextView = Objects.requireNonNull(getActivity()).findViewById(R.id.nowPlayingTextView);
        currentPlayingTextView.setText(musicController.getCurrentSong().getTitle());
    }

    public void routeSongDetail(View view){
        Intent intent = new Intent(getActivity(), SongDetailActivity.class);
        intent.putExtra(SONG_DETAIL, gsonBuilder.create().toJson(musicController.getCurrentSong()));
        startActivity(intent);
        Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top);
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
        private Context context;
        private List<Song> songs = musicController.getSongs();

        @Override
        public int getCount() {
            return songs.size();
        }

        @Override
        public Object getItem(int position) {
            return songs.get(position);
        }

        public int getItemByName(String name){
            for(int i = 0; musicController.getSongs().size() > i; i++){
                if (musicController.getSongs().get(i).getTitle().equals(name)){
                    return i;
                }
            }
            return -1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @SuppressLint({"ViewHolder", "InflateParams"})
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.listview_song, null);

            TextView songView = convertView.findViewById(R.id.songTextView);
            TextView artistView = convertView.findViewById(R.id.artistTextView);
            final ImageButton textViewOptions = convertView.findViewById(R.id.textViewOptions);

            songView.setText(songs.get(position).getTitle());
            artistView.setText(songs.get(position).getArtist().getName());

            textViewOptions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(getContext(), textViewOptions);
                    popupMenu.inflate(R.menu.menu_song);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch(item.getItemId()){
                                case R.id.action_add_queue:
                                    Toast.makeText(getContext(), "Song added to queue", Toast.LENGTH_SHORT).show();
                                    musicController.addToQueue(songs.get(position));
                                    break;
                                case R.id.action_add_playlist:
                                    Toast.makeText(getContext(), "Add to playlist", Toast.LENGTH_SHORT).show();
                                    break;
                                case R.id.action_delete_song:
                                    Toast.makeText(getContext(), "Delete song", Toast.LENGTH_SHORT).show();
                                    //notifyDataSetChanged();
                                    break;
                                default:
                                    break;
                            }
                            return false;
                        }
                    });
                    popupMenu.show();
                }
            });

            return convertView;
        }
    }
}
