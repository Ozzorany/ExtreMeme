package com.example.extrememe.services;

import android.util.Log;

import com.example.extrememe.model.Category;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class CategoryService {
    private static final String TAG = "CategoryService";

    public CategoryService() {}

    public interface MyCallBack {
        void onCallback(List<Category> memes);
    }

    public void getMemeCategories(final MyCallBack myCallback) {
        List<Category> list = new ArrayList<>();

        DatabaseDataLoader.getDB().collection("categories")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            list.add(document.toObject(Category.class));
                        }

                        myCallback.onCallback(list);
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                });
    }
}