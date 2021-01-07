package com.example.extrememe;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.extrememe.adapters.MyMemesAdapter;
import com.example.extrememe.model.Meme;
import com.example.extrememe.model.meme.MemeModel;

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
        AlertDialog.Builder alBuilder = new AlertDialog.Builder(view.getContext());
        adapter = new MyMemesAdapter(getLayoutInflater());
        memesRv.setAdapter(adapter);
        adapter.isEditAvailable = true;

        // TODO: use real logged in user oz - and prevent reaching this page when not logged in
        MemeModel.instance.getMemesByUserId("1234", result -> {
            data = result;
            adapter.data = data;
            adapter.notifyDataSetChanged();

            adapter.setOnClickListener(position -> {
            });

            adapter.setOnRemoveListener(meme -> {
                alBuilder.setTitle("INFO").setMessage("Are you sure you want to delete the meme?").setPositiveButton("yes", (dialogInterface, i) -> MemeModel.instance.removeMeme(meme.getId(), result1 -> {
                    adapter.data.remove(meme);
                    adapter.notifyDataSetChanged();
                    dialogInterface.dismiss();
                })).setNegativeButton("no", (dialogInterface, i) -> dialogInterface.dismiss());

                alBuilder.show();
            });
        });

        return view;
    }
}