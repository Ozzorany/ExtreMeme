package com.example.extrememe.services;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.extrememe.model.Meme;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MemesService {
    public MemesService() {
    }

    public interface MyCallBack {
        void onCallback(List<Meme> memes);
    }


    public void getAllMemes(final MyCallBack myCallback) {
        List<Meme> list = new ArrayList<>();

        DatabaseDataLoader.getDB().collection("memes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                list.add(document.toObject(Meme.class));
                            }
                            myCallback.onCallback(list);
                        } else {
                            Log.w("warning", "Error getting documents.", task.getException());
                        }
                    }
                });
    }


    public void getMemesByUserId(String userId, final MyCallBack myCallback) {
        List<Meme> list = new ArrayList<>();

        DatabaseDataLoader.getDB().collection("memes")
                .whereEqualTo("userId", userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                list.add(document.toObject(Meme.class));
                            }
                            myCallback.onCallback(list);
                        } else {
                            Log.w("warning", "Error getting documents.", task.getException());
                        }
                    }
                });
    }
}
