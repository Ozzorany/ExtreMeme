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

        MemeModel.instance.getMemesByUserId("1234", new MemeModel.GetMemesByUserListener() {
            @Override
            public void onComplete(List<Meme> result) {
                data = result;
                adapter.data = data;
                adapter.notifyDataSetChanged();

                adapter.setOnClickListener(new MyMemesAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                    }
                });

                adapter.setOnRemoveListener(new MyMemesAdapter.OnMemeRemoveListener() {
                    @Override
                    public void onItemRemove(Meme meme) {
                        alBuilder.setTitle("INFO").setMessage("Are you sure you want to delete the meme?").setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                MemeModel.instance.removeMeme(meme.getId(), new MemeModel.RemoveMemeListener() {
                                    @Override
                                    public void onComplete(Void result) {
                                        adapter.data.remove(meme);
                                        adapter.notifyDataSetChanged();
                                        dialogInterface.dismiss();
                                    }
                                });
                            }
                        }).setNegativeButton("no", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });

                        alBuilder.show();
                    }
                });
            }
        });

        return view;
    }
}