package com.example.tom.myfirstapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.GsonBuilder;

import java.util.Collections;
import java.util.List;

import provider.MusicController;
import domain.Artist;
import domain.Song;
import util.PermissionManager;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.example.tom.myfirstapp.MESSAGE";
    public static final String SONG_DETAIL = "com.example.tom.myfirstapp.SONG_DETAIL";
    private static final int MY_PERMISSION_REQUEST = 1;
    private static final String PREF_LAST_PLAYED_SONG = "prefLastPlayedSong";
    private static final String PREF_SHUFFLING = "prefShuffling";
    private static final String PREF_LOOPING = "prefLooping";
    private static final String PREF_REPEATING = "prefRepeating";

    private SharedPreferences sharedPreferences;
    private MusicController musicController;
    private PermissionManager permissionManager;
    private Handler progressHandler = new Handler();
    private ProgressBar progressBar;
    private SongBaseAdapter adapter;
    private ListView listView;

    private GsonBuilder gsonBuilder = new GsonBuilder().serializeNulls();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);

        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        musicController = new MusicController(this);
        permissionManager = new PermissionManager();

        if(permissionManager.requestPermission(this)){
            addToListView();
        }

        getLastPlayedSong();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        switch (requestCode){
            case MY_PERMISSION_REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(this, "Permission granted!", Toast.LENGTH_SHORT).show();

                        addToListView();
                    }
                } else {
                    Toast.makeText(this, "No permission granted!", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            }
        }
    }

    public void addToListView(){
        musicController.getMusic();
        adapter = new SongBaseAdapter();
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

    public void shuffle(View view){
        musicController.shuffle();
        switchShuffleBackground();
    }

    public void loop(View view){
        musicController.loop();
        switchLoopBackground();
    }

    public void play(Song song){
        musicController.play(song);
        switchPlayButtonBackground();
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
        musicController.play();
        switchPlayButtonBackground();
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
        Song nextSong = musicController.getNextSong();
        handleNextSong(nextSong);
        int pos = adapter.getItemByName(nextSong.getTitle());
        listView.setItemChecked(pos, true);
    }

    public void previous(View view){
        Song previousSong = musicController.getPreviousSong();
        handlePreviousSong(previousSong);
        int pos = adapter.getItemByName(previousSong.getTitle());
        listView.setItemChecked(pos, true);
    }

    public void handleNextSong(Song nextSong){
        musicController.play(nextSong);//musicController.handleNextSong());
    }

    public void handlePreviousSong(Song previousSong){
        musicController.play(previousSong);
    }

    public void pause(){
        musicController.pause();
    }

    public void releasePlayer(){
        musicController.releasePlayer();
    }

    protected void onStop(){
        super.onStop();
        releasePlayer();
    }

    public void routeSongDetail(View view){
        Intent intent = new Intent(this, SongDetailActivity.class);
        intent.putExtra(SONG_DETAIL, gsonBuilder.create().toJson(musicController.getCurrentSong()));
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.my_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_setting){
            Toast.makeText(MainActivity.this,
                    "You have clicked on setting action menu",
                    Toast.LENGTH_SHORT)
                    .show();
        }

        if(item.getItemId() == R.id.action_about_us){
            Toast.makeText(MainActivity.this,
                    "You have clicked on about us action menu",
                    Toast.LENGTH_SHORT)
                    .show();
        }

        return super.onOptionsItemSelected(item);
    }

    public void switchPlayButtonBackground(){
        ImageButton playButton = findViewById(R.id.button_play);
        if(musicController.isPlaying()){
            playButton.setImageResource(R.drawable.baseline_pause_circle_outline_white_48dp);
        } else {
            playButton.setImageResource(R.drawable.baseline_play_circle_outline_white_48dp);
        }
    }

    public void switchLoopBackground(){
        ImageButton loopButton = findViewById(R.id.button_repeat);
        if(musicController.isLooping()){
            loopButton.setImageResource(R.drawable.ic_repeat_green_36dp);
        } else if (musicController.isRepeating()){
            loopButton.setImageResource(R.drawable.ic_repeat_one_green_36dp);
        } else{
            loopButton.setImageResource(R.drawable.ic_repeat_white_36dp);
        }
    }

    public void switchShuffleBackground(){
        ImageButton shuffleButton = findViewById(R.id.button_shuffle);
        if(musicController.isShuffling()){
            shuffleButton.setImageResource(R.drawable.baseline_shuffle_green_36dp);
        } else {
            shuffleButton.setImageResource(R.drawable.baseline_shuffle_white_36dp);
        }
    }

    public void routeTab(MenuItem menuItem){
        Intent intent = new Intent(this, TabbedActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top);
    }

    public void saveLastPlayedSong(){
        sharedPreferences = getSharedPreferences(PREF_LAST_PLAYED_SONG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(PREF_LAST_PLAYED_SONG, gsonBuilder.create().toJson(musicController.getCurrentSong()));
        editor.apply();
    }

    public void getLastPlayedSong(){
        sharedPreferences = getSharedPreferences(PREF_LAST_PLAYED_SONG, Context.MODE_PRIVATE);
        String songJson = sharedPreferences.getString(PREF_LAST_PLAYED_SONG, "");
        if(!songJson.isEmpty()){
            musicController.setCurrentSong(gsonBuilder.create().fromJson(songJson, Song.class));
        }
    }

    public void saveIsShuffling(){
        sharedPreferences = getSharedPreferences(PREF_SHUFFLING, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(PREF_LAST_PLAYED_SONG, gsonBuilder.create().toJson(musicController.isShuffling()));
        editor.apply();
    }

    public void saveIsLooping(){
        sharedPreferences = getSharedPreferences(PREF_LOOPING, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(PREF_LAST_PLAYED_SONG, gsonBuilder.create().toJson(musicController.isShuffling()));
        editor.apply();
    }

    class SongBaseAdapter extends BaseAdapter{
        private Context context;
        private List<Song> songs = musicController.getSongs();

        @Override
        public int getCount() {
            return musicController.getSongs().size();
        }

        @Override
        public Object getItem(int position) {
            return null;
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
            return 0;
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
                    PopupMenu popupMenu = new PopupMenu(MainActivity.this, textViewOptions);
                    popupMenu.inflate(R.menu.menu_song);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch(item.getItemId()){
                                case R.id.action_add_queue:
                                    Toast.makeText(MainActivity.this, "Song added to queue", Toast.LENGTH_SHORT).show();
                                    musicController.addToQueue(songs.get(position));
                                    break;
                                case R.id.action_add_playlist:
                                    Toast.makeText(MainActivity.this, "Add to playlist", Toast.LENGTH_SHORT).show();
                                    break;
                                case R.id.action_delete_song:
                                    Toast.makeText(MainActivity.this, "Delete song", Toast.LENGTH_SHORT).show();
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
