package com.example.extrememe.services;

import android.provider.ContactsContract;
import android.util.Log;

import com.example.extrememe.entities.Category;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.List;

public class CategoryService {
    public CategoryService() {}

    public void getMemeCategories() {
        DatabaseDataLoader.getDB().collection("categories")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d("info", document.getId() + " => " + document.getData());
                        }
                    } else {
                        Log.w("warning", "Error getting documents.", task.getException());
                    }
                });
    }
}
