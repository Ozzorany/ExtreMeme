package com.example.extrememe.services;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.extrememe.model.Meme;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MemesService {
    private static final String TAG = "MemesService";

    public MemesService() {
    }

    public interface getMemesCallBack {
        void onCallback(List<Meme> memes);
    }

    public interface UpdateMemeCallBack {
        void onCallback();
    }


    public void getAllMemes(final getMemesCallBack getMemesCallback) {
        List<Meme> list = new ArrayList<>();

        DatabaseDataLoader.getDB().collection("memes")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            list.add(document.toObject(Meme.class));
                        }
                        getMemesCallback.onCallback(list);
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                });
    }


    public void getMemesByUserId(String userId, final getMemesCallBack getMemesCallback) {
        List<Meme> list = new ArrayList<>();

        DatabaseDataLoader.getDB().collection("memes")
                .whereEqualTo("userId", userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            list.add(document.toObject(Meme.class));
                        }
                        getMemesCallback.onCallback(list);
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                });
    }

    public void updateDocument(Meme meme, final UpdateMemeCallBack updateMemeCallBack) {

        DatabaseDataLoader.getDB().collection("memes").document(meme.getId())
                .update("description", meme.getDescription())
                .addOnSuccessListener(aVoid -> updateMemeCallBack.onCallback())
                .addOnFailureListener(e -> Log.w(TAG, "Error updating document", e));

    }
}
