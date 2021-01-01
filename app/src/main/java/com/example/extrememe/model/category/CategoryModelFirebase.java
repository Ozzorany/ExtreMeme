package com.example.extrememe.model.category;

import android.util.Log;

import com.example.extrememe.model.Category;
import com.example.extrememe.services.DatabaseDataLoader;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class CategoryModelFirebase {
    private static final String TAG = "CategoryService";

    public void getMemeCategories(final CategoryModel.GetMemeCategoriesListener listener) {
        List<Category> list = new ArrayList<>();

        DatabaseDataLoader.getDB().collection("categories")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            list.add(document.toObject(Category.class));
                        }

                        listener.onComplete(list);
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                });
    }
}
