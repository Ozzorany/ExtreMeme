package com.example.extrememe.model.meme;

import android.util.Log;

import com.example.extrememe.model.Meme;
import com.example.extrememe.services.DatabaseDataLoader;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MemeModelFirebase {
    private static final String TAG = "MemesService";

    public void getAllMemes(Long lastUpdated, final MemeModel.GetAllMemesListener listener) {
        List<Meme> list = new ArrayList<>();

        DatabaseDataLoader.getDB().collection("memes")
                .whereGreaterThan("lastUpdated",new Date(lastUpdated))
                .orderBy("lastUpdated")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Meme meme = new Meme();
                            meme.fromMap(document.getData());
                            list.add(meme);
                        }
                        listener.onComplete(list);
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                });
    }

    public void getMemesByUserId(String userId, Long lastUpdated, final MemeModel.GetMemesByUserListener listener) {
        List<Meme> list = new ArrayList<>();

        DatabaseDataLoader.getDB().collection("memes")
                .whereEqualTo("userId", userId)
                .whereGreaterThan("lastUpdated",new Date(lastUpdated))
                .orderBy("lastUpdated")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Meme meme = new Meme();
                            meme.fromMap(document.getData());
                            list.add(meme);
                        }
                        listener.onComplete(list);
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                });
    }

    public void updateMeme(Meme meme, final MemeModel.UpdateMemeListener listener) {
        DatabaseDataLoader.getDB().collection("memes").document(meme.getId())
                .set(meme.toMap())
                .addOnSuccessListener(listener::onComplete)
                .addOnFailureListener(e -> Log.w(TAG, "Error inserting document", e));
    }


    public void insertMeme(Meme meme, final MemeModel.UpdateMemeListener listener) {
        DatabaseDataLoader.getDB().collection("memes").document(meme.getId())
                .set(meme.toMap())
                .addOnSuccessListener(listener::onComplete)
                .addOnFailureListener(e -> Log.w(TAG, "Error inserting document", e));
    }

    public void removeMeme(String memeId, MemeModel.RemoveMemeListener listener) {
        DatabaseDataLoader.getDB().collection("memes").document(memeId)
                .delete()
                .addOnSuccessListener(listener::onComplete)
                .addOnFailureListener(e -> Log.w(TAG, "Error removing document", e));
    }
}
