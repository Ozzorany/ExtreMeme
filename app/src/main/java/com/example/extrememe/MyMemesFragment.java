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
import com.example.extrememe.model.Model;

import java.util.List;

public class MyMemesFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_memes, container, false);
        RecyclerView memesRv = view.findViewById(R.id.mymemes_rv);
        memesRv.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        memesRv.setLayoutManager(layoutManager);

        List<Meme> data = Model.instance.getAllStudents();

        MyMemesAdapter adapter = new MyMemesAdapter(getLayoutInflater());
        adapter.data = data;
        memesRv.setAdapter(adapter);

        adapter.setOnClickListener(new MyMemesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Log.d("TAG","meme " + position + " was clicked ");
            }
        });


        return view;
    }
}