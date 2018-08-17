package com.example.tom.myfirstapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.google.gson.GsonBuilder;

import domain.Song;

public class SongDetailActivity extends AppCompatActivity {
    private GsonBuilder gsonBuilder = new GsonBuilder().serializeNulls();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_detail);

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.SONG_DETAIL);
        Song song = gsonBuilder.create().fromJson(message, Song.class);

        Toolbar toolbar = findViewById(R.id.my_toolbar);
        toolbar.setTitle(song.getTitle());
        setSupportActionBar(toolbar);


    }

    public void play(View view){

    }

    public void loop(View view){

    }

    public void shuffle(View view){

    }

    public void previous(View view){

    }

    public void next(View view){

    }
}
