package com.example.extrememe.model.meme;

import android.util.Log;

import com.example.extrememe.model.Meme;
import com.example.extrememe.services.DatabaseDataLoader;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;

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
                .update("description", meme.getDescription(),
                        "usersLikes", meme.getUsersLikes())
                .addOnSuccessListener(listener::onComplete)
                .addOnFailureListener(e -> Log.w(TAG, "Error updating document", e));
    }

    public void insertMeme(Meme meme, final MemeModel.UpdateMemeListener listener) {
        DatabaseDataLoader.getDB().collection("memes").document(meme.getId())
                .set(meme, SetOptions.merge())
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
