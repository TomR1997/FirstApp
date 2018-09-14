package com.example.tom.myfirstapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

import com.google.gson.GsonBuilder;

import domain.Song;
import provider.MusicController;
import util.PreferenceManager;

public class SongDetailActivity extends AppCompatActivity {
    private GsonBuilder gsonBuilder = new GsonBuilder().serializeNulls();
    private MusicController musicController;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_detail);
        musicController = new MusicController(this);
        preferenceManager = new PreferenceManager(this);

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.SONG_DETAIL);
        musicController.setCurrentSong(gsonBuilder.create().fromJson(message, Song.class));

        Toolbar toolbar = findViewById(R.id.my_toolbar);
        toolbar.setTitle(musicController.getCurrentSong().getTitle());
        setSupportActionBar(toolbar);

        musicController.play();

        musicController.setShuffling(preferenceManager.getIsShuffling());
        musicController.setLooping(preferenceManager.getIsLooping());
        musicController.setRepeating(preferenceManager.getIsRepeating());
        switchLoopBackground();
        switchShuffleBackground();
        switchPlayButtonBackground();
    }

    public void play(View view){
        musicController.play();
        switchPlayButtonBackground();
    }

    public void loop(View view){
        musicController.loop();
        switchLoopBackground();
    }

    public void shuffle(View view){
        musicController.shuffle();
        switchShuffleBackground();
    }

    public void previous(View view){
        Song previousSong = musicController.getPreviousSong();
        musicController.play(previousSong);
    }

    public void next(View view){
        Song nextSong = musicController.getNextSong();
        musicController.play(nextSong);
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
}
