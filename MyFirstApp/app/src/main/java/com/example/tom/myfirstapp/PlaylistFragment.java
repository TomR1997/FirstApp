package com.example.tom.myfirstapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import adapter.PlaylistAdapter;
import dao.PlaylistDAO;

public class PlaylistFragment extends Fragment{
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private PlaylistDAO playlistDao;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.playlisttab, container, false);

        recyclerView = fragmentView.findViewById(R.id.playlistView);
        layoutManager = new LinearLayoutManager(fragmentView.getContext());
        recyclerView.setLayoutManager(layoutManager);
        playlistDao = new PlaylistDAO(fragmentView.getContext());

        adapter = new PlaylistAdapter(playlistDao.getAll());
        recyclerView.setAdapter(adapter);

        return fragmentView;
    }
}
