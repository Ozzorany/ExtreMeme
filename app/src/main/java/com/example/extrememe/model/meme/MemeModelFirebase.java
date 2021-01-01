package com.example.extrememe.model.meme;

import android.util.Log;

import com.example.extrememe.model.Meme;
import com.example.extrememe.services.DatabaseDataLoader;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MemeModelFirebase {
    private static final String TAG = "MemesService";

    public void getAllMemes(final MemeModel.GetAllMemesListener listener) {
        List<Meme> list = new ArrayList<>();

        DatabaseDataLoader.getDB().collection("memes")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            list.add(document.toObject(Meme.class));
                        }
                        listener.onComplete(list);
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                });
    }

    public void getMemesByUserId(String userId, final MemeModel.GetMemesByUserListener listener) {
        List<Meme> list = new ArrayList<>();

        DatabaseDataLoader.getDB().collection("memes")
                .whereEqualTo("userId", userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            list.add(document.toObject(Meme.class));
                        }
                        listener.onComplete(list);
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                });
    }

    public void updateMeme(Meme meme, final MemeModel.UpdateMemeListener listener) {
        DatabaseDataLoader.getDB().collection("memes").document(meme.getId())
                .update("description", meme.getDescription())
                .addOnSuccessListener(listener::onComplete)
                .addOnFailureListener(e -> Log.w(TAG, "Error updating document", e));
    }
}