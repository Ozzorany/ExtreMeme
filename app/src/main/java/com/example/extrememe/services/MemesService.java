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
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                list.add(document.toObject(Meme.class));
                            }
                            getMemesCallback.onCallback(list);
                        } else {
                            Log.w("warning", "Error getting documents.", task.getException());
                        }
                    }
                });
    }


    public void getMemesByUserId(String userId, final getMemesCallBack getMemesCallback) {
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
                            getMemesCallback.onCallback(list);
                        } else {
                            Log.w("warning", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    public void updateDocument(Meme meme, final UpdateMemeCallBack updateMemeCallBack) {

        DatabaseDataLoader.getDB().collection("memes").document(meme.getId())
                .update("description", meme.getDescription())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        updateMemeCallBack.onCallback();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("warning", "Error updating document", e);
                    }
                });

    }
}
