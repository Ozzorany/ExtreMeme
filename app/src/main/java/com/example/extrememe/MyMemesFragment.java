package com.example.extrememe;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.extrememe.adapters.MyMemesAdapter;
import com.example.extrememe.model.Meme;
import com.example.extrememe.services.MemesService;

import java.util.ArrayList;
import java.util.List;

public class MyMemesFragment extends Fragment {
    List<Meme> data = new ArrayList<>();
    MyMemesAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_memes, container, false);
        RecyclerView memesRv = view.findViewById(R.id.mymemes_rv);
        memesRv.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        memesRv.setLayoutManager(layoutManager);
        adapter = new MyMemesAdapter(getLayoutInflater());
        memesRv.setAdapter(adapter);


        new MemesService().getMemesByUserId("1234", new MemesService.MyCallBack() {
            @Override
            public void onCallback(List<Meme> memes) {
                data = memes;
                adapter.data = data;
                adapter.notifyDataSetChanged();


                adapter.setOnClickListener(new MyMemesAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                    }
                });
            }
        });

        return view;
    }
}